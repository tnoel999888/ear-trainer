package EarTrainer.Tests;

/**
 * Created by timannoel on 05/06/2017.
 */
import EarTrainer.Controllers.HarmonicIntervalRecognitionController;
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



public class HarmonicIntervalRecognitionTests extends GuiTest{

    private HarmonicIntervalRecognitionController harmonicIntervalRecognitionController;
    private Button[] buttons;
    private Random rn = new Random();
    private Button nextQuestionButton;
    private Button startButton;



    @Override
    protected Parent getRootNode() {
        Parent root= null;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/HarmonicIntervalRecognition.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        harmonicIntervalRecognitionController = loader.getController();
        nextQuestionButton = harmonicIntervalRecognitionController.nextQuestionButton;
        startButton = harmonicIntervalRecognitionController.startButton;


        Stage newStage = new Stage();
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.show();

        return root;
    }


    @Test
    public void changingDifficultyDisablesEnablesRelevantButtons() throws InterruptedException {
        verify(!harmonicIntervalRecognitionController.unisonButton.isDisabled());
        verify(!harmonicIntervalRecognitionController.minorThirdButton.isDisabled());
        verify(!harmonicIntervalRecognitionController.majorThirdButton.isDisabled());
        verify(!harmonicIntervalRecognitionController.perfectFifthButton.isDisabled());
        verify(!harmonicIntervalRecognitionController.octaveButton.isDisabled());

        verify(harmonicIntervalRecognitionController.minorSecondButton.isDisabled());
        verify(harmonicIntervalRecognitionController.majorSecondButton.isDisabled());
        verify(harmonicIntervalRecognitionController.perfectFourthButton.isDisabled());
        verify(harmonicIntervalRecognitionController.tritoneButton.isDisabled());
        verify(harmonicIntervalRecognitionController.minorSixthButton.isDisabled());
        verify(harmonicIntervalRecognitionController.majorSixthButton.isDisabled());
        verify(harmonicIntervalRecognitionController.minorSeventhButton.isDisabled());
        verify(harmonicIntervalRecognitionController.majorSeventhButton.isDisabled());


        click("#mediumRadioButton");

        verify(!harmonicIntervalRecognitionController.unisonButton.isDisabled());
        verify(!harmonicIntervalRecognitionController.minorThirdButton.isDisabled());
        verify(!harmonicIntervalRecognitionController.majorThirdButton.isDisabled());
        verify(!harmonicIntervalRecognitionController.perfectFifthButton.isDisabled());
        verify(!harmonicIntervalRecognitionController.octaveButton.isDisabled());
        verify(!harmonicIntervalRecognitionController.minorSecondButton.isDisabled());
        verify(!harmonicIntervalRecognitionController.majorSecondButton.isDisabled());

        verify(harmonicIntervalRecognitionController.perfectFourthButton.isDisabled());
        verify(harmonicIntervalRecognitionController.tritoneButton.isDisabled());
        verify(harmonicIntervalRecognitionController.minorSixthButton.isDisabled());
        verify(harmonicIntervalRecognitionController.majorSixthButton.isDisabled());
        verify(harmonicIntervalRecognitionController.minorSeventhButton.isDisabled());
        verify(harmonicIntervalRecognitionController.majorSeventhButton.isDisabled());


        click("#hardRadioButton");

        verify(!harmonicIntervalRecognitionController.unisonButton.isDisabled());
        verify(!harmonicIntervalRecognitionController.minorThirdButton.isDisabled());
        verify(!harmonicIntervalRecognitionController.majorThirdButton.isDisabled());
        verify(!harmonicIntervalRecognitionController.perfectFifthButton.isDisabled());
        verify(!harmonicIntervalRecognitionController.octaveButton.isDisabled());
        verify(!harmonicIntervalRecognitionController.minorSecondButton.isDisabled());
        verify(!harmonicIntervalRecognitionController.majorSecondButton.isDisabled());
        verify(!harmonicIntervalRecognitionController.perfectFourthButton.isDisabled());
        verify(!harmonicIntervalRecognitionController.tritoneButton.isDisabled());
        verify(!harmonicIntervalRecognitionController.minorSixthButton.isDisabled());
        verify(!harmonicIntervalRecognitionController.majorSixthButton.isDisabled());
        verify(!harmonicIntervalRecognitionController.minorSeventhButton.isDisabled());
        verify(!harmonicIntervalRecognitionController.majorSeventhButton.isDisabled());


        click("#easyRadioButton");

        verify(!harmonicIntervalRecognitionController.unisonButton.isDisabled());
        verify(!harmonicIntervalRecognitionController.minorThirdButton.isDisabled());
        verify(!harmonicIntervalRecognitionController.majorThirdButton.isDisabled());
        verify(!harmonicIntervalRecognitionController.perfectFifthButton.isDisabled());
        verify(!harmonicIntervalRecognitionController.octaveButton.isDisabled());

        verify(harmonicIntervalRecognitionController.minorSecondButton.isDisabled());
        verify(harmonicIntervalRecognitionController.majorSecondButton.isDisabled());
        verify(harmonicIntervalRecognitionController.perfectFourthButton.isDisabled());
        verify(harmonicIntervalRecognitionController.tritoneButton.isDisabled());
        verify(harmonicIntervalRecognitionController.minorSixthButton.isDisabled());
        verify(harmonicIntervalRecognitionController.majorSixthButton.isDisabled());
        verify(harmonicIntervalRecognitionController.minorSeventhButton.isDisabled());
        verify(harmonicIntervalRecognitionController.majorSeventhButton.isDisabled());
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
        String correctAnswer = harmonicIntervalRecognitionController.correctAnswer;
        verifyThat(correctAnswer, is(""));


        //Clicking Start sets correctAnswer variable
        click(startButton);
        correctAnswer = harmonicIntervalRecognitionController.correctAnswer;
        verifyThat(correctAnswer, not(""));
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

        Button correctButton = harmonicIntervalRecognitionController.correctButton;
        int numberOfCorrectAnswers = harmonicIntervalRecognitionController.numberOfCorrectAnswers;

        click(correctButton);

        int numberOfCorrectAnswersAfterClick = harmonicIntervalRecognitionController.numberOfCorrectAnswers;

        String text = ((Label) find("#correctIncorrectLabel")).getText();
        verifyThat(text, is("Correct!"));

        verifyThat(numberOfCorrectAnswersAfterClick, is(numberOfCorrectAnswers + 1));
    }


    @Test
    public void clickingStopResetsScore(){
        String startStopButtonText = startButton.getText();
        verifyThat(startStopButtonText, is("Start"));
        click(startButton);

        Button correctButton = harmonicIntervalRecognitionController.correctButton;
        int numberOfCorrectAnswers = harmonicIntervalRecognitionController.numberOfCorrectAnswers;
        verifyThat(numberOfCorrectAnswers, is(0));

        click(correctButton);

        String text = ((Label) find("#correctIncorrectLabel")).getText();
        verifyThat(text, is("Correct!"));
        numberOfCorrectAnswers = harmonicIntervalRecognitionController.numberOfCorrectAnswers;
        verifyThat(numberOfCorrectAnswers, is(1));


        startStopButtonText = startButton.getText();
        verifyThat(startStopButtonText, is("Stop"));
        click(startButton);
        numberOfCorrectAnswers = harmonicIntervalRecognitionController.numberOfCorrectAnswers;
        verifyThat(numberOfCorrectAnswers, is(0));
    }


    @Test
    public void clickingStopResetsQuestionNumber(){
        String startStopButtonText = startButton.getText();
        verifyThat(startStopButtonText, is("Start"));
        click(startButton);

        Button correctButton = harmonicIntervalRecognitionController.correctButton;
        int questionNumber = harmonicIntervalRecognitionController.questionNumber;
        verifyThat(questionNumber, is(1));

        click(correctButton);

        click(nextQuestionButton);

        questionNumber = harmonicIntervalRecognitionController.questionNumber;
        verifyThat(questionNumber, is(2));


        startStopButtonText = startButton.getText();
        verifyThat(startStopButtonText, is("Stop"));
        click(startButton);
        questionNumber = harmonicIntervalRecognitionController.questionNumber;
        verifyThat(questionNumber, is(1));
    }


    @Test
    public void clickingNextButtonIncrementsQuestionNumber(){
        buttons = new Button[]{harmonicIntervalRecognitionController.unisonButton,
                                harmonicIntervalRecognitionController.minorSecondButton,
                                harmonicIntervalRecognitionController.majorSecondButton,
                                harmonicIntervalRecognitionController.minorThirdButton,
                                harmonicIntervalRecognitionController.majorThirdButton,
                                harmonicIntervalRecognitionController.perfectFourthButton,
                                harmonicIntervalRecognitionController.tritoneButton,
                                harmonicIntervalRecognitionController.perfectFifthButton,
                                harmonicIntervalRecognitionController.minorSixthButton,
                                harmonicIntervalRecognitionController.majorSixthButton,
                                harmonicIntervalRecognitionController.minorSeventhButton,
                                harmonicIntervalRecognitionController.majorSeventhButton,
                                harmonicIntervalRecognitionController.octaveButton};


        click("#hardRadioButton");
        click(startButton);


        for(int i = 1; i < 10; i++){
            int randomButton = rn.nextInt(13);
            int questionNumber = harmonicIntervalRecognitionController.questionNumber;
            click(buttons[randomButton]);
            click(nextQuestionButton);
            int nextQuestionNumber = harmonicIntervalRecognitionController.questionNumber;
            verifyThat(nextQuestionNumber, is(questionNumber + 1));
        }
    }


   @Test
    public void nextQuestionButtonTextChangesToScoreOn10thQuestion(){
        buttons = new Button[]{harmonicIntervalRecognitionController.unisonButton,
                                harmonicIntervalRecognitionController.minorSecondButton,
                                harmonicIntervalRecognitionController.majorSecondButton,
                                harmonicIntervalRecognitionController.minorThirdButton,
                                harmonicIntervalRecognitionController.majorThirdButton,
                                harmonicIntervalRecognitionController.perfectFourthButton,
                                harmonicIntervalRecognitionController.tritoneButton,
                                harmonicIntervalRecognitionController.perfectFifthButton,
                                harmonicIntervalRecognitionController.minorSixthButton,
                                harmonicIntervalRecognitionController.majorSixthButton,
                                harmonicIntervalRecognitionController.minorSeventhButton,
                                harmonicIntervalRecognitionController.majorSeventhButton,
                                harmonicIntervalRecognitionController.octaveButton};


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
    public void clickingIncorrectButtonEasyModeOutputsIncorrectAndDoesNotIncrementScore(){
        buttons = new Button[]{harmonicIntervalRecognitionController.unisonButton,
                                harmonicIntervalRecognitionController.minorThirdButton,
                                harmonicIntervalRecognitionController.majorThirdButton,
                                harmonicIntervalRecognitionController.perfectFifthButton,
                                harmonicIntervalRecognitionController.octaveButton};


        click(startButton);

        Button correctButton = harmonicIntervalRecognitionController.correctButton;
        int numberOfCorrectAnswers = harmonicIntervalRecognitionController.numberOfCorrectAnswers;

        int i = rn.nextInt(buttons.length);

        while(buttons[i] == correctButton){
            i = rn.nextInt(buttons.length);
        }

        click(buttons[i]);

        int numberOfCorrectAnswersAfterClick = harmonicIntervalRecognitionController.numberOfCorrectAnswers;

        String text = ((Label) find("#correctIncorrectLabel")).getText();
        verifyThat(text, is("Incorrect."));

        verifyThat(numberOfCorrectAnswersAfterClick, is(numberOfCorrectAnswers));
    }


    @Test
    public void clickingIncorrectButtonMediumModeOutputsIncorrectAndDoesNotIncrementScore(){
        buttons = new Button[]{harmonicIntervalRecognitionController.unisonButton,
                harmonicIntervalRecognitionController.minorSecondButton,
                harmonicIntervalRecognitionController.majorSecondButton,
                harmonicIntervalRecognitionController.minorThirdButton,
                harmonicIntervalRecognitionController.majorThirdButton,
                harmonicIntervalRecognitionController.perfectFifthButton,
                harmonicIntervalRecognitionController.octaveButton};

        click("#mediumRadioButton");
        click(startButton);

        Button correctButton = harmonicIntervalRecognitionController.correctButton;
        int numberOfCorrectAnswers = harmonicIntervalRecognitionController.numberOfCorrectAnswers;

        int i = rn.nextInt(buttons.length);

        while(buttons[i] == correctButton){
            i = rn.nextInt(buttons.length);
        }

        click(buttons[i]);

        int numberOfCorrectAnswersAfterClick = harmonicIntervalRecognitionController.numberOfCorrectAnswers;

        String text = ((Label) find("#correctIncorrectLabel")).getText();
        verifyThat(text, is("Incorrect."));

        verifyThat(numberOfCorrectAnswersAfterClick, is(numberOfCorrectAnswers));

    }


    @Test
    public void clickingIncorrectButtonHardModeOutputsIncorrectAndDoesNotIncrementScore(){
        buttons = new Button[]{harmonicIntervalRecognitionController.unisonButton,
                                harmonicIntervalRecognitionController.minorSecondButton,
                                harmonicIntervalRecognitionController.majorSecondButton,
                                harmonicIntervalRecognitionController.minorThirdButton,
                                harmonicIntervalRecognitionController.majorThirdButton,
                                harmonicIntervalRecognitionController.perfectFourthButton,
                                harmonicIntervalRecognitionController.tritoneButton,
                                harmonicIntervalRecognitionController.perfectFifthButton,
                                harmonicIntervalRecognitionController.minorSixthButton,
                                harmonicIntervalRecognitionController.majorSixthButton,
                                harmonicIntervalRecognitionController.minorSeventhButton,
                                harmonicIntervalRecognitionController.majorSeventhButton,
                                harmonicIntervalRecognitionController.octaveButton};


        click("#hardRadioButton");
        click(startButton);

        Button correctButton = harmonicIntervalRecognitionController.correctButton;
        int numberOfCorrectAnswers = harmonicIntervalRecognitionController.numberOfCorrectAnswers;

        int i = rn.nextInt(buttons.length);

        while(buttons[i] == correctButton){
            i = rn.nextInt(buttons.length);
        }

        click(buttons[i]);

        int numberOfCorrectAnswersAfterClick = harmonicIntervalRecognitionController.numberOfCorrectAnswers;

        String text = ((Label) find("#correctIncorrectLabel")).getText();
        verifyThat(text, is("Incorrect."));

        verifyThat(numberOfCorrectAnswersAfterClick, is(numberOfCorrectAnswers));
    }
}