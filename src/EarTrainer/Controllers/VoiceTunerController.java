package EarTrainer.Controllers;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Write;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.*;
import javax.swing.*;
import static jm.constants.Pitches.*;
import static jm.constants.RhythmValues.*;


public class VoiceTunerController extends AbstractController implements PitchDetectionHandler {

    @FXML private Label questionNoteLabel;
    @FXML private Label correctIncorrectLabel;

    @FXML private Label noteLabel;
    @FXML private Label noteLabelm1;
    @FXML private Label noteLabelm2;
    @FXML private Label noteLabelm3;
    @FXML private Label noteLabelm4;
    @FXML private Label noteLabelp1;
    @FXML private Label noteLabelp2;
    @FXML private Label noteLabelp3;
    @FXML private Label noteLabelp4;

    @FXML public Button recordButton;

    @FXML private Pane inputPane;

    private Timeline questionTimeline;
    private boolean recording = false;

    private AudioDispatcher dispatcher;
    private Mixer currentMixer;
    private double timeStamp;
    private double firstTimeStamp;
    private int firstTimeStampCounter = 1;

    private PitchProcessor.PitchEstimationAlgorithm algo;

    private List pitches;
    private List times;
    private String[] notesStrings = {"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"};
    private Note[] melodyArray;

    private double QUARTER_NOTE_LENGTH_IN_SECONDS = 2.2291157245635986 - 1.0448979139328003;
    public JPanel inputPanel;


    @Override
    @FXML
    public void initialize() {
        noteLabelm4.setText("-");
        noteLabelm3.setText("-");
        noteLabelm2.setText("-");
        noteLabelm1.setText("-");
        noteLabel.setText("-");
        noteLabelp1.setText("-");
        noteLabelp2.setText("-");
        noteLabelp3.setText("-");
        noteLabelp4.setText("-");

        recordButton.setDisable(true);

        pitches = new LinkedList();
        times = new LinkedList();

//        for(Mixer.Info info : Shared.getMixerInfo(false, true)){
//            List<String> list = Arrays.asList(Shared.toLocalString(info).split(","));
//            dropDownBox.getItems().add(list.get(0));
//        }
//
//        dropDownBox.getSelectionModel().selectFirst();


        inputPanel = new InputPanel();

        SwingNode swingNode2 = new SwingNode();
        swingNode2.setContent(inputPanel);

        inputPane.getChildren().add(swingNode2);

        inputPanel.addPropertyChangeListener("mixer",
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent arg0) {
                        try {
                            setNewMixer((Mixer) arg0.getNewValue());
                        } catch (LineUnavailableException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (UnsupportedAudioFileException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });

        algo = PitchProcessor.PitchEstimationAlgorithm.YIN;
    }


    @FXML
    private void easyRadioButtonSelected(ActionEvent event) throws IOException {
        difficultyDescriptionLabel.setText("Sing/Play a single note");
    }


    @FXML
    private void mediumRadioButtonSelected(ActionEvent event) throws IOException {
        difficultyDescriptionLabel.setText("Sing/Play a two note melody");
    }


    @FXML
    private void hardRadioButtonSelected(ActionEvent event) throws IOException {
        difficultyDescriptionLabel.setText("Sing/Play a three note melody");
    }


    @Override
    @FXML
    void startButtonClicked() throws IOException, InvalidMidiDataException, MidiUnavailableException {
        if(!startClicked) {
            startClicked = true;
            questionNumber = 1;
            numberOfCorrectAnswers = 0;
            startTimer();
            questionLabel.setVisible(true);
            questionNoteLabel.setVisible(true);
            timerLabel.setVisible(true);
            radioButtonsGroup.setDisable(true);
            questionLabel.setText("Question 1");
            recordButton.setDisable(false);
            startButton.setText("Stop");
            startButton.setStyle("-fx-background-color: rgba(0,0,0,0.08), linear-gradient(#af595f, #754e53), linear-gradient(#ffd5de 0%, #facdd0 10%, #f9cdd6 50%, #fc8f9b 51%, #ffddeb 100%)");
            generateQuestion();
        } else {
            if(sequencer != null) {
                sequencer.stop();
                sequencer.close();
            }
            startButton.setStyle("-fx-background-color: rgba(0,0,0,0.08), linear-gradient(#5a61af, #51536d), linear-gradient(#e4fbff 0%,#cee6fb 10%, #a5d3fb 50%, #88c6fb 51%, #d5faff 100%)");
            startClicked = false;
            numberOfCorrectAnswers = 0;
            stopTimer();
            questionLabel.setVisible(false);
            timerLabel.setVisible(false);
            radioButtonsGroup.setDisable(false);
            startButton.setText("Start");
            correctIncorrectLabel.setText("");
        }
    }


    @Override
    @FXML
    void nextQuestionButtonClicked(ActionEvent event) throws IOException, InvalidMidiDataException, MidiUnavailableException {
        sequencer.stop();
        sequencer.close();

        pitches.clear();
        times.clear();
        recordButton.setText("Record");
        recordButton.setDisable(false);
        questionTimeline.stop();
        correctIncorrectLabel.setText("");

        if (questionNumber != TOTAL_QUESTIONS) {
            questionNumber++;
            questionLabel.setText("Question " + Integer.toString(questionNumber));

            questionAnswered = false;
            nextQuestionButton.setDisable(true);
            resetButtonColours();
            setScore(phrase);
            generateQuestion();
        } else {
            nextQuestionButton.setText("Next Question");
            questionLabel.setVisible(false);
            nextQuestionButton.setDisable(true);
            startClicked = false;
            recordButton.setDisable(true);
            stopTimer();
            loadScore();

            questionAnswered = false;
            nextQuestionButton.setDisable(true);
            resetButtonColours();
            setScore(phrase);
        }
    }


    @FXML
    private void recordButtonClicked(ActionEvent event) throws IOException {
        recordButton.setText("Recording");
        recordButton.setDisable(true);
        double lengthToRecordFor;

        if(easyRadioButton.isSelected()){
            lengthToRecordFor = Math.floor(QUARTER_NOTE_LENGTH_IN_SECONDS * 1000);
        } else if(mediumRadioButton.isSelected()){
            double n1Length = convertNoteDurationToSeconds(melodyArray[0].getRhythmValue());
            double n2Length = convertNoteDurationToSeconds(melodyArray[1].getRhythmValue());
            lengthToRecordFor = Math.floor((n1Length + n2Length) * 1000);
        } else {
            double n1Length = convertNoteDurationToSeconds(melodyArray[0].getRhythmValue());
            double n2Length = convertNoteDurationToSeconds(melodyArray[1].getRhythmValue());
            double n3Length = convertNoteDurationToSeconds(melodyArray[2].getRhythmValue());
            lengthToRecordFor = Math.floor((n1Length + n2Length + n3Length) * 1000);
        }

        questionTimeline = new Timeline(
                new KeyFrame(Duration.millis(0),
                        new EventHandler<ActionEvent>() {
                            int millisecs = 0;
                            @Override public void handle(ActionEvent actionEvent) {
                                millisecs++;
                                if(millisecs < lengthToRecordFor) {
                                    recording = true;
                                } else if (millisecs == lengthToRecordFor){
                                    recordButton.setText("Recorded");
                                    recording = false;
                                    checkAnswer();
                                }
                            }
                        }
                ),
                new KeyFrame(Duration.millis(1))
        );

        questionTimeline.setCycleCount(Animation.INDEFINITE);
        questionTimeline.play();
    }


    private double convertNoteDurationToSeconds(double noteDuration){
        if(noteDuration == 1.0){
            return QUARTER_NOTE_LENGTH_IN_SECONDS;
        } else if(noteDuration == 1.5){
            return QUARTER_NOTE_LENGTH_IN_SECONDS * 1.5;
        } else if(noteDuration == 2.0){
            return QUARTER_NOTE_LENGTH_IN_SECONDS * 2;
        } else if(noteDuration == 0.75){
            return QUARTER_NOTE_LENGTH_IN_SECONDS * 0.75;
        } else if(noteDuration == 0.5){
            return QUARTER_NOTE_LENGTH_IN_SECONDS * 0.5;
        } else if(noteDuration == 0.375){
            return QUARTER_NOTE_LENGTH_IN_SECONDS * 0.375;
        } else {
            return QUARTER_NOTE_LENGTH_IN_SECONDS * 0.25;
        }
    }


    private String getModePitch(List pitches){
        Map<String, Integer> map = new HashMap<String, Integer>();

        for(int i = 0; i < pitches.size(); i++) {
            String note = getNote(calculateNoteFromPitch((float)pitches.get(i)));

            if(map.get(note) == null){
                map.put(note,1);
            }else{
                map.put(note, map.get(note) + 1);
            }
        }

        int largest = 0;
        String modeNote = "";
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();
            if( value > largest){
                largest = value;
                modeNote = key;
            }
        }

        return modeNote;
    }


    private void checkAnswer() {
        firstTimeStampCounter = 1;

        if(easyRadioButton.isSelected()) {
            String noteMode = getModePitch(pitches);

            if(noteMode.equals(correctAnswer)) {
                makeButtonGreen(recordButton);
                numberOfCorrectAnswers++;
                correctIncorrectLabel.setTextFill(Color.web("#3abf4c"));
                correctIncorrectLabel.setText("Correct!");
            } else {
                makeButtonRed(recordButton);
                correctIncorrectLabel.setTextFill(Color.web("#da4343"));
                correctIncorrectLabel.setText("Incorrect. You sang on average: " + noteMode);
            }
        } else if(mediumRadioButton.isSelected()) {
            double n1Length = convertNoteDurationToSeconds(melodyArray[0].getRhythmValue());

            List note1Pitches = new LinkedList();
            List note2Pitches = new LinkedList();

            for (int i = 0; i < times.size(); i++) {
                if((double)times.get(i) > firstTimeStamp && (double)times.get(i) < n1Length + firstTimeStamp){
                    note1Pitches.add(pitches.get(i));
                } else {
                    note2Pitches.add(pitches.get(i));
                }
            }

            String note1String = getNote(melodyArray[0]);
            String note2String = getNote(melodyArray[1]);

            String theirNote1Mode = getModePitch(note1Pitches);
            String theirNote2Mode = getModePitch(note2Pitches);

            boolean note1CorrectMode = theirNote1Mode.equals(note1String);
            boolean note2CorrectMode = theirNote2Mode.equals(note2String);


            if (note1CorrectMode && note2CorrectMode) {
                makeButtonGreen(recordButton);
                numberOfCorrectAnswers++;
                correctIncorrectLabel.setTextFill(Color.web("#3abf4c"));
                correctIncorrectLabel.setText("Correct!");
            } else {
                makeButtonRed(recordButton);
                correctIncorrectLabel.setTextFill(Color.web("#da4343"));
                correctIncorrectLabel.setText("Incorrect. You sang: " + theirNote1Mode + ", " + theirNote2Mode);
            }
        } else {
            double n1Length = convertNoteDurationToSeconds(melodyArray[0].getRhythmValue());
            double n2Length = convertNoteDurationToSeconds(melodyArray[1].getRhythmValue());

            List note1Pitches = new LinkedList();
            List note2Pitches = new LinkedList();
            List note3Pitches = new LinkedList();

            for (int i = 0; i < times.size(); i++) {
                if((double)times.get(i) > firstTimeStamp && (double)times.get(i) < n1Length + firstTimeStamp){
                    note1Pitches.add(pitches.get(i));
                } else if((double)times.get(i) >= n1Length + firstTimeStamp && (double)times.get(i) < n1Length + n2Length + firstTimeStamp){
                    note2Pitches.add(pitches.get(i));
                } else {
                    note3Pitches.add(pitches.get(i));
                }
            }

            String note1String = getNote(melodyArray[0]);
            String note2String = getNote(melodyArray[1]);
            String note3String = getNote(melodyArray[2]);


            String theirNote1Mode = getModePitch(note1Pitches);
            String theirNote2Mode = getModePitch(note2Pitches);
            String theirNote3Mode = getModePitch(note3Pitches);

            boolean note1CorrectMode = theirNote1Mode.equals(note1String);
            boolean note2CorrectMode = theirNote2Mode.equals(note2String);
            boolean note3CorrectMode = theirNote3Mode.equals(note3String);


            if (note1CorrectMode && note2CorrectMode && note3CorrectMode) {
                makeButtonGreen(recordButton);
                numberOfCorrectAnswers++;
                correctIncorrectLabel.setTextFill(Color.web("#3abf4c"));
                correctIncorrectLabel.setText("Correct!");
            } else {
                makeButtonRed(recordButton);
                correctIncorrectLabel.setTextFill(Color.web("#da4343"));
                correctIncorrectLabel.setText("Incorrect. You sang: " + theirNote1Mode + ", " + theirNote2Mode + ", " + theirNote3Mode);
            }
        }

        nextQuestionButton.setDisable(false);

        if(questionNumber == TOTAL_QUESTIONS){
            nextQuestionButton.setText("Score");
        }
    }


    protected void resetButtonColours() {
        recordButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
    }


    private String makeMIDIEasyVoiceTuner(){
        int interval = rn.nextInt(12);

        setScore(phr1);

        Note n = new Note(C4 + interval, C);

        phr2.addNote(n);

        p.addPhrase(phr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Pitch.mid");

        return getNote(n);
    }


    private Note[] makeMIDIMediumVoiceTuner(){

        //Make minor or major scale
        chooseRandomRootAndMakeScale();


        //Set scale notesStrings
        if(minor) {
            scaleNotes = minorScale;
        } else {
            scaleNotes = majorScale;
        }


        //Make a one octave version of scaleNotes
        int[] scaleNotesOneOctave =  new int[7];
        for(int j = 0; j < 7; j++){
            scaleNotesOneOctave[j] = scaleNotes[j];
        }


        //Make 2 note melody
        int noteIndex = rn.nextInt(7);
        int durationIndex = rn.nextInt(7);

        Note n1 = new Note(scaleNotesOneOctave[noteIndex], (double)noteLengthsList.get(durationIndex));

        noteIndex = rn.nextInt(7);
        durationIndex = rn.nextInt(7);

        Note n2 = new Note(scaleNotesOneOctave[noteIndex], (double)noteLengthsList.get(durationIndex));

        Note[] melodyArray = {n1,n2};
        phr2.addNoteList(melodyArray);

        p.addPhrase(phr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/VoiceTuner.mid");
        return melodyArray;
    }


    private Note[] makeMIDIHardVoiceTuner(){

        //Make minor or major scale
        chooseRandomRootAndMakeScale();


        //Set scale notesStrings
        if(minor) {
            scaleNotes = minorScale;
        } else {
            scaleNotes = majorScale;
        }


        //Make a one octave version of scaleNotes
        int[] scaleNotesOneOctave =  new int[7];
        for(int j = 0; j < 7; j++){
            scaleNotesOneOctave[j] = scaleNotes[j];
        }


        //Make 3 note melody
        int noteIndex = rn.nextInt(7);
        int durationIndex = rn.nextInt(7);

        Note n1 = new Note(scaleNotesOneOctave[noteIndex], (double)noteLengthsList.get(durationIndex));

        noteIndex = rn.nextInt(7);
        durationIndex = rn.nextInt(7);

        Note n2 = new Note(scaleNotesOneOctave[noteIndex], (double)noteLengthsList.get(durationIndex));

        noteIndex = rn.nextInt(7);
        durationIndex = rn.nextInt(7);

        Note n3 = new Note(scaleNotesOneOctave[noteIndex], (double)noteLengthsList.get(durationIndex));

        Note[] melodyArray = {n1,n2,n3};
        phr2.addNoteList(melodyArray);

        p.addPhrase(phr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/VoiceTuner.mid");
        return melodyArray;
    }


    @FXML
    protected void generateQuestion() throws IOException, MidiUnavailableException, InvalidMidiDataException {
        phr1 = new Phrase();
        phr2 = new Phrase();
        p = new Part();
        s = new Score();

        if(easyRadioButton.isSelected()){
            correctAnswer = makeMIDIEasyVoiceTuner();
        } else if(mediumRadioButton.isSelected()){
            melodyArray = makeMIDIMediumVoiceTuner();
        } else if(hardRadioButton.isSelected()){
            melodyArray = makeMIDIHardVoiceTuner();
        }

        if(easyRadioButton.isSelected()) {
            questionNoteLabel.setText("Sing: " + correctAnswer);
        } else if(mediumRadioButton.isSelected()) {
            questionNoteLabel.setText("Sing: " + getNote(melodyArray[0]) +
                                        ", " + getNote(melodyArray[1]));
        } else {
            questionNoteLabel.setText("Sing: " + getNote(melodyArray[0]) +
                                          ", " + getNote(melodyArray[1]) +
                                          ", " + getNote(melodyArray[2]));
        }

        playSound();
    }


    protected void playSound() throws MidiUnavailableException, IOException, InvalidMidiDataException {
        final String MEDIA_URL;

        if(easyRadioButton.isSelected()){
            MEDIA_URL = "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Pitch.mid";
        } else {
            MEDIA_URL = "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/VoiceTuner.mid";
        }

        sequencer = MidiSystem.getSequencer();
        sequencer.open();
        InputStream is = new BufferedInputStream(new FileInputStream(new File(MEDIA_URL)));
        sequencer.setSequence(is);
        sequencer.start();
    }


    private void setNewMixer(Mixer mixer) throws LineUnavailableException,
            UnsupportedAudioFileException {

        if(dispatcher!= null){
            dispatcher.stop();
        }
        currentMixer = mixer;

        float sampleRate = 44100;
        int bufferSize = 1024;
        int overlap = 0;


        final AudioFormat format = new AudioFormat(sampleRate, 16, 1, true,
                true);
        final DataLine.Info dataLineInfo = new DataLine.Info(
                TargetDataLine.class, format);
        TargetDataLine line;
        line = (TargetDataLine) mixer.getLine(dataLineInfo);
        final int numberOfSamples = bufferSize;
        line.open(format, numberOfSamples);
        line.start();
        final AudioInputStream stream = new AudioInputStream(line);

        JVMAudioInputStream audioStream = new JVMAudioInputStream(stream);
        // create a new dispatcher
        dispatcher = new AudioDispatcher(audioStream, bufferSize,
                overlap);

        // add a processor
        dispatcher.addAudioProcessor(new PitchProcessor(algo, sampleRate, bufferSize, this));

        new Thread(dispatcher,"Audio dispatching").start();
    }


    private Note calculateNoteFromPitch(float pitch){
        float concertPitchA = 440;
        float ratio = pitch/concertPitchA;
        double a = Math.pow(2, 1.0/12);
        double n = Math.log(ratio)/Math.log(a);

        int noOfSemitonesFromMiddleA = (int)Math.round(n);
        noOfSemitonesFromMiddleA = noOfSemitonesFromMiddleA % 12;

        if(noOfSemitonesFromMiddleA < 0){
            noOfSemitonesFromMiddleA += 12;
        }

        return new Note(A4 + noOfSemitonesFromMiddleA, C);
    }


    @Override
    public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
        if (pitchDetectionResult.getPitch() != -1) {
            float pitch = pitchDetectionResult.getPitch();

            if(recording && firstTimeStampCounter > 0) {
                firstTimeStamp = audioEvent.getTimeStamp();
                firstTimeStampCounter--;
            }

            if(recording) {
                timeStamp = audioEvent.getTimeStamp();
                pitches.add(pitch);
                times.add(timeStamp);
            }


            String notem1;
            String notem2;
            String notem3;
            String notem4;

            String notep1;
            String notep2;
            String notep3;
            String notep4;


            int distanceFromMiddleA = calculateNoteFromPitch(pitch).getPitch() - A4;
            String note = getNote(calculateNoteFromPitch(pitch));


            if(distanceFromMiddleA != 0) {
                notem1 = notesStrings[distanceFromMiddleA - 1];
            } else {
                notem1 = notesStrings[11];
            }

            if(distanceFromMiddleA > 1) {
                notem2 = notesStrings[distanceFromMiddleA - 2];
            } else {
                notem2 = notesStrings[10];
            }

            if(distanceFromMiddleA > 2) {
                notem3 = notesStrings[distanceFromMiddleA - 3];
            } else {
                notem3 = notesStrings[9];
            }

            if(distanceFromMiddleA > 3) {
                notem4 = notesStrings[distanceFromMiddleA - 4];
            } else {
                notem4 = notesStrings[8];
            }





            if(distanceFromMiddleA != 11) {
                notep1 = notesStrings[distanceFromMiddleA + 1];
            } else {
                notep1 = notesStrings[0];
            }

            if(distanceFromMiddleA < 10) {
                notep2 = notesStrings[distanceFromMiddleA + 2];
            } else {
                notep2 = notesStrings[1];
            }

            if(distanceFromMiddleA < 9) {
                notep3 = notesStrings[distanceFromMiddleA + 3];
            } else {
                notep3 = notesStrings[2];
            }

            if(distanceFromMiddleA < 8) {
                notep4 = notesStrings[distanceFromMiddleA + 4];
            } else {
                notep4 = notesStrings[3];
            }


            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    noteLabelm4.setText(notem4);
                    noteLabelm3.setText(notem3);
                    noteLabelm2.setText(notem2);
                    noteLabelm1.setText(notem1);
                    noteLabel.setText(note);
                    noteLabelp1.setText(notep1);
                    noteLabelp2.setText(notep2);
                    noteLabelp3.setText(notep3);
                    noteLabelp4.setText(notep4);
                }
            });
        }
    }
}




