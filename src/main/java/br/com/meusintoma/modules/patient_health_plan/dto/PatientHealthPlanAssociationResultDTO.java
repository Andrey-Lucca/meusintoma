package br.com.meusintoma.modules.patient_health_plan.dto;

import br.com.meusintoma.utils.common.StatusResult;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PatientHealthPlanAssociationResultDTO {
    private String healthPlanName;
    private StatusResult status;
    private String message;
}
