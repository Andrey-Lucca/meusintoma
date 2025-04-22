package br.com.meusintoma.modules.consultation.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class CreateConsultationDTO {
    private UUID calendarId;
}
