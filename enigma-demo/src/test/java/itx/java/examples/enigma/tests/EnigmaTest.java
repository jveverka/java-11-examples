package itx.java.examples.enigma.tests;

import itx.java.examples.enigma.Enigma;
import itx.java.examples.enigma.EnigmaUtils;
import itx.java.examples.enigma.configuration.EnigmaConfiguration;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

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
        Enigma enigmaForEncryption = Enigma.builder().createEnigmaAlphabet26Rotors3(9, 19, 23)
                .build();
        Enigma enigmaForDecryption = Enigma.builder().createEnigmaAlphabet26Rotors3(9, 19, 23)
                .build();
        String encryptedMessage = enigmaForEncryption.encryptOrDecrypt(originalMessage);
        String decryptedMessage = enigmaForDecryption.encryptOrDecrypt(encryptedMessage);
        Assert.assertNotNull(encryptedMessage);
        Assert.assertEquals(originalMessage.length(), encryptedMessage.length());
        Assert.assertNotNull(decryptedMessage);
        Assert.assertEquals(encryptedMessage.length(), decryptedMessage.length());

        Assert.assertEquals(originalMessage, decryptedMessage);
        Assert.assertNotEquals(originalMessage, encryptedMessage);
        Assert.assertNotEquals(decryptedMessage, encryptedMessage);
    }

    @DataProvider(name = "messagesBase64")
    public static Object[][] getMassagesBase64() {
        return new Object[][]{
                {"dataFiles/dataFilePlain01.txt"},
        };
    }

    @Test(dataProvider = "messagesBase64")
    public void testSimpleMessageAlphabetBase64(String inputStreamClassPath) throws IOException {
        Enigma enigmaForEncryption = Enigma.builder()
                .createEnigmaAlphabetBase64Rotors3(18, 12, 21)
                .build();
        Enigma enigmaForDecryption = Enigma.builder()
                .createEnigmaAlphabetBase64Rotors3(18, 12, 21)
                .build();
        InputStream inputStream = EnigmaTest.class.getClassLoader().getResourceAsStream(inputStreamClassPath);
        Assert.assertNotNull(inputStream);
        String originalMessage = new BufferedReader(new InputStreamReader(inputStream))
                .lines().parallel().collect(Collectors.joining("\n"));
        String encryptedMessage = enigmaForEncryption.encryptGenericString(originalMessage);
        String decryptedMessage = enigmaForDecryption.decryptGenericString(encryptedMessage);
        Assert.assertNotNull(encryptedMessage);
        Assert.assertNotNull(decryptedMessage);
        String prettyPrint = EnigmaUtils.prettyPrint(12, encryptedMessage);
        Assert.assertNotNull(prettyPrint);

        Assert.assertEquals(originalMessage, decryptedMessage);
        Assert.assertNotEquals(originalMessage, encryptedMessage);
        Assert.assertNotEquals(decryptedMessage, encryptedMessage);
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
        EnigmaConfiguration enigmaConfiguration = EnigmaUtils.readEnigmaConfiguration(is);
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
        Assert.assertNotEquals(originalMessage, encryptedMessage);
        Assert.assertNotEquals(decryptedMessage, encryptedMessage);
    }

}