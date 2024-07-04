package tn.banque.springbootmvc.dtos;

import jakarta.persistence.*;
import lombok.Data;
import tn.banque.springbootmvc.entities.BankAccount;
import tn.banque.springbootmvc.enums.OperationType;

import java.util.Date;
@Data
public class AccountOperationDTO {

    private Long id;
    private Date operationDate;
    private double amount;
    private OperationType type;
    private String description;
}
