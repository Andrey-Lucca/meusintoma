package br.com.meusintoma.modules.doctorSecretary.services;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.AlreadyExistsException;
import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.doctor.entity.DoctorEntity;
import br.com.meusintoma.modules.doctor.services.DoctorService;
import br.com.meusintoma.modules.doctorSecretary.dto.DoctorSecretaryRequestDTO;
import br.com.meusintoma.modules.doctorSecretary.dto.DoctorSecretaryResponseDTO;
import br.com.meusintoma.modules.doctorSecretary.entity.DoctorSecretaryEntity;
import br.com.meusintoma.modules.doctorSecretary.mapper.DoctorSecretaryMapper;
import br.com.meusintoma.modules.doctorSecretary.repository.DoctorSecretaryRepository;
import br.com.meusintoma.modules.secretary.entity.SecretaryEntity;
import br.com.meusintoma.modules.secretary.services.SecretaryService;
import br.com.meusintoma.security.utils.AuthValidatorUtils;
import br.com.meusintoma.utils.common.AssociationStatus;
import br.com.meusintoma.utils.helpers.RepositoryUtils;
import br.com.meusintoma.utils.helpers.SystemClockUtils;

@Service
public class DoctorSecretaryService {

    @Autowired
    DoctorService doctorService;

    @Autowired
    SecretaryService secretaryService;

    @Autowired
    DoctorSecretaryRepository doctorSecretaryRepository;

    public DoctorSecretaryResponseDTO association(UUID doctorId, UUID secretaryId) {
        DoctorEntity doctor = doctorService.findDoctor(doctorId);

        SecretaryEntity secretary = secretaryService.findSecretaryById(secretaryId);

        checkAlreadyAssociated(doctorId, secretaryId);

        DoctorSecretaryEntity doctorSecretaryInvite = DoctorSecretaryServiceUtils.createDoctorSecretaryObject(doctor,
                secretary, null, SystemClockUtils.getCurrentDateTime());

        doctorSecretaryRepository.save(doctorSecretaryInvite);

        return DoctorSecretaryMapper.toDoctorSecretaryResponseCreation(doctor, secretary);
    }

    public DoctorSecretaryResponseDTO changeDoctorSecretaryRelationshipStatus(UUID inviteId,
            DoctorSecretaryRequestDTO requestDTO) {
        DoctorSecretaryEntity doctorSecretaryRelationship = RepositoryUtils.findOrThrow(
                doctorSecretaryRepository.findRelationshipByIdWithDoctorAndSecretary(inviteId),
                () -> new NotFoundException("Doutor-Secretária"));

        checkRelationshipPermissions(doctorSecretaryRelationship);

        if (requestDTO.getStatus().equals(AssociationStatus.ASSOCIATED)) {
            doctorSecretaryRelationship.setAssociatedAt(SystemClockUtils.getCurrentDateTime());
        }

        doctorSecretaryRelationship.setAssociation(requestDTO.getStatus());
        doctorSecretaryRepository.save(doctorSecretaryRelationship);

        return DoctorSecretaryMapper.toDoctorSecretaryResponse(doctorSecretaryRelationship);
    }

    public DoctorSecretaryResponseDTO getDoctorSecretaryInvites(UUID inviteId) {
        DoctorSecretaryEntity doctorSecretaryRelationship = RepositoryUtils.findOrThrow(
                doctorSecretaryRepository.findRelationshipByIdWithDoctorAndSecretary(inviteId),
                () -> new NotFoundException("Doutor-Secretária"));

        checkRelationshipPermissions(doctorSecretaryRelationship);

        return DoctorSecretaryMapper.toDoctorSecretaryResponse(doctorSecretaryRelationship);
    }

    public List<DoctorSecretaryResponseDTO> getAssociatedDoctorSecretaryByDoctorId(UUID doctorId) {
        List<DoctorSecretaryEntity> doctorSecretaryRelationships = doctorSecretaryRepository
                .findAllRelationshipByDoctorId(doctorId);

        for (DoctorSecretaryEntity doctorSecretaryRelationship : doctorSecretaryRelationships) {
            checkRelationshipPermissions(doctorSecretaryRelationship);
        }

        return doctorSecretaryRelationships.stream().map(DoctorSecretaryMapper::toDoctorSecretaryResponse).toList();
    }

    public List<DoctorSecretaryResponseDTO> getAssociatedDoctorSecretaryBySecretaryId(UUID secretaryId) {
        List<DoctorSecretaryEntity> doctorSecretaryRelationships = doctorSecretaryRepository
                .findAllRelationshipBySecretaryId(secretaryId);

        for (DoctorSecretaryEntity doctorSecretaryRelationship : doctorSecretaryRelationships) {
            checkRelationshipPermissions(doctorSecretaryRelationship);
        }

        return doctorSecretaryRelationships.stream().map(DoctorSecretaryMapper::toDoctorSecretaryResponse).toList();
    }

    public List<DoctorSecretaryResponseDTO> getAllInvitesByUserId() {
        UUID userId = AuthValidatorUtils.getAuthenticatedUserId();

        List<DoctorSecretaryEntity> invites = doctorSecretaryRepository.findAllInvitesByUserId(userId);

        return invites.stream().map(DoctorSecretaryMapper::toDoctorSecretaryResponse).toList();

    }

    private void checkAlreadyAssociated(UUID doctorId, UUID secretaryId) {
        boolean relationshipExists = doctorSecretaryRepository.alreadyExistsRelationship(doctorId, secretaryId);
        if (relationshipExists) {
            throw new AlreadyExistsException("O relacionamento entre esse doutor e secretária");
        }
    }

    private void checkRelationshipPermissions(DoctorSecretaryEntity doctorSecretaryRelationship) {
        UUID userId = AuthValidatorUtils.getAuthenticatedUserId();
        Set<UUID> allowedIds = DoctorSecretaryServiceUtils.getAllowedRelationshipIds(doctorSecretaryRelationship);
        DoctorSecretaryServiceUtils.checkAllowedRequest(allowedIds, userId);
    }

}
