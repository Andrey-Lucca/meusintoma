package br.com.meusintoma.modules.patient.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.chronicdisease.repository.DiseaseTypeRepository;
import br.com.meusintoma.modules.patient.entity.PatientEntity;
import br.com.meusintoma.modules.patient.repository.PatientRepository;
import br.com.meusintoma.utils.helpers.RepositoryUtils;

@Service
public class PatientService {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    DiseaseTypeRepository diseaseTypeRepository;

    public PatientEntity findPatient(UUID patientId) {
        PatientEntity patient = RepositoryUtils.findOrThrow(patientRepository.findById(patientId),
                () -> new NotFoundException("Paciente"));
        return patient;
    }

}
