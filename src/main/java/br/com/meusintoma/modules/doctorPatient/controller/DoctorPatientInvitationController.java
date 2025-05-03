package br.com.meusintoma.modules.doctorPatient.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.exceptions.globalCustomException.CustomAccessDeniedException;
import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.doctorPatient.dto.ChangeInviteStatusDTO;
import br.com.meusintoma.modules.doctorPatient.dto.DoctorPatientInviteDTO;
import br.com.meusintoma.modules.doctorPatient.services.DoctorPatientInvitationService;

@RestController
@RequestMapping("invitation")
public class DoctorPatientInvitationController {

    @Autowired
    DoctorPatientInvitationService doctorPatientInvitationService;

    @PostMapping("/{patientId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Object> invitePatient(@PathVariable UUID patientId) {
        try {
            DoctorPatientInviteDTO createdInvite = doctorPatientInvitationService.invitePatient(patientId);
            return ResponseEntity.status(201).body(createdInvite);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Something goes wrong on create the invite");
        }
    }

    @PatchMapping("/{inviteId}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Object> respondToInvite(@PathVariable UUID inviteId,
            @RequestBody ChangeInviteStatusDTO statusDTO) {
        try {
            DoctorPatientInviteDTO updatedInvite = doctorPatientInvitationService.changeStatus(inviteId, statusDTO);
            return ResponseEntity.ok().body(updatedInvite);
        } catch (CustomAccessDeniedException e) {
            throw e;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Something goes wrong on update the invite");
        }
    }
}
