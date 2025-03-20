package br.com.meusintoma.modules.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.UserFoundException;
import br.com.meusintoma.modules.doctor.entity.DoctorEntity;
import br.com.meusintoma.modules.doctor.repository.DoctorRepository;
import br.com.meusintoma.modules.patient.entity.PatientEntity;
import br.com.meusintoma.modules.patient.repository.PatientRepository;
import br.com.meusintoma.modules.secretary.entity.SecretaryEntity;
import br.com.meusintoma.modules.secretary.repository.SecretaryRepository;
import br.com.meusintoma.modules.user.dto.CreateUserDTO;
import br.com.meusintoma.modules.user.entity.UserEntity;
import br.com.meusintoma.modules.user.mapper.UserMapper;
import br.com.meusintoma.modules.user.repository.UserRepository;

@Service
public class CreateUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private SecretaryRepository secretaryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserEntity execute(CreateUserDTO userDTO) {
        if (userDTO == null) {
            throw new IllegalArgumentException("The fields cannot be null");
        }
        this.userRepository.findByEmail(userDTO.getEmail()).ifPresent((user) -> {
            throw new UserFoundException();
        });
        var password = passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(password);
        UserEntity userEntity = UserMapper.toEntity(userDTO);
        var userType = userEntity.getUserType();
        switch (userType) {
            case DOCTOR:
                return doctorRepository.save((DoctorEntity) userEntity);
            case PATIENT:
                return patientRepository.save((PatientEntity) userEntity);
            case SECRETARY:
                return secretaryRepository.save((SecretaryEntity) userEntity);
            default:
                throw new IllegalArgumentException("Invalid user type: " + userType);
        }
    }

}
