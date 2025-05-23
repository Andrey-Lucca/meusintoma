package br.com.meusintoma.modules.consultation.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import br.com.meusintoma.modules.consultation.enums.ConsultationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ConsultationResponseDTO {
    private String doctor;
    private String patient; 
    private ConsultationStatus status;
    private LocalTime startedAt;
    private LocalTime endAt;
    private LocalDate date;
    private String healthPlanName;
}
