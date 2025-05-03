package br.com.meusintoma.modules.consultation.exceptions;

public class AlreadyHaveConsultationException extends RuntimeException {
    public AlreadyHaveConsultationException(String message){
        super(message);
    }
}
