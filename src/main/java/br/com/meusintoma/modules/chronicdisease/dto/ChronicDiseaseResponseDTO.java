package br.com.meusintoma.modules.chronicdisease.dto;

import java.util.UUID;
import br.com.meusintoma.utils.common.Severity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChronicDiseaseResponseDTO {
    private UUID diseaseId;
    private UUID diseaseTypeId;
    private String diseaseTypeName;
    private int yearsQuantity;
    private Severity severity;
}
