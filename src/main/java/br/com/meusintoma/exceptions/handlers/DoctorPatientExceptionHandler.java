package br.com.meusintoma.exceptions.handlers;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.meusintoma.exceptions.globalCustomException.ErrorResponse;
import br.com.meusintoma.modules.doctorPatient.exceptions.DoctorPatientNotValidStatusException;
import br.com.meusintoma.modules.doctorPatient.exceptions.DoctorPatientDuplicatedInviteException;

@ControllerAdvice
@Order(1)
public class DoctorPatientExceptionHandler {
    @ExceptionHandler(DoctorPatientNotValidStatusException.class)
    public ResponseEntity<ErrorResponse> handleNotValidStatus(DoctorPatientNotValidStatusException ex) {
        ErrorResponse error = new ErrorResponse(
                "NOT_VALID_STATUS",
                ex.getMessage(),
                "Você tentou inserir um status indisponível");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DoctorPatientDuplicatedInviteException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatedInvite(DoctorPatientDuplicatedInviteException ex) {
        ErrorResponse error = new ErrorResponse(
                "DUPLICATED_INVITE",
                ex.getMessage(),
                "Você tentou enviar o convite mais de uma vez enquanto ele está ativo");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}
