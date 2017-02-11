package EarTrainer.Controllers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HomeController {

    @FXML private StackPane ap;

    @FXML
    private void PitchRecognitionButtonClicked(ActionEvent event) throws IOException {
        ButtonClicked(event, "../Views/PitchRecognition.fxml");
    }

    @FXML
    private void MelodicIntervalRecognitionButtonClicked(ActionEvent event) throws IOException {
        ButtonClicked(event, "../Views/MelodicIntervalRecognition.fxml");
    }

    @FXML
    private void HarmonicIntervalRecognitionButtonClicked(ActionEvent event) throws IOException {
        ButtonClicked(event, "../Views/HarmonicIntervalRecognition.fxml");
    }

    @FXML
    private void SharpFlatRecognitionButtonClicked(ActionEvent event) throws IOException {
        ButtonClicked(event, "../Views/SharpFlatRecognition.fxml");
    }

    @FXML
    private void CadenceRecognitionButtonClicked(ActionEvent event) throws IOException {
        ButtonClicked(event, "../Views/CadenceRecognition.fxml");
    }

    @FXML
    private void ModulationRecognitionButtonClicked(ActionEvent event) throws IOException {
        ButtonClicked(event, "../Views/ModulationRecognition.fxml");
    }

    public void ButtonClicked(ActionEvent e, String name) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(name));
        Parent root = (Parent)loader.load();
        Stage stage = (Stage) ap.getScene().getWindow();
        //stage.hide();

        Stage newStage = new Stage();
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.setMaximized(true);
        newStage.show();
    }
}