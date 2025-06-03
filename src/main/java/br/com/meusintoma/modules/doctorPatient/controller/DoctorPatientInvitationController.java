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

import br.com.meusintoma.modules.doctorPatient.dto.ChangeInviteStatusDTO;
import br.com.meusintoma.modules.doctorPatient.dto.DoctorPatientInviteDTO;
import br.com.meusintoma.modules.doctorPatient.dto.DoctorPatientInviteResponseDTO;

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
        DoctorPatientInviteDTO createdInvite = doctorPatientInvitationService.invitePatient(patientId, doctorId);
        return ResponseEntity.status(201).body(createdInvite);
    }

    @GetMapping("invites")
    @PreAuthorize("hasRole('DOCTOR') || hasRole('PATIENT')")
    public ResponseEntity<Object> getAllInvites() {
        List<DoctorPatientInviteResponseDTO> invites = doctorPatientInvitationService.getAllInvites();
        return ResponseEntity.status(200).body(invites);
    }

    @GetMapping("invites/secretary")
    @PreAuthorize("hasRole('SECRETARY')")
    public ResponseEntity<Object> getAllInvitesBySecretary(@RequestParam UUID doctorId) {
        List<DoctorPatientInviteResponseDTO> invites = doctorPatientInvitationService
                .getAllInvitesBySecretary(doctorId);
        return ResponseEntity.status(200).body(invites);
    }

    @PatchMapping("/{inviteId}/patient-status")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Object> respondToInvite(@PathVariable UUID inviteId,
            @RequestBody ChangeInviteStatusDTO statusDTO) {
        DoctorPatientInviteDTO updatedInvite = doctorPatientInvitationService.changeStatus(inviteId, statusDTO);
        return ResponseEntity.ok().body(updatedInvite);
    }

    @PatchMapping("/{inviteId}/doctor-status")
    @PreAuthorize("hasRole('DOCTOR') || hasRole('SECRETARY')")
    public ResponseEntity<Object> disassociateByDoctor(@PathVariable UUID inviteId,
            @RequestBody ChangeInviteStatusDTO statusDTO) {
        DoctorPatientInviteDTO updatedInvite = doctorPatientInvitationService.disassociateByDoctor(inviteId,
                statusDTO);
        return ResponseEntity.ok().body(updatedInvite);
    }
}
