package br.com.meusintoma.modules.chronicdisease.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.ForbiddenException;
import br.com.meusintoma.modules.chronicdisease.dto.DiseaseTypeRequestDTO;
import br.com.meusintoma.modules.chronicdisease.dto.DiseaseTypeResponseDTO;
import br.com.meusintoma.modules.chronicdisease.entity.DiseaseTypeEntity;
import br.com.meusintoma.modules.chronicdisease.enums.ApprovalStatus;
import br.com.meusintoma.modules.chronicdisease.mapper.DiseaseTypeMapper;
import br.com.meusintoma.modules.chronicdisease.repository.DiseaseTypeRepository;
import br.com.meusintoma.security.utils.AuthValidatorUtils;

@Service
public class DiseaseTypeService {

    @Autowired
    DiseaseTypeRepository diseaseTypeRepository;

    private void checkPatientDiseaseRequestsCount(UUID patientId) {
        boolean isMoreThanTenRequest = diseaseTypeRepository.isPatientMoreThanTenRequestPending(patientId, ApprovalStatus.PENDING);
        if (isMoreThanTenRequest) {
            throw new ForbiddenException("Já existem muitas solicitações pendentes, tente novamente mais tardes");
        }
    }

    public DiseaseTypeResponseDTO createDiseaseType(DiseaseTypeRequestDTO diseaseTypeRequest) {
        UUID patientId = AuthValidatorUtils.getAuthenticatedUserId();

        checkPatientDiseaseRequestsCount(patientId);

        System.out.println(diseaseTypeRequest);

        DiseaseTypeEntity diseaseType = DiseaseTypeEntity.builder().name(diseaseTypeRequest.getDiseaseName())
                .description(diseaseTypeRequest.getDescription()).patientId(patientId).build();

        diseaseTypeRepository.save(diseaseType);

        return DiseaseTypeMapper.toDiseaseTypeResponse(diseaseType);
    }
}
