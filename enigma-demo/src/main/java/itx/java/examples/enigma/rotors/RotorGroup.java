package itx.java.examples.enigma.rotors;

import itx.java.examples.enigma.impl.rotors.RotorGroupBuilder;

/**
 * Created by gergej on 17.1.2017.
 */
public interface RotorGroup {

    public void resetPosition();

    public int substituteForward(int character);

    public int substituteReverse(int character);

    public void shift();

    public static RotorGroupBuilder builder() {
        return new RotorGroupBuilder();
    }

}
