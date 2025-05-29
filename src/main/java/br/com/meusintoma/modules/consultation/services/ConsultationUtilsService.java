package br.com.meusintoma.modules.consultation.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.CustomAccessDeniedException;
import br.com.meusintoma.exceptions.globalCustomException.ForbiddenException;
import br.com.meusintoma.exceptions.globalCustomException.UnalterableException;
import br.com.meusintoma.modules.calendar.entity.CalendarEntity;
import br.com.meusintoma.modules.calendar.enums.CalendarStatus;
import br.com.meusintoma.modules.calendar.services.CalendarService;
import br.com.meusintoma.modules.consultation.dto.ConsultationResponseDTO;
import br.com.meusintoma.modules.consultation.entity.ConsultationEntity;
import br.com.meusintoma.modules.consultation.entity.SnapShotInfo;
import br.com.meusintoma.modules.consultation.enums.ConsultationStatus;
import br.com.meusintoma.modules.consultation.exceptions.AlreadyHaveConsultationException;
import br.com.meusintoma.modules.consultation.mapper.ConsultationMapper;
import br.com.meusintoma.modules.consultation.repository.ConsultationRepository;
import br.com.meusintoma.modules.doctorSecretary.services.DoctorSecretaryService;
import br.com.meusintoma.modules.patient.entity.PatientEntity;
import br.com.meusintoma.security.utils.AuthValidatorUtils;
import br.com.meusintoma.utils.helpers.SystemClockUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class ConsultationUtilsService {

    private final CalendarService calendarService;

    private final ConsultationRepository consultationRepository;

    private final DoctorSecretaryService doctorSecretaryService;

    private List<ConsultationStatus> statuses = new ArrayList<>(List.of(
            ConsultationStatus.PENDING,
            ConsultationStatus.CONFIRMED,
            ConsultationStatus.RESCHEDULED));

    public void persistChanges(ConsultationEntity consultation) {
        consultationRepository.save(consultation);
    }

    public boolean dateAndHourAvaliable(LocalDate wantedDate, LocalTime wantedTime) {
        LocalTime timeSystem = SystemClockUtils.getCurrentTime();
        LocalDate dateSystem = SystemClockUtils.getCurrentDate();

        if (wantedDate.isEqual(dateSystem)) {
            return wantedTime.minusHours(2).isAfter(timeSystem);
        }

        return wantedDate.isAfter(dateSystem);
    }

    public Optional<UUID> returnUserId(UUID userId) {
        return Optional.ofNullable(userId);
    }

    public void validateUserPermission(ConsultationEntity consultation) {
        UUID userId = AuthValidatorUtils.getAuthenticatedUserId();
        List<UUID> allowedIds = new ArrayList<>();
        returnUserId(consultation.getDoctorId()).ifPresent(allowedIds::add);
        returnUserId(consultation.getPatient().getId()).ifPresent(allowedIds::add);

        List<UUID> secretaryIds = doctorSecretaryService.getAllSecretaryIdsByDoctorId(consultation.getDoctorId());
        allowedIds.addAll(secretaryIds);

        if (!allowedIds.contains(userId)) {
            throw new CustomAccessDeniedException("Você não pode realizar essa operação");
        }
    }

    public SnapShotInfo createSnapshot(ConsultationEntity consultation) {
        SnapShotInfo snapshot = SnapShotInfo.builder().date(consultation.getCalendarSlot().getDate())
                .endTime(consultation.getCalendarSlot().getEndTime())
                .startTime(consultation.getCalendarSlot().getStartTime()).build();
        return snapshot;
    }

    public static SnapShotInfo createConsultationSnapshot() {
        SnapShotInfo snapshot = SnapShotInfo.builder()
                .date(null)
                .startTime(null)
                .endTime(null)
                .build();
        return snapshot;
    }

    public static ConsultationEntity createConsultation(CalendarEntity calendar, String healthPlan,
            PatientEntity patient,
            SnapShotInfo snapshot) {
        ConsultationEntity consultation = ConsultationEntity.builder().calendarSlot(calendar)
                .status(ConsultationStatus.PENDING).patient(patient)
                .doctorId(calendar.getDoctor().getId())
                .canceledBy(null)
                .snapshot(snapshot)
                .healthPlan(healthPlan)
                .build();
        return consultation;
    }

    public void checkConsultationStatus(ConsultationEntity consultation) {
        if (!statuses.contains(consultation.getStatus())) {
            throw new UnalterableException("Consulta");
        }
    }

    public void checkConsultationStatusForNotes(ConsultationEntity consultation,
            List<ConsultationStatus> consultationStatus) {
        if (!consultationStatus.contains(consultation.getStatus())) {
            throw new ForbiddenException("Não é possível inserir notas nessa consulta");
        }
    }

    public void checkUserAction(ConsultationStatus status) {
        String role = AuthValidatorUtils.getCurrentUserRole();

        boolean patientTryingInvalidAction = role.equals("PATIENT") && (status != ConsultationStatus.CONFIRMED);
        boolean staffTryingInvalidAction = (role.equals("SECRETARY") || role.equals("DOCTOR"))
                && (status != ConsultationStatus.COMPLETED);

        if (patientTryingInvalidAction || staffTryingInvalidAction) {
            throw new CustomAccessDeniedException("Você não tem permissão para realizar essa ação");
        }
    }

    public void checkStatusAndPermissions(ConsultationEntity consultation) {
        validateUserPermission(consultation);
        checkConsultationStatus(consultation);
    }

    public void changeCalendarStatus(ConsultationEntity consultation, CalendarStatus status) {
        boolean isDateAndHourWithInPeriod = dateAndHourAvaliable(consultation.getCalendarSlot().getDate(),
                consultation.getCalendarSlot().getStartTime());
        if (isDateAndHourWithInPeriod) {
            calendarService.updateCalendarStatus(consultation.getCalendarSlot(), status);
        }
    }

    public static List<ConsultationResponseDTO> getFilteredConsultationByDayAndHour(
            List<ConsultationEntity> consultations) {
        return consultations.stream()
                .filter(consultation -> {
                    LocalDate consultationDate;
                    LocalTime consultationTime;
                    boolean isStatusCanceled = consultation.getStatus() == ConsultationStatus.CANCELLED;
                    boolean isCalendarSlotNull = consultation.getCalendarSlot() == null;

                    if (isStatusCanceled || isCalendarSlotNull) {
                        consultationDate = consultation.getSnapshot().getDate();
                        consultationTime = consultation.getSnapshot().getStartTime();
                    } else {
                        consultationDate = consultation.getCalendarSlot().getDate();
                        consultationTime = consultation.getCalendarSlot().getStartTime();
                    }

                    LocalDate today = SystemClockUtils.getCurrentDate();
                    LocalTime now = SystemClockUtils.getCurrentTime();

                    if (consultationDate.equals(today)) {
                        return !consultationTime.isBefore(now);
                    }
                    return consultationDate.isAfter(today);
                })
                .map(ConsultationMapper::toResponseDTO)
                .toList();
    }

    public void cancelConsultation(ConsultationEntity consultation, SnapShotInfo snapshot) {
        consultation.setCalendarSlot(null);
        consultation.setSnapshot(snapshot);
        consultation.setStatus(ConsultationStatus.CANCELLED);
        consultation.setCanceledBy(AuthValidatorUtils.getCurrentUserRole());

        persistChanges(consultation);
    }

    public void updateConsultationStatusAndCalendar(ConsultationEntity consultation, CalendarEntity calendar,
            ConsultationStatus status) {
        consultation.setCalendarSlot(calendar);
        consultation.setStatus(status);
        persistChanges(consultation);
        changeCalendarStatus(consultation, CalendarStatus.UNAVAILABLE);
    }

    public void alreadyHaveConsultation(List<ConsultationEntity> consultations, UUID doctorId) {

        boolean exists = consultations.stream()
                .anyMatch(c -> c.getDoctorId().equals(doctorId) && statuses.contains(c.getStatus()));

        if (exists) {
            throw new AlreadyHaveConsultationException("Consulta já marcada com esse médico");
        }
    }

    public void consultationSecurityCheck(ConsultationEntity consultation, List<ConsultationStatus> statuses) {
        validateUserPermission(consultation);
        checkConsultationStatusForNotes(consultation, statuses);
    }
}
