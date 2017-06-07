package EarTrainer.Tests;

/**
 * Created by timannoel on 05/06/2017.
 */
import EarTrainer.Controllers.SharpFlatRecognitionController;
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
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.loadui.testfx.Assertions.verifyThat;



public class SharpFlatRecognitionTests extends GuiTest{

    private SharpFlatRecognitionController sharpFlatRecognitionController;
    private Button[] buttons;
    private Random rn = new Random();
    private Button nextQuestionButton;
    private Button startButton;



    @Override
    protected Parent getRootNode() {
        Parent root= null;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/SharpFlatRecognition.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sharpFlatRecognitionController = loader.getController();
        nextQuestionButton = sharpFlatRecognitionController.nextQuestionButton;
        startButton = sharpFlatRecognitionController.startButton;
        buttons = new Button[]{sharpFlatRecognitionController.sharpButton,
                                sharpFlatRecognitionController.flatButton};


        Stage newStage = new Stage();
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.show();

        return root;
    }


    @Test
    public void difficultyDescriptionsUpdate() throws InterruptedException {
        String text = ((Label) find("#difficultyDescriptionLabel")).getText();
        verifyThat(text, is("Middle C only. +/- 15-25 Cents"));

        click("#mediumRadioButton");
        text = ((Label) find("#difficultyDescriptionLabel")).getText();
        verifyThat(text, is("Middle C only. +/- 5-15 Cents"));

        click("#hardRadioButton");
        text = ((Label) find("#difficultyDescriptionLabel")).getText();
        verifyThat(text, is("All notes. 1 Octave. +/- 1-5 Cents"));

        click("#easyRadioButton");
        text = ((Label) find("#difficultyDescriptionLabel")).getText();
        verifyThat(text, is("Middle C only. +/- 15-25 Cents"));
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
        String correctAnswer = sharpFlatRecognitionController.correctAnswer;
        verifyThat(correctAnswer, is(""));


        //Clicking Start sets correctAnswer variable
        click(startButton);
        correctAnswer = sharpFlatRecognitionController.correctAnswer;
        verifyThat(correctAnswer, not(""));
    }


    @Test
    public void clickingStopResetsScore(){
        String startStopButtonText = startButton.getText();
        verifyThat(startStopButtonText, is("Start"));
        click(startButton);

        Button correctButton = sharpFlatRecognitionController.correctButton;
        int numberOfCorrectAnswers = sharpFlatRecognitionController.numberOfCorrectAnswers;
        verifyThat(numberOfCorrectAnswers, is(0));

        click(correctButton);

        String text = ((Label) find("#correctIncorrectLabel")).getText();
        verifyThat(text, is("Correct!"));
        numberOfCorrectAnswers = sharpFlatRecognitionController.numberOfCorrectAnswers;
        verifyThat(numberOfCorrectAnswers, is(1));


        startStopButtonText = startButton.getText();
        verifyThat(startStopButtonText, is("Stop"));
        click(startButton);
        numberOfCorrectAnswers = sharpFlatRecognitionController.numberOfCorrectAnswers;
        verifyThat(numberOfCorrectAnswers, is(0));
    }


    @Test
    public void clickingStopResetsQuestionNumber(){
        String startStopButtonText = startButton.getText();
        verifyThat(startStopButtonText, is("Start"));
        click(startButton);

        Button correctButton = sharpFlatRecognitionController.correctButton;
        int questionNumber = sharpFlatRecognitionController.questionNumber;
        verifyThat(questionNumber, is(1));

        click(correctButton);

        click(nextQuestionButton);

        questionNumber = sharpFlatRecognitionController.questionNumber;
        verifyThat(questionNumber, is(2));


        startStopButtonText = startButton.getText();
        verifyThat(startStopButtonText, is("Stop"));
        click(startButton);
        questionNumber = sharpFlatRecognitionController.questionNumber;
        verifyThat(questionNumber, is(1));
    }


    @Test
    public void clickingNextButtonIncrementsQuestionNumber(){
        click(startButton);

        for(int i = 1; i < 10; i++){
            int randomButton = rn.nextInt(buttons.length);
            int questionNumber = sharpFlatRecognitionController.questionNumber;
            click(buttons[randomButton]);
            click(nextQuestionButton);
            int nextQuestionNumber = sharpFlatRecognitionController.questionNumber;
            verifyThat(nextQuestionNumber, is(questionNumber + 1));
        }
    }


    @Test
    public void nextQuestionButtonTextChangesToScoreOn10thQuestion(){
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

        Button correctButton = sharpFlatRecognitionController.correctButton;
        int numberOfCorrectAnswers = sharpFlatRecognitionController.numberOfCorrectAnswers;

        click(correctButton);

        int numberOfCorrectAnswersAfterClick = sharpFlatRecognitionController.numberOfCorrectAnswers;

        String text = ((Label) find("#correctIncorrectLabel")).getText();
        verifyThat(text, is("Correct!"));

        verifyThat(numberOfCorrectAnswersAfterClick, is(numberOfCorrectAnswers + 1));
    }


    @Test
    public void answeringIncorrectlyOutputsIncorrectAndDoesNotIncrementScore(){
        click(startButton);

        Button correctButton = sharpFlatRecognitionController.correctButton;
        int numberOfCorrectAnswers = sharpFlatRecognitionController.numberOfCorrectAnswers;

        int i = rn.nextInt(buttons.length);

        while(buttons[i] == correctButton){
            i = rn.nextInt(buttons.length);
        }

        click(buttons[i]);

        int numberOfCorrectAnswersAfterClick = sharpFlatRecognitionController.numberOfCorrectAnswers;

        String text = ((Label) find("#correctIncorrectLabel")).getText();
        verifyThat(text, is("Incorrect."));

        verifyThat(numberOfCorrectAnswersAfterClick, is(numberOfCorrectAnswers));
    }
}