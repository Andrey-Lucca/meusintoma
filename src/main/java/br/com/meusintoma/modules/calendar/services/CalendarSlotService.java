package br.com.meusintoma.modules.calendar.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.meusintoma.modules.calendar.dto.CalendarResultDTO;
import br.com.meusintoma.modules.calendar.dto.CalendarWeeklySlotsGenerationDTO;
import br.com.meusintoma.modules.calendar.dto.GenerateDailySlotsRequestDTO;
import br.com.meusintoma.modules.calendar.entity.CalendarEntity;
import br.com.meusintoma.modules.calendar.mapper.CalendarMapperDTO;
import br.com.meusintoma.modules.calendar.repository.CalendarRepository;
import br.com.meusintoma.modules.doctor.entity.DoctorEntity;
import br.com.meusintoma.modules.doctor.services.DoctorService;
import br.com.meusintoma.utils.common.StatusResult;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalendarSlotService {

    private final CalendarRepository calendarRepository;

    private final CalendarService calendarService;

    private final CalendarPermissionService calendarPermissionService;

    private final DoctorService doctorService;

    public List<CalendarResultDTO> generateDailySlots(GenerateDailySlotsRequestDTO request) {
        validateRequest(request);

        DoctorEntity doctor = doctorService.findDoctor(request.getDoctorId());
        List<CalendarResultDTO> results = new ArrayList<>();
        List<CalendarEntity> slots = new ArrayList<>();

        LocalTime current = CalendarServiceUtils.calculateInitialSlotTime(request);
        LocalTime endTime = request.getEndTime();

        while (current.plusMinutes(request.getSlotDurationMinutes()).isBefore(endTime) ||
                current.plusMinutes(request.getSlotDurationMinutes()).equals(endTime)) {

            if (CalendarServiceUtils.shouldSkipDueToBreak(request, current)) {
                CalendarServiceUtils.addResult(results, StatusResult.ERROR, null,
                        "O horário invade o horário de parada, pulando");
            } else if (calendarService.doctorAlreadyHaveCalendarSlot(doctor.getId(), request.getDate(), current)) {
                CalendarServiceUtils.addResult(results, StatusResult.ALREADY_EXISTS, null,
                        "Esse horário já consta no seu calendário");
            } else {
                try {
                    CalendarEntity calendar = CalendarServiceUtils.createCalendarSlot(doctor, current, request);
                    slots.add(calendar);
                    CalendarServiceUtils.addResult(results, StatusResult.CREATED, calendar,
                            "Horário criado com sucesso");
                } catch (Exception e) {
                    CalendarServiceUtils.addResult(results, StatusResult.ERROR, null,
                            "Não foi possível associar o calendário");
                }
            }

            current = current.plusMinutes(request.getSlotDurationMinutes());
        }

        saveAll(slots);
        return results;
    }

    public List<GenerateDailySlotsRequestDTO> generateWeeklySlots(DoctorEntity doctor,
            CalendarWeeklySlotsGenerationDTO request) {

        LocalDate startDate = request.getRequestDate()
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.of(request.getStartDay())));
        LocalDate endDate = request.getRequestDate()
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.of(request.getEndDay())));

        if (endDate.isBefore(startDate)) {
            endDate = endDate.plusWeeks(1);
        }

        List<GenerateDailySlotsRequestDTO> weeklySlots = new ArrayList<>();

        startDate = startDate.isBefore(request.getRequestDate()) ? request.getRequestDate() : startDate;

        while (!startDate.isAfter(endDate)) {
            GenerateDailySlotsRequestDTO day = CalendarMapperDTO.fromWeeklyRequest(request, startDate);
            weeklySlots.add(day);
            startDate = startDate.plusDays(1);

        }
        return weeklySlots;
    }

    public List<CalendarResultDTO> generateWeeklyCalendarResults(CalendarWeeklySlotsGenerationDTO requestDTO) {
        requestDTO.setRequestDate(LocalDate.now());

        calendarPermissionService.validatePermissionCalendar(
                requestDTO.getDoctorId(),
                Optional.ofNullable(requestDTO.getDate()));

        DoctorEntity doctor = doctorService.findDoctor(requestDTO.getDoctorId());

        List<GenerateDailySlotsRequestDTO> weeklySlots = generateWeeklySlots(doctor, requestDTO);

        List<CalendarResultDTO> allResults = new ArrayList<>();

        for (GenerateDailySlotsRequestDTO dailyRequest : weeklySlots) {
            List<CalendarResultDTO> dailyResults = generateDailySlots(dailyRequest);
            allResults.addAll(dailyResults);
        }

        return allResults;
    }

    public List<CalendarEntity> saveAll(List<CalendarEntity> slots) {
        return calendarRepository.saveAll(slots);
    }

    private void validateRequest(GenerateDailySlotsRequestDTO request) {
        if (!request.isValid()) {
            throw new IllegalArgumentException("Parâmetros inválidos para geração de slots");
        }
        calendarPermissionService.validatePermissionCalendar(request.getDoctorId(),
                Optional.ofNullable(request.getDate()));
    }

}
