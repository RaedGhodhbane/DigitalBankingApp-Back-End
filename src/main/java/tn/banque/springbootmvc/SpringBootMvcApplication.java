package tn.banque.springbootmvc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tn.banque.springbootmvc.dtos.BankAccountDTO;
import tn.banque.springbootmvc.dtos.CurrentBankAccountDTO;
import tn.banque.springbootmvc.dtos.CustomerDTO;
import tn.banque.springbootmvc.dtos.SavingBankAccountDTO;
import tn.banque.springbootmvc.entities.*;
import tn.banque.springbootmvc.enums.AccountStatus;
import tn.banque.springbootmvc.enums.OperationType;
import tn.banque.springbootmvc.exceptions.BalanceNotSufficientException;
import tn.banque.springbootmvc.exceptions.BankAccountNotFoundException;
import tn.banque.springbootmvc.exceptions.CustomerNotFoundException;
import tn.banque.springbootmvc.repositories.AccountOperationRepository;
import tn.banque.springbootmvc.repositories.BankAccountRepository;
import tn.banque.springbootmvc.repositories.CustomerRepository;
import tn.banque.springbootmvc.services.BankAccountService;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class SpringBootMvcApplication {

    public static void main(String[] args) {

        SpringApplication.run(SpringBootMvcApplication.class, args);
    }
    //test de l'application (le mappage ) avec implémentation des services
    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
        return args->{
            Stream.of("Hassen","Yassine","Aicha").forEach(name->{
                CustomerDTO customer = new CustomerDTO();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");

                bankAccountService.saveCustomer(customer);
        });
            bankAccountService.listCustomer().forEach(customer->{
                try {

                    bankAccountService.saveCurrentBankAccount(Math.random()*90000,9000,customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random()*120000,5.5, customer.getId());




                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });
            List<BankAccountDTO> bankAccounts = bankAccountService.bankAccountList();
            for (BankAccountDTO bankAccount: bankAccounts) {
                for(int i=0 ; i<10 ; i++)
                {
                    String accountId;
                    if (bankAccount instanceof SavingBankAccountDTO)
                    {
                        accountId=((SavingBankAccountDTO) bankAccount).getId();
                    }else{
                        accountId=((CurrentBankAccountDTO) bankAccount).getId();

                    }
                    bankAccountService.credit(accountId, 10000+Math.random()*120000,"Credit");
                    bankAccountService.debit(accountId, 1000+Math.random()*9000,"Debit");
                }

            }
        };
    }
    //@Bean test de l'application sans implémentation des services
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository ){
        return args ->{
            Stream.of("Hassen","Yassine","Aicha").forEach(name->{
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(cust->{
                //création compte courant
                CurrentAccount currentAccount = new CurrentAccount();
                // affectation manuelle
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*90000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(cust);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);
                //création compte épargne
                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*90000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(cust);
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);

            });
            bankAccountRepository.findAll().forEach(acc->{
                for(int i = 0 ; i<5 ; i++){
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random()*12000);
                    accountOperation.setType(Math.random()>0.5? OperationType.DEBIT: OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);

                }
            });

        };
    }

}
