package itx.java.examples.enigma;

import com.fasterxml.jackson.databind.ObjectMapper;
import itx.java.examples.enigma.alphabet.Alphabet;
import itx.java.examples.enigma.configuration.EnigmaConfiguration;
import itx.java.examples.enigma.configuration.EnigmaSettings;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Random;

/**
 * Created by gergej on 17.1.2017.
 */
public final class Utils {

    private Utils() {
        throw new UnsupportedOperationException();
    }

    public static String encryptOrDecrypt(Enigma enigma, String message) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < message.length(); i++) {
            sb.append(enigma.encryptOrDecrypt(message.charAt(i)));
        }
        return sb.toString();
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
        Integer randomIndexes[] = new Integer[length];
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
            for (int j=0; j<table[0].length; j++) {
                result[i][j] = table[i][j];
            }
        }
        return result;
    }

    public static int[][] shiftSubstitutionTable(int[][] table) {
        int firstRow[] = table[0];
        for (int i = 0; i < (table.length-1); i++) {
            table[i] = table[i + 1];
        }
        table[table.length - 1] = firstRow;
        return table;
    }

    public static int[][] shiftSubstitutionTable(int[][] table, int shift) {
        for (int i=0; i<shift; i++) {
            table = shiftSubstitutionTable(table);
        }
        return table;
    }

    public static String prettyPrint(int columns, String data) {
        StringBuffer sb = new StringBuffer();
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
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<data.length(); i++) {
           if ('\t' != data.charAt(i) && '\n' != data.charAt(i) && ' ' != data.charAt(i) && '\r' != data.charAt(i)) {
               sb.append(data.charAt(i));
           }
        }
        return sb.toString().trim();
    }

    /**
     * encrypt ordinary unicode java string
     * @param enigma
     *   enigma with base64 alphabet
     * @param input
     *   ordinary unicode java string
     * @return
     *   encrypted string in base64 format
     */
    public static String encryptUnicodeString(Enigma enigma, String input) {
        byte[] inputBytes = Base64.getEncoder().encode(input.getBytes(Charset.forName("UTF-8")));
        String inputData = new String(inputBytes, Charset.forName("UTF-8"));
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<inputData.length(); i++) {
            sb.append(enigma.encryptOrDecrypt(inputData.charAt(i)));
        }
        return sb.toString();
    }

    /**
     * decrypt data in base64 format
     * @param enigma
     *   enigma with base64 alphabet
     * @param input
     *   encrypted data in base64 format
     * @return
     *   ordinary unicode java string
     */
    public static String decodeBase64String(Enigma enigma, String input) {
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<input.length(); i++) {
            sb.append(enigma.encryptOrDecrypt(input.charAt(i)));
        }
        byte[] inputBytes = Base64.getDecoder().decode(sb.toString());
        return new String(inputBytes, Charset.forName("UTF-8"));
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
        System.out.println(randomizedAlphabet);
    }

}