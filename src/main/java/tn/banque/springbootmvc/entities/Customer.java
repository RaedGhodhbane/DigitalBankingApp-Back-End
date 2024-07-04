package tn.banque.springbootmvc.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Entity
@Data
@NoArgsConstructor @AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    @OneToMany(mappedBy = "customer" , fetch = FetchType.LAZY)

    // fetch = FetchType.LAZY rapporte toutes les comptes bancaires du customer
    // sous forme de liste

    // la meilleure méthode est de créer des modèles (class_nameDTO) pour chaque JPAEntity
    // pour ne pas afficher la liste des comptes bancaires pour les débutant
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<BankAccount> bankAccount;
}
