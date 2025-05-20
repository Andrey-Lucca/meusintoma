package br.com.meusintoma.modules.patient_health_plan.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.modules.healthPlan.entity.HealthPlanEntity;
import br.com.meusintoma.modules.healthPlan.services.HealthPlanService;
import br.com.meusintoma.modules.patient.entity.PatientEntity;
import br.com.meusintoma.modules.patient.services.PatientService;
import br.com.meusintoma.modules.patient_health_plan.dto.PatientHealthPlanAssociationResultDTO;
import br.com.meusintoma.modules.patient_health_plan.dto.PatientHealthPlanRequestDTO;
import br.com.meusintoma.modules.patient_health_plan.entity.PatientHealthPlanEntity;
import br.com.meusintoma.modules.patient_health_plan.enums.PatientPlanAssociationStatusResult;
import br.com.meusintoma.modules.patient_health_plan.repository.PatientHealthPlanRepository;
import br.com.meusintoma.utils.helpers.GenericUtils;

@Service
public class PatientHealthPlanService {

    @Autowired
    HealthPlanService healthPlanService;

    @Autowired
    PatientService patientService;

    @Autowired
    PatientHealthPlanRepository patientHealthPlanRepository;

    public List<PatientHealthPlanAssociationResultDTO> associate(PatientHealthPlanRequestDTO dto) {
        List<PatientHealthPlanAssociationResultDTO> results = new ArrayList<>();
        List<PatientHealthPlanEntity> newAssociations = new ArrayList<>();

        PatientEntity patient = patientService.findPatient(dto.getPatientId());

        patientService.checkPatient(patient.getId());

        for (Map.Entry<String, String> entry : dto.getHealthPlans().entrySet()) {
            String planName = entry.getKey();
            String cardId = entry.getValue();

            try {
                HealthPlanEntity plan = healthPlanService.findPlanByName(planName);
                boolean alreadyExists = patientHealthPlanRepository.existsByPatientAndHealthPlan(patient, plan);
                if (alreadyExists) {
                    addResult(results, plan, PatientPlanAssociationStatusResult.ALREADY_EXISTS,
                            "Plan already associated");
                    continue;
                }
                PatientHealthPlanEntity association = buildPatientHealthPlanEntity(patient, plan, cardId);
                newAssociations.add(association);
                addResult(results, plan, PatientPlanAssociationStatusResult.ASSOCIATED, "Patient Associated");
            } catch (Exception e) {
                results.add(buildResult(planName, PatientPlanAssociationStatusResult.ERROR,
                        "Error associating plan: " + e.getMessage()));
            }
        }
        
        saveNewAssociations(newAssociations);

        return results;
    }

    private void saveNewAssociations(List<PatientHealthPlanEntity> newAssociations) {
        GenericUtils.checkIsEmptyList(newAssociations);
        patientHealthPlanRepository.saveAll(newAssociations);
    }

    private void addResult(List<PatientHealthPlanAssociationResultDTO> results, HealthPlanEntity plan,
            PatientPlanAssociationStatusResult status, String message) {
        results.add(buildResult(
                plan.getName(),
                status,
                message));
    }

    private static PatientHealthPlanEntity buildPatientHealthPlanEntity(PatientEntity patient,
            HealthPlanEntity healthPlan, String cardId) {
        return PatientHealthPlanEntity.builder()
                .cardIdentification(cardId).patient(patient)
                .healthPlan(healthPlan).build();
    }

    private PatientHealthPlanAssociationResultDTO buildResult(String planName,
            PatientPlanAssociationStatusResult status, String message) {
        return PatientHealthPlanAssociationResultDTO.builder()
                .healthPlanName(planName)
                .status(status)
                .message(message)
                .build();
    }

}
