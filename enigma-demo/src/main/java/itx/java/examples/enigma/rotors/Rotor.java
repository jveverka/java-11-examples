package itx.java.examples.enigma.rotors;

import itx.java.examples.enigma.impl.rotors.RotorBuilder;

/**
 * Created by gergej on 17.1.2017.
 */
public interface Rotor {

    public void resetPosition();

    public int substituteForward(int character);

    public int substituteReverse(int character);

    public void shift();

    public int getSize();

    public static RotorBuilder builder() {
        return new RotorBuilder();
    }

}
