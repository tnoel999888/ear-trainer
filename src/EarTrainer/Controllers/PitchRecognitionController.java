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
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import jm.gui.cpn.BassStave;
import jm.gui.cpn.Stave;
import jm.music.data.Phrase;
import jm.util.View;

import java.awt.*;
import java.io.*;


import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.swing.*;


public class PitchRecognitionController {

    public static final int TOTAL_QUESTIONS = 10;
    @FXML private StackPane stackPane;

    @FXML private ToggleGroup radioButtons;
    @FXML private HBox radioButtonsGroup;
    @FXML private RadioButton easyRadioButton;
    @FXML private RadioButton mediumRadioButton;
    @FXML private RadioButton hardRadioButton;

    @FXML private Button cButton;
    @FXML private Button cSharpButton;
    @FXML private Button dButton;
    @FXML private Button dSharpButton;
    @FXML private Button eButton;
    @FXML private Button fButton;
    @FXML private Button fSharpButton;
    @FXML private Button gButton;
    @FXML private Button gSharpButton;
    @FXML private Button aButton;
    @FXML private Button aSharpButton;
    @FXML private Button bButton;

    @FXML private Button correctButton = new Button();

    @FXML private Label timerLabel;
    @FXML private Label questionLabel;
    @FXML private Label difficultyDescriptionLabel;

    @FXML private Button startButton;
    @FXML private Button nextQuestionButton;
    @FXML private Button replayButton;

    @FXML private ImageView scoreImage;

    @FXML HBox mediaBar;
    MediaPlayer mediaPlayer;
    JMMusicCreator musicCreator;


    Sequencer sequencer;

    int questionNumber;
    int numberOfCorrectAnswers = 0;

    String correctAnswer = "c";
    private boolean questionAnswered;
    private Timeline timeline;
    private boolean startClicked = false;


    @FXML
    private void easyRadioButtonSelected(ActionEvent event) throws IOException {
        cSharpButton.setDisable(true);
        dSharpButton.setDisable(true);
        fSharpButton.setDisable(true);
        gSharpButton.setDisable(true);
        aSharpButton.setDisable(true);

        difficultyDescriptionLabel.setText("C, D, E, F, G, A and B. No Sharps or Flats. 1 Octave.");
    }

    @FXML
    private void mediumRadioButtonSelected(ActionEvent event) throws IOException {
        cSharpButton.setDisable(false);
        dSharpButton.setDisable(false);
        fSharpButton.setDisable(false);
        gSharpButton.setDisable(false);
        aSharpButton.setDisable(false);

        difficultyDescriptionLabel.setText("All notes. 1 Octave.");
    }

    @FXML
    private void hardRadioButtonSelected(ActionEvent event) throws IOException {
        cSharpButton.setDisable(false);
        dSharpButton.setDisable(false);
        fSharpButton.setDisable(false);
        gSharpButton.setDisable(false);
        aSharpButton.setDisable(false);

        difficultyDescriptionLabel.setText("All notes. 3 Octaves.");
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
        if (questionNumber != 10) {
            questionNumber++;
            questionLabel.setText("Question " + Integer.toString(questionNumber));
        } else {
            //nextQuestionButton.setText("Score");
            questionLabel.setVisible(false);
            nextQuestionButton.setDisable(true);
            stopTimer();
            loadScore();
        }

        questionAnswered = false;
        nextQuestionButton.setDisable(true);
        resetButtonColours();

//        mediaBar.getChildren().clear();
//        mediaPlayer.stop();

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
        cButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        cSharpButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        dButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        dSharpButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        eButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        fButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        fSharpButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        gButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        gSharpButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        aButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        aSharpButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        bButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
    }

    @FXML
    private void cButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("c", cButton);
        }
    }

    @FXML
    private void cSharpButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("c sharp", cSharpButton);
        }
    }

    @FXML
    private void dButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("d", dButton);
        }
    }

    @FXML
    private void dSharpButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("d sharp", dSharpButton);
        }
    }

    @FXML
    private void eButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("e", eButton);
        }
    }

    @FXML
    private void fButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("f", fButton);
        }
    }

    @FXML
    private void fSharpButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("f sharp", fSharpButton);
        }
    }

    @FXML
    private void gButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("g", gButton);
        }
    }

    @FXML
    private void gSharpButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("g sharp", gSharpButton);
        }
    }

    @FXML
    private void aButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("a", aButton);
        }
    }

    @FXML
    private void aSharpButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("a sharp", aSharpButton);
        }
    }

    @FXML
    private void bButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("b", bButton);
        }
    }

    @FXML
    private void AnswerButtonClicked() throws IOException {
        questionAnswered = true;
        nextQuestionButton.setDisable(false);

        Phrase phrase = musicCreator.getPhrase();
        View.notate(phrase, 700, 200);
//        Stave stave = new BassStave(phrase);
//        stave.setVisible(true);

//        final SwingNode swingNode = new SwingNode();
//        stackPane.getChildren().add(swingNode);
//        swingNode.setContent(new JButton("Click me!"));
//        swingNode.setContent(stave.getComponent(1));

//        stackPane.getChildren().add(swingNode);
    }

    private void checkAnswer(String answer, Button button) {
        if(answer != correctAnswer){
            makeButtonRed(button);
        } else {
            numberOfCorrectAnswers++;
        }

        makeButtonGreen(correctButton);

        if(questionNumber == 10){

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
                                String strSecs = Integer.toString(secs % 60);
                                String strMins = Integer.toString(secs/60);

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
//        gen = false;
        timerLabel.setVisible(false);
        timeline.stop();
    }

    private Button getCorrectButton(String correctAnswer) {
        switch(correctAnswer){
            case "c": return cButton;
            case "c sharp": return cSharpButton;
            case "d": return dButton;
            case "d sharp": return dSharpButton;
            case "e": return eButton;
            case "f": return fButton;
            case "f sharp": return fSharpButton;
            case "g": return gButton;
            case "g sharp": return gSharpButton;
            case "a": return aButton;
            case "a sharp": return aSharpButton;
            case "b": return bButton;
            default: return cButton;
        }
    }


    @FXML
    private void generateQuestion() throws IOException, MidiUnavailableException, InvalidMidiDataException {
        Stage stage = (Stage) stackPane.getScene().getWindow();

        musicCreator = new JMMusicCreator();

        if(easyRadioButton.isSelected()){
            correctAnswer = musicCreator.makeMIDIEasyPitch();
        } else if(mediumRadioButton.isSelected()){
            correctAnswer = musicCreator.makeMIDIMediumPitch();
        } else if(hardRadioButton.isSelected()){
            correctAnswer = musicCreator.makeMIDIHardPitch();
        }

        correctButton = getCorrectButton(correctAnswer);

        playSound();

//        Media media = new Media(new File(MEDIA_URL).toURI().toString());
//        mediaPlayer = new MediaPlayer(media);
//        mediaPlayer.setAutoPlay(true);

//        MediaControl mediaControl = new MediaControl(mediaPlayer, this, stackPane);
    }


    @FXML
    private void replayButtonClicked(ActionEvent event) throws IOException, InvalidMidiDataException, MidiUnavailableException {
        playSound();
    }

    private void playSound() throws MidiUnavailableException, IOException, InvalidMidiDataException {
        final String MEDIA_URL = "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Pitch.mid";

        sequencer = MidiSystem.getSequencer();
        sequencer.open();
        InputStream is = new BufferedInputStream(new FileInputStream(new File(MEDIA_URL)));
        sequencer.setSequence(is);
        sequencer.start();

//        JMSL.setViewFactory(new ViewFactorySwing());
//        ScoreWithoutScoreFrame test = new ScoreWithoutScoreFrame();
//        test.buildScore();
//        test.getScore().getScoreCanvas().getComponent();
//        stackPane.getChildren().add(test.getScore().getScoreCanvas().getComponent());
    }


}




