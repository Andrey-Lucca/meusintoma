package br.com.meusintoma.modules.doctorPatient.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientNoteEntity;

public interface DoctorPatientNoteRepository extends JpaRepository<DoctorPatientNoteEntity, UUID> {
    
}
