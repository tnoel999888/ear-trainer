package EarTrainer.Tests;

/**
 * Created by timannoel on 05/06/2017.
 */
import EarTrainer.Controllers.PitchRecognitionController;
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



public class PitchRecognitionTests extends GuiTest{

    private PitchRecognitionController pitchRecognitionController;
    private Button[] buttons;
    private Random rn = new Random();
    private Button nextQuestionButton;
    private Button startButton;



    @Override
    protected Parent getRootNode() {
        Parent root= null;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/PitchRecognition.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        pitchRecognitionController = loader.getController();
        nextQuestionButton = pitchRecognitionController.nextQuestionButton;
        startButton = pitchRecognitionController.startButton;


        Stage newStage = new Stage();
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.show();

        return root;
    }


    @Test
    public void changingDifficultyDisablesEnablesRelevantButtons() throws InterruptedException {
        verify(!pitchRecognitionController.cButton.isDisabled());
        verify(!pitchRecognitionController.dButton.isDisabled());
        verify(!pitchRecognitionController.eButton.isDisabled());
        verify(!pitchRecognitionController.fButton.isDisabled());
        verify(!pitchRecognitionController.gButton.isDisabled());
        verify(!pitchRecognitionController.aButton.isDisabled());
        verify(!pitchRecognitionController.bButton.isDisabled());

        verify(pitchRecognitionController.cSharpButton.isDisabled());
        verify(pitchRecognitionController.dSharpButton.isDisabled());
        verify(pitchRecognitionController.fSharpButton.isDisabled());
        verify(pitchRecognitionController.gSharpButton.isDisabled());
        verify(pitchRecognitionController.aSharpButton.isDisabled());


        click("#mediumRadioButton");

        verify(!pitchRecognitionController.cButton.isDisabled());
        verify(!pitchRecognitionController.dButton.isDisabled());
        verify(!pitchRecognitionController.eButton.isDisabled());
        verify(!pitchRecognitionController.fButton.isDisabled());
        verify(!pitchRecognitionController.gButton.isDisabled());
        verify(!pitchRecognitionController.aButton.isDisabled());
        verify(!pitchRecognitionController.bButton.isDisabled());
        verify(!pitchRecognitionController.cSharpButton.isDisabled());
        verify(!pitchRecognitionController.dSharpButton.isDisabled());
        verify(!pitchRecognitionController.fSharpButton.isDisabled());
        verify(!pitchRecognitionController.gSharpButton.isDisabled());
        verify(!pitchRecognitionController.aSharpButton.isDisabled());


        click("#hardRadioButton");

        verify(!pitchRecognitionController.cButton.isDisabled());
        verify(!pitchRecognitionController.dButton.isDisabled());
        verify(!pitchRecognitionController.eButton.isDisabled());
        verify(!pitchRecognitionController.fButton.isDisabled());
        verify(!pitchRecognitionController.gButton.isDisabled());
        verify(!pitchRecognitionController.aButton.isDisabled());
        verify(!pitchRecognitionController.bButton.isDisabled());
        verify(!pitchRecognitionController.cSharpButton.isDisabled());
        verify(!pitchRecognitionController.dSharpButton.isDisabled());
        verify(!pitchRecognitionController.fSharpButton.isDisabled());
        verify(!pitchRecognitionController.gSharpButton.isDisabled());
        verify(!pitchRecognitionController.aSharpButton.isDisabled());


        click("#easyRadioButton");

        verify(!pitchRecognitionController.cButton.isDisabled());
        verify(!pitchRecognitionController.dButton.isDisabled());
        verify(!pitchRecognitionController.eButton.isDisabled());
        verify(!pitchRecognitionController.fButton.isDisabled());
        verify(!pitchRecognitionController.gButton.isDisabled());
        verify(!pitchRecognitionController.aButton.isDisabled());
        verify(!pitchRecognitionController.bButton.isDisabled());

        verify(pitchRecognitionController.cSharpButton.isDisabled());
        verify(pitchRecognitionController.dSharpButton.isDisabled());
        verify(pitchRecognitionController.fSharpButton.isDisabled());
        verify(pitchRecognitionController.gSharpButton.isDisabled());
        verify(pitchRecognitionController.aSharpButton.isDisabled());
    }


    @Test
    public void difficultyDescriptionsUpdate() throws InterruptedException {
        String text = ((Label) find("#difficultyDescriptionLabel")).getText();
        verifyThat(text, is("White notes only. 1 Octave."));

        click("#mediumRadioButton");
        text = ((Label) find("#difficultyDescriptionLabel")).getText();
        verifyThat(text, is("All notes. 1 Octave."));

        click("#hardRadioButton");
        text = ((Label) find("#difficultyDescriptionLabel")).getText();
        verifyThat(text, is("All notes. 3 Octaves."));

        click("#easyRadioButton");
        text = ((Label) find("#difficultyDescriptionLabel")).getText();
        verifyThat(text, is("White notes only. 1 Octave."));
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
        String correctAnswer = pitchRecognitionController.correctAnswer;
        verifyThat(correctAnswer, is(""));


        //Clicking Start sets correctAnswer variable
        click(startButton);
        correctAnswer = pitchRecognitionController.correctAnswer;
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

        Button correctButton = pitchRecognitionController.correctButton;
        int numberOfCorrectAnswers = pitchRecognitionController.numberOfCorrectAnswers;

        click(correctButton);

        int numberOfCorrectAnswersAfterClick = pitchRecognitionController.numberOfCorrectAnswers;

        String text = ((Label) find("#correctIncorrectLabel")).getText();
        verifyThat(text, is("Correct!"));

        verifyThat(numberOfCorrectAnswersAfterClick, is(numberOfCorrectAnswers + 1));
    }


    @Test
    public void clickingStopResetsScore(){
        String startStopButtonText = startButton.getText();
        verifyThat(startStopButtonText, is("Start"));
        click(startButton);

        Button correctButton = pitchRecognitionController.correctButton;
        int numberOfCorrectAnswers = pitchRecognitionController.numberOfCorrectAnswers;
        verifyThat(numberOfCorrectAnswers, is(0));

        click(correctButton);

        String text = ((Label) find("#correctIncorrectLabel")).getText();
        verifyThat(text, is("Correct!"));
        numberOfCorrectAnswers = pitchRecognitionController.numberOfCorrectAnswers;
        verifyThat(numberOfCorrectAnswers, is(1));


        startStopButtonText = startButton.getText();
        verifyThat(startStopButtonText, is("Stop"));
        click(startButton);
        numberOfCorrectAnswers = pitchRecognitionController.numberOfCorrectAnswers;
        verifyThat(numberOfCorrectAnswers, is(0));
    }


    @Test
    public void clickingStopResetsQuestionNumber(){
        String startStopButtonText = startButton.getText();
        verifyThat(startStopButtonText, is("Start"));
        click(startButton);

        Button correctButton = pitchRecognitionController.correctButton;
        int questionNumber = pitchRecognitionController.questionNumber;
        verifyThat(questionNumber, is(1));

        click(correctButton);

        click(nextQuestionButton);

        questionNumber = pitchRecognitionController.questionNumber;
        verifyThat(questionNumber, is(2));


        startStopButtonText = startButton.getText();
        verifyThat(startStopButtonText, is("Stop"));
        click(startButton);
        questionNumber = pitchRecognitionController.questionNumber;
        verifyThat(questionNumber, is(1));
    }


    @Test
    public void clickingNextButtonIncrementsQuestionNumber(){
        buttons = new Button[]{pitchRecognitionController.cButton,
                pitchRecognitionController.cSharpButton,
                pitchRecognitionController.dButton,
                pitchRecognitionController.dSharpButton,
                pitchRecognitionController.eButton,
                pitchRecognitionController.fButton,
                pitchRecognitionController.fSharpButton,
                pitchRecognitionController.gButton,
                pitchRecognitionController.gSharpButton,
                pitchRecognitionController.aButton,
                pitchRecognitionController.aSharpButton,
                pitchRecognitionController.bButton};

        click("#hardRadioButton");
        click(startButton);


        for(int i = 1; i < 10; i++){
            int randomButton = rn.nextInt(12);
            int questionNumber = pitchRecognitionController.questionNumber;
            click(buttons[randomButton]);
            click(nextQuestionButton);
            int nextQuestionNumber = pitchRecognitionController.questionNumber;
            verifyThat(nextQuestionNumber, is(questionNumber + 1));
        }
    }


    @Test
    public void nextQuestionButtonTextChangesToScoreOn10thQuestion(){
        buttons = new Button[]{pitchRecognitionController.cButton,
                pitchRecognitionController.cSharpButton,
                pitchRecognitionController.dButton,
                pitchRecognitionController.dSharpButton,
                pitchRecognitionController.eButton,
                pitchRecognitionController.fButton,
                pitchRecognitionController.fSharpButton,
                pitchRecognitionController.gButton,
                pitchRecognitionController.gSharpButton,
                pitchRecognitionController.aButton,
                pitchRecognitionController.aSharpButton,
                pitchRecognitionController.bButton};

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
        buttons = new Button[]{pitchRecognitionController.cButton,
                pitchRecognitionController.dButton,
                pitchRecognitionController.eButton,
                pitchRecognitionController.fButton,
                pitchRecognitionController.gButton,
                pitchRecognitionController.aButton,
                pitchRecognitionController.bButton};

        click(startButton);

        Button correctButton = pitchRecognitionController.correctButton;
        int numberOfCorrectAnswers = pitchRecognitionController.numberOfCorrectAnswers;

        int i = rn.nextInt(buttons.length);

        while(buttons[i] == correctButton){
            i = rn.nextInt(buttons.length);
        }

        click(buttons[i]);

        int numberOfCorrectAnswersAfterClick = pitchRecognitionController.numberOfCorrectAnswers;

        String text = ((Label) find("#correctIncorrectLabel")).getText();
        verifyThat(text, is("Incorrect."));

        verifyThat(numberOfCorrectAnswersAfterClick, is(numberOfCorrectAnswers));

    }


    @Test
    public void clickingIncorrectButtonMediumModeOutputsIncorrectAndDoesNotIncrementScore(){
        buttons = new Button[]{pitchRecognitionController.cButton,
                pitchRecognitionController.cSharpButton,
                pitchRecognitionController.dButton,
                pitchRecognitionController.dSharpButton,
                pitchRecognitionController.eButton,
                pitchRecognitionController.fButton,
                pitchRecognitionController.fSharpButton,
                pitchRecognitionController.gButton,
                pitchRecognitionController.gSharpButton,
                pitchRecognitionController.aButton,
                pitchRecognitionController.aSharpButton,
                pitchRecognitionController.bButton};

        click("#mediumRadioButton");
        click(startButton);

        Button correctButton = pitchRecognitionController.correctButton;
        int numberOfCorrectAnswers = pitchRecognitionController.numberOfCorrectAnswers;

        int i = rn.nextInt(buttons.length);

        while(buttons[i] == correctButton){
            i = rn.nextInt(buttons.length);
        }

        click(buttons[i]);

        int numberOfCorrectAnswersAfterClick = pitchRecognitionController.numberOfCorrectAnswers;

        String text = ((Label) find("#correctIncorrectLabel")).getText();
        verifyThat(text, is("Incorrect."));

        verifyThat(numberOfCorrectAnswersAfterClick, is(numberOfCorrectAnswers));

    }


    @Test
    public void clickingIncorrectButtonHardModeOutputsIncorrectAndDoesNotIncrementScore(){
        buttons = new Button[]{pitchRecognitionController.cButton,
                pitchRecognitionController.cSharpButton,
                pitchRecognitionController.dButton,
                pitchRecognitionController.dSharpButton,
                pitchRecognitionController.eButton,
                pitchRecognitionController.fButton,
                pitchRecognitionController.fSharpButton,
                pitchRecognitionController.gButton,
                pitchRecognitionController.gSharpButton,
                pitchRecognitionController.aButton,
                pitchRecognitionController.aSharpButton,
                pitchRecognitionController.bButton};

        click("#hardRadioButton");
        click(startButton);

        Button correctButton = pitchRecognitionController.correctButton;
        int numberOfCorrectAnswers = pitchRecognitionController.numberOfCorrectAnswers;

        int i = rn.nextInt(buttons.length);

        while(buttons[i] == correctButton){
            i = rn.nextInt(buttons.length);
        }

        click(buttons[i]);

        int numberOfCorrectAnswersAfterClick = pitchRecognitionController.numberOfCorrectAnswers;

        String text = ((Label) find("#correctIncorrectLabel")).getText();
        verifyThat(text, is("Incorrect."));

        verifyThat(numberOfCorrectAnswersAfterClick, is(numberOfCorrectAnswers));
    }
}