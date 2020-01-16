package itx.examples.springbank.common.dto;

public class DepositRequest extends TransferRequest {

    public DepositRequest(AccountId targetAccountId, Long amount) {
        super(targetAccountId, amount);
    }

}
