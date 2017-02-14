package EarTrainer.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
//import java.awt.*;
import java.io.IOException;


/**
 * Created by timannoel on 31/01/2017.
 */
public class PopupScoreController {

    @FXML private StackPane stackPane;
    @FXML private StackPane prevPageStackPane;
    @FXML private Label scoreLabel;

    int numberOfCorrectAnswers;


    @FXML
    private void OKButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) stackPane.getScene().getWindow();
        stage.hide();

        ColorAdjust adj = new ColorAdjust(0, 0, 0, 0);
        GaussianBlur blur = new GaussianBlur(0); // 55 is just to show edge effect more clearly.
        adj.setInput(blur);
        prevPageStackPane.setEffect(adj);

//        ColorAdjust adj = new ColorAdjust(0, 0, 0, 0);
//        GaussianBlur blur = new GaussianBlur(0); // 55 is just to show edge effect more clearly.
//        adj.setInput(blur);
//        stackPane.setEffect(adj);
    }

    public void setNumberOfCorrectAnswers(int numberOfCorrectAnswers) {
        this.numberOfCorrectAnswers = numberOfCorrectAnswers;
        scoreLabel.setText(Integer.toString(this.numberOfCorrectAnswers) + "/10");
    }

    public void setStackPane(StackPane prevPageStackPane) {
        this.prevPageStackPane = prevPageStackPane;
    }
//
//    class SplashScreen extends Thread {
//    }
}
