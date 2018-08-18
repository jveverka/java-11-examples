# Simple BlockChain demo

This is simple block-chain demo for java, using sha256 hashing algorithm.

There are two block-chain implementations. 
1. simple one in ```itx.examples.blockchain.simple``` which does not require proof-of-work (mining) for next block to be 
added into ledger. This implementation is much faster.
2. advanced one in ```itx.examples.blockchain.advanced``` which requires proof-of-work (mining) for next block to be added into ledger. 
When new block is added into ledger, it's hash and nonce is setup to meed certain criteria. This procedure takes computing time.
This implementation is inspired by [this](https://www.youtube.com/watch?v=_160oMzblY8&t=204s) video tutorial.


### Example of use 
```
LedgerBuilder ledgerBuilder = new LedgerBuilder();
ledgerBuilder.setId("ledger 1");
ledgerBuilder.setHashPrefix("00"); //<- this is only for advanced ledger
ledgerBuilder.addData("data 1");
ledgerBuilder.addData("data 2");
ledgerBuilder.addData("data 3");
ledgerBuilder.addData("data 4");

Ledger ledger = ledgerBuilder.build();

boolean ledgerOk = BlockChainUtils.verifyLedger(ledger);
```

#### Build 
```gradle clean build```
