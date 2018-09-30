package itx.blockchain.test;

import itx.blockchain.api.InvalidDataBlockException;
import itx.blockchain.api.LedgerError;
import itx.blockchain.basic.LedgerUtils;
import itx.blockchain.api.Ledger;
import itx.blockchain.api.LedgerBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

public class LedgerTests {

    @Test
    public void testVerifyLedger() {
        try {
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
                ledger.addData(payload);
            }

            //2. verify basic ledger integrity
            Collection<LedgerError> ledgerErrors = LedgerUtils.verifyLedger(ledger);
            Assert.assertNotNull(ledgerErrors);
            Assert.assertTrue(ledgerErrors.size() == 0);

        } catch (NoSuchAlgorithmException | InvalidDataBlockException e) {
            Assert.fail();
        }
    }

}