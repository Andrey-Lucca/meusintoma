package br.com.meusintoma.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import br.com.meusintoma.exceptions.InvalidDateException;
import br.com.meusintoma.exceptions.CustomAccessDeniedException;
import br.com.meusintoma.exceptions.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDate(InvalidDateException ex) {
        ErrorResponse error = new ErrorResponse(
            "DATA_INVALIDA",
            ex.getMessage(),
            "Você tentou usar uma data anterior a data atual, e isso ocasionou o erro"
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleCustomAccessDenied(CustomAccessDeniedException ex) {
        ErrorResponse error = new ErrorResponse(
            "ACESSO_NEGADO",
            ex.getMessage(),
            null
        );
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
}