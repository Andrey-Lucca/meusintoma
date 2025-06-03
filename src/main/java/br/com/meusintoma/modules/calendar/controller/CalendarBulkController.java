package br.com.meusintoma.modules.calendar.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.modules.calendar.dto.CalendarResultDTO;
import br.com.meusintoma.modules.calendar.dto.CalendarWeeklySlotsGenerationDTO;
import br.com.meusintoma.modules.calendar.dto.GenerateDailySlotsRequestDTO;
import br.com.meusintoma.modules.calendar.services.CalendarSlotService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/calendar/bulk")
@RequiredArgsConstructor
public class CalendarBulkController {

        private final CalendarSlotService calendarSlotService;

        @PostMapping("/daily-slots")
        @PreAuthorize("hasRole('DOCTOR') || hasRole('SECRETARY')")
        public ResponseEntity<Object> createDailySlots(@RequestBody GenerateDailySlotsRequestDTO requestDTO) {
                List<CalendarResultDTO> results = calendarSlotService.generateDailySlots(requestDTO);
                return ResponseEntity.status(HttpStatus.CREATED).body(results);
        }

        @PostMapping("weekly-slots")
        @PreAuthorize("hasRole('DOCTOR') || hasRole('SECRETARY')")
        public ResponseEntity<List<CalendarResultDTO>> createWeeklySlots(
                        @RequestBody CalendarWeeklySlotsGenerationDTO requestDTO) {
                List<CalendarResultDTO> results = calendarSlotService.generateWeeklyCalendarResults(requestDTO);
                return ResponseEntity.status(HttpStatus.CREATED).body(results);
        }

}
