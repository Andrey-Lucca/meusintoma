package br.com.meusintoma.modules.doctor.services;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.calendar.entity.CalendarEntity;
import br.com.meusintoma.modules.calendarHealthPlan.entity.CalendarHealthPlanEntity;
import br.com.meusintoma.modules.calendarHealthPlan.services.CalendarHealthPlanService;
import br.com.meusintoma.modules.doctor.dto.DoctorCalendarResponseDTO;
import br.com.meusintoma.modules.doctor.entity.DoctorEntity;
import br.com.meusintoma.modules.doctor.mapper.DoctorMapper;
import br.com.meusintoma.modules.doctor.repository.DoctorRepository;
import br.com.meusintoma.modules.patient_health_plan.services.PatientHealthPlanService;
import br.com.meusintoma.utils.helpers.GenericUtils;
import br.com.meusintoma.utils.helpers.RepositoryUtils;

@Service
public class DoctorService {

        @Autowired
        DoctorRepository doctorRepository;

        @Autowired
        PatientHealthPlanService patientHealthPlanService;

        @Autowired
        CalendarHealthPlanService calendarHealthPlanService;

        public DoctorEntity findDoctor(UUID doctorId) {
                DoctorEntity doctor = RepositoryUtils.findOrThrow(doctorRepository.findById(doctorId),
                                () -> new NotFoundException("Doutor"));
                return doctor;
        }

        public UUID findSecretaryIdByDoctorId(UUID doctorId) {
                UUID secretaryId = RepositoryUtils.findOrThrow(doctorRepository.findSecretaryByDoctorId(doctorId),
                                () -> new NotFoundException("Secretária"));
                return secretaryId;
        }

        public UUID getDoctorIdBySecretaryId(UUID secretaryId) {
                UUID secretary = RepositoryUtils.findOrThrow(doctorRepository.findDoctorIdBySecretaryId(secretaryId),
                                () -> new NotFoundException("Secretaria"));
                return secretary;
        }

        public DoctorCalendarResponseDTO getSpecificalDoctorCalendarWithHealthPlan(UUID doctorId, UUID calendarId) {
                List<CalendarHealthPlanEntity> calendarHealthPlans = calendarHealthPlanService
                                .getCalendarHealthPlan(doctorId, calendarId);
                GenericUtils.checkIsEmptyList(calendarHealthPlans);
                Map<UUID, String> calendarHealthPlansMap = calendarHealthPlans.stream()
                                .collect(Collectors.toMap(
                                                CalendarHealthPlanEntity::getId,
                                                chp -> chp.getHealthPlan().getName()));
                CalendarEntity calendar = calendarHealthPlans.get(0).getCalendar();
                return DoctorMapper.toDoctorSpecificalCalendarResponseDTO(calendar, calendarHealthPlansMap);
        }

        public List<DoctorCalendarResponseDTO> getDoctorCalendarsFilteredWithPatientHealthPlans(UUID doctorId) {
                List<CalendarHealthPlanEntity> calendarsHealthPlans = calendarHealthPlanService
                                .getAllCalendarsWithHealthPlansByDoctor(doctorId);

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

                List<CalendarHealthPlanEntity> calendarsHealthPlans = calendarHealthPlanService
                                .getAllCalendarsWithHealthPlansByDoctor(doctorId);

                calendarsHealthPlans = calendarsHealthPlans.stream()
                                .filter(chp -> patientPlans.contains(chp.getHealthPlan().getName()))
                                .toList();

                List<DoctorCalendarResponseDTO> calendarsHealthPlansResponse = calendarsHealthPlans.stream()
                                .map(chp -> DoctorMapper.toDoctorCalendarResponseDTO(chp))
                                .toList();

                return calendarsHealthPlansResponse;
        }

        public void deleteCalendarHealthPlan(UUID doctorId, UUID calendarId, UUID calendarHealthPlanId) {
                calendarHealthPlanService.deleteCalendarHealthPlan(doctorId, calendarId, calendarHealthPlanId);
        }
}
