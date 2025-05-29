package br.com.meusintoma.modules.consultation.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.meusintoma.modules.consultation.enums.ConsultationStatus;
import br.com.meusintoma.modules.consultation.repository.ConsultationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsultationScheduleDelete {

    private final ConsultationRepository consultationRepository;

    @Scheduled(cron = "* * 0 * * *")
    //@Scheduled(cron = "5 * * * * *")
    @Transactional
    public void deleteExpiredConsultation() {
        List<ConsultationStatus> statuses = new ArrayList<>(List.of(ConsultationStatus.EXPIRED, ConsultationStatus.CANCELLED));
        consultationRepository.deleteExpiredConsultations(statuses);
    }

}
