package br.com.meusintoma.modules.consultation.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.meusintoma.modules.calendar.entity.CalendarEntity;
import br.com.meusintoma.modules.consultation.enums.ConsultationStatus;
import br.com.meusintoma.modules.patient.entity.PatientEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "consultation")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsultationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Enumerated(EnumType.STRING)
    private ConsultationStatus status;

    @Column(updatable = true, nullable = true)
    private String canceledBy;

    @Embedded
    private SnapShotInfo snapshot;

    @Column(name = "doctor_id", nullable = false, updatable = false)
    private UUID doctorId;

    @Column(name = "secretary_id", nullable = true, updatable = false)
    private UUID secretaryId;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientEntity patient;

    @OneToOne
    @JoinColumn(name = "calendar_id", nullable = true)
    private CalendarEntity calendarSlot;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = null;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
