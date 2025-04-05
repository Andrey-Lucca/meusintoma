package br.com.meusintoma.modules.calendar.exceptions;

public class NoDoctorCalendarException extends RuntimeException{
    public NoDoctorCalendarException(String message){
        super(message);
    }
}
