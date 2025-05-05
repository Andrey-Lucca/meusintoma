package br.com.meusintoma.modules.doctor.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.modules.doctor.dto.DoctorRelationshipDTO;
import br.com.meusintoma.modules.doctor.mapper.DoctorRelationshipMapper;
import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientEntity;
import br.com.meusintoma.modules.doctorPatient.repository.DoctorPatientRepository;
import br.com.meusintoma.security.utils.AuthValidatorUtils;
import br.com.meusintoma.utils.GenericUtils;

@Service
public class DoctorRelationshipService {
    
    @Autowired
    DoctorPatientRepository doctorPatientRepository;

    public List<DoctorRelationshipDTO> findRelationships() {
        UUID doctorId = AuthValidatorUtils.getAuthenticatedUserId();
        List<DoctorPatientEntity> doctorPatientsEntity = doctorPatientRepository.findPatientsByDoctor(doctorId);
        GenericUtils.checkIsEmptyList(doctorPatientsEntity);
        return DoctorRelationshipMapper.toDoctorRelationshipDTO(doctorPatientsEntity);
    }
}
