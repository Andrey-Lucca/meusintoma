package br.com.meusintoma.modules.doctorSecretary.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.meusintoma.utils.common.AssociationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorSecretaryResponseDTO {
    
    private UUID id;
    private String doctor;
    private String secretary;
    private LocalDateTime associatedAt;
    private LocalDateTime invitedAt;
    private AssociationStatus status;
}
