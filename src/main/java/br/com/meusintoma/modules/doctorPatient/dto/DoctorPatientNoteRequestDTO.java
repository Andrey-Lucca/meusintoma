package br.com.meusintoma.modules.doctorPatient.dto;

import java.util.List;

import lombok.Data;

@Data
public class DoctorPatientNoteRequestDTO {
    private List<String> notes;
}
