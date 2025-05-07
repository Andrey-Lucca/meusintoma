package br.com.meusintoma.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.meusintoma.exceptions.globalCustomException.CustomAccessDeniedException;
import br.com.meusintoma.exceptions.globalCustomException.ErrorResponse;
import br.com.meusintoma.exceptions.globalCustomException.InvalidDateException;
import br.com.meusintoma.exceptions.globalCustomException.NoContentException;
import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.exceptions.globalCustomException.UnalterableException;
import br.com.meusintoma.exceptions.globalCustomException.ForbiddenException;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDate(InvalidDateException ex) {
        ErrorResponse error = new ErrorResponse(
                "INVALIDE_DATE",
                ex.getMessage(),
                "Você tentou usar uma data anterior a data atual, e isso ocasionou o erro");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleCustomAccessDenied(CustomAccessDeniedException ex) {
        ErrorResponse error = new ErrorResponse(
                "DENIED_ACESS",
                ex.getMessage(),
                null);
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<ErrorResponse> handleNoContent(NoContentException ex) {
        ErrorResponse error = new ErrorResponse(
                "NO_CONTENT",
                ex.getMessage(),
                "Não existe nada a ser exibido");
        return new ResponseEntity<>(error, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFound(NotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                "ITEM_NOT_FOUND",
                ex.getMessage(),
                "Não foi possível encontrar o item solicitado");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnalterableException.class)
    public ResponseEntity<ErrorResponse> unalterableException(UnalterableException ex) {
        ErrorResponse error = new ErrorResponse(
                "ITEM_NOT_ALTERABLE",
                ex.getMessage(),
                "Não foi possível alterar este item, verifique sua condição");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> forbiddenException(ForbiddenException ex) {
        ErrorResponse error = new ErrorResponse(
                "FORBIDDEN",
                ex.getMessage(),
                "Você não pode realizar esta ação");
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
}