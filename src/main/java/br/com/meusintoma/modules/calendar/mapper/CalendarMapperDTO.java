package br.com.meusintoma.modules.calendar.mapper;

import br.com.meusintoma.modules.calendar.dto.CalendarResponseDTO;
import br.com.meusintoma.modules.calendar.dto.GenerateSlotsRequestDTO;
import br.com.meusintoma.modules.calendar.entity.CalendarEntity;

public class CalendarMapperDTO {

    public static GenerateSlotsRequestDTO toDto(GenerateSlotsRequestDTO requestDTO) {
        return GenerateSlotsRequestDTO.builder()
            .doctorId(requestDTO.getDoctorId())
            .breakStart(requestDTO.getBreakStart())
            .breakEnd(requestDTO.getBreakEnd())
            .startTime(requestDTO.getStartTime())
            .endTime(requestDTO.getEndTime())
            .date(requestDTO.getDate())
            .slotDurationMinutes(requestDTO.getSlotDurationMinutes())
            .build();
    }

    public static CalendarResponseDTO toResponseDTO(CalendarEntity calendarEntity){
        CalendarResponseDTO calendarResponseDTO = CalendarResponseDTO.builder().date(calendarEntity.getDate())
        .startTime(calendarEntity.getStartTime()).endTime(calendarEntity.getEndTime()).status(calendarEntity.getCalendarStatus())
        .doctorName(calendarEntity.getDoctor().getName()).doctorSpecialization(calendarEntity.getDoctor().getSpecialization()).build();
        return calendarResponseDTO;
    }
}
