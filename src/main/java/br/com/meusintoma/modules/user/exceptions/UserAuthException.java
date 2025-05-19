package br.com.meusintoma.modules.user.exceptions;

public class UserAuthException extends RuntimeException{
    public UserAuthException(){
        super("E-mail ou senha inválidos");
    }
}
