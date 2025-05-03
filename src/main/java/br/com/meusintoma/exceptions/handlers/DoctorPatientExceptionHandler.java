package br.com.meusintoma.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.meusintoma.exceptions.globalCustomException.ErrorResponse;
import br.com.meusintoma.modules.doctorPatient.exceptions.DoctorPatientNotValidStatusException;

@ControllerAdvice
public class DoctorPatientExceptionHandler {
    @ExceptionHandler(DoctorPatientNotValidStatusException.class)
    public ResponseEntity<ErrorResponse> handleNotValidStatus(DoctorPatientNotValidStatusException ex) {
        ErrorResponse error = new ErrorResponse(
                "NOT_VALID_STATUS",
                ex.getMessage(),
                "Você tentou inserir um status indisponível");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}
