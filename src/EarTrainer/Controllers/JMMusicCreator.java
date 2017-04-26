package EarTrainer.Controllers;

import be.tarsos.dsp.PitchShifter;
import jm.JMC;
import jm.gui.cpn.JGrandStave;
import jm.music.data.*;
import jm.music.tools.Mod;
import jm.util.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import java.awt.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A short example which generates a one octave c chromatic scale
 * and writes to a standard MIDI file called ChromaticScale.mid
 * @author Andrew Sorensen and Andrew Brown
 */
public final class JMMusicCreator implements JMC {

    private Score s = new Score("JMDemo - Scale");
    private Part p = new Part(0);
    private Phrase phr1 = new Phrase("Melodic Interval", 0.0);
    private Phrase phr2 = new Phrase("Melodic Interval", 0.0);
    private CPhrase cphr1 = new CPhrase("Harmonic Interval", 0.0);
    private JGrandStave jScore;


    public JMMusicCreator(JGrandStave jScore) {
        this.jScore = jScore;
    }


    public Phrase getPhrase() {
        return phr2;
    }


    public CPhrase getCPhrase() {
        return cphr1;
    }


    public Score getScore() {
        return s;
    }


    private String getInterval(int i) {
        switch(i){
            case 0: return "unison";

            case -1:
            case 1: return "minor second";

            case -2:
            case 2: return "major second";

            case -3:
            case 3: return "minor third";

            case -4:
            case 4: return "major third";

            case -5:
            case 5: return "perfect fourth";

            case -6:
            case 6: return "tritone";

            case -7:
            case 7: return "perfect fifth";

            case -8:
            case 8: return "minor sixth";

            case -9:
            case 9: return "major sixth";

            case -10:
            case 10: return "minor seventh";

            case -11:
            case 11: return "major seventh";

            case -12:
            case 12: return "octave";

            default: return "";
        }
    }


    private String getNote(Note n2) {
        switch(n2.getPitch()){
            case C3:
            case C5:
            case C4: return "c";

            case CS3:
            case CS5:
            case CS4: return "c sharp";

            case D3:
            case D5:
            case D4: return "d";

            case DS3:
            case DS5:
            case DS4: return "d sharp";

            case E3:
            case E5:
            case E4: return "e";

            case F3:
            case F5:
            case F4: return "f";

            case FS3:
            case FS5:
            case FS4: return "f sharp";

            case G3:
            case G5:
            case G4: return "g";

            case GS3:
            case GS5:
            case GS4: return "g sharp";

            case A3:
            case A5:
            case A4: return "a";

            case AS3:
            case AS5:
            case AS4: return "a sharp";

            case B3:
            case B5:
            case B4: return "b";

            default: return "";
        }
    }


    private void setScore(Phrase phr1) {
        jScore.setPhrase(this.phr1);

        Dimension d = new Dimension();
        d.setSize(600,300);
        jScore.setPreferredSize(d);
        jScore.setMaximumSize(d);

        jScore.removeTitle();
        jScore.setEditable(false);
    }


    public String makeMIDIEasyMelodic(){
        Note n1 = new Note(C4, C);
        phr1.addNote(n1);

        setScore(phr1);

        Random rn = new Random();
        int i = rn.nextInt(5);
        int[] array = {0, 3, 4, 7, 12};
        int interval = array[i];

        System.out.println(i);

        Note n2 = new Note(C4+interval, C);

        phr2.addNote(n1);
        phr2.addNote(n2);

        p.addPhrase(phr2);
        s.addPart(p);

        CPhrase chord = new CPhrase();
        Note[] notes = {n1, n2};
        chord.addChord(notes);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/MelodicInterval.mid");

        return getInterval(interval);
    }


    public String makeMIDIMediumMelodic(){
        Random rn = new Random();
        int i = rn.nextInt(7);
        int[] array = {0, 1, 2, 3, 4, 7, 12};
        int interval = array[i];

        System.out.println(interval);

        Random r2n = new Random();
        int i2 = r2n.nextInt(7);
        int[] roots = {C4,
                        D4,
                        E4,
                        F4,
                        G4,
                        A4,
                        B4,};

        int chosenRoot = roots[i2];

        Note n1 = new Note(chosenRoot, C);
        phr1.addNote(n1);

        setScore(phr1);

        Note n2 = new Note(chosenRoot+interval, C);

        phr2.addNote(n1);
        phr2.addNote(n2);

        p.addPhrase(phr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/MelodicInterval.mid");

        return getInterval(interval);
    }


    public String makeMIDIHardMelodic(){
        Random rn = new Random();
        int i = rn.nextInt(25) -12;

        System.out.println(i);

        Random r2n = new Random();
        int i2 = r2n.nextInt(21);
        int[] roots = {C3, C4, C5,
                       CS3, CS4, CS5,
                       D3, D4, D5,
                       DS3, DS4, DS5,
                       E3, E4, E5,
                       F3, F4, F5,
                       FS3, FS4, FS5,
                       G3, G4, G5,
                       GS3, GS4, GS5,
                       A3, A4, A5,
                       AS3, AS4, AS5,
                       B3, B4, B5};

        int chosenRoot = roots[i2];

        Note n1 = new Note(chosenRoot, C);
        phr1.addNote(n1);

        setScore(phr1);

        Note n2 = new Note(chosenRoot+i, C);

        phr2.addNote(n1);
        phr2.addNote(n2);

        p.addPhrase(phr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/MelodicInterval.mid");

        return getInterval(i);
    }






    public String makeMIDIEasyHarmonic(){
        Note n1 = new Note(C4, C);
        phr1.addNote(n1);

        setScore(phr1);

        Random rn = new Random();
        int i = rn.nextInt(5);
        int[] array = {0, 3, 4, 7, 12};
        int interval = array[i];

        System.out.println(i);

        Note n2 = new Note(C4+interval, C);

        Note[] notes = {n1, n2};
        cphr1.addChord(notes);

        p.addCPhrase(cphr1);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/HarmonicInterval.mid");

        return getInterval(interval);
    }


    public String makeMIDIMediumHarmonic(){
        Random rn = new Random();
        int i = rn.nextInt(7);
        int[] array = {0, 1, 2, 3, 4, 7, 12};
        int interval = array[i];

        System.out.println(interval);

        Random r2n = new Random();
        int i2 = r2n.nextInt(7);
        int[] roots = {C4,
                D4,
                E4,
                F4,
                G4,
                A4,
                B4,};

        int chosenRoot = roots[i2];

        Note n1 = new Note(chosenRoot, C);
        phr1.addNote(n1);

        setScore(phr1);

        Note n2 = new Note(chosenRoot+interval, C);

        Note[] notes = {n1, n2};
        cphr1.addChord(notes);

        p.addCPhrase(cphr1);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/HarmonicInterval.mid");

        return getInterval(interval);
    }


    public String makeMIDIHardHarmonic(){
        Random rn = new Random();
        int i = rn.nextInt(25) -12;

        System.out.println(i);

        Random r2n = new Random();
        int i2 = r2n.nextInt(21);
        int[] roots = {C3, C4, C5,
                CS3, CS4, CS5,
                D3, D4, D5,
                DS3, DS4, DS5,
                E3, E4, E5,
                F3, F4, F5,
                FS3, FS4, FS5,
                G3, G4, G5,
                GS3, GS4, GS5,
                A3, A4, A5,
                AS3, AS4, AS5,
                B3, B4, B5};

        int chosenRoot = roots[i2];

        Note n1 = new Note(chosenRoot, C);
        phr1.addNote(n1);

        setScore(phr1);

        Note n2 = new Note(chosenRoot+i, C);

        Note[] notes = {n1, n2};
        cphr1.addChord(notes);

        p.addCPhrase(cphr1);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/HarmonicInterval.mid");

        return getInterval(i);
    }






    public String makeMIDIEasyPitch(){
        Random rn = new Random();
        int i = rn.nextInt(7);
        int[] array = {0, 2, 4, 5, 7, 9, 11};
        int interval = array[i];

        setScore(phr2);

        Note n = new Note(C4+interval, C);

        phr2.addNote(n);

        p.addPhrase(phr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Pitch.mid");

        return getNote(n);
    }


    public String makeMIDIMediumPitch(){
        Random rn = new Random();
        int i = rn.nextInt(12);
        int[] array = new int[12];

        for(int j = 0; j < 12; j++){
            array[j] = j;
        }

        int interval = array[i];

        setScore(phr2);

        Note n = new Note(C4+interval, C);

        phr2.addNote(n);

        p.addPhrase(phr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Pitch.mid");

        return getNote(n);
    }


    public String makeMIDIHardPitch(){
        Random rn = new Random();
        int i = rn.nextInt(36);
        int[] array = new int[36];

        for(int j = 0; j < 36; j++){
            array[j] = j;
        }

        int interval = array[i];

        setScore(phr2);

        Note n = new Note(C3+interval, C);

        phr2.addNote(n);

        p.addPhrase(phr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Pitch.mid");

        return getNote(n);
    }






    public String makeMIDIEasySharpFlat(){
        Random rn = new Random();
        int i = rn.nextInt(7);
        int[] array = {0, 2, 4, 5, 7, 9, 11};
        int interval = array[i];

        //setScore(phr2);

        Note n = new Note(C4+interval, C);

        phr2.addNote(n);
        phr1.addNote(n);

//        double twentyFiveCentsRatio = 1.014545;
//        double fiftyCentsRatio = 1.029302;
//        //double hundredCentsRatio = 1.059463;
//        double random = ThreadLocalRandom.current().nextDouble(twentyFiveCentsRatio, fiftyCentsRatio);
//
//        System.out.println(n.getFrequency());
//        double freq1 = n.getFrequency() * random;
//        double freq2 = n.getFrequency() / random;
//        double[] freqs = {freq1, freq2};
//        int i3 = rn.nextInt(2);
//        double freq = freqs[i3];
//        System.out.println(freq);

        Note n2 = new Note(C4, C);

        phr1.addNote(n2);

        p.addPhrase(phr1);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/SharpFlat.mid");

        return getNote(n);
    }


    public String makeMIDIMediumSharpFlat(){
        Random rn = new Random();
        int i = rn.nextInt(12);
        int[] array = new int[12];

        for(int j = 0; j < 12; j++){
            array[j] = j;
        }

        int interval = array[i];

        setScore(phr2);

        Note n = new Note(C4+interval, C);

        phr2.addNote(n);

        p.addPhrase(phr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/SharpFlat.mid");

        return getNote(n);
    }


    public String makeMIDIHardSharpFlat(){
        Random rn = new Random();
        int i = rn.nextInt(36);
        int[] array = new int[36];

        for(int j = 0; j < 36; j++){
            array[j] = j;
        }

        int interval = array[i];

        setScore(phr2);

        Note n = new Note(C3+interval, C);

        phr2.addNote(n);

        p.addPhrase(phr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/SharpFlat.mid");

        return getNote(n);
    }
}