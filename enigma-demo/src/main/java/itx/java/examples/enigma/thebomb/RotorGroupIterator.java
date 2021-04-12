package itx.java.examples.enigma.thebomb;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gergej on 26.1.2017.
 */
public class RotorGroupIterator {

    private int[] positions;
    private int lastIndex;
    private String alphabet;
    private boolean hasNext;

    public RotorGroupIterator(Integer groupSize, String alphabet) {
        this.positions = new int[groupSize];
        this.lastIndex = alphabet.length() - 1;
        this.alphabet = alphabet;
        this.hasNext = true;
    }

    public List<Character> getNext() {
        List<Character> result = new ArrayList<>(positions.length);
        for (int i=0; i<positions.length; i++) {
            result.add(alphabet.charAt(positions[i]));
        }
        hasNext = false;
        for (int i=0; i<positions.length; i++) {
            if (positions[i] != lastIndex) {
                hasNext = true;
                break;
            }
        }
        shift(positions.length -1);
        return result;
    }

    public boolean hasNext() {
        return hasNext;
    }

    private void shift(int index) {
        if (index >= 0) {
            positions[index]++;
            if (positions[index] > lastIndex) {
                positions[index] = 0;
                shift(index - 1);
            }
        }
    }

}
