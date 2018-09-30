package itx.blockchain.basic;

import itx.blockchain.api.Block;
import itx.blockchain.api.Ledger;
import itx.blockchain.api.LedgerError;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public final class LedgerUtils {

    private LedgerUtils() {
        throw new UnsupportedOperationException("Please, do not instantiate utility class !");
    }

    public static final byte[] GENESIS_PREVIOUS_BLOCK_HASH = new byte[0];

    public static boolean verifyBlockIntegrity(byte[] previousHash, Block block, MessageDigest messageDigest) {
        byte[] dataBlob = combine(block.getData(), block.getPreviousHash());
        byte[] hash = messageDigest.digest(dataBlob);
        if (Arrays.equals(block.getHash(), hash)) {
            return Arrays.equals(block.getPreviousHash(), previousHash);
        }
        return false;
    }

    public static boolean verifyGenesisBlockIntegrity(Block block, MessageDigest messageDigest) {
        byte[] dataBlob = combine(block.getData(), block.getPreviousHash());
        byte[] hash = messageDigest.digest(dataBlob);
        return Arrays.equals(block.getHash(), hash);
    }

    public static byte[] combine(byte[] a, byte[] b) {
        int length = a.length + b.length;
        byte[] result = new byte[length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    public static MessageDigest getSha256MessageDigest() throws NoSuchAlgorithmException {
        return MessageDigest.getInstance("SHA-256");
    }

    public static Block createBlock(byte[] previousHash, byte[] data, MessageDigest messageDigest) {
        byte[] dataBlob = combine(data, previousHash);
        byte[] hash = messageDigest.digest(dataBlob);
        return new BlockImpl(data, hash, previousHash);
    }

    public static Block createGenesisBlock(byte[] data, MessageDigest messageDigest) {
        byte[] dataBlob = combine(data, GENESIS_PREVIOUS_BLOCK_HASH);
        byte[] hash = messageDigest.digest(dataBlob);
        return new BlockImpl(data, hash, GENESIS_PREVIOUS_BLOCK_HASH);
    }

    /**
     * Verify {@link Ledger} content. This method iterate through ledger starting from genesis block.
     * For each {@link Block}, checksum and previous block hash is checked.
     * @param ledger
     * @return
     */
    public static Collection<LedgerError> verifyLedger(Ledger ledger) {
        List<LedgerError> errorList = new ArrayList<>();
        Iterator<Block> blockIterator = ledger.getBlockIterator();
        Block previousBlock = null;
        int index = 0;
        while (blockIterator.hasNext()) {
            Block nextBlock = blockIterator.next();
            if (previousBlock == null) {
                if (!verifyGenesisBlockIntegrity(nextBlock, ledger.getMessageDigest())) {
                    errorList.add(new LedgerError(index, "invalid genesis block"));
                }
            } else {
                if (!verifyBlockIntegrity(previousBlock.getHash(), nextBlock, ledger.getMessageDigest())) {
                    errorList.add(new LedgerError(index, "invalid data block"));
                }
            }
            index++;
            previousBlock = nextBlock;
        }
        return errorList;
    }

    /**
     * Create hex string from byte array data.
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte byt : bytes) {
            result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }

    /**
     * Create byte array from hex string data.
     * @param data
     * @return
     */
    public static byte[] hexToBytes(String data) {
        int length = data.length() / 2;
        byte[] result = new byte[length];
        for (int i=0; i<length; i++) {
            String subStr = data.substring(i*2, (i+1)*2);
            int intData = Integer.parseInt(subStr, 16);
            result[i] = (byte)(intData - 0x100);
        }
        return result;
    }

}
