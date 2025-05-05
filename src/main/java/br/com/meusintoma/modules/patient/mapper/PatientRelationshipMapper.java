package br.com.meusintoma.modules.patient.mapper;

import java.util.List;

import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientEntity;
import br.com.meusintoma.modules.patient.dto.PatientRelationshipDTO;

public class PatientRelationshipMapper {

    public static List<PatientRelationshipDTO> toRelationshipDTO(List<DoctorPatientEntity> doctorsPatients) {
        return doctorsPatients.stream()
            .map(doctorsPatient -> PatientRelationshipDTO.builder()
                .relationshipId(doctorsPatient.getId())
                .doctorName(doctorsPatient.getDoctor().getName())
                .crm(doctorsPatient.getDoctor().getCrm())
                .specialization(doctorsPatient.getDoctor().getSpecialization())
                .build())
            .toList();
    }
    
}
