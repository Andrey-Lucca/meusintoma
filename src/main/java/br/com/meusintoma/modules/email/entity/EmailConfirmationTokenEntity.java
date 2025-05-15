package br.com.meusintoma.modules.email.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.meusintoma.modules.user.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "EmailConfirmationToken")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailConfirmationTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String token; 

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private LocalDateTime expirationDate;

    private LocalDateTime confirmedAt;
}
