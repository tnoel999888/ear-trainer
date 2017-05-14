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
import javafx.scene.control.*;
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
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Write;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import java.awt.*;
import java.io.*;
import javafx.scene.paint.Color;


public class WrongNoteIdentificationController {

    public static final int TOTAL_QUESTIONS = 10;
    @FXML private StackPane stackPane;

    @FXML private HBox radioButtonsGroup;
    @FXML private RadioButton easyRadioButton;
    @FXML private RadioButton mediumRadioButton;
    @FXML private RadioButton hardRadioButton;

    @FXML private Button submitButton;
    @FXML private Button playChangedButton;

    @FXML private Label timerLabel;
    @FXML private Label questionLabel;
    @FXML private Label difficultyDescriptionLabel;
    @FXML private Label correctIncorrectText;

    @FXML private Button startButton;
    @FXML private Button nextQuestionButton;

    @FXML private Pane scorePane;


    private JGrandStave jScore = new JGrandStave();
    private Phrase phrase = new Phrase();

    private JMMusicCreator musicCreator;
    private String strSecs;
    private String strMins;

    private int questionNumber;
    private int numberOfCorrectAnswers = 0;

    private int indexOfChangedNote = 0;

    private boolean questionAnswered;
    private Timeline timeline;
    private boolean startClicked = false;

    private String filePath = "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/WrongNote.mid";

    private Sequencer sequencer;



    @FXML
    public void initialize() {
        Dimension d = new Dimension();
        d.setSize(600,300);
        jScore.setPreferredSize(d);
        jScore.setMaximumSize(d);

        jScore.removeTitle();
        jScore.setEditable(true);

        SwingNode swingNode = new SwingNode();
        swingNode.setContent(jScore);

        scorePane.getChildren().add(swingNode);
    }


    @FXML
    private void easyRadioButtonSelected(ActionEvent event) throws IOException {
        difficultyDescriptionLabel.setText("Identify the note which is out of key and drag it into a position which is in key.");
    }


    @FXML
    private void mediumRadioButtonSelected(ActionEvent event) throws IOException {
        difficultyDescriptionLabel.setText("Identify the note on the stave that does not correspond to the respective note that was played. Drag the note to match the note that was played. +/- 3 semitones.");
    }


    @FXML
    private void hardRadioButtonSelected(ActionEvent event) throws IOException {
        difficultyDescriptionLabel.setText("Identify the note on the stave that does not correspond to the respective note that was played. Drag the note to match the note that was played. +/- 1 semitone.");
        playChangedButton.setDisable(true);
    }


    @FXML
    private void BackButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) stackPane.getScene().getWindow();
        stage.hide();
    }


    @FXML
    private void AnswerButtonClicked() throws IOException {
        questionAnswered = true;
        nextQuestionButton.setDisable(false);

        phrase = musicCreator.getOriginalPhrase();
        setScore(phrase);
    }


    @FXML
    private void SubmitButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer(musicCreator.getTheirMelodyAnswer(), musicCreator.getOriginalPhrase().getNoteArray());
        }
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
        correctIncorrectText.setText("");

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
        submitButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
    }


    private boolean contains(int[] scale, int note){
        for(int n : scale){
            if(n == note){
                return true;
            }
        }

        return false;
    }


    private void checkAnswer(Note[] theirMelodyAnswer, Note[] correctMelody) {
        for(int j = 0; j < theirMelodyAnswer.length; j++){
            System.out.println(theirMelodyAnswer[j].getPitch());
            System.out.println(correctMelody[j].getPitch());
            System.out.println("");
        }

        if(easyRadioButton.isSelected()) {
            for (int i = 0; i < theirMelodyAnswer.length; i++) {
                if (i != indexOfChangedNote) {
                    if (theirMelodyAnswer[i].getPitch() != correctMelody[i].getPitch()) {
                        makeButtonRed(submitButton);
                        correctIncorrectText.setTextFill(Color.web("#da4343"));
                        correctIncorrectText.setText("Incorrect. The wrong note was at position " + indexOfChangedNote + 1 + ". Here's an example of a correct answer.");
                        break;
                    }
                } else {
                    if (contains(musicCreator.getScaleNotes(), theirMelodyAnswer[i].getPitch())) {
                        makeButtonGreen(submitButton);
                        numberOfCorrectAnswers++;
                        correctIncorrectText.setTextFill(Color.web("#3abf4c"));
                        correctIncorrectText.setText("Correct!");
                    } else {
                        makeButtonRed(submitButton);
                        correctIncorrectText.setTextFill(Color.web("#da4343"));
                        correctIncorrectText.setText("Incorrect. You identified the correct note, but you did not put it in key! Here's an example of a correct answer.");
                        break;
                    }
                }
            }
        } else if((mediumRadioButton.isSelected()) || (hardRadioButton.isSelected())) {
            for (int i = 0; i < theirMelodyAnswer.length; i++) {
                if (i != indexOfChangedNote) {
                    if (theirMelodyAnswer[i].getPitch() != correctMelody[i].getPitch()) {
                        makeButtonRed(submitButton);
                        correctIncorrectText.setTextFill(Color.web("#da4343"));
                        correctIncorrectText.setText("Incorrect. The wrong note was at position " + indexOfChangedNote + 1 + ". Here's an example of a correct answer.");
                        break;
                    }
                } else {
                    if (theirMelodyAnswer[i].getPitch() == correctMelody[i].getPitch()) {
                        makeButtonGreen(submitButton);
                        numberOfCorrectAnswers++;
                        correctIncorrectText.setTextFill(Color.web("#3abf4c"));
                        correctIncorrectText.setText("Correct!");
                    } else {
                        makeButtonRed(submitButton);
                        correctIncorrectText.setTextFill(Color.web("#da4343"));
                        correctIncorrectText.setText("Incorrect. You identified the correct note, but you did not move it to the correct position.");
                        break;
                    }
                }
            }
        }

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


    @FXML
    private void generateQuestion() throws IOException, MidiUnavailableException, InvalidMidiDataException {
        musicCreator = new JMMusicCreator(jScore);
        phrase = musicCreator.getOriginalPhrase();

        if(easyRadioButton.isSelected()){
            indexOfChangedNote = musicCreator.makeMIDIEasyWrongNote();
        } else if(mediumRadioButton.isSelected()){
            indexOfChangedNote = musicCreator.makeMIDIMediumWrongNote();
        } else if(hardRadioButton.isSelected()){
            indexOfChangedNote = musicCreator.makeMIDIHardWrongNote();
        }

        playSound(filePath);
    }


    @FXML
    private void replayButtonClicked(ActionEvent event) throws IOException, InvalidMidiDataException, MidiUnavailableException {
        sequencer.stop();
        sequencer.close();

        playSound(filePath);
    }


    @FXML
    private void playChangedButtonClicked(ActionEvent event) throws IOException, InvalidMidiDataException, MidiUnavailableException {
        phrase = jScore.getPhrase();

        Part p = new Part();
        p.add(phrase);

        Score s = new Score();
        s.addPart(p);

        String newFilePath = "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/WrongNoteNew.mid";

        Write.midi(s, newFilePath);

        playSound(newFilePath);
    }


    private void playSound(String filePath) throws MidiUnavailableException, IOException, InvalidMidiDataException {
        sequencer = MidiSystem.getSequencer();
        sequencer.open();
        InputStream is = new BufferedInputStream(new FileInputStream(new File(filePath)));
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
        jScore.setEditable(true);
    }
}




