package br.com.meusintoma.modules.calendar.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import br.com.meusintoma.modules.calendar.enums.IntervalChoice;
import lombok.Data;

@Data
public class CalendarConsultationRequestDTO {
    
    private LocalDate startDate;
    private LocalDate finalDate;
    private LocalTime startTime;
    private IntervalChoice intervalChoice;
    private UUID doctorId;
}
