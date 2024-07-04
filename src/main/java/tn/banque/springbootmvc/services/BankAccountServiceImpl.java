package tn.banque.springbootmvc.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import tn.banque.springbootmvc.dtos.*;
import tn.banque.springbootmvc.entities.*;
import tn.banque.springbootmvc.enums.OperationType;
import tn.banque.springbootmvc.exceptions.BalanceNotSufficientException;
import tn.banque.springbootmvc.exceptions.BankAccountNotFoundException;
import tn.banque.springbootmvc.exceptions.CustomerNotFoundException;
import tn.banque.springbootmvc.mappers.BankAccountMapperImpl;
import tn.banque.springbootmvc.repositories.AccountOperationRepository;
import tn.banque.springbootmvc.repositories.BankAccountRepository;
import tn.banque.springbootmvc.repositories.CustomerRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

// @Service L'annotation Service est obligatoire
@Service
// @Transactional pour gérer les transactions ( 2 transactions : les commit : pour valider une
// transaction ( Exemple de commit : Compte bancaire créé avec succès) et les rollback : pour annuler
// une transaction ( Exemple de rollback : Compte bancaire annulé ) : Obligatoire
@Transactional // rollback commit
// @AllArgsConstructor pour injecter les dépendances métier ( l'instanciation des objets devient
// dynamique et non pas statique : Obligatoire

@AllArgsConstructor
// injection des dépendances
// @Slf4j pour la journalisation càd la documentation ( pour faire l'upload de votre
// api sur Github ) : Obligatoire
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{

    // Déclarer toutes les repositories nécessaires qui ont une relation avec la partie
    // métier ( les méthodes métier )

    private CustomerRepository customerRepository;

    private BankAccountRepository bankAccountRepository;

    private AccountOperationRepository accountOperationRepository;

    //Déclaration d'un objet mapper nommé dtoMapper puisque la majorité des méthodes métiers
    // vont traiter des objets de type JPADTO ( Exemple : CustomerDTO, BankAccountDTO... )

    private BankAccountMapperImpl dtoMapper;

    // pour la journalisation
    //Logger log = (Logger) LoggerFactory.getLogger(this.getClass().getName());

    // Cette une méthode redéfinie de l'interface BankAccountService ( contient une implémentation
    // métier )
    // Cette méthode va retourner un objet de type CustomerDTO
    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer");
        // Puisque on va sauvegarder les données dans une entité JPA ( mapper : changer les customersDTO
        // en Customer )
        // on va faire l'appel à la méthode fromCustomerDTO ( les données)
        // sont récupéré à partir d'un formulaire Angular ( DTO (model))
        // et on va le sauvegarder dans la base de données dans une entité JPA saveCustomer
        // Sauvegarde dans base de données : JPA
        // Affichage et utilisation dans formulaire ( front-end ) : DTO
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        // On a sauvegardé une instance saveCustomer de type JPA
        Customer saveCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(saveCustomer);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        // D'abord Il faut rechercher un client à travers son Id (findById)
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer == null)
            throw new CustomerNotFoundException("Customer not found");
        // En cas où le client existe il y'aura création d'une entité SavingAccount à travers
        // un constructeur par défaut après le sauvegarde d'une entité savingAccount dans la base
        // de données du système
        SavingAccount savingAccount = new SavingAccount() ;
        // Pour remplir les données du compte bancaire, on a créé une instance vide
        // en utilisant un constructeur par défaut puis pour remplir les données
        // relatives à l'instance créé ( l'instance est savingAccount )

        // On a utilisé la méthode UUID.randomUUID().toString() pour regénérer une chaîne
        // de caractère aléatoire qui désigne le numéro de compte
        savingAccount.setId(UUID.randomUUID().toString());
        // Concernant la date de création du compte; on a utilisé la fonction Date qui donne
        // la date d'aujourd'hui
        savingAccount.setCreatedAt(new Date());
        // Affectation à travers un setter le solde du compte bancaire
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        // L'entité JPA savedBankAccount ( remplie et sauvegardée dans la base de données)
        SavingAccount  savedBankAccount = bankAccountRepository.save(savingAccount);
        // On a fait le mappage JPA à DTO pour afficher l'instance savedBankAccount dans la liste
        // des comptes bancaires ( dans la partie front-end (Angular) )
        return dtoMapper.fromSavingBankAccount(savedBankAccount);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer == null)
            throw new CustomerNotFoundException("Customer not found");
        CurrentAccount currentAccount = new CurrentAccount() ;
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
        CurrentAccount  savedBankAccount = bankAccountRepository.save(currentAccount);

        return dtoMapper.fromCurrentBankAccount(savedBankAccount);
    }

    // Pour afficher la liste des Customer, on va
    @Override
    public List<CustomerDTO> listCustomer() {
        // D'abord on va faire l'appel à la customerRepository à l'aide de la
        // méthode findAll() pour retourner la liste des Customer ( de la base de données (ce sont des
        // JPA) )
        List<Customer> customers=customerRepository.findAll();
        // stream : pour récupérer la liste des customers sous forme de collection (liste dynamique)
        List<CustomerDTO> customerDTOS=customers.stream()
                // Chaque entité customer récupéré sera mappée en customerDTO pour l'afficher dans le
                // tableau ( partie front-end ( Angular ) )
                .map(customer -> dtoMapper.fromCustomer(customer))
                // On a sauvegardé dans la collection (liste)
                .collect(Collectors.toList());
        /*List<CustomerDTO> customerDTOS = new ArrayList<>();
        for(Customer customer :customers){
            CustomerDTO customerDTO = dtoMapper.fromCustomer(customer);
            customerDTOS.add(customerDTO);

        }
        */
        return customerDTOS;
    }

    // Cette méthode permet la recherche d'un compte en introduisant l'accountId
    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {

        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                // orElseThrow : Pour traiter le comportement de l'exception métier
                // BankAccountNotFoundException pour afficher le message d'erreur dans le cas
                // où l'accountId n'existe pas
                .orElseThrow(()-> new BankAccountNotFoundException("Bank account not found"));
        // On a utilisé l'opérateur instanceof ( cas d'héritage ) pour tester si l'instance bankAccount est de
        // type SavingAccount  alors dans ce cas, on va créer une instance de type SavingAccount
       if(bankAccount instanceof SavingAccount){
           // on a appliquer un cast sur l'instance bankAccount pour le rendre de type
           // SavingAccount
           SavingAccount savingAccount =(SavingAccount) bankAccount;
           // On va enregistrer dans la base de donné, donc on a fait un mappage de DTO en JPA à l'aide de
           // dtoMapper puisqu'on va sauvegarder dans la base de données une entité de type JPA ( savingAccount )
           return dtoMapper.fromSavingBankAccount(savingAccount);
       }else{
           // Dans ce cas, on va appliquer un cast sur l'instance bankAccount pour le rendre de type
           // CurrentAccount
           CurrentAccount currentAccount =(CurrentAccount) bankAccount;
           // On va enregistrer dans la base de donné, donc on a fait un mappage de DTO en JPA à l'aide de
           // dtoMapper puisqu'on va sauvegarder dans la base de données une entité de type JPA ( currentAccount )
           return dtoMapper.fromCurrentBankAccount(currentAccount);

       }
    }

    // Cette méthode debit ne concerne que les entités JPA ( AccountOperation et BankAccount )
    // Pas de DTO puisque le comportement de cette méthode, ne concerne que un simple click sur le bouton ( pas
    // d'affichage sur le front-end (Angular)
    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundException("Bank account not found"));
        if(bankAccount.getBalance()<amount)
            throw new BalanceNotSufficientException("Balance not sufficient");
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        //mise a jour du solde
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);

    }

    //Pas de DTO içi le comportement de cette méthode ne concerne que des JPA entities ( BankAccount
    // et AccountOperation )
    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                //orElseThrow est utilisée pour traiter et gérer les exceptions métiers
                .orElseThrow(()-> new BankAccountNotFoundException("Bank account not found"));
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        //mise a jour du solde
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);

    }
   // Pas de DTO
   // On a fait l'appel à la méthode debit qui a comme paramétre l'accountIdSource, le montant
   // à transférer et une description de l'opération de débit
   // On a fait l'appel aussi à la méthode credit qui a comme paramètre l'accountDestination, le montant
   // à créditer et une description de l'opération de crédit
    @Override
    public void transfert(String accountIdSource, String accountDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource,amount,"Transfer to "+accountDestination);
        credit(accountDestination,amount,"Transfer from "+accountIdSource);

    }

    @Override
    public List<BankAccountDTO> bankAccountList(){

        List <BankAccount> bankAccounts = bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS=bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount){
                SavingAccount savingAccount =(SavingAccount) bankAccount;
                return dtoMapper.fromSavingBankAccount(savingAccount);
            }else{
                CurrentAccount currentAccount =(CurrentAccount) bankAccount;
                return dtoMapper.fromCurrentBankAccount(currentAccount);
            }
        }).collect(Collectors.toList());
        return bankAccountDTOS;
    }
    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException{
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(()->new CustomerNotFoundException("Customer Not found"));
        return dtoMapper.fromCustomer(customer);
    }

    /*@Override
    public CustomerDTO getCustomer(Long customerId, int page,int size) throws CustomerNotFoundException{
        Page<Customer> customers = customerRepository.findById(customerId, PageRequest.of(page,size))
                .orElseThrow(()->new CustomerNotFoundException("Customer Not found"));
        return dtoMapper.fromCustomer(customers);
    }
     */

    // Pour la modification d'un customer, on a des données déjà saisi introduites à partir
    // d'un formulaire Angular, on a ajouté des nouvelles données ( customerDTO)
   @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        // log fichier de journalisation dans Github
        log.info("Updating a Customer");
        //  On va sauvegarder dans la base de données les nouvelles modifications dans une entité JPA ( customer )
        // C'est pourquoi on a fait un mappage customerDTO vers customer
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer saveCustomer = customerRepository.save(customer);
        // On va afficher à partir de la base de la base de données
       // le customer après la modification c'est pourquoi on a fait
       // un mappage savecustomer vers customerDTO
        return dtoMapper.fromCustomer(saveCustomer);
    }

    // Cette méthode permet de supprimer un customer à partir du customerId
    // Après l'opération de suppression du customer ( JPA ) la liste des customers ( Angular )
    // sera mis à jour après l'opération de suppression
    // deleteCustomer est purement JPA ( dialogue est directement avec la base de données )
    @Override
    public void deleteCustomer(Long customerId) throws CustomerNotFoundException
    {
        customerRepository.deleteById(customerId);
    }


    @Override
    public List <AccountOperationDTO> accountHistory(String accountId){
        // D'abord, on cherche la liste des opérations bancaires à travers l'accountId ( accès immédiat
        // JPA ( à la base de données )
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
        // Après la récupération des opérations bancaires relatives au compte bancaire ayant comme
        // Id ( account Id ) on a récupéré les opérations bancaires sous forme de collection ( stream() ( Design Pattern ):
        // Chaque ligne récupéré qui désigne une opération bancaire sera mappé en DTO ( BankAccountOperationDTO ) et après
        // on va l'afficher sur la table ( Template Angular )
        // On peut remplacer Collectors par Observor
        return accountOperations.stream().map(op->dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
    }

    // Cette méthode permet d'afficher l'historique des opérations bancaires pour un compte bancaire donnée
    // ( on introduisant l'accountId ) avec une pagination ( le numéro de la page ) et la taille de la page
    // ( le nombre de ligne à afficher dans la page : nombre d'opérations à afficher )
    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        if (bankAccount == null) throw new BankAccountNotFoundException("Account not Found");
    // On récupére les opérations bancaires et on les sauvegardes sous forme de liste de type page
    // PageRequest.of contient le numéro de page et combien de données sur la page
     Page<AccountOperation> accountOperations= accountOperationRepository.findByBankAccountId(accountId, PageRequest.of(page,size));
    // On a créé une instance de type accountHistoryDTO
     AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
    // On a créé une liste de type AccountOperationDTO dans laquelle on va sauvegarder les opérations
    // bancaires déjà existante dans la page accountOperations en utilisant la méthode stream
    // qui va ouvrir la liste et va parcourir la page accountOperations, pour chaque opération ( op )
    // il y'aura mappage JPA -> DTO ( parcourir toutes les accountOperations )
     List<AccountOperationDTO> accountOperationDTOS = accountOperations.getContent().stream().map(op->dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());

    // Pour affecter les données relatifs de l'opération Bancaire à travers un setter
     accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
     // Pour récupérer le numéro de compte bancaire et l'affecter dans la liste accountHistoryDTO
     accountHistoryDTO.setAccountId(bankAccount.getId());
     // Pour récupérer les données relative au montant de l'opération et l'affecter à l'accountHistoryDTO
     accountHistoryDTO.setBalance(bankAccount.getBalance());
     // Pour donner la page en cours
     accountHistoryDTO.setCurrentPage(page);
     // Pour affecter la taille de la page
     accountHistoryDTO.setPageSize(size);
     // Pour récupérer et affecter le nombre total de pages
     accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
     // Résultat final de l'historique des opérations bancaires avec pagination et size
     return accountHistoryDTO;

    }

    // Cette méthode permet de rechercher des clients à travers un mot clé ( keyword )
    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        // La méthode prédéfinie de l'interface repository findByNameContains qui possède un
        // paramètre keyword permet de chercher à travers un mot clé et retourne le résultat
        // sous forme de listes customers
        List<Customer> customers=customerRepository.searchCustomer(keyword);
        // Dans cette ligne, on va mapper la listes customers ( JPA ) en une autre liste
        // appelée customerDTOS en utilisant stream() d'abord puis le mappage DTO (dtoMapper)
        List<CustomerDTO> customerDTOS=customers.stream().map(cust->dtoMapper.fromCustomer(cust)).collect(Collectors.toList());
        /*if (customerDTOS != null) {
            return customerDTOS;
        }
        else {
            return null;
        }
        */
        return customerDTOS;
    }
}
