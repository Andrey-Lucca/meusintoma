package br.com.meusintoma.modules.healthPlan.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.meusintoma.modules.healthPlan.entity.HealthPlanEntity;

public interface HealthPlanRepository extends JpaRepository<HealthPlanEntity, UUID> {
    
    public Optional<HealthPlanEntity> findPlanByName(String name);
}
