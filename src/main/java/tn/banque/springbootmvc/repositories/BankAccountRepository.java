package tn.banque.springbootmvc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.banque.springbootmvc.entities.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
    // Pas d'implémentation car toutes les méthodes sont des méthodes métiers selon les besoin du système bancaire
}
