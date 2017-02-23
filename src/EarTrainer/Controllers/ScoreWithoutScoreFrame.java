package EarTrainer.Controllers;

/**
 * Created by timannoel on 23/02/2017.
 */
import javax.swing.JFrame;

import com.softsynth.jmsl.JMSL;
import com.softsynth.jmsl.score.Score;

/**
 * Displays score in your own gui
 *
 *
 * @author Nick Didkovsky
 */

public class ScoreWithoutScoreFrame {

    Score score;

    void buildScore() {
        score = new Score(2, 800, 600);
        score.addMeasure();
        score.addNote(2, 60, 0.5, 0.8);
        score.addNote(2, 72, 0.5, 0.8);
    }

    /**
     * @return the score
     */
    public Score getScore() {
        return score;
    }

    public static void create() {

        // important if you are going to display more than one Score in separateFrames.
        // Each must own its own ScoreCanvas for rendering
        Score.useSharedCanvas(false);

        JFrame f = new JFrame("Just some frame");
        f.setSize(1100, 800);

        f.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                JMSL.closeMusicDevices();
                System.exit(0);
            }
        });

        ScoreWithoutScoreFrame test = new ScoreWithoutScoreFrame();
        test.buildScore();
        f.add(test.getScore().getScoreCanvas().getComponent());

        f.validate();

        f.setVisible(true);

    }
}