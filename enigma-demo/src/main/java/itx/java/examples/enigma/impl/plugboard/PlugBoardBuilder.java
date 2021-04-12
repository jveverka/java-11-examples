package itx.java.examples.enigma.impl.plugboard;

import itx.java.examples.enigma.plugboard.PlugBoard;

/**
 * Created by gergej on 17.1.2017.
 */
public class PlugBoardBuilder {

    private Character[][] setup;

    public PlugBoardBuilder setSetup(Character[][] setup) {
        this.setup = setup;
        return this;
    }

    public PlugBoard build() {
        return new PlugBoardImpl(setup);
    }

}
