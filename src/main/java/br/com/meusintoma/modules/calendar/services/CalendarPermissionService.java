package br.com.meusintoma.modules.calendar.services;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.modules.calendar.security.CalendarPermissionValidator;
import br.com.meusintoma.security.utils.AuthValidatorUtils;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class CalendarPermissionService {

    @Autowired
    private CalendarPermissionValidator calendarPermissionValidator;

    public void validatePermissionCalendar(HttpServletRequest request, UUID doctorId, LocalDate requestDate){
        var userId = AuthValidatorUtils.getAuthenticatedUserId(request);
        var currentUserRole = AuthValidatorUtils.getCurrentUserRole();
        calendarPermissionValidator.validateCalendarDatePermission(requestDate);
        calendarPermissionValidator.validateCalendarOperationPermission(userId, currentUserRole, doctorId);
    }

}
