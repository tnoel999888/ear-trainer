package EarTrainer.Controllers;

import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import jm.gui.cpn.JGrandStave;
import jm.music.data.*;
import jm.util.Write;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import java.awt.*;
import java.io.*;


public class CadenceRecognitionController extends AbstractController{

    @FXML private Button perfectButton;
    @FXML private Button interruptiveButton;
    @FXML private Button imperfectButton;
    @FXML private Button plagalButton;

    @FXML private Pane scorePaneLeft;
    @FXML private Pane scorePaneRight;

    private boolean chord2Changed;
    private boolean chord3Changed;
    private boolean chord4Changed;

    private Note[] usedChord1Copy = new Note[4];
    private Note[] usedChord2Copy = new Note[4];
    private Note[] usedChord3Copy = new Note[4];
    private Note[] usedChord4Copy = new Note[4];





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
            answerButtonClicked();
            checkAnswer("perfect", perfectButton);
        }
    }


    @FXML
    private void interruptiveButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer("interruptive", interruptiveButton);
        }
    }


    @FXML
    private void imperfectButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer("imperfect", imperfectButton);
        }
    }


    @FXML
    private void plagalButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer("plagal", plagalButton);
        }
    }


    @Override
    @FXML
    void answerButtonClicked() throws IOException {
        questionAnswered = true;
        nextQuestionButton.setDisable(false);

        //Set the scores
//        setScoreSpecific(bottomNotes, "left");
//        setScoreSpecific(middleNotes, "middle");
//        setScoreSpecific(topNotes, "right");
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


    private void makePerfect(){

        //1st Chord. Add I chord
        usedChord1 = scaleChord1.clone();


        //2nd Chord. Add II or IV chord
        int IIOrIVChord = rn.nextInt(2);
        if (IIOrIVChord == 0) {
            usedChord2 = scaleChord2.clone();

        } else {
            usedChord2 = scaleChord4.clone();
        }


        //3rd Chord. Add V major chord if in minor scale
        if (minor) {
            usedChord3 = scaleChord5MelodicMinor.clone();
        } else {
            usedChord3 = scaleChord5.clone();
        }


        //4th Chord. Add I chord
        usedChord4 = scaleChord1.clone();



        for(int i = 0; i < usedChord1.length; i++){
            usedChord1Copy[i] = new Note(usedChord1[i].getPitch(), usedChord1[i].getRhythmValue());
            usedChord2Copy[i] = new Note(usedChord2[i].getPitch(), usedChord2[i].getRhythmValue());
            usedChord3Copy[i] = new Note(usedChord3[i].getPitch(), usedChord3[i].getRhythmValue());
            usedChord4Copy[i] = new Note(usedChord4[i].getPitch(), usedChord4[i].getRhythmValue());
        }


        //Rearrange voices to minimise movement
        System.out.println("2:");
        chord2Changed = placeCommonNotesInSameVoice(usedChord1, usedChord2);

        System.out.println("3:");
        chord3Changed = placeCommonNotesInSameVoice(usedChord2, usedChord3);

        System.out.println("4:");
        chord4Changed = placeCommonNotesInSameVoice(usedChord3, usedChord4);



        usedChord1 = usedChord1Copy;


        if(!chord2Changed){
            usedChord2 = usedChord2Copy;

            int removeFifth = rn.nextInt(2);
            if(removeFifth == 0){
                removeFifth(usedChord2);
            }
        }


        if(!chord3Changed){
            usedChord3 = usedChord3Copy;

            int removeFifth = rn.nextInt(2);
            if(removeFifth == 0){
                removeFifth(usedChord3);
            }
        }


        if(!chord4Changed){
            usedChord4 = usedChord4Copy;

            int removeFifth = rn.nextInt(2);
            if(removeFifth == 0){
                removeFifth(usedChord4);
            }
        }


        usedChord1 = putNotesInVoicesInAscendingOrder(usedChord1);
        usedChord2 = putNotesInVoicesInAscendingOrder(usedChord2);
        usedChord3 = putNotesInVoicesInAscendingOrder(usedChord3);
        usedChord4 = putNotesInVoicesInAscendingOrder(usedChord4);


        //Populate voice arrays with respective parts of chords
        bottomBottomNotesArray[0] = usedChord1[0];
        bottomNotesArray[0] = usedChord1[1];
        middleNotesArray[0] = usedChord1[2];
        topNotesArray[0] = usedChord1[3];

        bottomBottomNotesArray[1] = usedChord2[0];
        bottomNotesArray[1] = usedChord2[1];
        middleNotesArray[1] = usedChord2[2];
        topNotesArray[1] = usedChord2[3];

        bottomBottomNotesArray[2] = usedChord3[0];
        bottomNotesArray[2] = usedChord3[1];
        middleNotesArray[2] = usedChord3[2];
        topNotesArray[2] = usedChord3[3];

        bottomBottomNotesArray[3] = usedChord4[0];
        bottomNotesArray[3] = usedChord4[1];
        middleNotesArray[3] = usedChord4[2];
        topNotesArray[3] = usedChord4[3];


        //Add note arrays to the phrases
        bottomBottomNotes.addNoteList(bottomBottomNotesArray);
        bottomNotes.addNoteList(bottomNotesArray);
        middleNotes.addNoteList(middleNotesArray);
        topNotes.addNoteList(topNotesArray);


        //Remove jumps of an octave or more in each voice
        bottomBottomNotes = removeOctaveJumps(bottomBottomNotes);
        bottomNotes = removeOctaveJumps(bottomNotes);
        middleNotes = removeOctaveJumps(middleNotes);
        topNotes = removeOctaveJumps(topNotes);


        //Set the scores
        setScoreSpecific(bottomNotes, "left");
        setScoreSpecific(middleNotes, "middle");
        setScoreSpecific(topNotes, "right");


        //Add the selected chords to the CPhrases
        cphr1.addChord(usedChord1);
        cphr2.addChord(usedChord2);
        cphr3.addChord(usedChord3);
        cphr4.addChord(usedChord4);


        //Add CPhrases to Part p
        p.addCPhrase(cphr1);
        p.addCPhrase(cphr2);
        p.addCPhrase(cphr3);
        p.addCPhrase(cphr4);


        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Cadence.mid");

        minor = false;
        major = false;
    }


    private void makeInterruptive(){

        //1st Chord. Add I chord
        usedChord1 = scaleChord1.clone();

        //2nd Chord. Add II or IV chord
        int IIOrIVChord = rn.nextInt(2);
        if (IIOrIVChord == 0) {
            usedChord2 = scaleChord2.clone();
        } else {
            usedChord2 = scaleChord4.clone();
        }

        //3rd Chord. Add V chord
        usedChord3 = scaleChord5.clone();

        //4th Chord. Add VI chord
        usedChord4 = scaleChord6.clone();



        for(int i = 0; i < usedChord1.length; i++){
            usedChord1Copy[i] = new Note(usedChord1[i].getPitch(), usedChord1[i].getRhythmValue());
            usedChord2Copy[i] = new Note(usedChord2[i].getPitch(), usedChord2[i].getRhythmValue());
            usedChord3Copy[i] = new Note(usedChord3[i].getPitch(), usedChord3[i].getRhythmValue());
            usedChord4Copy[i] = new Note(usedChord4[i].getPitch(), usedChord4[i].getRhythmValue());
        }


        //Rearrange voices to minimise movement
        System.out.println("2:");
        chord2Changed = placeCommonNotesInSameVoice(usedChord1, usedChord2);

        System.out.println("3:");
        chord3Changed = placeCommonNotesInSameVoice(usedChord2, usedChord3);

        System.out.println("4:");
        chord4Changed = placeCommonNotesInSameVoice(usedChord3, usedChord4);


        usedChord1 = usedChord1Copy;

        if(!chord2Changed){
            usedChord2 = usedChord2Copy;

            int removeFifth = rn.nextInt(2);
            if(removeFifth == 0){
                removeFifth(usedChord2);
            }
        }

        if(!chord3Changed){
            usedChord3 = usedChord3Copy;

            int removeFifth = rn.nextInt(2);
            if(removeFifth == 0){
                removeFifth(usedChord3);
            }
        }

        if(!chord4Changed){
            usedChord4 = usedChord4Copy;

            int removeFifth = rn.nextInt(2);
            if(removeFifth == 0){
                removeFifth(usedChord4);
            }
        }


        usedChord1 = putNotesInVoicesInAscendingOrder(usedChord1);
        usedChord2 = putNotesInVoicesInAscendingOrder(usedChord2);
        usedChord3 = putNotesInVoicesInAscendingOrder(usedChord3);
        usedChord4 = putNotesInVoicesInAscendingOrder(usedChord4);


        //Populate voice arrays with respective parts of chords
        bottomBottomNotesArray[0] = usedChord1[0];
        bottomNotesArray[0] = usedChord1[1];
        middleNotesArray[0] = usedChord1[2];
        topNotesArray[0] = usedChord1[3];

        bottomBottomNotesArray[1] = usedChord2[0];
        bottomNotesArray[1] = usedChord2[1];
        middleNotesArray[1] = usedChord2[2];
        topNotesArray[1] = usedChord2[3];

        bottomBottomNotesArray[2] = usedChord3[0];
        bottomNotesArray[2] = usedChord3[1];
        middleNotesArray[2] = usedChord3[2];
        topNotesArray[2] = usedChord3[3];

        bottomBottomNotesArray[3] = usedChord4[0];
        bottomNotesArray[3] = usedChord4[1];
        middleNotesArray[3] = usedChord4[2];
        topNotesArray[3] = usedChord4[3];


        //Add note arrays to the phrases
        bottomBottomNotes.addNoteList(bottomBottomNotesArray);
        bottomNotes.addNoteList(bottomNotesArray);
        middleNotes.addNoteList(middleNotesArray);
        topNotes.addNoteList(topNotesArray);


        //Remove jumps of an octave or more in each voice
        bottomBottomNotes = removeOctaveJumps(bottomBottomNotes);
        bottomNotes = removeOctaveJumps(bottomNotes);
        middleNotes = removeOctaveJumps(middleNotes);
        topNotes = removeOctaveJumps(topNotes);


        //Set the scores
        setScoreSpecific(bottomNotes, "left");
        setScoreSpecific(middleNotes, "middle");
        setScoreSpecific(topNotes, "right");


        //Add the selected chords to the CPhrases
        cphr1.addChord(usedChord1);
        cphr2.addChord(usedChord2);
        cphr3.addChord(usedChord3);
        cphr4.addChord(usedChord4);


        //Add CPhrases to Part p
        p.addCPhrase(cphr1);
        p.addCPhrase(cphr2);
        p.addCPhrase(cphr3);
        p.addCPhrase(cphr4);


        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Cadence.mid");

        minor = false;
        major = false;
    }


    private void makeImperfect(){

        int i3 = rn.nextInt(7);

        //Don't let chord 3 be a V chord
        while(i3 == 4){
            i3 = rn.nextInt(7);
        }


        //1st Chord. Add I chord
        usedChord1 = scaleChord1;

        //2nd Chord. Add II or IV chord
        int IIOrIVChord = rn.nextInt(2);
        if (IIOrIVChord == 0) {
            usedChord2 = scaleChord2;
        } else {
            usedChord2 = scaleChord4;
        }

        //3rd Chord. Add random chord (not V)
        usedChord3 = scaleChords[i3];

        //4th Chord. Add V chord
        usedChord4 = scaleChord5;


        for(int i = 0; i < usedChord1.length; i++){
            usedChord1Copy[i] = new Note(usedChord1[i].getPitch(), usedChord1[i].getRhythmValue());
            usedChord2Copy[i] = new Note(usedChord2[i].getPitch(), usedChord2[i].getRhythmValue());
            usedChord3Copy[i] = new Note(usedChord3[i].getPitch(), usedChord3[i].getRhythmValue());
            usedChord4Copy[i] = new Note(usedChord4[i].getPitch(), usedChord4[i].getRhythmValue());
        }


        //Rearrange voices to minimise movement
        System.out.println("1");
        placeCommonNotesInSameVoice(usedChord1, usedChord2);

        System.out.println("2");
        placeCommonNotesInSameVoice(usedChord2, usedChord3);

        System.out.println("3");
        placeCommonNotesInSameVoice(usedChord3, usedChord4);


        usedChord1 = usedChord1Copy;

        if(!chord2Changed){
            usedChord2 = usedChord2Copy;

            int removeFifth = rn.nextInt(2);
            if(removeFifth == 0){
                removeFifth(usedChord2);
            }
        }

        if(!chord3Changed){
            usedChord3 = usedChord3Copy;

            int removeFifth = rn.nextInt(2);
            if(removeFifth == 0){
                removeFifth(usedChord3);
            }
        }

        if(!chord4Changed){
            usedChord4 = usedChord4Copy;

            int removeFifth = rn.nextInt(2);
            if(removeFifth == 0){
                removeFifth(usedChord4);
            }
        }


        usedChord1 = putNotesInVoicesInAscendingOrder(usedChord1);
        usedChord2 = putNotesInVoicesInAscendingOrder(usedChord2);
        usedChord3 = putNotesInVoicesInAscendingOrder(usedChord3);
        usedChord4 = putNotesInVoicesInAscendingOrder(usedChord4);


        //Populate voice arrays with respective parts of chords
        bottomBottomNotesArray[0] = usedChord1[0];
        bottomNotesArray[0] = usedChord1[1];
        middleNotesArray[0] = usedChord1[2];
        topNotesArray[0] = usedChord1[3];

        bottomBottomNotesArray[1] = usedChord2[0];
        bottomNotesArray[1] = usedChord2[1];
        middleNotesArray[1] = usedChord2[2];
        topNotesArray[1] = usedChord2[3];

        bottomBottomNotesArray[2] = usedChord3[0];
        bottomNotesArray[2] = usedChord3[1];
        middleNotesArray[2] = usedChord3[2];
        topNotesArray[2] = usedChord3[3];

        bottomBottomNotesArray[3] = usedChord4[0];
        bottomNotesArray[3] = usedChord4[1];
        middleNotesArray[3] = usedChord4[2];
        topNotesArray[3] = usedChord4[3];


        //Add note arrays to the phrases
        bottomBottomNotes.addNoteList(bottomBottomNotesArray);
        bottomNotes.addNoteList(bottomNotesArray);
        middleNotes.addNoteList(middleNotesArray);
        topNotes.addNoteList(topNotesArray);


        //Remove jumps of an octave or more in each voice
        bottomBottomNotes = removeOctaveJumps(bottomBottomNotes);
        bottomNotes = removeOctaveJumps(bottomNotes);
        middleNotes = removeOctaveJumps(middleNotes);
        topNotes = removeOctaveJumps(topNotes);


        //Set the scores
        setScoreSpecific(bottomNotes, "left");
        setScoreSpecific(middleNotes, "middle");
        setScoreSpecific(topNotes, "right");


        //Add the selected chords to the CPhrases
        cphr1.addChord(usedChord1);
        cphr2.addChord(usedChord2);
        cphr3.addChord(usedChord3);
        cphr4.addChord(usedChord4);


        //Add CPhrases to Part p
        p.addCPhrase(cphr1);
        p.addCPhrase(cphr2);
        p.addCPhrase(cphr3);
        p.addCPhrase(cphr4);


        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Cadence.mid");

        minor = false;
        major = false;
    }


    private void makePlagal(){

        int i2 = rn.nextInt(7);

        //Don't let chord 2 be a IV chord
        while(i2 == 3){
            i2 = rn.nextInt(7);
        }


        //1st Chord. Add I chord
        usedChord1 = scaleChord1;

        //2nd Chord. Add random chord, (6, 1 1st Inversion)
        usedChord2 = scaleChords[i2];

        //3rd Chord. Add IV chord
        usedChord3 = scaleChord4;

        //4th Chord. Add I chord
        usedChord4 = scaleChord1;


        for(int i = 0; i < usedChord1.length; i++){
            usedChord1Copy[i] = new Note(usedChord1[i].getPitch(), usedChord1[i].getRhythmValue());
            usedChord2Copy[i] = new Note(usedChord2[i].getPitch(), usedChord2[i].getRhythmValue());
            usedChord3Copy[i] = new Note(usedChord3[i].getPitch(), usedChord3[i].getRhythmValue());
            usedChord4Copy[i] = new Note(usedChord4[i].getPitch(), usedChord4[i].getRhythmValue());
        }


        //Rearrange voices to minimise movement
        System.out.println("1");
        placeCommonNotesInSameVoice(usedChord1, usedChord2);

        System.out.println("2");
        placeCommonNotesInSameVoice(usedChord2, usedChord3);

        System.out.println("3");
        placeCommonNotesInSameVoice(usedChord3, usedChord4);


        usedChord1 = usedChord1Copy;

        if(!chord2Changed){
            usedChord2 = usedChord2Copy;

            int removeFifth = rn.nextInt(2);
            if(removeFifth == 0){
                removeFifth(usedChord2);
            }
        }

        if(!chord3Changed){
            usedChord3 = usedChord3Copy;

            int removeFifth = rn.nextInt(2);
            if(removeFifth == 0){
                removeFifth(usedChord3);
            }
        }

        if(!chord4Changed){
            usedChord4 = usedChord4Copy;

            int removeFifth = rn.nextInt(2);
            if(removeFifth == 0){
                removeFifth(usedChord4);
            }
        }


        usedChord1 = putNotesInVoicesInAscendingOrder(usedChord1);
        usedChord2 = putNotesInVoicesInAscendingOrder(usedChord2);
        usedChord3 = putNotesInVoicesInAscendingOrder(usedChord3);
        usedChord4 = putNotesInVoicesInAscendingOrder(usedChord4);


        //Populate voice arrays with respective parts of chords
        bottomBottomNotesArray[0] = usedChord1[0];
        bottomNotesArray[0] = usedChord1[1];
        middleNotesArray[0] = usedChord1[2];
        topNotesArray[0] = usedChord1[3];

        bottomBottomNotesArray[1] = usedChord2[0];
        bottomNotesArray[1] = usedChord2[1];
        middleNotesArray[1] = usedChord2[2];
        topNotesArray[1] = usedChord2[3];

        bottomBottomNotesArray[2] = usedChord3[0];
        bottomNotesArray[2] = usedChord3[1];
        middleNotesArray[2] = usedChord3[2];
        topNotesArray[2] = usedChord3[3];

        bottomBottomNotesArray[3] = usedChord4[0];
        bottomNotesArray[3] = usedChord4[1];
        middleNotesArray[3] = usedChord4[2];
        topNotesArray[3] = usedChord4[3];


        //Add note arrays to the phrases
        bottomBottomNotes.addNoteList(bottomBottomNotesArray);
        bottomNotes.addNoteList(bottomNotesArray);
        middleNotes.addNoteList(middleNotesArray);
        topNotes.addNoteList(topNotesArray);


        //Remove jumps of an octave or more in each voice
        bottomBottomNotes = removeOctaveJumps(bottomBottomNotes);
        bottomNotes = removeOctaveJumps(bottomNotes);
        middleNotes = removeOctaveJumps(middleNotes);
        topNotes = removeOctaveJumps(topNotes);


        //Set the scores
        setScoreSpecific(bottomNotes, "left");
        setScoreSpecific(middleNotes, "middle");
        setScoreSpecific(topNotes, "right");


        //Add the selected chords to the CPhrases
        cphr1.addChord(usedChord1);
        cphr2.addChord(usedChord2);
        cphr3.addChord(usedChord3);
        cphr4.addChord(usedChord4);


        //Add CPhrases to Part p
        p.addCPhrase(cphr1);
        p.addCPhrase(cphr2);
        p.addCPhrase(cphr3);
        p.addCPhrase(cphr4);


        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Cadence.mid");

        minor = false;
        major = false;
    }


    private String makeMIDIEasyCadence() {

        chooseRandomRootAndMakeScale();

        //Randomly make Perfect or Interruptive cadence
        int i2 = rn.nextInt(2);
        switch(i2){
            case(0):
                makePerfect();
                break;
            case(1):
                makeInterruptive();
                break;
        }


        if (i2 == 0) {
            return "perfect";
        } else {
            return "interruptive";
        }
    }


    private String makeMIDIMediumCadence() {

        chooseRandomRootAndMakeScale();

        //Randomly make Perfect, Interruptive or Imperfect cadence
        int i2 = rn.nextInt(3);
        switch(i2){
            case(0):
                makePerfect();
                break;
            case(1):
                makeInterruptive();
                break;
            case(2):
                makeImperfect();
                break;
        }


        if(i2 == 0) {
            return "perfect";
        } else if(i2 == 1) {
            return "interruptive";
        } else {
            return "imperfect";
        }
    }


    private String makeMIDIHardCadence() {

        chooseRandomRootAndMakeScale();

        //Randomly make Perfect, Interruptive, Imperfect or Plagal cadence
        int i2 = rn.nextInt(4);
        switch(i2){
            case(0):
                makePerfect();
                break;
            case(1):
                makeInterruptive();
                break;
            case(2):
                makeImperfect();
                break;
            case(3):
                makePlagal();
                break;
        }


        if(i2 == 0) {
            return "perfect";
        } else if(i2 == 1) {
            return "interruptive";
        } else if(i2 == 2){
            return "imperfect";
        } else {
            return "plagal";
        }
    }


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
        usedChord1 = new Note[3];
        usedChord2 = new Note[3];
        usedChord3 = new Note[3];
        usedChord4 = new Note[3];
        usedChord5 = new Note[3];
        usedChord6 = new Note[3];
        usedChord7 = new Note[3];
        usedChord8 = new Note[3];
        usedChord9 = new Note[3];
        p = new Part();
        s = new Score();


        if(easyRadioButton.isSelected()){
            correctAnswer = makeMIDIEasyCadence();
        } else if(mediumRadioButton.isSelected()){
            correctAnswer = makeMIDIMediumCadence();
        } else if(hardRadioButton.isSelected()){
            correctAnswer = makeMIDIHardCadence();
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




