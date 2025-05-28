package br.com.meusintoma.modules.calendar.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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
import br.com.meusintoma.modules.calendar.exceptions.NoDoctorCalendarException;
import br.com.meusintoma.modules.calendar.exceptions.UnavaliableTimeException;
import br.com.meusintoma.modules.calendar.mapper.CalendarMapperDTO;
import br.com.meusintoma.modules.calendar.repository.CalendarRepository;
import br.com.meusintoma.utils.helpers.GenericUtils;
import br.com.meusintoma.utils.helpers.RepositoryUtils;
import br.com.meusintoma.utils.helpers.SystemClockUtils;

@Service
public class CalendarService {

    @Autowired
    CalendarRepository calendarRepository;

    @Autowired
    CalendarPermissionService calendarPermissionService;

    public boolean doctorAlreadyHaveCalendarSlot(UUID doctorId, LocalDate targetDate, LocalTime startedAt) {
        return calendarRepository.checkDoctorCalendarSlot(doctorId, targetDate, startedAt);
    }

    public void verifyDoctorHasCalendar(UUID doctorId) {
        if (!calendarRepository.existsByDoctorId(doctorId)) {
            throw new NoDoctorCalendarException("Nenhum calendário encontrado para o médico");
        }
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
        return calendars.stream().filter(calendar -> calendar.getCalendarStatus().equals(CalendarStatus.AVAILABLE))
                .filter(calendar -> {
                    LocalDate calendarDate = calendar.getDate();
                    LocalTime startTime = calendar.getStartTime();
                    if (calendarDate.isEqual(currentDate)) {
                        return startTime.isAfter(currentTime);
                    }

                    return calendarDate.isAfter(currentDate);
                })
                .map(CalendarMapperDTO::toResponseDTO).toList();
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
            case INTERVAL -> getCalendarBySpecificalInterval(doctorId, startDate, requestDTO.getFinalDate());
            case DAILY -> getCalendarBySpecificalInterval(doctorId, startDate, startDate);
            case WEEKLY ->
                getCalendarBySpecificalInterval(doctorId, startDate, startDate.plusDays(7)).stream().filter(c -> {
                    if (c.getDate().equals(startDate)) {
                        return !c.getStartTime().isBefore(SystemClockUtils.getCurrentTime());
                    }
                    return !c.getDate().isBefore(startDate);
                }).sorted(Comparator.comparing(CalendarConsultationResponseDTO::getDate)).toList();
            default -> throw new IllegalArgumentException("Tipo de intervalo inválido.");
        };
    }

    public CalendarResponseDTO updateCalendarStatus(UUID calendarId, CalendarStatus status) {
        CalendarEntity calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new CalendarNotFoundException("Horário não encontrado"));

        return updateCalendarStatusAndReturn(calendar, status);
    }

    public CalendarResponseDTO updateCalendarStatusAndReturn(CalendarEntity calendar, CalendarStatus status) {
        calendar.setCalendarStatus(status);
        calendarRepository.save(calendar);
        return CalendarMapperDTO.toResponseDTO(calendar);
    }

    public void updateCalendarStatus(CalendarEntity calendar, CalendarStatus status) {
        calendar.setCalendarStatus(status);
        calendarRepository.save(calendar);
    }

    public void deleteCalendarById(UUID calendarId) {
        CalendarEntity calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new CalendarNotFoundException("Calendário não encontrado"));
        calendarRepository.delete(calendar);
    }

    public static void checkCalendarStatus(CalendarStatus status) {
        if (status != CalendarStatus.AVAILABLE) {
            throw new CalendarStatusException();
        }
    }

    public static void checkCalendarStatusAndHour(CalendarStatus status, boolean isDateAndHourWithInPeriod) {
        if (status != CalendarStatus.AVAILABLE || !isDateAndHourWithInPeriod) {
            throw new UnavaliableTimeException("O horário se encontra indisponível");
        }
    }

    private LocalDate getStartDate(IntervalChoice choice, LocalDate startDate) {
        List<IntervalChoice> automaticSystemDateChoices = new ArrayList<>(
                List.of(IntervalChoice.DAILY, IntervalChoice.WEEKLY));
        return automaticSystemDateChoices.contains(choice) ? SystemClockUtils.getCurrentDate() : startDate;
    }

    private CalendarConsultationResponseDTO getCalendarBySpecificalDayAndHour(
            CalendarConsultationRequestDTO requestDTO) {
        CalendarEntity calendar = RepositoryUtils.findOrThrow(
                calendarRepository.findByDayAndHour(requestDTO.getStartDate(), requestDTO.getStartTime(),
                        requestDTO.getDoctorId()),
                () -> new NotFoundException("Calendário"));
        return CalendarMapperDTO.toCalendarConsultationResponse(calendar);
    }

    private List<CalendarConsultationResponseDTO> getCalendarBySpecificalInterval(
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

    public void checkCalendarPermissions(UUID doctorId, LocalDate startDate) {
        calendarPermissionService.validatePermissionCalendar(doctorId, Optional.ofNullable(startDate));
    }

}
