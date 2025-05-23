package br.com.meusintoma.modules.doctor.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
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
public class DoctorCalendarResponseDTO {
    private LocalDate date;
    private LocalTime startAt;
    private LocalTime endAt;
    private CalendarStatus calendarStatus;
    private UUID calendarId;

    //Específico para consulta do calendario unitário
    private Map<UUID,String> calendarHealthPlan;

}
