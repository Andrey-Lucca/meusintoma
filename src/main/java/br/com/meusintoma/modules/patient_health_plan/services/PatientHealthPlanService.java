package br.com.meusintoma.modules.patient_health_plan.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.healthPlan.entity.HealthPlanEntity;
import br.com.meusintoma.modules.healthPlan.services.HealthPlanService;
import br.com.meusintoma.modules.patient.entity.PatientEntity;
import br.com.meusintoma.modules.patient.services.PatientService;
import br.com.meusintoma.modules.patient_health_plan.dto.PatientHealthPlanAssociationResultDTO;
import br.com.meusintoma.modules.patient_health_plan.dto.PatientHealthPlanRequestDTO;
import br.com.meusintoma.modules.patient_health_plan.dto.PatientHealthPlanResponseDTO;
import br.com.meusintoma.modules.patient_health_plan.entity.PatientHealthPlanEntity;
import br.com.meusintoma.modules.patient_health_plan.enums.PatientPlanAssociationStatusResult;
import br.com.meusintoma.modules.patient_health_plan.mapper.PatientHealthPlanMapper;
import br.com.meusintoma.modules.patient_health_plan.repository.PatientHealthPlanRepository;
import br.com.meusintoma.security.utils.AuthValidatorUtils;
import br.com.meusintoma.utils.helpers.GenericUtils;
import br.com.meusintoma.utils.helpers.RepositoryUtils;

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
                    PatientHealthPlansServiceUtils.addResult(results, plan,
                            PatientPlanAssociationStatusResult.ALREADY_EXISTS,
                            "Plan already associated");
                    continue;
                }
                PatientHealthPlanEntity association = PatientHealthPlansServiceUtils
                        .buildPatientHealthPlanEntity(patient, plan, cardId);
                newAssociations.add(association);
                PatientHealthPlansServiceUtils.addResult(results, plan, PatientPlanAssociationStatusResult.ASSOCIATED,
                        "Patient Associated");
            } catch (Exception e) {
                results.add(
                        PatientHealthPlansServiceUtils.buildResult(planName, PatientPlanAssociationStatusResult.ERROR,
                                "Error associating plan: " + e.getMessage()));
            }
        }

        saveNewAssociations(newAssociations);

        return results;
    }

    public List<PatientHealthPlanResponseDTO> getPatientHealthPlans(UUID requestPatientId) {
        UUID patientId = AuthValidatorUtils.getAuthenticatedUserId();
        GenericUtils.compareId(requestPatientId, patientId);

        List<PatientHealthPlanEntity> associations = RepositoryUtils.findOrThrow(
                patientHealthPlanRepository.getAllPlansByPatientId(patientId),
                () -> new NotFoundException("Paciente-Plano"));

        GenericUtils.checkIsEmptyList(associations);

        return associations.stream().map(PatientHealthPlanMapper::toPatientHealthPlanResponse).toList();
    }

    public PatientHealthPlanResponseDTO disassociate(UUID patientHealthPlanId) {
        UUID patientId = AuthValidatorUtils.getAuthenticatedUserId();

        PatientHealthPlanEntity association = RepositoryUtils.findOrThrow(
                patientHealthPlanRepository.findById(patientHealthPlanId),
                () -> new NotFoundException("Paciente-Plano"));

        GenericUtils.compareId(association.getPatient().getId(), patientId);

        patientHealthPlanRepository.delete(association);

        return PatientHealthPlanMapper.toPatientHealthPlanResponse(association);
    }

    private void saveNewAssociations(List<PatientHealthPlanEntity> newAssociations) {
        GenericUtils.checkIsEmptyList(newAssociations);
        patientHealthPlanRepository.saveAll(newAssociations);
    }

}
