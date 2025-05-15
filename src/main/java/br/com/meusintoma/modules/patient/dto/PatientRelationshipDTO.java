package br.com.meusintoma.modules.patient.dto;


import java.util.UUID;

import br.com.meusintoma.modules.doctor.enums.DoctorSpecialization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientRelationshipDTO {
    private UUID relationshipId;
    private String doctorName;
    private DoctorSpecialization specialization;
    private String crm;
}
