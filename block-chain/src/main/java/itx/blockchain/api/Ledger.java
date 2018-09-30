package itx.blockchain.api;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Iterator;

/**
 * Block chain Ledger containing data blocks.
 */
public interface Ledger {

    /**
     * Get Id of this ledger.
     * @return
     */
    String getId();

    /**
     * Get genesis (first) {@link Block} of this ledger.
     * @return First {@link Block} in this Ledger.
     */
    Block getGenesisBlock();

    /**
     * Get last {@link Block} of this ledger.
     * @return Last added {@link Block}.
     */
    Block getLastBlock();

    /**
     * Get {@link Block} at specific index.
     * @param index index of requested {@link Block}. Must be greater than zero and less than length of this Ledger.
     * @return {@link Block} at index.
     */
    Block getBlock(int index);

    /**
     * Get {@link Iterator} to iterate through all blocks in the ledger.
     * @return
     */
    Iterator<Block> getBlockIterator();

    /**
     * Add data block into this ledger. Data block is verified before is accepted.
     * @param block incoming data {@link Block}
     * @throws InvalidDataBlockException thrown in case that incoming data {@link Block} is not valid.
     * Block is not valid when hashes does not match data payload and previous block.
     */
    void addDataBlock(Block block) throws InvalidDataBlockException;

    /**
     * Add raw data into this ledger.
     * @param data payload of data to be stored in this Ledger as next data {@link Block}
     * @return Data {@link Block} created internally.
     * @throws InvalidDataBlockException thrown in case that incoming data {@link Block} is not valid.
     * Block is not valid when hashes does not match data payload and previous block.
     */
    Block addData(byte[] data) throws InvalidDataBlockException;

    /**
     * Add object data into ledger as last {@link Block}. Input object is serialized using {@link DataWriter} before it is stored in Block.
     * @param data payload data to be stored in the {@link Block}
     * @return instance of {@link Block} containing data.
     * @throws InvalidDataBlockException thrown in case that incoming data {@link Block} is not valid.
     * Block is not valid when hashes does not match data payload and previous block.
     * @throws MissingWriterException thrown in case that {@link DataWriter} for this class is not registered.
     * @throws IOException thrown in case data writing fails.
     */
    <T> Block addDataObject(T data) throws InvalidDataBlockException, MissingWriterException, IOException;

    /**
     * Get data payload of {@link Block} at specified index.
     * @param type type of data stored in {@link Block} payload.
     * @param index ordinal of the block index in this ledger.
     * @param <T> type of block data to be deserialized using {@link DataReader} into instance.
     * @return instance of data payload as specified by type.
     * @throws MissingReaderException thrown in case that {@link DataReader} for this class is not registered.
     * @throws IOException thrown in case data reading fails.
     */
    <T> T getBlockData(Class<T> type, int index) throws MissingReaderException, IOException;

    /**
     * Length of this ledger. Number of data {@link Block}s in this ledger.
     * @return
     */
    int size();

    /**
     * Get {@link MessageDigest} used internally by this Ledger.
     * @return
     */
    MessageDigest getMessageDigest();

}
