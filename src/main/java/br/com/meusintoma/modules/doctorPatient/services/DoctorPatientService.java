package br.com.meusintoma.modules.doctorPatient.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.doctor.services.DoctorService;
import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientEntity;
import br.com.meusintoma.modules.doctorPatient.repository.DoctorPatientRepository;
import br.com.meusintoma.security.utils.AuthValidatorUtils;
import br.com.meusintoma.utils.GenericUtils;
import br.com.meusintoma.utils.RepositoryUtils;

@Service
public class DoctorPatientService {

    @Autowired
    DoctorPatientRepository doctorPatientRepository;

    @Autowired
    DoctorService doctorService;

    public DoctorPatientEntity getDoctorPatientRelationship(UUID relationshipId, UUID doctorId, UUID patientId) {
        String role = AuthValidatorUtils.getCurrentUserRole();

        DoctorPatientEntity relationship = RepositoryUtils.findOrThrow(
                doctorPatientRepository.findById(relationshipId),
                () -> new NotFoundException("Relação"));

        GenericUtils.compareId(doctorId, relationship.getDoctor().getId());
        GenericUtils.compareId(patientId, relationship.getPatient().getId());

        if ("SECRETARY".equals(role)) {
            UUID secretaryId = doctorService.findSecretaryIdByDoctorId(doctorId);
            UUID authenticatedId = AuthValidatorUtils.getAuthenticatedUserId();
            GenericUtils.compareId(authenticatedId, secretaryId);
        }

        return relationship;
    }

}
