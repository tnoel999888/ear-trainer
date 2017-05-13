package EarTrainer.Controllers;

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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import jm.gui.cpn.JGrandStave;
import jm.music.data.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import java.awt.*;
import java.io.*;


public class ModulationRecognitionController {

    public static final int TOTAL_QUESTIONS = 10;
    @FXML private StackPane stackPane;

    @FXML private HBox radioButtonsGroup;
    @FXML private RadioButton easyRadioButton;
    @FXML private RadioButton mediumRadioButton;
    @FXML private RadioButton hardRadioButton;

    @FXML private Button similarKey0Button;
    @FXML private Button similarKey1Button;
    @FXML private Button similarKey2Button;
    @FXML private Button similarKey3Button;
    @FXML private Button similarKey4Button;

    @FXML private Button correctButton;

    @FXML private Label timerLabel;
    @FXML private Label questionLabel;
    @FXML private Label rootKeyLabel;
    @FXML private Label difficultyDescriptionLabel;

    @FXML private Button startButton;
    @FXML private Button nextQuestionButton;

    @FXML private Pane scorePane;
    @FXML private Pane scorePaneLeft;
    @FXML private Pane scorePaneRight;

    @FXML HBox mediaBar;

    private JGrandStave jScore = new JGrandStave();
    private JGrandStave jScoreLeft = new JGrandStave();
    private JGrandStave jScoreRight = new JGrandStave();

    private Phrase phrase = new Phrase();
    private Phrase phraseLeft = new Phrase();
    private Phrase phraseRight = new Phrase();

    //MediaPlayer mediaPlayer;
    private JMMusicCreator musicCreator;
    private String strSecs;
    private String strMins;

    private int questionNumber;
    private int numberOfCorrectAnswers = 0;

    private String correctAnswer = "a";
    private boolean questionAnswered;
    private Timeline timeline;
    private boolean startClicked = false;

    private int[] similarKeys = new int[5];

    private Sequencer sequencer;


    @FXML
    public void initialize() {
        Dimension d = new Dimension();
        d.setSize(500,300);

        jScore.setPreferredSize(d);
        jScore.setMaximumSize(d);
        jScore.removeTitle();
        jScore.setEditable(false);

        jScoreLeft.setPreferredSize(d);
        jScoreLeft.setMaximumSize(d);
        jScoreLeft.removeTitle();
        jScoreLeft.setEditable(false);

        jScoreRight.setPreferredSize(d);
        jScoreRight.setMaximumSize(d);
        jScoreRight.removeTitle();
        jScoreRight.setEditable(false);

        SwingNode swingNode = new SwingNode();
        SwingNode swingNodeLeft = new SwingNode();
        SwingNode swingNodeRight = new SwingNode();

        swingNode.setContent(jScore);
        swingNodeLeft.setContent(jScoreLeft);
        swingNodeRight.setContent(jScoreRight);

        scorePane.getChildren().add(swingNode);
        scorePaneLeft.getChildren().add(swingNodeLeft);
        scorePaneRight.getChildren().add(swingNodeRight);
    }


    @FXML
    private void easyRadioButtonSelected(ActionEvent event) throws IOException {
        difficultyDescriptionLabel.setText("Identify the final key.");
    }


    @FXML
    private void mediumRadioButtonSelected(ActionEvent event) throws IOException {
        difficultyDescriptionLabel.setText("");
    }


    @FXML
    private void hardRadioButtonSelected(ActionEvent event) throws IOException {
        difficultyDescriptionLabel.setText("Identify both the starting key and the final key.");
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
            rootKeyLabel.setVisible(false);
            nextQuestionButton.setDisable(true);
            stopTimer();
            loadScore();
        }

        questionAnswered = false;
        nextQuestionButton.setDisable(true);
        resetButtonColours();

//        setScore(phrase);
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
        similarKey0Button.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        similarKey1Button.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        similarKey2Button.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        similarKey3Button.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        similarKey4Button.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
    }


    @FXML
    private void similarKey0ButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer(similarKey0Button.getText(), similarKey0Button);
        }
    }


    @FXML
    private void similarKey1ButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer(similarKey1Button.getText(), similarKey1Button);
        }
    }


    @FXML
    private void similarKey2ButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer(similarKey2Button.getText(), similarKey2Button);
        }
    }


    @FXML
    private void similarKey3ButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer(similarKey3Button.getText(), similarKey3Button);
        }
    }


    @FXML
    private void similarKey4ButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer(similarKey4Button.getText(), similarKey4Button);
        }
    }


    @FXML
    private void AnswerButtonClicked() throws IOException {
        questionAnswered = true;
        nextQuestionButton.setDisable(false);

        Phrase phrase = musicCreator.getPhrase();
//        setScore(phrase);
    }


    private void checkAnswer(String answer, Button button) {
        if(!answer.equals(correctAnswer)){
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
        Button[] buttons = {similarKey0Button, similarKey1Button, similarKey2Button, similarKey3Button, similarKey4Button};

        for(Button b : buttons){
            if(b.getText().equals(correctAnswer)){
                return b;
            }
        }

        return similarKey0Button;
    }


    @FXML
    private void generateQuestion() throws IOException, MidiUnavailableException, InvalidMidiDataException {
        musicCreator = new JMMusicCreator(jScore, jScoreLeft, jScoreRight);

        if(easyRadioButton.isSelected()){
            correctAnswer = musicCreator.makeMIDIEasyModulation();
        } else if(mediumRadioButton.isSelected()){
            correctAnswer = musicCreator.makeMIDIMediumModulation();
        } else if(hardRadioButton.isSelected()){
            correctAnswer = musicCreator.makeMIDIHardModulation();
        }

        rootKeyLabel.setVisible(true);
        rootKeyLabel.setText("Root key: " + musicCreator.getRootKeyString());

        System.out.println("Old key: " + musicCreator.getRootKeyString());
        System.out.println("New key: " + correctAnswer);

        similarKeys = musicCreator.getSimilarKeys();

        String similarKey0Text;
        String similarKey1Text;
        String similarKey2Text;
        String similarKey3Text;
        String similarKey4Text;

        if(musicCreator.getMinorOrMajor().equals("Major")) {
            similarKey0Text = musicCreator.getNote(new Note(similarKeys[0], 1.0)) + "m";
            similarKey1Text = musicCreator.getNote(new Note(similarKeys[1], 1.0));
            similarKey2Text = musicCreator.getNote(new Note(similarKeys[2], 1.0)) + "m";
            similarKey3Text = musicCreator.getNote(new Note(similarKeys[3], 1.0));
            similarKey4Text = musicCreator.getNote(new Note(similarKeys[4], 1.0)) + "m";
        } else {
            similarKey0Text = musicCreator.getNote(new Note(similarKeys[0], 1.0));
            similarKey1Text = musicCreator.getNote(new Note(similarKeys[1], 1.0)) + "m";
            similarKey2Text = musicCreator.getNote(new Note(similarKeys[2], 1.0));
            similarKey3Text = musicCreator.getNote(new Note(similarKeys[3], 1.0)) + "m";
            similarKey4Text = musicCreator.getNote(new Note(similarKeys[4], 1.0));
        }

        similarKey0Button.setText(similarKey0Text);
        similarKey1Button.setText(similarKey1Text);
        similarKey2Button.setText(similarKey2Text);
        similarKey3Button.setText(similarKey3Text);
        similarKey4Button.setText(similarKey4Text);

        correctButton = getCorrectButton(correctAnswer);

        playSound();
    }


    @FXML
    private void replayButtonClicked(ActionEvent event) throws IOException, InvalidMidiDataException, MidiUnavailableException {
        playSound();
    }


    private void playSound() throws MidiUnavailableException, IOException, InvalidMidiDataException {
        final String MEDIA_URL = "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Modulation.mid";

        sequencer = MidiSystem.getSequencer();
        sequencer.open();
        InputStream is = new BufferedInputStream(new FileInputStream(new File(MEDIA_URL)));
        sequencer.setSequence(is);
        sequencer.start();
    }


    public void setScore(Phrase phr, String score) {
        JGrandStave scoreToUse;

        if(score.equals("left")){
            scoreToUse = jScoreLeft;
        } else if(score.equals("middle")){
            scoreToUse = jScore;
        } else {
            scoreToUse = jScoreRight;
        }

        scoreToUse.setPhrase(phr);

        Dimension d = new Dimension();
        d.setSize(600,300);
        scoreToUse.setPreferredSize(d);
        scoreToUse.setMaximumSize(d);

        scoreToUse.removeTitle();
        scoreToUse.setEditable(false);
    }
}




