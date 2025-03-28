package br.com.meusintoma.modules.calendar.services;

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

    public void validatePermissionCalendar(HttpServletRequest request, UUID doctorId){
        var userId = AuthValidatorUtils.getAuthenticatedUserId(request);
        var currentUserRole = AuthValidatorUtils.getCurrentUserRole();
        calendarPermissionValidator.validateCalendarOperationPermission(userId, currentUserRole, doctorId);
    }

}
