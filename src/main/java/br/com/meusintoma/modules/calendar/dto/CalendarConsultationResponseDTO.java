package br.com.meusintoma.modules.calendar.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import br.com.meusintoma.modules.calendar.enums.CalendarStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CalendarConsultationResponseDTO {
    private UUID id;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private CalendarStatus status;
}
