package tn.banque.springbootmvc.mappers;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tn.banque.springbootmvc.dtos.AccountOperationDTO;
import tn.banque.springbootmvc.dtos.CurrentBankAccountDTO;
import tn.banque.springbootmvc.dtos.CustomerDTO;
import tn.banque.springbootmvc.dtos.SavingBankAccountDTO;
import tn.banque.springbootmvc.entities.AccountOperation;
import tn.banque.springbootmvc.entities.CurrentAccount;
import tn.banque.springbootmvc.entities.Customer;
import tn.banque.springbootmvc.entities.SavingAccount;

// Pour faire le mappage JPAEntity vers DTOEntity et vise vers ça, on va créer un package
// nommé mappers pour implémenter le mappage JPAEntity vers DTOENtity

//MapStruct framework permettant le mappage dto JpaEntity
@Service
// @Service car on va faire un service de mappage : obligatoire
public class BankAccountMapperImpl {
    //BankAccountMapperImpl dans cette classe on a créé 2 méthodes
    //1/ fromCustomer : est une méthode qui permet de mapper l'entité JPA customer afin de
    // retourner un objet de type CustomerDTO
    // copyProperties permet de récupérer juste les attributs de l'entité JPA (id, name et email) dans cet exemple
    public CustomerDTO fromCustomer(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        // 1ère méthode : En utilisant BeanUtils.copyProperties
        // BeanUtils permet de créer un objet qui contient les datas
        BeanUtils.copyProperties(customer,customerDTO);
        // BeanUtils.copyProperties(customer,customerDTO) : le sens de mappage va se faire du customer -> customerDTO
        // 2ème méthode : En utilisant les getters et setters

        /*
        customerDTO est le DTO (modèle) et customer est le JPA (classe Java)
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setEmail(customer.getEmail());
         */
        return customerDTO;

        // retour JPA
    }
    public Customer fromCustomerDTO(CustomerDTO customerDTO){
        Customer customer = new Customer();
        // MAPPAGE DE MANIERE DYNAMIQUE dto to jpaEntity
        BeanUtils.copyProperties(customerDTO,customer);
        // MAPPAGE DE MANIERE STATIQUE : l'implémentation se fait à travers le programmeur
        /*customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setName(customer.getEmail());
         */
        return customer;
    }

public SavingBankAccountDTO fromSavingBankAccount(SavingAccount savingAccount)
{
    SavingBankAccountDTO savingBankAccountDTO=new SavingBankAccountDTO();
    BeanUtils.copyProperties(savingAccount,savingBankAccountDTO);
    savingBankAccountDTO.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
    savingBankAccountDTO.setType(savingAccount.getClass().getSimpleName());
    return savingBankAccountDTO;
}
public SavingAccount fromSavingBankAccountDTO(SavingBankAccountDTO savingBankAccountDTO)
    {
        SavingAccount savingAccount=new SavingAccount();
        BeanUtils.copyProperties(savingBankAccountDTO,savingAccount);
        savingAccount.setCustomer(fromCustomerDTO(savingBankAccountDTO.getCustomerDTO()));
        return savingAccount;
    }
    public CurrentBankAccountDTO fromCurrentBankAccount(CurrentAccount currentAccount)
    {
        CurrentBankAccountDTO currentBankAccountDTO=new CurrentBankAccountDTO();
        BeanUtils.copyProperties(currentAccount,currentBankAccountDTO);
        currentBankAccountDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
        currentBankAccountDTO.setType(currentAccount.getClass().getSimpleName());
        return currentBankAccountDTO;
    }
    public CurrentAccount fromCurrentBankAccountDTO(CurrentBankAccountDTO currentBankAccountDTO)
    {
        CurrentAccount currentAccount=new CurrentAccount();
        BeanUtils.copyProperties(currentBankAccountDTO,currentAccount);
        currentAccount.setCustomer(fromCustomerDTO(currentBankAccountDTO.getCustomerDTO()));
        return currentAccount;

    }
    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation)
    {
        AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation,accountOperationDTO);
        return accountOperationDTO;
    }
}
