package br.com.meusintoma.modules.calendarHealthPlan.controller;

import java.util.List;
import java.util.UUID;

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

import br.com.meusintoma.modules.calendarHealthPlan.dto.CalendarHealthPlanAssociateRequestDTO;
import br.com.meusintoma.modules.calendarHealthPlan.dto.CalendarHealthPlanResponseCreationDTO;
import br.com.meusintoma.modules.calendarHealthPlan.services.CalendarHealthPlanService;
import br.com.meusintoma.modules.doctor.dto.DoctorCalendarResponseDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/calendar-health-plan")
@RequiredArgsConstructor
public class CalendarHealthPlanController {

    private final CalendarHealthPlanService calendarHealthPlanService;

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR') || hasRole('SECRETARY')")
    public ResponseEntity<Object> associateCalendarAndPlan(
            @RequestBody CalendarHealthPlanAssociateRequestDTO requestDTO) {
        List<CalendarHealthPlanResponseCreationDTO> associations = calendarHealthPlanService
                .associateHealthPlanToCalendar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(associations);
    }

    @GetMapping("{doctorId}/specifical-calendar-with-health-plans/calendar/{calendarId}")
    @PreAuthorize("hasRole('DOCTOR') || hasRole('SECRETARY')")
    public ResponseEntity<Object> getSpecificalDoctorCalendar(@PathVariable UUID doctorId,
            @PathVariable UUID calendarId) {
        DoctorCalendarResponseDTO calendar = calendarHealthPlanService
                .getSpecificalDoctorCalendarWithHealthPlan(doctorId, calendarId);
        return ResponseEntity.ok().body(calendar);
    }

    @GetMapping("{doctorId}/available-calendars-with-health-plans")
    @PreAuthorize("hasRole('DOCTOR') || hasRole('SECRETARY')")
    public ResponseEntity<Object> getDoctorAvaliableCalendar(@PathVariable UUID doctorId) {
        List<DoctorCalendarResponseDTO> calendars = calendarHealthPlanService
                .getDoctorCalendarsFilteredWithPatientHealthPlans(doctorId);
        return ResponseEntity.ok().body(calendars);
    }

    @GetMapping("{doctorId}/available-calendars-with-health-plans/patient/{patientId}")
    public ResponseEntity<Object> getDoctorAvaliableCalendarByPatient(@PathVariable UUID doctorId,
            @PathVariable UUID patientId) {
        List<DoctorCalendarResponseDTO> calendars = calendarHealthPlanService
                .getDoctorCalendarsFilteredWithPatientHealthPlans(doctorId, patientId);
        return ResponseEntity.ok().body(calendars);
    }

    @DeleteMapping("{doctorId}/delete-calendar/{calendarId}/calendar-health-plan/{calendarHealthPlanId}")
    @PreAuthorize("hasRole('DOCTOR') || hasRole('SECRETARY')")
    public ResponseEntity<Object> deleteDoctorCalendarHealthPlan(@PathVariable UUID doctorId,
            @PathVariable UUID calendarId,
            @PathVariable UUID calendarHealthPlanId) {
        calendarHealthPlanService.deleteCalendarHealthPlan(doctorId, calendarId, calendarHealthPlanId);
        return ResponseEntity.ok().body("Removido");
    }
}
