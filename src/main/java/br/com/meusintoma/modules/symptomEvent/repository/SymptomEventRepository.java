package br.com.meusintoma.modules.symptomEvent.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.meusintoma.modules.symptomEvent.entity.SymptomEventEntity;

public interface SymptomEventRepository extends JpaRepository<SymptomEventEntity, UUID> {

    List<SymptomEventEntity> findByPatientIdOrderByStartedAtDesc(UUID patientId);

    List<SymptomEventEntity> findByPatientIdAndSymptomNameContainingIgnoreCaseOrderByStartedAtDesc(UUID patientId,
            String symptomName);

    @Query("SELECT s FROM symptom s JOIN FETCH patient p WHERE s.id = :symptomId")
    Optional<SymptomEventEntity> findSymptomByIdWithPatient(@Param("symptomId") UUID symptomId);
}
