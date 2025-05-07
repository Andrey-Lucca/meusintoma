package br.com.meusintoma.modules.doctorPatient.Mapper;

import br.com.meusintoma.modules.doctorPatient.dto.DoctorPatientNoteDTO;
import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientNoteEntity;
import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientNoteItemEntity;

import java.util.stream.Collectors;

public class DoctorPatientNotesMapper {

    public static DoctorPatientNoteDTO toDoctorPatientNoteDTO(DoctorPatientNoteEntity doctorPatientNote) {
        return DoctorPatientNoteDTO.builder()
                .createdAt(doctorPatientNote.getCreatedAt())
                .date(doctorPatientNote.getConsultation().getCalendarSlot().getDate())
                .startedHour(doctorPatientNote.getConsultation().getCalendarSlot().getStartTime())
                .notes(doctorPatientNote.getItems().stream()
                        .map(DoctorPatientNoteItemEntity::getContent)
                        .collect(Collectors.toList()))
                .build();
    }
}
