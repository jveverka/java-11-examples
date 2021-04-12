package itx.java.examples.enigma.impl.rotors;

import itx.java.examples.enigma.Utils;
import itx.java.examples.enigma.rotors.Reflector;

/**
 * Created by gergej on 18.1.2017.
 */
public class ReflectorImpl implements Reflector {

    private int[][] substitutionTable;

    public ReflectorImpl(int[][] substitutionTable) {
        this.substitutionTable = substitutionTable;
    }

    @Override
    public int substitute(int character) {
        return Utils.getSubstitutionOf(substitutionTable, character, false);
    }

}
