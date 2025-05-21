package br.com.meusintoma.modules.calendar.exceptions;

public class CalendarInvalidDateException extends RuntimeException {
    public CalendarInvalidDateException(String message){
        super(message);
    }
}
