package br.com.meusintoma.modules.consultation.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import br.com.meusintoma.modules.consultation.enums.ConsultationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsultationByDoctorPatientDTO {
    private UUID consultationId;
    private String patient; 
    private ConsultationStatus status;
    private LocalTime startedAt;
    private LocalTime endAt;
    private LocalDate date;

}
