package itx.java.examples.enigma;

import com.fasterxml.jackson.databind.ObjectMapper;
import itx.java.examples.enigma.alphabet.Alphabet;
import itx.java.examples.enigma.configuration.EnigmaConfiguration;
import itx.java.examples.enigma.configuration.EnigmaSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by gergej on 17.1.2017.
 */
public final class EnigmaUtils {

    private static final Logger LOG = LoggerFactory.getLogger(EnigmaUtils.class);

    private EnigmaUtils() {
        throw new UnsupportedOperationException();
    }

    private static final Alphabet defaultAlphabet26;
    private static final int[][] reflectorData26;
    private static final int[][] rotor0Data26;
    private static final int[][] rotor1Data26;
    private static final int[][] rotor2Data26;

    private static final Alphabet defaultAlphabetBase64;
    private static final int[][] reflectorDataBase64;
    private static final int[][] rotor0DataBase64;
    private static final int[][] rotor1DataBase64;
    private static final int[][] rotor2DataBase64;

    static {
        //default 26 letter alphabet (all uppercase letters)
        defaultAlphabet26 = Alphabet.buildAlphabet26();
        rotor0Data26 = createSubstitutionMap(defaultAlphabet26,
                "FYLDVAEQHMCZNXTKJPWUOGISBR");
        rotor1Data26 = createSubstitutionMap(defaultAlphabet26,
                "SXROTNJHUBECYWPDGMAFIZVKQL");
        rotor2Data26 = createSubstitutionMap(defaultAlphabet26,
                "WGDYQLVBIFJAUSCTHENOKXRZMP");
        reflectorData26 = createReflectorSubstitutionMap(Alphabet.buildAlphabet26());

        //default base 64 alphabet
        defaultAlphabetBase64 = Alphabet.buildAlphabetBase64();
        rotor0DataBase64 = createSubstitutionMap(defaultAlphabetBase64,
                "2AFyaRcKfVvu1=Dp5+iYw9TXbOqm0NQdeJgnEG#hrUx3t8CWMHkzSs7ZPlL46oI/Bj");
        rotor1DataBase64 = createSubstitutionMap(defaultAlphabetBase64,
                "9C/c2YUL6#jbnhJGkDuK7SW5PpTqswzoig1OX+0VyrvdmHFQAEI3Rx=BZtN48ealMf");
        rotor2DataBase64 = createSubstitutionMap(defaultAlphabetBase64,
                "pzu4IXGK/7lanbrdBF#USyPsj5c6e2OwVhR8MfLY3xZ=1HT+gtDQJCAWikoEvm0qN9");
        reflectorDataBase64 = createReflectorSubstitutionMap(defaultAlphabetBase64);
    }

    public static int[][] getReflectorData26() {
        return copySubstitutionTable(reflectorData26);
    }

    public static int[][] getReflectorDataBase64() {
        return copySubstitutionTable(reflectorDataBase64);
    }

    public static int[][] getRotor0Data26() {
        return copySubstitutionTable(rotor0Data26);
    }

    public static int[][] getRotor1Data26() {
        return copySubstitutionTable(rotor1Data26);
    }

    public static int[][] getRotor2Data26() {
        return copySubstitutionTable(rotor2Data26);
    }

    public static int[][] getRotor0DataBase64() {
        return copySubstitutionTable(rotor0DataBase64);
    }

    public static int[][] getRotor1DataBase64() {
        return copySubstitutionTable(rotor1DataBase64);
    }

    public static int[][] getRotor2DataBase64() {
        return copySubstitutionTable(rotor2DataBase64);
    }


    public static int[][] createReflectorSubstitutionMap(Alphabet alphabet) {
        int[][] result = new int[alphabet.getSize()][2];
        for (int i = 0; i < alphabet.getSize(); i++) {
            result[i][0] = i;
            result[i][1] = alphabet.getIndex(alphabet.getCharacter(alphabet.getSize() - i - 1));
        }
        return result;
    }

    public static int[][] createSubstitutionMap(Alphabet alphabet, String randomizedAlphabet) {
        if (alphabet.getSize() != randomizedAlphabet.length()) {
            throw new UnsupportedOperationException("input and output string must have same lenght");
        }
        int[][] result = new int[alphabet.getSize()][2];
        for (int i = 0; i < alphabet.getSize(); i++) {
            result[i][0] = i;
            result[i][1] = alphabet.getIndex(randomizedAlphabet.charAt(i));
        }
        return result;
    }

    public static Character[][] generateRandomPlugBoardSetup(Alphabet alphabet) {
        int length = alphabet.getSize() / 2;
        if (length % 2 == 0) {
            throw new UnsupportedOperationException("alphabet length must be divisible by 2");
        }
        Integer[] randomIndexes = generateRandomIndexes(length);
        Character[][] result = new Character[length][2];
        for (int i = 0; i < length; i++) {
            result[i][0] = alphabet.getCharacter(randomIndexes[i]);
            result[i][1] = alphabet.getCharacter(randomIndexes[i] + length);
        }
        return result;
    }

    public static String getRandomizedAlphabet(Alphabet alphabet) {
        Integer[] indexes = generateRandomIndexes(alphabet.getSize());
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<indexes.length; i++) {
            sb.append(alphabet.getCharacter(indexes[i]));
        }
        return sb.toString();
    }

    public static Integer[] generateRandomIndexes(int length) {
        Random random = new Random(System.currentTimeMillis());
        Integer[] randomIndexes = new Integer[length];
        int counter = 0;
        while (true) {
            int rnd = random.nextInt(length);
            boolean isUsed = false;
            for (int i = 0; i < length; i++) {
                if (randomIndexes[i] != null && randomIndexes[i] == rnd) {
                    isUsed = true;
                }
            }
            if (!isUsed) {
                randomIndexes[counter] = rnd;
                counter++;
            }
            if (counter >= length) {
                break;
            }
        }
        return randomIndexes;
    }

    public static int getSubstitutionOf(int[][] table, int value, boolean reverse) {
        if (!reverse) {
            if (value >= table.length) {
                throw new UnsupportedOperationException("no forward substitution found for " + value);
            }
            return table[value][1];
        } else {
            for (int i = 0; i < table.length; i++) {
                if (table[i][1] == value) {
                    return i;
                }
            }
            throw new UnsupportedOperationException("no reverse substitution found for " + value);
        }
    }

    public static int[][] copySubstitutionTable(int[][] table) {
        int[][] result = new int[table.length][table[0].length];
        for (int i=0; i<table.length; i++) {
            result[i] = Arrays.copyOf(table[i], table[i].length);
        }
        return result;
    }

    public static int[][] shiftSubstitutionTable(int[][] table) {
        int[] firstRow = table[0];
        for (int i = 0; i < (table.length-1); i++) {
            table[i] = table[i + 1];
        }
        table[table.length - 1] = firstRow;
        return table;
    }

    public static int[][] shiftSubstitutionTable(int[][] table, int shift) {
        for (int i=0; i<shift; i++) {
            shiftSubstitutionTable(table);
        }
        return table;
    }

    public static String prettyPrint(int columns, String data) {
        StringBuilder sb = new StringBuilder();
        int groups = 0;
        int counter = 1;
        for (int i=0; i<data.length(); i++) {
            sb.append(data.charAt(i));
            if (counter % 5 == 0 ) {
                sb.append(' ');
                groups++;
                counter = 1;
            } else {
                counter++;
            }
            if (groups >= columns) {
                sb.append('\n');
                groups = 0;
            }
        }
        return sb.toString();
    }

    public static String prettyRead(String data) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<data.length(); i++) {
           if ('\t' != data.charAt(i) && '\n' != data.charAt(i) && ' ' != data.charAt(i) && '\r' != data.charAt(i)) {
               sb.append(data.charAt(i));
           }
        }
        return sb.toString().trim();
    }

    public static EnigmaConfiguration readEnigmaConfiguration(InputStream is) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(is, EnigmaConfiguration.class);
    }

    public static EnigmaSettings readEnigmaSetup(InputStream is) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(is, EnigmaSettings.class);
    }

    public static int factorial(int n) {
        int fact = 1;
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }

    public static boolean compareArrays(int[] data0, int[] data1) {
        if (data0.length != data1.length) {
            return false;
        }
        for (int i=0; i<data0.length; i++) {
            if (data0[i] != data1[i]) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] main) {
        String randomizedAlphabet = getRandomizedAlphabet(Alphabet.buildAlphabet26());
        LOG.info(randomizedAlphabet);
    }

}