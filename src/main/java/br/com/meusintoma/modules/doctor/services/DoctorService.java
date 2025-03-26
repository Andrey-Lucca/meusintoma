package br.com.meusintoma.modules.doctor.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.modules.doctor.entity.DoctorEntity;
import br.com.meusintoma.modules.doctor.repository.DoctorRepository;

@Service
public class DoctorService {

    @Autowired
    DoctorRepository doctorRepository;

    public DoctorEntity findDoctorById(UUID doctorId){
        var doctor = this.doctorRepository.findById(doctorId).orElseThrow(() -> new RuntimeException("Não foi possível localizar o doutor"));
        return doctor;
    }
}
