package br.com.meusintoma.modules.doctor.dto;

import br.com.meusintoma.modules.doctor.enums.DoctorSpecialization;
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
    private DoctorSpecialization specialization;
    private String crm;
}
