package br.com.meusintoma.modules.calendar.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.calendar.dto.CalendarConsultationResponseDTO;
import br.com.meusintoma.modules.calendar.dto.CalendarResponseDTO;
import br.com.meusintoma.modules.calendar.dto.CalendarConsultationRequestDTO;
import br.com.meusintoma.modules.calendar.entity.CalendarEntity;
import br.com.meusintoma.modules.calendar.enums.CalendarStatus;
import br.com.meusintoma.modules.calendar.exceptions.CalendarInvalidDateException;
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

    public void verifyDoctorHasCalendar(UUID doctorId) {
        if (!calendarRepository.existsByDoctorId(doctorId)) {
            throw new NoDoctorCalendarException("Nenhum calendário encontrado para o médico");
        }
    }

    public List<CalendarResponseDTO> getDoctorCalendar(UUID doctorId) {
        final LocalDate currentDate = SystemClockUtils.getCurrentDate();
        final LocalTime currentTime = SystemClockUtils.getCurrentTime();
        List<CalendarEntity> calendars = calendarRepository.findAllByDoctorId(doctorId);
        return calendars.stream().filter(calendar -> calendar.getCalendarStatus().equals(CalendarStatus.AVAILABLE))
                .filter(calendar -> !calendar.getDate().isBefore(currentDate)
                        && !calendar.getStartTime().isBefore(currentTime))
                .map(CalendarMapperDTO::toResponseDTO).toList();
    }

    public List<CalendarConsultationResponseDTO> getCalendarConsultation(CalendarConsultationRequestDTO requestDTO) {
        UUID doctorId = requestDTO.getDoctorId();
        LocalDate startDate = requestDTO.getStartDate();

        checkCalendarPermissions(doctorId, startDate);

        return switch (requestDTO.getIntervalChoice()) {
            case SPECIFIC -> {
                CalendarConsultationResponseDTO item = getCalendarBySpecificalDayAndHour(requestDTO);
                yield List.of(item);
            }
            case INTERVAL -> getCalendarBySpecificalInterval(doctorId, startDate, requestDTO.getFinalDate());
            case DAILY -> getCalendarBySpecificalInterval(doctorId, startDate, startDate.plusDays(1));
            case WEEKLY -> getCalendarBySpecificalInterval(doctorId, startDate, startDate.plusDays(7));
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

    private CalendarConsultationResponseDTO getCalendarBySpecificalDayAndHour(
            CalendarConsultationRequestDTO requestDTO) {
        checkCalendarPermissions(requestDTO.getDoctorId(), requestDTO.getStartDate());
        CalendarEntity calendar = RepositoryUtils.findOrThrow(
                calendarRepository.findByDayAndHour(requestDTO.getStartDate(), requestDTO.getStartTime(),
                        requestDTO.getDoctorId()),
                () -> new NotFoundException("Calendário"));
        return CalendarMapperDTO.toCalendarConsultationResponse(calendar);
    }

    private List<CalendarConsultationResponseDTO> getCalendarBySpecificalInterval(
            UUID doctorId, LocalDate startDate, LocalDate finalDate) {
        checkEndDate(startDate, finalDate);
        checkCalendarPermissions(doctorId, startDate);

        List<CalendarEntity> calendars = RepositoryUtils.findOrThrow(
                calendarRepository.findBySpecificalInterval(startDate, finalDate, doctorId),
                () -> new NotFoundException("Calendário"));

        GenericUtils.checkIsEmptyList(calendars);

        return calendars.stream().map(CalendarMapperDTO::toCalendarConsultationResponse).toList();
    }

    private void checkEndDate(LocalDate startDate, LocalDate endDate) {
        boolean isEndDateBiggerThanStartDate = endDate.isAfter(startDate);
        if (isEndDateBiggerThanStartDate) {
            throw new CalendarInvalidDateException("A data final não pode ser maior do que a data inicial, confira");
        }
    }

    private void checkCalendarPermissions(UUID doctorId, LocalDate startDate) {
        calendarPermissionService.validatePermissionCalendar(doctorId, Optional.of(startDate));
    }

}
