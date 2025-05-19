package br.com.meusintoma.modules.doctor.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.doctor.entity.DoctorEntity;
import br.com.meusintoma.modules.doctor.repository.DoctorRepository;
import br.com.meusintoma.utils.helpers.RepositoryUtils;

@Service
public class DoctorService {

    @Autowired
    DoctorRepository doctorRepository;

    public DoctorEntity findDoctor(UUID doctorId) {
        DoctorEntity doctor = RepositoryUtils.findOrThrow(doctorRepository.findById(doctorId),
                () -> new NotFoundException("Doutor"));
        return doctor;
    }

    public UUID findSecretaryIdByDoctorId(UUID doctorId) {
        UUID secretaryId = RepositoryUtils.findOrThrow(doctorRepository.findSecretaryByDoctorId(doctorId),
                () -> new NotFoundException("Secretária"));
        return secretaryId;
    }

    public UUID getDoctorIdBySecretaryId(UUID secretaryId) {
        UUID secretary = RepositoryUtils.findOrThrow(doctorRepository.findDoctorIdBySecretaryId(secretaryId),
                () -> new NotFoundException("Secretaria"));
        return secretary;
    }
}
