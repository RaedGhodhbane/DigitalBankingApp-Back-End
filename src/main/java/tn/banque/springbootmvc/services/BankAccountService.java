package tn.banque.springbootmvc.services;

import tn.banque.springbootmvc.dtos.*;
import tn.banque.springbootmvc.entities.BankAccount;
import tn.banque.springbootmvc.entities.CurrentAccount;
import tn.banque.springbootmvc.entities.Customer;
import tn.banque.springbootmvc.entities.SavingAccount;
import tn.banque.springbootmvc.exceptions.BalanceNotSufficientException;
import tn.banque.springbootmvc.exceptions.BankAccountNotFoundException;
import tn.banque.springbootmvc.exceptions.CustomerNotFoundException;

import java.util.List;

// Créer un service pour la partie métier dans lequel on implémente juste les signatures des méthodes métiers
// Dans la partie Servicemétier (Par exemple BankAccountServiceImp) : On implémente avec le code : CRUD
// On va traiter la partie logique ( partie métier ou aussi appelé besoin fonctionnel )
public interface BankAccountService {
    // Package service concerne la couche métier
    // Dans ce package, on va créer d'abord, une interface service dans laquelle
    // on implémente juste les signatures des méthodes métiers ( ce sont tous des méthodes abstraites )
    // saveCustomer permet de sauvegarder un customer
    // 1/ La première méthode saveCustomer : permet d'ajouter un customerDTO à la banque  ( la saisie des données
    // dans un formulaire en Angular contenant juste 3 champs (Input ) qui sont : l'id, name et email
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    // 2/ La deuxième méthode saveCurrentBankAccount : permet d'ajouter un compte courant (CurrentBankAccountDTO)
    // on l'a utilisé car dans la partie front-end Angular, on a un formulaire contenant les inputs suivant
    // (solde, overDrafts et l'Id du client)
    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    // 3/ La troisième méthode saveSavingBankAccount : permet d'ajouter un compte épargne (SavingBankAccountDTO)
    // on l'a utilisé car dans la partie front-end Angular, on a un formulaire contenant les inputs suivant
    // (solde, interestRate et l'Id du client)
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;

    // 4/ La 4ème méthode listCustomer() : permet de lister tous les clients bancaire à partir
    // De la liste CustomerDTO ( pour respecter la structure de la table à afficher dans la partie front-end ( Angular )
    // qui va contenir 3 colonnes qui sont les champs de CustomerDTO ( ID, name et email )
    List<CustomerDTO> listCustomer();
    // 5/ La 5ème méthode getBankAccount : cette méthode permet d'afficher les informations d'un compte
    // bancaire à travers l'accountId.
    BankAccountDTO getBankAccount (String accountId) throws BankAccountNotFoundException;
    // 6/ La 6ème méthode debit : cette méthode permet de débiter un compte bancaire ayant comme
    // accountId avec un montant appelé amount et un autre paramétre description qui indique
    // qu'il s'agit d'une opération de débit ( sous forme de message : informations de l'opération )

    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    // 7/ La 7ème méthode credit : cette méthode permet de créditer un compte bancaire ayant comme
    // accountId avec un montant appelé amount et un autre paramétre description qui indique
    // qu'il s'agit d'une opération de débit ( sous forme de message : informations de l'opération )
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    // 8/ La 8ème méthode transfert : cette méthode permet de transférer un amount du accountIdSource
    // vers l'accountDestination
    void transfert(String accountIdSource, String accountDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

    // 9/ La 9ème méthode bankAccountList() : cette méthode permet de lister les différents comptes
    // bancaires pour tous les clients
    List<BankAccountDTO> bankAccountList();

    // 10/ La 10ème méthode getCustomer : permet de récupérer les données d'un customer à travers
    // son id : customerId
    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    // 11/ La 11ème méthode updateCustomer : permet de modifier les données d'un customer à travers
    // l'objet CustomerDTO ( partie front (Angular) puisque l'utilisateur lorsqu'il veut modifier
    // les données d'un customer, il va cliquer sur un enregistrement ( ligne : qui désigne un objet CustomerDTO
    // à partir du tableau CustomerList
    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    // 12/ La 12ème méthode deleteCustomer : permet de supprimer un customer en introduisant son Id
    void deleteCustomer(Long customerId) throws CustomerNotFoundException;

    // 13/ La 13ème méthode accountHistory : permet d'afficher l'historique ( liste ) des opérations bancaires
    // en introduisant l'accountId
    List <AccountOperationDTO> accountHistory(String accountId) throws BankAccountNotFoundException;

    // 14/ La 14ème méthode getAccountHistory : permet d'afficher l'historique ( liste ) des opérations bancaires
    // en introduisant l'accountId avec une pagination et nombre d'opérations bancaire par page

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;

    // 15/ La 15ème méthode searchCustomers : permet de rechercher un client à travers un
    // mot clé ( exemple : partie front ( Angular ) : si l'utilisateur saisie les lettres
    // Moh, il y'aura affichage de tout les customer qui commencent par Moh
    List<CustomerDTO> searchCustomers(String keyword);

    /*
    // 16/ La 16ème méthode getCustomer : permet de récupérer les données d'un customer à travers
    // son id : customerId à l'aide d'une pagination
    CustomerDTO getCustomer(Long customerId, int page,int size) throws CustomerNotFoundException;
     */
}
