package EarTrainer.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jm.gui.cpn.JGrandStave;
import jm.music.data.Phrase;

import javax.sound.midi.Sequencer;
import java.awt.*;
import java.io.IOException;



/**
 * Created by timannoel on 31/01/2017.
 */
public class PopupScoreController {

    @FXML private StackPane stackPane;
    @FXML private StackPane prevPageStackPane;
    @FXML private Label scoreLabel;
    @FXML private Label timeLabel;
    @FXML private ImageView scoreImage;
    @FXML private Image image;

    private Phrase emptyPhr;
    private JGrandStave score;
    private JGrandStave scoreBottom;
    private JGrandStave scoreTop;


    @FXML
    private void OKButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) stackPane.getScene().getWindow();
        stage.hide();

        ColorAdjust adj = new ColorAdjust(0, 0, 0, 0);
        GaussianBlur blur = new GaussianBlur(0); // 55 is just to show edge effect more clearly.
        adj.setInput(blur);
        prevPageStackPane.setEffect(adj);
        prevPageStackPane.setDisable(false);



        score.setPhrase(emptyPhr);
        scoreBottom.setPhrase(emptyPhr);
        scoreTop.setPhrase(emptyPhr);

        Dimension d = new Dimension();
        d.setSize(600,300);

        score.setPreferredSize(d);
        score.setMaximumSize(d);
        score.removeTitle();
        score.setEditable(true);

        scoreBottom.setPreferredSize(d);
        scoreBottom.setMaximumSize(d);
        scoreBottom.removeTitle();
        scoreBottom.setEditable(true);

        scoreTop.setPreferredSize(d);
        scoreTop.setMaximumSize(d);
        scoreTop.removeTitle();
        scoreTop.setEditable(true);
    }


    public void setNumberOfCorrectAnswers(int numberOfCorrectAnswers) {
        scoreLabel.setText(Integer.toString(numberOfCorrectAnswers) + "/10");
    }


    public void setTime(String mins, String secs) {
        timeLabel.setText(mins + ":" + secs);
    }


    public void setStackPane(StackPane prevPageStackPane) {
        this.prevPageStackPane = prevPageStackPane;
    }


    public void setImageToUse(String imageToUse) {
        image = new Image(getClass().getResource(imageToUse).toExternalForm());
        scoreImage.setImage(image);
    }


    public void setEmptyPhr(Phrase emptyPhr) {
        this.emptyPhr = emptyPhr;
    }


    public void setScore(JGrandStave score) {
        this.score = score;
    }


    public void setScoreBottom(JGrandStave scoreBottom) {
        this.scoreBottom = scoreBottom;
    }


    public void setScoreTop(JGrandStave scoreTop) {
        this.scoreTop = scoreTop;
    }
}
