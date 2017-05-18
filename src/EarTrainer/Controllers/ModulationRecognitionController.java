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


public class ModulationRecognitionController extends AbstractController{

    @FXML private Button similarKey0Button;
    @FXML private Button similarKey1Button;
    @FXML private Button similarKey2Button;
    @FXML private Button similarKey3Button;
    @FXML private Button similarKey4Button;

    @FXML private Label rootKeyLabel;
    @FXML private Label similarKey0Label;
    @FXML private Label similarKey1Label;
    @FXML private Label similarKey2Label;
    @FXML private Label similarKey3Label;
    @FXML private Label similarKey4Label;

    @FXML private Pane scorePaneLeft;
    @FXML private Pane scorePaneRight;

    private JGrandStave jScoreLeft = new JGrandStave();
    private JGrandStave jScoreRight = new JGrandStave();

    private Phrase phrase = new Phrase();
    private Phrase phraseLeft = new Phrase();
    private Phrase phraseRight = new Phrase();

    private int[] similarKeys = new int[5];



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


    protected void resetButtonColours() {
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


    private Button getCorrectButton(String correctAnswer) {
        Button[] buttons = {similarKey0Button, similarKey1Button, similarKey2Button, similarKey3Button, similarKey4Button};

        for(Button b : buttons){
            if(b.getText().equals(correctAnswer)){
                return b;
            }
        }

        return similarKey0Button;
    }


    @Override
    @FXML
    protected void generateQuestion() throws IOException, MidiUnavailableException, InvalidMidiDataException {
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

        System.out.println("New key: " + correctAnswer);

        similarKeys = musicCreator.getSimilarKeys();

        String similarKey0Text;
        String similarKey1Text;
        String similarKey2Text;
        String similarKey3Text;
        String similarKey4Text;

        if(musicCreator.getMinorOrMajor().equals("Major")) {
            similarKey0Label.setText("Root Key Relative Minor:");
            similarKey0Text = musicCreator.getNote(new Note(similarKeys[0], 1.0)) + "m";
            similarKey1Label.setText("Sub Dominant:");
            similarKey1Text = musicCreator.getNote(new Note(similarKeys[1], 1.0));
            similarKey2Label.setText("Sub Dominant Relative Minor:");
            similarKey2Text = musicCreator.getNote(new Note(similarKeys[2], 1.0)) + "m";
            similarKey3Label.setText("Dominant:");
            similarKey3Text = musicCreator.getNote(new Note(similarKeys[3], 1.0));
            similarKey4Label.setText("Dominant Relative Minor:");
            similarKey4Text = musicCreator.getNote(new Note(similarKeys[4], 1.0)) + "m";
        } else {
            similarKey0Label.setText("Root Key Relative Major:");
            similarKey0Text = musicCreator.getNote(new Note(similarKeys[0], 1.0));
            similarKey1Label.setText("Sub Dominant:");
            similarKey1Text = musicCreator.getNote(new Note(similarKeys[1], 1.0)) + "m";
            similarKey2Label.setText("Sub Dominant Relative Major:");
            similarKey2Text = musicCreator.getNote(new Note(similarKeys[2], 1.0));
            similarKey3Label.setText("Dominant:");
            similarKey3Text = musicCreator.getNote(new Note(similarKeys[3], 1.0)) + "m";
            similarKey4Label.setText("Dominant Relative Major:");
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


    protected void playSound() throws MidiUnavailableException, IOException, InvalidMidiDataException {
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




