package br.com.meusintoma.modules.chronicdisease.dto;

import java.util.Set;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChronicDiseaseRequestDTO {
    private UUID patientId;
    private Set<ChronicDiseaseInputDTO> chronicDiseases;
}
