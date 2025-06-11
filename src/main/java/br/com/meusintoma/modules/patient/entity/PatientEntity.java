package br.com.meusintoma.modules.patient.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.meusintoma.modules.chronicdisease.entity.ChronicDiseaseEntity;
import br.com.meusintoma.modules.consultation.entity.ConsultationEntity;
import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientEntity;
import br.com.meusintoma.modules.patient_health_plan.entity.PatientHealthPlanEntity;
import br.com.meusintoma.modules.symptomEvent.entity.SymptomEventEntity;
import br.com.meusintoma.modules.user.entity.UserEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity(name = "patient")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class PatientEntity extends UserEntity {

    private String phoneNumber;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<PatientHealthPlanEntity> healthPlanLinks = new HashSet<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ChronicDiseaseEntity> chronicDiseases = new HashSet<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SymptomEventEntity> symptonEvents = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DoctorPatientEntity> doctorPatients = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ConsultationEntity> appointments = new ArrayList<>();

}
