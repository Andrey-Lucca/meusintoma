package br.com.meusintoma.modules.chronicdisease.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiseaseTypeRequestDTO {

    private String diseaseName;
    private String description;
}
