package EarTrainer.Controllers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HomeController{


    @FXML
    void PitchRecognitionButtonClicked() throws IOException {
        ButtonClicked("../Views/PitchRecognition.fxml");
    }


    @FXML
    private void MelodicIntervalRecognitionButtonClicked(ActionEvent event) throws IOException {
        ButtonClicked("../Views/MelodicIntervalRecognition.fxml");
    }


    @FXML
    private void HarmonicIntervalRecognitionButtonClicked(ActionEvent event) throws IOException {
        ButtonClicked("../Views/HarmonicIntervalRecognition.fxml");
    }


    @FXML
    private void SharpFlatRecognitionButtonClicked(ActionEvent event) throws IOException {
        ButtonClicked("../Views/SharpFlatRecognition.fxml");
    }


    @FXML
    private void CadenceRecognitionButtonClicked(ActionEvent event) throws IOException {
        ButtonClicked("../Views/CadenceRecognition.fxml");
    }


    @FXML
    private void ModulationRecognitionButtonClicked(ActionEvent event) throws IOException {
        ButtonClicked("../Views/ModulationRecognition.fxml");
    }


    @FXML
    private void VoiceTunerButtonClicked(ActionEvent event) throws IOException {
        ButtonClicked("../Views/VoiceTuner.fxml");
    }


    @FXML
    private void WrongNoteIdentificationButtonClicked(ActionEvent event) throws IOException {
        ButtonClicked("../Views/WrongNoteIdentification.fxml");
    }


    private void ButtonClicked(String name) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(name));
        Parent root = loader.load();

        Stage newStage = new Stage();
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.show();
    }
}
