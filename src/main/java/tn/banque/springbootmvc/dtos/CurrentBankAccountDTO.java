package tn.banque.springbootmvc.dtos;

import lombok.Data;
import tn.banque.springbootmvc.enums.AccountStatus;

import java.util.Date;
@Data
public class CurrentBankAccountDTO extends BankAccountDTO{
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private String currency;
    private CustomerDTO customerDTO;
    private double overDraft;
}
