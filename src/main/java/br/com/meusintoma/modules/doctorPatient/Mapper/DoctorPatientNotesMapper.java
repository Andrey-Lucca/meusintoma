package br.com.meusintoma.modules.doctorPatient.Mapper;

import br.com.meusintoma.modules.doctorPatient.dto.DoctorPatientNoteDTO;
import br.com.meusintoma.modules.doctorPatient.dto.DoctorPatientNoteGetDTO;
import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientNoteEntity;
import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientNoteItemEntity;

import java.util.List;
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

    public static DoctorPatientNoteGetDTO toDoctorPatientNoteGetDTO(DoctorPatientNoteEntity note) {
        return DoctorPatientNoteGetDTO.builder()
                .id(note.getId())
                .doctorId(note.getDoctorPatient().getDoctor().getId())
                .patientId(note.getDoctorPatient().getPatient().getId())
                .consultationId(note.getConsultation().getId())
                .notes(note.getItems().stream().map(DoctorPatientNoteItemEntity::getContent).toList())
                .createdAt(note.getCreatedAt())
                .date(note.getConsultation().getCalendarSlot().getDate())
                .startedHour(note.getConsultation().getCalendarSlot().getStartTime()).build();
    }

    public static List<DoctorPatientNoteGetDTO> toDoctorPatientNoteGetDTO(List<DoctorPatientNoteEntity> notes) {
        return notes.stream().map(note -> DoctorPatientNoteGetDTO.builder()
                .id(note.getId())
                .doctorId(note.getDoctorPatient().getDoctor().getId())
                .patientId(note.getDoctorPatient().getPatient().getId())
                .consultationId(note.getConsultation().getId())
                .notes(note.getItems().stream().map(DoctorPatientNoteItemEntity::getContent).toList())
                .createdAt(note.getCreatedAt())
                .date(note.getConsultation().getCalendarSlot().getDate())
                .startedHour(note.getConsultation().getCalendarSlot().getStartTime())
                .build())
                .toList();
    }
}
