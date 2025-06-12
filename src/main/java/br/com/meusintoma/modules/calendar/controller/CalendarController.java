package br.com.meusintoma.modules.calendar.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.modules.calendar.dto.CalendarConsultationRequestDTO;
import br.com.meusintoma.modules.calendar.dto.CalendarConsultationResponseDTO;
import br.com.meusintoma.modules.calendar.dto.CalendarResponseDTO;
import br.com.meusintoma.modules.calendar.dto.UpdateCalendarDTO;

import br.com.meusintoma.modules.calendar.services.CalendarService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor

public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<CalendarResponseDTO>> getCalendarByDoctor(@PathVariable UUID doctorId) {
        List<CalendarResponseDTO> calendars = calendarService.getDoctorCalendar(doctorId);
        return ResponseEntity.ok().body(calendars);
    }

    @PreAuthorize("hasRole('DOCTOR') || hasRole('SECRETARY')")
    @GetMapping("/calendar-consult")
    public ResponseEntity<Object> getCalendarConsult(
            @RequestBody CalendarConsultationRequestDTO calendarConsultationRequestDTO) {
        List<CalendarConsultationResponseDTO> calendars = calendarService
                .getCalendarConsultation(calendarConsultationRequestDTO);
        return ResponseEntity.ok().body(calendars);
    }

    @PreAuthorize("hasRole('DOCTOR') || hasRole('SECRETARY')")
    @PatchMapping("/doctor/{doctorId}/{calendarId}")
    public ResponseEntity<CalendarResponseDTO> updateCalendarStatus(@PathVariable UUID doctorId,
            @PathVariable UUID calendarId,
            @RequestBody UpdateCalendarDTO statusDto) {
        CalendarResponseDTO updatedCalendar = calendarService.updateCalendarStatus(calendarId, statusDto.getStatus());
        return ResponseEntity.ok().body(updatedCalendar);
    }

    @PreAuthorize("hasRole('DOCTOR') || hasRole('SECRETARY')")
    @DeleteMapping("/doctor/{doctorId}/{calendarId}")
    public ResponseEntity<Object> deleteCalendarById(@PathVariable UUID calendarId, @PathVariable UUID doctorId) {
        calendarService.deleteCalendarById(calendarId);
        return ResponseEntity.ok().body("Horário removido com sucesso");
    }
}
