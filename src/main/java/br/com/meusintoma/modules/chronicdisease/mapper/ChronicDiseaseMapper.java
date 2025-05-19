package br.com.meusintoma.modules.chronicdisease.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.meusintoma.modules.chronicdisease.dto.ChronicDiseaseResponseDTO;
import br.com.meusintoma.modules.chronicdisease.entity.ChronicDiseaseEntity;

public class ChronicDiseaseMapper {

    public static List<ChronicDiseaseResponseDTO> toChronicDiseaseResponseDTO(Set<ChronicDiseaseEntity> chronicDiseases) {
        return chronicDiseases.stream().map(disease -> ChronicDiseaseResponseDTO.builder().diseaseId(disease.getId())
                .diseaseTypeId(disease.getDiseaseType().getId())
                .diseaseTypeName(disease.getDiseaseType()
                .getName())
                .yearsQuantity(disease.getYearsQuantity())
                .severity(disease.getSeverity()).build())
                .collect(Collectors.toList());
    }
}
