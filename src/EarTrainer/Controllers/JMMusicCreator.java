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
    private Random rn = new Random();

    private Score s = new Score("JMDemo - Scale");
    private Part p = new Part(0);
    private JGrandStave jScore;

    private Phrase phr1 = new Phrase("Melodic Interval", 0.0);
    private Phrase phr2 = new Phrase("Melodic Interval", 0.0);
//    private CPhrase cphr1 = new CPhrase("Harmonic Interval", 0.0);
    private CPhrase cphr1 = new CPhrase();
    private CPhrase cphr2 = new CPhrase();
    private CPhrase cphr3 = new CPhrase();
    private CPhrase cphr4 = new CPhrase();

    private Note[] scaleChord1;
    private Note[] scaleChord2;
    private Note[] scaleChord3;
    private Note[] scaleChord4;
    private Note[] scaleChord5;
    private Note[] scaleChord6;
    private Note[] scaleChord7;
    private Note[] scaleChord8;
    private Note[][] scaleChords = {scaleChord1, scaleChord2, scaleChord3, scaleChord4, scaleChord5, scaleChord6, scaleChord7, scaleChord8};
    private Note[][] scaleChords1And6 = {scaleChord1, scaleChord6};
    private Note[][] randomAnd5 = {scaleChord1, scaleChord5};
    private Note[][] randomAnd5And4 = {scaleChord1, scaleChord5, scaleChord4};


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






    public void makeScaleAndChords(){
        int[] notes = {A4, AS4, B4, C4, CS4, D4, DS4, E4, F4, FS4, G4, GS4};


        int i = rn.nextInt(12);

        //Minor scale
        int[] minorScale = new int[8];
        minorScale[0] = notes[i];
        minorScale[1] = notes[(i + 2) % 12];
        minorScale[2] = notes[(i + 3) % 12];
        minorScale[3] = notes[(i + 5) % 12];
        minorScale[4] = notes[(i + 7) % 12];
        minorScale[5] = notes[(i + 8) % 12];
        minorScale[6] = notes[(i + 10) % 12];
        minorScale[7] = notes[(i + 12) % 12];

        scaleChord1 = new Note[] { new Note(minorScale[0], C), new Note(minorScale[2], C), new Note(minorScale[4], C)};
        scaleChord2 = new Note[] { new Note(minorScale[1], C), new Note(minorScale[3], C), new Note(minorScale[5], C)};
        scaleChord3 = new Note[] { new Note(minorScale[2], C), new Note(minorScale[4], C), new Note(minorScale[6], C)};
        scaleChord4 = new Note[] { new Note(minorScale[3], C), new Note(minorScale[5], C), new Note(minorScale[7], C)};
        scaleChord5 = new Note[] { new Note(minorScale[4], C), new Note(minorScale[6], C), new Note(minorScale[0], C)};
        scaleChord6 = new Note[] { new Note(minorScale[5], C), new Note(minorScale[7], C), new Note(minorScale[1], C)};
        scaleChord7 = new Note[] { new Note(minorScale[6], C), new Note(minorScale[0], C), new Note(minorScale[2], C)};
        scaleChord8 = new Note[] { new Note(minorScale[7], C), new Note(minorScale[1], C), new Note(minorScale[3], C)};

        scaleChords = new Note[][]{scaleChord1, scaleChord2, scaleChord3, scaleChord4, scaleChord5, scaleChord6, scaleChord7, scaleChord8};
        scaleChords1And6 = new Note[][] {scaleChord1, scaleChord6};
    }


    public String makeMIDIEasyCadence(){
        makeScaleAndChords();

        int i2 = rn.nextInt(8);
        int i3 = rn.nextInt(8);

        cphr1.addChord(scaleChords[i2]);
        cphr2.addChord(scaleChords[i3]);
        cphr3.addChord(scaleChord5);

        int i4 = rn.nextInt(2);
        cphr4.addChord(scaleChords1And6[i4]);

        p.addCPhrase(cphr1);
        p.addCPhrase(cphr2);
        p.addCPhrase(cphr3);
        p.addCPhrase(cphr4);

        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Cadence.mid");

        if(i4 == 0){
            return "perfect";
        } else {
            return "interruptive";
        }
    }


    public String makeMIDIMediumCadence(){
        makeScaleAndChords();

        int i2 = rn.nextInt(8);
        int i3 = rn.nextInt(8);
        int i4 = rn.nextInt(8);

        cphr1.addChord(scaleChords[i2]);
        cphr2.addChord(scaleChords[i3]);

        randomAnd5 = new Note[][]{scaleChords[i4], scaleChord5};
        int i5 = rn.nextInt(2);
        cphr3.addChord(randomAnd5[i5]);
        int i6 = -1;

        if(i5 == 0) {
            cphr4.addChord(scaleChord5);
        } else {
            i6 = rn.nextInt(2);
            cphr4.addChord(scaleChords1And6[i6]);
        }

        p.addCPhrase(cphr1);
        p.addCPhrase(cphr2);
        p.addCPhrase(cphr3);
        p.addCPhrase(cphr4);

        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Cadence.mid");

        if(i6 == 0) {
            return "perfect";
        } else if(i6 == 1) {
            return "interruptive";
        } else {
            return "imperfect";
        }
    }


    public String makeMIDIHardCadence(){
        makeScaleAndChords();

        int i2 = rn.nextInt(8);
        int i3 = rn.nextInt(8);
        int i4 = rn.nextInt(8);

        cphr1.addChord(scaleChords[i2]);
        cphr2.addChord(scaleChords[i3]);

        randomAnd5And4 = new Note[][]{scaleChords[i4], scaleChord5, scaleChord4};
        int i5 = rn.nextInt(3);
        cphr3.addChord(randomAnd5And4[i5]);
        int i6 = -1;

        if(i5 == 0){
            cphr4.addChord(scaleChord5);
        } else if(i5 == 1){
            i6 = rn.nextInt(2);
            cphr4.addChord(scaleChords1And6[i6]);
        } else {
            cphr4.addChord(scaleChord1);
        }

        p.addCPhrase(cphr1);
        p.addCPhrase(cphr2);
        p.addCPhrase(cphr3);
        p.addCPhrase(cphr4);

        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Cadence.mid");

        if(i5 == 1 && i6 == 0) {
            return "perfect";
        } else if(i5 == 1 && i6 == 1) {
            return "interruptive";
        } else if(i5 == 2) {
            return "plagal";
        } else {
            return "imperfect";
        }
    }
}