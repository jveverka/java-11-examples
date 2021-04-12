package itx.java.examples.enigma.impl.alphabet;

import itx.java.examples.enigma.alphabet.Alphabet;

/**
 * Created by gergej on 18.1.2017.
 */
public class AlphabetBuilder {

    private String alphabet;

    public AlphabetBuilder setAlphabet(String alphabet) {
        this.alphabet = alphabet;
        return this;
    }

    public Alphabet build() {
        return new AlphabetImpl(alphabet);
    }

}
