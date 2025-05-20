package br.com.meusintoma.modules.patient_health_plan.dto;

import br.com.meusintoma.modules.patient_health_plan.enums.PatientPlanAssociationStatusResult;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PatientHealthPlanAssociationResultDTO {
    private String healthPlanName;
    private PatientPlanAssociationStatusResult status;
    private String message;
}
