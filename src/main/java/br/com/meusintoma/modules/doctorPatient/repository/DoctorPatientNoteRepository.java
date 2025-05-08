package br.com.meusintoma.modules.doctorPatient.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientNoteEntity;

public interface DoctorPatientNoteRepository extends JpaRepository<DoctorPatientNoteEntity, UUID> {

    @Query("""
                SELECT DISTINCT dpn
                FROM doctor_patient_note dpn
                JOIN FETCH dpn.items i
                JOIN FETCH dpn.doctorPatient dp
                JOIN FETCH dp.doctor d
                JOIN FETCH dp.patient p
                JOIN FETCH dpn.consultation c
                JOIN FETCH c.calendarSlot cs
                WHERE dp.id = :relationshipId
            """)
    List<DoctorPatientNoteEntity> getNotes(@Param("relationshipId") UUID relationshipId);

    @EntityGraph(attributePaths = { "items" })
    @Query("SELECT n FROM doctor_patient_note n WHERE n.id = :noteId")
    Optional<DoctorPatientNoteEntity> findNoteWithItems(@Param("noteId") UUID noteId);
}
