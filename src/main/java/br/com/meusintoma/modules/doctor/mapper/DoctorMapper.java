package br.com.meusintoma.modules.doctor.mapper;

import br.com.meusintoma.modules.calendarHealthPlan.entity.CalendarHealthPlanEntity;
import br.com.meusintoma.modules.doctor.dto.DoctorCalendarResponseDTO;
import br.com.meusintoma.modules.doctor.dto.DoctorResponseDTO;
import br.com.meusintoma.modules.doctor.entity.DoctorEntity;

public class DoctorMapper {

    public static DoctorResponseDTO toDoctorResponseDTO(DoctorEntity doctor) {
        return DoctorResponseDTO.builder().crm(doctor.getCrm()).name(doctor.getName())
                .specialization(doctor.getSpecialization()).id(doctor.getId()).build();
    }

    public static DoctorCalendarResponseDTO toDoctorCalendarResponseDTO(CalendarHealthPlanEntity calendarHealthPlan) {
        return DoctorCalendarResponseDTO.builder().startAt(calendarHealthPlan.getCalendar().getStartTime())
                .endAt(calendarHealthPlan.getCalendar().getEndTime())
                .calendarId(calendarHealthPlan.getCalendar().getId())
                .calendarStatus(calendarHealthPlan.getCalendar().getCalendarStatus())
                .date(calendarHealthPlan.getCalendar().getDate()).build();
    }
}
