package br.com.meusintoma.modules.doctorPatient.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientEntity;
import br.com.meusintoma.modules.doctorPatient.repository.DoctorPatientRepository;
import br.com.meusintoma.utils.helpers.RepositoryUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class DoctorPatientService {

    private final DoctorPatientRepository doctorPatientRepository;

    public DoctorPatientEntity getByIdValidated(UUID relationshipId) {
        DoctorPatientEntity relationship = RepositoryUtils.findOrThrow(
                doctorPatientRepository.findWithDoctorAndPatientById(relationshipId),
                () -> new NotFoundException("Relação"));

        return relationship;
    }
}
