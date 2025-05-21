package br.com.meusintoma.exceptions.globalCustomException;

public class InvalidDateException extends RuntimeException {
    public InvalidDateException(String message){
        super(message);
    }
}
