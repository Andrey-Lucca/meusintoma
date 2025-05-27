package br.com.meusintoma.modules.secretary.entity;

import java.util.ArrayList;
import java.util.List;

import br.com.meusintoma.modules.doctorSecretary.entity.DoctorSecretaryEntity;
import br.com.meusintoma.modules.user.entity.UserEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Builder;
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

    @OneToMany(mappedBy = "secretary", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default private List<DoctorSecretaryEntity> secretaryDoctorsAssociateds = new ArrayList<>();
}