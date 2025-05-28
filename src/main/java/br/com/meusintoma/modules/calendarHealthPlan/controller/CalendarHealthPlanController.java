package br.com.meusintoma.modules.calendarHealthPlan.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.exceptions.globalCustomException.CustomAccessDeniedException;
import br.com.meusintoma.exceptions.globalCustomException.InvalidDateException;
import br.com.meusintoma.exceptions.globalCustomException.NoContentException;
import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.calendarHealthPlan.dto.CalendarHealthPlanAssociateRequestDTO;
import br.com.meusintoma.modules.calendarHealthPlan.dto.CalendarHealthPlanResponseCreationDTO;
import br.com.meusintoma.modules.calendarHealthPlan.services.CalendarHealthPlanService;
import br.com.meusintoma.modules.doctor.dto.DoctorCalendarResponseDTO;

@RestController
@RequestMapping("/calendar-health-plan")
public class CalendarHealthPlanController {

    @Autowired
    CalendarHealthPlanService calendarHealthPlanService;
    
    @PostMapping
    @PreAuthorize("hasRole('DOCTOR') || hasRole('SECRETARY')")
    public ResponseEntity<Object> associateCalendarAndPlan(@RequestBody CalendarHealthPlanAssociateRequestDTO requestDTO){
        try {
            List<CalendarHealthPlanResponseCreationDTO> associations = calendarHealthPlanService.associateHealthPlanToCalendar(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(associations);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Algo deu errado. Já iremos resolver");
        }
    }

    
    @GetMapping("{doctorId}/specifical-calendar-with-health-plans/calendar/{calendarId}")
    @PreAuthorize("hasRole('DOCTOR') || hasRole('SECRETARY')")
    public ResponseEntity<Object> getSpecificalDoctorCalendar(@PathVariable UUID doctorId,
            @PathVariable UUID calendarId) {
        try {
            DoctorCalendarResponseDTO calendar = calendarHealthPlanService
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
            List<DoctorCalendarResponseDTO> calendars = calendarHealthPlanService
                    .getDoctorCalendarsFilteredWithPatientHealthPlans(doctorId);
            return ResponseEntity.ok().body(calendars);
        } catch (CustomAccessDeniedException e) {
            throw e;
        } catch (NoContentException e) {
            throw e;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Algo deu errado! Já iremos resolver");
        }
    }

    @GetMapping("{doctorId}/available-calendars-with-health-plans/patient/{patientId}")
    public ResponseEntity<Object> getDoctorAvaliableCalendarByPatient(@PathVariable UUID doctorId,
            @PathVariable UUID patientId) {
        try {
            List<DoctorCalendarResponseDTO> calendars = calendarHealthPlanService
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
            calendarHealthPlanService.deleteCalendarHealthPlan(doctorId, calendarId, calendarHealthPlanId);
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
