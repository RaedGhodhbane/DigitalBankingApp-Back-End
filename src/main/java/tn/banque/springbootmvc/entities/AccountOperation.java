package tn.banque.springbootmvc.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.banque.springbootmvc.enums.OperationType;

import java.util.Date;
@Entity
@Data
@NoArgsConstructor @AllArgsConstructor
public class AccountOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date operationDate;
    private double amount;
    @Enumerated(EnumType.STRING)
    private OperationType type;
    @ManyToOne
    // @ManyToOne veut dire Many account operation dans une seule ( One ) bankAccount
    // Quand on a une association mère fille ( @ManyToOne ) il faut faire un mappage DTO sur la classe fille
    private BankAccount bankAccount;
    private String description;
}
