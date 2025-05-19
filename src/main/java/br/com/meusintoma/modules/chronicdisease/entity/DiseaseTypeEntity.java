package br.com.meusintoma.modules.chronicdisease.entity;

import java.util.UUID;

import br.com.meusintoma.modules.chronicdisease.enums.ApprovalStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "diseaseType")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiseaseTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String description;

    private UUID patientId;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;
}
