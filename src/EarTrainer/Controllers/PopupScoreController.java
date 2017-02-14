package EarTrainer.Controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by timannoel on 31/01/2017.
 */
public class PopupScoreController implements Initializable{

    @FXML private StackPane stackPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        new SplashScreen().start();
    }

    @FXML
    private void OKButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) stackPane.getScene().getWindow();
        stage.hide();

//        ColorAdjust adj = new ColorAdjust(0, 0, 0, 0);
//        GaussianBlur blur = new GaussianBlur(0); // 55 is just to show edge effect more clearly.
//        adj.setInput(blur);
//        stackPane.setEffect(adj);
    }
//
//    class SplashScreen extends Thread {
//    }
}
