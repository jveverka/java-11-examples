package itx.examples.springbank.common.dto;

public class WithdrawRequest extends TransferRequest {

    public WithdrawRequest(AccountId targetAccountId, Long amount) {
        super(targetAccountId, amount);
    }

}
