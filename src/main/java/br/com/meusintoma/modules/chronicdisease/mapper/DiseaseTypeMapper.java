package br.com.meusintoma.modules.chronicdisease.mapper;

import br.com.meusintoma.modules.chronicdisease.dto.DiseaseTypeResponseDTO;
import br.com.meusintoma.modules.chronicdisease.entity.DiseaseTypeEntity;

public class DiseaseTypeMapper {

    public static DiseaseTypeResponseDTO toDiseaseTypeResponse(DiseaseTypeEntity diseaseType) {
        return DiseaseTypeResponseDTO.builder().description(diseaseType.getDescription())
                .name(diseaseType.getName()).patientId(diseaseType.getPatientId())
                .status(diseaseType.getApprovalStatus()).build();
    }
}
