package itx.examples.springbank.common.dto;

public abstract class TransferRequest {

    private final AccountId targetAccountId;
    private final Long amount;

    public TransferRequest(AccountId targetAccountId, Long amount) {
        this.targetAccountId = targetAccountId;
        this.amount = amount;
    }

    public AccountId getTargetAccountId() {
        return targetAccountId;
    }

    public Long getAmount() {
        return amount;
    }

}
