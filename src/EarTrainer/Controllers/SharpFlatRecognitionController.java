package EarTrainer.Controllers;

import com.sun.media.sound.SimpleInstrument;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import jm.gui.cpn.JGrandStave;
import jm.music.data.Phrase;

import java.awt.*;
import java.io.*;


import javax.sound.midi.*;


public class SharpFlatRecognitionController {

    public static final int TOTAL_QUESTIONS = 10;
    @FXML private StackPane stackPane;

    @FXML private HBox radioButtonsGroup;
    @FXML private RadioButton easyRadioButton;
    @FXML private RadioButton mediumRadioButton;
    @FXML private RadioButton hardRadioButton;

    @FXML private Button flatButton;
    @FXML private Button sharpButton;


    @FXML private Button correctButton = new Button();

    @FXML private Label timerLabel;
    @FXML private Label questionLabel;
    @FXML private Label difficultyDescriptionLabel;

    @FXML private Button startButton;
    @FXML private Button nextQuestionButton;

    @FXML private Pane scorePane;

    @FXML HBox mediaBar;

    private JGrandStave jScore = new JGrandStave();
    private Phrase phrase = new Phrase();

    //MediaPlayer mediaPlayer;
    private JMMusicCreator musicCreator;
    private String strSecs;
    private String strMins;

    private int questionNumber;
    private int numberOfCorrectAnswers = 0;

    private String correctAnswer = "flat";
    private boolean questionAnswered;
    private Timeline timeline;
    private boolean startClicked = false;

    private Sequencer sequencer;


    @FXML
    public void initialize() {
        Dimension d = new Dimension();
        d.setSize(600,300);
        jScore.setPreferredSize(d);
        jScore.setMaximumSize(d);

        jScore.removeTitle();
        jScore.setEditable(false);

        SwingNode swingNode = new SwingNode();
        swingNode.setContent(jScore);

        scorePane.getChildren().add(swingNode);
    }


    @FXML
    private void easyRadioButtonSelected(ActionEvent event) throws IOException {
        difficultyDescriptionLabel.setText("Middle C only. +/- 10-25 Cents");
    }


    @FXML
    private void mediumRadioButtonSelected(ActionEvent event) throws IOException {
        difficultyDescriptionLabel.setText("White notes only. 1 Octave. +/- 5-10 Cents");
    }


    @FXML
    private void hardRadioButtonSelected(ActionEvent event) throws IOException {
        difficultyDescriptionLabel.setText("All notes. 3 Octaves. +/- 1-5 Cents");
    }


    @FXML
    private void BackButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) stackPane.getScene().getWindow();
        stage.hide();
    }


    @FXML
    private void StartButtonClicked(ActionEvent event) throws IOException, InvalidMidiDataException, MidiUnavailableException {
        startClicked = true;
        questionNumber = 1;
        numberOfCorrectAnswers = 0;
        startTimer();
        questionLabel.setVisible(true);
        startButton.setDisable(true);
        timerLabel.setVisible(true);
        radioButtonsGroup.setDisable(true);
        questionLabel.setText("Question 1");

        generateQuestion();
    }


    @FXML
    private void NextQuestionButtonClicked(ActionEvent event) throws IOException, InvalidMidiDataException, MidiUnavailableException {
        sequencer.stop();
        sequencer.close();

        if (questionNumber != TOTAL_QUESTIONS) {
            questionNumber++;
            questionLabel.setText("Question " + Integer.toString(questionNumber));
        } else {
            nextQuestionButton.setText("Next Question");
            questionLabel.setVisible(false);
            nextQuestionButton.setDisable(true);
            startClicked = false;
            stopTimer();
            loadScore();
        }

        questionAnswered = false;
        nextQuestionButton.setDisable(true);
        resetButtonColours();

        //setScore(phrase);
        generateQuestion();
    }


    private void loadScore() {
        ColorAdjust adj = new ColorAdjust(0, 0, -0.2, 0);
        GaussianBlur blur = new GaussianBlur(10);
        adj.setInput(blur);
        stackPane.setEffect(adj);
        stackPane.setDisable(true);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/PopupScore.fxml"));
        Parent root = null;
        try {
            root = (Parent)loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        startButton.setDisable(false);
        radioButtonsGroup.setDisable(false);

        PopupScoreController controller = loader.<PopupScoreController>getController();
        controller.setNumberOfCorrectAnswers(numberOfCorrectAnswers);
        controller.setTime(strMins, strSecs);
        controller.setStackPane(stackPane);

        if(numberOfCorrectAnswers >= 0 && numberOfCorrectAnswers <= 3){
            controller.setImageToUse("../Images/ScoreRed.png");
        } else if(numberOfCorrectAnswers > 3 && numberOfCorrectAnswers <= 6) {
            controller.setImageToUse("../Images/ScoreAmber.png");
        } else {
            controller.setImageToUse("../Images/ScoreGreen.png");
        }

        Stage newStage = new Stage();
        newStage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.show();
    }


    private void resetButtonColours() {
        flatButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        sharpButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
    }


    @FXML
    private void flatButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("unison", flatButton);
        }
    }


    @FXML
    private void sharpButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("minor second", sharpButton);
        }
    }


    @FXML
    private void AnswerButtonClicked() throws IOException {
        questionAnswered = true;
        nextQuestionButton.setDisable(false);

        Phrase phrase = musicCreator.getPhrase();
        //setScore(phrase);
    }


    private void checkAnswer(String answer, Button button) {
        if(answer != correctAnswer){
            makeButtonRed(button);
        } else {
            numberOfCorrectAnswers++;
        }

        makeButtonGreen(correctButton);

        if(questionNumber == 10){
            nextQuestionButton.setText("Score");
        }
    }


    private void makeButtonRed(Button button) {
        button.setStyle("-fx-base: #ffb3b3;");
    }


    private void makeButtonGreen(Button correctButton) {
        correctButton.setStyle("-fx-base: #adebad;");
    }


    private void startTimer() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        new EventHandler<ActionEvent>() {
                            int secs = 0;
                            @Override public void handle(ActionEvent actionEvent) {
                                secs++;
                                strSecs = Integer.toString(secs % 60);
                                strMins = Integer.toString(secs/60);

                                if (strSecs.length() == 1){
                                    strSecs = "0" + strSecs;
                                }

                                if (strMins.length() == 1){
                                    strMins = "0" + strMins;
                                }

                                timerLabel.setText(strMins + ":" + strSecs);
                            }
                        }
                ),
                new KeyFrame(Duration.seconds(1))
        );

        this.timeline = timeline;
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }


    private void stopTimer() {
        timerLabel.setVisible(false);
        timeline.stop();
    }


    private Button getCorrectButton(String correctAnswer) {
        switch(correctAnswer){
            case "flat":
                return flatButton;
            case "sharp":
                return sharpButton;
            default:
                return flatButton;
        }
    }


    @FXML
    private void generateQuestion() throws IOException, MidiUnavailableException, InvalidMidiDataException {
        musicCreator = new JMMusicCreator(jScore);

        if(easyRadioButton.isSelected()){
            correctAnswer = musicCreator.makeMIDIEasySharpFlat();
        } else if(mediumRadioButton.isSelected()){
            correctAnswer = musicCreator.makeMIDIMediumSharpFlat();
        } else if(hardRadioButton.isSelected()){
            correctAnswer = musicCreator.makeMIDIHardSharpFlat();
        }

        correctButton = getCorrectButton(correctAnswer);

        playSound();
    }


    @FXML
    private void replayButtonClicked(ActionEvent event) throws IOException, InvalidMidiDataException, MidiUnavailableException {
        playSound();
    }


    private void playSound() throws MidiUnavailableException, IOException, InvalidMidiDataException {
        final String MEDIA_URL = "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/SharpFlat.mid";

        sequencer = MidiSystem.getSequencer();
        sequencer.open();
        InputStream is = new BufferedInputStream(new FileInputStream(new File(MEDIA_URL)));


        ShortMessage myMsg = new ShortMessage();
        myMsg.setMessage(ShortMessage.PITCH_BEND, 3, 2, 100);
        MidiEvent pitchBend = new MidiEvent(myMsg, 1);
        
        Sequence sequence = MidiSystem.getSequence(is);
        Track track = sequence.createTrack();
        track.add(pitchBend);


        sequencer.setSequence(sequence);
        sequencer.start();
    }


//    public void setScore(Phrase phr) {
//        jScore.setPhrase(phr);
//
//        Dimension d = new Dimension();
//        d.setSize(600,300);
//        jScore.setPreferredSize(d);
//        jScore.setMaximumSize(d);
//
//        jScore.removeTitle();
//        jScore.setEditable(false);
//    }
}




