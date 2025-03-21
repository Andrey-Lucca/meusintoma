package br.com.meusintoma.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("E-mail not found");
    }
}
