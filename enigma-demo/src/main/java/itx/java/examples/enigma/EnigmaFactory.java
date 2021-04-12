package itx.java.examples.enigma;

import itx.java.examples.enigma.alphabet.Alphabet;
import itx.java.examples.enigma.plugboard.PlugBoard;
import itx.java.examples.enigma.rotors.Reflector;
import itx.java.examples.enigma.rotors.Rotor;
import itx.java.examples.enigma.rotors.RotorGroup;

/**
 * Created by gergej on 17.1.2017.
 */
public final class EnigmaFactory {

    private EnigmaFactory() {
        throw new UnsupportedOperationException();
    }

    public static Enigma createEnigma26(int[] initialRotorPositions, Character[][] plugBoardSetup) {
        Rotor rotor0 = Rotor.builder()
                .setIndex(initialRotorPositions[0])
                .setSubstitutionTable(EnigmaSetup.rotor0Data26)
                .build();
        Rotor rotor1 = Rotor.builder()
                .setIndex(initialRotorPositions[1])
                .setSubstitutionTable(EnigmaSetup.rotor1Data26)
                .build();
        Rotor rotor2 = Rotor.builder()
                .setIndex(initialRotorPositions[2])
                .setSubstitutionTable(EnigmaSetup.rotor2Data26)
                .build();
        PlugBoard plugBoard = PlugBoard.builder()
                .setSetup(plugBoardSetup)
                .build();
        Rotor[] rotors = new Rotor[3];
        rotors[0] = rotor0;
        rotors[1] = rotor1;
        rotors[2] = rotor2;
        RotorGroup rotorGroup = RotorGroup.builder()
                .addRotor(rotor0)
                .addRotor(rotor1)
                .addRotor(rotor2)
                .build();
        Reflector reflector = Reflector.builder()
                .setSubstitutionTable(EnigmaSetup.reflectorData26)
                .build();
        return Enigma.builder()
                .setAlphabet(Alphabet.buildAlphabet26())
                .setReflector(reflector)
                .setPlugBoard(plugBoard)
                .setRotorGroup(rotorGroup)
                .build();
    }

    public static Enigma createEnigmaBase64(int[] initialRotorPositions, Character[][] plugBoardSetup) {
        Rotor rotor0 = Rotor.builder()
                .setIndex(initialRotorPositions[0])
                .setSubstitutionTable(EnigmaSetup.rotor0DataBase64)
                .build();
        Rotor rotor1 = Rotor.builder()
                .setIndex(initialRotorPositions[1])
                .setSubstitutionTable(EnigmaSetup.rotor1DataBase64)
                .build();
        Rotor rotor2 = Rotor.builder()
                .setIndex(initialRotorPositions[2])
                .setSubstitutionTable(EnigmaSetup.rotor2DataBase64)
                .build();
        PlugBoard plugBoard = PlugBoard.builder()
                .setSetup(plugBoardSetup)
                .build();
        Rotor[] rotors = new Rotor[3];
        rotors[0] = rotor0;
        rotors[1] = rotor1;
        rotors[2] = rotor2;
        RotorGroup rotorGroup = RotorGroup.builder()
                .addRotor(rotor0)
                .addRotor(rotor1)
                .addRotor(rotor2)
                .build();
        Reflector reflector = Reflector.builder()
                .setSubstitutionTable(EnigmaSetup.reflectorDataBase64)
                .build();
        return Enigma.builder()
                .setAlphabet(Alphabet.buildAlphabetBase64())
                .setReflector(reflector)
                .setPlugBoard(plugBoard)
                .setRotorGroup(rotorGroup)
                .build();
    }

}
