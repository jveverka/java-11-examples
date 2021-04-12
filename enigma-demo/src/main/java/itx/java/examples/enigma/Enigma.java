package itx.java.examples.enigma;

import itx.java.examples.enigma.impl.enigma.EnigmaBuilder;

/**
 * Created by gergej on 17.1.2017.
 */
public interface Enigma {

    Character encryptOrDecrypt(Character character);

    void resetPosition();

    static EnigmaBuilder builder() {
        return new EnigmaBuilder();
    }

}
