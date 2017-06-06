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
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.loadui.testfx.Assertions.verifyThat;



public class PitchRecognitionTests extends GuiTest{

    private PitchRecognitionController pitchRecognitionController;
    private Button[] buttons;
    private Random rn = new Random();


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


        Stage newStage = new Stage();
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.show();

        return root;
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
        String text = ((Button) find("#lion-default")).getText();
        verifyThat(text, is("Start"));

        click("#lion-default");
        text = ((Button) find("#lion-default")).getText();
        verifyThat(text, is("Stop"));

        click("#lion-default");
        text = ((Button) find("#lion-default")).getText();
        verifyThat(text, is("Start"));
    }


    @Test
    public void clickingStartSetsCorrectAnswer(){
        String correctAnswer = pitchRecognitionController.correctAnswer;
        verifyThat(correctAnswer, is(""));


        //Clicking Start sets correctAnswer variable
        click("#lion-default");
        correctAnswer = pitchRecognitionController.correctAnswer;
        verifyThat(correctAnswer, not(""));
    }


    @Test
    public void clickingCorrectButtonOutputsCorrectAndIncrementsScore(){
        click("#lion-default");

        Button correctButton = pitchRecognitionController.correctButton;
        int numberOfCorrectAnswers = pitchRecognitionController.numberOfCorrectAnswers;

        click(correctButton);

        int numberOfCorrectAnswersAfterClick = pitchRecognitionController.numberOfCorrectAnswers;

        String text = ((Label) find("#correctIncorrectLabel")).getText();
        verifyThat(text, is("Correct!"));

        verifyThat(numberOfCorrectAnswersAfterClick, is(numberOfCorrectAnswers + 1));
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

        click("#lion-default");

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
        click("#lion-default");

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
        click("#lion-default");

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