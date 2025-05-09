package br.com.meusintoma.modules.doctorPatient.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.modules.doctor.entity.DoctorEntity;
import br.com.meusintoma.modules.doctor.services.DoctorService;
import br.com.meusintoma.modules.doctorPatient.Mapper.DoctorPatientMapper;
import br.com.meusintoma.modules.doctorPatient.dto.ChangeInviteStatusDTO;
import br.com.meusintoma.modules.doctorPatient.dto.DoctorPatientInviteDTO;
import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientEntity;
import br.com.meusintoma.modules.doctorPatient.enums.DoctorPatientStatus;
import br.com.meusintoma.modules.doctorPatient.exceptions.DoctorPatientDuplicatedInviteException;
import br.com.meusintoma.modules.doctorPatient.repository.DoctorPatientRepository;
import br.com.meusintoma.modules.patient.entity.PatientEntity;
import br.com.meusintoma.modules.patient.services.PatientService;
import br.com.meusintoma.security.utils.AuthValidatorUtils;

@Service
public class DoctorPatientInvitationService {

    @Autowired
    PatientService patientService;

    @Autowired
    DoctorService doctorService;

    @Autowired
    DoctorPatientRepository doctorPatientRepository;

    @Autowired
    DoctorPatientService doctorPatientService;

    @Autowired
    DoctorPatientUtilsService doctorPatientUtilsService;

    public DoctorPatientEntity findDoctorPatientEntity(UUID inviteRelationshipId) {
        return doctorPatientService.getByIdValidated(inviteRelationshipId);
    }

    private void checkRelationshipExistis(UUID doctorId, UUID patientId) {
        boolean isRelationshipExists = doctorPatientRepository.existsActiveDoctorPatientRelationship(doctorId,
                patientId);
        if (isRelationshipExists) {
            throw new DoctorPatientDuplicatedInviteException(
                    "Já existe um convite associado a esse médico e paciente, por favor aguarde");
        }
    }

    public DoctorPatientInviteDTO invitePatient(UUID patientId) {
        PatientEntity patient = patientService.findPatient(patientId);
        String role = AuthValidatorUtils.getCurrentUserRole();
        UUID userId = AuthValidatorUtils.getAuthenticatedUserId();
        UUID doctorId = "SECRETARY".equals(role) ? doctorService.getDoctorIdBySecretaryId(userId) : userId;
        DoctorEntity doctor = doctorService.findDoctor(doctorId);
        checkRelationshipExistis(doctorId, patientId);

        DoctorPatientEntity doctorPatient = DoctorPatientEntity.builder().doctor(doctor).patient(patient)
                .status(DoctorPatientStatus.PENDING).build();
        doctorPatientRepository.save(doctorPatient);
        DoctorPatientInviteDTO doctorPatientInviteDTO = DoctorPatientMapper.createInviteDTO(doctorPatient);
        return doctorPatientInviteDTO;
    }

    public DoctorPatientInviteDTO changeStatus(UUID relationshipInviteId, ChangeInviteStatusDTO changeStatusDTO) {
        DoctorPatientEntity relationship = doctorPatientService.getByIdValidated(relationshipInviteId);
        doctorPatientUtilsService.validateAcess(relationship);
        DoctorPatientUtilsService.checkPatientStatus(changeStatusDTO.getStatus());
        relationship.setStatus(changeStatusDTO.getStatus());
        doctorPatientRepository.save(relationship);
        DoctorPatientInviteDTO doctorPatientInviteDTO = DoctorPatientMapper.createInviteDTO(relationship);
        return doctorPatientInviteDTO;
    }

    public DoctorPatientInviteDTO disassociateByDoctor(UUID relationshipInviteId, ChangeInviteStatusDTO changeStatusDTO) {
        DoctorPatientEntity relationship = doctorPatientService.getByIdValidated(relationshipInviteId);
        doctorPatientUtilsService.validateAcess(relationship);
        DoctorPatientUtilsService.checkDoctorStatus(changeStatusDTO.getStatus());
        relationship.setStatus(changeStatusDTO.getStatus());
        doctorPatientRepository.save(relationship);
        DoctorPatientInviteDTO doctorPatientInviteDTO = DoctorPatientMapper.createInviteDTO(relationship);
        return doctorPatientInviteDTO;
    }
}
