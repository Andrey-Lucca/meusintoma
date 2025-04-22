package br.com.meusintoma.modules.consultation.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.modules.calendar.entity.CalendarEntity;
import br.com.meusintoma.modules.calendar.enums.CalendarStatus;
import br.com.meusintoma.modules.calendar.exceptions.CalendarNotFoundException;
import br.com.meusintoma.modules.calendar.repository.CalendarRepository;
import br.com.meusintoma.modules.calendar.services.CalendarService;
import br.com.meusintoma.modules.consultation.dto.ConsultationResponseDTO;
import br.com.meusintoma.modules.consultation.entity.ConsultationEntity;
import br.com.meusintoma.modules.consultation.enums.ConsultationStatus;
import br.com.meusintoma.modules.consultation.mapper.ConsultationMapper;
import br.com.meusintoma.modules.consultation.repository.ConsultationRepository;
import br.com.meusintoma.modules.patient.entity.PatientEntity;
import br.com.meusintoma.modules.patient.exceptions.PatientNotFoundException;
import br.com.meusintoma.modules.patient.repository.PatientRepository;
import br.com.meusintoma.security.utils.AuthValidatorUtils;
import static br.com.meusintoma.utils.RepositoryUtils.findOrThrow;

@Service
public class ConsultationService {

    @Autowired
    ConsultationRepository consultationRepository;

    @Autowired
    CalendarRepository calendarRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    CalendarService calendarService;

    public ConsultationResponseDTO createConsultation(UUID calendarId) {

        UUID patientId = AuthValidatorUtils.getAuthenticatedUserId();
        CalendarEntity calendar = findOrThrow(
                calendarRepository.findById(calendarId),
                () -> new CalendarNotFoundException("Horário Indisponível"));

        PatientEntity patient = findOrThrow(
                patientRepository.findById(patientId),
                () -> new PatientNotFoundException("Paciente inválido para essa operação"));

        ConsultationEntity consultation = ConsultationEntity.builder().calendarSlot(calendar)
                .status(ConsultationStatus.PENDING).patient(patient).build();

        consultationRepository.save(consultation);
        calendarService.updateCalendarStatus(calendar, CalendarStatus.UNAVAILABLE);
        ConsultationResponseDTO consultationResponseDTO = ConsultationMapper.toResponseDTO(consultation);
        return consultationResponseDTO;
    }
}
