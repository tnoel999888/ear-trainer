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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import jm.gui.cpn.JGrandStave;
import jm.music.data.Phrase;

import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.*;
import java.util.List;


import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.*;
import javax.swing.*;


public class VoiceTunerController implements PitchDetectionHandler {

    public static final int TOTAL_QUESTIONS = 10;
    @FXML private StackPane stackPane;

    @FXML private ToggleGroup radioButtons;
    @FXML private HBox radioButtonsGroup;
    @FXML private RadioButton easyRadioButton;
    @FXML private RadioButton mediumRadioButton;
    @FXML private RadioButton hardRadioButton;

    @FXML private Button correctButton = new Button();

    @FXML private Label timerLabel;
    @FXML private Label questionLabel;
    @FXML private Label questionNoteLabel;
    @FXML private Label difficultyDescriptionLabel;

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

    @FXML private Pane scorePane;
    @FXML private Pane inputPane;
    @FXML private Pane algoPane;

    @FXML HBox mediaBar;

    @FXML ChoiceBox dropDownBox;


    private JGrandStave jScore = new JGrandStave();
    private Phrase phrase = new Phrase();

    private JMMusicCreator musicCreator;
    private String strSecs;
    private String strMins;

    private int questionNumber;
    private int numberOfCorrectAnswers = 0;

    private String correctAnswer = "unison";
    private boolean questionAnswered;
    private Timeline timeline;
    private boolean startClicked = false;


    private AudioDispatcher dispatcher;
    private Mixer currentMixer;

    private PitchProcessor.PitchEstimationAlgorithm algo;


//    private ActionListener algoChangeListener = new ActionListener(){
//        @Override
//        public void actionPerformed(final java.awt.event.ActionEvent e) {
//            String name = e.getActionCommand();
//            PitchProcessor.PitchEstimationAlgorithm newAlgo = PitchProcessor.PitchEstimationAlgorithm.valueOf(name);
//            algo = newAlgo;
//            try {
//                setNewMixer(currentMixer);
//            } catch (LineUnavailableException e1) {
//                e1.printStackTrace();
//            } catch (UnsupportedAudioFileException e1) {
//                e1.printStackTrace();
//            }
//        }};


    @FXML
    public void initialize() {

        for(Mixer.Info info : Shared.getMixerInfo(false, true)){
            List<String> list = Arrays.asList(Shared.toLocalString(info).split(","));
            dropDownBox.getItems().add(list.get(0));
        }

        dropDownBox.getSelectionModel().selectFirst();
//
//        dropDownBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//        });


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
//
//        JPanel pitchDetectionPanel = new PitchDetectionPanel(algoChangeListener);
//
//        SwingNode swingNode3 = new SwingNode();
//        swingNode3.setContent(pitchDetectionPanel);
//
//        algoPane.getChildren().add(swingNode3);
    }


    @FXML
    private void easyRadioButtonSelected(ActionEvent event) throws IOException {
        difficultyDescriptionLabel.setText("White notes only. One octave.");
    }


    @FXML
    private void mediumRadioButtonSelected(ActionEvent event) throws IOException {
        difficultyDescriptionLabel.setText("All notes. One octave.");
    }


    @FXML
    private void hardRadioButtonSelected(ActionEvent event) throws IOException {
        difficultyDescriptionLabel.setText("All notes. Three octaves.");
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
//        questionNoteLabel.setText("Sing: ");

        generateQuestion();
    }


    @FXML
    private void NextQuestionButtonClicked(ActionEvent event) throws IOException, InvalidMidiDataException, MidiUnavailableException {
        if (questionNumber != TOTAL_QUESTIONS) {
            questionNumber++;
            questionLabel.setText("Question " + Integer.toString(questionNumber));
        } else {
            nextQuestionButton.setText("Next Question");
            questionLabel.setVisible(false);
            nextQuestionButton.setDisable(true);
            stopTimer();
            loadScore();
        }

        questionAnswered = false;
        nextQuestionButton.setDisable(true);
        resetButtonColours();

        setScore(phrase);
        generateQuestion();
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


    private void resetButtonColours() {
    }


    private void checkAnswer(String answer, Button button) {
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
            correctAnswer = musicCreator.makeMIDIEasyPitch();
        } else if(mediumRadioButton.isSelected()){
            correctAnswer = musicCreator.makeMIDIMediumPitch();
        } else if(hardRadioButton.isSelected()){
            correctAnswer = musicCreator.makeMIDIHardPitch();
        }

        questionNoteLabel.setText("Sing: " + correctAnswer);
        //correctButton = getCorrectButton(correctAnswer);

        playSound();
    }


    @FXML
    private void replayButtonClicked(ActionEvent event) throws IOException, InvalidMidiDataException, MidiUnavailableException {
        playSound();
    }


    private void playSound() throws MidiUnavailableException, IOException, InvalidMidiDataException {
        final String MEDIA_URL = "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Pitch.mid";

        Sequencer sequencer = MidiSystem.getSequencer();
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


    @Override
    public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
        if (pitchDetectionResult.getPitch() != -1) {
            //double timeStamp = audioEvent.getTimeStamp();
            float pitch = pitchDetectionResult.getPitch();
            float concertPitchA = 440;
            float ratio = pitch/concertPitchA;
            double a = Math.pow(2, 1.0/12);
            double n = Math.log(ratio)/Math.log(a);

            int n2 = (int) Math.round(n);
            n2 = n2 % 12;

            if(n2 < 0){
                n2 += 12;
            }

            String[] notes = {"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"};
            String note = notes[n2];

            String notem1;
            String notem2;
            String notem3;
            String notem4;

            String notep1;
            String notep2;
            String notep3;
            String notep4;



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


//            String message = String.format("%.2fHz\n", pitch);

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




