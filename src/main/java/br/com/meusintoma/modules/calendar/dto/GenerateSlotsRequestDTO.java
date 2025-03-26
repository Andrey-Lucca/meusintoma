package br.com.meusintoma.modules.calendar.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenerateSlotsRequestDTO {
    private UUID doctorId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private int slotDurationMinutes;
    private LocalTime breakStart; //Início do horário de descanso
    private LocalTime breakEnd; //Fim do horário de descanso

    public boolean isValid() {
        return date != null
                && startTime != null
                && endTime != null
                && slotDurationMinutes > 0
                && startTime.isBefore(endTime)
                && (breakStart == null || breakEnd == null || breakStart.isBefore(breakEnd));
    }
}
