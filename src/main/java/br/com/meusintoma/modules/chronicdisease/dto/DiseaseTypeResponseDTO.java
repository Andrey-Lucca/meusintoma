package br.com.meusintoma.modules.chronicdisease.dto;

import java.util.UUID;

import br.com.meusintoma.modules.chronicdisease.enums.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiseaseTypeResponseDTO {
    
    private String name;
    private String description;
    private ApprovalStatus status;
    private UUID patientId;
}
