package EarTrainer.Tests;

/**
 * Created by timannoel on 05/06/2017.
 */
import EarTrainer.Controllers.MelodicIntervalRecognitionController;
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



public class MelodicIntervalRecognitionTests extends GuiTest{

    private MelodicIntervalRecognitionController melodicIntervalRecognitionController;
    private Button[] buttons;
    private Random rn = new Random();
    private Button nextQuestionButton;
    private Button startButton;



    @Override
    protected Parent getRootNode() {
        Parent root= null;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/MelodicIntervalRecognition.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        melodicIntervalRecognitionController = loader.getController();
        nextQuestionButton = melodicIntervalRecognitionController.nextQuestionButton;
        startButton = melodicIntervalRecognitionController.startButton;


        Stage newStage = new Stage();
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.show();

        return root;
    }


    @Test
    public void changingDifficultyDisablesEnablesRelevantButtons() throws InterruptedException {
        verify(!melodicIntervalRecognitionController.unisonButton.isDisabled());
        verify(!melodicIntervalRecognitionController.minorThirdButton.isDisabled());
        verify(!melodicIntervalRecognitionController.majorThirdButton.isDisabled());
        verify(!melodicIntervalRecognitionController.perfectFifthButton.isDisabled());
        verify(!melodicIntervalRecognitionController.octaveButton.isDisabled());

        verify(melodicIntervalRecognitionController.minorSecondButton.isDisabled());
        verify(melodicIntervalRecognitionController.majorSecondButton.isDisabled());
        verify(melodicIntervalRecognitionController.perfectFourthButton.isDisabled());
        verify(melodicIntervalRecognitionController.tritoneButton.isDisabled());
        verify(melodicIntervalRecognitionController.minorSixthButton.isDisabled());
        verify(melodicIntervalRecognitionController.majorSixthButton.isDisabled());
        verify(melodicIntervalRecognitionController.minorSeventhButton.isDisabled());
        verify(melodicIntervalRecognitionController.majorSeventhButton.isDisabled());


        click("#mediumRadioButton");

        verify(!melodicIntervalRecognitionController.unisonButton.isDisabled());
        verify(!melodicIntervalRecognitionController.minorThirdButton.isDisabled());
        verify(!melodicIntervalRecognitionController.majorThirdButton.isDisabled());
        verify(!melodicIntervalRecognitionController.perfectFifthButton.isDisabled());
        verify(!melodicIntervalRecognitionController.octaveButton.isDisabled());
        verify(!melodicIntervalRecognitionController.minorSecondButton.isDisabled());
        verify(!melodicIntervalRecognitionController.majorSecondButton.isDisabled());

        verify(melodicIntervalRecognitionController.perfectFourthButton.isDisabled());
        verify(melodicIntervalRecognitionController.tritoneButton.isDisabled());
        verify(melodicIntervalRecognitionController.minorSixthButton.isDisabled());
        verify(melodicIntervalRecognitionController.majorSixthButton.isDisabled());
        verify(melodicIntervalRecognitionController.minorSeventhButton.isDisabled());
        verify(melodicIntervalRecognitionController.majorSeventhButton.isDisabled());


        click("#hardRadioButton");

        verify(!melodicIntervalRecognitionController.unisonButton.isDisabled());
        verify(!melodicIntervalRecognitionController.minorThirdButton.isDisabled());
        verify(!melodicIntervalRecognitionController.majorThirdButton.isDisabled());
        verify(!melodicIntervalRecognitionController.perfectFifthButton.isDisabled());
        verify(!melodicIntervalRecognitionController.octaveButton.isDisabled());
        verify(!melodicIntervalRecognitionController.minorSecondButton.isDisabled());
        verify(!melodicIntervalRecognitionController.majorSecondButton.isDisabled());
        verify(!melodicIntervalRecognitionController.perfectFourthButton.isDisabled());
        verify(!melodicIntervalRecognitionController.tritoneButton.isDisabled());
        verify(!melodicIntervalRecognitionController.minorSixthButton.isDisabled());
        verify(!melodicIntervalRecognitionController.majorSixthButton.isDisabled());
        verify(!melodicIntervalRecognitionController.minorSeventhButton.isDisabled());
        verify(!melodicIntervalRecognitionController.majorSeventhButton.isDisabled());


        click("#easyRadioButton");

        verify(!melodicIntervalRecognitionController.unisonButton.isDisabled());
        verify(!melodicIntervalRecognitionController.minorThirdButton.isDisabled());
        verify(!melodicIntervalRecognitionController.majorThirdButton.isDisabled());
        verify(!melodicIntervalRecognitionController.perfectFifthButton.isDisabled());
        verify(!melodicIntervalRecognitionController.octaveButton.isDisabled());

        verify(melodicIntervalRecognitionController.minorSecondButton.isDisabled());
        verify(melodicIntervalRecognitionController.majorSecondButton.isDisabled());
        verify(melodicIntervalRecognitionController.perfectFourthButton.isDisabled());
        verify(melodicIntervalRecognitionController.tritoneButton.isDisabled());
        verify(melodicIntervalRecognitionController.minorSixthButton.isDisabled());
        verify(melodicIntervalRecognitionController.majorSixthButton.isDisabled());
        verify(melodicIntervalRecognitionController.minorSeventhButton.isDisabled());
        verify(melodicIntervalRecognitionController.majorSeventhButton.isDisabled());
    }


    @Test
    public void difficultyDescriptionsUpdate() throws InterruptedException {
        String text = ((Label) find("#difficultyDescriptionLabel")).getText();
        verifyThat(text, is("Middle C root note. Only Unisons, Thirds, Perfect Fifths and Octaves. Only 1 Octave."));

        click("#mediumRadioButton");
        text = ((Label) find("#difficultyDescriptionLabel")).getText();
        verifyThat(text, is("Middle C, D, E, F, G, A, B as root notes. Only Unisons, Seconds, Thirds, Perfect Fifths and Octaves. Only 1 Octave."));

        click("#hardRadioButton");
        text = ((Label) find("#difficultyDescriptionLabel")).getText();
        verifyThat(text, is("All notes as root notes. All intervals. 3 Octaves."));

        click("#easyRadioButton");
        text = ((Label) find("#difficultyDescriptionLabel")).getText();
        verifyThat(text, is("Middle C root note. Only Unisons, Thirds, Perfect Fifths and Octaves. Only 1 Octave."));
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
        String correctAnswer = melodicIntervalRecognitionController.correctAnswer;
        verifyThat(correctAnswer, is(""));


        //Clicking Start sets correctAnswer variable
        click(startButton);
        correctAnswer = melodicIntervalRecognitionController.correctAnswer;
        verifyThat(correctAnswer, not(""));
    }


    @Test
    public void clickingStopResetsScore(){
        String startStopButtonText = startButton.getText();
        verifyThat(startStopButtonText, is("Start"));
        click(startButton);

        Button correctButton = melodicIntervalRecognitionController.correctButton;
        int numberOfCorrectAnswers = melodicIntervalRecognitionController.numberOfCorrectAnswers;
        verifyThat(numberOfCorrectAnswers, is(0));

        click(correctButton);

        String text = ((Label) find("#correctIncorrectLabel")).getText();
        verifyThat(text, is("Correct!"));
        numberOfCorrectAnswers = melodicIntervalRecognitionController.numberOfCorrectAnswers;
        verifyThat(numberOfCorrectAnswers, is(1));


        startStopButtonText = startButton.getText();
        verifyThat(startStopButtonText, is("Stop"));
        click(startButton);
        numberOfCorrectAnswers = melodicIntervalRecognitionController.numberOfCorrectAnswers;
        verifyThat(numberOfCorrectAnswers, is(0));
    }


    @Test
    public void clickingStopResetsQuestionNumber(){
        String startStopButtonText = startButton.getText();
        verifyThat(startStopButtonText, is("Start"));
        click(startButton);

        Button correctButton = melodicIntervalRecognitionController.correctButton;
        int questionNumber = melodicIntervalRecognitionController.questionNumber;
        verifyThat(questionNumber, is(1));

        click(correctButton);

        click(nextQuestionButton);

        questionNumber = melodicIntervalRecognitionController.questionNumber;
        verifyThat(questionNumber, is(2));


        startStopButtonText = startButton.getText();
        verifyThat(startStopButtonText, is("Stop"));
        click(startButton);
        questionNumber = melodicIntervalRecognitionController.questionNumber;
        verifyThat(questionNumber, is(1));
    }


    @Test
    public void clickingNextButtonIncrementsQuestionNumber(){
        buttons = new Button[]{melodicIntervalRecognitionController.unisonButton,
                                melodicIntervalRecognitionController.minorSecondButton,
                                melodicIntervalRecognitionController.majorSecondButton,
                                melodicIntervalRecognitionController.minorThirdButton,
                                melodicIntervalRecognitionController.majorThirdButton,
                                melodicIntervalRecognitionController.perfectFourthButton,
                                melodicIntervalRecognitionController.tritoneButton,
                                melodicIntervalRecognitionController.perfectFifthButton,
                                melodicIntervalRecognitionController.minorSixthButton,
                                melodicIntervalRecognitionController.majorSixthButton,
                                melodicIntervalRecognitionController.minorSeventhButton,
                                melodicIntervalRecognitionController.majorSeventhButton,
                                melodicIntervalRecognitionController.octaveButton};


        click("#hardRadioButton");
        click(startButton);


        for(int i = 1; i < 10; i++){
            int randomButton = rn.nextInt(13);
            int questionNumber = melodicIntervalRecognitionController.questionNumber;
            click(buttons[randomButton]);
            click(nextQuestionButton);
            int nextQuestionNumber = melodicIntervalRecognitionController.questionNumber;
            verifyThat(nextQuestionNumber, is(questionNumber + 1));
        }
    }


    @Test
    public void nextQuestionButtonTextChangesToScoreOn10thQuestion(){
        buttons = new Button[]{melodicIntervalRecognitionController.unisonButton,
                                melodicIntervalRecognitionController.minorSecondButton,
                                melodicIntervalRecognitionController.majorSecondButton,
                                melodicIntervalRecognitionController.minorThirdButton,
                                melodicIntervalRecognitionController.majorThirdButton,
                                melodicIntervalRecognitionController.perfectFourthButton,
                                melodicIntervalRecognitionController.tritoneButton,
                                melodicIntervalRecognitionController.perfectFifthButton,
                                melodicIntervalRecognitionController.minorSixthButton,
                                melodicIntervalRecognitionController.majorSixthButton,
                                melodicIntervalRecognitionController.minorSeventhButton,
                                melodicIntervalRecognitionController.majorSeventhButton,
                                melodicIntervalRecognitionController.octaveButton};


        click("#hardRadioButton");
        click(startButton);


        for(int i = 0; i < 10; i++){
            int randomButton = rn.nextInt(12);
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
    public void clickingCorrectButtonOutputsCorrectAndIncrementsScore(){
        int difficultyMode = rn.nextInt(3);

        if(difficultyMode == 0){
            click("#easyRadioButton");
        } else if(difficultyMode == 1){
            click("#mediumRadioButton");
        } else {
            click("#hardRadioButton");
        }

        click(startButton);

        Button correctButton = melodicIntervalRecognitionController.correctButton;
        int numberOfCorrectAnswers = melodicIntervalRecognitionController.numberOfCorrectAnswers;

        click(correctButton);

        int numberOfCorrectAnswersAfterClick = melodicIntervalRecognitionController.numberOfCorrectAnswers;

        String text = ((Label) find("#correctIncorrectLabel")).getText();
        verifyThat(text, is("Correct!"));

        verifyThat(numberOfCorrectAnswersAfterClick, is(numberOfCorrectAnswers + 1));
    }


    @Test
    public void clickingIncorrectButtonOutputsIncorrectAndDoesNotIncrementScore(){
        int difficultyMode = rn.nextInt(3);

        if(difficultyMode == 0){
            click("#easyRadioButton");
            buttons = new Button[]{melodicIntervalRecognitionController.unisonButton,
                    melodicIntervalRecognitionController.minorThirdButton,
                    melodicIntervalRecognitionController.majorThirdButton,
                    melodicIntervalRecognitionController.perfectFifthButton,
                    melodicIntervalRecognitionController.octaveButton};
        } else if(difficultyMode == 1){
            click("#mediumRadioButton");
            buttons = new Button[]{melodicIntervalRecognitionController.unisonButton,
                    melodicIntervalRecognitionController.minorSecondButton,
                    melodicIntervalRecognitionController.majorSecondButton,
                    melodicIntervalRecognitionController.minorThirdButton,
                    melodicIntervalRecognitionController.majorThirdButton,
                    melodicIntervalRecognitionController.perfectFifthButton,
                    melodicIntervalRecognitionController.octaveButton};
        } else {
            click("#hardRadioButton");
            buttons = new Button[]{melodicIntervalRecognitionController.unisonButton,
                    melodicIntervalRecognitionController.minorSecondButton,
                    melodicIntervalRecognitionController.majorSecondButton,
                    melodicIntervalRecognitionController.minorThirdButton,
                    melodicIntervalRecognitionController.majorThirdButton,
                    melodicIntervalRecognitionController.perfectFourthButton,
                    melodicIntervalRecognitionController.tritoneButton,
                    melodicIntervalRecognitionController.perfectFifthButton,
                    melodicIntervalRecognitionController.minorSixthButton,
                    melodicIntervalRecognitionController.majorSixthButton,
                    melodicIntervalRecognitionController.minorSeventhButton,
                    melodicIntervalRecognitionController.majorSeventhButton,
                    melodicIntervalRecognitionController.octaveButton};
        }


        click(startButton);

        Button correctButton = melodicIntervalRecognitionController.correctButton;
        int numberOfCorrectAnswers = melodicIntervalRecognitionController.numberOfCorrectAnswers;

        int i = rn.nextInt(buttons.length);

        while(buttons[i] == correctButton){
            i = rn.nextInt(buttons.length);
        }

        click(buttons[i]);

        int numberOfCorrectAnswersAfterClick = melodicIntervalRecognitionController.numberOfCorrectAnswers;

        String text = ((Label) find("#correctIncorrectLabel")).getText();
        verifyThat(text, is("Incorrect."));

        verifyThat(numberOfCorrectAnswersAfterClick, is(numberOfCorrectAnswers));
    }
}