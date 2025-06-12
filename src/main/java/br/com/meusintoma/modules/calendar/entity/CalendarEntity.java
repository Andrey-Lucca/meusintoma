package br.com.meusintoma.modules.calendar.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import br.com.meusintoma.modules.calendar.enums.CalendarStatus;
import br.com.meusintoma.modules.calendarHealthPlan.entity.CalendarHealthPlanEntity;
import br.com.meusintoma.modules.doctor.entity.DoctorEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "calendar")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalendarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<CalendarHealthPlanEntity> linkedHealthPlans = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private DoctorEntity doctor;

    @Enumerated(EnumType.STRING)
    private CalendarStatus calendarStatus;
}
