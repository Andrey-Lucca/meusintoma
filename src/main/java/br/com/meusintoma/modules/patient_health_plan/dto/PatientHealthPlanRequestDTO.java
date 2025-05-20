package br.com.meusintoma.modules.patient_health_plan.dto;

import java.util.Map;
import java.util.UUID;

import lombok.Data;

@Data
public class PatientHealthPlanRequestDTO {
    private UUID patientId;
    private Map<String, String> healthPlans;
}
