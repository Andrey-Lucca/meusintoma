package br.com.meusintoma.modules.doctor.mapper;

import br.com.meusintoma.modules.doctor.dto.DoctorResponseDTO;
import br.com.meusintoma.modules.doctor.entity.DoctorEntity;

public class DoctorMapper {

    public static DoctorResponseDTO toDoctorResponseDTO(DoctorEntity doctor) {
        return DoctorResponseDTO.builder().crm(doctor.getCrm()).name(doctor.getName())
                .specialization(doctor.getSpecialization()).build();
    }
}
