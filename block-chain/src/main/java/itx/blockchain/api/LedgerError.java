package itx.blockchain.api;

public class LedgerError {

    private final int blockIndex;
    private final String errorMessage;

    public LedgerError(int blockIndex, String errorMessage) {
        this.blockIndex = blockIndex;
        this.errorMessage = errorMessage;
    }

    public int getBlockIndex() {
        return blockIndex;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
