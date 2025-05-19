package br.com.meusintoma.modules.chronicdisease.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.meusintoma.modules.chronicdisease.entity.ChronicDiseaseEntity;

public interface ChronicDiseaseRepository extends JpaRepository<ChronicDiseaseEntity, UUID> {
    
}
