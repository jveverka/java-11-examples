package itx.java.examples.enigma.configuration;

import java.util.List;

/**
 * Created by gergej on 24.1.2017.
 */
public class EnigmaSettings {

    private List<Integer> rotorOrdinals;
    private List<Character> rotorStartingPositions;
    private Character[][] plugBoardSetup;

    public EnigmaSettings() {
    }

    public EnigmaSettings(List<Integer> rotorOrdinals,
                          List<Character> rotorStartingPositions,
                          Character[][] plugBoardSetup) {
        this.rotorOrdinals = rotorOrdinals;
        this.rotorStartingPositions = rotorStartingPositions;
        this.plugBoardSetup = plugBoardSetup;
    }

    public List<Integer> getRotorOrdinals() {
        return rotorOrdinals;
    }

    public void setRotorOrdinals(List<Integer> rotorOrdinals) {
        this.rotorOrdinals = rotorOrdinals;
    }

    public List<Character> getRotorStartingPositions() {
        return rotorStartingPositions;
    }

    public void setRotorStartingPositions(List<Character> rotorStartingPositions) {
        this.rotorStartingPositions = rotorStartingPositions;
    }

    public Character[][] getPlugBoardSetup() {
        return plugBoardSetup;
    }

    public void setPlugBoardSetup(Character[][] plugBoardSetup) {
        this.plugBoardSetup = plugBoardSetup;
    }

}
