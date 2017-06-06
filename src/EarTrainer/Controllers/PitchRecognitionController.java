package EarTrainer.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import jm.music.data.*;
import jm.util.Write;
import java.io.*;
import java.util.Random;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import static jm.constants.Pitches.*;
import static jm.constants.RhythmValues.*;



public class PitchRecognitionController extends AbstractController{

    @FXML public Button cButton;
    @FXML public Button cSharpButton;
    @FXML public Button dButton;
    @FXML public Button dSharpButton;
    @FXML public Button eButton;
    @FXML public Button fButton;
    @FXML public Button fSharpButton;
    @FXML public Button gButton;
    @FXML public Button gSharpButton;
    @FXML public Button aButton;
    @FXML public Button aSharpButton;
    @FXML public Button bButton;



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
    void cButtonClicked() throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer("C", cButton);
        }
    }


    @FXML
    private void cSharpButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer("C#", cSharpButton);
        }
    }


    @FXML
    private void dButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer("D", dButton);
        }
    }


    @FXML
    private void dSharpButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer("D#", dSharpButton);
        }
    }


    @FXML
    private void eButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer("E", eButton);
        }
    }


    @FXML
    private void fButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer("F", fButton);
        }
    }


    @FXML
    private void fSharpButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer("F#", fSharpButton);
        }
    }


    @FXML
    private void gButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer("G", gButton);
        }
    }


    @FXML
    private void gSharpButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer("G#", gSharpButton);
        }
    }


    @FXML
    private void aButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer("A", aButton);
        }
    }


    @FXML
    private void aSharpButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer("A#", aSharpButton);
        }
    }


    @FXML
    private void bButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer("B", bButton);
        }
    }


    Button getCorrectButton(String correctAnswer) {
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
        int interval = rn.nextInt(12);

        setScore(phr1);

        Note n = new Note(C4 + interval, C);

        phr2.addNote(n);

        p.addPhrase(phr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Pitch.mid");

        return getNote(n);
    }


    private String makeMIDIHardPitch() {
        int interval = rn.nextInt(36);

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




