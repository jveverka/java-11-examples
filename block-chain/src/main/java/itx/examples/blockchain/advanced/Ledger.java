package itx.examples.blockchain.advanced;

import java.util.Collections;
import java.util.List;

public class Ledger {

    private String id;
    private String hashPrefix;
    private List<Block> blocks;

    public Ledger(String id, String hashPrefix, List<Block> blocks) {
        this.id = id;
        this.hashPrefix = hashPrefix;
        this.blocks = blocks;
    }

    public String getId() {
        return id;
    }

    public String getHashPrefix() {
        return hashPrefix;
    }

    public List<Block> getBlocks() {
        return Collections.unmodifiableList(blocks);
    }

    public int size() {
        return blocks.size();
    }

    public Block getBlockAt(int index) {
        return blocks.get(index);
    }

}
