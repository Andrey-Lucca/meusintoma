package br.com.meusintoma.modules.doctorPatient.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.CustomAccessDeniedException;
import br.com.meusintoma.modules.doctor.entity.DoctorEntity;
import br.com.meusintoma.modules.doctor.services.DoctorService;
import br.com.meusintoma.modules.doctorPatient.Mapper.DoctorPatientMapper;
import br.com.meusintoma.modules.doctorPatient.dto.ChangeInviteStatusDTO;
import br.com.meusintoma.modules.doctorPatient.dto.DoctorPatientInviteDTO;
import br.com.meusintoma.modules.doctorPatient.dto.DoctorPatientInviteResponseDTO;
import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientEntity;
import br.com.meusintoma.modules.doctorPatient.enums.DoctorPatientStatus;
import br.com.meusintoma.modules.doctorPatient.exceptions.DoctorPatientDuplicatedInviteException;
import br.com.meusintoma.modules.doctorPatient.repository.DoctorPatientRepository;
import br.com.meusintoma.modules.doctorSecretary.services.DoctorSecretaryService;
import br.com.meusintoma.modules.patient.entity.PatientEntity;
import br.com.meusintoma.modules.patient.services.PatientService;
import br.com.meusintoma.security.utils.AuthValidatorUtils;
import br.com.meusintoma.utils.helpers.GenericUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorPatientInvitationService {

    private final PatientService patientService;

    private final DoctorService doctorService;

    private final DoctorPatientRepository doctorPatientRepository;

    private final DoctorPatientService doctorPatientService;

    private final DoctorPatientUtilsService doctorPatientUtilsService;

    private final DoctorSecretaryService doctorSecretaryService;

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

    public DoctorPatientInviteDTO invitePatient(UUID patientId, UUID doctorId) {
        PatientEntity patient = patientService.findPatient(patientId);

        String role = AuthValidatorUtils.getCurrentUserRole();

        if ("SECRETARY".equals(role)) {
            UUID secretaryId = AuthValidatorUtils.getAuthenticatedUserId();
            doctorSecretaryService.checkAssociation(doctorId, secretaryId);
        }

        DoctorEntity doctor = doctorService.findDoctor(doctorId);
        checkRelationshipExistis(doctorId, patientId);

        DoctorPatientEntity doctorPatient = DoctorPatientEntity.builder().doctor(doctor).patient(patient)
                .status(DoctorPatientStatus.PENDING).build();
        doctorPatientRepository.save(doctorPatient);
        DoctorPatientInviteDTO doctorPatientInviteDTO = DoctorPatientMapper.createInviteDTO(doctorPatient);
        return doctorPatientInviteDTO;
    }

    public List<DoctorPatientInviteResponseDTO> getAllInvites() {
        UUID userId = AuthValidatorUtils.getAuthenticatedUserId();
        String role = AuthValidatorUtils.getCurrentUserRole();

        List<DoctorPatientEntity> invites = new ArrayList<>();

        switch (role) {
            case "DOCTOR" -> invites = doctorPatientRepository.findAllByDoctorId(userId);
            case "PATIENT" -> invites = doctorPatientRepository.findAllByPatientId(userId);
            default -> throw new CustomAccessDeniedException("Role não permitida");
        }

        GenericUtils.checkIsEmptyList(invites);

        return invites.stream().map(DoctorPatientMapper::toInviteResponse).toList();
    }

    public List<DoctorPatientInviteResponseDTO> getAllInvitesBySecretary(UUID doctorId) {
        UUID secretaryId = AuthValidatorUtils.getAuthenticatedUserId();

        doctorSecretaryService.checkAssociation(doctorId, secretaryId);

        List<DoctorPatientEntity> invites = doctorPatientRepository.findAllByDoctorId(doctorId);

        GenericUtils.checkIsEmptyList(invites);

        return invites.stream().map(DoctorPatientMapper::toInviteResponse).toList();
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

    public DoctorPatientInviteDTO disassociateByDoctor(UUID relationshipInviteId,
            ChangeInviteStatusDTO changeStatusDTO) {
        DoctorPatientEntity relationship = doctorPatientService.getByIdValidated(relationshipInviteId);
        doctorPatientUtilsService.validateAcess(relationship);
        DoctorPatientUtilsService.checkDoctorStatus(changeStatusDTO.getStatus());
        relationship.setStatus(changeStatusDTO.getStatus());
        doctorPatientRepository.save(relationship);
        DoctorPatientInviteDTO doctorPatientInviteDTO = DoctorPatientMapper.createInviteDTO(relationship);
        return doctorPatientInviteDTO;
    }
}
