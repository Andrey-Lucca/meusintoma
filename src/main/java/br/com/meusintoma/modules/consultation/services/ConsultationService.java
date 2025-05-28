package br.com.meusintoma.modules.consultation.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.CustomAccessDeniedException;
import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.exceptions.globalCustomException.UnalterableException;
import br.com.meusintoma.modules.calendar.entity.CalendarEntity;
import br.com.meusintoma.modules.calendar.enums.CalendarStatus;
import br.com.meusintoma.modules.calendar.exceptions.UnavaliableTimeException;
import br.com.meusintoma.modules.calendar.repository.CalendarRepository;
import br.com.meusintoma.modules.calendar.services.CalendarService;
import br.com.meusintoma.modules.calendarHealthPlan.entity.CalendarHealthPlanEntity;
import br.com.meusintoma.modules.calendarHealthPlan.repository.CalendarHealthPlanRepository;
import br.com.meusintoma.modules.consultation.dto.ConsultationCanceledResponseDTO;
import br.com.meusintoma.modules.consultation.dto.ConsultationResponseDTO;
import br.com.meusintoma.modules.consultation.dto.RescheduleConsultationDTO;
import br.com.meusintoma.modules.consultation.entity.ConsultationEntity;
import br.com.meusintoma.modules.consultation.entity.SnapShotInfo;
import br.com.meusintoma.modules.consultation.enums.ConsultationStatus;
import br.com.meusintoma.modules.consultation.mapper.ConsultationMapper;
import br.com.meusintoma.modules.consultation.repository.ConsultationRepository;
import br.com.meusintoma.modules.doctorSecretary.services.DoctorSecretaryService;
import br.com.meusintoma.modules.patient.entity.PatientEntity;
import br.com.meusintoma.modules.patient.repository.PatientRepository;
import br.com.meusintoma.modules.patient.services.PatientService;
import br.com.meusintoma.security.utils.AuthValidatorUtils;
import br.com.meusintoma.utils.helpers.GenericUtils;
import br.com.meusintoma.utils.helpers.RepositoryUtils;

@Service
public class ConsultationService {

        @Autowired
        ConsultationRepository consultationRepository;

        @Autowired
        CalendarRepository calendarRepository;

        @Autowired
        PatientRepository patientRepository;

        @Autowired
        PatientService patientService;

        @Autowired
        CalendarService calendarService;

        @Autowired
        ConsultationUtilsService consultationUtilsService;

        @Autowired
        CalendarHealthPlanRepository calendarHealthPlanRepository;

        @Autowired
        DoctorSecretaryService doctorSecretaryService;

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

        public ConsultationResponseDTO createConsultation(UUID calendarId, String healthPlan) {
                UUID patientId = AuthValidatorUtils.getAuthenticatedUserId();
                CalendarEntity calendar = calendarService.findByCalendarIdWithDoctor(calendarId);
                List<CalendarHealthPlanEntity> linkedPlansList = calendarHealthPlanRepository
                                .findLinkedHealthPlansByCalendarId(calendarId);

                calendarHasHealthPlan(linkedPlansList, healthPlan);

                boolean isDateAndHourWithInPeriod = consultationUtilsService.dateAndHourAvaliable(calendar.getDate(),
                                calendar.getStartTime());

                CalendarService.checkCalendarStatusAndHour(CalendarStatus.AVAILABLE, isDateAndHourWithInPeriod);

                PatientEntity patient = patientService.findPatient(patientId);

                SnapShotInfo snapshot = ConsultationUtilsService.createConsultationSnapshot();

                List<ConsultationEntity> consultations = consultationRepository.findAllByPatientId(patientId);

                consultationUtilsService.alreadyHaveConsultation(consultations, calendar.getDoctor().getId());

                ConsultationEntity consultation = ConsultationUtilsService.createConsultation(calendar, healthPlan,
                                patient, snapshot);

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
                                List<UUID> doctorIds = doctorSecretaryService.getAllDoctorsIdsBySecretaryId(userId);
                                consultations = consultationRepository.findAllByDoctorIdIn(doctorIds);
                                break;
                        case "PATIENT":
                                consultations = consultationRepository.findAllByPatientId(userId);
                                break;
                        default:
                                throw new CustomAccessDeniedException("Você não tem permissões suficientes");
                }

                GenericUtils.checkIsEmptyList(consultations);

                return ConsultationUtilsService.getFilteredConsultationByDayAndHour(consultations);
        }

        public ConsultationResponseDTO reschedule(UUID consultationId, RescheduleConsultationDTO rescheduleDTO) {
                ConsultationEntity consultation = findConsultationWithCalendar(consultationId);

                CalendarEntity calendar = calendarService
                                .findByCalendarIdWithDoctor(rescheduleDTO.getNewCalendarId());

                List<CalendarHealthPlanEntity> linkedPlansList = calendarHealthPlanRepository
                                .findLinkedHealthPlansByCalendarId(calendar.getId());

                calendarHasHealthPlan(linkedPlansList, consultation.getHealthPlan());

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

                LocalDate consultationDate = consultation.getCalendarSlot().getDate();
                LocalTime consultationTime = consultation.getCalendarSlot().getStartTime();
                boolean isDateAndHourWithInPeriod = consultationUtilsService.dateAndHourAvaliable(consultationDate,
                                consultationTime);

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

        private void calendarHasHealthPlan(List<CalendarHealthPlanEntity> calendarHealthPlans, String healthPlan) {
                boolean hasPlan = calendarHealthPlans.stream()
                                .anyMatch(chp -> chp.getHealthPlan().getName().equals(healthPlan));
                if (!hasPlan) {
                        throw new UnavaliableTimeException("Esse plano não está disponível para esse horário");
                }
        }
}
