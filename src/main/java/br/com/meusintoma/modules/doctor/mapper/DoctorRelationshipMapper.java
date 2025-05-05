package br.com.meusintoma.modules.doctor.mapper;

import java.util.List;

import br.com.meusintoma.modules.doctor.dto.DoctorRelationshipDTO;
import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientEntity;

public class DoctorRelationshipMapper {

    public static List<DoctorRelationshipDTO> toDoctorRelationshipDTO(
            List<DoctorPatientEntity> doctorsPatientRelationship) {
        return doctorsPatientRelationship.stream().map(doctorPatientRelationship -> DoctorRelationshipDTO.builder()
                .relationshipId(doctorPatientRelationship.getId())
                .patientName(doctorPatientRelationship.getPatient().getName())
                .associatedDate(doctorPatientRelationship.getAssociatedDate()).build()).toList();
    }
}
