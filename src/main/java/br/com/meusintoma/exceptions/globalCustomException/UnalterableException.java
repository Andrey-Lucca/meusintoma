package br.com.meusintoma.exceptions.globalCustomException;

public class UnalterableException extends RuntimeException {
    public UnalterableException(String type) {
        super(type + " Não pode ser alterada");
    }
}
