package br.com.meusintoma.modules.calendar.security;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

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
                throw new AccessDeniedException("Médico só pode modificar seu próprio calendário");
            }
        } else if (authenticatedUserRole.equals("SECRETARY")) {
            boolean isAssociated = secretaryRepository.existsByIdAndDoctorsId(authenticatedUserId,
                    targetDoctorId);
            if (!isAssociated) {
                throw new AccessDeniedException("Secretária não está associada a este médico");
            }
        } else {
            throw new AccessDeniedException("Tipo de usuário não autorizado para esta operação");
        }
    }

}
