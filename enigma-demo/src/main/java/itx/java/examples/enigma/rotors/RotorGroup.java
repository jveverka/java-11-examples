package itx.java.examples.enigma.rotors;

import itx.java.examples.enigma.impl.rotors.RotorGroupBuilder;

/**
 * Created by gergej on 17.1.2017.
 */
public interface RotorGroup {

    void resetPosition();

    int substituteForward(int character);

    int substituteReverse(int character);

    void shift();

    static RotorGroupBuilder builder() {
        return new RotorGroupBuilder();
    }

}
