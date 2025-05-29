package br.com.meusintoma.modules.consultation.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.meusintoma.modules.calendar.enums.CalendarStatus;
import br.com.meusintoma.modules.consultation.entity.ConsultationEntity;
import br.com.meusintoma.modules.consultation.enums.ConsultationStatus;
import br.com.meusintoma.modules.consultation.repository.ConsultationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsultationScheduleUpdate {

    private final ConsultationRepository consultationRepository;

    @Scheduled(cron = "0 0/5 * * * *")
    //@Scheduled(cron = "10 * * * * *")
    @Transactional
    public void markExpiredConsultationAndBlocked() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        List<ConsultationEntity> expiredConsultations = consultationRepository.findExpiredConsultations(today, now);

        for (ConsultationEntity consultation : expiredConsultations) {
            consultation.setStatus(ConsultationStatus.EXPIRED);
            consultation.getCalendarSlot().setCalendarStatus(CalendarStatus.BLOCKED);
        }

        consultationRepository.saveAll(expiredConsultations);

    }
}
