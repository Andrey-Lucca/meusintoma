package br.com.meusintoma.modules.doctor.repository;

import java.util.List;
import java.util.UUID;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.meusintoma.modules.doctor.entity.DoctorEntity;

public interface DoctorRepository extends JpaRepository<DoctorEntity, UUID> {

    @Query(value = "SELECT * FROM doctor d JOIN users u ON d.id = u.id WHERE ST_DWithin(u.location, ST_SetSRID(:location, 4326), :distance) AND d.specialization = :specialization", nativeQuery = true)
    List<DoctorEntity> getNearbyDoctors(
            @Param("location") Point location,
            @Param("distance") Double distance,
            @Param("specialization") String specialization);
}
