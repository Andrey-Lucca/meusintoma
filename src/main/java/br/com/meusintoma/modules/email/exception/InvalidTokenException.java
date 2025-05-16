package br.com.meusintoma.modules.email.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message){
        super(message);
    }
}
