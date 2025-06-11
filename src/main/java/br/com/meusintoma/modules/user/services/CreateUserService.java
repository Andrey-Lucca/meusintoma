package br.com.meusintoma.modules.user.services;

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
import br.com.meusintoma.utils.helpers.GeoUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateUserService {

    private final UserRepository userRepository;

    private final DoctorRepository doctorRepository;

    private final PatientRepository patientRepository;

    private final SecretaryRepository secretaryRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    @Transactional
    public UserEntity execute(CreateUserDTO userDTO) {
        validateEmail(userDTO.getEmail());
        setHashedPassword(userDTO);
        UserEntity user = UserMapper.toEntity(userDTO);
        setUserLocation(user, userDTO);
        UserEntity savedUser = saveUserByType(user);

        emailService.generateAndSendConfirmation(user);
        return savedUser;
    }

    private void setHashedPassword(CreateUserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    }

    private void setUserLocation(UserEntity user, CreateUserDTO userDTO) {
        user.setLocation(GeoUtils.createPoint(userDTO.getLatitude(), userDTO.getLongitude()));
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
