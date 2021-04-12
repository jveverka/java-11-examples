package itx.java.examples.enigma.configuration;

import java.util.List;

/**
 * Created by gergej on 24.1.2017.
 */
public class EnigmaConfiguration {

    private String aplhabet;
    private List<String> rotorParameters;
    private EnigmaSettings enigmaSetup;

    public EnigmaConfiguration() {
    }

    public EnigmaConfiguration(String aplhabet, List<String> rotorParameters, EnigmaSettings enigmaSetup) {
        this.aplhabet = aplhabet;
        this.rotorParameters = rotorParameters;
        this.enigmaSetup = enigmaSetup;
    }

    public String getAplhabet() {
        return aplhabet;
    }

    public void setAplhabet(String aplhabet) {
        this.aplhabet = aplhabet;
    }

    public List<String> getRotorParameters() {
        return rotorParameters;
    }

    public void setRotorParameters(List<String> rotorParameters) {
        this.rotorParameters = rotorParameters;
    }

    public EnigmaSettings getEnigmaSetup() {
        return enigmaSetup;
    }

    public void setEnigmaSetup(EnigmaSettings enigmaSetup) {
        this.enigmaSetup = enigmaSetup;
    }

}
