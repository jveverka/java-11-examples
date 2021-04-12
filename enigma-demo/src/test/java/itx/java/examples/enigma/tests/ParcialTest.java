package itx.java.examples.enigma.tests;

import itx.java.examples.enigma.EnigmaSetup;
import itx.java.examples.enigma.Utils;
import itx.java.examples.enigma.impl.rotors.RotorGroupBuilder;
import itx.java.examples.enigma.rotors.Reflector;
import itx.java.examples.enigma.rotors.Rotor;
import itx.java.examples.enigma.rotors.RotorGroup;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by gergej on 18.1.2017.
 */
public class ParcialTest {

    @Test
    public void testCopyTable() {
        int[][] copy = Utils.copySubstitutionTable(EnigmaSetup.rotor0Data26);
        Assert.assertTrue(copy.length == EnigmaSetup.rotor0Data26.length);
        Assert.assertTrue(copy[0].length == EnigmaSetup.rotor0Data26[0].length);
        for (int i=0; i<copy.length; i++) {
            for (int j=0;j<copy[0].length; j++) {
                Assert.assertTrue(copy[i].length == EnigmaSetup.rotor0Data26[i].length);
                Assert.assertTrue(copy[i][j] == EnigmaSetup.rotor0Data26[i][j]);
            }
        }
    }

    @Test
    public void testSubstitution() {
        int[][] table = new int[3][2];
        table[0][0] = 0; table[0][1] = 2;
        table[1][0] = 1; table[1][1] = 0;
        table[2][0] = 2; table[2][1] = 1;
        int subsFwd = Utils.getSubstitutionOf(table, 0, false);
        int subsRev = Utils.getSubstitutionOf(table, 1, true);
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
        table = Utils.shiftSubstitutionTable(table);
        Assert.assertTrue(table[0][0] == 1);
        Assert.assertTrue(table[0][1] == 0);
        Assert.assertTrue(table[4][0] == 0);
        Assert.assertTrue(table[4][1] == 2);
    }

    @Test
    public void testRotorForward() {
        Rotor rotor = Rotor.builder()
                .setSubstitutionTable(EnigmaSetup.rotor0Data26)
                .setIndex(0)
                .build();
        for (int i=0; i<EnigmaSetup.rotor0Data26.length; i++) {
            int result = rotor.substituteForward(i);
            int subtitute = EnigmaSetup.rotor0Data26[i][1];
            Assert.assertEquals(subtitute, result);
        }
    }

    @Test
    public void testRotor() {
        Rotor rotor = Rotor.builder()
                .setSubstitutionTable(EnigmaSetup.rotor0Data26)
                .setIndex(0)
                .build();
        for (int i = 0; i < EnigmaSetup.rotor0Data26.length; i++) {
            Integer result = rotor.substituteForward(i);
            Integer reverse = rotor.substituteReverse(result);
            Assert.assertEquals(reverse, new Integer(i));
        }
    }

    @Test
    public void testShiftedRotor() {
        Rotor rotor = Rotor.builder()
                .setSubstitutionTable(EnigmaSetup.rotor0Data26)
                .setIndex(5)
                .build();
        for (int i = 0; i < EnigmaSetup.rotor0Data26.length; i++) {
            Integer result = rotor.substituteForward(i);
            Integer reverse = rotor.substituteReverse(result);
            Assert.assertEquals(reverse, new Integer(i));
        }
    }

    @Test
    public void testReflector() {
        Reflector reflector = Reflector.builder()
                .setSubstitutionTable(EnigmaSetup.reflectorData26)
                .build();
        for (int i=0; i<EnigmaSetup.reflectorData26.length; i++) {
            Integer result = reflector.substitute(i);
            Integer subtitute = EnigmaSetup.reflectorData26[i][1];
            Assert.assertEquals(subtitute, result);
        }
    }

    @Test
    public void testRotorGroup() {
        RotorGroupBuilder rotorBuilder = RotorGroup.builder();
        rotorBuilder.addRotor(Rotor.builder()
                .setSubstitutionTable(EnigmaSetup.rotor0Data26)
                .setIndex(0)
                .build());
        rotorBuilder.addRotor(Rotor.builder()
                .setSubstitutionTable(EnigmaSetup.rotor1Data26)
                .setIndex(0)
                .build());
        rotorBuilder.addRotor(Rotor.builder()
                .setSubstitutionTable(EnigmaSetup.rotor2Data26)
                .setIndex(0)
                .build());
        RotorGroup rotorGroup = rotorBuilder.build();
        Reflector reflector = Reflector.builder().setSubstitutionTable(EnigmaSetup.reflectorData26).build();

        for (int i=0; i<EnigmaSetup.rotor0Data26.length; i++) {
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
        RotorGroupBuilder rotorBuilder = RotorGroup.builder();
        rotorBuilder.addRotor(Rotor.builder()
                .setSubstitutionTable(EnigmaSetup.rotor0Data26)
                .setIndex(0)
                .build());
        rotorBuilder.addRotor(Rotor.builder()
                .setSubstitutionTable(EnigmaSetup.rotor1Data26)
                .setIndex(0)
                .build());
        rotorBuilder.addRotor(Rotor.builder()
                .setSubstitutionTable(EnigmaSetup.rotor2Data26)
                .setIndex(0)
                .build());
        RotorGroup rotorGroup = rotorBuilder.build();
        Reflector reflector = Reflector.builder().setSubstitutionTable(EnigmaSetup.reflectorData26).build();

        for (int r=0; r<10; r++) {
            for (int i = 0; i < EnigmaSetup.rotor0Data26.length; i++) {
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
        RotorGroupBuilder rotorBuilder = RotorGroup.builder();
        rotorBuilder.addRotor(Rotor.builder()
                .setSubstitutionTable(EnigmaSetup.rotor0Data26)
                .setIndex(0)
                .build());
        rotorBuilder.addRotor(Rotor.builder()
                .setSubstitutionTable(EnigmaSetup.rotor1Data26)
                .setIndex(0)
                .build());
        rotorBuilder.addRotor(Rotor.builder()
                .setSubstitutionTable(EnigmaSetup.rotor2Data26)
                .setIndex(0)
                .build());
        RotorGroup rotorGroup = rotorBuilder.build();
        Reflector reflector = Reflector.builder().setSubstitutionTable(EnigmaSetup.reflectorData26).build();

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
        String pretty = Utils.prettyPrint(12, data);
        Assert.assertNotNull(pretty);
        Assert.assertEquals(pretty, expectedResult);
    }

    @Test
    public void testPrettyRead() {
        String data = "FYLDV AEQHM CZNXT KJPWU OGISB RSXRO TNJHU BECYW PDGMA FIZVK QLABC DEFGH \n" +
                "IJKLM NOPQR STUVW XYZAB CDEFG HIJKL MNOPQ RSTUV WXYZA BCDEF GHIJK LMNOP \n" +
                "QRSTU VWXYZ ABCDE FGHIJ KLMNO PQRST UVWXY Z";
        String expectedResult = "FYLDVAEQHMCZNXTKJPWUOGISBRSXROTNJHUBECYWPDGMAFIZVKQLABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String pretty = Utils.prettyRead(data);
        Assert.assertNotNull(pretty);
        Assert.assertEquals(pretty, expectedResult);
    }

}
