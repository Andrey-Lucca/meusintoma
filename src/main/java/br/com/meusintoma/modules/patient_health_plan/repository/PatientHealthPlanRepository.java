package br.com.meusintoma.modules.patient_health_plan.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.meusintoma.modules.healthPlan.entity.HealthPlanEntity;
import br.com.meusintoma.modules.patient.entity.PatientEntity;
import br.com.meusintoma.modules.patient_health_plan.entity.PatientHealthPlanEntity;

public interface PatientHealthPlanRepository extends JpaRepository<PatientHealthPlanEntity, UUID> {
    
    boolean existsByPatientAndHealthPlan(PatientEntity patient, HealthPlanEntity healthPlan);
}
