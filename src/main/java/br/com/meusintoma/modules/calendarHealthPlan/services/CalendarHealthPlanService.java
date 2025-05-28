package br.com.meusintoma.modules.calendarHealthPlan.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.CustomAccessDeniedException;
import br.com.meusintoma.exceptions.globalCustomException.InvalidDateException;
import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.calendar.entity.CalendarEntity;
import br.com.meusintoma.modules.calendar.services.CalendarService;
import br.com.meusintoma.modules.calendarHealthPlan.dto.CalendarHealthPlanAssociateRequestDTO;
import br.com.meusintoma.modules.calendarHealthPlan.dto.CalendarHealthPlanResponseCreationDTO;
import br.com.meusintoma.modules.calendarHealthPlan.entity.CalendarHealthPlanEntity;
import br.com.meusintoma.modules.calendarHealthPlan.repository.CalendarHealthPlanRepository;
import br.com.meusintoma.modules.doctor.dto.DoctorCalendarResponseDTO;
import br.com.meusintoma.modules.doctor.mapper.DoctorMapper;
import br.com.meusintoma.modules.healthPlan.entity.HealthPlanEntity;
import br.com.meusintoma.modules.healthPlan.services.HealthPlanService;
import br.com.meusintoma.modules.patient_health_plan.services.PatientHealthPlanService;
import br.com.meusintoma.utils.common.StatusResult;
import br.com.meusintoma.utils.helpers.GenericUtils;
import br.com.meusintoma.utils.helpers.RepositoryUtils;
import jakarta.transaction.Transactional;

@Service
public class CalendarHealthPlanService {

    @Autowired
    CalendarService calendarService;

    @Autowired
    HealthPlanService healthPlanService;

    @Autowired
    PatientHealthPlanService patientHealthPlanService;

    @Autowired
    CalendarHealthPlanRepository calendarHealthPlanRepository;

    public List<CalendarHealthPlanResponseCreationDTO> associateHealthPlanToCalendar(
            CalendarHealthPlanAssociateRequestDTO calendarHealthPlanDTO) {

        List<CalendarHealthPlanResponseCreationDTO> results = new ArrayList<>();
        List<CalendarHealthPlanEntity> calendarHealthPlansToSave = new ArrayList<>();

        for (Map.Entry<UUID, Map<UUID, Set<String>>> doctorEntry : calendarHealthPlanDTO
                .getDoctorCalendarsHealthPlansMap().entrySet()) {
            UUID doctorId = doctorEntry.getKey();
            Map<UUID, Set<String>> calendarHealthPlansMap = doctorEntry.getValue();

            for (Map.Entry<UUID, Set<String>> calendarEntry : calendarHealthPlansMap.entrySet()) {
                UUID calendarId = calendarEntry.getKey();
                Set<String> healthPlanNames = calendarEntry.getValue();

                try {
                    CalendarEntity calendar = calendarService.findByCalendarIdWithDoctor(calendarId);

                    try {
                        calendarService.checkCalendarPermissions(doctorId, calendar.getDate());

                        for (String healthPlanName : healthPlanNames) {
                            boolean exists = calendarHealthPlanRepository
                                    .existsByCalendarIdAndHealthPlanName(calendarId, healthPlanName);

                            if (exists) {
                                CalendarHealthPlanServiceUtils.addResult(calendar, doctorId, healthPlanName,
                                        StatusResult.ALREADY_EXISTS, "Relação já existente", results);
                                continue;
                            }
                            try {
                                HealthPlanEntity healthPlan = healthPlanService.findPlanByName(healthPlanName);
                                CalendarHealthPlanEntity calendarHealthPlan = CalendarHealthPlanServiceUtils
                                        .createCalendarHealthPlan(calendar, healthPlan);
                                calendarHealthPlansToSave.add(calendarHealthPlan);
                                CalendarHealthPlanServiceUtils.addResult(calendar, doctorId, healthPlanName,
                                        StatusResult.ASSOCIATED, "Relação criada com sucesso", results);
                            } catch (NotFoundException e) {
                                CalendarHealthPlanServiceUtils.addResult(calendar, doctorId, healthPlanName,
                                        StatusResult.ERROR, e.getMessage(), results);
                            }
                        }
                    } catch (CustomAccessDeniedException | InvalidDateException e) {
                        CalendarHealthPlanServiceUtils.addResult(calendar, doctorId, null,
                                StatusResult.ERROR, e.getMessage(), results);
                    }

                } catch (Exception e) {
                    CalendarHealthPlanServiceUtils.addResult(doctorId, null,
                            StatusResult.ERROR, e.getMessage(), results);
                }
            }
        }

        calendarHealthPlanRepository.saveAll(calendarHealthPlansToSave);
        return results;
    }

    public List<CalendarHealthPlanEntity> getAllCalendarsWithHealthPlansByDoctor(UUID doctorId) {
        return RepositoryUtils.findOrThrow(calendarHealthPlanRepository.getAllAvaliableCalendarsByDoctorId(doctorId),
                () -> new NotFoundException("Calendário - Plano"));
    }

    public List<CalendarHealthPlanEntity> getCalendarHealthPlan(UUID doctorId, UUID calendarId) {
        return RepositoryUtils.findOrThrow(calendarHealthPlanRepository.getCalendarByIdAndDoctor(doctorId, calendarId),
                () -> new NotFoundException("Calendário - Plano"));
    }

    public DoctorCalendarResponseDTO getSpecificalDoctorCalendarWithHealthPlan(UUID doctorId, UUID calendarId) {
        List<CalendarHealthPlanEntity> calendarHealthPlans = getCalendarHealthPlan(doctorId, calendarId);
        GenericUtils.checkIsEmptyList(calendarHealthPlans);
        Map<UUID, String> calendarHealthPlansMap = calendarHealthPlans.stream()
                .collect(Collectors.toMap(
                        CalendarHealthPlanEntity::getId,
                        chp -> chp.getHealthPlan().getName()));
        CalendarEntity calendar = calendarHealthPlans.get(0).getCalendar();
        return DoctorMapper.toDoctorSpecificalCalendarResponseDTO(calendar, calendarHealthPlansMap);
    }

    public List<DoctorCalendarResponseDTO> getDoctorCalendarsFilteredWithPatientHealthPlans(UUID doctorId) {
        calendarService.checkCalendarPermissions(doctorId, null);

        List<CalendarHealthPlanEntity> calendarsHealthPlans = getAllCalendarsWithHealthPlansByDoctor(doctorId);

        return calendarsHealthPlans.stream()
                .map(chp -> {
                    Map<UUID, String> map = new HashMap<>();
                    map.put(chp.getId(), chp.getHealthPlan().getName());
                    return DoctorMapper.toDoctorSpecificalCalendarResponseDTO(chp.getCalendar(),
                            map);
                })
                .sorted(Comparator.comparing(DoctorCalendarResponseDTO::getDate)
                        .thenComparing(DoctorCalendarResponseDTO::getStartAt))

                .collect(Collectors.toList());
    }

    public List<DoctorCalendarResponseDTO> getDoctorCalendarsFilteredWithPatientHealthPlans(UUID doctorId,
            UUID patientId) {
        List<String> patientPlans = patientHealthPlanService.getOnlyHealthPlans(patientId);
        patientPlans.add("PARTICULAR");

        List<CalendarHealthPlanEntity> calendarsHealthPlans = getAllCalendarsWithHealthPlansByDoctor(doctorId);

        calendarsHealthPlans = calendarsHealthPlans.stream()
                .filter(chp -> patientPlans.contains(chp.getHealthPlan().getName()))
                .toList();

        List<DoctorCalendarResponseDTO> calendarsHealthPlansResponse = calendarsHealthPlans.stream()
                .map(chp -> DoctorMapper.toDoctorCalendarResponseDTO(chp))
                .toList();

        return calendarsHealthPlansResponse;
    }

    @Transactional
    public void deleteCalendarHealthPlan(UUID doctorId, UUID calendarId, UUID calendarHealthPlanId) {
        CalendarEntity calendar = calendarService.findByCalendarIdWithDoctor(calendarId);
        calendarService.checkCalendarPermissions(doctorId, calendar.getDate());
        RepositoryUtils.findOrThrow(calendarHealthPlanRepository.findById(calendarHealthPlanId),
                () -> new NotFoundException("Calendário - Plano"));
        calendarHealthPlanRepository.deleteById(calendarHealthPlanId);
    }

}
