package itx.java.examples.enigma.tests;

import itx.java.examples.enigma.EnigmaUtils;
import itx.java.examples.enigma.impl.rotors.RotorGroupBuilder;
import itx.java.examples.enigma.rotors.Reflector;
import itx.java.examples.enigma.rotors.Rotor;
import itx.java.examples.enigma.rotors.RotorGroup;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by gergej on 18.1.2017.
 */
public class PartialTest {

    @Test
    public void testCopyTable() {
        int[][] rotor0Data26 = EnigmaUtils.getRotor0Data26();
        int[][] copy = EnigmaUtils.copySubstitutionTable(rotor0Data26);
        Assert.assertTrue(copy.length == rotor0Data26.length);
        Assert.assertTrue(copy[0].length == rotor0Data26[0].length);
        for (int i=0; i<copy.length; i++) {
            for (int j=0;j<copy[0].length; j++) {
                Assert.assertTrue(copy[i].length == rotor0Data26[i].length);
                Assert.assertTrue(copy[i][j] == rotor0Data26[i][j]);
            }
        }
    }

    @Test
    public void testSubstitution() {
        int[][] table = new int[3][2];
        table[0][0] = 0; table[0][1] = 2;
        table[1][0] = 1; table[1][1] = 0;
        table[2][0] = 2; table[2][1] = 1;
        int subsFwd = EnigmaUtils.getSubstitutionOf(table, 0, false);
        int subsRev = EnigmaUtils.getSubstitutionOf(table, 1, true);
        Assert.assertTrue(2 == subsFwd);
        Assert.assertTrue(2 == subsRev);
    }

    @Test
    public void testShiftTable() {
        int[][] table = new int[5][2];
        table[0][0] = 0; table[0][1] = 2;
        table[1][0] = 1; table[1][1] = 0;
        table[2][0] = 2; table[2][1] = 1;
        table[3][0] = 3; table[3][1] = 4;
        table[4][0] = 4; table[4][1] = 3;
        table = EnigmaUtils.shiftSubstitutionTable(table);
        Assert.assertTrue(table[0][0] == 1);
        Assert.assertTrue(table[0][1] == 0);
        Assert.assertTrue(table[4][0] == 0);
        Assert.assertTrue(table[4][1] == 2);
    }

    @Test
    public void testRotorForward() {
        int[][] rotor0Data26 = EnigmaUtils.getRotor0Data26();
        Rotor rotor = Rotor.builder()
                .setSubstitutionTable(rotor0Data26)
                .setIndex(0)
                .build();
        for (int i=0; i<rotor0Data26.length; i++) {
            int result = rotor.substituteForward(i);
            int subtitute = rotor0Data26[i][1];
            Assert.assertEquals(subtitute, result);
        }
    }

    @Test
    public void testRotor() {
        int[][] rotor0Data26 = EnigmaUtils.getRotor0Data26();
        Rotor rotor = Rotor.builder()
                .setSubstitutionTable(rotor0Data26)
                .setIndex(0)
                .build();
        for (int i = 0; i < rotor0Data26.length; i++) {
            Integer result = rotor.substituteForward(i);
            Integer reverse = rotor.substituteReverse(result);
            Assert.assertEquals(reverse, Integer.valueOf(i));
        }
    }

    @Test
    public void testShiftedRotor() {
        int[][] rotor0Data26 = EnigmaUtils.getRotor0Data26();
        Rotor rotor = Rotor.builder()
                .setSubstitutionTable(rotor0Data26)
                .setIndex(5)
                .build();
        for (int i = 0; i < rotor0Data26.length; i++) {
            Integer result = rotor.substituteForward(i);
            Integer reverse = rotor.substituteReverse(result);
            Assert.assertEquals(reverse, Integer.valueOf(i));
        }
    }

    @Test
    public void testReflector() {
        int[][] reflectorData26 = EnigmaUtils.getReflectorData26();
        Reflector reflector = Reflector.builder()
                .setSubstitutionTable(reflectorData26)
                .build();
        for (int i=0; i<reflectorData26.length; i++) {
            Integer result = reflector.substitute(i);
            Integer subtitute = reflectorData26[i][1];
            Assert.assertEquals(subtitute, result);
        }
    }

    @Test
    public void testRotorGroup() {
        int[][] rotor0Data26 = EnigmaUtils.getRotor0Data26();
        int[][] rotor1Data26 = EnigmaUtils.getRotor1Data26();
        int[][] rotor2Data26 = EnigmaUtils.getRotor2Data26();
        RotorGroupBuilder rotorBuilder = RotorGroup.builder();
        rotorBuilder.addRotor(Rotor.builder()
                .setSubstitutionTable(rotor0Data26)
                .setIndex(0)
                .build());
        rotorBuilder.addRotor(Rotor.builder()
                .setSubstitutionTable(rotor1Data26)
                .setIndex(0)
                .build());
        rotorBuilder.addRotor(Rotor.builder()
                .setSubstitutionTable(rotor2Data26)
                .setIndex(0)
                .build());
        RotorGroup rotorGroup = rotorBuilder.build();
        Reflector reflector = Reflector.builder().setSubstitutionTable(EnigmaUtils.getReflectorData26()).build();

        for (int i=0; i<rotor0Data26.length; i++) {
            Integer result0 = rotorGroup.substituteForward(i);
            Integer reflected0 = reflector.substitute(result0);
            Integer outPut0 = rotorGroup.substituteReverse(reflected0);

            Integer result1 = rotorGroup.substituteForward(outPut0);
            Integer reflected1 = reflector.substitute(result1);
            Integer outPut1 = rotorGroup.substituteReverse(reflected1);

            Assert.assertTrue(i == outPut1, i + " != " + outPut0);
        }
    }

    @Test
    public void testRotorGroupRotate() {
        int[][] rotor0Data26 = EnigmaUtils.getRotor0Data26();
        int[][] rotor1Data26 = EnigmaUtils.getRotor1Data26();
        int[][] rotor2Data26 = EnigmaUtils.getRotor2Data26();
        RotorGroupBuilder rotorBuilder = RotorGroup.builder();
        rotorBuilder.addRotor(Rotor.builder()
                .setSubstitutionTable(rotor0Data26)
                .setIndex(0)
                .build());
        rotorBuilder.addRotor(Rotor.builder()
                .setSubstitutionTable(rotor1Data26)
                .setIndex(0)
                .build());
        rotorBuilder.addRotor(Rotor.builder()
                .setSubstitutionTable(rotor2Data26)
                .setIndex(0)
                .build());
        RotorGroup rotorGroup = rotorBuilder.build();
        Reflector reflector = Reflector.builder().setSubstitutionTable(EnigmaUtils.getReflectorData26()).build();

        for (int r=0; r<10; r++) {
            for (int i = 0; i < rotor0Data26.length; i++) {
                Integer result0 = rotorGroup.substituteForward(i);
                Integer reflected0 = reflector.substitute(result0);
                Integer outPut0 = rotorGroup.substituteReverse(reflected0);

                Integer result1 = rotorGroup.substituteForward(outPut0);
                Integer reflected1 = reflector.substitute(result1);
                Integer outPut1 = rotorGroup.substituteReverse(reflected1);

                Assert.assertTrue(i == outPut1, i + " != " + outPut0);
            }
            rotorGroup.shift();
        }
    }

    @Test
    public void testRotorGroupRotateSameInput() {
        int[][] rotor0Data26 = EnigmaUtils.getRotor0Data26();
        int[][] rotor1Data26 = EnigmaUtils.getRotor1Data26();
        int[][] rotor2Data26 = EnigmaUtils.getRotor2Data26();
        RotorGroupBuilder rotorBuilder = RotorGroup.builder();
        rotorBuilder.addRotor(Rotor.builder()
                .setSubstitutionTable(rotor0Data26)
                .setIndex(0)
                .build());
        rotorBuilder.addRotor(Rotor.builder()
                .setSubstitutionTable(rotor1Data26)
                .setIndex(0)
                .build());
        rotorBuilder.addRotor(Rotor.builder()
                .setSubstitutionTable(rotor2Data26)
                .setIndex(0)
                .build());
        RotorGroup rotorGroup = rotorBuilder.build();
        Reflector reflector = Reflector.builder().setSubstitutionTable(EnigmaUtils.getReflectorData26()).build();

        int input = 8;
        int encrypted = -1; //encrypted
        int decrypted = -1; //decrypted
        int encryptedOld = -1;

        for (int rotation=0; rotation<10; rotation++) {
            int result0 = rotorGroup.substituteForward(input);
            int reflected0 = reflector.substitute(result0);
            encrypted = rotorGroup.substituteReverse(reflected0);

            int result1 = rotorGroup.substituteForward(encrypted);
            int reflected1 = reflector.substitute(result1);
            decrypted = rotorGroup.substituteReverse(reflected1);

            Assert.assertTrue(input == decrypted, input + " != " + encrypted);

            if (rotation > 0) {
                Assert.assertTrue(encrypted != input);
                Assert.assertTrue(encrypted != encryptedOld);

            }
            encryptedOld = encrypted;
            rotorGroup.shift();
        }
    }

    @Test
    public void testPrettyPrint() {
        String data = "FYLDVAEQHMCZNXTKJPWUOGISBRSXROTNJHUBECYWPDGMAFIZVKQLABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String expectedResult = "FYLDV AEQHM CZNXT KJPWU OGISB RSXRO TNJHU BECYW PDGMA FIZVK QLABC DEFGH \n" +
                "IJKLM NOPQR STUVW XYZAB CDEFG HIJKL MNOPQ RSTUV WXYZA BCDEF GHIJK LMNOP \n" +
                "QRSTU VWXYZ ABCDE FGHIJ KLMNO PQRST UVWXY Z";
        String pretty = EnigmaUtils.prettyPrint(12, data);
        Assert.assertNotNull(pretty);
        Assert.assertEquals(pretty, expectedResult);
    }

    @Test
    public void testPrettyRead() {
        String data = "FYLDV AEQHM CZNXT KJPWU OGISB RSXRO TNJHU BECYW PDGMA FIZVK QLABC DEFGH \n" +
                "IJKLM NOPQR STUVW XYZAB CDEFG HIJKL MNOPQ RSTUV WXYZA BCDEF GHIJK LMNOP \n" +
                "QRSTU VWXYZ ABCDE FGHIJ KLMNO PQRST UVWXY Z";
        String expectedResult = "FYLDVAEQHMCZNXTKJPWUOGISBRSXROTNJHUBECYWPDGMAFIZVKQLABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String pretty = EnigmaUtils.prettyRead(data);
        Assert.assertNotNull(pretty);
        Assert.assertEquals(pretty, expectedResult);
    }

}
