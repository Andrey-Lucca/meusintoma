package br.com.meusintoma.modules.calendar.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.meusintoma.modules.calendar.entity.CalendarEntity;

public interface CalendarRepository extends JpaRepository<CalendarEntity, UUID> {

    boolean existsByDoctorId(UUID doctorId);

    List<CalendarEntity> findAllByDoctorId(UUID doctorId);
}
