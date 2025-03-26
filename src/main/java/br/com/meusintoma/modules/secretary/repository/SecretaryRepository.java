package br.com.meusintoma.modules.secretary.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import br.com.meusintoma.modules.secretary.entity.SecretaryEntity;

public interface SecretaryRepository extends JpaRepository<SecretaryEntity, UUID> {

    boolean existsByIdAndDoctorsId(UUID id, UUID doctorsId);

}