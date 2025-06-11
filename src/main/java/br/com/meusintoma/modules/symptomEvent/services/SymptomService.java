package br.com.meusintoma.modules.symptomEvent.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.patient.entity.PatientEntity;
import br.com.meusintoma.modules.patient.services.PatientService;
import br.com.meusintoma.modules.symptomEvent.dto.SymptomEventRequestDTO;
import br.com.meusintoma.modules.symptomEvent.dto.SymptomEventResponseDTO;
import br.com.meusintoma.modules.symptomEvent.entity.SymptomEventEntity;
import br.com.meusintoma.modules.symptomEvent.mapper.SymptomEventMapper;
import br.com.meusintoma.modules.symptomEvent.repository.SymptomEventRepository;
import br.com.meusintoma.security.utils.AuthValidatorUtils;
import br.com.meusintoma.utils.helpers.GenericUtils;
import br.com.meusintoma.utils.helpers.RepositoryUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SymptomService {

    private final PatientService patientService;

    private final SymptomEventRepository symptomEventRepository;

    public SymptomEventResponseDTO createSymptomEvent(SymptomEventRequestDTO symptonEventDTO) {
        PatientEntity patient = patientService.findPatient(getAuthenticatedPatientId());

        SymptomEventEntity symptomEvent = SymptomEventMapper.toEntity(symptonEventDTO, patient);

        SymptomEventEntity savedSymptom = symptomEventRepository.save(symptomEvent);

        SymptomEventResponseDTO createdSymptom = SymptomEventMapper.toResponseDTO(savedSymptom);

        return createdSymptom;
    }

    public List<SymptomEventResponseDTO> getPatientSymptom() {
        List<SymptomEventEntity> symptoms = symptomEventRepository
                .findByPatientIdOrderByStartedAtDesc(getAuthenticatedPatientId());

        List<SymptomEventResponseDTO> symptomsResponse = SymptomEventMapper.toResponseDTO(symptoms);

        return symptomsResponse;
    }

    public List<SymptomEventResponseDTO> getPatientSymptomsBySymptomName(String symptonName) {
        List<SymptomEventEntity> symptoms = this.symptomEventRepository
                .findByPatientIdAndSymptomNameContainingIgnoreCaseOrderByStartedAtDesc(getAuthenticatedPatientId(),
                        symptonName);

        List<SymptomEventResponseDTO> symptomsResponse = SymptomEventMapper.toResponseDTO(symptoms);

        return symptomsResponse;
    }

    public SymptomEventResponseDTO updatePatientSymptom(UUID symptonId, SymptomEventRequestDTO symptonEvent) {
        SymptomEventEntity symptom = findSymptomByIdWithPatient(symptonId);

        validateSymptomBelongsToAuthenticatedPatient(symptom.getPatient().getId());

        updateEntity(symptom, symptonEvent);

        SymptomEventEntity updatedSympton = symptomEventRepository.save(symptom);

        return SymptomEventMapper.toResponseDTO(updatedSympton);
    }

    public SymptomEventResponseDTO deleteSymptom(UUID symptomId) {
        SymptomEventEntity symptom = findSymptomByIdWithPatient(symptomId);

        validateSymptomBelongsToAuthenticatedPatient(symptom.getPatient().getId());

        SymptomEventResponseDTO symptomEventResponseDTO = SymptomEventMapper.toResponseDTO(symptom);
        symptomEventRepository.deleteById(symptom.getId());

        return symptomEventResponseDTO;
    }

    private SymptomEventEntity findSymptomByIdWithPatient(UUID symptonId) {
        return RepositoryUtils.findOrThrow(symptomEventRepository.findSymptomByIdWithPatient(symptonId),
                () -> new NotFoundException("Sintoma"));
    }

    private void updateEntity(SymptomEventEntity entity, SymptomEventRequestDTO symptomDto) {
        entity.setSymptomName(symptomDto.getSymptomName());
        entity.setSeverity(symptomDto.getSeverity());
    }

    private UUID getAuthenticatedPatientId() {
        return AuthValidatorUtils.getAuthenticatedUserId();
    }

    private void validateSymptomBelongsToAuthenticatedPatient(UUID symptonPatientId) {
        GenericUtils.compareId(symptonPatientId, getAuthenticatedPatientId());
    }
}
