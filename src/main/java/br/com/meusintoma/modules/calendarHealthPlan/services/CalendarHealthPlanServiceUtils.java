package br.com.meusintoma.modules.calendarHealthPlan.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import br.com.meusintoma.modules.calendar.entity.CalendarEntity;
import br.com.meusintoma.modules.calendarHealthPlan.dto.CalendarHealthPlanResponseCreationDTO;
import br.com.meusintoma.modules.calendarHealthPlan.entity.CalendarHealthPlanEntity;
import br.com.meusintoma.modules.healthPlan.entity.HealthPlanEntity;
import br.com.meusintoma.utils.common.StatusResult;

public class CalendarHealthPlanServiceUtils {

    public static void addResult(CalendarEntity calendar, UUID doctorId, String healthPlanName,
            StatusResult associationStatus, String errorMessage,
            List<CalendarHealthPlanResponseCreationDTO> results) {

        results.add(CalendarHealthPlanResponseCreationDTO.builder()
                .calendarStatus(calendar.getCalendarStatus())
                .doctorId(doctorId)
                .doctorName(calendar.getDoctor().getName())
                .date(calendar.getDate())
                .startedAt(calendar.getStartTime())
                .endAt(calendar.getEndTime())
                .healthPlanName(healthPlanName)
                .associationStatusResult(associationStatus)
                .message(errorMessage)
                .build());
    }

    public static void addResult(UUID doctorId, String healthPlanName,
            StatusResult associationStatus, String message, List<CalendarHealthPlanResponseCreationDTO> results) {

        results.add(CalendarHealthPlanResponseCreationDTO.builder()
                .calendarStatus(null)
                .doctorId(doctorId)
                .doctorName(null)
                .date(null)
                .startedAt(null)
                .endAt(null)
                .healthPlanName(healthPlanName)
                .associationStatusResult(associationStatus)
                .message(message)
                .build());
    }

    public static CalendarHealthPlanEntity createCalendarHealthPlan(CalendarEntity calendar,
            HealthPlanEntity healthPlan) {
        LocalDateTime validUntil = LocalDateTime.of(calendar.getDate(), calendar.getEndTime());
        return CalendarHealthPlanEntity.builder().calendar(calendar)
                .healthPlan(healthPlan)
                .validUntil(validUntil)
                .build();
    }

}
