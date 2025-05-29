package br.com.meusintoma.modules.patient.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.exceptions.globalCustomException.NoContentException;
import br.com.meusintoma.modules.patient.dto.PatientRelationshipDTO;
import br.com.meusintoma.modules.patient.services.PatientRelationshipService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor

public class PatientRelationshipController {

    private final PatientRelationshipService patientRelationshipService;

    @GetMapping("/relationship")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Object> getDoctorsPatient() {
        try {
            List<PatientRelationshipDTO> doctors = patientRelationshipService.findRelationships();
            return ResponseEntity.ok().body(doctors);
        } catch (NoContentException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Something goes wrong to show doctors");
        }
    }
}
