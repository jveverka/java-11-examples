package itx.blockchain.api;

import itx.blockchain.basic.LedgerImpl;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LedgerBuilder {

    private String id;
    private HashValidator hashValidator;
    private Block genesisBlock;
    private List<Block> blocks;
    private MessageDigest messageDigest;
    private List<DataReader<?>> readers;
    private List<DataWriter<?>> writers;

    public LedgerBuilder() {
        this.hashValidator = new HashValidator() {};
        this.blocks = new ArrayList<>();
        this.readers = new ArrayList<>();
        this.writers = new ArrayList<>();
    }

    public LedgerBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public LedgerBuilder setHashValidator(HashValidator hashValidator) {
        this.hashValidator = hashValidator;
        return this;
    }

    public LedgerBuilder setGenesisBlock(Block block) {
        this.genesisBlock = genesisBlock;
        return this;
    }

    public LedgerBuilder addBlocks(Collection<Block> blocks) {
        this.blocks.addAll(blocks);
        return this;
    }

    public LedgerBuilder addBlock(Block block) {
        this.blocks.add(block);
        return this;
    }

    public LedgerBuilder setMessageDigest(MessageDigest messageDigest) {
        this.messageDigest = messageDigest;
        return this;
    }

    public LedgerBuilder addDataReader(DataReader<?> dataReader) {
        this.readers.add(dataReader);
        return this;
    }

    public LedgerBuilder addDataWriter(DataWriter<?> dataWriter) {
        this.writers.add(dataWriter);
        return this;
    }

    public Ledger build() throws InvalidDataBlockException {
        LedgerImpl ledger = new LedgerImpl(id, hashValidator, messageDigest, readers, writers);
        for (Block b: blocks) {
            ledger.addDataBlock(b);
        };
        return ledger;
    }

}
