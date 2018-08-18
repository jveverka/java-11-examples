package itx.examples.blockchain.advanced;

import java.util.ArrayList;
import java.util.List;

public class LedgerBuilder {

    private String id;
    private String hashPrefix;
    private List<Block> blocks;

    public LedgerBuilder() {
        blocks = new ArrayList<>();
    }

    public LedgerBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public LedgerBuilder setHashPrefix(String hashPrefix) {
        this.hashPrefix = hashPrefix;
        return this;
    }

    public LedgerBuilder from(Ledger ledger) {
        this.id = ledger.getId();
        this.hashPrefix = ledger.getHashPrefix();
        this.blocks = new ArrayList<>(ledger.getBlocks());
        return this;
    }

    public LedgerBuilder addData(String data) {
        if (blocks.size() == 0) {
            Block nextBlock = BlockChainUtils.mineGenesisBlock(data, hashPrefix);
            this.blocks.add(nextBlock);
        } else {
            Block lastBlock = blocks.get(blocks.size() - 1);
            Block nextBlock = BlockChainUtils.mineNextBlock(lastBlock, data, hashPrefix);
            this.blocks.add(nextBlock);
        }
        return this;
    }

    public Ledger build() {
        return new Ledger(id, hashPrefix, blocks);
    }

}
