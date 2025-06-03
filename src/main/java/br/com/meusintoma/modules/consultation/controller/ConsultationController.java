package br.com.meusintoma.modules.consultation.controller;

import java.util.List;
import java.util.UUID;

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

import br.com.meusintoma.modules.consultation.dto.ChangeConsultationStatusDTO;
import br.com.meusintoma.modules.consultation.dto.ConsultationByDoctorPatientDTO;
import br.com.meusintoma.modules.consultation.dto.ConsultationCanceledResponseDTO;
import br.com.meusintoma.modules.consultation.dto.ConsultationResponseDTO;
import br.com.meusintoma.modules.consultation.dto.CreateConsultationDTO;
import br.com.meusintoma.modules.consultation.dto.RescheduleConsultationDTO;
import br.com.meusintoma.modules.consultation.services.ConsultationRelationshipService;
import br.com.meusintoma.modules.consultation.services.ConsultationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/consultation")
@RequiredArgsConstructor

public class ConsultationController {

    private final ConsultationService consultationService;

    private final ConsultationRelationshipService consultationRelationshipService;

    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Object> createConsultation(@RequestBody CreateConsultationDTO consultationDto) {
        ConsultationResponseDTO consultation = consultationService
                .createConsultation(consultationDto.getCalendarId(), consultationDto.getHealthPlan());
        return ResponseEntity.status(201).body(consultation);
    }

    @GetMapping
    public ResponseEntity<Object> getConsultations() {
        List<ConsultationResponseDTO> consultations = consultationService.getConsultations();
        return ResponseEntity.ok().body(consultations);
    }

    @GetMapping("/doctor/{doctorId}/patient/{patientId}")
    @PreAuthorize("hasRole('PATIENT') || hasRole('DOCTOR')")
    public ResponseEntity<Object> getConsultationByDoctorAndPatient(@PathVariable UUID doctorId,
            @PathVariable UUID patientId) {
        List<ConsultationByDoctorPatientDTO> consultations = consultationRelationshipService
                .getConsultationsByDoctorAndPatient(doctorId, patientId);
        return ResponseEntity.ok().body(consultations);
    }

    @PatchMapping("/{consultationId}")
    public ResponseEntity<Object> changeConsultationStatus(@PathVariable UUID consultationId,
            @RequestBody ChangeConsultationStatusDTO statusDTO) {
        ConsultationResponseDTO updated = consultationService.changeConsultationStatus(consultationId,
                statusDTO.getStatus());
        return ResponseEntity.ok().body(updated);
    }

    @PutMapping("/{consultationId}/reschedule")
    public ResponseEntity<Object> rescheduleConsultation(@PathVariable UUID consultationId,
            @RequestBody RescheduleConsultationDTO rescheduleDTO) {
        ConsultationResponseDTO updated = consultationService.reschedule(consultationId, rescheduleDTO);
        return ResponseEntity.ok().body(updated);
    }

    @DeleteMapping("/{consultationId}")
    public ResponseEntity<Object> cancelConsultation(@PathVariable UUID consultationId) {
        ConsultationCanceledResponseDTO consultation = consultationService.cancelConsultation(consultationId);
        return ResponseEntity.ok().body(consultation);
    }
}
