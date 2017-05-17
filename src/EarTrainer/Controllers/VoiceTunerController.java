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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import jm.music.data.Note;
import jm.music.data.Phrase;

import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.*;
import javax.swing.*;


public class VoiceTunerController implements PitchDetectionHandler {

    public static final int TOTAL_QUESTIONS = 10;
    @FXML private StackPane stackPane;

    @FXML private HBox radioButtonsGroup;
    @FXML private RadioButton easyRadioButton;
    @FXML private RadioButton mediumRadioButton;
    @FXML private RadioButton hardRadioButton;

    @FXML private Label timerLabel;
    @FXML private Label questionLabel;
    @FXML private Label questionNoteLabel;
    @FXML private Label difficultyDescriptionLabel;
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

    @FXML private Button startButton;
    @FXML private Button nextQuestionButton;
    @FXML private Button recordButton;

    @FXML private Pane inputPane;


    private JGrandStave jScore = new JGrandStave();
    private Phrase phrase = new Phrase();

    private JMMusicCreator musicCreator;
    private String strSecs;
    private String strMins;

    private int questionNumber;
    private int numberOfCorrectAnswers = 0;

    private String correctAnswer = "";
    private boolean questionAnswered;
    private Timeline timeline;
    private Timeline questionTimeline;
    private boolean startClicked = false;
    private boolean recording = false;

    private AudioDispatcher dispatcher;
    private Mixer currentMixer;
    private double timeStamp;
    private double firstTimeStamp;
    private int firstTimeStampCounter = 1;

    private PitchProcessor.PitchEstimationAlgorithm algo;

    private Sequencer sequencer;

    private List pitches;
    private List times;
    private String[] notes = {"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"};
    private Note[] melodyArray;

    private double QUARTER_NOTE_LENGTH_IN_SECONDS = 2.2291157245635986 - 1.0448979139328003;


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


        JPanel inputPanel = new InputPanel();

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


    @FXML
    private void BackButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) stackPane.getScene().getWindow();
        stage.hide();
    }


    @FXML
    private void StartButtonClicked(ActionEvent event) throws IOException, InvalidMidiDataException, MidiUnavailableException {
        startClicked = true;
        questionNumber = 1;
        numberOfCorrectAnswers = 0;
        startTimer();
        questionLabel.setVisible(true);
        questionNoteLabel.setVisible(true);

        noteLabel.setVisible(true);
        noteLabelm1.setVisible(true);
        noteLabelp1.setVisible(true);

        startButton.setDisable(true);
        timerLabel.setVisible(true);
        radioButtonsGroup.setDisable(true);
        questionLabel.setText("Question 1");
        recordButton.setDisable(false);

        generateQuestion();
    }


    @FXML
    private void NextQuestionButtonClicked(ActionEvent event) throws IOException, InvalidMidiDataException, MidiUnavailableException {
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
    private void RecordButtonClicked(ActionEvent event) throws IOException {
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


    private void loadScore() {
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


    private String getAveragePitch(List pitches){
        float totalPitch = 0;

        for (int i = 0; i < pitches.size(); i++) {
            totalPitch += (float) pitches.get(i);
        }

        float avgPitch = totalPitch / pitches.size();
        int noOfSemitonesFromMiddleA = calculateNoteFromPitch(avgPitch);
        String note = notes[noOfSemitonesFromMiddleA];

        return note;
    }


    private void checkAnswer() {
        firstTimeStampCounter = 1;

        if(easyRadioButton.isSelected()) {
//            float totalPitch = 0;
//
//            for (int i = 0; i < pitches.size(); i++) {
//                totalPitch += (float) pitches.get(i);
//            }
//
//            float avgPitch = totalPitch / pitches.size();
//            int noOfSemitonesFromMiddleA = calculateNoteFromPitch(avgPitch);
            String note = getAveragePitch(pitches);

            if (note.equals(correctAnswer)) {
                makeButtonGreen(recordButton);
                numberOfCorrectAnswers++;
                correctIncorrectLabel.setTextFill(Color.web("#3abf4c"));
                correctIncorrectLabel.setText("Correct!");
            } else {
                makeButtonRed(recordButton);
                correctIncorrectLabel.setTextFill(Color.web("#da4343"));
                correctIncorrectLabel.setText("Incorrect. You sang on average: " + note);
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

            String note1String = musicCreator.getNote(melodyArray[0]);
            String note2String = musicCreator.getNote(melodyArray[1]);

            String theirNote1 = getAveragePitch(note1Pitches);
            String theirNote2 = getAveragePitch(note2Pitches);

            boolean note1Correct = theirNote1.equals(note1String);
            boolean note2Correct = theirNote2.equals(note2String);

            if (note1Correct && note2Correct) {
                makeButtonGreen(recordButton);
                numberOfCorrectAnswers++;
                correctIncorrectLabel.setTextFill(Color.web("#3abf4c"));
                correctIncorrectLabel.setText("Correct!");
            } else {
                makeButtonRed(recordButton);
                correctIncorrectLabel.setTextFill(Color.web("#da4343"));
                correctIncorrectLabel.setText("Incorrect. You sang: " + theirNote1 + ", " + theirNote2);
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

            String note1String = musicCreator.getNote(melodyArray[0]);
            String note2String = musicCreator.getNote(melodyArray[1]);
            String note3String = musicCreator.getNote(melodyArray[2]);

            String theirNote1 = getAveragePitch(note1Pitches);
            String theirNote2 = getAveragePitch(note2Pitches);
            String theirNote3 = getAveragePitch(note3Pitches);

            boolean note1Correct = theirNote1.equals(note1String);
            boolean note2Correct = theirNote2.equals(note2String);
            boolean note3Correct = theirNote3.equals(note3String);

            if (note1Correct && note2Correct && note3Correct) {
                makeButtonGreen(recordButton);
                numberOfCorrectAnswers++;
                correctIncorrectLabel.setTextFill(Color.web("#3abf4c"));
                correctIncorrectLabel.setText("Correct!");
            } else {
                makeButtonRed(recordButton);
                correctIncorrectLabel.setTextFill(Color.web("#da4343"));
                correctIncorrectLabel.setText("Incorrect. You sang: " + theirNote1 + ", " + theirNote2 + ", " + theirNote3);
            }
        }

        nextQuestionButton.setDisable(false);

        if(questionNumber == TOTAL_QUESTIONS){
            nextQuestionButton.setText("Score");
        }
    }


//    private String checkMajority(List notePitches) {
//        Map<String, Integer> map = new HashMap<String, Integer>();
//
//        for(int i = 0; i < notePitches.size(); i++) {
//            int noOfSemitonesFromMiddleA = calculateNoteFromPitch((float)notePitches.get(i));
//            String note = notes[noOfSemitonesFromMiddleA];
//
//            if(map.get(note) == null){
//                map.put(note,1);
//            }else{
//                map.put(note, map.get(note) + 1);
//            }
//        }
//
//        int largest = 0;
//        String modeNote = "";
//        for (Map.Entry<String, Integer> entry : map.entrySet()) {
//            String key = entry.getKey();
//            int value = entry.getValue();
//            if( value > largest){
//                largest = value;
//                modeNote = key;
//            }
//        }
//
//        return modeNote;
//    }


    private void resetButtonColours() {
        recordButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
    }


    private void makeButtonRed(Button button) {
        button.setStyle("-fx-base: #ffb3b3;");
    }


    private void makeButtonGreen(Button correctButton) {
        correctButton.setStyle("-fx-base: #adebad;");
    }


    private void startTimer() {
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


    private void stopTimer() {
        timerLabel.setVisible(false);
        timeline.stop();
    }


    @FXML
    private void generateQuestion() throws IOException, MidiUnavailableException, InvalidMidiDataException {
        musicCreator = new JMMusicCreator(jScore);

        if(easyRadioButton.isSelected()){
            correctAnswer = musicCreator.makeMIDIEasyVoiceTuner();
        } else if(mediumRadioButton.isSelected()){
            melodyArray = musicCreator.makeMIDIMediumVoiceTuner();
        } else if(hardRadioButton.isSelected()){
            melodyArray = musicCreator.makeMIDIHardVoiceTuner();
        }

        if(easyRadioButton.isSelected()) {
            questionNoteLabel.setText("Sing: " + correctAnswer);
        } else if(mediumRadioButton.isSelected()) {
            questionNoteLabel.setText("Sing: " + musicCreator.getNote(melodyArray[0]) +
                                        ", " + musicCreator.getNote(melodyArray[1]));
        } else {
            questionNoteLabel.setText("Sing: " + musicCreator.getNote(melodyArray[0]) +
                                          ", " + musicCreator.getNote(melodyArray[1]) +
                                          ", " + musicCreator.getNote(melodyArray[2]));
        }

        playSound();
    }


    @FXML
    private void replayButtonClicked(ActionEvent event) throws IOException, InvalidMidiDataException, MidiUnavailableException {
        sequencer.stop();
        sequencer.close();

        playSound();
    }


    private void playSound() throws MidiUnavailableException, IOException, InvalidMidiDataException {
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


    public void setScore(Phrase phr) {
        jScore.setPhrase(phr);

        Dimension d = new Dimension();
        d.setSize(600,300);
        jScore.setPreferredSize(d);
        jScore.setMaximumSize(d);

        jScore.removeTitle();
        jScore.setEditable(false);
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


    private int calculateNoteFromPitch(float pitch){
        float concertPitchA = 440;
        float ratio = pitch/concertPitchA;
        double a = Math.pow(2, 1.0/12);
        double n = Math.log(ratio)/Math.log(a);

        int noOfSemitonesFromMiddleA = (int) Math.round(n);
        noOfSemitonesFromMiddleA = noOfSemitonesFromMiddleA % 12;

        if(noOfSemitonesFromMiddleA < 0){
            noOfSemitonesFromMiddleA += 12;
        }

        return noOfSemitonesFromMiddleA;
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

            int n2 = calculateNoteFromPitch(pitch);

            String notem1;
            String notem2;
            String notem3;
            String notem4;

            String notep1;
            String notep2;
            String notep3;
            String notep4;


            String note = notes[n2];

            if(n2 != 0) {
                notem1 = notes[n2 - 1];
            } else {
                notem1 = notes[11];
            }

            if(n2 > 1) {
                notem2 = notes[n2 - 2];
            } else {
                notem2 = notes[10];
            }

            if(n2 > 2) {
                notem3 = notes[n2 - 3];
            } else {
                notem3 = notes[9];
            }

            if(n2 > 3) {
                notem4 = notes[n2 - 4];
            } else {
                notem4 = notes[8];
            }





            if(n2 != 11) {
                notep1 = notes[n2 + 1];
            } else {
                notep1 = notes[0];
            }

            if(n2 < 10) {
                notep2 = notes[n2 + 2];
            } else {
                notep2 = notes[1];
            }

            if(n2 < 9) {
                notep3 = notes[n2 + 3];
            } else {
                notep3 = notes[2];
            }

            if(n2 < 8) {
                notep4 = notes[n2 + 4];
            } else {
                notep4 = notes[3];
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




