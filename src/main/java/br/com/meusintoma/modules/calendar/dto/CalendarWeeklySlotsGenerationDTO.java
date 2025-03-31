package br.com.meusintoma.modules.calendar.dto;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CalendarWeeklySlotsGenerationDTO extends GenerateDailySlotsRequestDTO {
    private int startDay;
    private int endDay;
    private LocalDate requestDate;
}
