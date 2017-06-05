package EarTrainer.Controllers;

import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import jm.gui.cpn.JGrandStave;
import jm.music.data.*;
import jm.util.Write;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import java.awt.*;
import java.io.*;
import java.util.Random;
import static jm.constants.Durations.*;
import static jm.constants.Pitches.*;


public class HarmonicIntervalRecognitionController extends AbstractController {

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

    @FXML private Pane scorePaneTop;




    @Override
    @FXML
    public void initialize() {
        Dimension d = new Dimension();
        d.setSize(475,300);

        jScore.setPreferredSize(d);
        jScore.setMaximumSize(d);
        jScore.removeTitle();
        jScore.setEditable(false);

        jScoreTop.setPreferredSize(d);
        jScoreTop.setMaximumSize(d);
        jScoreTop.removeTitle();
        jScoreTop.setEditable(false);

        SwingNode swingNode = new SwingNode();
        SwingNode swingNodeRight = new SwingNode();


        // Top 200px and bottom 200px of yourSwingNode will be trimed.
        swingNode.setLayoutY(-100.0);
        swingNode.layoutBoundsProperty().addListener((o, ov, nv) -> {
            scorePane.setMaxHeight(nv.getHeight() - 100.0);
        });

        swingNodeRight.setLayoutY(-100.0);
        swingNodeRight.layoutBoundsProperty().addListener((o, ov, nv) -> {
            scorePaneTop.setMaxHeight(nv.getHeight() - 100.0);
        });


        // Set a clip for the layout bounds of Pane if you need
        Rectangle clip = new Rectangle();
        scorePane.layoutBoundsProperty().addListener((o, ov, nv) -> {
            clip.setWidth(nv.getWidth());
            clip.setHeight(nv.getHeight());
        });
        scorePane.setClip(clip);

        Rectangle clip2 = new Rectangle();
        scorePaneTop.layoutBoundsProperty().addListener((o, ov, nv) -> {
            clip2.setWidth(nv.getWidth());
            clip2.setHeight(nv.getHeight());
        });
        scorePaneTop.setClip(clip2);


        swingNode.setContent(jScore);
        swingNodeRight.setContent(jScoreTop);

        scorePane.getChildren().add(swingNode);
        scorePaneTop.getChildren().add(swingNodeRight);
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



    protected void resetButtonColours() {
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
            answerButtonClicked();
            checkAnswer("unison", unisonButton);
        }
    }


    @FXML
    private void minorSecondButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer("minor second", minorSecondButton);
        }
    }


    @FXML
    private void majorSecondButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer("major second", majorSecondButton);
        }
    }


    @FXML
    private void perfectFourthButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer("perfect fourth", perfectFourthButton);
        }
    }


    @FXML
    private void tritoneButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer("tritone", tritoneButton);
        }
    }


    @FXML
    private void minorThirdButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer("minor third", minorThirdButton);
        }
    }


    @FXML
    private void majorThirdButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer("major third", majorThirdButton);
        }
    }


    @FXML
    private void perfectFifthButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer("perfect fifth", perfectFifthButton);
        }
    }


    @FXML
    private void octaveButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer("octave", octaveButton);
        }
    }


    @FXML
    private void minorSixthButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer("minor sixth", minorSixthButton);
        }
    }


    @FXML
    private void majorSixthButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer("major sixth", majorSixthButton);
        }
    }


    @FXML
    private void minorSeventhButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer("minor seventh", minorSeventhButton);
        }
    }


    @FXML
    private void majorSeventhButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer("major seventh", majorSeventhButton);
        }
    }


    @FXML
    void answerButtonClicked() throws IOException {
        questionAnswered = true;
        nextQuestionButton.setDisable(false);

        jScoreTop.setPhrase(phr2);
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


    private String makeMIDIEasyHarmonic() {
        Note n1 = new Note(C4, C);
        phr1.addNote(n1);

        setScore(phr1);
        jScoreTop.setPhrase(emptyPhr);

        int i = rn.nextInt(5);
        int[] array = {0, 3, 4, 7, 12};
        int interval = array[i];

        System.out.println(i);

        Note n2 = new Note(C4 + interval, C);
        phr2.addNote(n2);

        Note[] notes = {n1, n2};
        cphr1.addChord(notes);

        p.addCPhrase(cphr1);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/HarmonicInterval.mid");

        return getInterval(interval);
    }


    private String makeMIDIMediumHarmonic() {
        Random rn = new Random();
        int i = rn.nextInt(7);
        int[] array = {0, 1, 2, 3, 4, 7, 12};
        int interval = array[i];

        System.out.println(interval);

        int i2 = rn.nextInt(7);
        int[] roots = {C4,
                D4,
                E4,
                F4,
                G4,
                A4,
                B4,};

        int chosenRoot = roots[i2];

        Note n1 = new Note(chosenRoot, C);
        phr1.addNote(n1);

        setScore(phr1);
        jScoreTop.setPhrase(emptyPhr);

        Note n2 = new Note(chosenRoot + interval, C);
        phr2.addNote(n2);

        Note[] notes = {n1, n2};
        cphr1.addChord(notes);

        p.addCPhrase(cphr1);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/HarmonicInterval.mid");

        return getInterval(interval);
    }


    private String makeMIDIHardHarmonic() {
        int i = rn.nextInt(25) - 12;
        int i2 = rn.nextInt(21);

        System.out.println(i);

        int[] roots = {C3, C4, C5,
                CS3, CS4, CS5,
                D3, D4, D5,
                DS3, DS4, DS5,
                E3, E4, E5,
                F3, F4, F5,
                FS3, FS4, FS5,
                G3, G4, G5,
                GS3, GS4, GS5,
                A3, A4, A5,
                AS3, AS4, AS5,
                B3, B4, B5};

        int chosenRoot = roots[i2];

        Note n1 = new Note(chosenRoot, C);
        phr1.addNote(n1);

        setScore(phr1);
        jScoreTop.setPhrase(emptyPhr);

        Note n2 = new Note(chosenRoot + i, C);
        phr2.addNote(n2);

        Note[] notes = {n1, n2};
        cphr1.addChord(notes);

        p.addCPhrase(cphr1);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/HarmonicInterval.mid");

        return getInterval(i);
    }


    @FXML
    protected void generateQuestion() throws IOException, MidiUnavailableException, InvalidMidiDataException {
        phr1 = new Phrase();
        phr2 = new Phrase();
        cphr1 = new CPhrase();

        p = new Part();
        s = new Score();


        if(easyRadioButton.isSelected()){
            correctAnswer = makeMIDIEasyHarmonic();
        } else if(mediumRadioButton.isSelected()){
            correctAnswer = makeMIDIMediumHarmonic();
        } else if(hardRadioButton.isSelected()){
            correctAnswer = makeMIDIHardHarmonic();
        }

        correctButton = getCorrectButton(correctAnswer);

        playSound();
    }


    protected void playSound() throws MidiUnavailableException, IOException, InvalidMidiDataException {
        final String MEDIA_URL = "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/HarmonicInterval.mid";

        sequencer = MidiSystem.getSequencer();
        sequencer.open();
        InputStream is = new BufferedInputStream(new FileInputStream(new File(MEDIA_URL)));
        sequencer.setSequence(is);
        sequencer.start();
    }
}




