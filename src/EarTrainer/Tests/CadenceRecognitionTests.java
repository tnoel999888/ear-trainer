package EarTrainer.Tests;

/**
 * Created by timannoel on 05/06/2017.
 */

import EarTrainer.Controllers.CadenceRecognitionController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

import java.io.IOException;
import java.util.Random;

import static com.google.common.base.Verify.verify;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.loadui.testfx.Assertions.verifyThat;


public class CadenceRecognitionTests extends GuiTest{

    private CadenceRecognitionController cadenceRecognitionController;
    private Button[] buttons;
    private Random rn = new Random();
    private Button nextQuestionButton;
    private Button startButton;



    @Override
    protected Parent getRootNode() {
        Parent root= null;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/CadenceRecognition.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        cadenceRecognitionController = loader.getController();
        nextQuestionButton = cadenceRecognitionController.nextQuestionButton;
        startButton = cadenceRecognitionController.startButton;


        Stage newStage = new Stage();
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.show();

        return root;
    }


    @Test
    public void changingDifficultyDisablesEnablesRelevantButtons() throws InterruptedException {
        verify(!cadenceRecognitionController.perfectButton.isDisabled());
        verify(!cadenceRecognitionController.interruptiveButton.isDisabled());

        verify(cadenceRecognitionController.imperfectButton.isDisabled());
        verify(cadenceRecognitionController.plagalButton.isDisabled());


        click("#mediumRadioButton");

        verify(!cadenceRecognitionController.perfectButton.isDisabled());
        verify(!cadenceRecognitionController.interruptiveButton.isDisabled());
        verify(!cadenceRecognitionController.imperfectButton.isDisabled());

        verify(cadenceRecognitionController.plagalButton.isDisabled());


        click("#hardRadioButton");

        verify(!cadenceRecognitionController.perfectButton.isDisabled());
        verify(!cadenceRecognitionController.interruptiveButton.isDisabled());
        verify(!cadenceRecognitionController.imperfectButton.isDisabled());
        verify(!cadenceRecognitionController.plagalButton.isDisabled());


        click("#easyRadioButton");

        verify(!cadenceRecognitionController.perfectButton.isDisabled());
        verify(!cadenceRecognitionController.interruptiveButton.isDisabled());

        verify(cadenceRecognitionController.imperfectButton.isDisabled());
        verify(cadenceRecognitionController.plagalButton.isDisabled());
    }


    @Test
    public void difficultyDescriptionsUpdate() throws InterruptedException {
        String text = ((Label) find("#difficultyDescriptionLabel")).getText();
        verifyThat(text, is("Only Perfect/Authentic and Interruptive/Deceptive Cadences."));

        click("#mediumRadioButton");
        text = ((Label) find("#difficultyDescriptionLabel")).getText();
        verifyThat(text, is("Perfect/Authentic, Interruptive/Deceptive and Imperfect/Half Cadences."));

        click("#hardRadioButton");
        text = ((Label) find("#difficultyDescriptionLabel")).getText();
        verifyThat(text, is("All 4 Cadences."));

        click("#easyRadioButton");
        text = ((Label) find("#difficultyDescriptionLabel")).getText();
        verifyThat(text, is("Only Perfect/Authentic and Interruptive/Deceptive Cadences."));
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
    public void clickingStartSetsCorrectAnswer(){
        String correctAnswer = cadenceRecognitionController.correctAnswer;
        verifyThat(correctAnswer, is(""));


        //Clicking Start sets correctAnswer variable
        click(startButton);
        correctAnswer = cadenceRecognitionController.correctAnswer;
        verifyThat(correctAnswer, not(""));
    }


    @Test
    public void clickingStopResetsScore(){
        String startStopButtonText = startButton.getText();
        verifyThat(startStopButtonText, is("Start"));
        click(startButton);

        Button correctButton = cadenceRecognitionController.correctButton;
        int numberOfCorrectAnswers = cadenceRecognitionController.numberOfCorrectAnswers;
        verifyThat(numberOfCorrectAnswers, is(0));

        click(correctButton);

        String text = ((Label) find("#correctIncorrectLabel")).getText();
        verifyThat(text, is("Correct!"));
        numberOfCorrectAnswers = cadenceRecognitionController.numberOfCorrectAnswers;
        verifyThat(numberOfCorrectAnswers, is(1));


        startStopButtonText = startButton.getText();
        verifyThat(startStopButtonText, is("Stop"));
        click(startButton);
        numberOfCorrectAnswers = cadenceRecognitionController.numberOfCorrectAnswers;
        verifyThat(numberOfCorrectAnswers, is(0));
    }


    @Test
    public void clickingStopResetsQuestionNumber(){
        String startStopButtonText = startButton.getText();
        verifyThat(startStopButtonText, is("Start"));
        click(startButton);

        Button correctButton = cadenceRecognitionController.correctButton;
        int questionNumber = cadenceRecognitionController.questionNumber;
        verifyThat(questionNumber, is(1));

        click(correctButton);

        click(nextQuestionButton);

        questionNumber = cadenceRecognitionController.questionNumber;
        verifyThat(questionNumber, is(2));


        startStopButtonText = startButton.getText();
        verifyThat(startStopButtonText, is("Stop"));
        click(startButton);
        questionNumber = cadenceRecognitionController.questionNumber;
        verifyThat(questionNumber, is(1));
    }


    @Test
    public void clickingNextButtonIncrementsQuestionNumber(){
        buttons = new Button[]{cadenceRecognitionController.perfectButton,
                                cadenceRecognitionController.interruptiveButton,
                                cadenceRecognitionController.imperfectButton,
                                cadenceRecognitionController.plagalButton};


        click("#hardRadioButton");
        click(startButton);


        for(int i = 1; i < 10; i++){
            int randomButton = rn.nextInt(buttons.length);
            int questionNumber = cadenceRecognitionController.questionNumber;
            click(buttons[randomButton]);
            click(nextQuestionButton);
            int nextQuestionNumber = cadenceRecognitionController.questionNumber;
            verifyThat(nextQuestionNumber, is(questionNumber + 1));
        }
    }


    @Test
    public void nextQuestionButtonTextChangesToScoreOn10thQuestion(){
        buttons = new Button[]{cadenceRecognitionController.perfectButton,
                                cadenceRecognitionController.interruptiveButton,
                                cadenceRecognitionController.imperfectButton,
                                cadenceRecognitionController.plagalButton};


        click("#hardRadioButton");
        click(startButton);


        for(int i = 0; i < 10; i++){
            int randomButton = rn.nextInt(buttons.length);
            click(buttons[randomButton]);

            if(i == 9){
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

        Button correctButton = cadenceRecognitionController.correctButton;
        int numberOfCorrectAnswers = cadenceRecognitionController.numberOfCorrectAnswers;

        click(correctButton);

        int numberOfCorrectAnswersAfterClick = cadenceRecognitionController.numberOfCorrectAnswers;

        String text = ((Label) find("#correctIncorrectLabel")).getText();
        verifyThat(text, is("Correct!"));

        verifyThat(numberOfCorrectAnswersAfterClick, is(numberOfCorrectAnswers + 1));
    }


    @Test
    public void answeringIncorrectlyOutputsIncorrectAndDoesNotIncrementScore(){
        int difficultyMode = rn.nextInt(3);

        if(difficultyMode == 0){
            click("#easyRadioButton");
            buttons = new Button[]{cadenceRecognitionController.perfectButton,
                                    cadenceRecognitionController.interruptiveButton};
        } else if(difficultyMode == 1){
            click("#mediumRadioButton");
            buttons = new Button[]{cadenceRecognitionController.perfectButton,
                                    cadenceRecognitionController.interruptiveButton,
                                    cadenceRecognitionController.imperfectButton,
                                    cadenceRecognitionController.plagalButton};
        } else {
            click("#hardRadioButton");
            buttons = new Button[]{cadenceRecognitionController.perfectButton,
                                    cadenceRecognitionController.interruptiveButton,
                                    cadenceRecognitionController.imperfectButton,
                                    cadenceRecognitionController.plagalButton};
        }


        click(startButton);

        Button correctButton = cadenceRecognitionController.correctButton;
        int numberOfCorrectAnswers = cadenceRecognitionController.numberOfCorrectAnswers;

        int i = rn.nextInt(buttons.length);

        while(buttons[i] == correctButton){
            i = rn.nextInt(buttons.length);
        }

        click(buttons[i]);

        int numberOfCorrectAnswersAfterClick = cadenceRecognitionController.numberOfCorrectAnswers;

        String text = ((Label) find("#correctIncorrectLabel")).getText();
        verifyThat(text, is("Incorrect."));

        verifyThat(numberOfCorrectAnswersAfterClick, is(numberOfCorrectAnswers));
    }
}