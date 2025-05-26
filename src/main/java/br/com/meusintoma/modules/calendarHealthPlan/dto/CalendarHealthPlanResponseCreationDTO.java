package br.com.meusintoma.modules.calendarHealthPlan.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import br.com.meusintoma.modules.calendar.enums.CalendarStatus;
import br.com.meusintoma.utils.common.StatusResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CalendarHealthPlanResponseCreationDTO {
    
    private LocalDate date;
    private LocalTime startedAt;
    private LocalTime endAt;
    private String doctorName;
    private UUID doctorId;
    private String healthPlanName;
    private CalendarStatus calendarStatus;
    private String message; 
    private StatusResult associationStatusResult;
}
