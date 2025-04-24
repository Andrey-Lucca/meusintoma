package br.com.meusintoma.modules.consultation.mapper;

import br.com.meusintoma.modules.consultation.dto.ConsultationCanceledResponseDTO;
import br.com.meusintoma.modules.consultation.dto.ConsultationResponseDTO;
import br.com.meusintoma.modules.consultation.entity.ConsultationEntity;

public class ConsultationMapper {

    public static ConsultationResponseDTO toResponseDTO(ConsultationEntity consultation) {
        // String secretaryName = null;
        // if (consultation.getCalendarSlot().getDoctor() != null
        //         && consultation.getCalendarSlot().getDoctor().getSecretary() != null) {
        //     secretaryName = consultation.getCalendarSlot().getDoctor().getSecretary().getName();
        // }
        ConsultationResponseDTO responseDTO = ConsultationResponseDTO.builder()
                .date(consultation.getCalendarSlot().getDate())
                .doctor(consultation.getCalendarSlot().getDoctor().getName())
                .endAt(consultation.getCalendarSlot().getEndTime())
                .startedAt(consultation.getCalendarSlot().getStartTime())
                .patient(consultation.getPatient().getName())
                .status(consultation.getStatus())
                .build();
        return responseDTO;
    }

    public static ConsultationCanceledResponseDTO toCanceledResponseDTO(ConsultationEntity consultation) {
        ConsultationCanceledResponseDTO responseDTO = ConsultationCanceledResponseDTO.builder()
                .date(consultation.getSnapshot().getDate())
                .endAt(consultation.getSnapshot().getEndTime())
                .startedAt(consultation.getSnapshot().getStartTime())
                .patient(consultation.getPatient().getName())
                .status(consultation.getStatus())
                .build();
        return responseDTO;
    }
}
