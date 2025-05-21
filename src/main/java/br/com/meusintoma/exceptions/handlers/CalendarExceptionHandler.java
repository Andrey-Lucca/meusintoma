package br.com.meusintoma.exceptions.handlers;

import br.com.meusintoma.modules.calendar.exceptions.NoDoctorCalendarException;
import br.com.meusintoma.exceptions.globalCustomException.ErrorResponse;
import br.com.meusintoma.modules.calendar.exceptions.CalendarInvalidDateException;
import br.com.meusintoma.modules.calendar.exceptions.CalendarNotFoundException;
import br.com.meusintoma.modules.calendar.exceptions.UnavaliableTimeException;
import br.com.meusintoma.modules.calendar.exceptions.CalendarStatusException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CalendarExceptionHandler {
    @ExceptionHandler(NoDoctorCalendarException.class)
    public ResponseEntity<ErrorResponse> handleNoDoctorCalendar(NoDoctorCalendarException ex) {
        ErrorResponse error = new ErrorResponse(
                "CALENDAR_NOT_FOUND",
                ex.getMessage(),
                "O doutor em questão ainda não possui nenhum calendário cadastrado");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CalendarNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCalendarNotFound(CalendarNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                "CALENDAR_TIME_NOT_FOUND",
                ex.getMessage(),
                "Não foi possível excluir esse horário pois não existe no sistema");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnavaliableTimeException.class)
    public ResponseEntity<ErrorResponse> handleUnavaliableTime(UnavaliableTimeException ex) {
        ErrorResponse error = new ErrorResponse(
                "UNAVALIABLE_TIME",
                ex.getMessage(),
                "Horário Indisponível");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CalendarStatusException.class)
    public ResponseEntity<ErrorResponse> handleStatusException(CalendarStatusException ex) {
        ErrorResponse error = new ErrorResponse(
                "UNALTERABLE_CALENDAR",
                ex.getMessage(),
                "Alterações bloqueadas");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CalendarInvalidDateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDate(CalendarInvalidDateException ex) {
        ErrorResponse error = new ErrorResponse(
                "INVALID_DATE",
                ex.getMessage(),
                "Data inválida");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}
