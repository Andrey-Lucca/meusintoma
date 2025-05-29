package br.com.meusintoma.modules.doctorPatient.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.exceptions.globalCustomException.CustomAccessDeniedException;
import br.com.meusintoma.exceptions.globalCustomException.NoContentException;
import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.doctorPatient.dto.ChangeInviteStatusDTO;
import br.com.meusintoma.modules.doctorPatient.dto.DoctorPatientInviteDTO;
import br.com.meusintoma.modules.doctorPatient.dto.DoctorPatientInviteResponseDTO;
import br.com.meusintoma.modules.doctorPatient.exceptions.DoctorPatientDuplicatedInviteException;
import br.com.meusintoma.modules.doctorPatient.exceptions.DoctorPatientNotValidStatusException;
import br.com.meusintoma.modules.doctorPatient.services.DoctorPatientInvitationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("invitation")
@RequiredArgsConstructor
public class DoctorPatientInvitationController {

    private final DoctorPatientInvitationService doctorPatientInvitationService;

    @PostMapping("patient/{patientId}/doctor/{doctorId}")
    @PreAuthorize("hasRole('DOCTOR') || hasRole('SECRETARY')")
    public ResponseEntity<Object> invitePatient(@PathVariable UUID patientId, @PathVariable UUID doctorId) {
        try {
            DoctorPatientInviteDTO createdInvite = doctorPatientInvitationService.invitePatient(patientId, doctorId);
            return ResponseEntity.status(201).body(createdInvite);
        } catch (NotFoundException e) {
            throw e;
        } catch (DoctorPatientDuplicatedInviteException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Something goes wrong on create the invite");
        }
    }

    @GetMapping("invites")
    @PreAuthorize("hasRole('DOCTOR') || hasRole('PATIENT')")
    public ResponseEntity<Object> getAllInvites() {
        try {
            List<DoctorPatientInviteResponseDTO> invites = doctorPatientInvitationService.getAllInvites();
            return ResponseEntity.status(200).body(invites);
        } catch (NoContentException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Something goes wrong on create the invite");
        }
    }

    @GetMapping("invites/secretary")
    @PreAuthorize("hasRole('SECRETARY')")
    public ResponseEntity<Object> getAllInvitesBySecretary(@RequestParam UUID doctorId) {
        try {
            List<DoctorPatientInviteResponseDTO> invites = doctorPatientInvitationService
                    .getAllInvitesBySecretary(doctorId);
            return ResponseEntity.status(200).body(invites);
        } catch (CustomAccessDeniedException e) {
            throw e;
        } catch (NoContentException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Something goes wrong on create the invite");
        }
    }

    @PatchMapping("/{inviteId}/patient-status")
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
        } catch (DoctorPatientNotValidStatusException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Something goes wrong on update the invite");
        }
    }

    @PatchMapping("/{inviteId}/doctor-status")
    @PreAuthorize("hasRole('DOCTOR') || hasRole('SECRETARY')")
    public ResponseEntity<Object> disassociateByDoctor(@PathVariable UUID inviteId,
            @RequestBody ChangeInviteStatusDTO statusDTO) {
        try {
            DoctorPatientInviteDTO updatedInvite = doctorPatientInvitationService.disassociateByDoctor(inviteId,
                    statusDTO);
            return ResponseEntity.ok().body(updatedInvite);
        } catch (CustomAccessDeniedException e) {
            throw e;
        } catch (NotFoundException e) {
            throw e;
        } catch (DoctorPatientNotValidStatusException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Something goes wrong on update the invite");
        }
    }
}
