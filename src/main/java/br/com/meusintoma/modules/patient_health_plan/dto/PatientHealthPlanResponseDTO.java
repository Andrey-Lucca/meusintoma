package br.com.meusintoma.modules.patient_health_plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientHealthPlanResponseDTO {

    private String patientName;
    private String planName;
    private String cardIdentification;
}
