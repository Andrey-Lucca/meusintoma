package br.com.meusintoma.modules.doctor.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.calendarHealthPlan.entity.CalendarHealthPlanEntity;
import br.com.meusintoma.modules.calendarHealthPlan.services.CalendarHealthPlanService;
import br.com.meusintoma.modules.doctor.dto.DoctorCalendarResponseDTO;
import br.com.meusintoma.modules.doctor.entity.DoctorEntity;
import br.com.meusintoma.modules.doctor.mapper.DoctorMapper;
import br.com.meusintoma.modules.doctor.repository.DoctorRepository;
import br.com.meusintoma.modules.patient_health_plan.services.PatientHealthPlanService;
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
}
