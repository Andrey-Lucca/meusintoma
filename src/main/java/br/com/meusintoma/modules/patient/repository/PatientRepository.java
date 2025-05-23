package br.com.meusintoma.modules.patient.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.meusintoma.modules.patient.entity.PatientEntity;


public interface PatientRepository extends JpaRepository<PatientEntity, UUID> {
    
    @Query("SELECT p FROM patient p JOIN FETCH p.healthPlanLinks hpl WHERE p.id = :patientId")
    public Optional<PatientEntity> getPatientWithHealthPlan(@Param ("patientId") UUID patientId);
}
