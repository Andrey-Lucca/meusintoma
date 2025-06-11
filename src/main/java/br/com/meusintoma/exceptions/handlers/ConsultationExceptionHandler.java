package br.com.meusintoma.exceptions.handlers;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.meusintoma.exceptions.globalCustomException.ErrorResponse;
import br.com.meusintoma.modules.consultation.exceptions.AlreadyHaveConsultationException;

@ControllerAdvice
@Order(1)
public class ConsultationExceptionHandler {

    @ExceptionHandler(AlreadyHaveConsultationException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyHaveConsultation(AlreadyHaveConsultationException ex) {
        ErrorResponse error = new ErrorResponse(
                "ALREADY_HAVE_CONSULTATION",
                ex.getMessage(),
                "Você já possui uma consulta marcada para o médico em questão");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}
