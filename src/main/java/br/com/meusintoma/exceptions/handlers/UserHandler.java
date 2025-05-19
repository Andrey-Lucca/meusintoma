package br.com.meusintoma.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.meusintoma.exceptions.globalCustomException.ErrorResponse;
import br.com.meusintoma.modules.patient.exceptions.PatientNotFoundException;
import br.com.meusintoma.modules.user.exceptions.UserAlreadyRegistered;
import br.com.meusintoma.modules.user.exceptions.UserAuthException;

@ControllerAdvice
public class UserHandler {

    // remover de lugar depois
    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePatientNotFoundEntity(PatientNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                "PATIENT_NOT_FOUND",
                ex.getMessage(),
                "Não foi possível encontrar o paciente");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(UserAuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthUserException(UserAuthException ex) {
        ErrorResponse error = new ErrorResponse(
                "AUTH_ERROR",
                ex.getMessage(),
                "Não foi possível encontrar este usuário, verifique seu e-mail ou senha");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyRegistered.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyRegistered(UserAlreadyRegistered ex) {
        ErrorResponse error = new ErrorResponse(
                "PATIENT_ALREADY_REGISTERED",
                ex.getMessage(),
                "Já existe algum usuário associado a esse e-mail");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}