package itx.java.examples.enigma.tests;

import itx.java.examples.enigma.Enigma;
import itx.java.examples.enigma.Utils;
import itx.java.examples.enigma.configuration.EnigmaConfiguration;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.apache.commons.io.IOUtils;


import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;

/**
 * Created by gergej on 17.1.2017.
 *
 * https://en.wikipedia.org/wiki/Enigma_machine
 *
 */
public class EnigmaTest {

    @DataProvider(name = "messages26")
    public static Object[][] getMassages26() {
        return new Object[][]{
                {"SECRETMESSAGE"},
                {"HELLO"},
                {"ABCDEFGHIJKLMNOPQRSTUVWXYZ"},
                {"ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ"},
        };
    }

    @Test(dataProvider = "messages26")
    public void testSimpleMessageAlphabet26(String originalMessage) {
        int[] initialRotorPositions = new int[3];
        initialRotorPositions[0] = 9;
        initialRotorPositions[1] = 19;
        initialRotorPositions[2] = 23;
        Enigma enigmaForEncryption = Enigma.builder().createEnigma26(initialRotorPositions).build();
        Enigma enigmaForDecryption = Enigma.builder().createEnigma26(initialRotorPositions).build();
        String encryptedMessage = enigmaForEncryption.encryptOrDecrypt(originalMessage);
        String decryptedMessage = enigmaForDecryption.encryptOrDecrypt(encryptedMessage);
        Assert.assertNotNull(encryptedMessage);
        Assert.assertTrue(originalMessage.length() == encryptedMessage.length());
        Assert.assertNotNull(decryptedMessage);
        Assert.assertTrue(encryptedMessage.length() == decryptedMessage.length());
        Assert.assertEquals(originalMessage, decryptedMessage);
    }

    @DataProvider(name = "messagesBase64")
    public static Object[][] getMassagesBase64() {
        return new Object[][]{
                {"dataFiles/dataFilePlain01.txt"},
        };
    }

    @Test(dataProvider = "messagesBase64")
    public void testSimpleMessageAlphabetBase64(String inputStreamClassPath) throws IOException {
        int[] initialRotorPositions = new int[3];
        initialRotorPositions[0] = 18;
        initialRotorPositions[1] = 12;
        initialRotorPositions[2] = 21;
        Enigma enigmaForEncryption = Enigma.builder().createEnigmaBase64(initialRotorPositions).build();
        Enigma enigmaForDecryption = Enigma.builder().createEnigmaBase64(initialRotorPositions).build();
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(inputStreamClassPath);
        Assert.assertNotNull(is);
        StringWriter writer = new StringWriter();
        IOUtils.copy(is, writer, Charset.forName("UTF-8"));
        String originalMessage = writer.toString();
        String encryptedMessage = enigmaForEncryption.encryptGenericString(originalMessage);
        String decryptedMessage = enigmaForDecryption.decryptGenericString(encryptedMessage);
        Assert.assertNotNull(encryptedMessage);
        Assert.assertNotNull(decryptedMessage);
        String prettyPrint = Utils.prettyPrint(12, encryptedMessage);
        Assert.assertNotNull(prettyPrint);
        Assert.assertEquals(originalMessage, decryptedMessage);
    }

    @DataProvider(name = "configs")
    public static Object[][] getConfigs() {
        return new Object[][]{
                { "configurations/enigma-test-configuration-26-01.json", "THESECRETMESSAGE", Boolean.FALSE },
                { "configurations/enigma-test-configuration-26-02.json", "TOPSECRETMESSAGE", Boolean.FALSE },
                { "configurations/enigma-test-configuration-base64-01.json", "The Secret Message", Boolean.TRUE }
        };
    }

    @Test(dataProvider = "configs")
    public void testEnigmaConfiguration(String configPath, String originalMessage, Boolean useBase64) throws IOException {
        InputStream is = UtilsTest.class.getClassLoader().getResourceAsStream(configPath);
        EnigmaConfiguration enigmaConfiguration = Utils.readEnigmaConfiguration(is);
        Enigma enigmaForEncryption = Enigma.builder().fromConfiguration(enigmaConfiguration).build();
        Enigma enigmaForDecryption = Enigma.builder().fromConfiguration(enigmaConfiguration).build();
        String encryptedMessage = null;
        String decryptedMessage = null;
        if (useBase64) {
            encryptedMessage = enigmaForEncryption.encryptGenericString(originalMessage);
            decryptedMessage = enigmaForDecryption.decryptGenericString(encryptedMessage);
        } else {
            encryptedMessage = enigmaForEncryption.encryptOrDecrypt(originalMessage);
            decryptedMessage = enigmaForDecryption.encryptOrDecrypt(encryptedMessage);
        }
        Assert.assertNotNull(encryptedMessage);
        Assert.assertNotNull(decryptedMessage);
        Assert.assertEquals(originalMessage, decryptedMessage);
    }

}