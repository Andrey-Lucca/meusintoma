package br.com.meusintoma.modules.doctorSecretary.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class DoctorSecretaryAssociationRequestDTO {

    private UUID doctorId;
    private UUID secretaryId;
}
