package itx.examples.blockchain.tests;

import itx.examples.blockchain.advanced.Block;
import itx.examples.blockchain.advanced.BlockChainUtils;
import itx.examples.blockchain.advanced.Ledger;
import itx.examples.blockchain.advanced.LedgerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class AdvancedBlockChainTests {

    final private static Logger LOG = LoggerFactory.getLogger(AdvancedBlockChainTests.class);

    @DataProvider(name = "nonceData")
    public static Object[][] getNonceData() {
        return new Object[][] {
                { null, "0" },
                { "0", "1" },
                { "a", "b" },
                { "z", "00" },
                { "az1", "az2" },
                { "acz", "ad0" },
                { "zzz", "0000" },
                { "1zzz", "2000" },
                { "1zz5", "1zz6"}

        };
    }

    @Test(dataProvider = "nonceData")
    public void testNonce(String lastNonce, String expectedNextNonce) {
        String nextNonce = BlockChainUtils.getNextNonce(lastNonce);
        Assert.assertEquals(nextNonce, expectedNextNonce);
    }

    @Test
    public void testLedger() {
        //1. create ledger with some data in it
        LedgerBuilder ledgerBuilder = new LedgerBuilder();
        ledgerBuilder.setId("ledger 1");
        ledgerBuilder.setHashPrefix("00");
        ledgerBuilder.addData("data 0");
        ledgerBuilder.addData("data 1");
        ledgerBuilder.addData("data 2");
        ledgerBuilder.addData("data 3");
        ledgerBuilder.addData("data 4");

        Ledger ledger = ledgerBuilder.build();

        //2. verify Ledger blocks
        for (Block block: ledger.getBlocks()) {
            boolean blockOk = BlockChainUtils.verifyBlock(block, ledger.getHashPrefix());
            Assert.assertTrue(blockOk);
        }

        //3. verify Ledger block chaining
        boolean ledgerOk = BlockChainUtils.verifyLedger(ledger);
        Assert.assertTrue(ledgerOk);

        LOG.info("Ledger: {} is OK: {}", ledger.getId(), ledgerOk);
    }

    @Test
    public void testTamperedLedger() {
        //1. create ledger with some data in it
        LedgerBuilder ledgerBuilder = new LedgerBuilder();
        ledgerBuilder.setId("ledger 1");
        ledgerBuilder.setHashPrefix("00");
        ledgerBuilder.addData("data 0");
        ledgerBuilder.addData("data 1");
        ledgerBuilder.addData("data 2");
        ledgerBuilder.addData("data 3");
        ledgerBuilder.addData("data 4");

        Ledger ledger = ledgerBuilder.build();

        //2. modify data in block#2 in the ledger
        Block block2 = ledger.getBlockAt(2);
        Block tamperedBlock2 = new Block(block2.getId(), block2.getNonce(), "tampered data", block2.getPreviousHash(), block2.getHash());
        List<Block> tamperedList = new ArrayList(ledger.getBlocks());
        tamperedList.set(2, tamperedBlock2);
        Ledger tamperedLedger = new Ledger(ledger.getId(), ledger.getHashPrefix(), tamperedList);

        //3. verify Ledger block chaining
        boolean ledgerOk = BlockChainUtils.verifyLedger(tamperedLedger);
        Assert.assertFalse(ledgerOk);

        LOG.info("Ledger: {} is OK: {}", ledger.getId(), ledgerOk);
    }

}
