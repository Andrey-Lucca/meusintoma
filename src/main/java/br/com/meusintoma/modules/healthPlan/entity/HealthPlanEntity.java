package br.com.meusintoma.modules.healthPlan.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import br.com.meusintoma.modules.calendarHealthPlan.entity.CalendarHealthPlanEntity;
import br.com.meusintoma.modules.patient_health_plan.entity.PatientHealthPlanEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "healthPlan")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HealthPlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @OneToMany(mappedBy = "healthPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<PatientHealthPlanEntity> patientAssociations = new HashSet<>();

    @OneToMany(mappedBy = "healthPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<CalendarHealthPlanEntity> linkedCalendars  = new HashSet<>();
}
