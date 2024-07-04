package tn.banque.springbootmvc.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tn.banque.springbootmvc.dtos.CustomerDTO;
import tn.banque.springbootmvc.entities.Customer;
import tn.banque.springbootmvc.exceptions.CustomerNotFoundException;
import tn.banque.springbootmvc.repositories.CustomerRepository;
import tn.banque.springbootmvc.services.BankAccountService;

import java.util.List;

@RestController
@AllArgsConstructor // pour injection des dépendances
@Slf4j
@CrossOrigin("*")
public class CustomerRestController {
    private BankAccountService bankAccountService;
    @GetMapping("/customers")
    public List<CustomerDTO> customers(){

        return bankAccountService.listCustomer();
    }
    @GetMapping("/customers/search")
    public List<CustomerDTO> searchCustomers(@RequestParam(name="keyword",defaultValue="") String keyword){

        return bankAccountService.searchCustomers("%" + keyword + "%");
    }
    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer(@PathVariable(name="id") Long customerId) throws CustomerNotFoundException {
        return bankAccountService.getCustomer(customerId);
    }
    @PostMapping("/customers")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        return bankAccountService.saveCustomer(customerDTO);
    }

    @PutMapping("/customers/{customerId}")
    public CustomerDTO updateCustomer(@PathVariable Long customerId, @RequestBody CustomerDTO customerDTO)
    {
        customerDTO.setId(customerId);
        return bankAccountService.updateCustomer(customerDTO);
    }
    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable Long id) throws CustomerNotFoundException {
        bankAccountService.deleteCustomer(id);
    }


}
