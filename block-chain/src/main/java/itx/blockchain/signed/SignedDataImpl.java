package itx.blockchain.signed;

import java.util.Arrays;
import java.util.Objects;

public class SignedDataImpl implements SignedData {

    private final String algorithm;
    private final byte[] data;
    private final byte[] signature;

    public SignedDataImpl(String algorithm, byte[] data, byte[] signature) {
        this.algorithm = algorithm;
        this.data = Arrays.copyOf(data, data.length);
        this.signature = Arrays.copyOf(signature, signature.length);
    }

    @Override
    public String getAlgorithm() {
        return algorithm;
    }

    @Override
    public byte[] getData() {
        return Arrays.copyOf(data, data.length);
    }

    @Override
    public byte[] getSignature() {
        return Arrays.copyOf(signature, signature.length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SignedDataImpl that = (SignedDataImpl) o;
        return Objects.equals(algorithm, that.algorithm) &&
                Arrays.equals(data, that.data) &&
                Arrays.equals(signature, that.signature);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(algorithm);
        result = 31 * result + Arrays.hashCode(data);
        result = 31 * result + Arrays.hashCode(signature);
        return result;
    }

}
