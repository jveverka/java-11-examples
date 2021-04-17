package itx.java.examples.enigma.impl.rotors;

import itx.java.examples.enigma.rotors.Rotor;
import itx.java.examples.enigma.rotors.RotorGroup;

/**
 * Created by gergej on 17.1.2017.
 */
public class RotorGroupImpl implements RotorGroup {

    private final Rotor[] rotors;
    private final int[] positions;
    private final int size;

    public RotorGroupImpl(Rotor[] rotors) {
        this.rotors = rotors;
        this.positions = new int[rotors.length];
        this.size = rotors[0].getSize();
    }

    @Override
    public void resetPosition() {
        for (int i=0; i<rotors.length; i++) {
            rotors[i].resetPosition();
            positions[i] = 0;
        }
    }

    @Override
    public int substituteForward(int character) {
        int result = character;
        for (int i=0; i<rotors.length; i++) {
            result = rotors[i].substituteForward(result);
        }
        return result;
    }

    @Override
    public int substituteReverse(int character) {
        int result = character;
        for (int i=(rotors.length-1); i>=0; i--) {
            result = rotors[i].substituteReverse(result);
        }
        return result;
    }

    @Override
    public void shift() {
        shift(rotors.length - 1);
    }

    private void shift(int rotorIndex) {
        if (rotorIndex < 0) return;
        rotors[rotorIndex].shift();
        positions[rotorIndex]++;
        if (positions[rotorIndex] >= size) {
            positions[rotorIndex] = 0;
            shift(rotorIndex -1);
        }
    }

}
