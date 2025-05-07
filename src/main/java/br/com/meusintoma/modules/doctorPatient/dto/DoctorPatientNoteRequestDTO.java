package br.com.meusintoma.modules.doctorPatient.dto;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class DoctorPatientNoteRequestDTO {
    private List<String> notes;
    private UUID doctorId;
    private UUID patientId;
}
