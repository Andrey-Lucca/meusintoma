package br.com.meusintoma.modules.doctorPatient.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import br.com.meusintoma.modules.consultation.entity.ConsultationEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "doctor_patient_note")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorPatientNoteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "doctor_patient_id", nullable = false)
    private DoctorPatientEntity doctorPatient;

    @ManyToOne
    private ConsultationEntity consultation;

    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
