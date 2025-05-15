package br.com.meusintoma.exceptions.globalCustomException;

public class BadRequestException extends RuntimeException {
    public BadRequestException(){
        super("Algo deu errado");
    }
}
