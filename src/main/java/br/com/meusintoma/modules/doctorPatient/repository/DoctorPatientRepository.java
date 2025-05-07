package br.com.meusintoma.modules.doctorPatient.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientEntity;

public interface DoctorPatientRepository extends JpaRepository<DoctorPatientEntity, UUID> {

    @Query("SELECT dp FROM doctor_patient dp WHERE dp.patient.id = :patientId AND dp.status IN ('ACCEPTED', 'RECONCILE')")
    List<DoctorPatientEntity> findDoctorsByPatientId(UUID patientId);

    @Query("SELECT dp FROM doctor_patient dp WHERE dp.doctor.id = :doctorId AND dp.status IN ('ACCEPTED', 'RECONCILE')")
    List<DoctorPatientEntity> findPatientsByDoctor(UUID doctorId);
}
