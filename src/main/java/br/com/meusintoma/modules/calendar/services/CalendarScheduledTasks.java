package br.com.meusintoma.modules.calendar.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.meusintoma.modules.calendar.enums.CalendarStatus;
import br.com.meusintoma.modules.calendar.repository.CalendarRepository;
import jakarta.transaction.Transactional;

@Service
public class CalendarScheduledTasks {

    @Autowired
    private CalendarRepository calendarRepository;

    @Scheduled(cron = "0 3 2 * * *")
    @Transactional
    public void deleteOldAvaliableAndBlockedSlots() {
        List<CalendarStatus> statuses = List.of(CalendarStatus.AVAILABLE, CalendarStatus.BLOCKED);
        calendarRepository.deleteOldCalendarsByStatus(LocalDate.now().minusDays(1), statuses);
    }

}
