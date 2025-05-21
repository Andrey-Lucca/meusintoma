package br.com.meusintoma.modules.patient_health_plan.mapper;

import br.com.meusintoma.modules.patient_health_plan.dto.PatientHealthPlanResponseDTO;
import br.com.meusintoma.modules.patient_health_plan.entity.PatientHealthPlanEntity;

public class PatientHealthPlanMapper {

    public static PatientHealthPlanResponseDTO toPatientHealthPlanResponse(PatientHealthPlanEntity patientHealthPlan) {
        return PatientHealthPlanResponseDTO.builder().cardIdentification(patientHealthPlan.getCardIdentification())
                .id(patientHealthPlan.getId())
                .planName(patientHealthPlan.getHealthPlan().getName()).build();
    }
}
