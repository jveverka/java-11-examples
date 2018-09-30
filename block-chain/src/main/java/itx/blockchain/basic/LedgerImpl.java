package itx.blockchain.basic;

import itx.blockchain.api.Block;
import itx.blockchain.api.DataReader;
import itx.blockchain.api.DataWriter;
import itx.blockchain.api.HashValidator;
import itx.blockchain.api.InvalidDataBlockException;
import itx.blockchain.api.Ledger;
import itx.blockchain.api.MissingReaderException;
import itx.blockchain.api.MissingWriterException;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LedgerImpl implements Ledger {

    private final String id;
    private final HashValidator hashValidator;
    private final MessageDigest messageDigest;
    private final List<Block> blockList;
    private Block lastBlock;
    private Block genesisBlock;
    private final Map<Class<?>, DataReader<?>> readers;
    private final Map<Class<?>, DataWriter<?>> writers;

    public LedgerImpl(String id, HashValidator hashValidator, MessageDigest messageDigest,
                      List<DataReader<?>> readers, List<DataWriter<?>> writers) {
        this.id = id;
        this.hashValidator = hashValidator;
        this.messageDigest = messageDigest;
        this.blockList = new ArrayList<>();
        this.readers = new HashMap<>();
        this.writers = new HashMap<>();
        for (DataReader<?> reader: readers) {
            this.readers.put(reader.forClass(), reader);
        }
        for (DataWriter<?> writer: writers) {
            this.writers.put(writer.forClass(), writer);
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Block getGenesisBlock() {
        return genesisBlock;
    }

    @Override
    public Block getLastBlock() {
        return lastBlock;
    }

    @Override
    public Block getBlock(int index) {
        return blockList.get(index);
    }

    @Override
    public Iterator<Block> getBlockIterator() {
        return blockList.iterator();
    }

    @Override
    public void addDataBlock(Block block) throws InvalidDataBlockException {
        if(!hashValidator.checkHash(block.getHash())) {
            throw new InvalidDataBlockException("invalid block hash");
        }
        if (blockList.size() == 0) {
            //add genesis block
            if (LedgerUtils.verifyGenesisBlockIntegrity(block, messageDigest)) {
                blockList.add(block);
                lastBlock = block;
                genesisBlock = block;
            } else {
                throw new InvalidDataBlockException("invalid genesis block content");
            }
        } else {
            //add next block
            if (LedgerUtils.verifyBlockIntegrity(lastBlock.getHash(), block, messageDigest)) {
                blockList.add(block);
                lastBlock = block;
            } else {
                throw new InvalidDataBlockException("invalid block content");
            }
        }
    }

    @Override
    public Block addData(byte[] data) throws InvalidDataBlockException {
        if (blockList.size() == 0) {
            //add genesis block
            Block genesisBlock = LedgerUtils.createGenesisBlock(Arrays.copyOf(data, data.length), messageDigest);
            addDataBlock(genesisBlock);
            return genesisBlock;
        } else {
            //add next block
            Block block = LedgerUtils.createBlock(lastBlock.getHash(), Arrays.copyOf(data, data.length), messageDigest);
            addDataBlock(block);
            return block;
        }
    }

    @Override
    public <T> Block addDataObject(T data) throws InvalidDataBlockException, MissingWriterException, IOException {
        DataWriter<T> dataWriter = (DataWriter<T>)writers.get(data.getClass());
        if (dataWriter == null) {
            throw new MissingWriterException();
        }
        byte[] dataBytes = dataWriter.writeData(data);
        return addData(dataBytes);
    }

    @Override
    public <T> T getBlockData(Class<T> type, int index) throws MissingReaderException, IOException {
        DataReader<?> dataReader = readers.get(type);
        if (dataReader == null) {
            throw new MissingReaderException();
        }
        Block block = blockList.get(index);
        return (T)dataReader.readData(block.getData());
    }

    @Override
    public int size() {
        return blockList.size();
    }

    @Override
    public MessageDigest getMessageDigest() {
        return messageDigest;
    }

}
