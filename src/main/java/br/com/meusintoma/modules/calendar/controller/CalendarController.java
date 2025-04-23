package br.com.meusintoma.modules.calendar.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.exceptions.globalCustomException.CustomAccessDeniedException;
import br.com.meusintoma.exceptions.globalCustomException.InvalidDateException;
import br.com.meusintoma.modules.calendar.dto.UpdateCalendarDTO;
import br.com.meusintoma.modules.calendar.exceptions.CalendarNotFoundException;
import br.com.meusintoma.modules.calendar.exceptions.NoDoctorCalendarException;
import br.com.meusintoma.modules.calendar.services.CalendarPermissionService;
import br.com.meusintoma.modules.calendar.services.CalendarService;

@RestController
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    CalendarService calendarService;

    @Autowired
    CalendarPermissionService calendarPermissionService;

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<Object> getCalendarByDoctor(@PathVariable UUID doctorId) {
        try {
            calendarService.verifyDoctorHasCalendar(doctorId);
            var response = calendarService.getDoctorCalendar(doctorId);
            return ResponseEntity.ok().body(response);

        } catch (NoDoctorCalendarException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Houve uma exceção ao tentar visualizar o calendário do doutor!");
        }
    }

    @PreAuthorize("hasRole('DOCTOR') || hasRole('SECRETARY')")
    @PatchMapping("/doctor/{doctorId}/{calendarId}")
    public ResponseEntity<Object> updateCalendarStatus(@PathVariable UUID doctorId, @PathVariable UUID calendarId,
            @RequestBody UpdateCalendarDTO statusDto) {
        try {
            calendarService.verifyDoctorHasCalendar(doctorId);
            calendarPermissionService.validatePermissionCalendar(doctorId, Optional.empty());
            var response = calendarService.updateCalendarStatus(calendarId, statusDto.getStatus());
            return ResponseEntity.ok().body(response);

        } catch (InvalidDateException | CustomAccessDeniedException e) {
            throw e;
        } catch (NoDoctorCalendarException | CalendarNotFoundException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ocorreu um erro ao tentar excluir esse horário do calendário");
        }
    }

    @PreAuthorize("hasRole('DOCTOR') || hasRole('SECRETARY')")
    @DeleteMapping("/doctor/{doctorId}/{calendarId}")
    public ResponseEntity<Object> deleteCalendarById(@PathVariable UUID calendarId, @PathVariable UUID doctorId) {
        try {
            calendarService.verifyDoctorHasCalendar(doctorId);
            calendarPermissionService.validatePermissionCalendar(doctorId, Optional.empty());
            calendarService.deleteCalendarById(calendarId);
            return ResponseEntity.ok().body("Horário removido com sucesso");
        } catch (InvalidDateException | CustomAccessDeniedException e) {
            throw e;
        } catch (NoDoctorCalendarException | CalendarNotFoundException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ocorreu um erro ao tentar excluir esse horário do calendário");
        }
    }
}
