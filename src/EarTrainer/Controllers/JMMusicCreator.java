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
public final class JMMusicCreator implements JMC {

    Phrase phr2 = new Phrase("Melodic Interval", 0.0);
    CPhrase cphr2 = new CPhrase("Harmonic Interval", 0.0);

    public Phrase getPhrase() {
        return phr2;
    }

    public CPhrase getCPhrase() {
        return cphr2;
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


    public String makeMIDIEasyMelodic(){
        Score s = new Score("JMDemo - Scale");
        Part p = new Part(0);
        Phrase phr1 = new Phrase("Chromatic scale", 0.0);

        Note n1 = new Note(C4, C);
        phr1.addNote(n1);

        View.notate(phr1, 20, 100);

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
        Score s = new Score("JMDemo - Scale");
        Part p = new Part(0);
        Phrase phr1 = new Phrase("Chromatic scale", 0.0);

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

        View.notate(phr1, 20, 100);

        Note n2 = new Note(chosenRoot+interval, C);

        phr2.addNote(n1);
        phr2.addNote(n2);

        p.addPhrase(phr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/MelodicInterval.mid");

        return getInterval(interval);
    }


    public String makeMIDIHardMelodic(){
        Score s = new Score("JMDemo - Scale");
        Part p = new Part(0);
        Phrase phr = new Phrase("Chromatic scale", 0.0);

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
        phr.addNote(n1);

        View.notate(phr, 20, 100);

        Note n2 = new Note(chosenRoot+i, C);

        phr2.addNote(n1);
        phr2.addNote(n2);

        p.addPhrase(phr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/MelodicInterval.mid");

        return getInterval(i);
    }


    public String makeMIDIEasyHarmonic(){
        Score s = new Score("JMDemo - Scale");
        Part p = new Part(0);
        Phrase phr1 = new Phrase("Chromatic scale", 0.0);

        Note n1 = new Note(C4, C);
        phr1.addNote(n1);

        View.notate(phr1, 20, 100);

        Random rn = new Random();
        int i = rn.nextInt(5);
        int[] array = {0, 3, 4, 7, 12};
        int interval = array[i];

        System.out.println(i);

        Note n2 = new Note(C4+interval, C);

        Note[] notes = {n1, n2};
        cphr2.addChord(notes);

        p.addCPhrase(cphr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/HarmonicInterval.mid");

        return getInterval(interval);
    }


    public String makeMIDIMediumHarmonic(){
        Score s = new Score("JMDemo - Scale");
        Part p = new Part(0);
        Phrase phr1 = new Phrase("Chromatic scale", 0.0);

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

        View.notate(phr1, 20, 100);

        Note n2 = new Note(chosenRoot+interval, C);

        Note[] notes = {n1, n2};
        cphr2.addChord(notes);

        p.addCPhrase(cphr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/HarmonicInterval.mid");

        return getInterval(interval);
    }


    public String makeMIDIHardHarmonic(){
        Score s = new Score("JMDemo - Scale");
        Part p = new Part(0);
        Phrase phr = new Phrase("Chromatic scale", 0.0);

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
        phr.addNote(n1);

        View.notate(phr, 20, 100);

        Note n2 = new Note(chosenRoot+i, C);

        Note[] notes = {n1, n2};
        cphr2.addChord(notes);

        p.addCPhrase(cphr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/HarmonicInterval.mid");

        return getInterval(i);
    }
}