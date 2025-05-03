package br.com.meusintoma.modules.doctorPatient.dto;

import java.time.LocalDateTime;

import br.com.meusintoma.modules.doctorPatient.enums.DoctorPatientStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorPatientInviteDTO {
    DoctorPatientStatus status;
    String patientName;
    String doctorName;
    LocalDateTime associatedDate;
}
