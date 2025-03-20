package br.com.meusintoma.exceptions;

public class UserFoundException extends RuntimeException {
    public UserFoundException() {
        super("The email provided is already registered");
    }
}
