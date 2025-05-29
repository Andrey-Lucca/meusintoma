package br.com.meusintoma.modules.doctor.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.doctor.dto.DoctorResponseDTO;
import br.com.meusintoma.modules.doctor.entity.DoctorEntity;
import br.com.meusintoma.modules.doctor.mapper.DoctorMapper;
import br.com.meusintoma.modules.doctor.repository.DoctorRepository;
import br.com.meusintoma.utils.helpers.RepositoryUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorService {

        private final DoctorRepository doctorRepository;

        public DoctorEntity findDoctor(UUID doctorId) {
                DoctorEntity doctor = RepositoryUtils.findOrThrow(doctorRepository.findById(doctorId),
                                () -> new NotFoundException("Doutor"));
                return doctor;
        }

        public DoctorResponseDTO getDoctorInfo(UUID doctorId) {
                return DoctorMapper.toDoctorResponseDTO(findDoctor(doctorId));
        }

}
