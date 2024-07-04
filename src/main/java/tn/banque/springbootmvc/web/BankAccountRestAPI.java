package tn.banque.springbootmvc.web;

import org.springframework.web.bind.annotation.*;
import tn.banque.springbootmvc.dtos.AccountHistoryDTO;
import tn.banque.springbootmvc.dtos.AccountOperationDTO;
import tn.banque.springbootmvc.dtos.BankAccountDTO;
import tn.banque.springbootmvc.exceptions.BankAccountNotFoundException;
import tn.banque.springbootmvc.services.BankAccountService;

import java.util.List;

@RestController
@CrossOrigin("*")
public class BankAccountRestAPI {
    private BankAccountService bankAccountService;

    public BankAccountRestAPI(BankAccountService bankAccountService) {

        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }

    @GetMapping("/accounts")
    public List<BankAccountDTO> listAccounts() {

        return bankAccountService.bankAccountList();
    }


    @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationDTO> getHistory(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.accountHistory(accountId);
    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getAccountHistory(@PathVariable String accountId,
                                               @RequestParam(name="page" , defaultValue = "0") int page , @RequestParam(name="size" , defaultValue = "5") int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId,page,size);
    }
}