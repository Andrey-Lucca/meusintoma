package br.com.meusintoma.modules.doctor.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.exceptions.globalCustomException.CustomAccessDeniedException;
import br.com.meusintoma.exceptions.globalCustomException.InvalidDateException;
import br.com.meusintoma.exceptions.globalCustomException.NoContentException;
import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.doctor.dto.DoctorCalendarResponseDTO;
import br.com.meusintoma.modules.doctor.services.DoctorService;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    DoctorService doctorService;

    @GetMapping("{doctorId}/specifical-calendar-with-health-plans/calendar/{calendarId}")
    @PreAuthorize("hasRole('DOCTOR') || hasRole('SECRETARY')")
    public ResponseEntity<Object> getSpecificalDoctorCalendar(@PathVariable UUID doctorId,
            @PathVariable UUID calendarId) {
        try {
            DoctorCalendarResponseDTO calendar = doctorService
                    .getSpecificalDoctorCalendarWithHealthPlan(doctorId, calendarId);
            return ResponseEntity.ok().body(calendar);
        } catch (NoContentException e) {
            throw e;
        } catch (CustomAccessDeniedException e) {
            throw e;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Algo deu errado! Já iremos resolver");
        }
    }

    @GetMapping("{doctorId}/available-calendars-with-health-plans")
    @PreAuthorize("hasRole('DOCTOR') || hasRole('SECRETARY')")
    public ResponseEntity<Object> getDoctorAvaliableCalendar(@PathVariable UUID doctorId) {
        try {
            List<DoctorCalendarResponseDTO> calendars = doctorService
                    .getDoctorCalendarsFilteredWithPatientHealthPlans(doctorId);
            return ResponseEntity.ok().body(calendars);
        } catch (CustomAccessDeniedException e) {
            throw e;
        } catch (NoContentException e) {
            throw e;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Algo deu errado! Já iremos resolver");
        }
    }

    @GetMapping("{doctorId}/available-calendars-with-health-plans/patient/{patientId}")
    public ResponseEntity<Object> getDoctorAvaliableCalendarByPatient(@PathVariable UUID doctorId,
            @PathVariable UUID patientId) {
        try {
            List<DoctorCalendarResponseDTO> calendars = doctorService
                    .getDoctorCalendarsFilteredWithPatientHealthPlans(doctorId, patientId);
            return ResponseEntity.ok().body(calendars);
        } catch (CustomAccessDeniedException e) {
            throw e;
        } catch (NoContentException e) {
            throw e;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Algo deu errado! Já iremos resolver");
        }
    }

    @DeleteMapping("{doctorId}/delete-calendar/{calendarId}/calendar-health-plan/{calendarHealthPlanId}")
    @PreAuthorize("hasRole('DOCTOR') || hasRole('SECRETARY')")
    public ResponseEntity<Object> deleteDoctorCalendarHealthPlan(@PathVariable UUID doctorId,
            @PathVariable UUID calendarId,
            @PathVariable UUID calendarHealthPlanId) {
        try {
            doctorService.deleteCalendarHealthPlan(doctorId, calendarId, calendarHealthPlanId);
            return ResponseEntity.ok().body("Removido");
        } catch (CustomAccessDeniedException e) {
            throw e;
        } catch (InvalidDateException e) {
            throw e;
        } catch (NoContentException e) {
            throw e;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Algo deu errado! Já iremos resolver");
        }
    }
}
