package itx.java.examples.enigma.impl.rotors;

import itx.java.examples.enigma.Utils;
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
        this.shiftedTable = Utils.shiftSubstitutionTable(Utils.copySubstitutionTable(substitutionTable), startIndex);
        this.startIndex = startIndex;
    }

    @Override
    public void resetPosition() {
        Utils.shiftSubstitutionTable(Utils.copySubstitutionTable(substitutionTable), startIndex);
    }

    @Override
    public int substituteForward(int character) {
        return Utils.getSubstitutionOf(shiftedTable, character, false);
    }

    @Override
    public int substituteReverse(int character) {
        return Utils.getSubstitutionOf(shiftedTable, character, true);
    }

    @Override
    public void shift() {
        this.shiftedTable = Utils.shiftSubstitutionTable(shiftedTable);
    }

    @Override
    public int getSize() {
        return substitutionTable.length;
    }

}
