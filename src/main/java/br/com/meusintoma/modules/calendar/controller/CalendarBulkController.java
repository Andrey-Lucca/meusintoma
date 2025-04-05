package br.com.meusintoma.modules.calendar.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.exceptions.CustomAccessDeniedException;
import br.com.meusintoma.exceptions.InvalidDateException;
import br.com.meusintoma.modules.calendar.dto.CalendarResponseDTO;
import br.com.meusintoma.modules.calendar.dto.CalendarWeeklySlotsGenerationDTO;
import br.com.meusintoma.modules.calendar.dto.GenerateDailySlotsRequestDTO;
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
        public ResponseEntity<Object> createDailySlots(@RequestBody GenerateDailySlotsRequestDTO requestDTO,
                        HttpServletRequest request) {
                try {
                        if (!requestDTO.isValid()) {
                                throw new IllegalArgumentException("Parâmetros inválidos para geração de slots");
                        }

                        calendarPermissionService.validatePermissionCalendar(request, requestDTO.getDoctorId(),
                                        Optional.ofNullable(requestDTO.getDate()));

                        DoctorEntity doctor = doctorService.findDoctorById(requestDTO.getDoctorId());

                        GenerateDailySlotsRequestDTO slotsRequest = CalendarMapperDTO.toDailyDto(requestDTO);

                        List<CalendarEntity> slots = calendarSlotService.generateDailySlots(doctor, slotsRequest);

                        calendarSlotService.saveAll(slots);

                        List<CalendarResponseDTO> response = slots.stream()
                                        .map(CalendarMapperDTO::toResponseDTO)
                                        .toList();
                        return ResponseEntity.ok().body(response);
                } catch (InvalidDateException | CustomAccessDeniedException e) {
                        throw e;
                } catch (Exception e) {
                        return ResponseEntity.badRequest().body("Não foi possível criar o calendário");
                }
        }

        @PostMapping("weekly-slots")
        @PreAuthorize("hasRole('DOCTOR') || hasRole('SECRETARY')")
        public ResponseEntity<Object> createWeeklySlots(
                        @RequestBody CalendarWeeklySlotsGenerationDTO requestDTO,
                        HttpServletRequest request) {

                try {
                        requestDTO.setRequestDate(LocalDate.now());
                        calendarPermissionService.validatePermissionCalendar(request, requestDTO.getDoctorId(),
                                        Optional.ofNullable(requestDTO.getDate()));

                        DoctorEntity doctor = doctorService.findDoctorById(requestDTO.getDoctorId());

                        List<GenerateDailySlotsRequestDTO> weeklySlots = calendarSlotService.generateWeeklySlots(doctor,
                                        requestDTO);

                        List<CalendarResponseDTO> response = weeklySlots.stream()
                                        .flatMap(slot -> {
                                                List<CalendarEntity> slots = calendarSlotService
                                                                .generateDailySlots(doctor, slot);
                                                calendarSlotService.saveAll(slots);
                                                return slots.stream();
                                        })
                                        .map(CalendarMapperDTO::toResponseDTO)
                                        .toList();

                        return ResponseEntity.status(HttpStatus.CREATED).body(response);

                } catch (InvalidDateException | CustomAccessDeniedException e) {
                        throw e;
                } catch (Exception e) {
                        return ResponseEntity.badRequest().body("Não foi possível criar o calendário");
                }
        }

}
