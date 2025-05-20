package br.com.meusintoma.modules.patient_health_plan.entity;

import java.util.UUID;

import br.com.meusintoma.modules.healthPlan.entity.HealthPlanEntity;
import br.com.meusintoma.modules.patient.entity.PatientEntity;
import jakarta.persistence.Column;
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

@Entity(name = "patient_health_plan")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientHealthPlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String cardIdentification;

    @ManyToOne
    @JoinColumn(name = "health_plan_id")
    private HealthPlanEntity healthPlan;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private PatientEntity patient;
}
