package itx.java.examples.enigma.impl.rotors;

import itx.java.examples.enigma.rotors.Rotor;

/**
 * Created by gergej on 17.1.2017.
 */
public class RotorBuilder {

    private int[][] substitutionTable;
    private int index;

    public RotorBuilder setSubstitutionTable(int[][] substitutionTable) {
        this.substitutionTable = substitutionTable;
        return this;
    }

    public RotorBuilder setIndex(int index) {
        this.index = index;
        return this;
    }

    public Rotor build() {
        return new RotorImpl(substitutionTable, index);
    }

}
