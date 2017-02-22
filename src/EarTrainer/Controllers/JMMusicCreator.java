package EarTrainer.Controllers;

import jm.JMC;
import jm.gui.show.ShowScore;
import jm.music.data.*;
import jm.util.*;

import java.util.Random;

/**
 * A short example which generates a one octave c chromatic scale
 * and writes to a standard MIDI file called ChromaticScale.mid
 * @author Andrew Sorensen and Andrew Brown
 */
public final class JMMusicCreator implements JMC{

    private String getInterval(int i) {
        switch(i){
            case 0: return "unison";
            case 1: return "minor second";
            case 2: return "major second";
            case 3: return "minor third";
            case 4: return "major third";
            case 5: return "perfect fourth";
            case 6: return "tritone";
            case 7: return "perfect fifth";
            case 8: return "minor sixth";
            case 9: return "major sixth";
            case 10: return "minor seventh";
            case 11: return "major seventh";
            case 12: return "octave";
            default: return "";
        }
    }

    public String makeMIDI(){
        Score s = new Score("JMDemo - Scale");
        Part p = new Part(0);
        Phrase phr = new Phrase("Chromatic scale", 0.0);

        // create the scale notes and
        // add them to a phrase
        Note n1 = new Note(C4, C);
        phr.addNote(n1);

        Random rn = new Random();
        int i = rn.nextInt(12) + 1;

        //System.out.println(i);

        Note n2 = new Note(C4+i, C);
        phr.addNote(n2);

        // add the phrase to a part
        p.addPhrase(phr);
        // add the part to the score
        s.addPart(p);

        //write a MIDI file to disk of the score
        Write.midi(s, "/Users/timannoel/ChromaticScale.mid");

        String answer = getInterval(i);

        return answer;
    }

}