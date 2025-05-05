package br.com.meusintoma.modules.doctorPatient.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.doctor.entity.DoctorEntity;
import br.com.meusintoma.modules.doctor.services.DoctorService;
import br.com.meusintoma.modules.doctorPatient.Mapper.DoctorPatientMapper;
import br.com.meusintoma.modules.doctorPatient.dto.ChangeInviteStatusDTO;
import br.com.meusintoma.modules.doctorPatient.dto.DoctorPatientInviteDTO;
import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientEntity;
import br.com.meusintoma.modules.doctorPatient.enums.DoctorPatientStatus;
import br.com.meusintoma.modules.doctorPatient.repository.DoctorPatientRepository;
import br.com.meusintoma.modules.patient.entity.PatientEntity;
import br.com.meusintoma.modules.patient.services.PatientService;
import br.com.meusintoma.security.utils.AuthValidatorUtils;
import br.com.meusintoma.utils.GenericUtils;
import br.com.meusintoma.utils.RepositoryUtils;

@Service
public class DoctorPatientInvitationService {

    @Autowired
    PatientService patientService;

    @Autowired
    DoctorService doctorService;

    @Autowired
    DoctorPatientRepository doctorPatientRepository;

    public DoctorPatientEntity findDoctorPatientEntity(UUID inviteId) {
        return RepositoryUtils.findOrThrow(doctorPatientRepository.findById(inviteId),
                () -> new NotFoundException("Relação Doutor Paciente"));
    }

    public DoctorPatientInviteDTO invitePatient(UUID patientId) {
        PatientEntity patient = patientService.findPatient(patientId);
        DoctorEntity doctor = doctorService.findDoctor(AuthValidatorUtils.getAuthenticatedUserId());

        DoctorPatientEntity doctorPatient = DoctorPatientEntity.builder().doctor(doctor).patient(patient)
                .status(DoctorPatientStatus.PENDING).build();
        doctorPatientRepository.save(doctorPatient);
        DoctorPatientInviteDTO doctorPatientInviteDTO = DoctorPatientMapper.createInviteDTO(doctorPatient);
        return doctorPatientInviteDTO;
    }

    public DoctorPatientInviteDTO changeStatus(UUID inviteId, ChangeInviteStatusDTO changeStatusDTO) {
        UUID patientId = AuthValidatorUtils.getAuthenticatedUserId();
        DoctorPatientEntity doctorPatientInvite = findDoctorPatientEntity(inviteId);
        GenericUtils.compareId(doctorPatientInvite.getPatient().getId(), patientId);
        DoctorPatientUtilsService.checkPatientStatus(changeStatusDTO.getStatus());
        doctorPatientInvite.setStatus(changeStatusDTO.getStatus());
        doctorPatientRepository.save(doctorPatientInvite);
        DoctorPatientInviteDTO doctorPatientInviteDTO = DoctorPatientMapper.createInviteDTO(doctorPatientInvite);
        return doctorPatientInviteDTO;
    }

    public DoctorPatientInviteDTO disassociateByDoctor(UUID inviteId, ChangeInviteStatusDTO changeStatusDTO) {
        UUID doctorId = AuthValidatorUtils.getAuthenticatedUserId();
        DoctorPatientEntity doctorPatientRelationship = findDoctorPatientEntity(inviteId);
        GenericUtils.compareId(doctorPatientRelationship.getDoctor().getId(), doctorId);
        DoctorPatientUtilsService.checkDoctorStatus(changeStatusDTO.getStatus());
        doctorPatientRelationship.setStatus(changeStatusDTO.getStatus());
        doctorPatientRepository.save(doctorPatientRelationship);
        DoctorPatientInviteDTO doctorPatientInviteDTO = DoctorPatientMapper.createInviteDTO(doctorPatientRelationship);
        return doctorPatientInviteDTO;
    }
}
