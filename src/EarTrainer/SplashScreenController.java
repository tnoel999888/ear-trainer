package EarTrainer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Created by timannoel on 31/01/2017.
 */
public class SplashScreenController implements Initializable{

    @FXML
    private StackPane rootPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new SplashScreen().start();
    }

    class SplashScreen extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(4000);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Parent root = null;
                        try {
                            root = FXMLLoader.load(getClass().getResource("Home.fxml"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Stage primaryStage = new Stage();
                        primaryStage.setTitle("Ear Trainer");
                        primaryStage.setScene(new Scene(root, 300, 275));
                        primaryStage.setMaximized(true);
                        primaryStage.show();
                        rootPane.getScene().getWindow().hide();
                    }
                });
            } catch (InterruptedException ex) {
                Logger.getLogger(SplashScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
