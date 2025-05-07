package br.com.meusintoma.modules.doctorPatient.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.modules.consultation.entity.ConsultationEntity;
import br.com.meusintoma.modules.consultation.enums.ConsultationStatus;
import br.com.meusintoma.modules.consultation.services.ConsultationService;
import br.com.meusintoma.modules.consultation.services.ConsultationUtilsService;
import br.com.meusintoma.modules.doctorPatient.Mapper.DoctorPatientNotesMapper;
import br.com.meusintoma.modules.doctorPatient.dto.DoctorPatientNoteDTO;
import br.com.meusintoma.modules.doctorPatient.dto.DoctorPatientNoteRequestDTO;
import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientEntity;
import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientNoteEntity;
import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientNoteItemEntity;
import br.com.meusintoma.modules.doctorPatient.repository.DoctorPatientNoteRepository;

@Service
public class DoctorPatientNotesService {

    @Autowired
    ConsultationService consultationService;

    @Autowired
    DoctorPatientService doctorPatientService;

    @Autowired
    ConsultationUtilsService consultationUtilsService;

    @Autowired
    DoctorPatientNoteRepository doctorPatientNoteRepository;

    final List<ConsultationStatus> statuses = new ArrayList<>(
            List.of(ConsultationStatus.COMPLETED, ConsultationStatus.CONFIRMED));

    public DoctorPatientNoteDTO createNotes(UUID relationshipId, UUID consultationId,
            DoctorPatientNoteRequestDTO requestDTO) {

        DoctorPatientEntity doctorPatientRelationship = doctorPatientService
                .getDoctorPatientRelationship(relationshipId, requestDTO.getDoctorId(), requestDTO.getPatientId());

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

}
