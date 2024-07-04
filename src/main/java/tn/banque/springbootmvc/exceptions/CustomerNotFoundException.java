package tn.banque.springbootmvc.exceptions;
//création d'une exception surveillée (throws or try catch)
public class CustomerNotFoundException extends Exception{
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
