package itx.java.examples.enigma.rotors;

import itx.java.examples.enigma.impl.rotors.RotorBuilder;

/**
 * Created by gergej on 17.1.2017.
 */
public interface Rotor {

    void resetPosition();

    int substituteForward(int character);

    int substituteReverse(int character);

    void shift();

    int getSize();

    static RotorBuilder builder() {
        return new RotorBuilder();
    }

}
