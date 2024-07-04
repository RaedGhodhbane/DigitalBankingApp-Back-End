package tn.banque.springbootmvc.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.banque.springbootmvc.entities.BankAccount;
import tn.banque.springbootmvc.entities.Customer;
import tn.banque.springbootmvc.enums.AccountStatus;

import java.util.Date;


@Data
public class SavingBankAccountDTO extends BankAccountDTO{
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private String currency;
    private CustomerDTO customerDTO;
    private double interestRate;
}
