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

import br.com.meusintoma.exceptions.globalCustomException.AlreadyExistsException;
import br.com.meusintoma.exceptions.globalCustomException.CustomAccessDeniedException;
import br.com.meusintoma.exceptions.globalCustomException.NoContentException;
import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
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
    public ResponseEntity<?> associateDoctorAndSecretary(@RequestBody DoctorSecretaryAssociationRequestDTO requestDTO) {
        try {
            DoctorSecretaryResponseDTO associatedDoctorSecretary = doctorSecretaryService.association(
                    requestDTO.getDoctorId(),
                    requestDTO.getSecretaryId());
            return ResponseEntity.ok().body(associatedDoctorSecretary);
        } catch (AlreadyExistsException e) {
            throw e;
        } catch (CustomAccessDeniedException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao associar doutor e secretária.");
        }
    }

    @PutMapping("invite/{inviteId}/status")
    @PreAuthorize("hasRole('DOCTOR') || hasRole('SECRETARY')")
    public ResponseEntity<?> changeRelationshipStatus(@PathVariable UUID inviteId,
            @RequestBody DoctorSecretaryRequestDTO requestDTO) {
        try {
            DoctorSecretaryResponseDTO updatedDoctorSecretaryStatus = doctorSecretaryService
                    .changeDoctorSecretaryRelationshipStatus(inviteId, requestDTO);
            return ResponseEntity.ok().body(updatedDoctorSecretaryStatus);
        } catch (NotFoundException e) {
            throw e;
        } catch (CustomAccessDeniedException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao atualizar o status da associação.");
        }
    }

    @GetMapping("invite/{inviteId}")
    @PreAuthorize("hasRole('DOCTOR') || hasRole('SECRETARY')")
    public ResponseEntity<?> getInviteById(@PathVariable UUID inviteId) {
        try {
            DoctorSecretaryResponseDTO invite = doctorSecretaryService.getDoctorSecretaryInvites(inviteId);
            return ResponseEntity.ok().body(invite);
        } catch (NotFoundException e) {
            throw e;
        } catch (CustomAccessDeniedException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar o convite.");
        }
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> getAllAssociationsByDoctor(@PathVariable UUID doctorId) {
        try {
            List<DoctorSecretaryResponseDTO> doctorAssociations = doctorSecretaryService
                    .getAssociatedDoctorSecretaryByDoctorId(doctorId);
            return ResponseEntity.ok().body(doctorAssociations);
        } catch (CustomAccessDeniedException e) {
            throw e;
        } catch (NoContentException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar associações do médico.");
        }
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('DOCTOR') || hasRole('SECRETARY')")
    public ResponseEntity<?> getMyInvites() {
        try {
            List<DoctorSecretaryResponseDTO> invites = doctorSecretaryService.getAllInvitesByUserId();
            return ResponseEntity.ok().body(invites);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar seus convites.");
        }
    }

}
