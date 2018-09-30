package itx.blockchain.pow;

import itx.blockchain.api.HashValidator;

/**
 * Data object for storing proof of work data.
 */
public interface PowData {

    /**
     * Data payload. (any user data)
     * @return
     */
    byte[] getData();

    /**
     * Nonce, special supplement for data payload which is crafted exactly for the payload.
     * Hash of data and nonce combined must meet {@link HashValidator} criteria in proof of work ledger.
     * @return
     */
    byte[] getNonce();

}
