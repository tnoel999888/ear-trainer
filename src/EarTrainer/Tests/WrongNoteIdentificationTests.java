package EarTrainer.Tests;

/**
 * Created by timannoel on 05/06/2017.
 */
import EarTrainer.Controllers.WrongNoteIdentificationController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import jm.music.data.Note;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import static jm.constants.Durations.C;
import static jm.constants.Pitches.C4;
import static org.hamcrest.core.Is.is;
import static org.loadui.testfx.Assertions.verifyThat;


public class WrongNoteIdentificationTests extends GuiTest{

    private WrongNoteIdentificationController wrongNoteIdentificationController;
    private Random rn = new Random();
    private Button nextQuestionButton;
    private Button startButton;
    private Button submitButton;



    @Override
    protected Parent getRootNode() {
        Parent root= null;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/WrongNoteIdentification.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        wrongNoteIdentificationController = loader.getController();
        nextQuestionButton = wrongNoteIdentificationController.nextQuestionButton;
        startButton = wrongNoteIdentificationController.startButton;
        submitButton = wrongNoteIdentificationController.submitButton;


        Stage newStage = new Stage();
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.show();

        return root;
    }


    @Test
    public void difficultyDescriptionsUpdate() throws InterruptedException {
        String text = ((Label) find("#difficultyDescriptionLabel")).getText();
        verifyThat(text, is("Identify the note on the stave that does not correspond to the respective note that was played. Drag the note to match the note that was played. +/- 3 semitones."));

        click("#mediumRadioButton");
        text = ((Label) find("#difficultyDescriptionLabel")).getText();
        verifyThat(text, is("Identify the note on the stave that does not correspond to the respective note that was played. Drag the note to match the note that was played. +/- 2 semitones."));

        click("#hardRadioButton");
        text = ((Label) find("#difficultyDescriptionLabel")).getText();
        verifyThat(text, is("Identify the note on the stave that does not correspond to the respective note that was played. Drag the note to match the note that was played. +/- 1 semitone."));

        click("#easyRadioButton");
        text = ((Label) find("#difficultyDescriptionLabel")).getText();
        verifyThat(text, is("Identify the note on the stave that does not correspond to the respective note that was played. Drag the note to match the note that was played. +/- 3 semitones."));
    }


    @Test
    public void startButtonTogglesToStopAndBack(){
        String text = startButton.getText();
        verifyThat(text, is("Start"));

        click(startButton);
        text = startButton.getText();
        verifyThat(text, is("Stop"));

        click(startButton);
        text = startButton.getText();
        verifyThat(text, is("Start"));
    }


    @Test
    public void clickingStopResetsScore(){
        String startStopButtonText = startButton.getText();
        verifyThat(startStopButtonText, is("Start"));
        click(startButton);


        wrongNoteIdentificationController.theirMelodyAnswer = Arrays.asList(wrongNoteIdentificationController.melodyArray2);
        int numberOfCorrectAnswers = wrongNoteIdentificationController.numberOfCorrectAnswers;
        verifyThat(numberOfCorrectAnswers, is(0));


        click(submitButton);


        String text = ((Label) find("#correctIncorrectText")).getText();
        verifyThat(text, is("Correct!"));
        numberOfCorrectAnswers = wrongNoteIdentificationController.numberOfCorrectAnswers;
        verifyThat(numberOfCorrectAnswers, is(1));


        startStopButtonText = startButton.getText();
        verifyThat(startStopButtonText, is("Stop"));
        click(startButton);
        numberOfCorrectAnswers = wrongNoteIdentificationController.numberOfCorrectAnswers;
        verifyThat(numberOfCorrectAnswers, is(0));
    }


    @Test
    public void clickingStopResetsQuestionNumber(){
        String startStopButtonText = startButton.getText();
        verifyThat(startStopButtonText, is("Start"));
        click(startButton);


        wrongNoteIdentificationController.theirMelodyAnswer = Arrays.asList(wrongNoteIdentificationController.melodyArray2);
        int numberOfCorrectAnswers = wrongNoteIdentificationController.numberOfCorrectAnswers;
        verifyThat(numberOfCorrectAnswers, is(0));


        int questionNumber = wrongNoteIdentificationController.questionNumber;
        verifyThat(questionNumber, is(1));
        click(submitButton);


        click(nextQuestionButton);

        questionNumber = wrongNoteIdentificationController.questionNumber;
        verifyThat(questionNumber, is(2));


        startStopButtonText = startButton.getText();
        verifyThat(startStopButtonText, is("Stop"));
        click(startButton);
        questionNumber = wrongNoteIdentificationController.questionNumber;
        verifyThat(questionNumber, is(1));
    }


    @Test
    public void clickingNextButtonIncrementsQuestionNumber(){
        click(startButton);


        for(int i = 1; i < 10; i++){
            wrongNoteIdentificationController.theirMelodyAnswer = Arrays.asList(wrongNoteIdentificationController.melodyArray2);
            int questionNumber = wrongNoteIdentificationController.questionNumber;

            click(submitButton);
            click(nextQuestionButton);

            int nextQuestionNumber = wrongNoteIdentificationController.questionNumber;
            verifyThat(nextQuestionNumber, is(questionNumber + 1));
        }
    }


    @Test
    public void nextQuestionButtonTextChangesToScoreOn10thQuestion(){
        click(startButton);


        for(int i = 0; i < 10; i++){
            wrongNoteIdentificationController.theirMelodyAnswer = Arrays.asList(wrongNoteIdentificationController.melodyArray2);

            click(submitButton);

            if(i == 10){
                String nextQuestionButtonText = nextQuestionButton.getText();
                verifyThat(nextQuestionButtonText, is("Score"));
            } else {
                String nextQuestionButtonText = nextQuestionButton.getText();
                verifyThat(nextQuestionButtonText, is("Next Question"));
            }

            click(nextQuestionButton);
        }
    }


    @Test
    public void answeringCorrectlyOutputsCorrectAndIncrementsScore(){
        int difficultyMode = rn.nextInt(3);

        if(difficultyMode == 0){
            click("#easyRadioButton");
        } else if(difficultyMode == 1){
            click("#mediumRadioButton");
        } else {
            click("#hardRadioButton");
        }

        click(startButton);

        wrongNoteIdentificationController.theirMelodyAnswer = Arrays.asList(wrongNoteIdentificationController.melodyArray2);
        int numberOfCorrectAnswers = wrongNoteIdentificationController.numberOfCorrectAnswers;

        click(submitButton);

        int numberOfCorrectAnswersAfterClick = wrongNoteIdentificationController.numberOfCorrectAnswers;

        String text = ((Label) find("#correctIncorrectText")).getText();
        verifyThat(text, is("Correct!"));

        verifyThat(numberOfCorrectAnswersAfterClick, is(numberOfCorrectAnswers + 1));
    }


    @Test
    public void answeringIncorrectlyOutputsIncorrectAndDoesNotIncrementScore(){
        int difficultyMode = rn.nextInt(3);

        if(difficultyMode == 0){
            click("#easyRadioButton");
        } else if(difficultyMode == 1){
            click("#mediumRadioButton");
        } else {
            click("#hardRadioButton");
        }

        click(startButton);

        wrongNoteIdentificationController.theirMelodyAnswer = Arrays.asList(new Note[wrongNoteIdentificationController.melodyArray2.length]);

        for(int i = 0; i < wrongNoteIdentificationController.theirMelodyAnswer.size(); i++){
            wrongNoteIdentificationController.theirMelodyAnswer.set(i, new Note(C4, C));
        }

        int numberOfCorrectAnswers = wrongNoteIdentificationController.numberOfCorrectAnswers;

        click(submitButton);

        int numberOfCorrectAnswersAfterClick = wrongNoteIdentificationController.numberOfCorrectAnswers;

        String text = ((Label) find("#correctIncorrectText")).getText();
        verifyThat(text, is("Incorrect."));

        verifyThat(numberOfCorrectAnswersAfterClick, is(numberOfCorrectAnswers));
    }
}