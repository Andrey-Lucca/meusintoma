package br.com.meusintoma.modules.patient.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.meusintoma.modules.patient.entity.PatientEntity;


public interface PatientRepository extends JpaRepository<PatientEntity, UUID> {
    
}
