package br.com.meusintoma.modules.doctorPatient.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.CustomAccessDeniedException;
import br.com.meusintoma.modules.doctor.services.DoctorService;
import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientEntity;
import br.com.meusintoma.modules.doctorPatient.enums.DoctorPatientStatus;
import br.com.meusintoma.modules.doctorPatient.exceptions.DoctorPatientNotValidStatusException;
import br.com.meusintoma.modules.doctorSecretary.services.DoctorSecretaryService;
import br.com.meusintoma.security.utils.AuthValidatorUtils;

@Service
public class DoctorPatientUtilsService {

    @Autowired
    DoctorService doctorService;

    @Autowired
    DoctorPatientService doctorPatientService;

    @Autowired
    DoctorSecretaryService doctorSecretaryService;

    public static void checkPatientStatus(DoctorPatientStatus status) {
        List<DoctorPatientStatus> statuses = new ArrayList<>(
                List.of(DoctorPatientStatus.ACCEPTED, DoctorPatientStatus.REJECTED, DoctorPatientStatus.DISASSOCIATE));
        if (!statuses.contains(status)) {
            throw new DoctorPatientNotValidStatusException("Uso indevido dos estados");
        }
    }

    public static void checkDoctorStatus(DoctorPatientStatus status) {
        List<DoctorPatientStatus> statuses = new ArrayList<>(
                List.of(DoctorPatientStatus.DISASSOCIATE, DoctorPatientStatus.RECONCILE));
        if (!statuses.contains(status)) {
            throw new DoctorPatientNotValidStatusException("Uso indevido dos estados");
        }
    }

    public static void checkUserPermission(List<UUID> allowedIds, UUID targetId) {
        boolean isIdInRelationship = allowedIds.contains(targetId);
        if (!isIdInRelationship) {
            throw new CustomAccessDeniedException("Você não tem permissão para controlar conteúdo");
        }
    }

    public List<UUID> createPermissionList(DoctorPatientEntity relationship) {
        UUID patientId = relationship.getPatient().getId();
        UUID doctorId = relationship.getDoctor().getId();

        List<UUID> secretaryIds = doctorSecretaryService.getAllSecretaryIdsByDoctorId(doctorId);
        List<UUID> allowedIds = new ArrayList<>(List.of(patientId, doctorId));
        allowedIds.addAll(secretaryIds);

        return allowedIds;
    }

    public void validateAcess(DoctorPatientEntity relationship) {
        UUID targetUserId = AuthValidatorUtils.getAuthenticatedUserId();
        List<UUID> allowedIds = createPermissionList(relationship);
        checkUserPermission(allowedIds, targetUserId);
    }
}
