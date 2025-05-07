package br.com.meusintoma.modules.consultation.mapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import br.com.meusintoma.modules.consultation.dto.ConsultationByDoctorPatientDTO;
import br.com.meusintoma.modules.consultation.dto.ConsultationCanceledResponseDTO;
import br.com.meusintoma.modules.consultation.dto.ConsultationResponseDTO;
import br.com.meusintoma.modules.consultation.entity.ConsultationEntity;
import br.com.meusintoma.modules.consultation.enums.ConsultationStatus;

public class ConsultationMapper {

        public static ConsultationResponseDTO toResponseDTO(ConsultationEntity consultation) {
                boolean isCancelled = consultation.getStatus() == ConsultationStatus.CANCELLED;

                LocalDate date = isCancelled
                                ? consultation.getSnapshot().getDate()
                                : consultation.getCalendarSlot().getDate();

                LocalTime startedAt = isCancelled
                                ? consultation.getSnapshot().getStartTime()
                                : consultation.getCalendarSlot().getStartTime();

                LocalTime endAt = isCancelled
                                ? consultation.getSnapshot().getEndTime()
                                : consultation.getCalendarSlot().getEndTime();

                String doctorName = isCancelled
                                ? null
                                : consultation.getCalendarSlot().getDoctor().getName();

                ConsultationResponseDTO responseDTO = ConsultationResponseDTO.builder()
                                .date(date)
                                .startedAt(startedAt)
                                .endAt(endAt)
                                .doctor(doctorName)
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

        public static List<ConsultationByDoctorPatientDTO> toConsultationByDoctorPatientDTO(
                        List<ConsultationEntity> consultations) {
                return consultations.stream()
                                .map(consultation -> ConsultationByDoctorPatientDTO.builder()
                                                .date(consultation.getCalendarSlot().getDate())
                                                .patient(consultation.getPatient().getName())
                                                .consultationId(consultation.getId())
                                                .startedAt(consultation.getCalendarSlot().getStartTime())
                                                .endAt(consultation.getCalendarSlot().getEndTime())
                                                .status(consultation.getStatus()).build())
                                .toList();
        }
}
