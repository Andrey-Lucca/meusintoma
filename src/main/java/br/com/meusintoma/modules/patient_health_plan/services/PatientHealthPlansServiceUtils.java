package br.com.meusintoma.modules.patient_health_plan.services;

import java.util.List;

import br.com.meusintoma.modules.healthPlan.entity.HealthPlanEntity;
import br.com.meusintoma.modules.patient.entity.PatientEntity;
import br.com.meusintoma.modules.patient_health_plan.dto.PatientHealthPlanAssociationResultDTO;
import br.com.meusintoma.modules.patient_health_plan.entity.PatientHealthPlanEntity;
import br.com.meusintoma.modules.patient_health_plan.enums.PatientPlanAssociationStatusResult;

public class PatientHealthPlansServiceUtils {
    public static PatientHealthPlanEntity buildPatientHealthPlanEntity(PatientEntity patient,
            HealthPlanEntity healthPlan, String cardId) {
        return PatientHealthPlanEntity.builder()
                .cardIdentification(cardId).patient(patient)
                .healthPlan(healthPlan).build();
    }

    public static PatientHealthPlanAssociationResultDTO buildResult(String planName,
            PatientPlanAssociationStatusResult status, String message) {
        return PatientHealthPlanAssociationResultDTO.builder()
                .healthPlanName(planName)
                .status(status)
                .message(message)
                .build();
    }

    public static void addResult(List<PatientHealthPlanAssociationResultDTO> results, HealthPlanEntity plan,
            PatientPlanAssociationStatusResult status, String message) {
        results.add(buildResult(
                plan.getName(),
                status,
                message));
    }
}
