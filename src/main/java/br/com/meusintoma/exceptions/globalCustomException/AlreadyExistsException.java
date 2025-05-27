package br.com.meusintoma.exceptions.globalCustomException;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message + " Já existe no sistema");
    }
}
