package br.com.meusintoma.modules.doctor.entity;

import java.util.ArrayList;
import java.util.List;

import br.com.meusintoma.modules.calendar.entity.CalendarEntity;
import br.com.meusintoma.modules.doctor.enums.DoctorSpecialization;
import br.com.meusintoma.modules.doctorPatient.entity.DoctorPatientEntity;
import br.com.meusintoma.modules.secretary.entity.SecretaryEntity;
import br.com.meusintoma.modules.user.entity.UserEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity(name = "doctor")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class DoctorEntity extends UserEntity {

    @Enumerated(EnumType.STRING)
    private DoctorSpecialization specialization;
    private String crm;

    @Builder.Default @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DoctorPatientEntity> doctorPatients = new ArrayList<>();

    @Builder.Default @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CalendarEntity> calendars = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "secretary_id")
    private SecretaryEntity secretary;
}
