package br.com.meusintoma.modules.calendar.services;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.modules.calendar.dto.GenerateSlotsRequestDTO;
import br.com.meusintoma.modules.calendar.entity.CalendarEntity;
import br.com.meusintoma.modules.calendar.enums.CalendarStatus;
import br.com.meusintoma.modules.calendar.repository.CalendarRepository;
import br.com.meusintoma.modules.doctor.entity.DoctorEntity;

@Service
public class CalendarSlotService {

    @Autowired
    CalendarRepository calendarRepository;

    public List<CalendarEntity> generateDailySlots(DoctorEntity doctor, GenerateSlotsRequestDTO request) {

        if (!request.isValid()) {
            throw new IllegalArgumentException("Parâmetros inválidos para geração de slots");
        }

        List<CalendarEntity> slots = new ArrayList<>(); // Aqui cria-se a lista parar adicionar os horários no
                                                        // calendário
        LocalTime current = request.getStartTime(); // Inicialmente é a hora inicial, exemplo: 08:00, porém vai mudando
                                                    // conforme o passar das horas.

        var slotDurationMinutes = request.getSlotDurationMinutes();
        var endTime = request.getEndTime();

        while (current.plusMinutes(slotDurationMinutes).isBefore(endTime)
                || current.plusMinutes(slotDurationMinutes).equals(endTime)) {

            if (request.getBreakStart() == null || request.getBreakEnd() == null
                    || !(current.isBefore(request.getBreakEnd()) && current.plusMinutes(slotDurationMinutes)
                            .isAfter(request.getBreakStart()))) {
                CalendarEntity calendarEntity = CalendarEntity.builder().date(request.getDate())
                        .startTime(current)
                        .endTime(current.plusMinutes(request.getSlotDurationMinutes())).doctor(doctor)
                        .calendarStatus(CalendarStatus.AVAILABLE).build();
                slots.add(calendarEntity);
            }
            current = current.plusMinutes(request.getSlotDurationMinutes()); // Pega a hora atual e soma tempo da consulta. Exemplo: 08h + 01h = 09h
        }
        return slots;
    }

    public List<CalendarEntity> saveAll(List<CalendarEntity> slots) {
        return calendarRepository.saveAll(slots);
    }
}
