package itx.examples.springbank.common.dto;

public class TransactionRequest extends TransferRequest {

    private final AccountId sourceAccountId;

    public TransactionRequest(AccountId sourceAccountId, AccountId targetAccountId, Long amount) {
        super(targetAccountId, amount);
        this.sourceAccountId = sourceAccountId;
    }

    public AccountId getSourceAccountId() {
        return sourceAccountId;
    }

}
