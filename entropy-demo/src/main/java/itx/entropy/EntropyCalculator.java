package itx.entropy;

import java.util.HashMap;
import java.util.Map;

public final class EntropyCalculator {

    private EntropyCalculator() {
    }

    public static Double calculateEntropy(Integer data) {
        if (data == null) {
            return 0D;
        }
        return calculateEntropy(Integer.toString(data));
    }

    public static Double calculateEntropy(Long data) {
        if (data == null) {
            return 0D;
        }
        return calculateEntropy(Long.toString(data));
    }

    public static Double calculateEntropy(String data) {
        if (data == null || data.isEmpty()) {
            return 0D;
        }
        Map<String, Integer> frequencies = new HashMap<>();
        for(int i=0; i<data.length(); i++) {
            String key = String.valueOf(data.charAt(i));
            Integer integer = frequencies.get(key);
            if (integer == null) {
                frequencies.put(key, 1);
            } else {
                frequencies.put(key, integer + 1);
            }
        }
        double dataLength = (double)data.length();
        double entropy = 0D;
        for (Map.Entry<String, Integer> entry: frequencies.entrySet()) {
            double frequency = entry.getValue() / dataLength;
            double log = frequency * (Math.log10(frequency) / Math.log10(2));
            entropy = entropy + log;
        }
        return Math.abs(entropy);
    }

}
