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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import jm.gui.cpn.BassStave;
import jm.gui.cpn.JGrandStave;
import jm.gui.cpn.Stave;
import jm.music.data.*;
import jm.util.View;
import jm.util.Write;

import java.awt.*;
import java.io.*;
import java.util.Random;


import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.swing.*;

import static jm.constants.Pitches.*;
import static jm.constants.RhythmValues.*;
import static jm.constants.Durations.*;


public class PitchRecognitionController extends AbstractController{

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



    @FXML
    private void easyRadioButtonSelected(ActionEvent event) throws IOException {
        cSharpButton.setDisable(true);
        dSharpButton.setDisable(true);
        fSharpButton.setDisable(true);
        gSharpButton.setDisable(true);
        aSharpButton.setDisable(true);

        difficultyDescriptionLabel.setText("White notes only. 1 Octave.");
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



    protected void resetButtonColours() {
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
            checkAnswer("C", cButton);
        }
    }


    @FXML
    private void cSharpButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("C#", cSharpButton);
        }
    }


    @FXML
    private void dButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("D", dButton);
        }
    }


    @FXML
    private void dSharpButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("D#", dSharpButton);
        }
    }


    @FXML
    private void eButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("E", eButton);
        }
    }


    @FXML
    private void fButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("F", fButton);
        }
    }


    @FXML
    private void fSharpButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("F#", fSharpButton);
        }
    }


    @FXML
    private void gButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("G", gButton);
        }
    }


    @FXML
    private void gSharpButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("G#", gSharpButton);
        }
    }


    @FXML
    private void aButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("A", aButton);
        }
    }


    @FXML
    private void aSharpButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("A#", aSharpButton);
        }
    }


    @FXML
    private void bButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("B", bButton);
        }
    }


    private Button getCorrectButton(String correctAnswer) {
        switch(correctAnswer){
            case "C":
                return cButton;
            case "C#":
                return cSharpButton;
            case "D":
                return dButton;
            case "D#":
                return dSharpButton;
            case "E":
                return eButton;
            case "F":
                return fButton;
            case "F#":
                return fSharpButton;
            case "G":
                return gButton;
            case "G#":
                return gSharpButton;
            case "A":
                return aButton;
            case "A#":
                return aSharpButton;
            case "B":
                return bButton;
            default:
                return cButton;
        }
    }


    private String makeMIDIEasyPitch() {
        Random rn = new Random();
        int i = rn.nextInt(7);
        int[] array = {0, 2, 4, 5, 7, 9, 11};
        int interval = array[i];

        setScore(phr1);

        Note n = new Note(C4 + interval, C);

        phr2.addNote(n);

        p.addPhrase(phr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Pitch.mid");

        return getNote(n);
    }


    private String makeMIDIMediumPitch() {
        Random rn = new Random();
        int i = rn.nextInt(12);
        int[] array = new int[12];

        for (int j = 0; j < 12; j++) {
            array[j] = j;
        }

        int interval = array[i];

        setScore(phr1);

        Note n = new Note(C4 + interval, C);

        phr2.addNote(n);

        p.addPhrase(phr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Pitch.mid");

        return getNote(n);
    }


    private String makeMIDIHardPitch() {
        Random rn = new Random();
        int i = rn.nextInt(36);
        int[] array = new int[36];

        for (int j = 0; j < 36; j++) {
            array[j] = j;
        }

        int interval = array[i];

        setScore(phr1);

        Note n = new Note(C3 + interval, C);

        phr2.addNote(n);

        p.addPhrase(phr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Pitch.mid");

        return getNote(n);
    }


    @FXML
    protected void generateQuestion() throws IOException, MidiUnavailableException, InvalidMidiDataException {
//        musicCreator = new JMMusicCreator(jScore);
        phr1 = new Phrase();
        phr2 = new Phrase();
        p = new Part();
        s = new Score();


        if(easyRadioButton.isSelected()){
            correctAnswer = makeMIDIEasyPitch();
        } else if(mediumRadioButton.isSelected()){
            correctAnswer = makeMIDIMediumPitch();
        } else if(hardRadioButton.isSelected()){
            correctAnswer = makeMIDIHardPitch();
        }

        correctButton = getCorrectButton(correctAnswer);

        playSound();
    }



    protected void playSound() throws MidiUnavailableException, IOException, InvalidMidiDataException {
        final String MEDIA_URL = "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Pitch.mid";

        sequencer = MidiSystem.getSequencer();
        sequencer.open();
        InputStream is = new BufferedInputStream(new FileInputStream(new File(MEDIA_URL)));
        sequencer.setSequence(is);
        sequencer.start();
    }

}




