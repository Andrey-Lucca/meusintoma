package br.com.meusintoma.exceptions.globalCustomException;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message){
        super(message);
    }
}
