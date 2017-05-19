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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import jm.audio.*;
import jm.gui.cpn.JGrandStave;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Write;

import java.awt.*;
import java.io.*;
import java.util.concurrent.ThreadLocalRandom;


import javax.sound.midi.*;
import javax.sound.midi.Instrument;
import javax.sound.sampled.*;

import static jm.constants.Pitches.*;
import static jm.constants.RhythmValues.*;
import static jm.constants.Durations.*;


public class SharpFlatRecognitionController extends AbstractController {

    @FXML private Button flatButton;
    @FXML private Button sharpButton;



    @FXML
    private void easyRadioButtonSelected(ActionEvent event) throws IOException {
        difficultyDescriptionLabel.setText("Middle C only. +/- 15-25 Cents");
    }


    @FXML
    private void mediumRadioButtonSelected(ActionEvent event) throws IOException {
        difficultyDescriptionLabel.setText("Middle C only. +/- 5-15 Cents");
    }


    @FXML
    private void hardRadioButtonSelected(ActionEvent event) throws IOException {
        difficultyDescriptionLabel.setText("All notes. 1 Octave. +/- 1-5 Cents");
    }


    @FXML
    private void BackButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) stackPane.getScene().getWindow();
        stage.hide();
    }


    @Override
    @FXML
    void NextQuestionButtonClicked(ActionEvent event) throws IOException, InvalidMidiDataException, MidiUnavailableException, LineUnavailableException, UnsupportedAudioFileException {
//        sequencer.stop();
//        sequencer.close();

        if (questionNumber != TOTAL_QUESTIONS) {
            questionNumber++;
            questionLabel.setText("Question " + Integer.toString(questionNumber));

            questionAnswered = false;
            nextQuestionButton.setDisable(true);
            resetButtonColours();
            //setScore(phrase);
            generateQuestion();
        } else {
            nextQuestionButton.setText("Next Question");
            questionLabel.setVisible(false);
            nextQuestionButton.setDisable(true);
            startClicked = false;
            stopTimer();
            loadScore();

            questionAnswered = false;
            nextQuestionButton.setDisable(true);
            resetButtonColours();
        }
    }


    protected void resetButtonColours() {
        flatButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        sharpButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
    }


    @FXML
    private void flatButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("flat", flatButton);
        }
    }


    @FXML
    private void sharpButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("sharp", sharpButton);
        }
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


    private double getSecondNoteFrequency(double ratio1, double ratio2, double noteOneFrequency){
        double random = ThreadLocalRandom.current().nextDouble(ratio1, ratio2);

        System.out.println(noteOneFrequency);
        double freq1 = noteOneFrequency * random;
        double freq2 = noteOneFrequency / random;
        double[] freqs = {freq1, freq2};
        int i3 = rn.nextInt(2);
        double freq = freqs[i3];
        System.out.println(freq);

        return freq;
    }


    private String makeMIDIEasySharpFlat() {
        //setScore(phr2);

        Note n = new Note(C4, C);

        phr2.addNote(n);
        phr1.addNote(n);

        // set up an audio instrument
        jm.audio.Instrument sineWave = new OvertoneInst(44100);

        double note2Freq = getSecondNoteFrequency(FIFTEEN_CENTS_RATIO, TWENTY_FIVE_CENTS_RATIO, n.getFrequency());


        Note n2 = new Note(note2Freq, C);
        phr1.addNote(n2);


        p.addPhrase(phr1);
        s.addPart(p);

        Write.au(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/SharpFlat.au", sineWave);

        if(n.getFrequency() < note2Freq){
            return "sharp";
        } else {
            return "flat";
        }
    }


    private String makeMIDIMediumSharpFlat() {
        //setScore(phr2);

        Note n = new Note(C4, C);

        phr2.addNote(n);
        phr1.addNote(n);

        // set up an audio instrument
        jm.audio.Instrument sineWave = new OvertoneInst(44100);

        double note2Freq = getSecondNoteFrequency(FIVE_CENTS_RATIO, FIFTEEN_CENTS_RATIO, n.getFrequency());


        Note n2 = new Note(note2Freq, C);
        phr1.addNote(n2);


        p.addPhrase(phr1);
        s.addPart(p);

        Write.au(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/SharpFlat.au", sineWave);

        if(n.getFrequency() < note2Freq){
            return "sharp";
        } else {
            return "flat";
        }
    }


    private String makeMIDIHardSharpFlat() {
        int interval = rn.nextInt(12);

        //setScore(phr2);

        Note n = new Note(C4 + interval, C);

        phr2.addNote(n);
        phr1.addNote(n);

        // set up an audio instrument
        jm.audio.Instrument sineWave = new OvertoneInst(44100);

        double note2Freq = getSecondNoteFrequency(ONE_CENT_RATIO, FIVE_CENTS_RATIO, n.getFrequency());


        Note n2 = new Note(note2Freq, C);
        phr1.addNote(n2);


        p.addPhrase(phr1);
        s.addPart(p);

        Write.au(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/SharpFlat.au", sineWave);

        if(n.getFrequency() < note2Freq){
            return "sharp";
        } else {
            return "flat";
        }
    }


    @FXML
    protected void generateQuestion() throws IOException, MidiUnavailableException, InvalidMidiDataException, LineUnavailableException, UnsupportedAudioFileException {
//        musicCreator = new JMMusicCreator(jScore);
        phr1 = new Phrase();
        phr2 = new Phrase();
        p = new Part();
        s = new Score();


        if(easyRadioButton.isSelected()){
            correctAnswer = makeMIDIEasySharpFlat();
        } else if(mediumRadioButton.isSelected()){
            correctAnswer = makeMIDIMediumSharpFlat();
        } else if(hardRadioButton.isSelected()){
            correctAnswer = makeMIDIHardSharpFlat();
        }

        correctButton = getCorrectButton(correctAnswer);

        playSound();
    }


    @Override
    @FXML
    void replayButtonClicked(ActionEvent event) throws IOException, InvalidMidiDataException, MidiUnavailableException, LineUnavailableException, UnsupportedAudioFileException {
        playSound();
    }


    @Override
    protected void playSound() throws MidiUnavailableException, IOException, InvalidMidiDataException, UnsupportedAudioFileException, LineUnavailableException {
        final String MEDIA_URL = "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/SharpFlat.au";

        File audioFile = new File(MEDIA_URL);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
        AudioFormat format = audioStream.getFormat();
        DataLine.Info info = new DataLine.Info(Clip.class, format);
        Clip audioClip = (Clip) AudioSystem.getLine(info);
        audioClip.open(audioStream);
        audioClip.start();
    }

}




