package br.com.meusintoma.modules.symptomEvent.mapper;

import java.util.List;

import br.com.meusintoma.modules.patient.entity.PatientEntity;
import br.com.meusintoma.modules.symptomEvent.dto.SymptomEventRequestDTO;
import br.com.meusintoma.modules.symptomEvent.dto.SymptomEventResponseDTO;
import br.com.meusintoma.modules.symptomEvent.entity.SymptomEventEntity;

public class SymptomEventMapper {

    public static SymptomEventEntity toEntity(SymptomEventRequestDTO symptonEventDTO, PatientEntity patient) {
        return SymptomEventEntity.builder()
                .symptomName(symptonEventDTO.getSymptomName())
                .severity(symptonEventDTO.getSeverity())
                .patient(patient)
                .build();
    }

    public static SymptomEventResponseDTO toResponseDTO(SymptomEventEntity symptomEventEntity) {
        SymptomEventResponseDTO responseDTO = SymptomEventResponseDTO.builder().id(symptomEventEntity.getId())
                .symptomName(symptomEventEntity.getSymptomName())
                .severity(symptomEventEntity.getSeverity()).startedAt(symptomEventEntity.getStartedAt())
                .updatedAt(symptomEventEntity.getUpdatedAt()).build();
        return responseDTO;
    }

    public static List<SymptomEventResponseDTO> toResponseDTO(List<SymptomEventEntity> entities) {
        return entities.stream()
                .map(SymptomEventMapper::toResponseDTO)
                .toList();
    }

}
