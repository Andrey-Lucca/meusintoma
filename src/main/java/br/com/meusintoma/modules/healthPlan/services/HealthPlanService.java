package br.com.meusintoma.modules.healthPlan.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.healthPlan.entity.HealthPlanEntity;
import br.com.meusintoma.modules.healthPlan.repository.HealthPlanRepository;
import br.com.meusintoma.utils.helpers.RepositoryUtils;

@Service
public class HealthPlanService {

    @Autowired
    HealthPlanRepository healthPlanRepository;

    public HealthPlanEntity findPlanByName(String name) {
        return RepositoryUtils.findOrThrow(healthPlanRepository.findPlanByName(name),
                () -> new NotFoundException("Plano"));
    }
}
