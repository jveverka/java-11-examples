package itx.java.examples.enigma.plugboard;

import itx.java.examples.enigma.impl.plugboard.PlugBoardBuilder;

/**
 * Created by gergej on 17.1.2017.
 */
public interface PlugBoard {

    public Character swapBefore(Character character);

    public Character swapAfter(Character character);

    public static PlugBoardBuilder builder() {
        return new PlugBoardBuilder();
    }

}
