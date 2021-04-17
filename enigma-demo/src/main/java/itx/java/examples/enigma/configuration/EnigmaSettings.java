package itx.java.examples.enigma.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by gergej on 24.1.2017.
 */
public class EnigmaSettings {

    private final List<Integer> rotorOrdinals;
    private final List<Character> rotorStartingPositions;
    private final Character[][] plugBoardSetup;

    @JsonCreator
    public EnigmaSettings(@JsonProperty("rotorOrdinals") List<Integer> rotorOrdinals,
                          @JsonProperty("rotorStartingPositions") List<Character> rotorStartingPositions,
                          @JsonProperty("plugBoardSetup") Character[][] plugBoardSetup) {
        this.rotorOrdinals = rotorOrdinals;
        this.rotorStartingPositions = rotorStartingPositions;
        this.plugBoardSetup = plugBoardSetup;
    }

    public List<Integer> getRotorOrdinals() {
        return rotorOrdinals;
    }

    public List<Character> getRotorStartingPositions() {
        return rotorStartingPositions;
    }

    public Character[][] getPlugBoardSetup() {
        return plugBoardSetup;
    }

}
