package br.com.meusintoma.modules.doctorSecretary.mapper;

import br.com.meusintoma.modules.doctor.entity.DoctorEntity;
import br.com.meusintoma.modules.doctorSecretary.dto.DoctorSecretaryResponseDTO;
import br.com.meusintoma.modules.doctorSecretary.entity.DoctorSecretaryEntity;
import br.com.meusintoma.modules.secretary.entity.SecretaryEntity;
import br.com.meusintoma.utils.common.AssociationStatus;
import br.com.meusintoma.utils.helpers.SystemClockUtils;

public class DoctorSecretaryMapper {

    public static DoctorSecretaryResponseDTO toDoctorSecretaryResponseCreation(DoctorEntity doctor,
            SecretaryEntity secretary) {
        return DoctorSecretaryResponseDTO.builder().doctor(doctor.getName()).secretary(secretary.getName())
                .invitedAt(SystemClockUtils.getCurrentDateTime())
                .associatedAt(null)
                .status(AssociationStatus.PENDING)
                .build();
    }

    public static DoctorSecretaryResponseDTO toDoctorSecretaryResponse(DoctorSecretaryEntity doctorSecretary) {
        return DoctorSecretaryResponseDTO.builder().doctor(doctorSecretary.getDoctor().getName())
                .secretary(doctorSecretary.getSecretary().getName())
                .invitedAt(doctorSecretary.getInvitedAt())
                .associatedAt(doctorSecretary.getAssociatedAt())
                .status(doctorSecretary.getAssociation())
                .build();
    }
}
