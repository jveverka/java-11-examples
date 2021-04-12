package itx.java.examples.enigma.impl.enigma;

import itx.java.examples.enigma.Enigma;
import itx.java.examples.enigma.alphabet.Alphabet;
import itx.java.examples.enigma.plugboard.PlugBoard;
import itx.java.examples.enigma.rotors.Reflector;
import itx.java.examples.enigma.rotors.RotorGroup;

/**
 * Created by gergej on 17.1.2017.
 */
public class EnigmaImpl implements Enigma {

    private Reflector reflector;
    private RotorGroup rotorGroup;
    private PlugBoard plugBoard;
    private Alphabet alphabet;

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
    public void resetPosition() {
        rotorGroup.resetPosition();
    }

}
