package itx.java.examples.enigma.impl.plugboard;

import itx.java.examples.enigma.plugboard.PlugBoard;

/**
 * Created by gergej on 17.1.2017.
 */
public class PlugBoardImpl implements PlugBoard {

    private final Character[][] setup;

    public PlugBoardImpl(Character[][] setup) {
        this.setup = setup;
    }

    @Override
    public Character swapBefore(Character character) {
        return swap(character);
    }

    @Override
    public Character swapAfter(Character character) {
        return swap(character);
    }

    private Character swap(Character character) {
        for (int i=0; i<setup.length; i++) {
            if (character.equals(setup[i][0])) {
                return setup[i][1];
            }
            if (character.equals(setup[i][1])) {
                return setup[i][0];
            }
        }
        return character;
    }

}
