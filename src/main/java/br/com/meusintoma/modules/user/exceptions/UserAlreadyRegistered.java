package br.com.meusintoma.modules.user.exceptions;

public class UserAlreadyRegistered extends RuntimeException {
    public UserAlreadyRegistered(String message){
        super(message);
    }
}
