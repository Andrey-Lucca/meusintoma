package br.com.meusintoma.modules.calendar.dto;

import br.com.meusintoma.utils.common.StatusResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CalendarResultDTO {
    CalendarResponseDTO calendarResponseDTO;
    private StatusResult statusResult;
    private String message;
}
