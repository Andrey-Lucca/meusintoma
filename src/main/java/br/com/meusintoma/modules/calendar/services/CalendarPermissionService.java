package br.com.meusintoma.modules.calendar.services;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.meusintoma.modules.calendar.security.CalendarPermissionValidator;
import br.com.meusintoma.security.utils.AuthValidatorUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class CalendarPermissionService {

    private final CalendarPermissionValidator calendarPermissionValidator;

    public void validatePermissionCalendar(UUID doctorId, Optional<LocalDate> requestDate) {
        var userId = AuthValidatorUtils.getAuthenticatedUserId();
        var currentUserRole = AuthValidatorUtils.getCurrentUserRole();
        requestDate.ifPresent(date -> 
        calendarPermissionValidator.validateCalendarDatePermission(date));
        calendarPermissionValidator.validateCalendarOperationPermission(userId, currentUserRole, doctorId);
    }

}
