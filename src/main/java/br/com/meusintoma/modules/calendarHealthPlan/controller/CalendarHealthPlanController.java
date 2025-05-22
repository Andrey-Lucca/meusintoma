package br.com.meusintoma.modules.calendarHealthPlan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.modules.calendarHealthPlan.dto.CalendarHealthPlanAssociateRequestDTO;
import br.com.meusintoma.modules.calendarHealthPlan.dto.CalendarHealthPlanResponseCreationDTO;
import br.com.meusintoma.modules.calendarHealthPlan.services.CalendarHealthPlanService;

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
}
