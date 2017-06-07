package EarTrainer.Tests;

/**
 * Created by timannoel on 05/06/2017.
 */

import EarTrainer.Controllers.ModulationRecognitionController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import static com.google.common.base.Verify.verify;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.loadui.testfx.Assertions.verifyThat;


public class ModulationRecognitionTests extends GuiTest{

    private ModulationRecognitionController modulationRecognitionController;
    private Button[] buttons;
    private Random rn = new Random();
    private Button nextQuestionButton;
    private Button startButton;



    @Override
    protected Parent getRootNode() {
        Parent root= null;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/ModulationRecognition.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        modulationRecognitionController = loader.getController();
        nextQuestionButton = modulationRecognitionController.nextQuestionButton;
        startButton = modulationRecognitionController.startButton;


        Stage newStage = new Stage();
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.show();

        return root;
    }


    @Test
    public void changingDifficultyDisablesEnablesRelevantButtons() throws InterruptedException {
        int numberOfButtonsDisabled = 0;


        click("#mediumRadioButton");
        click(startButton);

        if(modulationRecognitionController.similarKey0Button.isDisabled()){
            numberOfButtonsDisabled++;
        }

        if(modulationRecognitionController.similarKey1Button.isDisabled()){
            numberOfButtonsDisabled++;
        }

        if(modulationRecognitionController.similarKey2Button.isDisabled()){
            numberOfButtonsDisabled++;
        }

        if(modulationRecognitionController.similarKey3Button.isDisabled()){
            numberOfButtonsDisabled++;
        }

        if(modulationRecognitionController.similarKey4Button.isDisabled()){
            numberOfButtonsDisabled++;
        }

        verify(numberOfButtonsDisabled == 1);


        click(startButton);
        click("#hardRadioButton");
        numberOfButtonsDisabled = 0;
        click(startButton);


        if(modulationRecognitionController.similarKey0Button.isDisabled()){
            numberOfButtonsDisabled++;
        }

        if(modulationRecognitionController.similarKey1Button.isDisabled()){
            numberOfButtonsDisabled++;
        }

        if(modulationRecognitionController.similarKey2Button.isDisabled()){
            numberOfButtonsDisabled++;
        }

        if(modulationRecognitionController.similarKey3Button.isDisabled()){
            numberOfButtonsDisabled++;
        }

        if(modulationRecognitionController.similarKey4Button.isDisabled()){
            numberOfButtonsDisabled++;
        }

        verify(numberOfButtonsDisabled == 0);



        click(startButton);
        click("#easyRadioButton");
        numberOfButtonsDisabled = 0;
        click(startButton);


        if(modulationRecognitionController.similarKey0Button.isDisabled()){
            numberOfButtonsDisabled++;
        }

        if(modulationRecognitionController.similarKey1Button.isDisabled()){
            numberOfButtonsDisabled++;
        }

        if(modulationRecognitionController.similarKey2Button.isDisabled()){
            numberOfButtonsDisabled++;
        }

        if(modulationRecognitionController.similarKey3Button.isDisabled()){
            numberOfButtonsDisabled++;
        }

        if(modulationRecognitionController.similarKey4Button.isDisabled()){
            numberOfButtonsDisabled++;
        }

        verify(numberOfButtonsDisabled == 2);
    }


    @Test
    public void difficultyDescriptionsUpdate() throws InterruptedException {
        String text = ((Label) find("#difficultyDescriptionLabel")).getText();
        verifyThat(text, is("Identify the new key. 3 options."));

        click("#mediumRadioButton");
        text = ((Label) find("#difficultyDescriptionLabel")).getText();
        verifyThat(text, is("Identify the new key. 4 options."));

        click("#hardRadioButton");
        text = ((Label) find("#difficultyDescriptionLabel")).getText();
        verifyThat(text, is("Identify the new key. 5 options."));

        click("#easyRadioButton");
        text = ((Label) find("#difficultyDescriptionLabel")).getText();
        verifyThat(text, is("Identify the new key. 3 options."));
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
        String correctAnswer = modulationRecognitionController.correctAnswer;
        verifyThat(correctAnswer, is(""));


        //Clicking Start sets correctAnswer variable
        click(startButton);
        correctAnswer = modulationRecognitionController.correctAnswer;
        verifyThat(correctAnswer, not(""));
    }


    @Test
    public void clickingStopResetsScore(){
        String startStopButtonText = startButton.getText();
        verifyThat(startStopButtonText, is("Start"));
        click(startButton);

        Button correctButton = modulationRecognitionController.correctButton;
        int numberOfCorrectAnswers = modulationRecognitionController.numberOfCorrectAnswers;
        verifyThat(numberOfCorrectAnswers, is(0));

        click(correctButton);

        String text = ((Label) find("#correctIncorrectLabel")).getText();
        verifyThat(text, is("Correct!"));
        numberOfCorrectAnswers = modulationRecognitionController.numberOfCorrectAnswers;
        verifyThat(numberOfCorrectAnswers, is(1));


        startStopButtonText = startButton.getText();
        verifyThat(startStopButtonText, is("Stop"));
        click(startButton);
        numberOfCorrectAnswers = modulationRecognitionController.numberOfCorrectAnswers;
        verifyThat(numberOfCorrectAnswers, is(0));
    }


    @Test
    public void clickingStopResetsQuestionNumber(){
        String startStopButtonText = startButton.getText();
        verifyThat(startStopButtonText, is("Start"));
        click(startButton);

        Button correctButton = modulationRecognitionController.correctButton;
        int questionNumber = modulationRecognitionController.questionNumber;
        verifyThat(questionNumber, is(1));

        click(correctButton);

        click(nextQuestionButton);

        questionNumber = modulationRecognitionController.questionNumber;
        verifyThat(questionNumber, is(2));


        startStopButtonText = startButton.getText();
        verifyThat(startStopButtonText, is("Stop"));
        click(startButton);
        questionNumber = modulationRecognitionController.questionNumber;
        verifyThat(questionNumber, is(1));
    }


    @Test
    public void clickingNextButtonIncrementsQuestionNumber(){
        int difficultyMode = rn.nextInt(3);

        if(difficultyMode == 0){
            click("#easyRadioButton");
            buttons = new Button[3];
        } else if(difficultyMode == 1){
            click("#mediumRadioButton");
            buttons = new Button[4];
        } else {
            click("#hardRadioButton");
            buttons = new Button[5];
        }


        click(startButton);


        int numberEnabled = -1;

        if(!modulationRecognitionController.similarKey0Button.isDisabled()){
            numberEnabled++;
            buttons[numberEnabled] = modulationRecognitionController.similarKey0Button;
        }

        if(!modulationRecognitionController.similarKey1Button.isDisabled()){
            numberEnabled++;
            buttons[numberEnabled] = modulationRecognitionController.similarKey1Button;
        }

        if(!modulationRecognitionController.similarKey2Button.isDisabled()){
            numberEnabled++;
            buttons[numberEnabled] = modulationRecognitionController.similarKey2Button;
        }

        if(!modulationRecognitionController.similarKey3Button.isDisabled()){
            numberEnabled++;
            buttons[numberEnabled] = modulationRecognitionController.similarKey3Button;
        }

        if(!modulationRecognitionController.similarKey4Button.isDisabled()){
            numberEnabled++;
            buttons[numberEnabled] = modulationRecognitionController.similarKey4Button;
        }


        for(int i = 1; i < 10; i++){
            int randomButton = rn.nextInt(buttons.length);
            int questionNumber = modulationRecognitionController.questionNumber;
            click(buttons[randomButton]);
            click(nextQuestionButton);
            int nextQuestionNumber = modulationRecognitionController.questionNumber;
            verifyThat(nextQuestionNumber, is(questionNumber + 1));
        }
    }


    @Test
    public void nextQuestionButtonTextChangesToScoreOn10thQuestion(){
        click("#hardRadioButton");
        buttons = new Button[5];
        click(startButton);


        int numberEnabled = -1;

        if(!modulationRecognitionController.similarKey0Button.isDisabled()){
            numberEnabled++;
            buttons[numberEnabled] = modulationRecognitionController.similarKey0Button;
        }

        if(!modulationRecognitionController.similarKey1Button.isDisabled()){
            numberEnabled++;
            buttons[numberEnabled] = modulationRecognitionController.similarKey1Button;
        }

        if(!modulationRecognitionController.similarKey2Button.isDisabled()){
            numberEnabled++;
            buttons[numberEnabled] = modulationRecognitionController.similarKey2Button;
        }

        if(!modulationRecognitionController.similarKey3Button.isDisabled()){
            numberEnabled++;
            buttons[numberEnabled] = modulationRecognitionController.similarKey3Button;
        }

        if(!modulationRecognitionController.similarKey4Button.isDisabled()){
            numberEnabled++;
            buttons[numberEnabled] = modulationRecognitionController.similarKey4Button;
        }


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

        Button correctButton = modulationRecognitionController.correctButton;
        int numberOfCorrectAnswers = modulationRecognitionController.numberOfCorrectAnswers;

        click(correctButton);

        int numberOfCorrectAnswersAfterClick = modulationRecognitionController.numberOfCorrectAnswers;

        String text = ((Label) find("#correctIncorrectLabel")).getText();
        verifyThat(text, is("Correct!"));

        verifyThat(numberOfCorrectAnswersAfterClick, is(numberOfCorrectAnswers + 1));
    }


    @Test
    public void answeringIncorrectlyOutputsIncorrectAndDoesNotIncrementScore(){
        int difficultyMode = rn.nextInt(3);

        if(difficultyMode == 0){
            click("#easyRadioButton");
            buttons = new Button[3];
        } else if(difficultyMode == 1){
            click("#mediumRadioButton");
            buttons = new Button[4];
        } else {
            click("#hardRadioButton");
            buttons = new Button[5];
        }


        click(startButton);


        int numberEnabled = -1;

        if(!modulationRecognitionController.similarKey0Button.isDisabled()){
            numberEnabled++;
            buttons[numberEnabled] = modulationRecognitionController.similarKey0Button;
        }

        if(!modulationRecognitionController.similarKey1Button.isDisabled()){
            numberEnabled++;
            buttons[numberEnabled] = modulationRecognitionController.similarKey1Button;
        }

        if(!modulationRecognitionController.similarKey2Button.isDisabled()){
            numberEnabled++;
            buttons[numberEnabled] = modulationRecognitionController.similarKey2Button;
        }

        if(!modulationRecognitionController.similarKey3Button.isDisabled()){
            numberEnabled++;
            buttons[numberEnabled] = modulationRecognitionController.similarKey3Button;
        }

        if(!modulationRecognitionController.similarKey4Button.isDisabled()){
            numberEnabled++;
            buttons[numberEnabled] = modulationRecognitionController.similarKey4Button;
        }



        Button correctButton = modulationRecognitionController.correctButton;
        int numberOfCorrectAnswers = modulationRecognitionController.numberOfCorrectAnswers;

        int i = rn.nextInt(buttons.length);

        while(buttons[i] == correctButton){
            i = rn.nextInt(buttons.length);
        }

        click(buttons[i]);

        int numberOfCorrectAnswersAfterClick = modulationRecognitionController.numberOfCorrectAnswers;

        String text = ((Label) find("#correctIncorrectLabel")).getText();
        verifyThat(text, is("Incorrect."));

        verifyThat(numberOfCorrectAnswersAfterClick, is(numberOfCorrectAnswers));
    }
}