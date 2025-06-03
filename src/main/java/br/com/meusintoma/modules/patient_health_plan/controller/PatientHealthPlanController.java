package br.com.meusintoma.modules.patient_health_plan.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.modules.patient_health_plan.dto.PatientHealthPlanAssociationResultDTO;
import br.com.meusintoma.modules.patient_health_plan.dto.PatientHealthPlanRequestDTO;
import br.com.meusintoma.modules.patient_health_plan.dto.PatientHealthPlanResponseDTO;
import br.com.meusintoma.modules.patient_health_plan.services.PatientHealthPlanService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/patient-healthplan")
@RequiredArgsConstructor
public class PatientHealthPlanController {

    private final PatientHealthPlanService patientHealthPlanService;

    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Object> associate(@RequestBody PatientHealthPlanRequestDTO patientHealthPlanRequest) {
        List<PatientHealthPlanAssociationResultDTO> results = patientHealthPlanService
                .associate(patientHealthPlanRequest);
        return ResponseEntity.status(201).body(results);
    }

    @GetMapping("/plans/{patientId}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Object> getAssociations(@PathVariable UUID patientId) {
        List<PatientHealthPlanResponseDTO> associations = patientHealthPlanService.getPatientHealthPlans(patientId);
        return ResponseEntity.status(200).body(associations);
    }

    @DeleteMapping("/disassociate/patient-plans/{patientHealthPlanId}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Object> disassociate(@PathVariable UUID patientHealthPlanId) {
        PatientHealthPlanResponseDTO deletedAssociation = patientHealthPlanService
                .disassociate(patientHealthPlanId);
        return ResponseEntity.status(200).body(deletedAssociation);
    }
}
