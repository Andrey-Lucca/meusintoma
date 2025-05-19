package br.com.meusintoma.modules.chronicdisease.entity;

import java.util.UUID;

import br.com.meusintoma.modules.patient.entity.PatientEntity;
import br.com.meusintoma.utils.common.Severity;
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

@Entity(name = "chronicDisease")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChronicDiseaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "disease_type_id")
    private DiseaseTypeEntity diseaseType;

    private int yearsQuantity;

    @Enumerated(EnumType.STRING)

    private Severity severity;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private PatientEntity patient;

}
