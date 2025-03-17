package br.com.meusintoma.modules.symptonEvent.entity;

import java.util.UUID;

import br.com.meusintoma.modules.patient.entity.PatientEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "sympton")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SymptonEventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private PatientEntity patient;
    
    @Enumerated(EnumType.STRING)
    private Severity severity;
}
