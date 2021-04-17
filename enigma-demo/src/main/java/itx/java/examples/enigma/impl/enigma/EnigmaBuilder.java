package itx.java.examples.enigma.impl.enigma;

import itx.java.examples.enigma.Enigma;
import itx.java.examples.enigma.EnigmaSetup;
import itx.java.examples.enigma.Utils;
import itx.java.examples.enigma.alphabet.Alphabet;
import itx.java.examples.enigma.configuration.EnigmaConfiguration;
import itx.java.examples.enigma.impl.rotors.RotorGroupBuilder;
import itx.java.examples.enigma.plugboard.PlugBoard;
import itx.java.examples.enigma.rotors.Reflector;
import itx.java.examples.enigma.rotors.Rotor;
import itx.java.examples.enigma.rotors.RotorGroup;

/**
 * Created by gergej on 17.1.2017.
 */
public class EnigmaBuilder {

    private Alphabet alphabet;
    private Reflector reflector;
    private RotorGroup rotorGroup;
    private PlugBoard plugBoard;

    public EnigmaBuilder setAlphabet(Alphabet alphabet) {
        this.alphabet = alphabet;
        return this;
    }

    public EnigmaBuilder setReflector(Reflector reflector) {
        this.reflector = reflector;
        return this;
    }

    public EnigmaBuilder setRotorGroup(RotorGroup rotorGroup) {
        this.rotorGroup = rotorGroup;
        return this;
    }

    public EnigmaBuilder setPlugBoard(PlugBoard plugBoard) {
        this.plugBoard = plugBoard;
        return this;
    }

    public EnigmaBuilder fromConfiguration(EnigmaConfiguration enigmaConfiguration) {
        this.alphabet = Alphabet.builder()
                .setAlphabet(enigmaConfiguration.getAplhabet())
                .build();
        this.reflector = Reflector.builder()
                .setSubstitutionTable(Utils.createReflectorSubstitutionMap(alphabet))
                .build();
        RotorGroupBuilder rotorGroupBuilder = RotorGroup.builder();
        for (int i=0; i<enigmaConfiguration.getEnigmaSetup().getRotorOrdinals().size(); i++) {
            Integer rotorOrdinal = enigmaConfiguration.getEnigmaSetup().getRotorOrdinals().get(i);
            String rotorParameters = enigmaConfiguration.getRotorParameters().get(rotorOrdinal);
            Rotor rotor = Rotor.builder()
                    .setIndex(alphabet.getIndex(enigmaConfiguration.getEnigmaSetup().getRotorStartingPositions().get(i)))
                    .setSubstitutionTable(Utils.createSubstitutionMap(alphabet, rotorParameters))
                    .build();
            rotorGroupBuilder.addRotor(rotor);
        }
        this.rotorGroup = rotorGroupBuilder.build();
        this.plugBoard = PlugBoard.builder()
                .setSetup(enigmaConfiguration.getEnigmaSetup().getPlugBoardSetup())
                .build();
        return this;
    }

    public EnigmaBuilder createEnigma26(int[] initialRotorPositions) {
        this.alphabet = Alphabet.buildAlphabet26();
        Character[][] plugBoardSetup = Utils.generateRandomPlugBoadrSetup(alphabet);
        Rotor rotor0 = Rotor.builder()
                .setIndex(initialRotorPositions[0])
                .setSubstitutionTable(EnigmaSetup.getRotor0Data26())
                .build();
        Rotor rotor1 = Rotor.builder()
                .setIndex(initialRotorPositions[1])
                .setSubstitutionTable(EnigmaSetup.getRotor1Data26())
                .build();
        Rotor rotor2 = Rotor.builder()
                .setIndex(initialRotorPositions[2])
                .setSubstitutionTable(EnigmaSetup.getRotor2Data26())
                .build();
        this.plugBoard = PlugBoard.builder()
                .setSetup(plugBoardSetup)
                .build();
        Rotor[] rotors = new Rotor[3];
        rotors[0] = rotor0;
        rotors[1] = rotor1;
        rotors[2] = rotor2;
        this.rotorGroup = RotorGroup.builder()
                .addRotor(rotor0)
                .addRotor(rotor1)
                .addRotor(rotor2)
                .build();
        this.reflector = Reflector.builder()
                .setSubstitutionTable(EnigmaSetup.getReflectorData26())
                .build();
        return this;
    }

    public EnigmaBuilder createEnigmaBase64(int[] initialRotorPositions) {
        this.alphabet = Alphabet.buildAlphabetBase64();
        Character[][] plugBoardSetup = Utils.generateRandomPlugBoadrSetup(alphabet);
        Rotor rotor0 = Rotor.builder()
                .setIndex(initialRotorPositions[0])
                .setSubstitutionTable(EnigmaSetup.getRotor0DataBase64())
                .build();
        Rotor rotor1 = Rotor.builder()
                .setIndex(initialRotorPositions[1])
                .setSubstitutionTable(EnigmaSetup.getRotor1DataBase64())
                .build();
        Rotor rotor2 = Rotor.builder()
                .setIndex(initialRotorPositions[2])
                .setSubstitutionTable(EnigmaSetup.getRotor2DataBase64())
                .build();
        this.plugBoard = PlugBoard.builder()
                .setSetup(plugBoardSetup)
                .build();
        Rotor[] rotors = new Rotor[3];
        rotors[0] = rotor0;
        rotors[1] = rotor1;
        rotors[2] = rotor2;
        this.rotorGroup = RotorGroup.builder()
                .addRotor(rotor0)
                .addRotor(rotor1)
                .addRotor(rotor2)
                .build();
        this.reflector = Reflector.builder()
                .setSubstitutionTable(EnigmaSetup.getReflectorDataBase64())
                .build();
        return this;
    }

    public Enigma build() {
        return new EnigmaImpl(alphabet, reflector, rotorGroup, plugBoard);
    }

}
