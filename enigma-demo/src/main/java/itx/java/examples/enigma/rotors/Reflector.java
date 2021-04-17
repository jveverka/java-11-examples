package itx.java.examples.enigma.rotors;

import itx.java.examples.enigma.impl.rotors.ReflectorBuilder;

/**
 * Created by gergej on 18.1.2017.
 */
public interface Reflector {

    int substitute(int character);

    static ReflectorBuilder builder() {
        return new ReflectorBuilder();
    }

}
