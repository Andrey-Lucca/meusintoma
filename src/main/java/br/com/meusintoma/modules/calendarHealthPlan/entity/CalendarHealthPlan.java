package br.com.meusintoma.modules.calendarHealthPlan.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.meusintoma.modules.calendar.entity.CalendarEntity;
import br.com.meusintoma.modules.healthPlan.entity.HealthPlanEntity;
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

@Entity(name = "calendar_health_plan")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalendarHealthPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDateTime validUntil;

    @ManyToOne
    @JoinColumn(name = "calendar_id")
    private CalendarEntity calendar;

    @ManyToOne
    @JoinColumn(name = "health_plan_id")
    private HealthPlanEntity healthPlan;
}
