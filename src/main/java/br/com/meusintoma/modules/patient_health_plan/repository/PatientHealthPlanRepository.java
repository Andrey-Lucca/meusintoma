package br.com.meusintoma.modules.patient_health_plan.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.meusintoma.modules.healthPlan.entity.HealthPlanEntity;
import br.com.meusintoma.modules.patient.entity.PatientEntity;
import br.com.meusintoma.modules.patient_health_plan.entity.PatientHealthPlanEntity;

public interface PatientHealthPlanRepository extends JpaRepository<PatientHealthPlanEntity, UUID> {

    boolean existsByPatientAndHealthPlan(PatientEntity patient, HealthPlanEntity healthPlan);

    @Query("SELECT php FROM patient_health_plan php JOIN FETCH healthPlan hp WHERE php.patient.id = :patientId")
    Optional<List<PatientHealthPlanEntity>> getAllPlansByPatientId(@Param("patientId") UUID patientId);

    @Query("SELECT hp.name FROM patient_health_plan php JOIN php.healthPlan hp WHERE php.patient.id = :patientId")
    List<String> getAllPlansNameByPatientId(@Param("patientId") UUID patientId);

}
