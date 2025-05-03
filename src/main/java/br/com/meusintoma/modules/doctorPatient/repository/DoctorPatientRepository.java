package br.com.meusintoma.modules.doctorPatient.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientEntity;

public interface DoctorPatientRepository extends JpaRepository<DoctorPatientEntity, UUID> {

}
