package EarTrainer.Controllers;

import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import jm.gui.cpn.JGrandStave;
import jm.music.data.CPhrase;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
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

    private JGrandStave jScoreLeft = new JGrandStave();
    private JGrandStave jScoreRight = new JGrandStave();



    @Override
    @FXML
    public void initialize() {
        Dimension d = new Dimension();
        d.setSize(475,170);

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
        swingNodeLeft.setContent(jScoreLeft);
        swingNodeRight.setContent(jScoreRight);

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
        setScoreSpecific(bottomNotes, "left");
        setScoreSpecific(middleNotes, "middle");
        setScoreSpecific(topNotes, "right");
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
        cphr1.addChord(scaleChord1);
        bottomNotesArray[0] = scaleChord1[0];
        middleNotesArray[0] = scaleChord1[1];
        topNotesArray[0] = scaleChord1[2];


        //2nd Chord. Add II or IV chord
        int IIOrIVChord = rn.nextInt(2);
        if (IIOrIVChord == 0) {
            cphr2.addChord(scaleChord2);
            bottomNotesArray[1] = scaleChord2[0];
            middleNotesArray[1] = scaleChord2[1];
            topNotesArray[1] = scaleChord2[2];
        } else {
            cphr2.addChord(scaleChord4);
            bottomNotesArray[1] = scaleChord4[0];
            middleNotesArray[1] = scaleChord4[1];
            topNotesArray[1] = scaleChord4[2];
        }


        //3rd Chord. Add V major chord if in minor scale
        if (minor) {
            cphr3.addChord(scaleChord5MelodicMinor);
            bottomNotesArray[2] = scaleChord5MelodicMinor[0];
            middleNotesArray[2] = scaleChord5MelodicMinor[1];
            topNotesArray[2] = scaleChord5MelodicMinor[2];
        } else {
            cphr3.addChord(scaleChord5);
            bottomNotesArray[2] = scaleChord5[0];
            middleNotesArray[2] = scaleChord5[1];
            topNotesArray[2] = scaleChord5[2];
        }


        //4th Chord. Add I or VI chord
        cphr4.addChord(scaleChord1);
        bottomNotesArray[3] = scaleChord1[0];
        middleNotesArray[3] = scaleChord1[1];
        topNotesArray[3] = scaleChord1[2];


        //Add note arrays to the phrases
        bottomNotes.addNoteList(bottomNotesArray);
        middleNotes.addNoteList(middleNotesArray);
        topNotes.addNoteList(topNotesArray);


        //Set the scores
        setScoreSpecific(bottomNotes, "left");
        setScoreSpecific(middleNotes, "middle");
        setScoreSpecific(topNotes, "right");


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
        cphr1.addChord(scaleChord1);
        bottomNotesArray[0] = scaleChord1[0];
        middleNotesArray[0] = scaleChord1[1];
        topNotesArray[0] = scaleChord1[2];


        //2nd Chord. Add II or IV chord
        int IIOrIVChord = rn.nextInt(2);
        if (IIOrIVChord == 0) {
            cphr2.addChord(scaleChord2);
            bottomNotesArray[1] = scaleChord2[0];
            middleNotesArray[1] = scaleChord2[1];
            topNotesArray[1] = scaleChord2[2];
        } else {
            cphr2.addChord(scaleChord4);
            bottomNotesArray[1] = scaleChord4[0];
            middleNotesArray[1] = scaleChord4[1];
            topNotesArray[1] = scaleChord4[2];
        }


        //3rd Chord. Add V chord
        cphr3.addChord(scaleChord5);
        bottomNotesArray[2] = scaleChord5[0];
        middleNotesArray[2] = scaleChord5[1];
        topNotesArray[2] = scaleChord5[2];


        //4th Chord. Add VI chord
        cphr4.addChord(scaleChord6);
        bottomNotesArray[3] = scaleChord6[0];
        middleNotesArray[3] = scaleChord6[1];
        topNotesArray[3] = scaleChord6[2];


        //Add note arrays to the phrases
        bottomNotes.addNoteList(bottomNotesArray);
        middleNotes.addNoteList(middleNotesArray);
        topNotes.addNoteList(topNotesArray);


        //Set the scores
        setScoreSpecific(bottomNotes, "left");
        setScoreSpecific(middleNotes, "middle");
        setScoreSpecific(topNotes, "right");


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

        int i2 = rn.nextInt(7);
        int i3 = rn.nextInt(7);

        //1st Chord. Add I chord
        cphr1.addChord(scaleChord1);
        bottomNotesArray[0] = scaleChord1[0];
        middleNotesArray[0] = scaleChord1[1];
        topNotesArray[0] = scaleChord1[2];


        //2nd Chord. Add random chord (2,4,5)
        cphr2.addChord(scaleChords[i2]);
        bottomNotesArray[1] = scaleChords[i2][0];
        middleNotesArray[1] = scaleChords[i2][1];
        topNotesArray[1] = scaleChords[i2][2];


        //3rd Chord. Add random chord
        cphr3.addChord(scaleChords[i3]);
        bottomNotesArray[2] = scaleChords[i3][0];
        middleNotesArray[2] = scaleChords[i3][1];
        topNotesArray[2] = scaleChords[i3][2];


        //4th Chord. Add V chord
        cphr4.addChord(scaleChord5);
        bottomNotesArray[3] = scaleChord5[0];
        middleNotesArray[3] = scaleChord5[1];
        topNotesArray[3] = scaleChord5[2];


        //Add note arrays to the phrases
        bottomNotes.addNoteList(bottomNotesArray);
        middleNotes.addNoteList(middleNotesArray);
        topNotes.addNoteList(topNotesArray);


        //Set the scores
        setScoreSpecific(bottomNotes, "left");
        setScoreSpecific(middleNotes, "middle");
        setScoreSpecific(topNotes, "right");


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

        //1st Chord. Add I chord
        cphr1.addChord(scaleChord1);
        bottomNotesArray[0] = scaleChord1[0];
        middleNotesArray[0] = scaleChord1[1];
        topNotesArray[0] = scaleChord1[2];


        //2nd Chord. Add random chord, (6, 1 1st Inversion)
        cphr2.addChord(scaleChords[i2]);
        bottomNotesArray[1] = scaleChords[i2][0];
        middleNotesArray[1] = scaleChords[i2][1];
        topNotesArray[1] = scaleChords[i2][2];


        //3rd Chord. Add IV chord
        cphr3.addChord(scaleChord4);
        bottomNotesArray[2] = scaleChord4[0];
        middleNotesArray[2] = scaleChord4[1];
        topNotesArray[2] = scaleChord4[2];


        //4th Chord. Add I chord
        cphr4.addChord(scaleChord1);
        bottomNotesArray[3] = scaleChord1[0];
        middleNotesArray[3] = scaleChord1[1];
        topNotesArray[3] = scaleChord1[2];


        //Add note arrays to the phrases
        bottomNotes.addNoteList(bottomNotesArray);
        middleNotes.addNoteList(middleNotesArray);
        topNotes.addNoteList(topNotesArray);


        //Set the scores
        setScoreSpecific(bottomNotes, "left");
        setScoreSpecific(middleNotes, "middle");
        setScoreSpecific(topNotes, "right");


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
//        musicCreator = new JMMusicCreator(jScore, jScoreLeft, jScoreRight);
        phr1 = new Phrase();
        phr2 = new Phrase();
        bottomNotes = new Phrase();
        middleNotes = new Phrase();
        topNotes = new Phrase();
        cphr1 = new CPhrase();
        cphr2 = new CPhrase();
        cphr3 = new CPhrase();
        cphr4 = new CPhrase();
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




