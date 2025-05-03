package br.com.meusintoma.modules.doctorPatient.Mapper;

import br.com.meusintoma.modules.doctorPatient.dto.DoctorPatientInviteDTO;
import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientEntity;

public class DoctorPatientMapper {
    public static DoctorPatientInviteDTO createInviteDTO(DoctorPatientEntity doctorPatient) {
        return DoctorPatientInviteDTO.builder()
                .associatedDate(doctorPatient.getAssociatedDate())
                .doctorName(doctorPatient.getDoctor().getName()).patientName(doctorPatient.getPatient().getName())
                .status(doctorPatient.getStatus()).build();
    }
}
