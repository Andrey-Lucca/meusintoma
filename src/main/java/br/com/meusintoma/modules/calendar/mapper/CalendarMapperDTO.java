package br.com.meusintoma.modules.calendar.mapper;

import java.time.LocalDate;

import br.com.meusintoma.modules.calendar.dto.CalendarResponseDTO;
import br.com.meusintoma.modules.calendar.dto.CalendarWeeklySlotsGenerationDTO;
import br.com.meusintoma.modules.calendar.dto.GenerateDailySlotsRequestDTO;
import br.com.meusintoma.modules.calendar.entity.CalendarEntity;

public class CalendarMapperDTO {

    public static GenerateDailySlotsRequestDTO toDailyDto(GenerateDailySlotsRequestDTO requestDTO) {
        return GenerateDailySlotsRequestDTO.builder()
                .doctorId(requestDTO.getDoctorId())
                .breakStart(requestDTO.getBreakStart())
                .breakEnd(requestDTO.getBreakEnd())
                .startTime(requestDTO.getStartTime())
                .endTime(requestDTO.getEndTime())
                .date(requestDTO.getDate())
                .slotDurationMinutes(requestDTO.getSlotDurationMinutes())
                .build();
    }

    public static GenerateDailySlotsRequestDTO fromWeeklyRequest(
            CalendarWeeklySlotsGenerationDTO weeklyDto,
            LocalDate date) {
        return GenerateDailySlotsRequestDTO.builder()
                .doctorId(weeklyDto.getDoctorId())
                .date(date)
                .startTime(weeklyDto.getStartTime())
                .endTime(weeklyDto.getEndTime())
                .slotDurationMinutes(weeklyDto.getSlotDurationMinutes())
                .breakStart(weeklyDto.getBreakStart())
                .breakEnd(weeklyDto.getBreakEnd())
                .build();
    }

    public static CalendarWeeklySlotsGenerationDTO toWeeklyDTO(CalendarWeeklySlotsGenerationDTO requestDTO) {
        return CalendarWeeklySlotsGenerationDTO.builder()
                .doctorId(requestDTO.getDoctorId())
                .breakStart(requestDTO.getBreakStart())
                .breakEnd(requestDTO.getBreakEnd())
                .startTime(requestDTO.getStartTime())
                .endTime(requestDTO.getEndTime())
                .date(requestDTO.getDate())
                .slotDurationMinutes(requestDTO.getSlotDurationMinutes())
                .startDay(requestDTO.getStartDay())
                .endDay(requestDTO.getEndDay())
                .build();
    }

    public static CalendarResponseDTO toResponseDTO(CalendarEntity calendarEntity) {
        CalendarResponseDTO calendarResponseDTO = CalendarResponseDTO.builder().date(calendarEntity.getDate())
                .startTime(calendarEntity.getStartTime()).endTime(calendarEntity.getEndTime())
                .status(calendarEntity.getCalendarStatus())
                .doctorName(calendarEntity.getDoctor().getName())
                .doctorSpecialization(calendarEntity.getDoctor().getSpecialization()).build();
        return calendarResponseDTO;
    }
}
