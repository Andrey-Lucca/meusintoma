package br.com.meusintoma.modules.consultation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.modules.calendar.exceptions.CalendarNotFoundException;
import br.com.meusintoma.modules.consultation.dto.ConsultationResponseDTO;
import br.com.meusintoma.modules.consultation.dto.CreateConsultationDTO;
import br.com.meusintoma.modules.consultation.service.ConsultationService;
import br.com.meusintoma.modules.patient.exceptions.PatientNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/consultation")
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;

    @PostMapping("/")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Object> createConsultation(@RequestBody CreateConsultationDTO consultationDto,
            HttpServletRequest request) {
        try {
            ConsultationResponseDTO consultation = consultationService
                    .createConsultation(consultationDto.getCalendarId());
            return ResponseEntity.status(201).body(consultation);
        } catch (CalendarNotFoundException e) {
            throw e;
        } catch (PatientNotFoundException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Algo deu errado");
        }
    }
}
