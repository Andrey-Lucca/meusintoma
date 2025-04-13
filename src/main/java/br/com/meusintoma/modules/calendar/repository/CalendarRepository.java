package br.com.meusintoma.modules.calendar.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.meusintoma.modules.calendar.entity.CalendarEntity;
import br.com.meusintoma.modules.calendar.enums.CalendarStatus;

public interface CalendarRepository extends JpaRepository<CalendarEntity, UUID> {

    boolean existsByDoctorId(UUID doctorId);

    List<CalendarEntity> findAllByDoctorId(UUID doctorId);

    @Modifying
    @Query("DELETE FROM calendar c WHERE c.date < :date AND c.calendarStatus IN :statuses")
    void deleteOldCalendarsByStatus(@Param("date") LocalDate date, @Param("statuses") List<CalendarStatus> statuses);

}
