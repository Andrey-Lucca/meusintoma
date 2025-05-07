package br.com.meusintoma.modules.doctor.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.meusintoma.modules.doctor.entity.DoctorEntity;

public interface DoctorRepository extends JpaRepository<DoctorEntity, UUID> {
    @Query("SELECT d.secretary FROM doctor d WHERE d.id = :doctorId")
    Optional<UUID> findSecretaryByDoctorId(@Param("doctorId") UUID doctorId);
}
