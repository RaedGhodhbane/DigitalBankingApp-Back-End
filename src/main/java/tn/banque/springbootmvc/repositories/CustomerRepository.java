package tn.banque.springbootmvc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.banque.springbootmvc.entities.Customer;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
 // List<Customer> findByNameContains(String keyword);
 // On peut remplacer le traitement de cette méthode à travers une requête Hibernate
 @Query ("SELECT c FROM Customer c WHERE c.name LIKE :kw") // Requête HSQL paramétrée
 // c : instance, :kw : paramètre
 List<Customer> searchCustomer(@Param("kw") String keyword);
}
