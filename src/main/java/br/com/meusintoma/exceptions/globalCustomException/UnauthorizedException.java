package br.com.meusintoma.exceptions.globalCustomException;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("Usuário não autenticado");
    }
}
