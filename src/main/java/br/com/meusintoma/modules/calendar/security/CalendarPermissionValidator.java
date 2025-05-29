package br.com.meusintoma.modules.calendar.security;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Component;

import br.com.meusintoma.exceptions.globalCustomException.CustomAccessDeniedException;
import br.com.meusintoma.exceptions.globalCustomException.InvalidDateException;
import br.com.meusintoma.modules.doctorSecretary.services.DoctorSecretaryService;
import br.com.meusintoma.utils.helpers.SystemClockUtils;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor

public class CalendarPermissionValidator {

    private final DoctorSecretaryService doctorSecretaryService;

    public void validateCalendarOperationPermission(UUID authenticatedUserId, String authenticatedUserRole,
            UUID targetDoctorId) {
        if (authenticatedUserRole.equals("DOCTOR")) {
            if (!authenticatedUserId.equals(targetDoctorId)) {
                throw new CustomAccessDeniedException("Você não tem permissão para alterar a agenda desse médico");
            }
        } else if (authenticatedUserRole.equals("SECRETARY")) {
            doctorSecretaryService.checkAssociation(targetDoctorId, authenticatedUserId);

        } else {
            throw new CustomAccessDeniedException("Você não pode realizar esta operação");
        }
    }

    public void validateCalendarDatePermission(LocalDate requestDateDTO) {

        if(requestDateDTO == null) return;
        LocalDate currentDate = SystemClockUtils.getCurrentDate();
        if (requestDateDTO.isBefore(currentDate)) {
            throw new InvalidDateException("A data fornecida tem que ser igual ou maior do que a data atual");
        }
    }

}
