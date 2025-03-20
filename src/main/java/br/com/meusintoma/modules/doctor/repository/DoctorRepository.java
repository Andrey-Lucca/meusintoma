package br.com.meusintoma.modules.doctor.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.meusintoma.modules.doctor.entity.DoctorEntity;

public interface DoctorRepository extends JpaRepository<DoctorEntity, UUID> {
    
}
