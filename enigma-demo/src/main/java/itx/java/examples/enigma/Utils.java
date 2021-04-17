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
public final class Utils {

    private static final Logger LOG = LoggerFactory.getLogger(Utils.class);

    private Utils() {
        throw new UnsupportedOperationException();
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

    public static Character[][] generateRandomPlugBoadrSetup(Alphabet alphabet) {
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