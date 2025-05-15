package br.com.meusintoma.modules.calendar.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import br.com.meusintoma.modules.calendar.enums.CalendarStatus;
import br.com.meusintoma.modules.doctor.enums.DoctorSpecialization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CalendarResponseDTO {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private CalendarStatus status;
    private String doctorName;
    private DoctorSpecialization doctorSpecialization;
}
