package itx.blockchain.test;

import itx.blockchain.basic.LedgerUtils;
import itx.blockchain.signed.SignedDataImpl;
import itx.blockchain.signed.SignedDataReader;
import itx.blockchain.api.Block;
import itx.blockchain.api.Ledger;
import itx.blockchain.api.LedgerBuilder;
import itx.blockchain.api.LedgerError;
import itx.blockchain.signed.SignedData;
import itx.blockchain.signed.SignedDataWriter;
import itx.blockchain.signed.SignedDataUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.Collection;
import java.util.Iterator;


public class SignedDataTests {

    private static final String algorithm = "SHA256withRSA";

    @Test
    public void testSignedDataReadAndWrite() {
        try {
            byte[] data = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
            byte[] signature = new byte[] { 4, 5, 6, 7 };
            SignedData signedDataIn = new SignedDataImpl("algo1", data, signature);
            SignedDataWriter signedDataWriter = new SignedDataWriter();
            SignedDataReader signedDataReader = new SignedDataReader();
            byte[] bytes = signedDataWriter.writeData(signedDataIn);
            SignedData signedDataOut = signedDataReader.readData(bytes);
            Assert.assertEquals(signedDataIn, signedDataOut);
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public void dataSignAndVerifyTest() {
        try {
            byte[] data = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
            InputStream is = this.getClass().getResourceAsStream("/keystore.jks");
            Assert.assertNotNull(is);
            PrivateKey privateKey = SignedDataUtils.getPrivateKey(is, "secret", "localhost", "secret");
            is.close();

            //1. create signed data DTO
            SignedData signedData = SignedDataUtils.createSignedData(data, privateKey, algorithm);
            Assert.assertNotNull(signedData);

            //2. verify digital signature of original data
            is = this.getClass().getResourceAsStream("/keystore.jks");
            Assert.assertNotNull(is);
            Certificate certificate = SignedDataUtils.getCertificate(is, "secret", "localhost");
            is.close();
            boolean result = SignedDataUtils.verifySignedData(signedData, certificate);
            Assert.assertTrue(result);

            //3. verify digital signature of tampered data
            byte[] tamperedData = new byte[] { 5, 1, 2, 3, 4, 5, 6, 7, 8 };
            SignedData tamperedSignedData = new SignedDataImpl(signedData.getAlgorithm(), tamperedData, signedData.getSignature());
            result = SignedDataUtils.verifySignedData(tamperedSignedData, certificate);
            Assert.assertFalse(result);

        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testLedgerWithSignedData() {
        try {
            InputStream is = this.getClass().getResourceAsStream("/keystore.jks");
            PrivateKey privateKey = SignedDataUtils.getPrivateKey(is, "secret", "localhost", "secret");
            is = this.getClass().getResourceAsStream("/keystore.jks");
            Certificate certificate = SignedDataUtils.getCertificate(is, "secret", "localhost");

            SignedDataWriter signedDataWriter = new SignedDataWriter();
            SignedDataReader signedDataReader = new SignedDataReader();

            MessageDigest sha256MessageDigest = LedgerUtils.getSha256MessageDigest();
            int dataSize = 100;
            byte[] payload = new byte[dataSize];

            //0. create empty ledger
            Ledger ledger = new LedgerBuilder()
                    .setId("ledger01")
                    .setMessageDigest(sha256MessageDigest)
                    .build();

            //1. populate ledger with data
            for (int i=0; i<dataSize; i++) {
                payload[i] = (byte)i;
                SignedData signedData = SignedDataUtils.createSignedData(payload, privateKey, algorithm);
                byte[] dataPayload = signedDataWriter.writeData(signedData);
                ledger.addData(dataPayload);
            }

            //2. verify basic ledger integrity
            Collection<LedgerError> ledgerErrors = LedgerUtils.verifyLedger(ledger);
            Assert.assertNotNull(ledgerErrors);
            Assert.assertTrue(ledgerErrors.size() == 0);

            //3. verify digital signatures of data payloads in the ledger
            Iterator<Block> blockIterator = ledger.getBlockIterator();
            while (blockIterator.hasNext()) {
                Block nextBlock = blockIterator.next();
                SignedData signedData = signedDataReader.readData(nextBlock.getData());
                boolean verificationResult = SignedDataUtils.verifySignedData(signedData, certificate);
                Assert.assertTrue(verificationResult);
            }

        } catch (Exception e) {
            Assert.fail();
        }
    }

}
