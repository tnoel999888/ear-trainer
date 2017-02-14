package EarTrainer.Controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InterruptedIOException;

public class MelodicIntervalRecognitionController {

    @FXML private StackPane stackPane;

    @FXML private ToggleGroup radioButtons;
    @FXML private HBox radioButtonsGroup;
    @FXML private RadioButton easyRadioButton;
    @FXML private RadioButton mediumRadioButton;
    @FXML private RadioButton hardRadioButton;

    @FXML private Button unisonButton;
    @FXML private Button minorSecondButton;
    @FXML private Button majorSecondButton;
    @FXML private Button perfectFourthButton;
    @FXML private Button tritoneButton;
    @FXML private Button minorThirdButton;
    @FXML private Button majorThirdButton;
    @FXML private Button perfectFifthButton;
    @FXML private Button octaveButton;
    @FXML private Button minorSixthButton;
    @FXML private Button majorSixthButton;
    @FXML private Button minorSeventhButton;
    @FXML private Button majorSeventhButton;

    @FXML private Label timerLabel;
    @FXML private Label questionLabel;

    @FXML private Button startButton;

    int questionNumber;
    int numberOfCorrectAnswers = 0;


    @FXML
    private void easyRadioButtonSelected(ActionEvent event) throws IOException {
        minorSecondButton.setDisable(true);
        majorSecondButton.setDisable(true);
        perfectFourthButton.setDisable(true);
        tritoneButton.setDisable(true);
        octaveButton.setDisable(true);
        minorSixthButton.setDisable(true);
        majorSixthButton.setDisable(true);
        minorSeventhButton.setDisable(true);
        majorSeventhButton.setDisable(true);
    }

    @FXML
    private void mediumRadioButtonSelected(ActionEvent event) throws IOException {
        minorSecondButton.setDisable(false);
        majorSecondButton.setDisable(false);
        perfectFourthButton.setDisable(true);
        tritoneButton.setDisable(true);
        octaveButton.setDisable(false);
        minorSixthButton.setDisable(true);
        majorSixthButton.setDisable(true);
        minorSeventhButton.setDisable(true);
        majorSeventhButton.setDisable(true);
    }

    @FXML
    private void hardRadioButtonSelected(ActionEvent event) throws IOException {
        minorSecondButton.setDisable(false);
        majorSecondButton.setDisable(false);
        perfectFourthButton.setDisable(false);
        tritoneButton.setDisable(false);
        octaveButton.setDisable(false);
        minorSixthButton.setDisable(false);
        majorSixthButton.setDisable(false);
        minorSeventhButton.setDisable(false);
        majorSeventhButton.setDisable(false);
    }

    @FXML
    private void BackButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) stackPane.getScene().getWindow();
        stage.hide();
    }

    @FXML
    private void StartButtonClicked(ActionEvent event) throws IOException {
        questionNumber = 1;
        startTimer();
        questionLabel.setVisible(true);
        startButton.setDisable(true);
        radioButtonsGroup.setDisable(true);
        questionLabel.setText("Question 1");
    }

    @FXML
    private void AnswerButtonClicked(ActionEvent event) throws IOException {
        if (questionNumber != 10) {
            questionNumber++;
            questionLabel.setText("Question " + Integer.toString(questionNumber));
        } else {
            startButton.setDisable(false);
            radioButtonsGroup.setDisable(false);
            timerLabel.setVisible(false);
            questionLabel.setVisible(false);

            ColorAdjust adj = new ColorAdjust(0, 0, -0.2, 0);
            GaussianBlur blur = new GaussianBlur(10); // 55 is just to show edge effect more clearly.
            adj.setInput(blur);
            stackPane.setEffect(adj);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/PopupScore.fxml"));
            Parent root = (Parent)loader.load();

            PopupScoreController controller = loader.<PopupScoreController>getController();
            controller.setNumberOfCorrectAnswers(numberOfCorrectAnswers);
            controller.setStackPane(stackPane);

            Stage stage = (Stage) stackPane.getScene().getWindow();

            Stage newStage = new Stage();
            newStage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();
        }
    }

    private void startTimer() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        new EventHandler<ActionEvent>() {
                            int secs = 0;
                            @Override public void handle(ActionEvent actionEvent) {
                                secs++;
                                String strSecs = Integer.toString(secs % 60);
                                String strMins = Integer.toString(secs/60);

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
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}



