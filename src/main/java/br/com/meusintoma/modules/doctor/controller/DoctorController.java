package br.com.meusintoma.modules.doctor.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.exceptions.globalCustomException.CustomAccessDeniedException;
import br.com.meusintoma.exceptions.globalCustomException.NoContentException;
import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.doctor.dto.DoctorCalendarResponseDTO;
import br.com.meusintoma.modules.doctor.services.DoctorService;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    DoctorService doctorService;

    @GetMapping("available-calendars-with-health-plans/{doctorId}/patient/{patientId}")
    public ResponseEntity<Object> getDoctorAvaliableCalendar(@PathVariable UUID doctorId,
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
}
