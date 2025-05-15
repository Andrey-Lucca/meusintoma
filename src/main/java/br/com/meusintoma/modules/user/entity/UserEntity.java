package br.com.meusintoma.modules.user.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.locationtech.jts.geom.Point;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.meusintoma.modules.email.entity.EmailConfirmationTokenEntity;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity(name = "users")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;

    @Column(columnDefinition = "geography(Point,4326)")
    @JsonIgnore
    private Point location;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EmailConfirmationTokenEntity> emailConfirmationTokens = new ArrayList<>();

    @Column(nullable = true)
    @Builder.Default
    private boolean enabled = false;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
