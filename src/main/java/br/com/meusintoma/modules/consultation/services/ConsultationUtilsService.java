package br.com.meusintoma.modules.consultation.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.CustomAccessDeniedException;
import br.com.meusintoma.exceptions.globalCustomException.UnalterableException;
import br.com.meusintoma.modules.calendar.entity.CalendarEntity;
import br.com.meusintoma.modules.calendar.enums.CalendarStatus;
import br.com.meusintoma.modules.calendar.services.CalendarService;
import br.com.meusintoma.modules.consultation.entity.ConsultationEntity;
import br.com.meusintoma.modules.consultation.entity.SnapShotInfo;
import br.com.meusintoma.modules.consultation.enums.ConsultationStatus;
import br.com.meusintoma.modules.consultation.exceptions.AlreadyHaveConsultationException;
import br.com.meusintoma.modules.consultation.repository.ConsultationRepository;
import br.com.meusintoma.security.utils.AuthValidatorUtils;
import br.com.meusintoma.utils.SystemClockUtils;

@Service
public class ConsultationUtilsService {

    @Autowired
    CalendarService calendarService;

    @Autowired
    ConsultationRepository consultationRepository;

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
        returnUserId(consultation.getSecretaryId()).ifPresent(allowedIds::add);

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

    public void checkConsultationStatus(ConsultationEntity consultation) {
        if (!statuses.contains(consultation.getStatus())) {
            throw new UnalterableException("Consulta");
        }
    }

    public void checkUserAction(ConsultationStatus status) {
        String role = AuthValidatorUtils.getCurrentUserRole();

        boolean patientTryingInvalidAction = role.equals("PATIENT") && status != ConsultationStatus.CONFIRMED;
        boolean staffTryingInvalidAction = (role.equals("SECRETARY") || role.equals("DOCTOR"))
                && status != ConsultationStatus.COMPLETED;

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

    public void alredyHaveConsultation(List<ConsultationEntity> consultations, UUID doctorId) {

        boolean exists = consultations.stream()
                .anyMatch(c -> c.getDoctorId().equals(doctorId) && statuses.contains(c.getStatus()));

        if (exists) {
            throw new AlreadyHaveConsultationException("Consulta já marcada com esse médico");
        }
    }
}
