package itx.examples.blockchain.simple;

import java.util.ArrayList;
import java.util.List;

public class LedgerBuilder {

    private String id;
    private List<Block> blocks;

    public LedgerBuilder() {
        blocks = new ArrayList<>();
    }

    public LedgerBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public LedgerBuilder from(Ledger ledger) {
        this.id = ledger.getId();
        this.blocks = new ArrayList<>(ledger.getBlocks());
        return this;
    }

    public LedgerBuilder addData(String data) {
        if (blocks.size() == 0) {
            Block nextBlock = BlockChainUtils.createGenesisBlock(data);
            this.blocks.add(nextBlock);
        } else {
            Block lastBlock = blocks.get(blocks.size() - 1);
            Block nextBlock = BlockChainUtils.createNextBlock(lastBlock, data);
            this.blocks.add(nextBlock);
        }
        return this;
    }

    public Ledger build() {
        return new Ledger(id, blocks);
    }

}
