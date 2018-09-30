package itx.blockchain.pow;

import itx.blockchain.api.HashValidator;
import itx.blockchain.basic.LedgerUtils;
import itx.blockchain.api.Block;

import java.io.IOException;
import java.security.MessageDigest;

/**
 * Utilities for proof of work ledger use cases.
 */
public final class PowUtils {

    private PowUtils() {
        throw new UnsupportedOperationException("Please, do not instantiate utility class !");
    }

    /**
     * This method finds correct nonce which creates expected hash with data payload using brute-force method.
     * This is essentially mining of next block in the ledger in proof of work ledger systems.
     * Based on how complicated is hash validation, this method may take very long to run or run indefinitely (no suitable nonce is found).
     * @param data payload data to store in the block.
     * @param previousBlockHash hash of previous block.
     * @param hashValidator validator of hash produced for the block.
     * @param messageDigest message digest instance.
     * @return
     * @throws IOException
     */
    public static byte[] generateNonce(byte[] data, byte[] previousBlockHash, HashValidator hashValidator,
                                       MessageDigest messageDigest) throws IOException {
        byte[] nonce = new byte[1];
        nonce[0] = Byte.MIN_VALUE;
        int nonceIndex = 0;
        PowDataWriter powDataWriter = new PowDataWriter();

        while(true) {
            byte[] powData = powDataWriter.writeData(new PowDataImpl(data, nonce));
            byte[] combined = LedgerUtils.combine(powData, previousBlockHash);
            byte[] hash = messageDigest.digest(combined);
            if (hashValidator.checkHash(hash)) {
                break;
            } else {
                if (nonce[nonceIndex] >= Byte.MAX_VALUE) {
                    nonceIndex++;
                    byte[] nonceNew = new byte[nonceIndex+1];
                    System.arraycopy(nonce, 0, nonceNew, 0, nonce.length);
                    nonceNew[nonceIndex] = Byte.MIN_VALUE;
                    nonce = nonceNew;
                }
                nonce[nonceIndex]++;
            }
        }
        return nonce;
    }

    /**
     * Create valid ledger block which contains proof of work data payload.
     * @param data payload data to store in the block.
     * @param previousBlockHash hash of previous block.
     * @param hashValidator validator of hash produced for the block.
     * @param messageDigest message digest instance.
     * @return
     * @throws IOException
     */
    public static Block createPowBlock(byte[] data, byte[] previousBlockHash, HashValidator hashValidator,
                                    MessageDigest messageDigest) throws IOException {
        byte[] nonce = generateNonce(data, previousBlockHash, hashValidator, messageDigest);
        PowData powData = new PowDataImpl(data, nonce);
        PowDataWriter powDataWriter = new PowDataWriter();
        byte[] payload = powDataWriter.writeData(powData);
        return LedgerUtils.createBlock(previousBlockHash, payload, messageDigest);
    }

}
