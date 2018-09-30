package itx.blockchain.basic;

import itx.blockchain.api.Block;

import java.util.Arrays;

public class BlockImpl implements Block {

    private final byte[] data;
    private final byte[] hash;
    private final byte[] previousHash;

    public BlockImpl(byte[] data, byte[] hash, byte[] previousHash) {
        this.data = Arrays.copyOf(data, data.length);
        this.hash = Arrays.copyOf(hash, hash.length);
        this.previousHash = Arrays.copyOf(previousHash, previousHash.length);
    }

    @Override
    public byte[] getData() {
        return Arrays.copyOf(data, data.length);
    }

    @Override
    public byte[] getHash() {
        return Arrays.copyOf(hash, hash.length);
    }

    @Override
    public byte[] getPreviousHash() {
        return Arrays.copyOf(previousHash, previousHash.length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockImpl block = (BlockImpl) o;
        return Arrays.equals(data, block.data) &&
                Arrays.equals(hash, block.hash) &&
                Arrays.equals(previousHash, block.previousHash);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(data);
        result = 31 * result + Arrays.hashCode(hash);
        result = 31 * result + Arrays.hashCode(previousHash);
        return result;
    }

}
