package itx.blockchain.pow;

import itx.blockchain.api.HashValidator;

/**
 * Proof of work hash validator.
 * Hash must start with number of zero digits.
 */
public class PowHashValidator implements HashValidator {

    final int zeroPrefixLength;

    public PowHashValidator(int zeroPrefixLength) {
        this.zeroPrefixLength = zeroPrefixLength;
    }

    @Override
    public boolean checkHash(byte[] hash) {
        for (int i=0; i<zeroPrefixLength; i++) {
            if (hash[i] != 0) {
                return false;
            }
        }
        return true;
    }

}
