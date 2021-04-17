package itx.java.examples.enigma.tests;

import itx.java.examples.enigma.Enigma;
import itx.java.examples.enigma.EnigmaUtils;
import itx.java.examples.enigma.configuration.EnigmaConfiguration;
import itx.java.examples.enigma.thebomb.RotorGroupIterator;
import itx.java.examples.enigma.thebomb.TheBomb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by gergej on 24.1.2017.
 */
public class BruteForceBreakTest {

    final private static Logger LOG = LoggerFactory.getLogger(BruteForceBreakTest.class);

    @Test(enabled = false)
    public void findEnigmaSetup() throws IOException {
        String enigmaConfigPath = "configurations/enigma-test-configuration-26-02.json";
        String originalMessage = "THETOPSECRETMESSAGEITTHIS";
        String messagePart = "THETOP";

        LOG.info("encrypting message ...");
        InputStream is = UtilsTest.class.getClassLoader().getResourceAsStream(enigmaConfigPath);
        EnigmaConfiguration enigmaConfiguration = EnigmaUtils.readEnigmaConfiguration(is);
        Enigma enigmaForEncryption = Enigma.builder().fromConfiguration(enigmaConfiguration).build();
        String encryptedMessage = enigmaForEncryption.encryptOrDecrypt(originalMessage);

        LOG.info("starting the bomb ...");
        EnigmaConfiguration config = new EnigmaConfiguration(enigmaConfiguration.getAlphabet(), enigmaConfiguration.getRotorParameters(), null);
        TheBomb theBomb = new TheBomb(config, encryptedMessage, messagePart, 3, enigmaConfiguration.getEnigmaSetup().getPlugBoardSetup());
        EnigmaConfiguration calculatedConfiguration = null;

        try {
            calculatedConfiguration = theBomb.findEnigmaSetup();
            LOG.error("ERROR: setup not found !");
        } catch (Exception e) {
            LOG.error("Exception: ", e);
            Assert.fail();
        }

        LOG.info("checking enigma settings ...");
        Enigma enigmaForDecryption = Enigma.builder().fromConfiguration(calculatedConfiguration).build();
        String decryptedMessage = enigmaForDecryption.encryptOrDecrypt(encryptedMessage);

        LOG.info("evaluating the results ...");
        Assert.assertNotNull(encryptedMessage);
        Assert.assertNotNull(decryptedMessage);
        Assert.assertEquals(originalMessage, decryptedMessage);
        Assert.assertNotEquals(originalMessage, encryptedMessage);
        Assert.assertNotEquals(decryptedMessage, encryptedMessage);
    }

    @Test
    public void testRotorGroupIterator() {
        RotorGroupIterator rotorGroupIterator = new RotorGroupIterator(3, "abc");
        Assert.assertTrue(rotorGroupIterator.hasNext());
        Assert.assertTrue(compareContent(rotorGroupIterator.getNext(), new Character[]{ 'a', 'a', 'a' }));
        Assert.assertTrue(rotorGroupIterator.hasNext());
        Assert.assertTrue(compareContent(rotorGroupIterator.getNext(), new Character[]{ 'a', 'a', 'b' }));
        Assert.assertTrue(rotorGroupIterator.hasNext());
        Assert.assertTrue(compareContent(rotorGroupIterator.getNext(), new Character[]{ 'a', 'a', 'c' }));
        Assert.assertTrue(rotorGroupIterator.hasNext());
        Assert.assertTrue(compareContent(rotorGroupIterator.getNext(), new Character[]{ 'a', 'b', 'a' }));
        Assert.assertTrue(rotorGroupIterator.hasNext());
        Assert.assertTrue(compareContent(rotorGroupIterator.getNext(), new Character[]{ 'a', 'b', 'b' }));
        Assert.assertTrue(rotorGroupIterator.hasNext());
        Assert.assertTrue(compareContent(rotorGroupIterator.getNext(), new Character[]{ 'a', 'b', 'c' }));
        Assert.assertTrue(rotorGroupIterator.hasNext());
        Assert.assertTrue(compareContent(rotorGroupIterator.getNext(), new Character[]{ 'a', 'c', 'a' }));
    }

    private boolean compareContent(List<Character> chars, Character[] data) {
        if (chars.size() != data.length) return false;
        for (int i=0; i<data.length; i++) {
            if (data[i] != chars.get(i)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws IOException {
        BruteForceBreakTest bruteForceBreakTest = new BruteForceBreakTest();
        bruteForceBreakTest.findEnigmaSetup();
    }

}
