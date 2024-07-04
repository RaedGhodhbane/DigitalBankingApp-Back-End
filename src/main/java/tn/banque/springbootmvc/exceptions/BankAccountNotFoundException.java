package tn.banque.springbootmvc.exceptions;

public class BankAccountNotFoundException extends Exception{
    public BankAccountNotFoundException(String message) {
        super(message);
    }
}
