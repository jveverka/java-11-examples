package itx.java.examples.enigma.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by gergej on 24.1.2017.
 */
public class EnigmaConfiguration {

    private final String alphabet;
    private final List<String> rotorParameters;
    private final EnigmaSettings enigmaSetup;

    @JsonCreator
    public EnigmaConfiguration(@JsonProperty("alphabet") String alphabet,
                               @JsonProperty("rotorParameters") List<String> rotorParameters,
                               @JsonProperty("enigmaSetup") EnigmaSettings enigmaSetup) {
        this.alphabet = alphabet;
        this.rotorParameters = rotorParameters;
        this.enigmaSetup = enigmaSetup;
    }

    public String getAlphabet() {
        return alphabet;
    }

    public List<String> getRotorParameters() {
        return rotorParameters;
    }

    public EnigmaSettings getEnigmaSetup() {
        return enigmaSetup;
    }

}
