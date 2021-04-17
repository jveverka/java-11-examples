package itx.java.examples.enigma.plugboard;

import itx.java.examples.enigma.impl.plugboard.PlugBoardBuilder;

/**
 * Created by gergej on 17.1.2017.
 */
public interface PlugBoard {

    Character swapBefore(Character character);

    Character swapAfter(Character character);

    static PlugBoardBuilder builder() {
        return new PlugBoardBuilder();
    }

}
