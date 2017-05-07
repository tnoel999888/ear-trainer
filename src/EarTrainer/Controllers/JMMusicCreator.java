package EarTrainer.Controllers;

import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import jm.JMC;
import jm.audio.*;

import jm.gui.cpn.JGrandStave;
import jm.music.data.*;
import jm.util.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;

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

    private CPhrase cphr1 = new CPhrase();
    private CPhrase cphr2 = new CPhrase();
    private CPhrase cphr3 = new CPhrase();
    private CPhrase cphr4 = new CPhrase();
    private CPhrase cphr5 = new CPhrase();

    int[] notes = {A4, AS4, B4, C4, CS4, D4, DS4, E4, F4, FS4, G4, GS4, A5, AS5, B5, C5, CS5, D5, DS5, E5, F5, FS5, G5, GS5};
    int[] minorScale = new int[15];
    int[] majorScale = new int[15];
    int[] scaleNotes = new int[15];

    double[] noteLengths = {SIXTEENTH_NOTE, DOTTED_SIXTEENTH_NOTE,
                            EIGHTH_NOTE,DOTTED_EIGHTH_NOTE,
                            QUARTER_NOTE,DOTTED_QUARTER_NOTE,
                            HALF_NOTE};

    private Note[] theirMelodyAnswer = {};

    private Note[] scaleChord1 = {};
    private Note[] scaleChord2 = {};
    private Note[] scaleChord3 = {};
    private Note[] scaleChord4 = {};
    private Note[] scaleChord5 = {};
    private Note[] scaleChord5MelodicMinor = {};
    private Note[] scaleChord6 = {};
    private Note[] scaleChord7 = {};

    private Note[][] scaleChords = {scaleChord1, scaleChord2, scaleChord3, scaleChord4, scaleChord5, scaleChord6, scaleChord7};
    private Note[][] scaleChords1And6 = {scaleChord1, scaleChord6};

    private Note[][] randomAnd5 = {scaleChord1, scaleChord5};
    private Note[][] randomAnd5And4 = {scaleChord1, scaleChord5, scaleChord4};

    private boolean minor = false;
    private boolean major = false;


    public JMMusicCreator(JGrandStave jScore) {
        this.jScore = jScore;
    }


    public Phrase getPhrase() {
        return phr2;
    }


    public Note[] getTheirMelodyAnswer() {
        return theirMelodyAnswer;
    }

    public int[] getScaleNotes() {
        return scaleNotes;
    }


    @FXML
    public void initialize() {
        jScore.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                theirMelodyAnswer = jScore.getPhrase().getNoteArray();
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
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


    private void setScore(Phrase phr) {
        jScore.setPhrase(phr);

        Dimension d = new Dimension();
        d.setSize(600,300);
        jScore.setPreferredSize(d);
        jScore.setMaximumSize(d);

        jScore.removeTitle();
        jScore.setEditable(false);
    }


    private void setScoreEditable(Phrase phr) {
        jScore.setPhrase(phr);

        Dimension d = new Dimension();
        d.setSize(600,300);
        jScore.setPreferredSize(d);
        jScore.setMaximumSize(d);

        jScore.removeTitle();
        jScore.setEditable(true);
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

        setScore(phr1);

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

        setScore(phr1);

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

        setScore(phr1);

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

        Note n = new Note(C4, C);

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






    public int sharpen(int note){
        for(int i = 0; i < notes.length; i++){
            if(notes[i] == note){
                return notes[(i+1) % 12];
            }
        }
        return 0;
    }

    public void makeChords(int[] scale){
        scaleChord1 = new Note[] { new Note(scale[0], C), new Note(scale[2], C), new Note(scale[4], C)};

        //If minor, diminished, invert, 3-5-1
        if(minor) {
            scaleChord2 = new Note[]{new Note(scale[3], C), new Note(scale[5], C), new Note(scale[8], C)};
        } else {
            scaleChord2 = new Note[]{new Note(scale[1], C), new Note(scale[3], C), new Note(scale[5], C)};
        }

        scaleChord3 = new Note[] { new Note(scale[2], C), new Note(scale[4], C), new Note(scale[6], C)};

        scaleChord4 = new Note[] { new Note(scale[3], C), new Note(scale[5], C), new Note(scale[7], C)};

        //Minor 5 chord, make major if doing cadence into tonic, 7#, melodic minor scale
        scaleChord5 = new Note[] { new Note(scale[4], C), new Note(scale[6], C), new Note(scale[8], C)};
        scaleChord5MelodicMinor = new Note[] { new Note(scale[4], C), new Note(sharpen(scale[6]), C), new Note(scale[8], C)};

        scaleChord6 = new Note[] { new Note(scale[5], C), new Note(scale[7], C), new Note(scale[9], C)};

        //For major and minor 1st inversion
        scaleChord7 = new Note[] { new Note(scale[8], C), new Note(scale[10], C), new Note(scale[13], C)};
    }


    public void makeMinorScale(int root){

        for(int i = 0; i < notes.length; i++) {
            if(notes[i] == root) {
                minorScale[0] = notes[i];
                minorScale[1] = notes[(i + 2) % 12];
                minorScale[2] = notes[(i + 3) % 12];
                minorScale[3] = notes[(i + 5) % 12];
                minorScale[4] = notes[(i + 7) % 12];
                minorScale[5] = notes[(i + 8) % 12];
                minorScale[6] = notes[(i + 10) % 12];
                minorScale[7] = notes[(i + 12) % 12];

                minorScale[8] = notes[(i + 14) % 12];
                minorScale[9] = notes[(i + 15) % 12];
                minorScale[10] = notes[(i + 17) % 12];
                minorScale[11] = notes[(i + 19) % 12];
                minorScale[12] = notes[(i + 20) % 12];
                minorScale[13] = notes[(i + 22) % 12];
                minorScale[14] = notes[(i + 24) % 12];
                break;
            }
        }

        makeChords(minorScale);

        scaleChords = new Note[][]{scaleChord1, scaleChord2, scaleChord3, scaleChord4, scaleChord5, scaleChord6, scaleChord7};
        scaleChords1And6 = new Note[][] {scaleChord1, scaleChord6};
    }


    public void makeMajorScale(int root){

        for(int i = 0; i < notes.length; i++) {
            if (notes[i] == root) {
                majorScale[0] = notes[i];
                majorScale[1] = notes[(i + 2) % 12];
                majorScale[2] = notes[(i + 4) % 12];
                majorScale[3] = notes[(i + 5) % 12];
                majorScale[4] = notes[(i + 7) % 12];
                majorScale[5] = notes[(i + 9) % 12];
                majorScale[6] = notes[(i + 11) % 12];
                majorScale[7] = notes[(i + 12) % 12];

                majorScale[8] = notes[(i + 14) % 12];
                majorScale[9] = notes[(i + 16) % 12];
                majorScale[10] = notes[(i + 17) % 12];
                majorScale[11] = notes[(i + 19) % 12];
                majorScale[12] = notes[(i + 21) % 12];
                majorScale[13] = notes[(i + 23) % 12];
                majorScale[14] = notes[(i + 24) % 12];
            }
        }

        makeChords(majorScale);

        scaleChords = new Note[][]{scaleChord1, scaleChord2, scaleChord3, scaleChord4, scaleChord5, scaleChord6, scaleChord7};
        scaleChords1And6 = new Note[][] {scaleChord1, scaleChord6};
    }


    public String makeMIDIEasyCadence(){
        int i = rn.nextInt(12);
        int rootNote = notes[i];
        int minorOrMajor = rn.nextInt(2);

        if(minorOrMajor == 0) {
            minor = true;
            makeMinorScale(rootNote);
        } else {
            major = true;
            makeMajorScale(rootNote);
        }

        //1st Chord. Add I chord
        cphr1.addChord(scaleChord1);

        //2nd Chord. Add II or IV chord
        int IIOrIVChord = rn.nextInt(2);
        if(IIOrIVChord == 0) {
            cphr2.addChord(scaleChord2);
        } else {
            cphr2.addChord(scaleChord4);
        }

        //4th Chord. Add I or VI chord
        int i4 = rn.nextInt(2);
        cphr4.addChord(scaleChords1And6[i4]);

        //3rd Chord. Add V major chord if into tonic, else V minor chord
        if(i4 == 0 && minor) {
            cphr3.addChord(scaleChord5MelodicMinor);
        } else {
            cphr3.addChord(scaleChord5);
        }

        p.addCPhrase(cphr1);
        p.addCPhrase(cphr2);
        p.addCPhrase(cphr3);
        p.addCPhrase(cphr4);

        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Cadence.mid");

        minor = false;
        major = false;

        if(i4 == 0){
            return "perfect";
        } else {
            return "interruptive";
        }
    }


    public String makeMIDIMediumCadence(){
        int i = rn.nextInt(12);
        int rootNote = notes[i];
        int minorOrMajor = rn.nextInt(2);

        if(minorOrMajor == 0) {
            minor = true;
            makeMinorScale(rootNote);
        } else {
            major = true;
            makeMajorScale(rootNote);
        }

        int i3 = rn.nextInt(7);
        int i4 = rn.nextInt(7);

        //1st Chord. Add I chord
        cphr1.addChord(scaleChord1);

        randomAnd5 = new Note[][]{scaleChords[i4], scaleChord5};
        int i5 = rn.nextInt(2);
        int i6 = -1;

        //4th Chord. Add I, V or VI chord
        if(i5 == 0) {
            cphr4.addChord(scaleChord5);
        } else {
            i6 = rn.nextInt(2);
            cphr4.addChord(scaleChords1And6[i6]);
        }

        //3rd Chord. Add V or random
        if(i6 == 0 && minor) {
            cphr3.addChord(scaleChord5MelodicMinor);
        } else if(i6 == 0 && major) {
            cphr3.addChord(scaleChord5);
        } else if(i6 == 1) {
            cphr3.addChord(randomAnd5[1]);
        } else {
            cphr3.addChord(randomAnd5[0]);
        }

        //2nd Chord. Add random chord or II/IV chord if 3rd chord is V
        if(i6 == -1) {
            cphr2.addChord(scaleChords[i3]);
        } else {
            int IIOrIVChord = rn.nextInt(2);
            if(IIOrIVChord == 0) {
                cphr2.addChord(scaleChord2);
            } else {
                cphr2.addChord(scaleChord4);
            }
        }

        p.addCPhrase(cphr1);
        p.addCPhrase(cphr2);
        p.addCPhrase(cphr3);
        p.addCPhrase(cphr4);

        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Cadence.mid");

        minor = false;
        major = false;

        if(i6 == 0) {
            return "perfect";
        } else if(i6 == 1) {
            return "interruptive";
        } else {
            return "imperfect";
        }
    }


    public String makeMIDIHardCadence(){
        int i = rn.nextInt(12);
        int rootNote = notes[i];
        int minorOrMajor = rn.nextInt(2);

        if(minorOrMajor == 0) {
            minor = true;
            makeMinorScale(rootNote);
        } else {
            major = true;
            makeMajorScale(rootNote);
        }

        int i3 = rn.nextInt(7);
        int i4 = rn.nextInt(7);

        //1st Chord. Add I chord
        cphr1.addChord(scaleChord1);

        int i5 = rn.nextInt(3);
        int i6 = -1;

        //4th Chord. Add I, V or VI chord
        if(i5 == 0) {
            cphr4.addChord(scaleChord5);
        } else if(i5 == 1) {
            i6 = rn.nextInt(2);
            cphr4.addChord(scaleChords1And6[i6]);
        } else {
            cphr4.addChord(scaleChord1);
        }

        //3rd Chord. Add IV, V or random
        randomAnd5And4 = new Note[][]{scaleChords[i4], scaleChord5, scaleChord4};
        if(i6 == 0 && minor) {
            cphr3.addChord(scaleChord5MelodicMinor);
        } else if(i6 == 0 && major) {
            cphr3.addChord(scaleChord5);
        } else if(i6 == 1) {
            cphr3.addChord(randomAnd5And4[1]);
        } else if(i5 == 0) {
            cphr3.addChord(randomAnd5And4[0]);
        } else {
            cphr3.addChord(randomAnd5And4[2]);
        }

        //2nd Chord. Add random chord or II/IV chord if 3rd chord is V
        if(i6 == -1) {
            cphr2.addChord(scaleChords[i3]);
        } else {
            int IIOrIVChord = rn.nextInt(2);
            if(IIOrIVChord == 0) {
                cphr2.addChord(scaleChord2);
            } else {
                cphr2.addChord(scaleChord4);
            }
        }

        p.addCPhrase(cphr1);
        p.addCPhrase(cphr2);
        p.addCPhrase(cphr3);
        p.addCPhrase(cphr4);

        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Cadence.mid");

        minor = false;
        major = false;

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







    public int getRelativeMinor(int note){
        int relativeMinor = 0;

        for(int j = 0; j < 12; j++){
            if(notes[j] == note){
                int index = (j-3) % 12;

                if (index < 0)
                {
                    index += 12;
                }

                relativeMinor = notes[index];
                break;
            }
        }

        return relativeMinor;
    }

    public ArrayList findCommonChords(Note[][] origKeyChords, Note[][] newKeyChords){
        ArrayList commonChords = new ArrayList<Note[][]>();

        for(int i = 0; i < origKeyChords.length; i++){
            for(int j = 0; j < newKeyChords.length; j++) {
                if((origKeyChords[i][0].equals(newKeyChords[j][0])) &&
                   (origKeyChords[i][1].equals(newKeyChords[j][1])) &&
                   (origKeyChords[i][2].equals(newKeyChords[j][2]))){
                    commonChords.add(origKeyChords[j]);
                }
            }
        }

        return commonChords;
    }


    public String makeMIDIEasyModulation(){
        int[] circleOfFifths = {C4, G4, D4, A4, E4, B4, FS4, DF4, AF4, EF4, BF4, F4};
        int[] similarKeys = new int[5];

        int i = rn.nextInt(12);
        int root = circleOfFifths[i];
        makeMajorScale(root);

        Note[][] rootKeyChords = scaleChords;

        int index = (i-1) % 12;

        if (index < 0)
        {
            index += 12;
        }

        similarKeys[0] = getRelativeMinor(root);
        similarKeys[1] = circleOfFifths[index];
        similarKeys[2] = getRelativeMinor(circleOfFifths[index]);
        similarKeys[3] = circleOfFifths[(i + 1) % 12];
        similarKeys[4] = getRelativeMinor(circleOfFifths[(i + 1) % 12]);

        //Add tonic of original key
        cphr1.addChord(scaleChords[0]);

        //Add random chord of original key
        int i2 = rn.nextInt(7);
        cphr2.addChord(scaleChords[i2]);

        //Find key to modulate to from similar keys
        int i3 = rn.nextInt(5);
        int newKey = similarKeys[i3];

        if(i3 == 0 || i3 == 2 || i3 == 4){
            makeMinorScale(newKey);
        } else {
            makeMajorScale(newKey);
        }

        Note[][] newKeyChords = scaleChords;

        ArrayList commonChords = findCommonChords(rootKeyChords,newKeyChords);

        if(commonChords.contains(rootKeyChords[1])){
            System.out.println("2");
            cphr3.addChord(scaleChord2);
        } else if(commonChords.contains(rootKeyChords[3])){
            System.out.println("4");
            cphr3.addChord(scaleChord4);
        } else {
            System.out.println("Random");
            int i4 = rn.nextInt(commonChords.size());
            cphr3.addChord((Note[])commonChords.get(i4));
        }

        cphr4.addChord(scaleChord5);
        cphr5.addChord(scaleChord1);

        p.addCPhrase(cphr1);
        p.addCPhrase(cphr2);
        p.addCPhrase(cphr3);
        p.addCPhrase(cphr4);
        p.addCPhrase(cphr5);

        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Modulation.mid");

        return "";
    }

    public String makeMIDIMediumModulation(){
        return "";
    }

    public String makeMIDIHardModulation(){
        return "";
    }







    public int makeMIDIEasyWrongNote(){
        int i = rn.nextInt(12);
        int rootNote = notes[i];

        int minorOrMajor = rn.nextInt(2);

        if(minorOrMajor == 0) {
            minor = true;
            makeMinorScale(rootNote);
        } else {
            major = true;
            makeMajorScale(rootNote);
        }


        List melody = new LinkedList<Note>();

        if(minor) {
            scaleNotes = minorScale;
        } else {
            scaleNotes = majorScale;
        }


        double lengthSoFar = 0.0;

        while(lengthSoFar != 4.0){
            int pitch = scaleNotes[rn.nextInt(15)];
            double duration = noteLengths[rn.nextInt(7)];

            if(duration + lengthSoFar <= 4.0){
                melody.add(new Note(pitch, duration));
                lengthSoFar += duration;
            }
        }

        Note[] melodyArray = new Note[melody.size()];
        melody.toArray(melodyArray);


        //Find notes which do not belong to this scale
        List nonScaleNotes = new ArrayList();

        for(int n : notes){
            if(!Arrays.asList(scaleNotes).contains(n)){
                nonScaleNotes.add(n);
            }
        }


        //Find random note in melody to change
        int i2 = rn.nextInt(melody.size());
        int i3 = rn.nextInt(nonScaleNotes.size());

        double duration = melodyArray[i2].getDuration();
        int pitch = (int)nonScaleNotes.get(i3);
        melodyArray[i2] = new Note(pitch, duration);

        System.out.println(i2);


        //Add melody array to phrase
        phr2.addNoteList(melodyArray);

        setScoreEditable(phr2);

        p.add(phr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/WrongNote.mid");

        return i2;
    }

    public String makeMIDIMediumWrongNote(){
        return "";
    }

    public String makeMIDIHardWrongNote(){
        return "";
    }


}