package br.com.meusintoma.modules.calendar.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.modules.calendar.dto.CalendarResponseDTO;
import br.com.meusintoma.modules.calendar.entity.CalendarEntity;
import br.com.meusintoma.modules.calendar.enums.CalendarStatus;
import br.com.meusintoma.modules.calendar.exceptions.CalendarNotFoundException;
import br.com.meusintoma.modules.calendar.exceptions.NoDoctorCalendarException;
import br.com.meusintoma.modules.calendar.mapper.CalendarMapperDTO;
import br.com.meusintoma.modules.calendar.repository.CalendarRepository;

@Service
public class CalendarService {

    @Autowired
    CalendarRepository calendarRepository;

    public void verifyDoctorHasCalendar(UUID doctorId) {
        if (!calendarRepository.existsByDoctorId(doctorId)) {
            throw new NoDoctorCalendarException("Nenhum calendário encontrado para o médico");
        }
    }

    public List<CalendarResponseDTO> getDoctorCalendar(UUID doctorId) {
        final LocalDate currentDate = LocalDate.now();
        final LocalTime currentTime = LocalTime.now();
        List<CalendarEntity> calendars = calendarRepository.findAllByDoctorId(doctorId);
        return calendars.stream().filter(calendar -> calendar.getCalendarStatus().equals(CalendarStatus.AVAILABLE))
                .filter(calendar -> !calendar.getDate().isBefore(currentDate)
                        && !calendar.getStartTime().isBefore(currentTime))
                .map(CalendarMapperDTO::toResponseDTO).toList();
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
}
