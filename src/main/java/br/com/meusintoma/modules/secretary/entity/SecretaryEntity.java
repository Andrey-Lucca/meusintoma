package br.com.meusintoma.modules.secretary.entity;

import java.util.ArrayList;
import java.util.List;

import br.com.meusintoma.modules.doctor.entity.DoctorEntity;
import br.com.meusintoma.modules.user.entity.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity(name = "secretary")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor

public class SecretaryEntity extends UserEntity {

    @OneToMany(mappedBy = "secretary")
    private List<DoctorEntity> doctors = new ArrayList<>();
}