package br.com.meusintoma.modules.doctorSecretary.services;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import br.com.meusintoma.exceptions.globalCustomException.CustomAccessDeniedException;
import br.com.meusintoma.modules.doctor.entity.DoctorEntity;
import br.com.meusintoma.modules.doctorSecretary.entity.DoctorSecretaryEntity;
import br.com.meusintoma.modules.secretary.entity.SecretaryEntity;
import br.com.meusintoma.utils.common.AssociationStatus;

public class DoctorSecretaryServiceUtils {

    public static DoctorSecretaryEntity createDoctorSecretaryObject(DoctorEntity doctor, SecretaryEntity secretary,
            LocalDateTime associatedAt, LocalDateTime invitedAt) {
        return DoctorSecretaryEntity.builder().doctor(doctor).secretary(secretary)
                .associatedAt(associatedAt).association(AssociationStatus.PENDING)
                .invitedAt(invitedAt).build();
    }

    public static Set<UUID> getAllowedRelationshipIds(DoctorSecretaryEntity doctorSecretaryEntity) {
        Set<UUID> allowedIds = new HashSet<UUID>();
        allowedIds.add(doctorSecretaryEntity.getDoctor().getId());
        allowedIds.add(doctorSecretaryEntity.getSecretary().getId());
        return allowedIds;
    }

    public static void checkAllowedRequest(Set<UUID> allowedIds, UUID targetId) {
        if (!allowedIds.contains(targetId)) {
            throw new CustomAccessDeniedException("Você não faz parte desse relacionamento");
        }
    }
}
