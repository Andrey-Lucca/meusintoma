package br.com.meusintoma.exceptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("Usuário não autenticado");
    }
}
