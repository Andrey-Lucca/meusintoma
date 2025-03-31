package br.com.meusintoma.modules.calendar.security;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.meusintoma.exceptions.CustomAccessDeniedException;
import br.com.meusintoma.exceptions.InvalidDateException;
import br.com.meusintoma.modules.doctor.repository.DoctorRepository;
import br.com.meusintoma.modules.secretary.repository.SecretaryRepository;

@Component
public class CalendarPermissionValidator {

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    SecretaryRepository secretaryRepository;

    public void validateCalendarOperationPermission(UUID authenticatedUserId, String authenticatedUserRole,
            UUID targetDoctorId) {
        if (authenticatedUserRole.equals("DOCTOR")) {
            if (!authenticatedUserId.equals(targetDoctorId)) {
                throw new CustomAccessDeniedException("Você não tem permissão para alterar a agenda desse médico");
            }
        } else if (authenticatedUserRole.equals("SECRETARY")) {
            boolean isAssociated = secretaryRepository.existsByIdAndDoctorsId(authenticatedUserId,
                    targetDoctorId);
            if (!isAssociated) {
                throw new CustomAccessDeniedException("Você não está associada a esse médico");
            }
        } else {
            throw new CustomAccessDeniedException("Você não pode realizar esta operação");
        }
    }

    public void validateCalendarDatePermission(LocalDate requestDateDTO){
        LocalDate currentDate = LocalDate.now();
        if(requestDateDTO.isBefore(currentDate)){
            throw new InvalidDateException();
        }
    }

}
