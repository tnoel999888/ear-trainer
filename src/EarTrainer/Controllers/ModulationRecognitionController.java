package EarTrainer.Controllers;

import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jm.gui.cpn.JGrandStave;
import jm.music.data.*;
import jm.util.Write;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

import static jm.constants.Pitches.*;
import static jm.constants.Pitches.D4;
import static jm.constants.Pitches.G4;
import static jm.constants.RhythmValues.*;


public class ModulationRecognitionController extends AbstractController{

    @FXML public Button similarKey0Button;
    @FXML public Button similarKey1Button;
    @FXML public Button similarKey2Button;
    @FXML public Button similarKey3Button;
    @FXML public Button similarKey4Button;

    @FXML private Label rootKeyLabel;
    @FXML private Label similarKey0Label;
    @FXML private Label similarKey1Label;
    @FXML private Label similarKey2Label;
    @FXML private Label similarKey3Label;
    @FXML private Label similarKey4Label;

    @FXML private Pane scorePaneLeft;
    @FXML private Pane scorePaneRight;

    private int[] similarKeys = new int[5];

    private Note[] usedChord1Copy = new Note[4];
    private Note[] usedChord2Copy = new Note[4];
    private Note[] usedChord3Copy = new Note[4];
    private Note[] usedChord4Copy = new Note[4];
    private Note[] usedChord5Copy = new Note[4];
    private Note[] usedChord6Copy = new Note[4];
    private Note[] usedChord7Copy = new Note[4];
    private Note[] usedChord8Copy = new Note[4];
    private Note[] usedChord9Copy = new Note[4];

    private int[] circleOfFifthsMajor = {C4, G4, D4, A4, E4, B4, FS4, DF4, AF4, EF4, BF4, F4};
    private int[] circleOfFifthsMinor = {A4, E4, B4, FS4, CS4, GS4, DS4, BF4, F4, C4, G4, D4};

    private boolean chord2Changed;
    private boolean chord3Changed;
    private boolean chord4Changed;
    private boolean chord5Changed;
    private boolean chord6Changed;
    private boolean chord7Changed;
    private boolean chord8Changed;
    private boolean chord9Changed;

    private String rootKeyString;

    private boolean rootMajor;
    private boolean rootMinor;





    @Override
    @FXML
    public void initialize() {
        Dimension d = new Dimension();
        d.setSize(475,300);

        jScore.setPreferredSize(d);
        jScore.setMaximumSize(d);
        jScore.removeTitle();
        jScore.setEditable(false);

        jScoreBottom.setPreferredSize(d);
        jScoreBottom.setMaximumSize(d);
        jScoreBottom.removeTitle();
        jScoreBottom.setEditable(false);

        jScoreTop.setPreferredSize(d);
        jScoreTop.setMaximumSize(d);
        jScoreTop.removeTitle();
        jScoreTop.setEditable(false);

        SwingNode swingNode = new SwingNode();
        SwingNode swingNodeLeft = new SwingNode();
        SwingNode swingNodeRight = new SwingNode();


        // Top 200px and bottom 200px of yourSwingNode will be trimed.
        swingNode.setLayoutY(-100.0);
        swingNode.layoutBoundsProperty().addListener((o, ov, nv) -> {
            scorePane.setMaxHeight(nv.getHeight() - 100.0);
        });

        swingNodeLeft.setLayoutY(-100.0);
        swingNodeLeft.layoutBoundsProperty().addListener((o, ov, nv) -> {
            scorePaneLeft.setMaxHeight(nv.getHeight() - 100.0);
        });

        swingNodeRight.setLayoutY(-100.0);
        swingNodeRight.layoutBoundsProperty().addListener((o, ov, nv) -> {
            scorePaneRight.setMaxHeight(nv.getHeight() - 100.0);
        });


        // Set a clip for the layout bounds of Pane if you need
        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle();
        scorePane.layoutBoundsProperty().addListener((o, ov, nv) -> {
            clip.setWidth(nv.getWidth());
            clip.setHeight(nv.getHeight());
        });
        scorePane.setClip(clip);

        javafx.scene.shape.Rectangle clip2 = new javafx.scene.shape.Rectangle();
        scorePaneLeft.layoutBoundsProperty().addListener((o, ov, nv) -> {
            clip2.setWidth(nv.getWidth());
            clip2.setHeight(nv.getHeight());
        });
        scorePaneLeft.setClip(clip2);

        javafx.scene.shape.Rectangle clip3 = new javafx.scene.shape.Rectangle();
        scorePaneRight.layoutBoundsProperty().addListener((o, ov, nv) -> {
            clip3.setWidth(nv.getWidth());
            clip3.setHeight(nv.getHeight());
        });
        scorePaneRight.setClip(clip3);


        swingNode.setContent(jScore);
        swingNodeLeft.setContent(jScoreBottom);
        swingNodeRight.setContent(jScoreTop);

        scorePane.getChildren().add(swingNode);
        scorePaneLeft.getChildren().add(swingNodeLeft);
        scorePaneRight.getChildren().add(swingNodeRight);
    }


    @FXML
    private void easyRadioButtonSelected(ActionEvent event) throws IOException {
        difficultyDescriptionLabel.setText("Identify the new key. 3 options.");
    }


    @FXML
    private void mediumRadioButtonSelected(ActionEvent event) throws IOException {
        difficultyDescriptionLabel.setText("Identify the new key. 4 options.");
    }


    @FXML
    private void hardRadioButtonSelected(ActionEvent event) throws IOException {
        difficultyDescriptionLabel.setText("Identify the new key. 5 options.");
    }


    protected void resetButtonColours() {
        similarKey0Button.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        similarKey1Button.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        similarKey2Button.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        similarKey3Button.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        similarKey4Button.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
    }


    @Override
    void loadScore() {
        Button[] buttons = {similarKey0Button, similarKey1Button, similarKey2Button, similarKey3Button, similarKey4Button};

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
        controller.setScore(jScore);
        controller.setScoreBottom(jScoreBottom);
        controller.setScoreTop(jScoreTop);
        controller.setEmptyPhr(emptyPhr);
        controller.setNumberOfCorrectAnswers(numberOfCorrectAnswers);
        controller.setTime(strMins, strSecs);
        controller.setStackPane(stackPane);

        controller.setRootKeyLabel(rootKeyLabel);
        controller.setButtons(buttons);

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


    @FXML
    private void similarKey0ButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer(similarKey0Button.getText(), similarKey0Button);
        }
    }


    @FXML
    private void similarKey1ButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer(similarKey1Button.getText(), similarKey1Button);
        }
    }


    @FXML
    private void similarKey2ButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer(similarKey2Button.getText(), similarKey2Button);
        }
    }


    @FXML
    private void similarKey3ButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer(similarKey3Button.getText(), similarKey3Button);
        }
    }


    @FXML
    private void similarKey4ButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer(similarKey4Button.getText(), similarKey4Button);
        }
    }


    @Override
    @FXML
    void startButtonClicked() throws IOException, InvalidMidiDataException, MidiUnavailableException, LineUnavailableException, UnsupportedAudioFileException {
        Button[] buttons = {similarKey0Button, similarKey1Button, similarKey2Button, similarKey3Button, similarKey4Button};
        questionNumber = 1;
        numberOfCorrectAnswers = 0;
        questionLabel.setVisible(!startClicked);
        timerLabel.setVisible(!startClicked);
        radioButtonsGroup.setDisable(!startClicked);

        if(!startClicked) {
            startClicked = true;
            startTimer();
            questionLabel.setText("Question 1");
            startButton.setText("Stop");
            startButton.setStyle("-fx-background-color: rgba(0,0,0,0.08), linear-gradient(#af595f, #754e53), linear-gradient(#ffd5de 0%, #facdd0 10%, #f9cdd6 50%, #fc8f9b 51%, #ffddeb 100%)");
            generateQuestion();
        } else {
            if(sequencer != null) {
                sequencer.stop();
                sequencer.close();
            }

            for(Button button : buttons){
                button.setText("");
            }

            rootKeyLabel.setText("");

            startButton.setStyle("-fx-background-color: rgba(0,0,0,0.08), linear-gradient(#5a61af, #51536d), linear-gradient(#e4fbff 0%,#cee6fb 10%, #a5d3fb 50%, #88c6fb 51%, #d5faff 100%)");
            startClicked = false;
            stopTimer();
            startButton.setText("Start");
            correctIncorrectLabel.setText("");
            resetButtonColours();
            nextQuestionButton.setDisable(true);
            jScore.setPhrase(emptyPhr);
            jScore.removeTitle();
            jScoreBottom.setPhrase(emptyPhr);
            jScoreBottom.removeTitle();
            jScoreTop.setPhrase(emptyPhr);
            jScoreTop.removeTitle();
            questionAnswered = false;
        }
    }


    @Override
    @FXML
    void answerButtonClicked() throws IOException {
        questionAnswered = true;
        nextQuestionButton.setDisable(false);

        //Set the scores
        setScoreSpecific(bottomNotes, "left");
        setScoreSpecific(middleNotes, "middle");
        setScoreSpecific(topNotes, "right");
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


    private int getRelativeMinor(int note) {
        int relativeMinor = 0;

        for (int j = 0; j < SIZE_OF_NOTES_ARRAY; j++) {
            if (notes[j] == note) {
                int index = (j - 3) % SIZE_OF_NOTES_ARRAY;

                if (index < 0) {
                    index += 12;
                }

                relativeMinor = notes[index];
                break;
            }
        }

        return relativeMinor;
    }


    private int getRelativeMajor(int note) {
        int relativeMajor = 0;

        for (int j = 0; j < SIZE_OF_NOTES_ARRAY; j++) {
            if (notes[j] == note) {
                int index = (j + 3) % SIZE_OF_NOTES_ARRAY;

                relativeMajor = notes[index];
                break;
            }
        }

        return relativeMajor;
    }


    private ArrayList findCommonChords(Note[][] origKeyChords, Note[][] newKeyChords) {
        ArrayList commonChords = new ArrayList<Note[][]>();

        for (int i = 0; i < origKeyChords.length; i++) {
            for (int j = 0; j < newKeyChords.length; j++) {
                if ((origKeyChords[i][0].equals(newKeyChords[j][0])) &&
                        (origKeyChords[i][1].equals(newKeyChords[j][1])) &&
                        (origKeyChords[i][2].equals(newKeyChords[j][2]))) {
                    commonChords.add(origKeyChords[j]);
                }
            }
        }

        return commonChords;
    }


    private String modulateFromMajor(int i, int root) {
        Note[][] rootKeyChords = scaleChords;

        int leftOne = (i - 1) % 12;
        int rightOne = (i + 1) % 12;

        if (leftOne < 0) {
            leftOne += 12;
        }

        //Populate similarKeys array
        similarKeys[0] = getRelativeMinor(root);
        similarKeys[1] = circleOfFifthsMajor[leftOne];
        similarKeys[2] = getRelativeMinor(circleOfFifthsMajor[leftOne]);
        similarKeys[3] = circleOfFifthsMajor[rightOne];
        similarKeys[4] = getRelativeMinor(circleOfFifthsMajor[rightOne]);


        //1st Chord. Add tonic of original key
        usedChord1 = scaleChord1;


        //2nd Chord. Add random chord of original key
        int i2 = rn.nextInt(6);
        usedChord2 = scaleChords[i2];


        //Find key to modulate to from similar keys
        int i3 = rn.nextInt(5);
        int newKey = similarKeys[i3];
        String newKeyString = getNote(new Note(newKey, C));


        //Make the scale and chords of the new key
        if (i3 == 0 || i3 == 2 || i3 == 4) {
            minor = true;
            major = false;
            makeMinorScale(newKey);
        } else {
            major = true;
            minor = false;
            makeMajorScale(newKey);
        }


        //3rd Chord. Add a common chord to begin the modulation
        Note[][] newKeyChords = scaleChords;
        ArrayList commonChords = findCommonChords(rootKeyChords, newKeyChords);

        if (commonChords.contains(newKeyChords[1])) {
            usedChord3 = scaleChord2;
        } else if (commonChords.contains(newKeyChords[3])) {
            usedChord3 = scaleChord4;
        } else {
            int i4 = rn.nextInt(commonChords.size());
            usedChord3 = (Note[])commonChords.get(i4);
        }


        //4th Chord. Add the dominant of the new key
        if(minor) {
            usedChord4 = scaleChord5MelodicMinor;
        } else {
            usedChord4 = scaleChord5;
        }


        //5th Chord. Add the tonic of the new key
        usedChord5 = scaleChord1;

        //6th Chord. Add the inverted dominant of new key
        usedChord6 = scaleChord5Inverted;

        //7th Chord. Add the tonic of the new key
        usedChord7 = scaleChord1;


        //Make copies of the original chords
        for(int j = 0; j < usedChord1.length; j++){
            usedChord1Copy[j] = new Note(usedChord1[j].getPitch(), usedChord1[j].getRhythmValue());
            usedChord2Copy[j] = new Note(usedChord2[j].getPitch(), usedChord2[j].getRhythmValue());
            usedChord3Copy[j] = new Note(usedChord3[j].getPitch(), usedChord3[j].getRhythmValue());
            usedChord4Copy[j] = new Note(usedChord4[j].getPitch(), usedChord4[j].getRhythmValue());
            usedChord5Copy[j] = new Note(usedChord5[j].getPitch(), usedChord5[j].getRhythmValue());
            usedChord6Copy[j] = new Note(usedChord6[j].getPitch(), usedChord6[j].getRhythmValue());
            usedChord7Copy[j] = new Note(usedChord7[j].getPitch(), usedChord7[j].getRhythmValue());
        }


        //Rearrange voices to minimise movement
        chord2Changed = placeCommonNotesInSameVoice(usedChord1, usedChord2);
        chord3Changed = placeCommonNotesInSameVoice(usedChord2, usedChord3);
        chord4Changed = placeCommonNotesInSameVoice(usedChord3, usedChord4);
        chord5Changed = placeCommonNotesInSameVoice(usedChord4, usedChord5);
        chord6Changed = placeCommonNotesInSameVoice(usedChord5, usedChord6);
        chord7Changed = placeCommonNotesInSameVoice(usedChord6, usedChord7);


        //Put notes in chords in ascending order
//        usedChord1 = putNotesInVoicesInAscendingOrder(usedChord1);
//        usedChord2 = putNotesInVoicesInAscendingOrder(usedChord2);
//        usedChord3 = putNotesInVoicesInAscendingOrder(usedChord3);
//        usedChord4 = putNotesInVoicesInAscendingOrder(usedChord4);
//        usedChord5 = putNotesInVoicesInAscendingOrder(usedChord5);
//        usedChord6 = putNotesInVoicesInAscendingOrder(usedChord6);
//        usedChord7 = putNotesInVoicesInAscendingOrder(usedChord7);


        usedChord1 = usedChord1Copy;
        int removeFifth;

        //If chords not changed then use the copy
        if(!chord2Changed){
            usedChord2 = usedChord2Copy;

            removeFifth = rn.nextInt(2);
            if (removeFifth == 0) {
                removeFifth(usedChord1, usedChord2);
            }
        }

        if(!chord3Changed){
            usedChord3 = usedChord3Copy;

            if(!chord2Changed) {
                removeFifth = rn.nextInt(2);
                if(removeFifth == 0){
                    removeFifth(usedChord2, usedChord3);
                }
            }
        }

        if(!chord4Changed){
            usedChord4 = usedChord4Copy;

            if(!chord3Changed) {
                removeFifth = rn.nextInt(2);
                if (removeFifth == 0) {
                    removeFifth(usedChord3, usedChord4);
                }
            }
        }

        if(!chord5Changed){
            usedChord5 = usedChord5Copy;

            if(!chord4Changed) {
                removeFifth = rn.nextInt(2);
                if (removeFifth == 0) {
                    removeFifth(usedChord4, usedChord5);
                }
            }
        }

        usedChord6 = usedChord6Copy;

        usedChord7 = usedChord7Copy;



        //Put notes in chords in ascending order
        usedChord1 = putNotesInVoicesInAscendingOrder(usedChord1);
        usedChord2 = putNotesInVoicesInAscendingOrder(usedChord2);
        usedChord3 = putNotesInVoicesInAscendingOrder(usedChord3);
        usedChord4 = putNotesInVoicesInAscendingOrder(usedChord4);
        usedChord5 = putNotesInVoicesInAscendingOrder(usedChord5);
        usedChord6 = putNotesInVoicesInAscendingOrder(usedChord6);
        usedChord7 = putNotesInVoicesInAscendingOrder(usedChord7);


        //Add the relevant parts of each chord to the different voices
        bottomBottomNotes.add(usedChord1[0]);
        bottomBottomNotes.add(usedChord2[0]);
        bottomBottomNotes.add(usedChord3[0]);
        bottomBottomNotes.add(usedChord4[0]);
        bottomBottomNotes.add(usedChord5[0]);
        bottomBottomNotes.add(usedChord6[0]);
        bottomBottomNotes.add(usedChord7[0]);

        bottomNotes.add(usedChord1[1]);
        bottomNotes.add(usedChord2[1]);
        bottomNotes.add(usedChord3[1]);
        bottomNotes.add(usedChord4[1]);
        bottomNotes.add(usedChord5[1]);
        bottomNotes.add(usedChord6[1]);
        bottomNotes.add(usedChord7[1]);

        middleNotes.add(usedChord1[2]);
        middleNotes.add(usedChord2[2]);
        middleNotes.add(usedChord3[2]);
        middleNotes.add(usedChord4[2]);
        middleNotes.add(usedChord5[2]);
        middleNotes.add(usedChord6[2]);
        middleNotes.add(usedChord7[2]);

        topNotes.add(usedChord1[3]);
        topNotes.add(usedChord2[3]);
        topNotes.add(usedChord3[3]);
        topNotes.add(usedChord4[3]);
        topNotes.add(usedChord5[3]);
        topNotes.add(usedChord6[3]);
        topNotes.add(usedChord7[3]);


        //Remove jumps of an octave or more
        bottomBottomNotes = removeOctaveJumpsAndHighNotes(bottomBottomNotes);
        bottomNotes = removeOctaveJumpsAndHighNotes(bottomNotes);
        middleNotes = removeOctaveJumpsAndHighNotes(middleNotes);
        topNotes = removeOctaveJumpsAndHighNotes(topNotes);


        //Set the scores
        Phrase bottomFirstNote = new Phrase(new Note[]{usedChord1[1], usedChord2[1], usedChord3[1]});
        Phrase middleFirstNote = new Phrase(new Note[]{usedChord1[2], usedChord2[2], usedChord3[2]});
        Phrase topFirstNote = new Phrase(new Note[]{usedChord1[3], usedChord2[3], usedChord3[3]});
        setScoreSpecific(bottomFirstNote, "left");
        setScoreSpecific(middleFirstNote, "middle");
        setScoreSpecific(topFirstNote, "right");


        //Add the selected chords to the CPhrases
        cphr1.addChord(usedChord1);
        cphr2.addChord(usedChord2);
        cphr3.addChord(usedChord3);
        cphr4.addChord(usedChord4);
        cphr5.addChord(usedChord5);
        cphr6.addChord(usedChord6);
        cphr7.addChord(usedChord7);


        //Add CPhrases to Part p
        p.addCPhrase(cphr1);
        p.addCPhrase(cphr2);
        p.addCPhrase(cphr3);
        p.addCPhrase(cphr4);
        p.addCPhrase(cphr5);
        p.addCPhrase(cphr6);
        p.addCPhrase(cphr7);


        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Modulation.mid");


        if(i3 == 0 || i3 == 2 || i3 == 4){
            return newKeyString + "m";
        } else {
            return newKeyString;
        }
    }


    private String modulateFromMinor(int i, int root){
        int leftOne = (i - 1) % 12;
        int rightOne = (i + 1) % 12;

        if (leftOne < 0) {
            leftOne += 12;
        }

        //Populate similarKeys array
        similarKeys[0] = getRelativeMajor(root);
        similarKeys[1] = circleOfFifthsMinor[leftOne];
        similarKeys[2] = getRelativeMajor(circleOfFifthsMinor[leftOne]);
        similarKeys[3] = circleOfFifthsMinor[rightOne];
        similarKeys[4] = getRelativeMajor(circleOfFifthsMinor[rightOne]);


        //1st Chord. Add tonic of original key
        usedChord1 = scaleChord1;


        //2nd Chord. Add IV or VI of original key
        int i2 = rn.nextInt(2);
        if(i2 == 0){
            usedChord2 = scaleChord4;
        } else {
            usedChord2 = scaleChord6;
        }


        //3rd Chord. Add II of original key
        usedChord3 = scaleChord2;

        //4th Chord. Add V of original key
        usedChord4 = scaleChord5MelodicMinor;

        //5th Chord. Add tonic of original key as common key
        usedChord5 = scaleChord1;

        //6th Chord. Add tonic of original key as common key
        usedChord6 = scaleChord1RemovedFifth;


        //Find key to modulate to from similar keys
        int i3 = rn.nextInt(5);
        int newKey = similarKeys[i3];
        String newKeyString = getNote(new Note(newKey, C));


        //Make the scale and chords of the new key
        if (i3 == 1 || i3 == 3) {
            minor = true;
            major = false;
            makeMinorScale(newKey);
        } else {
            major = true;
            minor = false;
            makeMajorScale(newKey);
        }


        //7th Chord. Add the 2nd inverted dominant of the new key
        usedChord7 = scaleChord5Inverted;

        //8th Chord. Add the dominant 7 of new key
        usedChord8 = scaleChord5;

        //9th Chord. Add the tonic of the new key
        usedChord9 = scaleChord1;


        //Make copies of the original chords
        for(int j = 0; j < usedChord1.length; j++){
            usedChord1Copy[j] = new Note(usedChord1[j].getPitch(), usedChord1[j].getRhythmValue());
            usedChord2Copy[j] = new Note(usedChord2[j].getPitch(), usedChord2[j].getRhythmValue());
            usedChord3Copy[j] = new Note(usedChord3[j].getPitch(), usedChord3[j].getRhythmValue());
            usedChord4Copy[j] = new Note(usedChord4[j].getPitch(), usedChord4[j].getRhythmValue());
            usedChord5Copy[j] = new Note(usedChord5[j].getPitch(), usedChord5[j].getRhythmValue());
            usedChord6Copy[j] = new Note(usedChord6[j].getPitch(), usedChord6[j].getRhythmValue());
            usedChord7Copy[j] = new Note(usedChord7[j].getPitch(), usedChord7[j].getRhythmValue());
            usedChord8Copy[j] = new Note(usedChord8[j].getPitch(), usedChord8[j].getRhythmValue());
            usedChord9Copy[j] = new Note(usedChord9[j].getPitch(), usedChord9[j].getRhythmValue());
        }


        //Rearrange voices to minimise movement
        chord2Changed = placeCommonNotesInSameVoice(usedChord1, usedChord2);
        chord3Changed = placeCommonNotesInSameVoice(usedChord2, usedChord3);
        chord4Changed = placeCommonNotesInSameVoice(usedChord3, usedChord4);
        chord5Changed = placeCommonNotesInSameVoice(usedChord4, usedChord5);
        chord6Changed = placeCommonNotesInSameVoice(usedChord5, usedChord6);
        chord7Changed = placeCommonNotesInSameVoice(usedChord6, usedChord7);
        chord8Changed = placeCommonNotesInSameVoice(usedChord7, usedChord8);
        chord9Changed = placeCommonNotesInSameVoice(usedChord8, usedChord9);


        //Put notes in chords in ascending order
//        usedChord1 = putNotesInVoicesInAscendingOrder(usedChord1);
//        usedChord2 = putNotesInVoicesInAscendingOrder(usedChord2);
//        usedChord3 = putNotesInVoicesInAscendingOrder(usedChord3);
//        usedChord4 = putNotesInVoicesInAscendingOrder(usedChord4);
//        usedChord5 = putNotesInVoicesInAscendingOrder(usedChord5);
//        usedChord6 = putNotesInVoicesInAscendingOrder(usedChord6);
//        usedChord7 = putNotesInVoicesInAscendingOrder(usedChord7);
//        usedChord8 = putNotesInVoicesInAscendingOrder(usedChord8);
//        usedChord9 = putNotesInVoicesInAscendingOrder(usedChord9);


        usedChord1 = usedChord1Copy;
        int removeFifth;

        //If chords not changed then use the copy
        if(!chord2Changed){
            usedChord2 = usedChord2Copy;

            removeFifth = rn.nextInt(2);
            if (removeFifth == 0) {
                removeFifth(usedChord1, usedChord2);
            }
        }

        if(!chord3Changed){
            usedChord3 = usedChord3Copy;

            if(!chord2Changed) {
                removeFifth = rn.nextInt(2);
                if(removeFifth == 0){
                    removeFifth(usedChord2, usedChord3);
                }
            }
        }

        if(!chord4Changed){
            usedChord4 = usedChord4Copy;

            if(!chord3Changed) {
                removeFifth = rn.nextInt(2);
                if (removeFifth == 0) {
                    removeFifth(usedChord3, usedChord4);
                }
            }
        }

        if(!chord5Changed){
            usedChord5 = usedChord5Copy;

            if(!chord4Changed) {
                removeFifth = rn.nextInt(2);
                if (removeFifth == 0) {
                    removeFifth(usedChord4, usedChord5);
                }
            }
        }

        if(!chord6Changed){
            usedChord6 = usedChord6Copy;

//            if(!chord5Changed) {
//                removeFifth = rn.nextInt(2);
//                if (removeFifth == 0) {
//                    removeFifth(usedChord5, usedChord6);
//                }
//            }
        }

        if(!chord7Changed){
            usedChord7 = usedChord7Copy;

            if(!chord6Changed) {
                removeFifth = rn.nextInt(2);
                if (removeFifth == 0) {
                    removeFifth(usedChord6, usedChord7);
                }
            }
        }

        usedChord8 = usedChord8Copy;

        usedChord9 = usedChord9Copy;



        //Put notes in chords in ascending order
        usedChord1 = putNotesInVoicesInAscendingOrder(usedChord1);
        usedChord2 = putNotesInVoicesInAscendingOrder(usedChord2);
        usedChord3 = putNotesInVoicesInAscendingOrder(usedChord3);
        usedChord4 = putNotesInVoicesInAscendingOrder(usedChord4);
        usedChord5 = putNotesInVoicesInAscendingOrder(usedChord5);
        usedChord6 = putNotesInVoicesInAscendingOrder(usedChord6);
        usedChord7 = putNotesInVoicesInAscendingOrder(usedChord7);
        usedChord8 = putNotesInVoicesInAscendingOrder(usedChord8);
        usedChord9 = putNotesInVoicesInAscendingOrder(usedChord9);


        //Add the relevant parts of each chord to the different voices
        bottomBottomNotes.add(usedChord1[0]);
        bottomBottomNotes.add(usedChord2[0]);
        bottomBottomNotes.add(usedChord3[0]);
        bottomBottomNotes.add(usedChord4[0]);
        bottomBottomNotes.add(usedChord5[0]);
        bottomBottomNotes.add(usedChord6[0]);
        bottomBottomNotes.add(usedChord7[0]);
        bottomBottomNotes.add(usedChord8[0]);
        bottomBottomNotes.add(usedChord9[0]);

        bottomNotes.add(usedChord1[1]);
        bottomNotes.add(usedChord2[1]);
        bottomNotes.add(usedChord3[1]);
        bottomNotes.add(usedChord4[1]);
        bottomNotes.add(usedChord5[1]);
        bottomNotes.add(usedChord6[1]);
        bottomNotes.add(usedChord7[1]);
        bottomNotes.add(usedChord8[1]);
        bottomNotes.add(usedChord9[1]);

        middleNotes.add(usedChord1[2]);
        middleNotes.add(usedChord2[2]);
        middleNotes.add(usedChord3[2]);
        middleNotes.add(usedChord4[2]);
        middleNotes.add(usedChord5[2]);
        middleNotes.add(usedChord6[2]);
        middleNotes.add(usedChord7[2]);
        middleNotes.add(usedChord8[2]);
        middleNotes.add(usedChord9[2]);

        topNotes.add(usedChord1[3]);
        topNotes.add(usedChord2[3]);
        topNotes.add(usedChord3[3]);
        topNotes.add(usedChord4[3]);
        topNotes.add(usedChord5[3]);
        topNotes.add(usedChord6[3]);
        topNotes.add(usedChord7[3]);
        topNotes.add(usedChord8[3]);
        topNotes.add(usedChord9[3]);


        //Remove jumps of an octave or more
        bottomBottomNotes = removeOctaveJumpsAndHighNotes(bottomBottomNotes);
        bottomNotes = removeOctaveJumpsAndHighNotes(bottomNotes);
        middleNotes = removeOctaveJumpsAndHighNotes(middleNotes);
        topNotes = removeOctaveJumpsAndHighNotes(topNotes);


        //Set the scores
        Phrase bottomFirstNote = new Phrase(new Note[]{usedChord1[1], usedChord2[1], usedChord3[1], usedChord4[1], usedChord5[1]});
        Phrase middleFirstNote = new Phrase(new Note[]{usedChord1[2], usedChord2[2], usedChord3[2], usedChord4[2], usedChord5[2]});
        Phrase topFirstNote = new Phrase(new Note[]{usedChord1[3], usedChord2[3], usedChord3[3], usedChord4[3], usedChord5[3]});
        setScoreSpecific(bottomFirstNote, "left");
        setScoreSpecific(middleFirstNote, "middle");
        setScoreSpecific(topFirstNote, "right");


        //Add the selected chords to the CPhrases
        cphr1.addChord(usedChord1);
        cphr2.addChord(usedChord2);
        cphr3.addChord(usedChord3);
        cphr4.addChord(usedChord4);
        cphr5.addChord(usedChord5);
        cphr6.addChord(usedChord6);
        cphr7.addChord(usedChord7);
        cphr8.addChord(usedChord8);
        cphr9.addChord(usedChord9);


        //Add CPhrases to Part p
        p.addCPhrase(cphr1);
        p.addCPhrase(cphr2);
        p.addCPhrase(cphr3);
        p.addCPhrase(cphr4);
        p.addCPhrase(cphr5);
        p.addCPhrase(cphr6);
        p.addCPhrase(cphr7);
        p.addCPhrase(cphr8);
        p.addCPhrase(cphr9);


        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Modulation.mid");

        if(i3 == 1 || i3 == 3){
            return newKeyString + "m";
        } else {
            return newKeyString;
        }
    }


    private String makeMIDIEasyModulation(){
        int i = rn.nextInt(12);
        int root;

        rootMinor = false;
        rootMajor = false;

        int minorOrMajor = rn.nextInt(2);

        if(minorOrMajor == 0) {
            rootMinor = true;
            rootMajor = false;
            root = circleOfFifthsMinor[i];
            makeMinorScale(root);
            rootKeyString = getNote(new Note(root, C));
            rootKeyString += "m";
            return modulateFromMinor(i, root);
        } else {
            rootMajor = true;
            rootMinor = false;
            root = circleOfFifthsMajor[i];
            makeMajorScale(root);
            rootKeyString = getNote(new Note(root, C));
            return modulateFromMajor(i, root);
        }
    }


    private String makeMIDIMediumModulation(){
        return makeMIDIEasyModulation();
    }


    private String makeMIDIHardModulation(){
        return makeMIDIEasyModulation();
    }


    @Override
    @FXML
    protected void generateQuestion() throws IOException, MidiUnavailableException, InvalidMidiDataException {
        Button[] buttons = {similarKey0Button, similarKey1Button, similarKey2Button, similarKey3Button, similarKey4Button};
        phr1 = new Phrase();
        phr2 = new Phrase();
        bottomNotes = new Phrase();
        middleNotes = new Phrase();
        topNotes = new Phrase();
        cphr1 = new CPhrase();
        cphr2 = new CPhrase();
        cphr3 = new CPhrase();
        cphr4 = new CPhrase();
        cphr5 = new CPhrase();
        cphr6 = new CPhrase();
        cphr7 = new CPhrase();
        cphr8 = new CPhrase();
        cphr9 = new CPhrase();
        p = new Part();
        s = new Score();


        if(easyRadioButton.isSelected()){
            correctAnswer = makeMIDIEasyModulation();
        } else if(mediumRadioButton.isSelected()){
            correctAnswer = makeMIDIMediumModulation();
        } else if(hardRadioButton.isSelected()){
            correctAnswer = makeMIDIHardModulation();
        }

        rootKeyLabel.setVisible(true);
        rootKeyLabel.setText("Root key: " + rootKeyString);

        System.out.println("Root key: " + rootKeyString);
        System.out.println("New key: " + correctAnswer);

        String similarKey0Text;
        String similarKey1Text;
        String similarKey2Text;
        String similarKey3Text;
        String similarKey4Text;


        if (rootMajor && !rootMinor) {
            similarKey0Label.setText("Root Key Relative Minor:");
            similarKey0Text = getNote(new Note(similarKeys[0], 1.0)) + "m";
            similarKey1Label.setText("Sub Dominant:");
            similarKey1Text = getNote(new Note(similarKeys[1], 1.0));
            similarKey2Label.setText("Sub Dominant Relative Minor:");
            similarKey2Text = getNote(new Note(similarKeys[2], 1.0)) + "m";
            similarKey3Label.setText("Dominant:");
            similarKey3Text = getNote(new Note(similarKeys[3], 1.0));
            similarKey4Label.setText("Dominant Relative Minor:");
            similarKey4Text = getNote(new Note(similarKeys[4], 1.0)) + "m";
        } else {
            similarKey0Label.setText("Root Key Relative Major:");
            similarKey0Text = getNote(new Note(similarKeys[0], 1.0));
            similarKey1Label.setText("Sub Dominant:");
            similarKey1Text = getNote(new Note(similarKeys[1], 1.0)) + "m";
            similarKey2Label.setText("Sub Dominant Relative Major:");
            similarKey2Text = getNote(new Note(similarKeys[2], 1.0));
            similarKey3Label.setText("Dominant:");
            similarKey3Text = getNote(new Note(similarKeys[3], 1.0)) + "m";
            similarKey4Label.setText("Dominant Relative Major:");
            similarKey4Text = getNote(new Note(similarKeys[4], 1.0));
        }

        similarKey0Button.setText(similarKey0Text);
        similarKey1Button.setText(similarKey1Text);
        similarKey2Button.setText(similarKey2Text);
        similarKey3Button.setText(similarKey3Text);
        similarKey4Button.setText(similarKey4Text);


        correctButton = getCorrectButton(correctAnswer);
        System.out.println("Correct button: " + correctButton.getText());

        for(Button button : buttons){
            button.setDisable(false);
        }

        if(easyRadioButton.isSelected()){
            int numberToDisable = 2;

            do {
                int i = rn.nextInt(5);
                if(!buttons[i].equals(correctButton) && !buttons[i].isDisabled()){
                    buttons[i].setDisable(true);
                    numberToDisable--;
                }
            } while(numberToDisable != 0);

        } else if(mediumRadioButton.isSelected()){
            int numberToDisable = 1;

            do {
                int i = rn.nextInt(5);
                if(!buttons[i].equals(correctButton) && !buttons[i].isDisabled()){
                    buttons[i].setDisable(true);
                    numberToDisable--;
                }
            } while(numberToDisable != 0);
        }


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


    private void setScoreSpecific(Phrase phr, String score) {
        JGrandStave scoreToUse;

        if(score.equals("left")){
            scoreToUse = jScoreBottom;
        } else if(score.equals("middle")){
            scoreToUse = jScore;
        } else {
            scoreToUse = jScoreTop;
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




