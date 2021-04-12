package itx.java.examples.enigma.impl.rotors;

import itx.java.examples.enigma.rotors.Reflector;

/**
 * Created by gergej on 18.1.2017.
 */
public class ReflectorBuilder {

    private int[][] substitutionTable;

    public ReflectorBuilder setSubstitutionTable(int[][] substitutionTable) {
        this.substitutionTable = substitutionTable;
        return this;
    }

    public Reflector build() {
        return new ReflectorImpl(substitutionTable);
    }

}
