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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


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

    @FXML private Button correctButton = new Button();

    @FXML private Label timerLabel;
    @FXML private Label questionLabel;

    @FXML private Button startButton;
    @FXML private Button nextQuestionButton;

    @FXML HBox mediaBar;
    MediaPlayer mediaPlayer;


    int questionNumber;
    int numberOfCorrectAnswers = 0;

    String correctAnswer = "unison";
    private boolean questionAnswered;
    private Timeline timeline;
    private boolean startClicked = false;


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
        startClicked = true;
        questionNumber = 1;
        startTimer();
        questionLabel.setVisible(true);
        startButton.setDisable(true);
        timerLabel.setVisible(true);
        radioButtonsGroup.setDisable(true);
        questionLabel.setText("Question 1");

        //temporary
        correctButton = unisonButton;

        playSound();
    }

    @FXML
    private void NextQuestionButtonClicked(ActionEvent event) throws IOException {
        if (questionNumber != 10) {
            questionNumber++;
            questionLabel.setText("Question " + Integer.toString(questionNumber));
        } else {
            //nextQuestionButton.setText("Score");
            questionLabel.setVisible(false);
            nextQuestionButton.setDisable(true);
            stopTimer();
            loadScore();
        }

        questionAnswered = false;
        nextQuestionButton.setDisable(true);
        resetButtonColours();

        mediaBar.getChildren().clear();
        mediaPlayer.stop();
        playSound();
        //Generate new question
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
        unisonButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        minorSecondButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        majorSecondButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        perfectFourthButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        tritoneButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        minorThirdButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        majorThirdButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        perfectFifthButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        octaveButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        minorSixthButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        majorSixthButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        minorSeventhButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        majorSeventhButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
    }

    @FXML
    private void unisonButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("unison", unisonButton);
        }
    }

    @FXML
    private void minorSecondButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("minor second", minorSecondButton);
        }
    }

    @FXML
    private void majorSecondButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("major second", majorSecondButton);
        }
    }

    @FXML
    private void perfectFourthButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("perfect fourth", perfectFourthButton);
        }
    }

    @FXML
    private void tritoneButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("tritone", tritoneButton);
        }
    }

    @FXML
    private void minorThirdButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("minor third", minorThirdButton);
        }
    }

    @FXML
    private void majorThirdButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("major third", majorThirdButton);
        }
    }

    @FXML
    private void perfectFifthButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("perfect fifth", perfectFifthButton);
        }
    }

    @FXML
    private void octaveButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("octave", octaveButton);
        }
    }

    @FXML
    private void minorSixthButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("minor sixth", minorSixthButton);
        }
    }

    @FXML
    private void majorSixthButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("major sixth", majorSixthButton);
        }
    }

    @FXML
    private void minorSeventhButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("minor seventh", minorSeventhButton);
        }
    }

    @FXML
    private void majorSeventhButtonClicked(ActionEvent event) throws IOException {
        if(!questionAnswered && startClicked) {
            AnswerButtonClicked();
            checkAnswer("major seventh", majorSeventhButton);
        }
    }

    @FXML
    private void AnswerButtonClicked() throws IOException {
        questionAnswered = true;
        nextQuestionButton.setDisable(false);
    }

    private void checkAnswer(String answer, Button button) {
        if(answer != correctAnswer){
            makeButtonRed(button);
        } else {
            numberOfCorrectAnswers++;
        }

        makeButtonGreen(correctButton);

        if(questionNumber == 10){

        }
    }

    private void makeButtonRed(Button button) {
        button.setStyle("-fx-base: #ffb3b3;");
    }

    private void makeButtonGreen(Button correctButton) {
        correctButton.setStyle("-fx-base: #adebad;");
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
        
        this.timeline = timeline;
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    
    private void stopTimer() {
        timerLabel.setVisible(false);
        timeline.stop();
    }

    @FXML
    private void playSound() throws UnsupportedEncodingException {
        Stage stage = (Stage) stackPane.getScene().getWindow();

        final String MEDIA_URL = "/Users/timannoel/Music/Music/Event Horizon/Event Horizon - Fatter.mp3";

        Media media = new Media(new File(MEDIA_URL).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);

        EmbeddedMediaPlayer emp = new EmbeddedMediaPlayer(this, stackPane, mediaPlayer);
        emp.start(stage);
    }
}




