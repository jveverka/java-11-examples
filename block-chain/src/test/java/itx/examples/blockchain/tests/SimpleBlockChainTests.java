package itx.examples.blockchain.tests;

import itx.examples.blockchain.simple.Block;
import itx.examples.blockchain.simple.BlockChainUtils;
import itx.examples.blockchain.simple.Ledger;
import itx.examples.blockchain.simple.LedgerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class SimpleBlockChainTests {

    final private static Logger LOG = LoggerFactory.getLogger(SimpleBlockChainTests.class);

    @Test
    public void testLedger() {
        //1. create ledger with some data in it
        LedgerBuilder ledgerBuilder = new LedgerBuilder();
        ledgerBuilder.setId("ledger 1");
        ledgerBuilder.addData("data 0");
        ledgerBuilder.addData("data 1");
        ledgerBuilder.addData("data 2");
        ledgerBuilder.addData("data 3");
        ledgerBuilder.addData("data 4");

        Ledger ledger = ledgerBuilder.build();

        //2. verify Ledger blocks
        for (Block block: ledger.getBlocks()) {
            boolean blockOk = BlockChainUtils.verifyBlock(block);
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
        ledgerBuilder.addData("data 0");
        ledgerBuilder.addData("data 1");
        ledgerBuilder.addData("data 2");
        ledgerBuilder.addData("data 3");
        ledgerBuilder.addData("data 4");

        Ledger ledger = ledgerBuilder.build();

        //2. modify data in block#2 in the ledger
        Block block2 = ledger.getBlockAt(2);
        Block tamperedBlock2 = new Block(block2.getId(),"tampered data", block2.getPreviousHash(), block2.getHash());
        List<Block> tamperedList = new ArrayList(ledger.getBlocks());
        tamperedList.set(2, tamperedBlock2);
        Ledger tamperedLedger = new Ledger(ledger.getId(), tamperedList);

        //3. verify Ledger block chaining
        boolean ledgerOk = BlockChainUtils.verifyLedger(tamperedLedger);
        Assert.assertFalse(ledgerOk);

        LOG.info("Ledger: {} is OK: {}", ledger.getId(), ledgerOk);
    }
}
