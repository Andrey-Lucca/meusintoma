package br.com.meusintoma.modules.symptonEvent.mapper;

import br.com.meusintoma.modules.patient.entity.PatientEntity;
import br.com.meusintoma.modules.symptonEvent.dto.SymptonEventRequestDTO;
import br.com.meusintoma.modules.symptonEvent.dto.SymptonEventResponseDTO;
import br.com.meusintoma.modules.symptonEvent.entity.SymptonEventEntity;

public class SymptonEventMapper {

    public static SymptonEventEntity toEntity(SymptonEventRequestDTO symptonEventDTO, PatientEntity patient) {
        return SymptonEventEntity.builder()
                .symptonName(symptonEventDTO.getSymptonName())
                .severity(symptonEventDTO.getSeverity())
                .patient(patient)
                .build();
    }

    public static SymptonEventResponseDTO toResponseDTO(SymptonEventEntity symptonEventEntity) {
        var responseDTO = SymptonEventResponseDTO.builder().id(symptonEventEntity.getId())
                .symptonName(symptonEventEntity.getSymptonName())
                .severity(symptonEventEntity.getSeverity()).startedAt(symptonEventEntity.getStartedAt()).build();
        return responseDTO;
    }
}
