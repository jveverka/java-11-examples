package itx.java.examples.enigma.impl.rotors;

import itx.java.examples.enigma.EnigmaUtils;
import itx.java.examples.enigma.rotors.Rotor;

/**
 * Created by gergej on 17.1.2017.
 */
public class RotorImpl implements Rotor {

    private final int[][] substitutionTable;
    private final int startIndex;
    private int[][] shiftedTable;

    public RotorImpl(int[][] substitutionTable, int startIndex) {
        this.substitutionTable = substitutionTable;
        this.shiftedTable = EnigmaUtils.shiftSubstitutionTable(EnigmaUtils.copySubstitutionTable(substitutionTable), startIndex);
        this.startIndex = startIndex;
    }

    @Override
    public void resetPosition() {
        EnigmaUtils.shiftSubstitutionTable(EnigmaUtils.copySubstitutionTable(substitutionTable), startIndex);
    }

    @Override
    public int substituteForward(int character) {
        return EnigmaUtils.getSubstitutionOf(shiftedTable, character, false);
    }

    @Override
    public int substituteReverse(int character) {
        return EnigmaUtils.getSubstitutionOf(shiftedTable, character, true);
    }

    @Override
    public void shift() {
        this.shiftedTable = EnigmaUtils.shiftSubstitutionTable(shiftedTable);
    }

    @Override
    public int getSize() {
        return substitutionTable.length;
    }

}
