package br.com.meusintoma.modules.calendar.exceptions;

public class CalendarNotFoundException extends RuntimeException {
    public CalendarNotFoundException(String message){
        super(message);
    }
}
