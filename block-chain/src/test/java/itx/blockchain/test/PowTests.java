package itx.blockchain.test;

import itx.blockchain.api.HashValidator;
import itx.blockchain.api.InvalidDataBlockException;
import itx.blockchain.api.LedgerError;
import itx.blockchain.basic.LedgerUtils;
import itx.blockchain.pow.PowData;
import itx.blockchain.pow.PowDataWriter;
import itx.blockchain.pow.PowHashValidator;
import itx.blockchain.pow.PowUtils;
import itx.blockchain.api.Block;
import itx.blockchain.api.Ledger;
import itx.blockchain.api.LedgerBuilder;
import itx.blockchain.pow.PowDataImpl;
import itx.blockchain.pow.PowDataReader;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Iterator;

public class PowTests {

    @Test
    public void testPowDataReaderAndWriter() {
        try {
            byte[] data = new byte[] { 0, 1, 2, 3, 4 };
            byte[] nonce = new byte[] { 0, 1, 2 };
            PowData powDataIn = new PowDataImpl(data, nonce);
            PowDataReader powDataReader = new PowDataReader();
            PowDataWriter powDataWriter = new PowDataWriter();
            byte[] bytes = powDataWriter.writeData(powDataIn);
            PowData powDataOut = powDataReader.readData(bytes);
            Assert.assertEquals(powDataIn, powDataOut);
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public void testPowHashValidator() {
        byte[] validHash = new byte[] { 0, 0, 0, 1, 2, 3 };
        byte[] invalidHash = new byte[] { 1, 2, 3, 4, 5, 6 };
        HashValidator hashValidator = new PowHashValidator(1);
        Assert.assertTrue(hashValidator.checkHash(validHash));
        Assert.assertFalse(hashValidator.checkHash(invalidHash));

        hashValidator = new PowHashValidator(2);
        Assert.assertTrue(hashValidator.checkHash(validHash));
        Assert.assertFalse(hashValidator.checkHash(invalidHash));

        hashValidator = new PowHashValidator(3);
        Assert.assertTrue(hashValidator.checkHash(validHash));
        Assert.assertFalse(hashValidator.checkHash(invalidHash));

        hashValidator = new PowHashValidator(4);
        Assert.assertFalse(hashValidator.checkHash(validHash));
        Assert.assertFalse(hashValidator.checkHash(invalidHash));
    }

    @Test
    public void testNonceCalculation() {
        try {
            MessageDigest sha256MessageDigest = LedgerUtils.getSha256MessageDigest();
            byte[] data = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 15 };
            byte[] previousBlockHash = new byte[sha256MessageDigest.getDigestLength()];
            HashValidator hashValidator = new PowHashValidator(2);
            byte[] nonce = PowUtils.generateNonce(data, previousBlockHash, hashValidator, sha256MessageDigest);
            Assert.assertNotNull(nonce);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    /**
     * Test proof of work ledger use case.
     */
    @Test
    public void testPowLedger() {
        try {
            MessageDigest sha256MessageDigest = LedgerUtils.getSha256MessageDigest();
            int dataSize = 100;
            byte[] payload = new byte[dataSize];
            byte[] previousBlockHash = new byte[] {};
            HashValidator hashValidator = new PowHashValidator(1);

            //0. create empty ledger
            Ledger ledger = new LedgerBuilder()
                    .setId("powLedger01")
                    .setMessageDigest(sha256MessageDigest)
                    .setHashValidator(hashValidator)
                    .build();

            //1. populate ledger with data
            for (int i=0; i<dataSize; i++) {
                payload[i] = (byte)i;
                Block powBlock = PowUtils.createPowBlock(payload, previousBlockHash, hashValidator, sha256MessageDigest);
                ledger.addDataBlock(powBlock);
                previousBlockHash = powBlock.getHash();
            }

            //2. verify basic ledger integrity
            Collection<LedgerError> ledgerErrors = LedgerUtils.verifyLedger(ledger);
            Assert.assertNotNull(ledgerErrors);
            Assert.assertTrue(ledgerErrors.size() == 0);

            //3. verify pow data payloads
            PowDataReader powDataReader = new PowDataReader();
            Iterator<Block> blockIterator = ledger.getBlockIterator();
            while(blockIterator.hasNext()) {
                Block next = blockIterator.next();
                PowData powData = powDataReader.readData(next.getData());
                Assert.assertNotNull(powData);
            }

        } catch (IOException | NoSuchAlgorithmException | InvalidDataBlockException e) {
            Assert.fail();
        }
    }

}
