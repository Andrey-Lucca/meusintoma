package br.com.meusintoma.modules.patient.entity;

import java.util.ArrayList;
import java.util.List;

import br.com.meusintoma.modules.appointment.entity.AppointmentEntity;
import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientEntity;
import br.com.meusintoma.modules.symptonEvent.entity.SymptonEventEntity;
import br.com.meusintoma.modules.user.entity.UserEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity(name = "patient")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class PatientEntity extends UserEntity {

    private String phoneNumber;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SymptonEventEntity> symptonEvents = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DoctorPatientEntity> doctorPatients = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AppointmentEntity> appointments = new ArrayList<>();

}
