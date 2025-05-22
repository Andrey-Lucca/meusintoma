package br.com.meusintoma.modules.calendarHealthPlan.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.meusintoma.modules.calendarHealthPlan.entity.CalendarHealthPlanEntity;

public interface CalendarHealthPlanRepository extends JpaRepository<CalendarHealthPlanEntity, UUID> {
    @Query("SELECT COUNT(chp) > 0 FROM calendar_health_plan chp WHERE chp.calendar.id = :calendarId AND chp.healthPlan.name = :healthPlanName")
    boolean existsByCalendarIdAndHealthPlanName(@Param("calendarId") UUID calendarId,
            @Param("healthPlanName") String healthPlanName);

    @Query("SELECT chp FROM calendar_health_plan chp JOIN FETCH calendar c JOIN FETCH c.doctor d JOIN FETCH healthPlan hp WHERE d.id = :doctorId AND c.calendarStatus = 'AVAILABLE'")
    Optional<List<CalendarHealthPlanEntity>> getAllAvaliableCalendarsByDoctorId(@Param("doctorId") UUID doctorId);

}
