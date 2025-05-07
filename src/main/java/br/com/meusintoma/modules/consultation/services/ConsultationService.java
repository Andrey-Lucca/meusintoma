package br.com.meusintoma.modules.consultation.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.CustomAccessDeniedException;
import br.com.meusintoma.exceptions.globalCustomException.NoContentException;
import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.exceptions.globalCustomException.UnalterableException;
import br.com.meusintoma.modules.calendar.entity.CalendarEntity;
import br.com.meusintoma.modules.calendar.enums.CalendarStatus;
import br.com.meusintoma.modules.calendar.exceptions.CalendarNotFoundException;
import br.com.meusintoma.modules.calendar.repository.CalendarRepository;
import br.com.meusintoma.modules.calendar.services.CalendarService;
import br.com.meusintoma.modules.consultation.dto.ConsultationCanceledResponseDTO;
import br.com.meusintoma.modules.consultation.dto.ConsultationResponseDTO;
import br.com.meusintoma.modules.consultation.dto.RescheduleConsultationDTO;
import br.com.meusintoma.modules.consultation.entity.ConsultationEntity;
import br.com.meusintoma.modules.consultation.entity.SnapShotInfo;
import br.com.meusintoma.modules.consultation.enums.ConsultationStatus;
import br.com.meusintoma.modules.consultation.mapper.ConsultationMapper;
import br.com.meusintoma.modules.consultation.repository.ConsultationRepository;
import br.com.meusintoma.modules.patient.entity.PatientEntity;
import br.com.meusintoma.modules.patient.exceptions.PatientNotFoundException;
import br.com.meusintoma.modules.patient.repository.PatientRepository;
import br.com.meusintoma.security.utils.AuthValidatorUtils;
import br.com.meusintoma.utils.RepositoryUtils;
import br.com.meusintoma.utils.SystemClockUtils;

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

        @Autowired
        ConsultationUtilsService consultationUtilsService;

        public ConsultationEntity findConsultation(UUID consultationId) {
                ConsultationEntity consultation = RepositoryUtils.findOrThrow(
                                consultationRepository.findById(consultationId),
                                () -> new NotFoundException("Consulta"));
                return consultation;
        }

        public ConsultationEntity findConsultationWithCalendar(UUID id) {
                return RepositoryUtils.findOrThrow(
                                consultationRepository.findWithCalendarSlotById(id),
                                () -> new NotFoundException("Consulta"));
        }

        public ConsultationResponseDTO createConsultation(UUID calendarId) {
                UUID patientId = AuthValidatorUtils.getAuthenticatedUserId();
                CalendarEntity calendar = findOrThrow(
                                calendarRepository.findByIdWithDoctorAndSecretary(calendarId),
                                () -> new CalendarNotFoundException("Horário Indisponível"));

                boolean isDateAndHourWithInPeriod = consultationUtilsService.dateAndHourAvaliable(calendar.getDate(),
                                calendar.getStartTime());

                CalendarService.checkCalendarStatusAndHour(CalendarStatus.AVAILABLE, isDateAndHourWithInPeriod);

                PatientEntity patient = findOrThrow(
                                patientRepository.findById(patientId),
                                () -> new PatientNotFoundException("Paciente inválido para essa operação"));

                SnapShotInfo snapshot = SnapShotInfo.builder()
                                .date(null)
                                .startTime(null)
                                .endTime(null)
                                .build();

                List<ConsultationEntity> consultations = consultationRepository.findAllByPatientId(patientId);

                consultationUtilsService.alredyHaveConsultation(consultations, calendar.getDoctor().getId());

                ConsultationEntity consultation = ConsultationEntity.builder().calendarSlot(calendar)
                                .status(ConsultationStatus.PENDING).patient(patient)
                                .doctorId(calendar.getDoctor().getId())
                                .secretaryId(calendar.getDoctor().getSecretary() != null
                                                ? calendar.getDoctor().getSecretary().getId()
                                                : null)
                                .canceledBy(null)
                                .snapshot(snapshot)
                                .build();

                consultationRepository.save(consultation);
                consultationUtilsService.changeCalendarStatus(consultation, CalendarStatus.UNAVAILABLE);
                ConsultationResponseDTO consultationResponseDTO = ConsultationMapper.toResponseDTO(consultation);
                return consultationResponseDTO;
        }

        public List<ConsultationResponseDTO> getConsultations() {
                UUID userId = AuthValidatorUtils.getAuthenticatedUserId();
                String userRole = AuthValidatorUtils.getCurrentUserRole();
                List<ConsultationEntity> consultations = new ArrayList<ConsultationEntity>();

                switch (userRole) {
                        case "DOCTOR":
                                consultations = consultationRepository.findAllByDoctorId(userId);
                                break;
                        case "SECRETARY":
                                consultations = consultationRepository.findAllBySecretaryId(userId);
                                break;
                        case "PATIENT":
                                consultations = consultationRepository.findAllByPatientId(userId);
                                break;
                        default:
                                throw new CustomAccessDeniedException("Você não tem permissões suficientes");
                }
                if (consultations.isEmpty()) {
                        throw new NoContentException("Não existe nada para ser mostrado");
                }
                return consultations.stream()
                                .filter(consultation -> {
                                        LocalDate date;
                                        LocalTime time;

                                        if (consultation.getStatus() == ConsultationStatus.CANCELLED
                                                        || consultation.getCalendarSlot() == null) {
                                                date = consultation.getSnapshot().getDate();
                                                time = consultation.getSnapshot().getStartTime();
                                        } else {
                                                date = consultation.getCalendarSlot().getDate();
                                                time = consultation.getCalendarSlot().getStartTime();
                                        }

                                        LocalDate today = SystemClockUtils.getCurrentDate();
                                        LocalTime now = SystemClockUtils.getCurrentTime();

                                        if (date.equals(today)) {
                                                return !time.isBefore(now);
                                        }
                                        return date.isAfter(today);
                                })
                                .map(ConsultationMapper::toResponseDTO)
                                .toList();
        }

        public ConsultationResponseDTO reschedule(UUID consultationId, RescheduleConsultationDTO rescheduleDTO) {
                ConsultationEntity consultation = RepositoryUtils.findOrThrow(
                                consultationRepository.findById(consultationId),
                                () -> new NotFoundException("Consulta"));

                CalendarEntity calendar = findOrThrow(
                                calendarRepository.findByIdWithDoctorAndSecretary(rescheduleDTO.getNewCalendarId()),
                                () -> new CalendarNotFoundException("Horário Indisponível"));

                consultationUtilsService.checkStatusAndPermissions(consultation);
                consultationUtilsService.changeCalendarStatus(consultation, CalendarStatus.AVAILABLE);
                consultationUtilsService.updateConsultationStatusAndCalendar(consultation, calendar,
                                ConsultationStatus.RESCHEDULED);
                return ConsultationMapper.toResponseDTO(consultation);

        }

        public ConsultationResponseDTO changeConsultationStatus(UUID consultationId, ConsultationStatus status) {
                consultationUtilsService.checkUserAction(status);
                ConsultationEntity consultation = findConsultation(consultationId);
                consultationUtilsService.checkStatusAndPermissions(consultation);
                boolean isDateAndHourWithInPeriod = consultationUtilsService.dateAndHourAvaliable(
                                consultation.getCalendarSlot().getDate(),
                                consultation.getCalendarSlot().getStartTime());
                if (!isDateAndHourWithInPeriod && status != ConsultationStatus.CONFIRMED) {
                        throw new UnalterableException("Consulta");
                }
                consultation.setStatus(status);
                consultationRepository.save(consultation);
                return ConsultationMapper.toResponseDTO(consultation);
        }

        public ConsultationCanceledResponseDTO cancelConsultation(UUID consultationId) {
                ConsultationEntity consultation = RepositoryUtils.findOrThrow(
                                consultationRepository.findById(consultationId),
                                () -> new NotFoundException("Consulta"));

                consultationUtilsService.checkStatusAndPermissions(consultation);

                SnapShotInfo snapshot = consultationUtilsService.createSnapshot(consultation);

                consultationUtilsService.changeCalendarStatus(consultation, CalendarStatus.AVAILABLE);
                consultationUtilsService.cancelConsultation(consultation, snapshot);
                return ConsultationMapper.toCanceledResponseDTO(consultation);
        }
}
