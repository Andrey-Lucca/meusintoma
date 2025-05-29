package br.com.meusintoma.modules.doctorPatient.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.meusintoma.modules.doctorPatient.enums.DoctorPatientStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorPatientInviteResponseDTO {
    private UUID id;
    private DoctorPatientStatus status;
    private String doctorName;
    private LocalDateTime associatedDate;
}
