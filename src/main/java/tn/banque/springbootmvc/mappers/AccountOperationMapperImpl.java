package tn.banque.springbootmvc.mappers;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tn.banque.springbootmvc.dtos.BankAccountDTO;
import tn.banque.springbootmvc.entities.BankAccount;

@Service
public class AccountOperationMapperImpl {
    public BankAccountDTO fromBankAccount(BankAccount bankAccount) {
        BankAccountDTO bankAccountDTO = new BankAccountDTO();
        BeanUtils.copyProperties(bankAccount, bankAccountDTO);
        return bankAccountDTO;
    }

    public BankAccount fromBankAccountDTO(BankAccountDTO bankAccountDTO) {
        BankAccount bankAccount = new BankAccount();
        BeanUtils.copyProperties(bankAccountDTO, bankAccount);
        return bankAccount;
    }
}
