package itx.java.examples.enigma.impl.enigma;

import itx.java.examples.enigma.Enigma;
import itx.java.examples.enigma.alphabet.Alphabet;
import itx.java.examples.enigma.plugboard.PlugBoard;
import itx.java.examples.enigma.rotors.Reflector;
import itx.java.examples.enigma.rotors.RotorGroup;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Created by gergej on 17.1.2017.
 */
public class EnigmaImpl implements Enigma {

    private final Reflector reflector;
    private final RotorGroup rotorGroup;
    private final PlugBoard plugBoard;
    private final Alphabet alphabet;

    public EnigmaImpl(Alphabet alphabet, Reflector reflector, RotorGroup rotorGroup, PlugBoard plugBoard) {
        this.alphabet = alphabet;
        this.reflector = reflector;
        this.rotorGroup = rotorGroup;
        this.plugBoard = plugBoard;
    }

    @Override
    public Character encryptOrDecrypt(Character character) {
        Character result = plugBoard.swapBefore(character);
        int index = alphabet.getIndex(result);
        index = rotorGroup.substituteForward(index);
        index = reflector.substitute(index);
        index = rotorGroup.substituteReverse(index);
        result = alphabet.getCharacter(index);
        result = plugBoard.swapAfter(result);
        rotorGroup.shift();
        return result;
    }

    @Override
    public String encryptOrDecrypt(String message) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            sb.append(encryptOrDecrypt(message.charAt(i)));
        }
        return sb.toString();
    }

    @Override
    public String encryptGenericString(String input) {
        byte[] inputBytes = Base64.getEncoder().encode(input.getBytes(StandardCharsets.UTF_8));
        String inputData = new String(inputBytes, StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<inputData.length(); i++) {
            sb.append(encryptOrDecrypt(inputData.charAt(i)));
        }
        return sb.toString();
    }

    @Override
    public String decryptGenericString(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<input.length(); i++) {
            sb.append(encryptOrDecrypt(input.charAt(i)));
        }
        byte[] inputBytes = Base64.getDecoder().decode(sb.toString());
        return new String(inputBytes, StandardCharsets.UTF_8);
    }

    @Override
    public void resetPosition() {
        rotorGroup.resetPosition();
    }

    @Override
    public Enigma copy() {
        return new EnigmaImpl(alphabet, reflector, rotorGroup, plugBoard);
    }

}
