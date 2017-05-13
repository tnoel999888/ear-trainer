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
import javafx.scene.control.ToggleGroup;
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

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import java.awt.*;
import java.io.*;


public class HarmonicIntervalRecognitionController {

    public static final int TOTAL_QUESTIONS = 10;
    @FXML private StackPane stackPane;

    @FXML private HBox radioButtonsGroup;
    @FXML private RadioButton easyRadioButton;
    @FXML private RadioButton mediumRadioButton;
    @FXML private RadioButton hardRadioButton;

    @FXML private Button unisonButton;
    @FXML private Button minorSecondButton;
    @FXML private Button majorSecondButton;
    @FXML private Button perfectFourthButton;
    @FXML private Button tritoneButton;
    @FXML private Button minorThirdButton;
    @FXML private Button majorThirdButton;
    @FXML private Button perfectFifthButton;
    @FXML private Button octaveButton;
    @FXML private Button minorSixthButton;
    @FXML private Button majorSixthButton;
    @FXML private Button minorSeventhButton;
    @FXML private Button majorSeventhButton;

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

    private String correctAnswer = "unison";
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
        minorSecondButton.setDisable(true);
        majorSecondButton.setDisable(true);
        perfectFourthButton.setDisable(true);
        tritoneButton.setDisable(true);
        minorSixthButton.setDisable(true);
        majorSixthButton.setDisable(true);
        minorSeventhButton.setDisable(true);
        majorSeventhButton.setDisable(true);

        difficultyDescriptionLabel.setText("Middle C as root note. Only Unisons, Thirds, Perfect Fifths and Octaves. Only 1 Octave.");
    }


    @FXML
    private void mediumRadioButtonSelected(ActionEvent event) throws IOException {
        minorSecondButton.setDisable(false);
        majorSecondButton.setDisable(false);
        perfectFourthButton.setDisable(true);
        tritoneButton.setDisable(true);
        octaveButton.setDisable(false);
        minorSixthButton.setDisable(true);
        majorSixthButton.setDisable(true);
        minorSeventhButton.setDisable(true);
        majorSeventhButton.setDisable(true);

        difficultyDescriptionLabel.setText("Middle C, D, E, F, G, A, B as root notes. Only Unisons, Seconds, Thirds, Perfect Fifths and Octaves. Only 1 Octave.");
    }


    @FXML
    private void hardRadioButtonSelected(ActionEvent event) throws IOException {
        minorSecondButton.setDisable(false);
        majorSecondButton.setDisable(false);
        perfectFourthButton.setDisable(false);
        tritoneButton.setDisable(false);
        octaveButton.setDisable(false);
        minorSixthButton.setDisable(false);
        majorSixthButton.setDisable(false);
        minorSeventhButton.setDisable(false);
        majorSeventhButton.setDisable(false);

        difficultyDescriptionLabel.setText("All notes as root notes. All intervals. 3 Octaves.");
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

        setScore(phrase);
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
        unisonButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        minorSecondButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        majorSecondButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        perfectFourthButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        tritoneButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        minorThirdButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        majorThirdButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        perfectFifthButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        octaveButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        minorSixthButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        majorSixthButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        minorSeventhButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        majorSeventhButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
    }


    @FXML
    private void unisonButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("unison", unisonButton);
        }
    }


    @FXML
    private void minorSecondButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("minor second", minorSecondButton);
        }
    }


    @FXML
    private void majorSecondButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("major second", majorSecondButton);
        }
    }


    @FXML
    private void perfectFourthButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("perfect fourth", perfectFourthButton);
        }
    }


    @FXML
    private void tritoneButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("tritone", tritoneButton);
        }
    }


    @FXML
    private void minorThirdButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("minor third", minorThirdButton);
        }
    }


    @FXML
    private void majorThirdButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("major third", majorThirdButton);
        }
    }


    @FXML
    private void perfectFifthButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("perfect fifth", perfectFifthButton);
        }
    }


    @FXML
    private void octaveButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("octave", octaveButton);
        }
    }


    @FXML
    private void minorSixthButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("minor sixth", minorSixthButton);
        }
    }


    @FXML
    private void majorSixthButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("major sixth", majorSixthButton);
        }
    }


    @FXML
    private void minorSeventhButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("minor seventh", minorSeventhButton);
        }
    }


    @FXML
    private void majorSeventhButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("major seventh", majorSeventhButton);
        }
    }


    @FXML
    private void AnswerButtonClicked() throws IOException {
        questionAnswered = true;
        nextQuestionButton.setDisable(false);

        Phrase phrase = musicCreator.getPhrase();
        setScore(phrase);
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
            case "unison":
                return unisonButton;
            case "minor second":
                return minorSecondButton;
            case "major second":
                return majorSecondButton;
            case "minor third":
                return minorThirdButton;
            case "major third":
                return majorThirdButton;
            case "perfect fourth":
                return perfectFourthButton;
            case "tritone":
                return tritoneButton;
            case "perfect fifth":
                return perfectFifthButton;
            case "minor sixth":
                return minorSixthButton;
            case "major sixth":
                return majorSixthButton;
            case "minor seventh":
                return minorSeventhButton;
            case "major seventh":
                return majorSeventhButton;
            case "octave":
                return octaveButton;
            default:
                return unisonButton;
        }
    }


    @FXML
    private void generateQuestion() throws IOException, MidiUnavailableException, InvalidMidiDataException {
        musicCreator = new JMMusicCreator(jScore);

        if(easyRadioButton.isSelected()){
            correctAnswer = musicCreator.makeMIDIEasyHarmonic();
        } else if(mediumRadioButton.isSelected()){
            correctAnswer = musicCreator.makeMIDIMediumHarmonic();
        } else if(hardRadioButton.isSelected()){
            correctAnswer = musicCreator.makeMIDIHardHarmonic();
        }

        correctButton = getCorrectButton(correctAnswer);

        playSound();
    }


    @FXML
    private void replayButtonClicked(ActionEvent event) throws IOException, InvalidMidiDataException, MidiUnavailableException {
        playSound();
    }


    private void playSound() throws MidiUnavailableException, IOException, InvalidMidiDataException {
        final String MEDIA_URL = "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/HarmonicInterval.mid";

        sequencer = MidiSystem.getSequencer();
        sequencer.open();
        InputStream is = new BufferedInputStream(new FileInputStream(new File(MEDIA_URL)));
        sequencer.setSequence(is);
        sequencer.start();
    }


    public void setScore(Phrase phr) {
        jScore.setPhrase(phr);

        Dimension d = new Dimension();
        d.setSize(600,300);
        jScore.setPreferredSize(d);
        jScore.setMaximumSize(d);

        jScore.removeTitle();
        jScore.setEditable(false);
    }
}




