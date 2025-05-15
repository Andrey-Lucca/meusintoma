package br.com.meusintoma.modules.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.meusintoma.modules.doctor.entity.DoctorEntity;
import br.com.meusintoma.modules.doctor.repository.DoctorRepository;
import br.com.meusintoma.modules.email.services.EmailService;
import br.com.meusintoma.modules.patient.entity.PatientEntity;
import br.com.meusintoma.modules.patient.repository.PatientRepository;
import br.com.meusintoma.modules.secretary.entity.SecretaryEntity;
import br.com.meusintoma.modules.secretary.repository.SecretaryRepository;
import br.com.meusintoma.modules.user.dto.CreateUserDTO;
import br.com.meusintoma.modules.user.entity.UserEntity;
import br.com.meusintoma.modules.user.exceptions.UserAlreadyRegistered;
import br.com.meusintoma.modules.user.mapper.UserMapper;
import br.com.meusintoma.modules.user.repository.UserRepository;
import br.com.meusintoma.utils.GeoUtils;
import jakarta.transaction.Transactional;

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

    @Autowired
    private EmailService emailService;

    @Transactional
    public UserEntity execute(CreateUserDTO userDTO) {
        validateEmail(userDTO.getEmail());

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        UserEntity user = UserMapper.toEntity(userDTO);
        user.setLocation(GeoUtils.createPoint(userDTO.getLatitude(), userDTO.getLongitude()));
        emailService.generateAndSendConfirmation(user);
        return saveUserByType(user);
    }

    private void validateEmail(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            throw new UserAlreadyRegistered("Email: " + email + " já cadastrado");
        });
    }

    private UserEntity saveUserByType(UserEntity user) {
        return switch (user.getUserType()) {
            case DOCTOR -> doctorRepository.save((DoctorEntity) user);
            case PATIENT -> patientRepository.save((PatientEntity) user);
            case SECRETARY -> secretaryRepository.save((SecretaryEntity) user);
            default -> throw new IllegalArgumentException("Invalid user type: " + user.getUserType());
        };
    }
}
