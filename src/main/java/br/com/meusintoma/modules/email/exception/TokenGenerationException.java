package br.com.meusintoma.modules.email.exception;

public class TokenGenerationException extends RuntimeException {
    public TokenGenerationException(){
        super("Não foi possível criar o envio do e-mail. Tenta novamente mais tarde");
    }
}
