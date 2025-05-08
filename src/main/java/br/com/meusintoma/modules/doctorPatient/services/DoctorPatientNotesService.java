package br.com.meusintoma.modules.doctorPatient.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.consultation.entity.ConsultationEntity;
import br.com.meusintoma.modules.consultation.enums.ConsultationStatus;
import br.com.meusintoma.modules.consultation.services.ConsultationService;
import br.com.meusintoma.modules.consultation.services.ConsultationUtilsService;
import br.com.meusintoma.modules.doctor.services.DoctorService;
import br.com.meusintoma.modules.doctorPatient.Mapper.DoctorPatientNotesMapper;
import br.com.meusintoma.modules.doctorPatient.dto.DoctorPatientNoteDTO;
import br.com.meusintoma.modules.doctorPatient.dto.DoctorPatientNoteGetDTO;
import br.com.meusintoma.modules.doctorPatient.dto.DoctorPatientNoteRequestDTO;
import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientEntity;
import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientNoteEntity;
import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientNoteItemEntity;
import br.com.meusintoma.modules.doctorPatient.repository.DoctorPatientNoteRepository;
import br.com.meusintoma.modules.doctorPatient.repository.DoctorPatientRepository;
import br.com.meusintoma.security.utils.AuthValidatorUtils;
import br.com.meusintoma.utils.RepositoryUtils;

@Service
public class DoctorPatientNotesService {

        @Autowired
        ConsultationService consultationService;

        @Autowired
        DoctorPatientService doctorPatientService;

        @Autowired
        DoctorService doctorService;

        @Autowired
        ConsultationUtilsService consultationUtilsService;

        @Autowired
        DoctorPatientNoteRepository doctorPatientNoteRepository;

        @Autowired
        DoctorPatientUtilsService doctorPatientUtilsService;

        @Autowired
        DoctorPatientRepository doctorPatientRepository;

        final List<ConsultationStatus> statuses = new ArrayList<>(
                        List.of(ConsultationStatus.COMPLETED, ConsultationStatus.CONFIRMED));

        public DoctorPatientNoteDTO createNotes(UUID relationshipId, UUID consultationId,
                        DoctorPatientNoteRequestDTO requestDTO) {

                DoctorPatientEntity doctorPatientRelationship = doctorPatientService
                                .getDoctorPatientRelationship(relationshipId, requestDTO.getDoctorId(),
                                                requestDTO.getPatientId());

                ConsultationEntity consultation = consultationService.findConsultationWithCalendar(consultationId);

                consultationUtilsService.validateUserPermission(consultation);
                consultationUtilsService.checkConsultationStatusForNotes(consultation, statuses);

                DoctorPatientNoteEntity doctorPatientNotes = DoctorPatientNoteEntity.builder()
                                .consultation(consultation)
                                .doctorPatient(doctorPatientRelationship)
                                .build();

                List<DoctorPatientNoteItemEntity> items = requestDTO.getNotes().stream()
                                .map(note -> DoctorPatientNoteItemEntity.builder()
                                                .content(note)
                                                .doctorPatientNote(doctorPatientNotes)
                                                .build())
                                .collect(Collectors.toList());

                doctorPatientNotes.setItems(items);

                doctorPatientNoteRepository.save(doctorPatientNotes);

                return DoctorPatientNotesMapper.toDoctorPatientNoteDTO(doctorPatientNotes);
        }

        public List<DoctorPatientNoteGetDTO> getNotes(UUID relationshipId) {
                UUID targetUserId = AuthValidatorUtils.getAuthenticatedUserId();
                DoctorPatientEntity relationship = doctorPatientService.getByIdValidated(relationshipId);
                List<UUID> allowedIds = doctorPatientUtilsService.createPermissionList(relationship);
                DoctorPatientUtilsService.checkUserPermission(allowedIds, targetUserId);
                List<DoctorPatientNoteEntity> notes = doctorPatientNoteRepository.getNotes(relationshipId);
                return DoctorPatientNotesMapper.toDoctorPatientNoteGetDTO(notes);
        }

        public DoctorPatientNoteGetDTO updateNote(UUID relationshipId, UUID noteId, List<String> newNotes) {
                UUID targetUserId = AuthValidatorUtils.getAuthenticatedUserId();
                DoctorPatientEntity relationship = doctorPatientService.getByIdValidated(relationshipId);
                List<UUID> allowedIds = doctorPatientUtilsService
                                .createPermissionList(relationship);
                DoctorPatientUtilsService.checkUserPermission(allowedIds, targetUserId);

                DoctorPatientNoteEntity note = RepositoryUtils.findOrThrow(
                                doctorPatientNoteRepository.findNoteWithItems(noteId),
                                () -> new NotFoundException("Nota"));

                // ✅ Alternativa segura: limpar corretamente e reconstruir a lista
                note.getItems().clear();
                newNotes.forEach(content -> {
                        DoctorPatientNoteItemEntity item = DoctorPatientNoteItemEntity.builder()
                                        .content(content)
                                        .doctorPatientNote(note)
                                        .build();
                        note.getItems().add(item); // garante que você está modificando a lista diretamente
                });

                doctorPatientNoteRepository.save(note);

                return DoctorPatientNotesMapper.toDoctorPatientNoteGetDTO(note);
        }

}
