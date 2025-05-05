package br.com.meusintoma.modules.doctor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.exceptions.globalCustomException.NoContentException;
import br.com.meusintoma.modules.doctor.dto.DoctorRelationshipDTO;
import br.com.meusintoma.modules.doctor.services.DoctorRelationshipService;

@RestController
@RequestMapping("/doctor")
public class DoctorRelationshipController {

    @Autowired
    DoctorRelationshipService doctorRelationshipService;

    @GetMapping("/relationship")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Object> getDoctorsPatient() {
        try {
            List<DoctorRelationshipDTO> patients = doctorRelationshipService.findRelationships();
            return ResponseEntity.ok().body(patients);
        } catch (NoContentException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Something goes wrong to show doctors");
        }
    }
}
