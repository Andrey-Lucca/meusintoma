package br.com.meusintoma.modules.doctorPatient.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorPatientNoteGetDTO {
    private UUID id;
    private UUID doctorId;
    private UUID patientId;
    private UUID consultationId;

    private List<String> notes;

    private LocalDateTime createdAt;
    private LocalDate date;
    private LocalTime startedHour;
}
