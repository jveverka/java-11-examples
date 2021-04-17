package itx.java.examples.enigma.impl.alphabet;

import itx.java.examples.enigma.alphabet.Alphabet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by gergej on 18.1.2017.
 */
public class AlphabetImpl implements Alphabet {

    private final String alphabet;
    private final Map<Character, Integer> keys;
    private final Map<Integer, Character> indexes;

    public AlphabetImpl(String alphabet) {
        if (alphabet.length() % 2 != 0) {
            throw new UnsupportedOperationException("alphabet length must be divisible by 2: " + alphabet.length());
        }
        this.alphabet = alphabet;
        this.keys = new ConcurrentHashMap<>();
        this.indexes = new ConcurrentHashMap<>();
        for (int i=0; i<alphabet.length(); i++) {
            keys.put(alphabet.charAt(i),i);
            indexes.put(i, alphabet.charAt(i));
        }
    }

    @Override
    public int getIndex(Character character) {
        Integer result = keys.get(character);
        if (result != null) {
            return result.intValue();
        }
        throw new UnsupportedOperationException("Input character '" + character + "' is not supported !");
    }

    @Override
    public Character getCharacter(int index) {
        Character result = indexes.get(index);
        if (result != null) {
            return result.charValue();
        }
        throw new UnsupportedOperationException("Input index '" + index + "' is not supported !");
    }

    @Override
    public int getSize() {
        return keys.size();
    }

    @Override
    public String getAlphabet() {
        return alphabet;
    }

}
