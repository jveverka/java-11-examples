package itx.blockchain.api;

/**
 * Basic building block of ledger is data block.
 * This class represents single data block in the {@link Ledger}
 */
public interface Block {

    /**
     * Get data payload of this block.
     * @return
     */
    byte[] getData();

    /**
     * Get calculated hash of data payload and previous block hash.
     * Hash function used is specified by {@link Ledger}.
     * @return
     */
    byte[] getHash();

    /**
     * Get calculated hash of previous data block.
     * Hash function used is specified by {@link Ledger}.
     * @return
     */
    byte[] getPreviousHash();

}
