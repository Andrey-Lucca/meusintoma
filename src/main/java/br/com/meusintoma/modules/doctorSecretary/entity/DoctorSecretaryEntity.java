package br.com.meusintoma.modules.doctorSecretary.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.meusintoma.modules.doctor.entity.DoctorEntity;
import br.com.meusintoma.modules.secretary.entity.SecretaryEntity;
import br.com.meusintoma.utils.common.AssociationStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "doctor_secretary")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorSecretaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private DoctorEntity doctor;

    @ManyToOne
    @JoinColumn(name = "secretary_id", nullable = false)
    private SecretaryEntity secretary;

    private LocalDateTime associatedAt;

    private LocalDateTime invitedAt;

    @Enumerated(EnumType.STRING)
    private AssociationStatus association;
}
