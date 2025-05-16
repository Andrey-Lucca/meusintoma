package br.com.meusintoma.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.meusintoma.exceptions.globalCustomException.ErrorResponse;
import br.com.meusintoma.modules.email.exception.InvalidTokenException;
import br.com.meusintoma.modules.email.exception.TokenGenerationException;

@ControllerAdvice
public class EmailExceptionHandler {

    @ExceptionHandler(TokenGenerationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenGeneration(TokenGenerationException ex) {
        ErrorResponse error = new ErrorResponse(
                "CREATE_EMAIL_EXCEPTION",
                ex.getMessage(),
                "Não foi possível criar o envio de e-mail, tente novamente mais tarde");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidToken(InvalidTokenException ex) {
        ErrorResponse error = new ErrorResponse(
                "INVALID_TOKEN_EXCEPTION",
                ex.getMessage(),
                "Token inválido");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
