package br.com.meusintoma.modules.doctorPatient.dto;

import br.com.meusintoma.modules.doctorPatient.enums.DoctorPatientStatus;
import lombok.Data;

@Data
public class ChangeInviteStatusDTO {
    DoctorPatientStatus status;
}
