package br.com.meusintoma.modules.calendar.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.modules.calendar.dto.GenerateSlotsRequestDTO;
import br.com.meusintoma.modules.calendar.entity.CalendarEntity;
import br.com.meusintoma.modules.calendar.security.CalendarPermissionValidator;
import br.com.meusintoma.modules.calendar.services.CalendarSlotService;
import br.com.meusintoma.modules.doctor.entity.DoctorEntity;
import br.com.meusintoma.modules.doctor.services.DoctorService;
import br.com.meusintoma.security.SecurityUserFilter;
import br.com.meusintoma.security.utils.AuthUtils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/calendar/bulk")
public class CalendarBulkController {

    @Autowired
    private CalendarSlotService calendarSlotService;

    @Autowired
    private CalendarPermissionValidator calendarPermissionValidator;

    @Autowired
    private DoctorService doctorService;

    @PostMapping("/daily-slots")
    @PreAuthorize("hasRole('DOCTOR') || hasRole('SECRETARY')")
    public ResponseEntity<Object> createDailySlots(@RequestBody @Valid GenerateSlotsRequestDTO requestDTO,
            HttpServletRequest request) {

        if (!requestDTO.isValid()) {
            throw new IllegalArgumentException("Parâmetros inválidos para geração de slots");
        }

        UUID authenticatedUserId = AuthUtils.getAuthenticatedPatientId(request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userRole = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElseThrow(() -> new AccessDeniedException("Nenhuma role definida"));

        calendarPermissionValidator.validateCalendarOperationPermission(
                authenticatedUserId,
                userRole.replace("ROLE_", ""),
                requestDTO.getDoctorId());

        DoctorEntity doctor = doctorService.findDoctorById(requestDTO.getDoctorId());

        GenerateSlotsRequestDTO slotsRequest = GenerateSlotsRequestDTO.builder().doctorId(requestDTO.getDoctorId())
                .breakStart(requestDTO.getBreakStart())
                .breakEnd(requestDTO.getBreakEnd()).startTime(requestDTO.getStartTime())
                .endTime(requestDTO.getEndTime()).date(requestDTO.getDate())
                .slotDurationMinutes(requestDTO.getSlotDurationMinutes()).build();

        List<CalendarEntity> slots = calendarSlotService.generateDailySlots(doctor, slotsRequest);

        var response = calendarSlotService.saveAll(slots);

        return ResponseEntity.ok().body(response);
    }

}
