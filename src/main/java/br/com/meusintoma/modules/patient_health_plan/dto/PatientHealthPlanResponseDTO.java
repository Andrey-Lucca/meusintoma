package br.com.meusintoma.modules.patient_health_plan.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientHealthPlanResponseDTO {

    private String planName;
    private String cardIdentification;
    private UUID id;
}
