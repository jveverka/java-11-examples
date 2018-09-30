package itx.blockchain.pow;

import java.util.Arrays;

public class PowDataImpl implements PowData {

    private final byte[] data;
    private final byte[] nonce;

    public PowDataImpl(byte[] data, byte[] nonce) {
        this.data = Arrays.copyOf(data, data.length);
        this.nonce = Arrays.copyOf(nonce, nonce.length);
    }

    @Override
    public byte[] getData() {
        return Arrays.copyOf(data, data.length);
    }

    @Override
    public byte[] getNonce() {
        return Arrays.copyOf(nonce, nonce.length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PowDataImpl powData = (PowDataImpl) o;
        return Arrays.equals(data, powData.data) &&
                Arrays.equals(nonce, powData.nonce);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(data);
        result = 31 * result + Arrays.hashCode(nonce);
        return result;
    }

}
