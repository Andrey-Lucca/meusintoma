package br.com.meusintoma.exceptions.globalCustomException;

public class InvalidDateException extends RuntimeException {
    public InvalidDateException(){
        super("A data fornecida tem que ser igual ou maior do que a data atual");
    }
}
