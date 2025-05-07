package br.com.meusintoma.modules.doctorPatient.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorPatientNoteDTO {
    private List<String> notes;
    private LocalDateTime createdAt;
    private LocalDate date;
    private LocalTime startedHour;
}
