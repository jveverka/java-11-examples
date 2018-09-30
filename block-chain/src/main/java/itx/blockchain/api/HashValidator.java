package itx.blockchain.api;

/**
 * Validate hash format. Some block chain systems have specific criteria for hash format.
 * This validator is used by {@link Ledger} to validate incoming block hash format.
 */
public interface HashValidator {

    /**
     * Validate hash format. This default implementation does not require any hash validation.
     * @param hash {@link Block} hash to be validated.
     * @return true if hash is valid, false otherwise.
     */
    default boolean checkHash(byte[] hash) {
        return true;
    }

}
