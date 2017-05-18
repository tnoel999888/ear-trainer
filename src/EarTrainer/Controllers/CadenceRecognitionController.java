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
import jm.gui.cpn.JStaveActionHandler;
import jm.gui.cpn.PianoStave;
import jm.gui.cpn.Stave;
import jm.music.data.Phrase;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Vector;


public class CadenceRecognitionController extends AbstractController{

    @FXML private Button perfectButton;
    @FXML private Button interruptiveButton;
    @FXML private Button imperfectButton;
    @FXML private Button plagalButton;


//    @FXML private Pane scorePane;
    @FXML private Pane scorePaneLeft;
    @FXML private Pane scorePaneRight;


//    private JGrandStave jScore = new JGrandStave();
    private JGrandStave jScoreLeft = new JGrandStave();
    private JGrandStave jScoreRight = new JGrandStave();



    @Override
    @FXML
    public void initialize() {
        Dimension d = new Dimension();
        d.setSize(475,300);

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
        imperfectButton.setDisable(true);
        plagalButton.setDisable(true);

        difficultyDescriptionLabel.setText("Only Perfect/Authentic and Interruptive/Deceptive Cadences.");
    }


    @FXML
    private void mediumRadioButtonSelected(ActionEvent event) throws IOException {
        imperfectButton.setDisable(false);
        plagalButton.setDisable(true);

        difficultyDescriptionLabel.setText("Perfect/Authentic, Interruptive/Deceptive and Imperfect/Half Cadences.");
    }


    @FXML
    private void hardRadioButtonSelected(ActionEvent event) throws IOException {
        imperfectButton.setDisable(false);
        plagalButton.setDisable(false);

        difficultyDescriptionLabel.setText("All 4 Cadences.");
    }


    protected void resetButtonColours() {
        perfectButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        interruptiveButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        imperfectButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        plagalButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
    }


    @FXML
    private void perfectButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("perfect", perfectButton);
        }
    }


    @FXML
    private void interruptiveButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("interruptive", interruptiveButton);
        }
    }


    @FXML
    private void imperfectButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("imperfect", imperfectButton);
        }
    }


    @FXML
    private void plagalButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("plagal", plagalButton);
        }
    }


    private Button getCorrectButton(String correctAnswer) {
        switch(correctAnswer){
            case "perfect":
                return perfectButton;
            case "interruptive":
                return interruptiveButton;
            case "imperfect":
                return imperfectButton;
            case "plagal":
                return plagalButton;
            default:
                return perfectButton;
        }
    }


    @FXML
    protected void generateQuestion() throws IOException, MidiUnavailableException, InvalidMidiDataException {
        musicCreator = new JMMusicCreator(jScore, jScoreLeft, jScoreRight);

        if(easyRadioButton.isSelected()){
            correctAnswer = musicCreator.makeMIDIEasyCadence();
        } else if(mediumRadioButton.isSelected()){
            correctAnswer = musicCreator.makeMIDIMediumCadence();
        } else if(hardRadioButton.isSelected()){
            correctAnswer = musicCreator.makeMIDIHardCadence();
        }

        correctButton = getCorrectButton(correctAnswer);

        playSound();
    }



    protected void playSound() throws MidiUnavailableException, IOException, InvalidMidiDataException {
        final String MEDIA_URL = "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Cadence.mid";

        sequencer = MidiSystem.getSequencer();
        sequencer.open();
        InputStream is = new BufferedInputStream(new FileInputStream(new File(MEDIA_URL)));
        sequencer.setSequence(is);
        sequencer.start();
    }



    public void setScoreSpecific(Phrase phr, String score) {
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




