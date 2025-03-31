package br.com.meusintoma.modules.calendar.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.modules.calendar.dto.CalendarWeeklySlotsGenerationDTO;
import br.com.meusintoma.modules.calendar.dto.GenerateDailySlotsRequestDTO;
import br.com.meusintoma.modules.calendar.entity.CalendarEntity;
import br.com.meusintoma.modules.calendar.enums.CalendarStatus;
import br.com.meusintoma.modules.calendar.mapper.CalendarMapperDTO;
import br.com.meusintoma.modules.calendar.repository.CalendarRepository;
import br.com.meusintoma.modules.doctor.entity.DoctorEntity;

@Service
public class CalendarSlotService {

    @Autowired
    CalendarRepository calendarRepository;

    public List<CalendarEntity> generateDailySlots(DoctorEntity doctor, GenerateDailySlotsRequestDTO request) {

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
            current = current.plusMinutes(request.getSlotDurationMinutes()); // Pega a hora atual e soma tempo da
                                                                             // consulta. Exemplo: 08h + 01h = 09h
        }
        return slots;
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

    public List<CalendarEntity> saveAll(List<CalendarEntity> slots) {
        return calendarRepository.saveAll(slots);
    }
}
