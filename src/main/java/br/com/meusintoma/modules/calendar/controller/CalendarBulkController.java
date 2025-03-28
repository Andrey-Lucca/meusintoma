package br.com.meusintoma.modules.calendar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.modules.calendar.dto.CalendarResponseDTO;
import br.com.meusintoma.modules.calendar.dto.GenerateSlotsRequestDTO;
import br.com.meusintoma.modules.calendar.entity.CalendarEntity;
import br.com.meusintoma.modules.calendar.mapper.CalendarMapperDTO;
import br.com.meusintoma.modules.calendar.services.CalendarPermissionService;
import br.com.meusintoma.modules.calendar.services.CalendarSlotService;
import br.com.meusintoma.modules.doctor.entity.DoctorEntity;
import br.com.meusintoma.modules.doctor.services.DoctorService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/calendar/bulk")
public class CalendarBulkController {

        @Autowired
        private CalendarSlotService calendarSlotService;

        @Autowired
        private CalendarPermissionService calendarPermissionService;

        @Autowired
        private DoctorService doctorService;

        @PostMapping("/daily-slots")
        @PreAuthorize("hasRole('DOCTOR') || hasRole('SECRETARY')")
        public ResponseEntity<Object> createDailySlots(@RequestBody @Valid GenerateSlotsRequestDTO requestDTO,
                        HttpServletRequest request) {
                try {
                        if (!requestDTO.isValid()) {
                                throw new IllegalArgumentException("Parâmetros inválidos para geração de slots");
                        }

                        calendarPermissionService.validatePermissionCalendar(request, requestDTO.getDoctorId());

                        DoctorEntity doctor = doctorService.findDoctorById(requestDTO.getDoctorId());

                        GenerateSlotsRequestDTO slotsRequest = CalendarMapperDTO.toDto(requestDTO);

                        List<CalendarEntity> slots = calendarSlotService.generateDailySlots(doctor, slotsRequest);

                        calendarSlotService.saveAll(slots);

                        List<CalendarResponseDTO> response = slots.stream()
                                        .map(CalendarMapperDTO::toResponseDTO)
                                        .toList();
                        return ResponseEntity.ok().body(response);
                } catch (Exception e) {
                        e.printStackTrace();
                        return ResponseEntity.badRequest().body("Não foi possível criar o calendário");
                }
        }

}
