package br.com.meusintoma.modules.consultation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.exceptions.globalCustomException.CustomAccessDeniedException;
import br.com.meusintoma.exceptions.globalCustomException.NoContentException;
import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.exceptions.globalCustomException.UnalterableException;
import br.com.meusintoma.modules.calendar.exceptions.CalendarNotFoundException;
import br.com.meusintoma.modules.calendar.exceptions.UnavaliableTimeException;
import br.com.meusintoma.modules.consultation.dto.ChangeConsultationStatusDTO;
import br.com.meusintoma.modules.consultation.dto.ConsultationByDoctorPatientDTO;
import br.com.meusintoma.modules.consultation.dto.ConsultationCanceledResponseDTO;
import br.com.meusintoma.modules.consultation.dto.ConsultationResponseDTO;
import br.com.meusintoma.modules.consultation.dto.CreateConsultationDTO;
import br.com.meusintoma.modules.consultation.dto.RescheduleConsultationDTO;
import br.com.meusintoma.modules.consultation.exceptions.AlreadyHaveConsultationException;
import br.com.meusintoma.modules.consultation.services.ConsultationRelationshipService;
import br.com.meusintoma.modules.consultation.services.ConsultationService;
import br.com.meusintoma.modules.patient.exceptions.PatientNotFoundException;

@RestController
@RequestMapping("/consultation")
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;

    @Autowired
    private ConsultationRelationshipService consultationRelationshipService;

    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Object> createConsultation(@RequestBody CreateConsultationDTO consultationDto) {
        try {
            ConsultationResponseDTO consultation = consultationService
                    .createConsultation(consultationDto.getCalendarId());
            return ResponseEntity.status(201).body(consultation);
        } catch (UnavaliableTimeException e) {
            throw e;
        } catch (CalendarNotFoundException e) {
            throw e;
        } catch (PatientNotFoundException e) {
            throw e;
        } catch (AlreadyHaveConsultationException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Algo deu errado ao criar as consultas");
        }
    }

    @GetMapping
    public ResponseEntity<Object> getConsultations() {
        try {
            List<ConsultationResponseDTO> consultations = consultationService.getConsultations();
            return ResponseEntity.ok().body(consultations);
        } catch (NoContentException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Algo deu errado ao exibir as consultas");
        }
    }

    @GetMapping("/doctor/{doctorId}/patient/{patientId}")
    @PreAuthorize("hasRole('PATIENT') || hasRole('DOCTOR')")
    public ResponseEntity<Object> getConsultationByDoctorAndPatient(@PathVariable UUID doctorId,
            @PathVariable UUID patientId) {
        try {
            List<ConsultationByDoctorPatientDTO> consultations = consultationRelationshipService.getConsultationsByDoctorAndPatient(doctorId, patientId);
            return ResponseEntity.ok().body(consultations);
        } catch (NoContentException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Algo deu errado ao exibir as consultas");
        }
    }

    @PatchMapping("/{consultationId}")
    public ResponseEntity<Object> changeConsultationStatus(@PathVariable UUID consultationId,
            @RequestBody ChangeConsultationStatusDTO statusDTO) {
        try {
            ConsultationResponseDTO updated = consultationService.changeConsultationStatus(consultationId,
                    statusDTO.getStatus());
            return ResponseEntity.ok().body(updated);
        } catch (NotFoundException | CustomAccessDeniedException e) {
            throw e;
        } catch (UnalterableException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Algo deu errado ao cancelar a consulta");
        }
    }

    @PutMapping("/{consultationId}/reschedule")
    public ResponseEntity<Object> rescheduleConsultation(@PathVariable UUID consultationId,
            @RequestBody RescheduleConsultationDTO rescheduleDTO) {
        try {
            ConsultationResponseDTO updated = consultationService.reschedule(consultationId, rescheduleDTO);
            return ResponseEntity.ok().body(updated);
        } catch (NotFoundException | CustomAccessDeniedException e) {
            throw e;
        } catch (UnalterableException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Algo deu errado ao cancelar a consulta");
        }
    }

    @DeleteMapping("/{consultationId}")
    public ResponseEntity<Object> cancelConsultation(@PathVariable UUID consultationId) {
        try {
            ConsultationCanceledResponseDTO consultation = consultationService.cancelConsultation(consultationId);
            return ResponseEntity.ok().body(consultation);
        } catch (NotFoundException | CustomAccessDeniedException e) {
            throw e;
        } catch (UnalterableException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Algo deu errado ao cancelar a consulta");
        }
    }
}
