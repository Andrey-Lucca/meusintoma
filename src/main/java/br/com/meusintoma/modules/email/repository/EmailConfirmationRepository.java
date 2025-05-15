package br.com.meusintoma.modules.email.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.meusintoma.modules.email.entity.EmailConfirmationTokenEntity;

public interface EmailConfirmationRepository extends JpaRepository<EmailConfirmationTokenEntity, UUID> {

    @EntityGraph(attributePaths = "user")
    Optional<EmailConfirmationTokenEntity> findByToken(String token);
}
