package br.com.meusintoma.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import br.com.meusintoma.modules.patient.exceptions.PatientNotFoundException;
import br.com.meusintoma.exceptions.ErrorResponse;

@ControllerAdvice
public class UsersNotFoundHandler {

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePatientNotFoundEntity(PatientNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                "PATIENT_NOT_FOUND",
                ex.getMessage(),
                "Não foi possível encontrar o paciente");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }
}