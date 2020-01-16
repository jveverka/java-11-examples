package itx.examples.springbank.common.dto;

public class Account {

    private final Long credit;

    public Account(Long credit) {
        this.credit = credit;
    }

    public Long getCredit() {
        return credit;
    }

}
