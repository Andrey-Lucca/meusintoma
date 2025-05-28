package br.com.meusintoma.modules.doctorSecretary.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.meusintoma.modules.doctorSecretary.entity.DoctorSecretaryEntity;

public interface DoctorSecretaryRepository extends JpaRepository<DoctorSecretaryEntity, UUID> {

    @Query("SELECT ds FROM doctor_secretary ds JOIN FETCH ds.doctor d JOIN FETCH ds.secretary s WHERE ds.id = :id")
    Optional<DoctorSecretaryEntity> findRelationshipByIdWithDoctorAndSecretary(@Param("id") UUID id);

    @Query("SELECT COUNT(ds) > 0 FROM doctor_secretary ds WHERE ds.doctor.id = :doctorId AND ds.secretary.id = :secretaryId")
    boolean alreadyExistsRelationship(@Param("doctorId") UUID doctorId, @Param("secretaryId") UUID secretaryId);

    @Query("SELECT ds FROM doctor_secretary ds WHERE ds.doctor.id = :doctorId AND ds.association = 'ASSOCIATED'")
    List<DoctorSecretaryEntity> findAllRelationshipByDoctorId(@Param("doctorId") UUID doctorId);

    @Query("SELECT ds FROM doctor_secretary ds JOIN FETCH ds.secretary s WHERE ds.doctor.id = :doctorId AND ds.association = 'ASSOCIATED'")
    List<DoctorSecretaryEntity> findAllRelationshipByDoctorIdWithSecretary(@Param("doctorId") UUID doctorId);

    @Query("SELECT ds FROM doctor_secretary ds WHERE ds.secretary.id = :secretaryId AND ds.association = 'ASSOCIATED'")
    List<DoctorSecretaryEntity> findAllRelationshipBySecretaryId(@Param("secretaryId") UUID secretaryId);

    @Query("SELECT ds FROM doctor_secretary ds WHERE ds.secretary.id = :userId OR ds.doctor.id = :userId")
    List<DoctorSecretaryEntity> findAllInvitesByUserId(@Param("userId") UUID userId);

    @Query("SELECT ds.doctor.id FROM doctor_secretary ds WHERE ds.secretary.id = :secretaryId AND ds.association = 'ASSOCIATED'")
    List<UUID> findAllDoctorsBySecretaryId(@Param("secretaryId") UUID secretaryId);
}
