package br.com.meusintoma.modules.secretary.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

 import br.com.meusintoma.modules.secretary.entity.SecretaryEntity;

public interface SecretaryRepository extends JpaRepository<SecretaryEntity, UUID> {

}