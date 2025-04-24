package br.com.meusintoma.modules.consultation.entity;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class SnapShotInfo {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
}

