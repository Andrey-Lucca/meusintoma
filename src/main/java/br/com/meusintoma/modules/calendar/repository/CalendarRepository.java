package br.com.meusintoma.modules.calendar.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.meusintoma.modules.calendar.entity.CalendarEntity;
import br.com.meusintoma.modules.calendar.enums.CalendarStatus;

public interface CalendarRepository extends JpaRepository<CalendarEntity, UUID> {

    boolean existsByDoctorId(UUID doctorId);

    @EntityGraph(attributePaths = { "doctor" })
    List<CalendarEntity> findAllByDoctorId(UUID doctorId);

    @Modifying
    @Query("DELETE FROM calendar c WHERE c.date < :date AND c.calendarStatus IN :statuses")
    void deleteOldCalendarsByStatus(@Param("date") LocalDate date,
            @Param("statuses") List<CalendarStatus> statuses);

    @Query("""
                SELECT c FROM calendar c
                JOIN FETCH c.doctor d
                LEFT JOIN FETCH d.secretary
                WHERE c.id = :calendarId
            """)
    Optional<CalendarEntity> findByIdWithDoctorAndSecretary(@Param("calendarId") UUID calendarId);

    @Query("""
            SELECT DISTINCT c FROM calendar c
            JOIN FETCH c.doctor d
            LEFT JOIN FETCH d.secretary
            JOIN FETCH c.linkedHealthPlans lhp
            WHERE c.id = :calendarId
                  """)
    Optional<CalendarEntity> findByIdWithDoctorAndSecretaryAndHealthPlan(@Param("calendarId") UUID calendarId);

    @Query("SELECT c FROM calendar c WHERE c.date = :date AND c.startTime = :hour AND c.doctor.id = :doctorId")
    Optional<CalendarEntity> findByDayAndHour(@Param("date") LocalDate date, @Param("hour") LocalTime hour,
            @Param("doctorId") UUID doctorId);

    @Query("SELECT c FROM calendar c WHERE c.date BETWEEN :startDate AND :finalDate AND c.doctor.id = :doctorId")
    Optional<List<CalendarEntity>> findBySpecificalInterval(@Param("startDate") LocalDate startDate,
            @Param("finalDate") LocalDate finalDate, @Param("doctorId") UUID doctorId);

    @Query("SELECT COUNT(c) > 0 FROM calendar c WHERE c.doctor.id = :doctorId AND c.date = :targetDate AND c.startTime = :startedAt")
    boolean checkDoctorCalendarSlot(@Param("doctorId") UUID doctorId, @Param("targetDate") LocalDate targetDate,
            @Param("startedAt") LocalTime startedAt);
}
