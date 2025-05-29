package br.com.meusintoma.modules.doctorPatient.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.consultation.entity.ConsultationEntity;
import br.com.meusintoma.modules.consultation.enums.ConsultationStatus;
import br.com.meusintoma.modules.consultation.services.ConsultationService;
import br.com.meusintoma.modules.consultation.services.ConsultationUtilsService;
import br.com.meusintoma.modules.doctorPatient.Mapper.DoctorPatientNotesMapper;
import br.com.meusintoma.modules.doctorPatient.dto.DoctorPatientNoteDTO;
import br.com.meusintoma.modules.doctorPatient.dto.DoctorPatientNoteGetDTO;
import br.com.meusintoma.modules.doctorPatient.dto.DoctorPatientNoteRequestDTO;
import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientEntity;
import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientNoteEntity;
import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientNoteItemEntity;
import br.com.meusintoma.modules.doctorPatient.repository.DoctorPatientNoteRepository;
import br.com.meusintoma.utils.helpers.RepositoryUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorPatientNotesService {

        private final ConsultationService consultationService;

        private final DoctorPatientService doctorPatientService;

        private final ConsultationUtilsService consultationUtilsService;

        private final DoctorPatientNoteRepository doctorPatientNoteRepository;

        private final DoctorPatientUtilsService doctorPatientUtilsService;


        final List<ConsultationStatus> statuses = new ArrayList<>(
                        List.of(ConsultationStatus.COMPLETED, ConsultationStatus.CONFIRMED));

        public DoctorPatientNoteDTO createNotes(UUID relationshipId, UUID consultationId,
                        DoctorPatientNoteRequestDTO requestDTO) {

                DoctorPatientEntity relationship = doctorPatientService.getByIdValidated(relationshipId);
                doctorPatientUtilsService.validateAcess(relationship);

                ConsultationEntity consultation = consultationService.findConsultationWithCalendar(consultationId);

                consultationUtilsService.consultationSecurityCheck(consultation, statuses);

                DoctorPatientNoteEntity doctorPatientNotes = DoctorPatientNoteEntity.builder()
                                .consultation(consultation)
                                .doctorPatient(relationship)
                                .build();

                List<DoctorPatientNoteItemEntity> items = buildNoteItems(requestDTO.getNotes(), doctorPatientNotes);

                doctorPatientNotes.setItems(items);

                doctorPatientNoteRepository.save(doctorPatientNotes);

                return DoctorPatientNotesMapper.toDoctorPatientNoteDTO(doctorPatientNotes);
        }

        public List<DoctorPatientNoteGetDTO> getNotes(UUID relationshipId) {
                DoctorPatientEntity relationship = doctorPatientService.getByIdValidated(relationshipId);
                doctorPatientUtilsService.validateAcess(relationship);

                List<DoctorPatientNoteEntity> notes = doctorPatientNoteRepository.getNotes(relationshipId);

                return DoctorPatientNotesMapper.toDoctorPatientNoteGetDTO(notes);
        }

        public DoctorPatientNoteGetDTO updateNote(UUID relationshipId, UUID noteId, List<String> newNotes) {
                DoctorPatientEntity relationship = doctorPatientService.getByIdValidated(relationshipId);
                doctorPatientUtilsService.validateAcess(relationship);

                DoctorPatientNoteEntity note = getNoteWithItems(noteId);
                replaceNoteItems(note, newNotes);

                doctorPatientNoteRepository.save(note);
                return DoctorPatientNotesMapper.toDoctorPatientNoteGetDTO(note);
        }

        private List<DoctorPatientNoteItemEntity> buildNoteItems(List<String> contents, DoctorPatientNoteEntity note) {
                return contents.stream()
                                .map(content -> DoctorPatientNoteItemEntity.builder()
                                                .content(content)
                                                .doctorPatientNote(note)
                                                .build())
                                .collect(Collectors.toList());
        }

        private DoctorPatientNoteEntity getNoteWithItems(UUID noteId) {
                return RepositoryUtils.findOrThrow(
                                doctorPatientNoteRepository.findNoteWithItems(noteId),
                                () -> new NotFoundException("Nota"));
        }

        private void replaceNoteItems(DoctorPatientNoteEntity note, List<String> newContents) {
                note.getItems().clear();
                newContents.forEach(content -> note.getItems().add(DoctorPatientNoteItemEntity.builder()
                                .content(content)
                                .doctorPatientNote(note)
                                .build()));
        }

}
