package br.com.meusintoma.modules.patient_health_plan.dto;

import br.com.meusintoma.utils.common.AssociationStatusResult;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PatientHealthPlanAssociationResultDTO {
    private String healthPlanName;
    private AssociationStatusResult status;
    private String message;
}
