package itx.java.examples.enigma;

import itx.java.examples.enigma.alphabet.Alphabet;

/**
 * Created by gergej on 17.1.2017.
 */
public final class EnigmaSetup {

    private EnigmaSetup() {
        throw new UnsupportedOperationException();
    }

    public static final Alphabet defaultAlphabet26;
    public static final int[][] reflectorData26;
    public static final int[][] rotor0Data26;
    public static final int[][] rotor1Data26;
    public static final int[][] rotor2Data26;

    public static final Alphabet defaultAlphabetBase64;
    public static final int[][] reflectorDataBase64;
    public static final int[][] rotor0DataBase64;
    public static final int[][] rotor1DataBase64;
    public static final int[][] rotor2DataBase64;

    static {
        //default 26 letter alphabet (all uppercase letters)
        defaultAlphabet26 = Alphabet.buildAlphabet26();
        rotor0Data26 = Utils.createSubstitutionMap(defaultAlphabet26,
                "FYLDVAEQHMCZNXTKJPWUOGISBR");
        rotor1Data26 = Utils.createSubstitutionMap(defaultAlphabet26,
                "SXROTNJHUBECYWPDGMAFIZVKQL");
        rotor2Data26 = Utils.createSubstitutionMap(defaultAlphabet26,
                "WGDYQLVBIFJAUSCTHENOKXRZMP");
        reflectorData26 = Utils.createReflectorSubstitutionMap(Alphabet.buildAlphabet26());

        //default base 64 alphabet
        defaultAlphabetBase64 = Alphabet.buildAlphabetBase64();
        rotor0DataBase64 = Utils.createSubstitutionMap(defaultAlphabetBase64,
        "2AFyaRcKfVvu1=Dp5+iYw9TXbOqm0NQdeJgnEG#hrUx3t8CWMHkzSs7ZPlL46oI/Bj");
        rotor1DataBase64 = Utils.createSubstitutionMap(defaultAlphabetBase64,
        "9C/c2YUL6#jbnhJGkDuK7SW5PpTqswzoig1OX+0VyrvdmHFQAEI3Rx=BZtN48ealMf");
        rotor2DataBase64 = Utils.createSubstitutionMap(defaultAlphabetBase64,
        "pzu4IXGK/7lanbrdBF#USyPsj5c6e2OwVhR8MfLY3xZ=1HT+gtDQJCAWikoEvm0qN9");
        reflectorDataBase64 = Utils.createReflectorSubstitutionMap(defaultAlphabetBase64);
    }

}
