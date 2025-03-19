package br.com.meusintoma.modules.doctor.entity;

import java.util.ArrayList;
import java.util.List;

import br.com.meusintoma.modules.appointment.entity.AppointmentEntity;
import br.com.meusintoma.modules.calendar.entity.CalendarEntity;
import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientEntity;
import br.com.meusintoma.modules.user.entity.UserEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "doctor")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorEntity extends UserEntity {

    private String specialization;
    private String crm;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AppointmentEntity> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DoctorPatientEntity> doctorPatients = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CalendarEntity> calendars = new ArrayList<>();
}
