package itx.java.examples.enigma.impl.rotors;

import itx.java.examples.enigma.rotors.Rotor;
import itx.java.examples.enigma.rotors.RotorGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gergej on 17.1.2017.
 */
public class RotorGroupBuilder {

    private List<Rotor> rotorList;

    public RotorGroupBuilder() {
        rotorList = new ArrayList<>();
    }

    public RotorGroupBuilder addRotor(Rotor rotor) {
        rotorList.add(rotor);
        return this;
    }

    public RotorGroup build() {
        return new RotorGroupImpl(rotorList.toArray(new Rotor[rotorList.size()]));
    }

}
