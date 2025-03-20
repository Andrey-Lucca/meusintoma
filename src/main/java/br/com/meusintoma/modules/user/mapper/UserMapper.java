package br.com.meusintoma.modules.user.mapper;

import br.com.meusintoma.modules.doctor.entity.DoctorEntity;
import br.com.meusintoma.modules.patient.entity.PatientEntity;
import br.com.meusintoma.modules.secretary.entity.SecretaryEntity;
import br.com.meusintoma.modules.user.dto.CreateUserDTO;
import br.com.meusintoma.modules.user.entity.UserEntity;
import br.com.meusintoma.modules.user.entity.UserType;

public class UserMapper {

    public static UserEntity toEntity(CreateUserDTO createUserDTO) {
        if (createUserDTO == null) {
            throw new IllegalArgumentException("CreateUserDTO cannot be null");
        }

        var userType = createUserDTO.getUserType();
        if (userType == null) {
            throw new IllegalArgumentException("UserType cannot be null");
        }

        switch (userType) {
            case DOCTOR:
                return mapToDoctorEntity(createUserDTO);
            case PATIENT:
                return mapToPatientEntity(createUserDTO);
            case SECRETARY:
                return mapToSecretaryEntity(createUserDTO);
            default:
                throw new IllegalArgumentException("Invalid user type: " + userType);
        }
    }

    private static SecretaryEntity mapToSecretaryEntity(CreateUserDTO createUserDTO) {
        return SecretaryEntity.builder()
                .name(createUserDTO.getName())
                .email(createUserDTO.getEmail())
                .password(createUserDTO.getPassword())
                .userType(createUserDTO.getUserType())
                .build();
    }

    private static DoctorEntity mapToDoctorEntity(CreateUserDTO createUserDTO) {
        if (createUserDTO.getCrm() == null || createUserDTO.getSpecialization() == null) {
            throw new IllegalArgumentException("CRM and Specialization are required for DoctorEntity");
        }

        return DoctorEntity.builder()
                .name(createUserDTO.getName())
                .email(createUserDTO.getEmail())
                .password(createUserDTO.getPassword())
                .userType(createUserDTO.getUserType())
                .crm(createUserDTO.getCrm())
                .specialization(createUserDTO.getSpecialization())
                .build();
    }

    private static PatientEntity mapToPatientEntity(CreateUserDTO createUserDTO) {
        return PatientEntity.builder()
                .name(createUserDTO.getName())
                .email(createUserDTO.getEmail())
                .password(createUserDTO.getPassword())
                .userType(createUserDTO.getUserType())
                .phoneNumber(createUserDTO.getPhoneNumber())
                .build();
    }
}