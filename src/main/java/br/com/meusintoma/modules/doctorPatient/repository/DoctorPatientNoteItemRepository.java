package br.com.meusintoma.modules.doctorPatient.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientNoteItemEntity;

public interface DoctorPatientNoteItemRepository extends JpaRepository<DoctorPatientNoteItemEntity, UUID> {
}
