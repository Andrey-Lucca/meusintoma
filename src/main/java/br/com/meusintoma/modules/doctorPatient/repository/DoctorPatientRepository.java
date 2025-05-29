package br.com.meusintoma.modules.doctorPatient.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientEntity;

public interface DoctorPatientRepository extends JpaRepository<DoctorPatientEntity, UUID> {

    @Query("SELECT dp FROM doctor_patient dp WHERE dp.patient.id = :patientId AND dp.status IN ('ACCEPTED', 'RECONCILE')")
    List<DoctorPatientEntity> findDoctorsByPatientId(UUID patientId);

    @Query("SELECT dp FROM doctor_patient dp WHERE dp.doctor.id = :doctorId AND dp.status IN ('ACCEPTED', 'RECONCILE')")
    List<DoctorPatientEntity> findPatientsByDoctor(UUID doctorId);

    @Query("SELECT COUNT(dp) > 0 FROM doctor_patient dp WHERE dp.doctor.id = :doctorId AND dp.patient.id = :patientId AND dp.status NOT IN ('REJECTED', 'DISASSOCIATE')")
    boolean existsActiveDoctorPatientRelationship(@Param("doctorId") UUID doctorId, @Param("patientId") UUID patientId);

    @Query("SELECT dp FROM doctor_patient dp WHERE dp.doctor.id = :userId")
    List<DoctorPatientEntity> findAllByDoctorId(@Param("userId") UUID userId);

    @Query("SELECT dp FROM doctor_patient dp WHERE dp.patient.id = :userId")
    List<DoctorPatientEntity> findAllByPatientId(@Param("userId") UUID userId);

    @EntityGraph(attributePaths = { "doctor", "patient" })
    Optional<DoctorPatientEntity> findWithDoctorAndPatientById(UUID id);

    @EntityGraph(attributePaths = { "notes" })
    @Query("SELECT dp FROM doctor_patient dp WHERE dp.id = :relationshipId")
    Optional<DoctorPatientEntity> findWithNotes(@Param("relationshipId") UUID relationshipId);
}
