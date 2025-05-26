package br.com.meusintoma.modules.calendar.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import br.com.meusintoma.modules.calendar.dto.CalendarResponseDTO;
import br.com.meusintoma.modules.calendar.dto.CalendarResultDTO;
import br.com.meusintoma.modules.calendar.dto.GenerateDailySlotsRequestDTO;
import br.com.meusintoma.modules.calendar.entity.CalendarEntity;
import br.com.meusintoma.modules.calendar.enums.CalendarStatus;
import br.com.meusintoma.modules.calendar.mapper.CalendarMapperDTO;
import br.com.meusintoma.modules.doctor.entity.DoctorEntity;
import br.com.meusintoma.utils.common.StatusResult;

public class CalendarServiceUtils {

    public static void addResult(List<CalendarResultDTO> results, StatusResult status, CalendarEntity calendar,
            String message) {
        CalendarResponseDTO calendarResponse = null;
        if (calendar != null) {
            calendarResponse = CalendarMapperDTO.toResponseDTO(calendar);
        }

        CalendarResultDTO calendarResult = CalendarResultDTO.builder()
                .calendarResponseDTO(calendarResponse)
                .message(message)
                .statusResult(status)
                .build();

        results.add(calendarResult);
    }

    public static CalendarEntity createCalendarSlot(DoctorEntity doctor, LocalTime current,
            GenerateDailySlotsRequestDTO request) {
        LocalDate date = request.getDate();
        int slotDuration = request.getSlotDurationMinutes();
        CalendarEntity calendar = CalendarEntity.builder().date(date)
                .startTime(current)
                .endTime(current.plusMinutes(slotDuration))
                .doctor(doctor)
                .calendarStatus(CalendarStatus.AVAILABLE).build();
        return calendar;
    }

    public static LocalTime roundUpToNearestSlot(LocalTime time, int slotMinutes) {
        int minutes = time.getMinute();
        int mod = minutes % slotMinutes;

        if (mod == 0)
            return time.truncatedTo(ChronoUnit.MINUTES);

        return time.plusMinutes(slotMinutes - mod).truncatedTo(ChronoUnit.MINUTES);
    }

}
