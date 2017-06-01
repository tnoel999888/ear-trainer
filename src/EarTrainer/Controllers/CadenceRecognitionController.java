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
import java.util.Arrays;
import static jm.constants.Durations.C;


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




    @Override
    @FXML
    public void initialize() {
        Dimension d = new Dimension();
        d.setSize(475,170);

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


    private boolean placeCommonNotesInSameVoice(Note[] chord1, Note[] chord2, boolean chordChanged){
        for(int i = 0; i < chord1.length; i++){
            int pitch1 = chord1[i].getPitch();

            for(int j = 0; j < chord2.length; j++){
                int pitch2 = chord2[j].getPitch();

                if(pitch2 + 12 == pitch1 || pitch2 + 24 == pitch1 || pitch2 + 36 == pitch1
                        ||pitch2 - 12 == pitch1 || pitch2 - 24 == pitch1 || pitch2 - 36 == pitch1) {
                    System.out.println("Here");
                    chord2[j].setPitch(chord2[i].getPitch());
                    chord2[i].setPitch(pitch1);
                    chordChanged = true;
                }
            }
        }

        return chordChanged;
    }


    private Note[] putNotesInVoicesInAscendingOrder(Note[] chord1){
        int bottomPitch = chord1[0].getPitch();
        int middlePitch = chord1[1].getPitch();
        int topPitch = chord1[2].getPitch();

        int[] pitches = {bottomPitch, middlePitch, topPitch};

        Arrays.sort(pitches);

        Note note1 = new Note(pitches[0], C);
        Note note2 = new Note(pitches[1], C);
        Note note3 = new Note(pitches[2], C);

        Note[] newChord = {note1, note2, note3};

        return newChord;
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



        System.out.println("Before");


        for(Note n : usedChord1){
            System.out.println(n.getPitch());
        }

        System.out.println("");


        for(Note n : usedChord2){
            System.out.println(n.getPitch());
        }

        System.out.println("");


        for(Note n : usedChord3){
            System.out.println(n.getPitch());
        }

        System.out.println("");


        for(Note n : usedChord4){
            System.out.println(n.getPitch());
        }

        System.out.println("");



        Note[] chord1Copy = new Note[3];
        Note[] chord2Copy = new Note[3];
        Note[] chord3Copy = new Note[3];
        Note[] chord4Copy = new Note[3];


        System.arraycopy(usedChord1, 0, chord1Copy, 0, usedChord1.length);
        System.arraycopy(usedChord2, 0, chord2Copy, 0, usedChord2.length);
        System.arraycopy(usedChord3, 0, chord3Copy, 0, usedChord3.length);
        System.arraycopy(usedChord4, 0, chord4Copy, 0, usedChord4.length);




        //Rearrange voices to minimise movement
        System.out.println("2:");
        chord2Changed = placeCommonNotesInSameVoice(usedChord1, usedChord2, chord2Changed);

        System.out.println("3:");
        chord3Changed = placeCommonNotesInSameVoice(usedChord2, usedChord3, chord3Changed);

        System.out.println("4:");
        chord4Changed = placeCommonNotesInSameVoice(usedChord3, usedChord4, chord4Changed);
        System.out.println("");


        usedChord1 = putNotesInVoicesInAscendingOrder(usedChord1);
        usedChord2 = putNotesInVoicesInAscendingOrder(usedChord2);
        usedChord3 = putNotesInVoicesInAscendingOrder(usedChord3);
        usedChord4 = putNotesInVoicesInAscendingOrder(usedChord4);





        System.out.println("Edited");

        for(Note n : usedChord1){
            System.out.println(n.getPitch());
        }

        System.out.println("");


        for(Note n : usedChord2){
            System.out.println(n.getPitch());
        }

        System.out.println("");


        for(Note n : usedChord3){
            System.out.println(n.getPitch());
        }

        System.out.println("");


        for(Note n : usedChord4){
            System.out.println(n.getPitch());
        }

        System.out.println("");





        bottomNotesArray[0] = usedChord1[0];
        middleNotesArray[0] = usedChord1[1];
        topNotesArray[0] = usedChord1[2];

        bottomNotesArray[1] = usedChord2[0];
        middleNotesArray[1] = usedChord2[1];
        topNotesArray[1] = usedChord2[2];

        bottomNotesArray[2] = usedChord3[0];
        middleNotesArray[2] = usedChord3[1];
        topNotesArray[2] = usedChord3[2];

        bottomNotesArray[3] = usedChord4[0];
        middleNotesArray[3] = usedChord4[1];
        topNotesArray[3] = usedChord4[2];


        //Add note arrays to the phrases
        bottomNotes.addNoteList(bottomNotesArray);
        middleNotes.addNoteList(middleNotesArray);
        topNotes.addNoteList(topNotesArray);


        //Set the scores
        setScoreSpecific(bottomNotes, "left");
        setScoreSpecific(middleNotes, "middle");
        setScoreSpecific(topNotes, "right");



        //Add the selected chords to the CPhrases
        cphr1.addChord(usedChord1);

        if(chord2Changed) {
            System.out.println("Chord 2 changed");
            cphr2.addChord(usedChord2);
        } else {
            cphr2.addChord(chord2Copy);

        }

        if(chord3Changed) {
            System.out.println("Chord 3 changed");
            cphr3.addChord(usedChord3);
        } else {
            cphr3.addChord(chord3Copy);

        }

        if(chord4Changed) {
            System.out.println("Chord 4 changed");
            cphr4.addChord(usedChord4);
        } else {
            cphr4.addChord(chord4Copy);

        }

        chord2Changed = false;
        chord3Changed = false;
        chord4Changed = false;


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





        System.out.println("Before");


        for(Note n : usedChord1){
            System.out.println(n.getPitch());
        }

        System.out.println("");


        for(Note n : usedChord2){
            System.out.println(n.getPitch());
        }

        System.out.println("");


        for(Note n : usedChord3){
            System.out.println(n.getPitch());
        }

        System.out.println("");


        for(Note n : usedChord4){
            System.out.println(n.getPitch());
        }

        System.out.println("");





//        //Rearrange voices to minimise movement
//        System.out.println("2:");
//        placeCommonNotesInSameVoice(usedChord1, usedChord2);
//
//        System.out.println("3:");
//        placeCommonNotesInSameVoice(usedChord2, usedChord3);
//
//        System.out.println("4:");
//        placeCommonNotesInSameVoice(usedChord3, usedChord4);
//        System.out.println("");



        usedChord1 = putNotesInVoicesInAscendingOrder(usedChord1);
        usedChord2 = putNotesInVoicesInAscendingOrder(usedChord2);
        usedChord3 = putNotesInVoicesInAscendingOrder(usedChord3);
        usedChord4 = putNotesInVoicesInAscendingOrder(usedChord4);




        System.out.println("Edited");

        for(Note n : usedChord1){
            System.out.println(n.getPitch());
        }

        System.out.println("");


        for(Note n : usedChord2){
            System.out.println(n.getPitch());
        }

        System.out.println("");


        for(Note n : usedChord3){
            System.out.println(n.getPitch());
        }

        System.out.println("");


        for(Note n : usedChord4){
            System.out.println(n.getPitch());
        }

        System.out.println("");




        bottomNotesArray[0] = usedChord1[0];
        middleNotesArray[0] = usedChord1[1];
        topNotesArray[0] = usedChord1[2];

        bottomNotesArray[1] = usedChord2[0];
        middleNotesArray[1] = usedChord2[1];
        topNotesArray[1] = usedChord2[2];

        bottomNotesArray[2] = usedChord3[0];
        middleNotesArray[2] = usedChord3[1];
        topNotesArray[2] = usedChord3[2];

        bottomNotesArray[3] = usedChord4[0];
        middleNotesArray[3] = usedChord4[1];
        topNotesArray[3] = usedChord4[2];


        //Add note arrays to the phrases
        bottomNotes.addNoteList(bottomNotesArray);
        middleNotes.addNoteList(middleNotesArray);
        topNotes.addNoteList(topNotesArray);


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
        bottomNotesArray[0] = scaleChord1[0];
        middleNotesArray[0] = scaleChord1[1];
        topNotesArray[0] = scaleChord1[2];
        usedChord1 = scaleChord1;


        //2nd Chord. Add II or IV chord
        int IIOrIVChord = rn.nextInt(2);
        if (IIOrIVChord == 0) {
            bottomNotesArray[1] = scaleChord2[0];
            middleNotesArray[1] = scaleChord2[1];
            topNotesArray[1] = scaleChord2[2];
            usedChord2 = scaleChord2;
        } else {
            bottomNotesArray[1] = scaleChord4[0];
            middleNotesArray[1] = scaleChord4[1];
            topNotesArray[1] = scaleChord4[2];
            usedChord2 = scaleChord4;
        }


        //3rd Chord. Add random chord (not V)
        bottomNotesArray[2] = scaleChords[i3][0];
        middleNotesArray[2] = scaleChords[i3][1];
        topNotesArray[2] = scaleChords[i3][2];
        usedChord3 = scaleChords[i3];


        //4th Chord. Add V chord
        bottomNotesArray[3] = scaleChord5[0];
        middleNotesArray[3] = scaleChord5[1];
        topNotesArray[3] = scaleChord5[2];
        usedChord4 = scaleChord5;


        //Add note arrays to the phrases
        bottomNotes.addNoteList(bottomNotesArray);
        middleNotes.addNoteList(middleNotesArray);
        topNotes.addNoteList(topNotesArray);


        //Set the scores
        setScoreSpecific(bottomNotes, "left");
        setScoreSpecific(middleNotes, "middle");
        setScoreSpecific(topNotes, "right");


//        //Rearrange voices to minimise movement
//        placeCommonNotesInSameVoice(usedChord1, usedChord2);
//        placeCommonNotesInSameVoice(usedChord2, usedChord3);
//        placeCommonNotesInSameVoice(usedChord3, usedChord4);


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
        bottomNotesArray[0] = scaleChord1[0];
        middleNotesArray[0] = scaleChord1[1];
        topNotesArray[0] = scaleChord1[2];
        usedChord1 = scaleChord1;


        //2nd Chord. Add random chord, (6, 1 1st Inversion)
        bottomNotesArray[1] = scaleChords[i2][0];
        middleNotesArray[1] = scaleChords[i2][1];
        topNotesArray[1] = scaleChords[i2][2];
        usedChord2 = scaleChords[i2];


        //3rd Chord. Add IV chord
        bottomNotesArray[2] = scaleChord4[0];
        middleNotesArray[2] = scaleChord4[1];
        topNotesArray[2] = scaleChord4[2];
        usedChord3 = scaleChord4;


        //4th Chord. Add I chord
        bottomNotesArray[3] = scaleChord1[0];
        middleNotesArray[3] = scaleChord1[1];
        topNotesArray[3] = scaleChord1[2];
        usedChord4 = scaleChord1;


        //Add note arrays to the phrases
        bottomNotes.addNoteList(bottomNotesArray);
        middleNotes.addNoteList(middleNotesArray);
        topNotes.addNoteList(topNotesArray);


        //Set the scores
        setScoreSpecific(bottomNotes, "left");
        setScoreSpecific(middleNotes, "middle");
        setScoreSpecific(topNotes, "right");


        //Rearrange voices to minimise movement
//        placeCommonNotesInSameVoice(usedChord1, usedChord2);
//        placeCommonNotesInSameVoice(usedChord2, usedChord3);
//        placeCommonNotesInSameVoice(usedChord3, usedChord4);


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

        chooseRandomRootAndMakeMinorOrMajorScale();

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

        chooseRandomRootAndMakeMinorOrMajorScale();

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

        chooseRandomRootAndMakeMinorOrMajorScale();

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




