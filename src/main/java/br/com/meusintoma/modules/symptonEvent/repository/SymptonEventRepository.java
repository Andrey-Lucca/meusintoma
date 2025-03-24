package br.com.meusintoma.modules.symptonEvent.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.meusintoma.modules.symptonEvent.entity.SymptonEventEntity;

public interface SymptonEventRepository extends JpaRepository<SymptonEventEntity, UUID> {
    List<SymptonEventEntity> findByPatientIdOrderByStartedAtDesc(UUID patientId);

    List<SymptonEventEntity> findByPatientIdAndSymptonNameContainingIgnoreCaseOrderByStartedAtDesc(UUID patientId, String symptonName);
}
