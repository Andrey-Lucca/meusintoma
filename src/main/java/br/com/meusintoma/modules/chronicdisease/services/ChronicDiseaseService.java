package br.com.meusintoma.modules.chronicdisease.services;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.BadRequestException;
import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.chronicdisease.dto.ChronicDiseaseRequestDTO;
import br.com.meusintoma.modules.chronicdisease.dto.ChronicDiseaseResponseDTO;
import br.com.meusintoma.modules.chronicdisease.entity.ChronicDiseaseEntity;
import br.com.meusintoma.modules.chronicdisease.entity.DiseaseTypeEntity;
import br.com.meusintoma.modules.chronicdisease.enums.ApprovalStatus;
import br.com.meusintoma.modules.chronicdisease.mapper.ChronicDiseaseMapper;
import br.com.meusintoma.modules.chronicdisease.repository.ChronicDiseaseRepository;
import br.com.meusintoma.modules.chronicdisease.repository.DiseaseTypeRepository;
import br.com.meusintoma.modules.patient.entity.PatientEntity;
import br.com.meusintoma.modules.patient.services.PatientService;
import br.com.meusintoma.security.utils.AuthValidatorUtils;
import br.com.meusintoma.utils.helpers.GenericUtils;
import br.com.meusintoma.utils.helpers.RepositoryUtils;
import jakarta.transaction.Transactional;

@Service
public class ChronicDiseaseService {

    @Autowired
    DiseaseTypeRepository diseaseTypeRepository;

    @Autowired
    PatientService patientService;

    @Autowired
    ChronicDiseaseRepository chronicDiseaseRepository;

    private Set<ChronicDiseaseEntity> getChronicDisease(PatientEntity patient, ChronicDiseaseRequestDTO dto) {
        Set<ChronicDiseaseEntity> chronicDiseases = dto.getChronicDiseases().stream().map(diseaseInput -> {
            DiseaseTypeEntity diseaseType = RepositoryUtils.findOrThrow(
                    diseaseTypeRepository.findById(diseaseInput.getDiseaseTypeId()),
                    () -> new NotFoundException("Tipo de doença não encontrado"));

            if (!diseaseType.getApprovalStatus().equals(ApprovalStatus.PROD)) {
                throw new BadRequestException();
            }
            return ChronicDiseaseEntity.builder()
                    .patient(patient)
                    .diseaseType(diseaseType)
                    .yearsQuantity(diseaseInput.getYearsQuantity())
                    .severity(diseaseInput.getSeverity())
                    .build();
        }).collect(Collectors.toSet());
        return chronicDiseases;
    }

    @Transactional
    public List<ChronicDiseaseResponseDTO> createPatientChronicDiseases(ChronicDiseaseRequestDTO dto) {
        UUID patientId = AuthValidatorUtils.getAuthenticatedUserId();
        GenericUtils.compareId(patientId, dto.getPatientId());

        PatientEntity patient = patientService.findPatient(patientId);

        Set<ChronicDiseaseEntity> chronicDiseases = getChronicDisease(patient, dto);
        chronicDiseaseRepository.saveAll(chronicDiseases);
        return ChronicDiseaseMapper.toChronicDiseaseResponseDTO(chronicDiseases);
    }

}
