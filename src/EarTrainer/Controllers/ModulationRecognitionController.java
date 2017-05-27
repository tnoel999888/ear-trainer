package EarTrainer.Controllers;

import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import jm.gui.cpn.JGrandStave;
import jm.music.data.*;
import jm.util.Write;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import static jm.constants.RhythmValues.*;


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

    private int[] similarKeys = new int[5];

    private String rootKeyString;




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


    private String getRootKeyString(){
        if(minor){
            rootKeyString += "m";
        }

        return rootKeyString;
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
        bottomNotes.add(scaleChord1[0]);
        middleNotes.add(scaleChord1[1]);
        topNotes.add(scaleChord1[2]);
        usedChord1 = scaleChord1;


        //2nd Chord. Add random chord of original key
        int i2 = rn.nextInt(7);
        bottomNotes.add(scaleChords[i2][0]);
        middleNotes.add(scaleChords[i2][1]);
        topNotes.add(scaleChords[i2][2]);
        usedChord2 = scaleChords[i2];


        //Find key to modulate to from similar keys
        int i3 = rn.nextInt(5);
        int newKey = similarKeys[i3];
        String newKeyString = getNote(new Note(newKey, C));


        //Make the scale and chords of the new key
        if (i3 == 0 || i3 == 2 || i3 == 4) {
            minor = true;
            makeMinorScale(newKey);
        } else {
            major = true;
            makeMajorScale(newKey);
        }


        //3rd Chord. Add a common chord to begin the modulation
        Note[][] newKeyChords = scaleChords;
        ArrayList commonChords = findCommonChords(rootKeyChords, newKeyChords);

        if (commonChords.contains(newKeyChords[1])) {
            bottomNotes.add(scaleChord2[0]);
            middleNotes.add(scaleChord2[1]);
            topNotes.add(scaleChord2[2]);
            usedChord3 = scaleChord2;
        } else if (commonChords.contains(newKeyChords[3])) {
            bottomNotes.add(scaleChord4[0]);
            middleNotes.add(scaleChord4[1]);
            topNotes.add(scaleChord4[2]);
            usedChord3 = scaleChord4;
        } else {
            int i4 = rn.nextInt(commonChords.size());
            bottomNotes.add(((Note[])commonChords.get(i4))[0]);
            middleNotes.add(((Note[])commonChords.get(i4))[1]);
            topNotes.add(((Note[])commonChords.get(i4))[2]);
            usedChord3 = (Note[])commonChords.get(i4);
        }


        //4th Chord. Add the dominant of the new key
        if(minor) {
            bottomNotes.add(scaleChord5MelodicMinor[0]);
            middleNotes.add(scaleChord5MelodicMinor[1]);
            topNotes.add(scaleChord5MelodicMinor[2]);
            usedChord4 = scaleChord5MelodicMinor;
        } else {
            bottomNotes.add(scaleChord5[0]);
            middleNotes.add(scaleChord5[1]);
            topNotes.add(scaleChord5[2]);
            usedChord4 = scaleChord5;
        }


        //5th Chord. Add the tonic of the new key
        bottomNotes.add(scaleChord1[0]);
        middleNotes.add(scaleChord1[1]);
        topNotes.add(scaleChord1[2]);
        usedChord5 = scaleChord1;


        //6th Chord. Add the dominant 7 of new key
        bottomNotes.add(scaleChord5Inverted[0]);
        middleNotes.add(scaleChord5Inverted[1]);
        topNotes.add(scaleChord5Inverted[2]);
        usedChord6 = scaleChord5Inverted;


        //7th Chord. Add the dominant 7 of new key
        bottomNotes.add(scaleChord1[0]);
        middleNotes.add(scaleChord1[1]);
        topNotes.add(scaleChord1[2]);
        usedChord7 = scaleChord1;


        //Set the scores
        setScoreSpecific(bottomNotes, "left");
        setScoreSpecific(middleNotes, "middle");
        setScoreSpecific(topNotes, "right");


        //Rearrange voices to minimise movement
        placeCommonNotesInSameVoice(usedChord1, usedChord2);
        placeCommonNotesInSameVoice(usedChord2, usedChord3);
        placeCommonNotesInSameVoice(usedChord3, usedChord4);
        placeCommonNotesInSameVoice(usedChord4, usedChord5);
        placeCommonNotesInSameVoice(usedChord5, usedChord6);
        placeCommonNotesInSameVoice(usedChord6, usedChord7);


        //Add the selected chords to the CPhrases
        cphr1.addChord(usedChord1);
        cphr2.addChord(usedChord2);
        cphr3.addChord(usedChord3);
        cphr4.addChord(usedChord4);
        cphr5.addChord(usedChord5);
        cphr5.addChord(usedChord6);
        cphr5.addChord(usedChord7);


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
        bottomNotes.add(scaleChord1[0]);
        middleNotes.add(scaleChord1[1]);
        topNotes.add(scaleChord1[2]);
        usedChord1 = scaleChord1;


        //2nd Chord. Add IV or VI of original key
        int i2 = rn.nextInt(2);
        if(i2 == 0){
            bottomNotes.add(scaleChord4[0]);
            middleNotes.add(scaleChord4[1]);
            topNotes.add(scaleChord4[2]);
            usedChord2 = scaleChord4;
        } else {
            bottomNotes.add(scaleChord6[0]);
            middleNotes.add(scaleChord6[1]);
            topNotes.add(scaleChord6[2]);
            usedChord2 = scaleChord6;
        }


        //3rd Chord. Add II of original key
        bottomNotes.add(scaleChord2[0]);
        middleNotes.add(scaleChord2[1]);
        topNotes.add(scaleChord2[2]);
        usedChord3 = scaleChord2;


        //4th Chord. Add V of original key
        bottomNotes.add(scaleChord5MelodicMinor[0]);
        middleNotes.add(scaleChord5MelodicMinor[1]);
        topNotes.add(scaleChord5MelodicMinor[2]);
        usedChord4 = scaleChord5MelodicMinor;


        //5th Chord. Add tonic of original key as common key
        bottomNotes.add(scaleChord1[0]);
        middleNotes.add(scaleChord1[1]);
        topNotes.add(scaleChord1[2]);
        usedChord5 = scaleChord1;


        //6th Chord. Add tonic of original key as common key
        bottomNotes.add(scaleChord1RemovedFifth[0]);
        middleNotes.add(scaleChord1RemovedFifth[1]);
        topNotes.add(scaleChord1RemovedFifth[2]);
        usedChord6 = scaleChord1RemovedFifth;


        //Find key to modulate to from similar keys
        int i3 = rn.nextInt(5);
        int newKey = similarKeys[i3];
        String newKeyString = getNote(new Note(newKey, C));


        //Make the scale and chords of the new key
        if (i3 == 1 || i3 == 3) {
            makeMinorScale(newKey);
        } else {
            makeMajorScale(newKey);
        }


        //7th Chord. Add the 2nd inverted dominant of the new key
        bottomNotes.add(scaleChord5Inverted[0]);
        middleNotes.add(scaleChord5Inverted[1]);
        topNotes.add(scaleChord5Inverted[2]);
        usedChord7 = scaleChord5Inverted;


        //8th Chord. Add the dominant 7 of new key
        bottomNotes.add(scaleChord5Seventh[0]);
        middleNotes.add(scaleChord5Seventh[1]);
        topNotes.add(scaleChord5Seventh[2]);
        usedChord8 = scaleChord5Seventh;


        //9th Chord. Add the tonic of the new key
        bottomNotes.add(scaleChord1[0]);
        middleNotes.add(scaleChord1[1]);
        topNotes.add(scaleChord1[2]);
        usedChord9 = scaleChord1;


        //Set the scores
        setScoreSpecific(bottomNotes, "left");
        setScoreSpecific(middleNotes, "middle");
        setScoreSpecific(topNotes, "right");


        //Rearrange voices to minimise movement
        placeCommonNotesInSameVoice(usedChord1, usedChord2);
        placeCommonNotesInSameVoice(usedChord2, usedChord3);
        placeCommonNotesInSameVoice(usedChord3, usedChord4);
        placeCommonNotesInSameVoice(usedChord4, usedChord5);
//        placeCommonNotesInSameVoice(usedChord5, usedChord6);
        placeCommonNotesInSameVoice(usedChord6, usedChord7);
        placeCommonNotesInSameVoice(usedChord7, usedChord8);
        placeCommonNotesInSameVoice(usedChord8, usedChord9);


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

        minor = false;
        major = false;

        int minorOrMajor = rn.nextInt(2);

        if(minorOrMajor == 0) {
            minor = true;
            root = circleOfFifthsMinor[i];
            makeMinorScale(root);
            rootKeyString = getNote(new Note(root, C));
            return modulateFromMinor(i, root);
        } else {
            major = true;
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
        rootKeyLabel.setText("Root key: " + getRootKeyString());

        System.out.println("New key: " + correctAnswer);

        String similarKey0Text;
        String similarKey1Text;
        String similarKey2Text;
        String similarKey3Text;
        String similarKey4Text;

        if(major) {
            System.out.println("major");
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
            System.out.println("minor");
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




