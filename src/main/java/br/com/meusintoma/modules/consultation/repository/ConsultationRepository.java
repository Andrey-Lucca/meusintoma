package br.com.meusintoma.modules.consultation.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.meusintoma.modules.consultation.entity.ConsultationEntity;

@Repository
public interface ConsultationRepository extends JpaRepository<ConsultationEntity, UUID> {
    List<ConsultationEntity> findAllByDoctorId(UUID doctorId);
    List<ConsultationEntity> findAllByPatientId(UUID patientId);
    List<ConsultationEntity> findAllBySecretaryId(UUID secretaryId);
}
