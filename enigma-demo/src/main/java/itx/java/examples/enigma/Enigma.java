package itx.java.examples.enigma;

import itx.java.examples.enigma.impl.enigma.EnigmaBuilder;

/**
 * Created by gergej on 17.1.2017.
 * Software implementation of mechanical Enigma cipher device.
 * https://en.wikipedia.org/wiki/Enigma_machine
 */
public interface Enigma {

    /**
     * Encrypt or decrypt single character.
     * Rotor positions are shifted accordingly after calling this method.
     * This is equivalent to single keystroke on mechanical Enigma machine.
     * @param character input character to encrypt.
     * @return output encrypted character.
     */
    Character encryptOrDecrypt(Character character);

    /**
     * Encrypt or decrypt string message.
     * Rotor positions are shifted accordingly after calling this method.
     * @param message input message to encrypt.
     * @return output encrypted message.
     */
    String encryptOrDecrypt(String message);

    /**
     * Decrypt data in base64 format.
     * @param input encrypted data in base64 format.
     * @return ordinary unicode java string.
     */
    String decodeBase64String(String input);

    /**
     * Decrypt ordinary unicode java string.
     * @param input ordinary unicode java string.
     * @return encrypted string in base64 format.
     */
    String encryptUnicodeString(String input);

    /**
     * Reset rotor positions to initial state.
     */
    void resetPosition();

    /**
     * Get enigma builder.
     * @return
     */
    static EnigmaBuilder builder() {
        return new EnigmaBuilder();
    }

}
