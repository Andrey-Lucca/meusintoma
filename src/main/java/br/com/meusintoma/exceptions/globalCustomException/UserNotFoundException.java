package br.com.meusintoma.exceptions.globalCustomException;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("E-mail not found");
    }
}
