package EarTrainer.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Write;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.*;
import java.util.List;

import javafx.scene.paint.Color;


public class WrongNoteIdentificationController extends AbstractController{

    @FXML private Button submitButton;
    @FXML private Button playChangedButton;

    @FXML private Label correctIncorrectText;

    private int indexOfChangedNote = 0;
    private List theirMelodyAnswer;

    private Phrase originalPhr2 = new Phrase();
    private Note[] melodyArray;
    private Note[] melodyArray2;

    private String filePath = "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/WrongNote.mid";

    private MouseListener ml = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            theirMelodyAnswer = new LinkedList<Note>(Arrays.asList(jScore.getPhrase().getNoteArray()));
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    };




    @FXML
    private void easyRadioButtonSelected(ActionEvent event) throws IOException {
        difficultyDescriptionLabel.setText("Identify the note which is out of key and drag it into a position which is in key.");
    }


    @FXML
    private void mediumRadioButtonSelected(ActionEvent event) throws IOException {
        difficultyDescriptionLabel.setText("Identify the note on the stave that does not correspond to the respective note that was played. Drag the note to match the note that was played. +/- 3 semitones.");
    }


    @FXML
    private void hardRadioButtonSelected(ActionEvent event) throws IOException {
        difficultyDescriptionLabel.setText("Identify the note on the stave that does not correspond to the respective note that was played. Drag the note to match the note that was played. +/- 1 semitone.");
        playChangedButton.setDisable(true);
    }


    @FXML
    private void SubmitButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            answerButtonClicked();
            checkAnswer(theirMelodyAnswer, melodyArray2);
        }
    }


    @Override
    @FXML
    void nextQuestionButtonClicked(ActionEvent event) throws IOException, InvalidMidiDataException, MidiUnavailableException {
        sequencer.stop();
        sequencer.close();

        if (questionNumber != TOTAL_QUESTIONS) {
            questionNumber++;
            questionLabel.setText("Question " + Integer.toString(questionNumber));

            questionAnswered = false;
            nextQuestionButton.setDisable(true);
            resetButtonColours();
            correctIncorrectText.setText("");
//            setScore(phrase);

            jScore.removeMouseListener(ml);
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
            correctIncorrectText.setText("");
//            setScore(phrase);
        }


    }


    protected void resetButtonColours() {
        submitButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
    }


    private boolean contains(int[] scale, int note){
        for(int n : scale){
            if(n == note){
                return true;
            }
        }

        return false;
    }


    private void checkAnswer(List theirMelodyAnswer, Note[] correctMelody) {
        System.out.println("*********************");
        System.out.println("After score changed:");

        for(int j = 0; j < theirMelodyAnswer.size(); j++){
            System.out.println("melodyArray2,      Note " + j + ": " + correctMelody[j].getPitch());
            System.out.println("theirMelodyAnswer, Note " + j + ": " + ((Note)theirMelodyAnswer.get(j)).getPitch());
            System.out.println("");
        }


        for (int i = 0; i < theirMelodyAnswer.size(); i++) {
            if (i != indexOfChangedNote) {
                if (((Note)theirMelodyAnswer.get(i)).getPitch() != correctMelody[i].getPitch()) {
                    makeButtonRed(submitButton);
                    correctIncorrectText.setTextFill(Color.web("#da4343"));
//                        correctIncorrectText.setText("Incorrect. You moved the wrong note.");
                    correctIncorrectText.setText("Incorrect.");
                    break;
                }
            } else {
                if (((Note)theirMelodyAnswer.get(i)).getPitch() == correctMelody[i].getPitch()) {
                    makeButtonGreen(submitButton);
                    numberOfCorrectAnswers++;
                    correctIncorrectText.setTextFill(Color.web("#3abf4c"));
                    correctIncorrectText.setText("Correct!");
                } else {
                    makeButtonRed(submitButton);
                    correctIncorrectText.setTextFill(Color.web("#da4343"));
//                        correctIncorrectText.setText("Incorrect. You moved the correct note to an incorrect place.");
                    correctIncorrectText.setText("Incorrect.");
                    break;
                }
            }
        }
//        }

        theirMelodyAnswer.clear();

        if(questionNumber == TOTAL_QUESTIONS){
            nextQuestionButton.setText("Score");
        }
    }


    private LinkedList makeMelody(){
        LinkedList melody = new LinkedList<Note>();
        double lengthSoFar = 0.0;
        List noteLengthsListCopy = new LinkedList<Note>((noteLengthsList));


        while(lengthSoFar != 4.0){
            int p = rn.nextInt(7);
            int pitch = scaleNotes[p];

            if(noteLengthsListCopy.size() == 0){
                break;
            }


            int d = rn.nextInt(noteLengthsListCopy.size());
            double duration = (double)noteLengthsListCopy.get(d);

            if(duration + lengthSoFar <= 4.0){
                melody.add(new Note(pitch, duration));
                lengthSoFar += duration;
            } else {
                noteLengthsListCopy.remove(d);
            }
        }

        return melody;
    }


    private int makeMIDIEasyWrongNote(){
        //Make major or minor scale
        chooseRandomRootAndMakeMinorOrMajorScale();


        //Make melody
        LinkedList<Note> melody = makeMelody();
        LinkedList<Note> melody2 = new LinkedList<Note>();

        for(int m = 0; m < melody.size(); m++){
            melody2.add(melody.get(m));
        }

        melodyArray = new Note[melody.size()];
        melodyArray2 = new Note[melody2.size()];

        melody.toArray(melodyArray);
        melody2.toArray(melodyArray2);


        //Add melody array to phrase
        phr2.addNoteList(melodyArray);
        originalPhr2.addNoteList(melodyArray);


        //Find random note in melody to change
        int i2 = rn.nextInt(melodyArray.length);
        int i3 = rn.nextInt(2);

        double duration = melodyArray[i2].getDuration();
        int pitch = melodyArray[i2].getPitch();

        int[] upOrDown = {-3,3};
        int newPitch = pitch + upOrDown[i3];

        System.out.println("Wrong note: " + i2);
        System.out.println("Down or up: " + i3);

        melodyArray[i2] = new Note(newPitch, duration);
        phr1.addNoteList(melodyArray);

        setScoreEditable(phr1);


        p.add(phr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/WrongNote.mid");

        return i2;
    }


    private int makeMIDIMediumWrongNote(){

        //Make major or minor scale
        chooseRandomRootAndMakeMinorOrMajorScale();


        //Make melody
        LinkedList<Note> melody = makeMelody();
        LinkedList<Note> melody2 = new LinkedList<Note>();

        for(int m = 0; m < melody.size(); m++){
            melody2.add(melody.get(m));
        }

        melodyArray = new Note[melody.size()];
        melodyArray2 = new Note[melody2.size()];

        melody.toArray(melodyArray);
        melody2.toArray(melodyArray2);


        //Add melody array to phrase
        phr2.addNoteList(melodyArray);
        originalPhr2.addNoteList(melodyArray);


        //Find random note in melody to change
        int i2 = rn.nextInt(melodyArray.length);
        int i3 = rn.nextInt(2);

        double duration = melodyArray[i2].getDuration();
        int pitch = melodyArray[i2].getPitch();

        int[] upOrDown = {-2,2};
        int newPitch = pitch + upOrDown[i3];

        System.out.println("Wrong note: " + i2);
        System.out.println("Down or up: " + i3);

        melodyArray[i2] = new Note(newPitch, duration);
        phr1.addNoteList(melodyArray);

        setScoreEditable(phr1);


        p.add(phr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/WrongNote.mid");

        return i2;
    }


    private int makeMIDIHardWrongNote(){

        //Make major or minor scale
        chooseRandomRootAndMakeMinorOrMajorScale();


        //Make melody
        LinkedList<Note> melody = makeMelody();
        LinkedList<Note> melody2 = new LinkedList<Note>();

        for(int m = 0; m < melody.size(); m++){
            melody2.add(melody.get(m));
        }

        melodyArray = new Note[melody.size()];
        melodyArray2 = new Note[melody2.size()];

        melody.toArray(melodyArray);
        melody2.toArray(melodyArray2);


        //Add melody array to phrase
        phr2.addNoteList(melodyArray);
        originalPhr2.addNoteList(melodyArray);


        //Find random note in melody to change
        int i2 = rn.nextInt(melodyArray.length);
        int i3 = rn.nextInt(2);

        double duration = melodyArray[i2].getDuration();
        int pitch = melodyArray[i2].getPitch();

        int[] upOrDown = {-1,1};
        int newPitch = pitch + upOrDown[i3];

        System.out.println("Wrong note: " + i2);
        System.out.println("Down or up: " + i3);

        melodyArray[i2] = new Note(newPitch, duration);
        phr1.addNoteList(melodyArray);

        setScoreEditable(phr1);


        p.add(phr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/WrongNote.mid");

        return i2;
    }


    @FXML
    protected void generateQuestion() throws IOException, MidiUnavailableException, InvalidMidiDataException {
//        phrase = originalPhr2;
        phr1 = new Phrase();
        phr2 = new Phrase();
        p = new Part();
        s = new Score();

        jScore.addMouseListener(ml);

        if(easyRadioButton.isSelected()){
            indexOfChangedNote = makeMIDIEasyWrongNote();
        } else if(mediumRadioButton.isSelected()){
            indexOfChangedNote = makeMIDIMediumWrongNote();
        } else if(hardRadioButton.isSelected()){
            indexOfChangedNote = makeMIDIHardWrongNote();
        }

        playSound(filePath);
    }


    @FXML
    void replayButtonClicked(ActionEvent event) throws IOException, InvalidMidiDataException, MidiUnavailableException {
        sequencer.stop();
        sequencer.close();

        playSound(filePath);
    }


    @FXML
    private void playChangedButtonClicked(ActionEvent event) throws IOException, InvalidMidiDataException, MidiUnavailableException {
        phrase = jScore.getPhrase();

        Part p = new Part();
        p.add(phrase);

        Score s = new Score();
        s.addPart(p);

        String newFilePath = "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/WrongNoteNew.mid";

        Write.midi(s, newFilePath);

        playSound(newFilePath);
    }


    private void playSound(String filePath) throws MidiUnavailableException, IOException, InvalidMidiDataException {
        sequencer = MidiSystem.getSequencer();
        sequencer.open();
        InputStream is = new BufferedInputStream(new FileInputStream(new File(filePath)));
        sequencer.setSequence(is);
        sequencer.start();
    }


    protected void playSound() throws MidiUnavailableException, IOException, InvalidMidiDataException {
    }


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




