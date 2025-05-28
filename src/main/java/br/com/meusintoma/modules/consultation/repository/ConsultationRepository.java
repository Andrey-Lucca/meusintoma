package br.com.meusintoma.modules.consultation.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
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

    @Query("SELECT c FROM consultation c WHERE c.doctorId = :doctorId AND c.patient.id = :patientId AND c.status IN :statuses")
    List<ConsultationEntity> findAllByDoctorAndPatientRelationship(@Param("doctorId") UUID doctorId,
            @Param("patientId") UUID patientId, List<ConsultationStatus> statuses);

    List<ConsultationEntity> findAllByDoctorIdIn(List<UUID> doctorIds);
 

    @Query("SELECT c FROM consultation c JOIN calendarSlot cal WHERE cal.date < :today OR (cal.date = :today AND cal.startTime < :now)")
    List<ConsultationEntity> findExpiredConsultations(@Param("today") LocalDate today, @Param("now") LocalTime now);

    @Modifying
    @Query("DELETE FROM consultation c WHERE c.status IN :status")
    void deleteExpiredConsultations(@Param("status") List<ConsultationStatus> status);

    @EntityGraph(attributePaths = { "calendarSlot" })
    Optional<ConsultationEntity> findWithCalendarSlotById(UUID id);

}
