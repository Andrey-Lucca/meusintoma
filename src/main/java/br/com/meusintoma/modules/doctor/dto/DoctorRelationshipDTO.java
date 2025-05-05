package br.com.meusintoma.modules.doctor.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRelationshipDTO {
    private UUID relationshipId;
    private String patientName;
    private LocalDateTime associatedDate;
}
