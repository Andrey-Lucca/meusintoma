package br.com.meusintoma.modules.patient.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientEntity;
import br.com.meusintoma.modules.doctorPatient.repository.DoctorPatientRepository;
import br.com.meusintoma.modules.patient.dto.PatientRelationshipDTO;
import br.com.meusintoma.modules.patient.mapper.PatientRelationshipMapper;
import br.com.meusintoma.security.utils.AuthValidatorUtils;
import br.com.meusintoma.utils.helpers.GenericUtils;

@Service
public class PatientRelationshipService {

    @Autowired
    DoctorPatientRepository doctorPatientRepository;

    public List<PatientRelationshipDTO> findRelationships() {
        UUID patientId = AuthValidatorUtils.getAuthenticatedUserId();
        List<DoctorPatientEntity> doctors = doctorPatientRepository.findDoctorsByPatientId(patientId);
        GenericUtils.checkIsEmptyList(doctors);
        return PatientRelationshipMapper.toRelationshipDTO(doctors);
    }
}
