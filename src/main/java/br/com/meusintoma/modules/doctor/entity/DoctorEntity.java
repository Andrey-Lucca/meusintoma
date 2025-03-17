package br.com.meusintoma.modules.doctor.entity;

import br.com.meusintoma.modules.user.entity.UserEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "doctor")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorEntity extends UserEntity{

    private String specialization;
    private String crm;

}
