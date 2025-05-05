package br.com.meusintoma.modules.patient.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.patient.entity.PatientEntity;
import br.com.meusintoma.modules.patient.repository.PatientRepository;
import br.com.meusintoma.utils.RepositoryUtils;

@Service
public class PatientService {

    @Autowired
    PatientRepository patientRepository;

    public PatientEntity findPatient(UUID patientId) {
        PatientEntity patient = RepositoryUtils.findOrThrow(patientRepository.findById(patientId),
                () -> new NotFoundException("Paciente"));
        return patient;
    }

}
