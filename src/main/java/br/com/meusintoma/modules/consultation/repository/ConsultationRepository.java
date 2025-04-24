package br.com.meusintoma.modules.consultation.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.meusintoma.modules.consultation.entity.ConsultationEntity;
import br.com.meusintoma.modules.consultation.enums.ConsultationStatus;

@Repository
public interface ConsultationRepository extends JpaRepository<ConsultationEntity, UUID> {
    List<ConsultationEntity> findAllByDoctorId(UUID doctorId);

    List<ConsultationEntity> findAllByPatientId(UUID patientId);

    List<ConsultationEntity> findAllBySecretaryId(UUID secretaryId);

    @Query("SELECT c FROM consultation c JOIN calendarSlot cal WHERE cal.date < :today OR (cal.date = :today AND cal.startTime < :now)")
    List<ConsultationEntity> findExpiredConsultations(@Param("today") LocalDate today, @Param("now") LocalTime now);

    @Modifying
    @Query("DELETE FROM consultation c WHERE c.status = :status")
    void deleteExpiredConsultations(@Param("status") ConsultationStatus status);
}
