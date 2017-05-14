package EarTrainer.Controllers;

import jm.JMC;

import jm.gui.cpn.JGrandStave;
import jm.music.data.*;
import jm.util.*;
import org.apache.commons.lang3.ArrayUtils;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;


public final class JMMusicCreator implements JMC {
    private Random rn = new Random();

    private Score s = new Score();
    private Part p = new Part(0);

    private JGrandStave jScore = new JGrandStave();
    private JGrandStave jScoreLeft = new JGrandStave();
    private JGrandStave jScoreRight = new JGrandStave();

    private Phrase phr1 = new Phrase(0.0);
    private Phrase phr2 = new Phrase(0.0);
    private Phrase originalPhr2 = new Phrase( 0.0);
    private Phrase bottomNotes = new Phrase();
    private Phrase middleNotes = new Phrase();
    private Phrase topNotes = new Phrase();

    private CPhrase cphr1 = new CPhrase();
    private CPhrase cphr2 = new CPhrase();
    private CPhrase cphr3 = new CPhrase();
    private CPhrase cphr4 = new CPhrase();
    private CPhrase cphr5 = new CPhrase();
    private CPhrase cphr6 = new CPhrase();
    private CPhrase cphr7 = new CPhrase();
    private CPhrase cphr8 = new CPhrase();

    private int[] notes = {A3, AS3, B3, C4, CS4, D4, DS4, E4, F4, FS4, G4, GS4, A4, AS4, B4, C5, CS5, D5, DS5, E5, F5, FS5, G5, GS5, A5, AS5, B5, C6, CS6, D6, DS6, E6, F6, FS6};
    private int SIZE_OF_NOTES_ARRAY = notes.length;
    private int[] notesOneOctave = {A4, AS4, B4, C4, CS4, D4, DS4, E4, F4, FS4, G4, GS4};
    private int[] circleOfFifthsMajor = {C4, G4, D4, A4, E4, B4, FS4, DF4, AF4, EF4, BF4, F4};
    private int[] circleOfFifthsMinor = {A4, E4, B4, FS4, CS4, GS4, DS4, BF4, F4, C4, G4, D4};
    private int[] similarKeys = new int[5];

    private int[] minorScale = new int[15];
    private int[] majorScale = new int[15];
    private int[] scaleNotes = new int[15];

    private List noteLengthsList = new LinkedList(Arrays.asList(SIXTEENTH_NOTE, DOTTED_SIXTEENTH_NOTE,
                                                                EIGHTH_NOTE, DOTTED_EIGHTH_NOTE,
                                                                QUARTER_NOTE, DOTTED_QUARTER_NOTE,
                                                                HALF_NOTE));

    private Note[] theirMelodyAnswer;
    private Note[] scaleChord1 = new Note[3];
    private Note[] scaleChord2 = new Note[3];
    private Note[] scaleChord3 = new Note[3];
    private Note[] scaleChord4 = new Note[3];
    private Note[] scaleChord5 = new Note[3];
    private Note[] scaleChord5MelodicMinor = new Note[3];
    private Note[] scaleChord6 = new Note[3];
    private Note[] scaleChord7 = new Note[3];
    private Note[] bottomNotesArray = new Note[4];
    private Note[] middleNotesArray = new Note[4];
    private Note[] topNotesArray = new Note[4];

    private Note[][] scaleChords = {scaleChord1, scaleChord2, scaleChord3, scaleChord4, scaleChord5, scaleChord6, scaleChord7};
    private Note[][] scaleChords1And6 = {scaleChord1, scaleChord6};
    private Note[][] randomAnd5 = {scaleChord1, scaleChord5};
    private Note[][] randomAnd5And4 = {scaleChord1, scaleChord5, scaleChord4};

    private boolean minor = false;
    private boolean major = false;

    private String rootKeyString;



    public JMMusicCreator(JGrandStave score) {
        jScore = score;

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


    public JMMusicCreator(JGrandStave score, JGrandStave scoreLeft, JGrandStave scoreRight) {
        jScore = score;
        jScoreLeft = scoreLeft;
        jScoreRight = scoreRight;

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


    public Phrase getPhrase() {
        return phr2;
    }


    public Phrase getOriginalPhrase() {
        return originalPhr2;
    }


    public Note[] getTheirMelodyAnswer() {
        return theirMelodyAnswer;
    }


    public int[] getScaleNotes() {
        return scaleNotes;
    }


    public int[] getSimilarKeys() {
        return similarKeys;
    }


    private String getInterval(int i) {
        switch (i) {
            case 0:
                return "unison";

            case -1:
            case 1:
                return "minor second";

            case -2:
            case 2:
                return "major second";

            case -3:
            case 3:
                return "minor third";

            case -4:
            case 4:
                return "major third";

            case -5:
            case 5:
                return "perfect fourth";

            case -6:
            case 6:
                return "tritone";

            case -7:
            case 7:
                return "perfect fifth";

            case -8:
            case 8:
                return "minor sixth";

            case -9:
            case 9:
                return "major sixth";

            case -10:
            case 10:
                return "minor seventh";

            case -11:
            case 11:
                return "major seventh";

            case -12:
            case 12:
                return "octave";

            default:
                return "";
        }
    }


    public String getNote(Note n2) {
        switch (n2.getPitch()) {
            case C3:
            case C5:
            case C4:
                return "C";

            case CS3:
            case CS5:
            case CS4:
                return "C#";

            case D3:
            case D5:
            case D4:
                return "D";

            case DS3:
            case DS5:
            case DS4:
                return "D#";

            case E3:
            case E5:
            case E4:
                return "E";

            case F3:
            case F5:
            case F4:
                return "F";

            case FS3:
            case FS5:
            case FS4:
                return "F#";

            case G3:
            case G5:
            case G4:
                return "G";

            case GS3:
            case GS5:
            case GS4:
                return "G#";

            case A3:
            case A5:
            case A4:
                return "A";

            case AS3:
            case AS5:
            case AS4:
                return "A#";

            case B3:
            case B5:
            case B4:
                return "B";

            default:
                return "";
        }
    }


    public String getRootKeyString(){
        if(minor){
            rootKeyString += "m";
        }

        return rootKeyString;
    }


    public String getMinorOrMajor(){
        if(minor){
            return "Minor";
        } else {
            return "Major";
        }
    }


    private void setScore(Phrase phr) {
        jScore.setPhrase(phr);

        Dimension d = new Dimension();
        d.setSize(600, 300);
        jScore.setPreferredSize(d);
        jScore.setMaximumSize(d);

        jScore.removeTitle();
        jScore.setEditable(false);
    }


    public void setScoreSpecific(Phrase phr, String score) {
        JGrandStave scoreToUse;

        if(score.equals("left")){
            scoreToUse = jScoreLeft;
        } else if(score.equals("middle")){
            scoreToUse = jScore;
        } else {
            scoreToUse = jScoreRight;
        }

        scoreToUse.setPhrase(phr);

        Dimension d = new Dimension();
        d.setSize(600,300);
        scoreToUse.setPreferredSize(d);
        scoreToUse.setMaximumSize(d);

        scoreToUse.removeTitle();
        scoreToUse.setEditable(false);
    }


    private void setScoreEditable(Phrase phr) {
        jScore.setPhrase(phr);

        Dimension d = new Dimension();
        d.setSize(600, 300);
        jScore.setPreferredSize(d);
        jScore.setMaximumSize(d);

        jScore.removeTitle();
        jScore.setEditable(true);
    }




//****************************************************************************
//   __  __      _           _ _        _____       _                       _
//  |  \/  |    | |         | (_)      |_   _|     | |                     | |
//  | \  / | ___| | ___   __| |_  ___    | |  _ __ | |_ ___ _ ____   ____ _| |
//  | |\/| |/ _ \ |/ _ \ / _` | |/ __|   | | | '_ \| __/ _ \ '__\ \ / / _` | |
//  | |  | |  __/ | (_) | (_| | | (__   _| |_| | | | ||  __/ |   \ V / (_| | |
//  |_|  |_|\___|_|\___/ \__,_|_|\___| |_____|_| |_|\__\___|_|    \_/ \__,_|_|
// ****************************************************************************

    public String makeMIDIEasyMelodic() {
        Note n1 = new Note(C4, C);
        phr1.addNote(n1);

        setScore(phr1);

        Random rn = new Random();
        int i = rn.nextInt(5);
        int[] array = {0, 3, 4, 7, 12};
        int interval = array[i];

        System.out.println(i);

        Note n2 = new Note(C4 + interval, C);

        phr2.addNote(n1);
        phr2.addNote(n2);

        p.addPhrase(phr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/MelodicInterval.mid");

        return getInterval(interval);
    }


    public String makeMIDIMediumMelodic() {
        Random rn = new Random();
        int i = rn.nextInt(7);
        int[] array = {0, 1, 2, 3, 4, 7, 12};
        int interval = array[i];

        System.out.println(interval);

        int i2 = rn.nextInt(7);
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

        Note n2 = new Note(chosenRoot + interval, C);

        phr2.addNote(n1);
        phr2.addNote(n2);

        p.addPhrase(phr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/MelodicInterval.mid");

        return getInterval(interval);
    }


    public String makeMIDIHardMelodic() {
        Random rn = new Random();
        int i = rn.nextInt(25) - 12;

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

        Note n2 = new Note(chosenRoot + i, C);

        phr2.addNote(n1);
        phr2.addNote(n2);

        p.addPhrase(phr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/MelodicInterval.mid");

        return getInterval(i);
    }




//******************************************************************************************
//   _    _                                  _        _____       _                       _
//  | |  | |                                (_)      |_   _|     | |                     | |
//  | |__| | __ _ _ __ _ __ ___   ___  _ __  _  ___    | |  _ __ | |_ ___ _ ____   ____ _| |
//  |  __  |/ _` | '__| '_ ` _ \ / _ \| '_ \| |/ __|   | | | '_ \| __/ _ \ '__\ \ / / _` | |
//  | |  | | (_| | |  | | | | | | (_) | | | | | (__   _| |_| | | | ||  __/ |   \ V / (_| | |
//  |_|  |_|\__,_|_|  |_| |_| |_|\___/|_| |_|_|\___| |_____|_| |_|\__\___|_|    \_/ \__,_|_|
//******************************************************************************************

    public String makeMIDIEasyHarmonic() {
        Note n1 = new Note(C4, C);
        phr1.addNote(n1);

        setScore(phr1);

        Random rn = new Random();
        int i = rn.nextInt(5);
        int[] array = {0, 3, 4, 7, 12};
        int interval = array[i];

        System.out.println(i);

        Note n2 = new Note(C4 + interval, C);

        Note[] notes = {n1, n2};
        cphr1.addChord(notes);

        p.addCPhrase(cphr1);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/HarmonicInterval.mid");

        return getInterval(interval);
    }


    public String makeMIDIMediumHarmonic() {
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

        Note n2 = new Note(chosenRoot + interval, C);

        Note[] notes = {n1, n2};
        cphr1.addChord(notes);

        p.addCPhrase(cphr1);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/HarmonicInterval.mid");

        return getInterval(interval);
    }


    public String makeMIDIHardHarmonic() {
        Random rn = new Random();
        int i = rn.nextInt(25) - 12;

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

        Note n2 = new Note(chosenRoot + i, C);

        Note[] notes = {n1, n2};
        cphr1.addChord(notes);

        p.addCPhrase(cphr1);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/HarmonicInterval.mid");

        return getInterval(i);
    }




//************************************
//   _____    _   _            _
//  |  __ \  (_) | |          | |
//  | |__) |  _  | |_    ___  | |__
//  |  ___/  | | | __|  / __| | '_ \
//  | |      | | | |_  | (__  | | | |
//  |_|      |_|  \__|  \___| |_| |_|
//************************************

    public String makeMIDIEasyPitch() {
        Random rn = new Random();
        int i = rn.nextInt(7);
        int[] array = {0, 2, 4, 5, 7, 9, 11};
        int interval = array[i];

        setScore(phr1);

        Note n = new Note(C4 + interval, C);

        phr2.addNote(n);

        p.addPhrase(phr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Pitch.mid");

        return getNote(n);
    }


    public String makeMIDIMediumPitch() {
        Random rn = new Random();
        int i = rn.nextInt(12);
        int[] array = new int[12];

        for (int j = 0; j < 12; j++) {
            array[j] = j;
        }

        int interval = array[i];

        setScore(phr1);

        Note n = new Note(C4 + interval, C);

        phr2.addNote(n);

        p.addPhrase(phr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Pitch.mid");

        return getNote(n);
    }


    public String makeMIDIHardPitch() {
        Random rn = new Random();
        int i = rn.nextInt(36);
        int[] array = new int[36];

        for (int j = 0; j < 36; j++) {
            array[j] = j;
        }

        int interval = array[i];

        setScore(phr1);

        Note n = new Note(C3 + interval, C);

        phr2.addNote(n);

        p.addPhrase(phr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Pitch.mid");

        return getNote(n);
    }




//********************************************************
//   _____ _                         ________ _       _
//  / ____| |                       / /  ____| |     | |
// | (___ | |__   __ _ _ __ _ __   / /| |__  | | __ _| |_
//  \___ \| '_ \ / _` | '__| '_ \ / / |  __| | |/ _` | __|
//  ____) | | | | (_| | |  | |_) / /  | |    | | (_| | |_
// |_____/|_| |_|\__,_|_|  | .__/_/   |_|    |_|\__,_|\__|
//                         | |
//                         |_|
//********************************************************

    public String makeMIDIEasySharpFlat() {
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


    public String makeMIDIMediumSharpFlat() {
        Random rn = new Random();
        int i = rn.nextInt(12);
        int[] array = new int[12];

        for (int j = 0; j < 12; j++) {
            array[j] = j;
        }

        int interval = array[i];

        setScore(phr2);

        Note n = new Note(C4 + interval, C);

        phr2.addNote(n);

        p.addPhrase(phr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/SharpFlat.mid");

        return getNote(n);
    }


    public String makeMIDIHardSharpFlat() {
        Random rn = new Random();
        int i = rn.nextInt(36);
        int[] array = new int[36];

        for (int j = 0; j < 36; j++) {
            array[j] = j;
        }

        int interval = array[i];

        setScore(phr2);

        Note n = new Note(C3 + interval, C);

        phr2.addNote(n);

        p.addPhrase(phr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/SharpFlat.mid");

        return getNote(n);
    }




//*****************************************
//   _____          _
//  / ____|        | |
// | |     __ _  __| | ___ _ __   ___ ___
// | |    / _` |/ _` |/ _ \ '_ \ / __/ _ \
// | |___| (_| | (_| |  __/ | | | (_|  __/
//  \_____\__,_|\__,_|\___|_| |_|\___\___|
//*****************************************

    public int sharpen(int note) {
        for (int i = 0; i < notes.length; i++) {
            if (notes[i] == note) {
                return notes[(i + 1) % SIZE_OF_NOTES_ARRAY];
            }
        }
        return 0;
    }


    public void makeChords(int[] scale) {
        scaleChord1 = new Note[]{new Note(scale[0], C), new Note(scale[2], C), new Note(scale[4], C)};

        //If minor, diminished, invert, 3-5-8
        if (minor) {
            scaleChord2 = new Note[]{new Note(scale[3], C), new Note(scale[5], C), new Note(scale[8], C)};
        } else {
            scaleChord2 = new Note[]{new Note(scale[1], C), new Note(scale[3], C), new Note(scale[5], C)};
        }

        scaleChord3 = new Note[]{new Note(scale[2], C), new Note(scale[4], C), new Note(scale[6], C)};

        scaleChord4 = new Note[]{new Note(scale[3], C), new Note(scale[5], C), new Note(scale[7], C)};

        //Minor 5 chord, make major if doing cadence into tonic, 7#, melodic minor scale
        scaleChord5 = new Note[]{new Note(scale[4], C), new Note(scale[6], C), new Note(scale[8], C)};
        scaleChord5MelodicMinor = new Note[]{new Note(scale[4], C), new Note(sharpen(scale[6]), C), new Note(scale[8], C)};

        scaleChord6 = new Note[]{new Note(scale[5], C), new Note(scale[7], C), new Note(scale[9], C)};

        //For major and minor 1st inversion
        scaleChord7 = new Note[]{new Note(scale[8], C), new Note(scale[10], C), new Note(scale[13], C)};
    }


    public void makeMinorScale(int root) {

//        for (int i = 0; i < notes.length; i++) {
//            if (notes[i] == root) {
//                minorScale[0] = notes[i];
//                minorScale[1] = notes[(i + 2) % 12];
//                minorScale[2] = notes[(i + 3) % 12];
//                minorScale[3] = notes[(i + 5) % 12];
//                minorScale[4] = notes[(i + 7) % 12];
//                minorScale[5] = notes[(i + 8) % 12];
//                minorScale[6] = notes[(i + 10) % 12];
//                minorScale[7] = notes[(i + 12) % 12];
//
//                minorScale[8] = notes[(i + 14) % 12];
//                minorScale[9] = notes[(i + 15) % 12];
//                minorScale[10] = notes[(i + 17) % 12];
//                minorScale[11] = notes[(i + 19) % 12];
//                minorScale[12] = notes[(i + 20) % 12];
//                minorScale[13] = notes[(i + 22) % 12];
//                minorScale[14] = notes[(i + 24) % 12];
//                break;
//            }
//        }

        for (int i = 0; i < notes.length; i++) {
            if (notes[i] == root) {
                minorScale[0] = notes[i];
                minorScale[1] = notes[(i + 2) % SIZE_OF_NOTES_ARRAY];
                minorScale[2] = notes[(i + 3) % SIZE_OF_NOTES_ARRAY];
                minorScale[3] = notes[(i + 5) % SIZE_OF_NOTES_ARRAY];
                minorScale[4] = notes[(i + 7) % SIZE_OF_NOTES_ARRAY];
                minorScale[5] = notes[(i + 8) % SIZE_OF_NOTES_ARRAY];
                minorScale[6] = notes[(i + 10) % SIZE_OF_NOTES_ARRAY];
                minorScale[7] = notes[(i + 12) % SIZE_OF_NOTES_ARRAY];

                minorScale[8] = notes[(i + 14) % SIZE_OF_NOTES_ARRAY];
                minorScale[9] = notes[(i + 15) % SIZE_OF_NOTES_ARRAY];
                minorScale[10] = notes[(i + 17) % SIZE_OF_NOTES_ARRAY];
                minorScale[11] = notes[(i + 19) % SIZE_OF_NOTES_ARRAY];
                minorScale[12] = notes[(i + 20) % SIZE_OF_NOTES_ARRAY];
                minorScale[13] = notes[(i + 22) % SIZE_OF_NOTES_ARRAY];
                minorScale[14] = notes[(i + 24) % SIZE_OF_NOTES_ARRAY];
                break;
            }
        }

        System.out.println("");

        for(int note: minorScale){
            System.out.println(note);
        }

        System.out.println("");

        makeChords(minorScale);

        scaleChords = new Note[][]{scaleChord1, scaleChord2, scaleChord3, scaleChord4, scaleChord5, scaleChord6, scaleChord7};
        scaleChords1And6 = new Note[][]{scaleChord1, scaleChord6};
    }


    public void makeMajorScale(int root) {

//        for (int i = 0; i < notes.length; i++) {
//            if (notes[i] == root) {
//                majorScale[0] = notes[i];
//                majorScale[1] = notes[(i + 2) % 12];
//                majorScale[2] = notes[(i + 4) % 12];
//                majorScale[3] = notes[(i + 5) % 12];
//                majorScale[4] = notes[(i + 7) % 12];
//                majorScale[5] = notes[(i + 9) % 12];
//                majorScale[6] = notes[(i + 11) % 12];
//                majorScale[7] = notes[(i + 12) % 12];
//
//                majorScale[8] = notes[(i + 14) % 12];
//                majorScale[9] = notes[(i + 16) % 12];
//                majorScale[10] = notes[(i + 17) % 12];
//                majorScale[11] = notes[(i + 19) % 12];
//                majorScale[12] = notes[(i + 21) % 12];
//                majorScale[13] = notes[(i + 23) % 12];
//                majorScale[14] = notes[(i + 24) % 12];
//            }
//        }

        for (int i = 0; i < notes.length; i++) {
            if (notes[i] == root) {
                majorScale[0] = notes[i];
                majorScale[1] = notes[(i + 2) % SIZE_OF_NOTES_ARRAY];
                majorScale[2] = notes[(i + 4) % SIZE_OF_NOTES_ARRAY];
                majorScale[3] = notes[(i + 5) % SIZE_OF_NOTES_ARRAY];
                majorScale[4] = notes[(i + 7) % SIZE_OF_NOTES_ARRAY];
                majorScale[5] = notes[(i + 9) % SIZE_OF_NOTES_ARRAY];
                majorScale[6] = notes[(i + 11) % SIZE_OF_NOTES_ARRAY];
                majorScale[7] = notes[(i + 12) % SIZE_OF_NOTES_ARRAY];

                majorScale[8] = notes[(i + 14) % SIZE_OF_NOTES_ARRAY];
                majorScale[9] = notes[(i + 16) % SIZE_OF_NOTES_ARRAY];
                majorScale[10] = notes[(i + 17) % SIZE_OF_NOTES_ARRAY];
                majorScale[11] = notes[(i + 19) % SIZE_OF_NOTES_ARRAY];
                majorScale[12] = notes[(i + 21) % SIZE_OF_NOTES_ARRAY];
                majorScale[13] = notes[(i + 23) % SIZE_OF_NOTES_ARRAY];
                majorScale[14] = notes[(i + 24) % SIZE_OF_NOTES_ARRAY];
            }
        }

        System.out.println("");

        for(int note: majorScale){
            System.out.println(note);
        }

        System.out.println("");

        makeChords(majorScale);

        scaleChords = new Note[][]{scaleChord1, scaleChord2, scaleChord3, scaleChord4, scaleChord5, scaleChord6, scaleChord7};
        scaleChords1And6 = new Note[][]{scaleChord1, scaleChord6};
    }


    public String makeMIDIEasyCadence() {
        int i = rn.nextInt(12);
        int rootNote = notes[i];
        int minorOrMajor = rn.nextInt(2);

        if (minorOrMajor == 0) {
            minor = true;
            makeMinorScale(rootNote);
            System.out.println("minor");
        } else {
            major = true;
            makeMajorScale(rootNote);
            System.out.println("major");
        }


        //1st Chord. Add I chord
        cphr1.addChord(scaleChord1);
        bottomNotesArray[0] = scaleChord1[0];
        middleNotesArray[0] = scaleChord1[1];
        topNotesArray[0] = scaleChord1[2];
        System.out.println("1");


        //2nd Chord. Add II or IV chord
        int IIOrIVChord = rn.nextInt(2);
        if (IIOrIVChord == 0) {
            cphr2.addChord(scaleChord2);
            bottomNotesArray[1] = scaleChord2[0];
            middleNotesArray[1] = scaleChord2[1];
            topNotesArray[1] = scaleChord2[2];
            System.out.println("2");
        } else {
            cphr2.addChord(scaleChord4);
            bottomNotesArray[1] = scaleChord4[0];
            middleNotesArray[1] = scaleChord4[1];
            topNotesArray[1] = scaleChord4[2];
            System.out.println("4");
        }


        //4th Chord. Add I or VI chord
        int i4 = rn.nextInt(2);
        cphr4.addChord(scaleChords1And6[i4]);
        bottomNotesArray[3] = scaleChords1And6[i4][0];
        middleNotesArray[3] = scaleChords1And6[i4][1];
        topNotesArray[3] = scaleChords1And6[i4][2];



        //3rd Chord. Add V major chord if into tonic, else V minor chord
        if (i4 == 0 && minor) {
            cphr3.addChord(scaleChord5MelodicMinor);
            bottomNotesArray[2] = scaleChord5MelodicMinor[0];
            middleNotesArray[2] = scaleChord5MelodicMinor[1];
            topNotesArray[2] = scaleChord5MelodicMinor[2];
            System.out.println("5 major");
        } else {
            cphr3.addChord(scaleChord5);
            bottomNotesArray[2] = scaleChord5[0];
            middleNotesArray[2] = scaleChord5[1];
            topNotesArray[2] = scaleChord5[2];
            System.out.println("5");
        }


        if(i4 == 0) {
            System.out.println("1");
        } else {
            System.out.println("6");
        }


        //Add note arrays to the phrases
        bottomNotes.addNoteList(bottomNotesArray);
        middleNotes.addNoteList(middleNotesArray);
        topNotes.addNoteList(topNotesArray);


        //Set the scores
        setScoreSpecific(bottomNotes, "left");
        setScoreSpecific(middleNotes, "middle");
        setScoreSpecific(topNotes, "right");


        p.addCPhrase(cphr1);
        p.addCPhrase(cphr2);
        p.addCPhrase(cphr3);
        p.addCPhrase(cphr4);

        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Cadence.mid");

        minor = false;
        major = false;

        if (i4 == 0) {
            return "perfect";
        } else {
            return "interruptive";
        }
    }


    public String makeMIDIMediumCadence() {
        int i = rn.nextInt(12);
        int rootNote = notes[i];
        int minorOrMajor = rn.nextInt(2);

        if (minorOrMajor == 0) {
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
        bottomNotesArray[0] = scaleChord1[0];
        middleNotesArray[0] = scaleChord1[1];
        topNotesArray[0] = scaleChord1[2];


        randomAnd5 = new Note[][]{scaleChords[i4], scaleChord5};
        int i5 = rn.nextInt(2);
        int i6 = -1;


        //4th Chord. Add I, V or VI chord
        if (i5 == 0) {
            cphr4.addChord(scaleChord5);
            bottomNotesArray[3] = scaleChord5[0];
            middleNotesArray[3] = scaleChord5[1];
            topNotesArray[3] = scaleChord5[2];
        } else {
            i6 = rn.nextInt(2);
            cphr4.addChord(scaleChords1And6[i6]);
            bottomNotesArray[3] = scaleChords1And6[i6][0];
            middleNotesArray[3] = scaleChords1And6[i6][1];
            topNotesArray[3] = scaleChords1And6[i6][2];
        }


        //3rd Chord. Add V or random
        if (i6 == 0 && minor) {
            cphr3.addChord(scaleChord5MelodicMinor);
            bottomNotesArray[2] = scaleChord5MelodicMinor[0];
            middleNotesArray[2] = scaleChord5MelodicMinor[1];
            topNotesArray[2] = scaleChord5MelodicMinor[2];
        } else if (i6 == 0 && major) {
            cphr3.addChord(scaleChord5);
            bottomNotesArray[2] = scaleChord5[0];
            middleNotesArray[2] = scaleChord5[1];
            topNotesArray[2] = scaleChord5[2];
        } else if (i6 == 1) {
            cphr3.addChord(randomAnd5[1]);
            bottomNotesArray[2] = randomAnd5[1][0];
            middleNotesArray[2] = randomAnd5[1][1];
            topNotesArray[2] = randomAnd5[1][2];
        } else {
            cphr3.addChord(randomAnd5[0]);
            bottomNotesArray[2] = randomAnd5[0][0];
            middleNotesArray[2] = randomAnd5[0][1];
            topNotesArray[2] = randomAnd5[0][2];
        }


        //2nd Chord. Add random chord or II/IV chord if 3rd chord is V
        if (i6 == -1) {
            cphr2.addChord(scaleChords[i3]);
            bottomNotesArray[1] = scaleChords[i3][0];
            middleNotesArray[1] = scaleChords[i3][1];
            topNotesArray[1] = scaleChords[i3][2];
        } else {
            int IIOrIVChord = rn.nextInt(2);
            if (IIOrIVChord == 0) {
                cphr2.addChord(scaleChord2);
                bottomNotesArray[1] = scaleChord2[0];
                middleNotesArray[1] = scaleChord2[1];
                topNotesArray[1] = scaleChord2[2];
            } else {
                cphr2.addChord(scaleChord4);
                bottomNotesArray[1] = scaleChord2[0];
                middleNotesArray[1] = scaleChord2[1];
                topNotesArray[1] = scaleChord2[2];
            }
        }


        //Add note arrays to the phrases
        bottomNotes.addNoteList(bottomNotesArray);
        middleNotes.addNoteList(middleNotesArray);
        topNotes.addNoteList(topNotesArray);


        //Set the scores
        setScoreSpecific(bottomNotes, "left");
        setScoreSpecific(middleNotes, "middle");
        setScoreSpecific(topNotes, "right");


        p.addCPhrase(cphr1);
        p.addCPhrase(cphr2);
        p.addCPhrase(cphr3);
        p.addCPhrase(cphr4);

        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Cadence.mid");

        minor = false;
        major = false;

        if (i6 == 0) {
            return "perfect";
        } else if (i6 == 1) {
            return "interruptive";
        } else {
            return "imperfect";
        }
    }


    public String makeMIDIHardCadence() {
        int i = rn.nextInt(12);
        int rootNote = notes[i];
        int minorOrMajor = rn.nextInt(2);

        if (minorOrMajor == 0) {
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
        bottomNotesArray[0] = scaleChord1[0];
        middleNotesArray[0] = scaleChord1[1];
        topNotesArray[0] = scaleChord1[2];

        int i5 = rn.nextInt(3);
        int i6 = -1;


        //4th Chord. Add I, V or VI chord
        if (i5 == 0) {
            cphr4.addChord(scaleChord5);
            bottomNotesArray[3] = scaleChord5[0];
            middleNotesArray[3] = scaleChord5[1];
            topNotesArray[3] = scaleChord5[2];
        } else if (i5 == 1) {
            i6 = rn.nextInt(2);
            cphr4.addChord(scaleChords1And6[i6]);
            bottomNotesArray[3] = scaleChords1And6[i6][0];
            middleNotesArray[3] = scaleChords1And6[i6][1];
            topNotesArray[3] = scaleChords1And6[i6][2];
        } else {
            cphr4.addChord(scaleChord1);
            bottomNotesArray[3] = scaleChord1[0];
            middleNotesArray[3] = scaleChord1[1];
            topNotesArray[3] = scaleChord1[2];
        }


        //3rd Chord. Add IV, V or random
        randomAnd5And4 = new Note[][]{scaleChords[i4], scaleChord5, scaleChord4};
        if (i6 == 0 && minor) {
            cphr3.addChord(scaleChord5MelodicMinor);
            bottomNotesArray[2] = scaleChord5MelodicMinor[0];
            middleNotesArray[2] = scaleChord5MelodicMinor[1];
            topNotesArray[2] = scaleChord5MelodicMinor[2];
        } else if (i6 == 0 && major) {
            cphr3.addChord(scaleChord5);
            bottomNotesArray[2] = scaleChord5[0];
            middleNotesArray[2] = scaleChord5[1];
            topNotesArray[2] = scaleChord5[2];
        } else if (i6 == 1) {
            cphr3.addChord(randomAnd5And4[1]);
            bottomNotesArray[2] = randomAnd5And4[1][0];
            middleNotesArray[2] = randomAnd5And4[1][1];
            topNotesArray[2] = randomAnd5And4[1][2];
        } else if (i5 == 0) {
            cphr3.addChord(randomAnd5And4[0]);
            bottomNotesArray[2] = randomAnd5And4[0][0];
            middleNotesArray[2] = randomAnd5And4[0][1];
            topNotesArray[2] = randomAnd5And4[0][2];
        } else {
            cphr3.addChord(randomAnd5And4[2]);
            bottomNotesArray[2] = randomAnd5And4[2][0];
            middleNotesArray[2] = randomAnd5And4[2][1];
            topNotesArray[2] = randomAnd5And4[2][2];
        }


        //2nd Chord. Add random chord or II/IV chord if 3rd chord is V
        if (i6 == -1) {
            cphr2.addChord(scaleChords[i3]);
            bottomNotesArray[1] = scaleChords[i3][0];
            middleNotesArray[1] = scaleChords[i3][1];
            topNotesArray[1] = scaleChords[i3][2];
        } else {
            int IIOrIVChord = rn.nextInt(2);
            if (IIOrIVChord == 0) {
                cphr2.addChord(scaleChord2);
                bottomNotesArray[1] = scaleChord2[0];
                middleNotesArray[1] = scaleChord2[1];
                topNotesArray[1] = scaleChord2[2];
            } else {
                cphr2.addChord(scaleChord4);
                bottomNotesArray[1] = scaleChord4[0];
                middleNotesArray[1] = scaleChord4[1];
                topNotesArray[1] = scaleChord4[2];
            }
        }


        //Add note arrays to the phrases
        bottomNotes.addNoteList(bottomNotesArray);
        middleNotes.addNoteList(middleNotesArray);
        topNotes.addNoteList(topNotesArray);


        //Set the scores
        setScoreSpecific(bottomNotes, "left");
        setScoreSpecific(middleNotes, "middle");
        setScoreSpecific(topNotes, "right");


        p.addCPhrase(cphr1);
        p.addCPhrase(cphr2);
        p.addCPhrase(cphr3);
        p.addCPhrase(cphr4);

        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Cadence.mid");

        minor = false;
        major = false;

        if (i5 == 1 && i6 == 0) {
            return "perfect";
        } else if (i5 == 1 && i6 == 1) {
            return "interruptive";
        } else if (i5 == 2) {
            return "plagal";
        } else {
            return "imperfect";
        }
    }




//*****************************************************
//  __  __           _       _       _   _
// |  \/  |         | |     | |     | | (_)
// | \  / | ___   __| |_   _| | __ _| |_ _  ___  _ __
// | |\/| |/ _ \ / _` | | | | |/ _` | __| |/ _ \| '_ \
// | |  | | (_) | (_| | |_| | | (_| | |_| | (_) | | | |
// |_|  |_|\___/ \__,_|\__,_|_|\__,_|\__|_|\___/|_| |_|
//*****************************************************

    public int getRelativeMinor(int note) {
        int relativeMinor = 0;

        for (int j = 0; j < 12; j++) {
            if (notes[j] == note) {
                int index = (j - 3) % SIZE_OF_NOTES_ARRAY;

                if (index < 0) {
                    index += 12;
                }

                relativeMinor = notes[index];
                break;
            }
        }

        return relativeMinor;
    }


    public int getRelativeMajor(int note) {
        int relativeMajor = 0;

        for (int j = 0; j < 12; j++) {
            if (notes[j] == note) {
                int index = (j + 3) % SIZE_OF_NOTES_ARRAY;

                relativeMajor = notes[index];
                break;
            }
        }

        return relativeMajor;
    }


    public ArrayList findCommonChords(Note[][] origKeyChords, Note[][] newKeyChords) {
        ArrayList commonChords = new ArrayList<Note[][]>();

        for (int i = 0; i < origKeyChords.length; i++) {
            for (int j = 0; j < newKeyChords.length; j++) {
                if ((origKeyChords[i][0].equals(newKeyChords[j][0])) &&
                        (origKeyChords[i][1].equals(newKeyChords[j][1])) &&
                        (origKeyChords[i][2].equals(newKeyChords[j][2]))) {
                    commonChords.add(origKeyChords[j]);
                }
            }
        }

        return commonChords;
    }


    public String modulateFromMajor(int i, int root) {
        Note[][] rootKeyChords = scaleChords;

        int leftOne = (i - 1) % 12;
        int rightOne = (i + 1) % 12;

        if (leftOne < 0) {
            leftOne += 12;
        }

        //Populate similarKeys array
        similarKeys[0] = getRelativeMinor(root);
        similarKeys[1] = circleOfFifthsMajor[leftOne];
        similarKeys[2] = getRelativeMinor(circleOfFifthsMajor[leftOne]);
        similarKeys[3] = circleOfFifthsMajor[rightOne];
        similarKeys[4] = getRelativeMinor(circleOfFifthsMajor[rightOne]);


        //1st Chord. Add tonic of original key
        cphr1.addChord(scaleChord1);
        bottomNotes.add(scaleChord1[0]);
        middleNotes.add(scaleChord1[1]);
        topNotes.add(scaleChord1[2]);


        //2nd Chord. Add random chord of original key
        int i2 = rn.nextInt(7);
        cphr2.addChord(scaleChords[i2]);
        bottomNotes.add(scaleChords[i2][0]);
        middleNotes.add(scaleChords[i2][1]);
        topNotes.add(scaleChords[i2][2]);


        //Find key to modulate to from similar keys
        int i3 = rn.nextInt(5);
        int newKey = similarKeys[i3];
        String newKeyString = getNote(new Note(newKey, C));


        //Make the scale and chords of the new key
        if (i3 == 0 || i3 == 2 || i3 == 4) {
            makeMinorScale(newKey);
        } else {
            makeMajorScale(newKey);
        }


        //3rd Chord. Add a common chord to begin the modulation
        Note[][] newKeyChords = scaleChords;
        ArrayList commonChords = findCommonChords(rootKeyChords, newKeyChords);

        if (commonChords.contains(rootKeyChords[1])) {
            cphr3.addChord(scaleChord2);
            bottomNotes.add(scaleChord2[0]);
            middleNotes.add(scaleChord2[1]);
            topNotes.add(scaleChord2[2]);
        } else if (commonChords.contains(rootKeyChords[3])) {
            cphr3.addChord(scaleChord4);
            bottomNotes.add(scaleChord4[0]);
            middleNotes.add(scaleChord4[1]);
            topNotes.add(scaleChord4[2]);
        } else {
            int i4 = rn.nextInt(commonChords.size());
            cphr3.addChord((Note[])commonChords.get(i4));
            bottomNotes.add(((Note[])commonChords.get(i4))[0]);
            middleNotes.add(((Note[])commonChords.get(i4))[1]);
            topNotes.add(((Note[])commonChords.get(i4))[2]);
        }


        //4th Chord. Add the dominant of the new key
        cphr4.addChord(scaleChord5MelodicMinor);
        bottomNotes.add(scaleChord5MelodicMinor[0]);
        middleNotes.add(scaleChord5MelodicMinor[1]);
        topNotes.add(scaleChord5MelodicMinor[2]);


        //5th Chord. Add the tonic of the new key
        cphr5.addChord(scaleChord1);
        bottomNotes.add(scaleChord1[0]);
        middleNotes.add(scaleChord1[1]);
        topNotes.add(scaleChord1[2]);


        //Set the scores
        setScoreSpecific(bottomNotes, "left");
        setScoreSpecific(middleNotes, "middle");
        setScoreSpecific(topNotes, "right");


        p.addCPhrase(cphr1);
        p.addCPhrase(cphr2);
        p.addCPhrase(cphr3);
        p.addCPhrase(cphr4);
        p.addCPhrase(cphr5);

        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Modulation.mid");

        if(i3 == 0 || i3 == 2 || i3 == 4){
            return newKeyString + "m";
        } else {
            return newKeyString;
        }
    }


    public String modulateFromMinor(int i, int root){
        int leftOne = (i - 1) % 12;
        int rightOne = (i + 1) % 12;

        if (leftOne < 0) {
            leftOne += 12;
        }

        //Populate similarKeys array
        similarKeys[0] = getRelativeMajor(root);
        similarKeys[1] = circleOfFifthsMinor[leftOne];
        similarKeys[2] = getRelativeMajor(circleOfFifthsMinor[leftOne]);
        similarKeys[3] = circleOfFifthsMinor[rightOne];
        similarKeys[4] = getRelativeMajor(circleOfFifthsMinor[rightOne]);


        //1st Chord. Add tonic of original key
        cphr1.addChord(scaleChord1);
        bottomNotes.add(scaleChord1[0]);
        middleNotes.add(scaleChord1[1]);
        topNotes.add(scaleChord1[2]);


        //2nd Chord. Add IV or VI of original key
        int i2 = rn.nextInt(2);
        if(i2 == 0){
            cphr2.addChord(scaleChord4);
            bottomNotes.add(scaleChord4[0]);
            middleNotes.add(scaleChord4[1]);
            topNotes.add(scaleChord4[2]);
        } else {
            cphr2.addChord(scaleChord6);
            bottomNotes.add(scaleChord6[0]);
            middleNotes.add(scaleChord6[1]);
            topNotes.add(scaleChord6[2]);
        }


        //3rd Chord. Add II of original key
        cphr3.addChord(scaleChord2);
        bottomNotes.add(scaleChord2[0]);
        middleNotes.add(scaleChord2[1]);
        topNotes.add(scaleChord2[2]);


        //4th Chord. Add V of original key
        cphr4.addChord(scaleChord5MelodicMinor);
        bottomNotes.add(scaleChord5MelodicMinor[0]);
        middleNotes.add(scaleChord5MelodicMinor[1]);
        topNotes.add(scaleChord5MelodicMinor[2]);


        //5th Chord. Add tonic of original key as common key
        cphr5.addChord(scaleChord1);
        bottomNotes.add(scaleChord1[0]);
        middleNotes.add(scaleChord1[1]);
        topNotes.add(scaleChord1[2]);


        //6th Chord. Add tonic of original key as common key
        cphr6.addChord(scaleChord1);
        bottomNotes.add(scaleChord1[0]);
        middleNotes.add(scaleChord1[1]);
        topNotes.add(scaleChord1[2]);


        //Find key to modulate to from similar keys
        int i3 = rn.nextInt(5);
        int newKey = similarKeys[i3];
        String newKeyString = getNote(new Note(newKey, C));


        //Make the scale and chords of the new key
        if (i3 == 1 || i3 == 3) {
            makeMinorScale(newKey);
        } else {
            makeMajorScale(newKey);
        }


        //7th Chord. Add the dominant of the new key
        cphr7.addChord(scaleChord5);
        bottomNotes.add(scaleChord5[0]);
        middleNotes.add(scaleChord5[1]);
        topNotes.add(scaleChord5[2]);


        //8th Chord. Add the tonic of the new key
        cphr8.addChord(scaleChord1);
        bottomNotes.add(scaleChord1[0]);
        middleNotes.add(scaleChord1[1]);
        topNotes.add(scaleChord1[2]);


        //Set the scores
        setScoreSpecific(bottomNotes, "left");
        setScoreSpecific(middleNotes, "middle");
        setScoreSpecific(topNotes, "right");


        p.addCPhrase(cphr1);
        p.addCPhrase(cphr2);
        p.addCPhrase(cphr3);
        p.addCPhrase(cphr4);
        p.addCPhrase(cphr5);
        p.addCPhrase(cphr6);
        p.addCPhrase(cphr7);
        p.addCPhrase(cphr8);

        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/Modulation.mid");

        if(i3 == 1 || i3 == 3){
            return newKeyString + "m";
        } else {
            return newKeyString;
        }
    }


    public String makeMIDIEasyModulation(){
        int i = rn.nextInt(12);
        int root;

        int minorOrMajor = rn.nextInt(2);

        if(minorOrMajor == 0) {
            minor = true;
            root = circleOfFifthsMinor[i];
            makeMinorScale(root);
            rootKeyString = getNote(new Note(root, C));
            return modulateFromMinor(i, root);
        } else {
            major = true;
            root = circleOfFifthsMajor[i];
            makeMajorScale(root);
            rootKeyString = getNote(new Note(root, C));
            return modulateFromMajor(i, root);
        }

    }


    public String makeMIDIMediumModulation(){
        return "";
    }


    public String makeMIDIHardModulation(){
        return "";
    }




//*********************************************************
// __          __                      _   _       _
// \ \        / /                     | \ | |     | |
//  \ \  /\  / / __ ___  _ __   __ _  |  \| | ___ | |_ ___
//   \ \/  \/ / '__/ _ \| '_ \ / _` | | . ` |/ _ \| __/ _ \
//    \  /\  /| | | (_) | | | | (_| | | |\  | (_) | ||  __/
//     \/  \/ |_|  \___/|_| |_|\__, | |_| \_|\___/ \__\___|
//                              __/ |
//                             |___/
//*********************************************************

    public int makeMIDIEasyWrongNote(){
        int i = rn.nextInt(12);
        int rootNote = notes[i];
        jScore.setKeySignature(rootNote);


        //Make major or minor scale
        int minorOrMajor = rn.nextInt(2);

        if(minorOrMajor == 0) {
            minor = true;
            makeMinorScale(rootNote);
        } else {
            major = true;
            makeMajorScale(rootNote);
        }


        //Set scale notes
        if(minor) {
            scaleNotes = minorScale;
        } else {
            scaleNotes = majorScale;
        }


        //Make melody
        List melody = new LinkedList<Note>();
        double lengthSoFar = 0.0;

        while(lengthSoFar != 4.0){
            int p = rn.nextInt(15);
            int pitch = scaleNotes[p];

            if(noteLengthsList.size() == 0){
                break;
            }

            int d = rn.nextInt(noteLengthsList.size());
            double duration = (double)noteLengthsList.get(d);

            if(duration + lengthSoFar <= 4.0){
                melody.add(new Note(pitch, duration));
                lengthSoFar += duration;
            } else {
                noteLengthsList.remove(d);
            }
        }

        Note[] melodyArray = new Note[melody.size()];
        melody.toArray(melodyArray);


        //Add melody array to phrase
        phr2.addNoteList(melodyArray);
        setScore(phr2);
        originalPhr2.addNoteList(melodyArray);

        //Find notes which do not belong to this scale
        List nonScaleNotes = new LinkedList();

        for(int n : notes){
            if(!ArrayUtils.contains(scaleNotes,n)){
                nonScaleNotes.add(n);
            }
        }


        //Find random note in melody to change
        int i2 = rn.nextInt(melody.size());
        int i3 = rn.nextInt(nonScaleNotes.size());

        double duration = melodyArray[i2].getDuration();
        int pitch = (int)nonScaleNotes.get(i3);
        int originalPitch = melodyArray[i2].getPitch();


        //Ensure new note is maximum of 3 semitones from original so it's not too obvious
        while(Math.abs(originalPitch - pitch) > 3){
            i3 = rn.nextInt(nonScaleNotes.size());
            pitch = (int)nonScaleNotes.get(i3);
        }

        melodyArray[i2] = new Note(pitch, duration);

        System.out.println("Incorrect note index: " + i2);


        //Add melody array to phrase
        phr1.addNoteList(melodyArray);
        theirMelodyAnswer = melodyArray;
        setScoreEditable(phr1);

        p.add(phr1);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/WrongNote.mid");

        return i2;
    }


    public int makeMIDIMediumWrongNote(){
        int i = rn.nextInt(12);
        int rootNote = notes[i];
        jScore.setKeySignature(rootNote);


        //Make major or minor scale
        int minorOrMajor = rn.nextInt(2);

        if(minorOrMajor == 0) {
            minor = true;
            makeMinorScale(rootNote);
        } else {
            major = true;
            makeMajorScale(rootNote);
        }


        //Set scale notes
        if(minor) {
            scaleNotes = minorScale;
        } else {
            scaleNotes = majorScale;
        }


        //Made melody
        List melody = new LinkedList<Note>();
        double lengthSoFar = 0.0;

        while(lengthSoFar != 4.0){
            int p = rn.nextInt(15);
            int pitch = scaleNotes[p];

            if(noteLengthsList.size() == 0){
                break;
            }

            int d = rn.nextInt(noteLengthsList.size());
            double duration = (double)noteLengthsList.get(d);


            if(duration + lengthSoFar <= 4.0){
                melody.add(new Note(pitch, duration));
                lengthSoFar += duration;
            } else {
                noteLengthsList.remove(d);
            }
        }

        Note[] melodyArray = new Note[melody.size()];
        melody.toArray(melodyArray);


        //Add melody array to phrase
        phr2.addNoteList(melodyArray);
        originalPhr2.addNoteList(melodyArray);


        //Find random note in melody to change
        int i2 = rn.nextInt(melodyArray.length);
        int i3 = rn.nextInt(2);

        double duration = melodyArray[i2].getDuration();
        int pitch = melodyArray[i2].getPitch();

        int[] upOrDown = {-3,3};
        int newPitch = pitch + upOrDown[i3];

        System.out.println("Up or down: " + i3);
        System.out.println("Wrong note: " + i2);

        melodyArray[i2] = new Note(newPitch, duration);
        phr1.addNoteList(melodyArray);

        setScoreEditable(phr1);


        p.add(phr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/WrongNote.mid");

        return i2;
    }


    public int makeMIDIHardWrongNote(){
        int i = rn.nextInt(12);
        int rootNote = notes[i];
        jScore.setKeySignature(rootNote);


        //Make major or minor scale
        int minorOrMajor = rn.nextInt(2);

        if(minorOrMajor == 0) {
            minor = true;
            makeMinorScale(rootNote);
        } else {
            major = true;
            makeMajorScale(rootNote);
        }


        //Set scale notes
        if(minor) {
            scaleNotes = minorScale;
        } else {
            scaleNotes = majorScale;
        }


        //Made melody
        List melody = new LinkedList<Note>();
        double lengthSoFar = 0.0;

        while(lengthSoFar != 4.0){
            int p = rn.nextInt(15);
            int pitch = scaleNotes[p];

            if(noteLengthsList.size() == 0){
                break;
            }

            int d = rn.nextInt(noteLengthsList.size());
            double duration = (double)noteLengthsList.get(d);


            if(duration + lengthSoFar <= 4.0){
                melody.add(new Note(pitch, duration));
                lengthSoFar += duration;
            } else {
                noteLengthsList.remove(d);
            }
        }

        Note[] melodyArray = new Note[melody.size()];
        melody.toArray(melodyArray);


        //Add melody array to phrase
        phr2.addNoteList(melodyArray);
        originalPhr2.addNoteList(melodyArray);


        //Find random note in melody to change
        int i2 = rn.nextInt(melodyArray.length);
        int i3 = rn.nextInt(2);

        double duration = melodyArray[i2].getDuration();
        int pitch = melodyArray[i2].getPitch();

        int[] upOrDown = {-1,1};
        int newPitch = pitch + upOrDown[i3];

        System.out.println("Up or down: " + i3);
        System.out.println("Wrong note: " + i2);

        melodyArray[i2] = new Note(newPitch, duration);
        phr1.addNoteList(melodyArray);

        setScoreEditable(phr1);


        p.add(phr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/WrongNote.mid");

        return i2;
    }




//******************************************************
// __      __   _            _______
// \ \    / /  (_)          |__   __|
//  \ \  / /__  _  ___ ___     | |_   _ _ __   ___ _ __
//   \ \/ / _ \| |/ __/ _ \    | | | | | '_ \ / _ \ '__|
//    \  / (_) | | (_|  __/    | | |_| | | | |  __/ |
//     \/ \___/|_|\___\___|    |_|\__,_|_| |_|\___|_|
// ******************************************************

    public String makeMIDIEasyVoiceTuner(){
        return makeMIDIMediumPitch();
    }


    public Note[] makeMIDIMediumVoiceTuner(){
        int i = rn.nextInt(12);
        int rootNote = notes[i];


        //Make major or minor scale
        int minorOrMajor = rn.nextInt(2);

        if(minorOrMajor == 0) {
            minor = true;
            makeMinorScale(rootNote);
        } else {
            major = true;
            makeMajorScale(rootNote);
        }


        //Set scale notes
        if(minor) {
            scaleNotes = minorScale;
        } else {
            scaleNotes = majorScale;
        }


        //Make a one octave version of scaleNotes
        int[] scaleNotesOneOctave =  new int[7];
        for(int j = 0; j < 7; j++){
            scaleNotesOneOctave[j] = scaleNotes[j];
        }


        //Make 3 note melody
        int noteIndex = rn.nextInt(7);
        int durationIndex = rn.nextInt(7);

        Note n1 = new Note(scaleNotesOneOctave[noteIndex], (double)noteLengthsList.get(durationIndex));

        noteIndex = rn.nextInt(7);
        durationIndex = rn.nextInt(7);

        Note n2 = new Note(scaleNotesOneOctave[noteIndex], (double)noteLengthsList.get(durationIndex));

        noteIndex = rn.nextInt(7);
        durationIndex = rn.nextInt(7);

        Note n3 = new Note(scaleNotesOneOctave[noteIndex], (double)noteLengthsList.get(durationIndex));

        Note[] melodyArray = {n1,n2,n3};
        phr2.addNoteList(melodyArray);

        p.addPhrase(phr2);
        s.addPart(p);

        Write.midi(s, "/Users/timannoel/Documents/Uni/3rd Year/Individual Project/EarTrainerProject/src/EarTrainer/Music/VoiceTuner.mid");
        return melodyArray;
    }


    public String makeMIDIHardVoiceTuner(){
        return "";
    }
}