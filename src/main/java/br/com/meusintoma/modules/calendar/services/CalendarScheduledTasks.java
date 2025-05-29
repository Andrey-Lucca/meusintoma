package br.com.meusintoma.modules.calendar.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.meusintoma.modules.calendar.enums.CalendarStatus;
import br.com.meusintoma.modules.calendar.repository.CalendarRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalendarScheduledTasks {

    private final CalendarRepository calendarRepository;

    @Scheduled(cron = "0 0 2 * * *")
    //@Scheduled(cron = "1 * * * * *")
    @Transactional
    public void deleteOldAvaliableAndBlockedSlots() {
        List<CalendarStatus> statuses = List.of(CalendarStatus.AVAILABLE, CalendarStatus.BLOCKED);
        calendarRepository.deleteOldCalendarsByStatus(LocalDate.now().minusDays(1), statuses);
    }

}
