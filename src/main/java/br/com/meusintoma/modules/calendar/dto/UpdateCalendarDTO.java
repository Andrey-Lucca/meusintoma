package br.com.meusintoma.modules.calendar.dto;

import br.com.meusintoma.modules.calendar.enums.CalendarStatus;
import lombok.Data;

@Data
public class UpdateCalendarDTO {
    private CalendarStatus status;
}
