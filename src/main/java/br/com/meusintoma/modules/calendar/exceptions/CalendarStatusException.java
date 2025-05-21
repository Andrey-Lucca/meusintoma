package br.com.meusintoma.modules.calendar.exceptions;

public class CalendarStatusException extends RuntimeException{
    public CalendarStatusException(){
        super("O status do calendário se encontra bloqueado ou indisponível, não sendo possível fazer alterações no momento");
    }
}
