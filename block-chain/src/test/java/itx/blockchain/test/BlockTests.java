package itx.blockchain.test;

import itx.blockchain.basic.BlockImpl;
import itx.blockchain.basic.LedgerUtils;
import itx.blockchain.api.Block;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BlockTests {

    private static final byte[] payload = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7 };
    private static final byte[] empty = new byte[] {};
    private static final byte[] payloadSha256Hash = new byte[]
            { -118, -123, 31, -8, 46, -25, 4, -118, -48, -98, -61, -124, 127, 29, -33, 68, -108, 65, 4, -46, -53,
                    -47, 126, -12, -29, -37, 34, -58, 120, 90, 13, 69 };
    private static final byte[] payloadCombinedSha256Hash = new byte[]
            { 74, -22, 87, -54, 62, 44, -127, 121, 41, 88, 3, -36, -104, -57, -13, 41, -109, -107, -68, -10, -10,
                    117, 28, -102, -79, -77, -51, -74, 72, -69, 119, 71 };

    @DataProvider(name = "BlocksData")
    public static Object[][] getArrayCompareData() {
        return new Object[][] {
                { empty, empty, empty, Boolean.FALSE },                                   //invalid block
                { payload, empty, empty, Boolean.FALSE },                                 //invalid block
                { payload, empty, payload, Boolean.FALSE },                               //invalid block
                { payload, payload, empty, Boolean.FALSE },                               //invalid block
                { payload, payload, payload, Boolean.FALSE },                             //invalid block
                { payload, payloadSha256Hash, empty, Boolean.TRUE },                      //genesis block
                { payload, payloadCombinedSha256Hash, payloadSha256Hash, Boolean.TRUE },  //next block
                { payload, payloadSha256Hash, payloadCombinedSha256Hash, Boolean.FALSE }, //invalid block
                { payload, payloadSha256Hash, payloadSha256Hash, Boolean.FALSE },         //invalid block
        };
    }

    @Test(dataProvider = "BlocksData")
    public void testBlocks(byte[] data, byte[] hash, byte[] previousHash, Boolean expectedResult) {
        try {
            MessageDigest sha256MessageDigest = LedgerUtils.getSha256MessageDigest();
            Block block = new BlockImpl(data, hash, previousHash);
            Boolean result = LedgerUtils.verifyBlockIntegrity(previousHash, block, sha256MessageDigest);
            Assert.assertTrue(result.equals(expectedResult));
        } catch (NoSuchAlgorithmException e) {
            Assert.fail();
        }
    }

    @Test
    public void testVerifyBlocks() {
        try {
            MessageDigest sha256MessageDigest = LedgerUtils.getSha256MessageDigest();
            int dataSize = 100;
            byte[] payload = new byte[dataSize];
            Block previousBlock = LedgerUtils.createGenesisBlock(payload, sha256MessageDigest);
            Assert.assertTrue(LedgerUtils.verifyGenesisBlockIntegrity(previousBlock, sha256MessageDigest));
            for (int i=1; i<dataSize; i++) {
                payload[i] = (byte)i;
                Block nextBlock = LedgerUtils.createBlock(previousBlock.getHash(), payload, sha256MessageDigest);
                Assert.assertTrue(LedgerUtils.verifyBlockIntegrity(previousBlock.getHash(), nextBlock, sha256MessageDigest));
                previousBlock = nextBlock;
            }
        } catch (NoSuchAlgorithmException e) {
            Assert.fail();
        }
    }

}
