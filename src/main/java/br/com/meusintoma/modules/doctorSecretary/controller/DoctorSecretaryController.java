package br.com.meusintoma.modules.doctorSecretary.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.modules.doctorSecretary.dto.DoctorSecretaryAssociationRequestDTO;
import br.com.meusintoma.modules.doctorSecretary.dto.DoctorSecretaryRequestDTO;
import br.com.meusintoma.modules.doctorSecretary.dto.DoctorSecretaryResponseDTO;
import br.com.meusintoma.modules.doctorSecretary.services.DoctorSecretaryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/doctor-secretary")
@RequiredArgsConstructor

public class DoctorSecretaryController {

    private final DoctorSecretaryService doctorSecretaryService;

    @PostMapping("/associate")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorSecretaryResponseDTO> associateDoctorAndSecretary(
            @RequestBody DoctorSecretaryAssociationRequestDTO requestDTO) {
        DoctorSecretaryResponseDTO associatedDoctorSecretary = doctorSecretaryService.association(
                requestDTO.getDoctorId(),
                requestDTO.getSecretaryId());
        return ResponseEntity.ok().body(associatedDoctorSecretary);
    }

    @PutMapping("invite/{inviteId}/status")
    @PreAuthorize("hasRole('DOCTOR') || hasRole('SECRETARY')")
    public ResponseEntity<DoctorSecretaryResponseDTO> changeRelationshipStatus(@PathVariable UUID inviteId,
            @RequestBody DoctorSecretaryRequestDTO requestDTO) {
        DoctorSecretaryResponseDTO updatedDoctorSecretaryStatus = doctorSecretaryService
                .changeDoctorSecretaryRelationshipStatus(inviteId, requestDTO);
        return ResponseEntity.ok().body(updatedDoctorSecretaryStatus);
    }

    @GetMapping("invite/{inviteId}")
    @PreAuthorize("hasRole('DOCTOR') || hasRole('SECRETARY')")
    public ResponseEntity<DoctorSecretaryResponseDTO> getInviteById(@PathVariable UUID inviteId) {
        DoctorSecretaryResponseDTO invite = doctorSecretaryService.getDoctorSecretaryInvites(inviteId);
        return ResponseEntity.ok().body(invite);
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<DoctorSecretaryResponseDTO>> getAllAssociationsByDoctor(@PathVariable UUID doctorId) {
        List<DoctorSecretaryResponseDTO> doctorAssociations = doctorSecretaryService
                .getAssociatedDoctorSecretaryByDoctorId(doctorId);
        return ResponseEntity.ok().body(doctorAssociations);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('DOCTOR') || hasRole('SECRETARY')")
    public ResponseEntity<List<DoctorSecretaryResponseDTO>> getMyInvites() {
        List<DoctorSecretaryResponseDTO> invites = doctorSecretaryService.getAllInvitesByUserId();
        return ResponseEntity.ok().body(invites);
    }

}
