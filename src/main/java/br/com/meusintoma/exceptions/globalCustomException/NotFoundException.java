package br.com.meusintoma.exceptions.globalCustomException;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String typeException) {
        super(typeException + " não encontrado");
    }
}
