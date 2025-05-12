package br.com.meusintoma.modules.doctor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorResponseDTO {
    private String name;
    private String specialization;
    private String crm;
}
