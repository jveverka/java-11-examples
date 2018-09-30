package itx.blockchain.signed;

/**
 * Data object for storing signed data.
 */
public interface SignedData {

    /**
     * Algorithm name used for data signature.
     * @return
     */
    String getAlgorithm();

    /**
     * Data payload. (any user data)
     * @return
     */
    byte[] getData();

    /**
     * Digital signature of data payload.
     * @return
     */
    byte[] getSignature();

}
