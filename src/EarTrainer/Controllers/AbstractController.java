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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import jm.gui.cpn.JGrandStave;
import jm.music.data.*;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.*;
import java.util.List;
import static jm.constants.Pitches.*;
import static jm.constants.RhythmValues.*;


/**
 * Created by timannoel on 18/05/2017.
 */
public abstract class AbstractController {

    Random rn = new Random();

    Score s = new Score();
    Part p = new Part(0);

    Phrase phr1 = new Phrase(0.0);
    Phrase phr2 = new Phrase(0.0);
    Phrase bottomNotes = new Phrase();
    Phrase middleNotes = new Phrase();
    Phrase topNotes = new Phrase();

    CPhrase cphr1 = new CPhrase();
    CPhrase cphr2 = new CPhrase();
    CPhrase cphr3 = new CPhrase();
    CPhrase cphr4 = new CPhrase();
    CPhrase cphr5 = new CPhrase();
    CPhrase cphr6 = new CPhrase();
    CPhrase cphr7 = new CPhrase();
    CPhrase cphr8 = new CPhrase();

    int[] notes = {A3, AS3, B3, C4, CS4, D4, DS4, E4, F4, FS4, G4, GS4, A4, AS4, B4, C5, CS5, D5, DS5, E5, F5, FS5, G5, GS5, A5, AS5, B5, C6, CS6, D6, DS6, E6, F6, FS6, G6, GS6};
    int SIZE_OF_NOTES_ARRAY = notes.length;
    int[] circleOfFifthsMajor = {C4, G4, D4, A4, E4, B4, FS4, DF4, AF4, EF4, BF4, F4};
    int[] circleOfFifthsMinor = {A4, E4, B4, FS4, CS4, GS4, DS4, BF4, F4, C4, G4, D4};

    int[] minorScale = new int[22];
    int[] majorScale = new int[22];
    int[] scaleNotes = new int[22];

    List noteLengthsList = new LinkedList(Arrays.asList(SIXTEENTH_NOTE, DOTTED_SIXTEENTH_NOTE,
                                                                    EIGHTH_NOTE, DOTTED_EIGHTH_NOTE,
                                                                    QUARTER_NOTE, DOTTED_QUARTER_NOTE,
                                                                    HALF_NOTE));

//    Note[] theirMelodyAnswer;
    Note[] scaleChord1 = new Note[3];
    Note[] scaleChord2 = new Note[3];
    Note[] scaleChord3 = new Note[3];
    Note[] scaleChord4 = new Note[3];
    Note[] scaleChord5 = new Note[3];
    Note[] scaleChord5MelodicMinor = new Note[3];
    Note[] scaleChord6 = new Note[3];
    Note[] scaleChord7 = new Note[3];
    Note[] bottomNotesArray = new Note[4];
    Note[] middleNotesArray = new Note[4];
    Note[] topNotesArray = new Note[4];

    Note[] usedChord1;
    Note[] usedChord2;
    Note[] usedChord3;
    Note[] usedChord4;
    Note[] usedChord5;
    Note[] usedChord6;
    Note[] usedChord7;
    Note[] usedChord8;

    Note[][] scaleChords = {scaleChord1, scaleChord2, scaleChord3, scaleChord4, scaleChord5, scaleChord6, scaleChord7};
    Note[][] scaleChords1And6 = {scaleChord1, scaleChord6};

    boolean minor = false;
    boolean major = false;

    static final int TOTAL_QUESTIONS = 10;
    @FXML StackPane stackPane;

    @FXML HBox radioButtonsGroup;
    @FXML RadioButton easyRadioButton;
    @FXML RadioButton mediumRadioButton;
    @FXML RadioButton hardRadioButton;

    @FXML Button correctButton = new Button();

    @FXML Label timerLabel;
    @FXML Label questionLabel;
    @FXML Label difficultyDescriptionLabel;
    @FXML Label correctIncorrectLabel;

    @FXML Button startButton;
    @FXML Button nextQuestionButton;

    @FXML Pane scorePane;

    JGrandStave jScore = new JGrandStave();
    Phrase phrase = new Phrase();

    String strSecs;
    String strMins;

    int questionNumber;
    int numberOfCorrectAnswers = 0;

    String correctAnswer = "unison";
    boolean questionAnswered;
    Timeline timeline;
    boolean startClicked = false;

    Sequencer sequencer;




    String getInterval(int i) {
        switch (i) {
            case 0:
                return "unison";

            case -1:
            case 1:
                return "minor second";

            case -2:
            case 2:
                return "major second";

            case -3:
            case 3:
                return "minor third";

            case -4:
            case 4:
                return "major third";

            case -5:
            case 5:
                return "perfect fourth";

            case -6:
            case 6:
                return "tritone";

            case -7:
            case 7:
                return "perfect fifth";

            case -8:
            case 8:
                return "minor sixth";

            case -9:
            case 9:
                return "major sixth";

            case -10:
            case 10:
                return "minor seventh";

            case -11:
            case 11:
                return "major seventh";

            case -12:
            case 12:
                return "octave";

            default:
                return "";
        }
    }


    String getNote(Note n2) {
        switch (n2.getPitch()) {
            case C3:
            case C4:
            case C5:
            case C6:
                return "C";

            case CS3:
            case CS4:
            case CS5:
            case CS6:
                return "C#";

            case D3:
            case D4:
            case D5:
            case D6:
                return "D";

            case DS3:
            case DS4:
            case DS5:
            case DS6:
                return "D#";

            case E3:
            case E4:
            case E5:
            case E6:
                return "E";

            case F3:
            case F4:
            case F5:
            case F6:
                return "F";

            case FS3:
            case FS4:
            case FS5:
            case FS6:
                return "F#";

            case G3:
            case G4:
            case G5:
            case G6:
                return "G";

            case GS3:
            case GS4:
            case GS5:
            case GS6:
                return "G#";

            case A3:
            case A4:
            case A5:
            case A6:
                return "A";

            case AS3:
            case AS4:
            case AS5:
            case AS6:
                return "A#";

            case B3:
            case B4:
            case B5:
            case B6:
                return "B";

            default:
                return "";
        }
    }


    void setScoreEditable(Phrase phr) {
        jScore.setPhrase(phr);

        Dimension d = new Dimension();
        d.setSize(600, 300);
        jScore.setPreferredSize(d);
        jScore.setMaximumSize(d);

        jScore.removeTitle();
        jScore.setEditable(true);
    }


    private int sharpen(int note) {
        for (int i = 0; i < notes.length; i++) {
            if (notes[i] == note) {
                return notes[(i + 1) % SIZE_OF_NOTES_ARRAY];
            }
        }
        return 0;
    }


    private void makeChords(int[] scale) {
        scaleChord1 = new Note[]{new Note(scale[0], C), new Note(scale[2], C), new Note(scale[4], C)};

        //If minor, diminished, invert, 3-5-8
        if (minor) {
            scaleChord2 = new Note[]{new Note(scale[3], C), new Note(scale[5], C), new Note(scale[8], C)};
        } else {
            scaleChord2 = new Note[]{new Note(scale[1], C), new Note(scale[3], C), new Note(scale[5], C)};
        }

        scaleChord3 = new Note[]{new Note(scale[2], C), new Note(scale[4], C), new Note(scale[6], C)};

        scaleChord4 = new Note[]{new Note(scale[3], C), new Note(scale[5], C), new Note(scale[7], C)};

        //Minor 5 chord, make major if doing cadence into tonic, 7#, melodic minor scale
        scaleChord5 = new Note[]{new Note(scale[4], C), new Note(scale[6], C), new Note(scale[8], C)};
        scaleChord5MelodicMinor = new Note[]{new Note(scale[4], C), new Note(sharpen(scale[6]), C), new Note(scale[8], C)};

        scaleChord6 = new Note[]{new Note(scale[5], C), new Note(scale[7], C), new Note(scale[9], C)};

        //For major and minor 1st inversion
        scaleChord7 = new Note[]{new Note(scale[8], C), new Note(scale[10], C), new Note(scale[13], C)};
    }


    void makeMinorScale(int root) {

        for (int i = 0; i < notes.length; i++) {
            if (notes[i] == root) {
                minorScale[0] = notes[i];
                minorScale[1] = notes[(i + 2) % SIZE_OF_NOTES_ARRAY];
                minorScale[2] = notes[(i + 3) % SIZE_OF_NOTES_ARRAY];
                minorScale[3] = notes[(i + 5) % SIZE_OF_NOTES_ARRAY];
                minorScale[4] = notes[(i + 7) % SIZE_OF_NOTES_ARRAY];
                minorScale[5] = notes[(i + 8) % SIZE_OF_NOTES_ARRAY];
                minorScale[6] = notes[(i + 10) % SIZE_OF_NOTES_ARRAY];

                minorScale[7] = notes[(i + 12) % SIZE_OF_NOTES_ARRAY];
                minorScale[8] = notes[(i + 14) % SIZE_OF_NOTES_ARRAY];
                minorScale[9] = notes[(i + 15) % SIZE_OF_NOTES_ARRAY];
                minorScale[10] = notes[(i + 17) % SIZE_OF_NOTES_ARRAY];
                minorScale[11] = notes[(i + 19) % SIZE_OF_NOTES_ARRAY];
                minorScale[12] = notes[(i + 20) % SIZE_OF_NOTES_ARRAY];
                minorScale[13] = notes[(i + 22) % SIZE_OF_NOTES_ARRAY];

                minorScale[14] = notes[(i + 24) % SIZE_OF_NOTES_ARRAY];
                minorScale[15] = notes[(i + 26) % SIZE_OF_NOTES_ARRAY];
                minorScale[16] = notes[(i + 27) % SIZE_OF_NOTES_ARRAY];
                minorScale[17] = notes[(i + 29) % SIZE_OF_NOTES_ARRAY];
                minorScale[18] = notes[(i + 31) % SIZE_OF_NOTES_ARRAY];
                minorScale[19] = notes[(i + 32) % SIZE_OF_NOTES_ARRAY];
                minorScale[20] = notes[(i + 34) % SIZE_OF_NOTES_ARRAY];
                minorScale[21] = notes[(i + 36) % SIZE_OF_NOTES_ARRAY];
                break;
            }
        }

        makeChords(minorScale);

        scaleChords = new Note[][]{scaleChord1, scaleChord2, scaleChord3, scaleChord4, scaleChord5, scaleChord6, scaleChord7};
        scaleChords1And6 = new Note[][]{scaleChord1, scaleChord6};
    }


    void makeMajorScale(int root) {

        for (int i = 0; i < notes.length; i++) {
            if (notes[i] == root) {
                majorScale[0] = notes[i];
                majorScale[1] = notes[(i + 2) % SIZE_OF_NOTES_ARRAY];
                majorScale[2] = notes[(i + 4) % SIZE_OF_NOTES_ARRAY];
                majorScale[3] = notes[(i + 5) % SIZE_OF_NOTES_ARRAY];
                majorScale[4] = notes[(i + 7) % SIZE_OF_NOTES_ARRAY];
                majorScale[5] = notes[(i + 9) % SIZE_OF_NOTES_ARRAY];
                majorScale[6] = notes[(i + 11) % SIZE_OF_NOTES_ARRAY];

                majorScale[7] = notes[(i + 12) % SIZE_OF_NOTES_ARRAY];
                majorScale[8] = notes[(i + 14) % SIZE_OF_NOTES_ARRAY];
                majorScale[9] = notes[(i + 16) % SIZE_OF_NOTES_ARRAY];
                majorScale[10] = notes[(i + 17) % SIZE_OF_NOTES_ARRAY];
                majorScale[11] = notes[(i + 19) % SIZE_OF_NOTES_ARRAY];
                majorScale[12] = notes[(i + 21) % SIZE_OF_NOTES_ARRAY];
                majorScale[13] = notes[(i + 23) % SIZE_OF_NOTES_ARRAY];

                minorScale[14] = notes[(i + 24) % SIZE_OF_NOTES_ARRAY];
                minorScale[15] = notes[(i + 26) % SIZE_OF_NOTES_ARRAY];
                minorScale[16] = notes[(i + 28) % SIZE_OF_NOTES_ARRAY];
                minorScale[17] = notes[(i + 29) % SIZE_OF_NOTES_ARRAY];
                minorScale[18] = notes[(i + 31) % SIZE_OF_NOTES_ARRAY];
                minorScale[19] = notes[(i + 33) % SIZE_OF_NOTES_ARRAY];
                minorScale[20] = notes[(i + 35) % SIZE_OF_NOTES_ARRAY];
                minorScale[21] = notes[(i + 36) % SIZE_OF_NOTES_ARRAY];
            }
        }

        makeChords(majorScale);

        scaleChords = new Note[][]{scaleChord1, scaleChord2, scaleChord3, scaleChord4, scaleChord5, scaleChord6, scaleChord7};
        scaleChords1And6 = new Note[][]{scaleChord1, scaleChord6};
    }


    void chooseRandomRootAndMakeMinorOrMajorScale(){
        int i = rn.nextInt(12);
        int rootNote = notes[i];
        int minorOrMajor = rn.nextInt(2);

        if (minorOrMajor == 0) {
            minor = true;
            makeMinorScale(rootNote);
            scaleNotes = minorScale.clone();
        } else {
            major = true;
            makeMajorScale(rootNote);
            scaleNotes = majorScale.clone();
        }
    }


    void placeCommonNotesInSameVoice(Note[] chord1, Note[] chord2){
        for(int i = 0; i < chord1.length; i++){
            int pitch1 = chord1[i].getPitch();

            for(int j = 0; j < chord2.length; j++){
                int pitch2 = chord2[j].getPitch();

                if(pitch1 + 12 == pitch2 || pitch1 + 24 == pitch2 || pitch1 + 36 == pitch2
                        ||pitch1 - 12 == pitch2 || pitch1 - 24 == pitch2 || pitch1 - 36 == pitch2) {
                    chord2[j].setPitch(chord2[i].getPitch());
                    chord2[i].setPitch(pitch1);
                }
            }
        }
    }


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

//        jScore.addMouseListener(new MouseListener() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                theirMelodyAnswer = jScore.getPhrase().getNoteArray();
//            }
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//
//            }
//        });
    }


    @FXML
    private void BackButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) stackPane.getScene().getWindow();
        stage.hide();
    }


    @FXML
    void StartButtonClicked(ActionEvent event) throws IOException, InvalidMidiDataException, MidiUnavailableException, LineUnavailableException, UnsupportedAudioFileException {
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
    void NextQuestionButtonClicked(ActionEvent event) throws IOException, InvalidMidiDataException, MidiUnavailableException, LineUnavailableException, UnsupportedAudioFileException {
        sequencer.stop();
        sequencer.close();
        correctIncorrectLabel.setText("");


        if (questionNumber != TOTAL_QUESTIONS) {
            questionNumber++;
            questionLabel.setText("Question " + Integer.toString(questionNumber));

            questionAnswered = false;
            nextQuestionButton.setDisable(true);
            resetButtonColours();
//        setScore(phrase);
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


    protected abstract void resetButtonColours();


    void loadScore() {
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


    void stopTimer() {
        timerLabel.setVisible(false);
        timeline.stop();
    }


    @FXML
    void answerButtonClicked() throws IOException {
        questionAnswered = true;
        nextQuestionButton.setDisable(false);

        setScore(phr2);
    }


    void checkAnswer(String answer, Button button) {
        if(!answer.equals(correctAnswer)){
            correctIncorrectLabel.setTextFill(Color.web("#da4343"));
            correctIncorrectLabel.setText("Incorrect.");
            makeButtonRed(button);
        } else {
            correctIncorrectLabel.setTextFill(javafx.scene.paint.Color.web("#3abf4c"));
            correctIncorrectLabel.setText("Correct!");
            numberOfCorrectAnswers++;
        }

        makeButtonGreen(correctButton);

        if(questionNumber == TOTAL_QUESTIONS){
            nextQuestionButton.setText("Score");
        }
    }


    void makeButtonRed(Button button) {
        button.setStyle("-fx-base: #ffb3b3;");
    }


    void makeButtonGreen(Button correctButton) {
        correctButton.setStyle("-fx-base: #adebad;");
    }


    void startTimer() {
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


    protected abstract void generateQuestion() throws IOException, MidiUnavailableException, InvalidMidiDataException, LineUnavailableException, UnsupportedAudioFileException;


    @FXML
    void replayButtonClicked(ActionEvent event) throws IOException, InvalidMidiDataException, MidiUnavailableException, LineUnavailableException, UnsupportedAudioFileException {
        sequencer.stop();
        sequencer.close();

        playSound();
    }


    protected abstract void playSound() throws MidiUnavailableException, IOException, InvalidMidiDataException, UnsupportedAudioFileException, LineUnavailableException;


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
