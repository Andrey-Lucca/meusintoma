package br.com.meusintoma.modules.calendarHealthPlan.dto;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import lombok.Data;

@Data
public class CalendarHealthPlanAssociateRequestDTO {
    
    private Map<UUID, Map<UUID, Set<String>>> doctorCalendarsHealthPlansMap;
}
