package itx.examples.springbank.server.controller;

import itx.examples.springbank.common.dto.Client;
import itx.examples.springbank.common.dto.ClientId;
import itx.examples.springbank.common.dto.DepositRequest;
import itx.examples.springbank.common.dto.ServiceException;
import itx.examples.springbank.common.dto.TransactionRequest;
import itx.examples.springbank.common.dto.WithdrawRequest;
import itx.examples.springbank.server.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/services/bank")
public class BankController {

    @Autowired
    private BankService bankService;

    @PostMapping("/transfer")
    public void transferFunds(TransactionRequest transactionRequest) throws ServiceException {
        bankService.transferFunds(transactionRequest);
    }

    @PutMapping("/deposit")
    public void deposit(DepositRequest depositRequest) throws ServiceException {
        bankService.deposit(depositRequest);
    }

    @PutMapping("/withdraw")
    public void withDraw(WithdrawRequest withdrawRequest) throws ServiceException {
        bankService.withDraw(withdrawRequest);
    }

    @GetMapping("/info/client")
    public Client getClientInfo(ClientId clientId) throws ServiceException {
        return bankService.getClientInfo(clientId);
    }

}
