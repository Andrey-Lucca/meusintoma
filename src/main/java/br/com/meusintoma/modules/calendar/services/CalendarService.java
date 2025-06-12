package br.com.meusintoma.modules.calendar.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.InvalidDateException;
import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.calendar.dto.CalendarConsultationResponseDTO;
import br.com.meusintoma.modules.calendar.dto.CalendarResponseDTO;
import br.com.meusintoma.modules.calendar.dto.CalendarConsultationRequestDTO;
import br.com.meusintoma.modules.calendar.entity.CalendarEntity;
import br.com.meusintoma.modules.calendar.enums.CalendarStatus;
import br.com.meusintoma.modules.calendar.enums.IntervalChoice;
import br.com.meusintoma.modules.calendar.exceptions.CalendarNotFoundException;
import br.com.meusintoma.modules.calendar.exceptions.CalendarStatusException;
import br.com.meusintoma.modules.calendar.exceptions.UnavaliableTimeException;
import br.com.meusintoma.modules.calendar.mapper.CalendarMapperDTO;
import br.com.meusintoma.modules.calendar.repository.CalendarRepository;
import br.com.meusintoma.utils.helpers.GenericUtils;
import br.com.meusintoma.utils.helpers.RepositoryUtils;
import br.com.meusintoma.utils.helpers.SystemClockUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;

    private final CalendarPermissionService calendarPermissionService;

    private static final Set<IntervalChoice> AUTOMATIC_SYSTEM_DATE_CHOICES = EnumSet.of(IntervalChoice.DAILY,
            IntervalChoice.WEEKLY);

    public boolean doctorAlreadyHaveCalendarSlot(UUID doctorId, LocalDate targetDate, LocalTime startedAt) {
        return calendarRepository.checkDoctorCalendarSlot(doctorId, targetDate, startedAt);
    }

    public void verifyDoctorHasCalendar(List<CalendarEntity> calendars) {
        GenericUtils.checkIsEmptyList(calendars);
    }

    public CalendarEntity findByCalendarIdWithDoctor(UUID calendarId) {
        return RepositoryUtils.findOrThrow(
                calendarRepository.findByIdWithDoctor(calendarId),
                () -> new CalendarNotFoundException("Horário Indisponível"));
    }

    public List<CalendarResponseDTO> getDoctorCalendar(UUID doctorId) {
        final LocalDate currentDate = SystemClockUtils.getCurrentDate();
        final LocalTime currentTime = SystemClockUtils.getCurrentTime();
        List<CalendarEntity> calendars = calendarRepository.findAllByDoctorId(doctorId);
        verifyDoctorHasCalendar(calendars);
        return calendars.stream()
                .filter(calendar -> isCalendarAvailableAfterNow(calendar, currentDate, currentTime))
                .map(CalendarMapperDTO::toResponseDTO)
                .toList();
    }

    public List<CalendarConsultationResponseDTO> getCalendarConsultation(CalendarConsultationRequestDTO requestDTO) {
        UUID doctorId = requestDTO.getDoctorId();
        LocalDate startDate = getStartDate(requestDTO.getIntervalChoice(), requestDTO.getStartDate());
        checkCalendarPermissions(doctorId, startDate);

        return switch (requestDTO.getIntervalChoice()) {
            case SPECIFIC -> {
                CalendarConsultationResponseDTO item = getCalendarBySpecificalDayAndHour(requestDTO);
                yield List.of(item);
            }
            case INTERVAL -> getCalendarByInterval(doctorId, startDate, requestDTO.getFinalDate());
            case DAILY -> getCalendarByInterval(doctorId, startDate, startDate);
            case WEEKLY ->
                getCalendarByInterval(doctorId, startDate, startDate.plusDays(7)).stream().filter(c -> {
                    if (c.getDate().equals(startDate)) {
                        return !c.getStartTime().isBefore(SystemClockUtils.getCurrentTime());
                    }
                    return !c.getDate().isBefore(startDate);
                }).sorted(Comparator.comparing(CalendarConsultationResponseDTO::getDate)).toList();
            default -> throw new IllegalArgumentException("Tipo de intervalo inválido.");
        };
    }

    public CalendarResponseDTO updateCalendarStatus(UUID calendarId, CalendarStatus status) {
        CalendarEntity calendar = getCalendarByIdAndCheckDoctorPermission(calendarId);
        checkActionTypeAndCalendarStatus(calendar);
        CalendarEntity updated = updateCalendarStatus(calendar, status);
        return CalendarMapperDTO.toResponseDTO(updated);
    }

    public CalendarEntity updateCalendarStatus(CalendarEntity calendar, CalendarStatus status) {
        calendar.setCalendarStatus(status);
        return calendarRepository.save(calendar);
    }

    public void deleteCalendarById(UUID calendarId) {
        CalendarEntity calendar = getCalendarByIdAndCheckDoctorPermission(calendarId);
        calendarRepository.delete(calendar);
    }

    public static void checkCalendarStatus(CalendarStatus status) {
        if (status != CalendarStatus.AVAILABLE) {
            throw new CalendarStatusException();
        }
    }

    private static void checkActionTypeAndCalendarStatus(CalendarEntity calendar) {
        if (calendar.getCalendarStatus().equals(CalendarStatus.UNAVAILABLE)) {
            throw new CalendarStatusException();

        }
    }

    public static void checkCalendarStatusAndHour(CalendarStatus status, boolean isDateAndHourWithInPeriod) {
        if (status != CalendarStatus.AVAILABLE || !isDateAndHourWithInPeriod) {
            throw new UnavaliableTimeException("O horário se encontra indisponível");
        }
    }

    public void checkCalendarPermissions(UUID doctorId, LocalDate startDate) {
        calendarPermissionService.validatePermissionCalendar(doctorId, Optional.ofNullable(startDate));
    }

    private CalendarEntity getCalendarByIdAndCheckDoctorPermission(UUID calendarId) {
        CalendarEntity calendar = calendarRepository.findByIdWithDoctor(calendarId)
                .orElseThrow(() -> new CalendarNotFoundException("Calendário não encontrado"));
        checkCalendarPermissions(calendar.getDoctor().getId(), calendar.getDate());
        return calendar;
    }

    private LocalDate getStartDate(IntervalChoice choice, LocalDate startDate) {
        return AUTOMATIC_SYSTEM_DATE_CHOICES.contains(choice)
                ? SystemClockUtils.getCurrentDate()
                : startDate;
    }

    private CalendarConsultationResponseDTO getCalendarBySpecificalDayAndHour(
            CalendarConsultationRequestDTO requestDTO) {
        CalendarEntity calendar = RepositoryUtils.findOrThrow(
                calendarRepository.findByDayAndHour(requestDTO.getStartDate(), requestDTO.getStartTime(),
                        requestDTO.getDoctorId()),
                () -> new NotFoundException("Calendário"));
        return CalendarMapperDTO.toCalendarConsultationResponse(calendar);
    }

    private List<CalendarConsultationResponseDTO> getCalendarByInterval(
            UUID doctorId, LocalDate startDate, LocalDate finalDate) {
        checkEndDate(startDate, finalDate);

        List<CalendarEntity> calendars = RepositoryUtils.findOrThrow(
                calendarRepository.findBySpecificalInterval(startDate, finalDate, doctorId),
                () -> new NotFoundException("Calendário"));

        GenericUtils.checkIsEmptyList(calendars);

        return calendars.stream().map(CalendarMapperDTO::toCalendarConsultationResponse).toList();
    }

    private void checkEndDate(LocalDate startDate, LocalDate endDate) {
        boolean isEndDateBiggerThanStartDate = endDate.isBefore(startDate);
        if (isEndDateBiggerThanStartDate) {
            throw new InvalidDateException("Data final maior que a inicial");
        }
    }

    private boolean isCalendarAvailableAfterNow(CalendarEntity calendar, LocalDate today, LocalTime now) {
        return calendar.getCalendarStatus().equals(CalendarStatus.AVAILABLE) &&
                (calendar.getDate().isAfter(today) ||
                        (calendar.getDate().isEqual(today) && calendar.getStartTime().isAfter(now)));
    }

}
