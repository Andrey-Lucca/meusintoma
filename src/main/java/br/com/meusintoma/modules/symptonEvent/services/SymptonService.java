package br.com.meusintoma.modules.symptonEvent.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.modules.symptonEvent.dto.SymptonEventResponseDTO;
import br.com.meusintoma.modules.symptonEvent.entity.SymptonEventEntity;
import br.com.meusintoma.modules.symptonEvent.mapper.SymptonEventMapper;
import br.com.meusintoma.modules.symptonEvent.repository.SymptonEventRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class SymptonService {

    @Autowired
    SymptonEventRepository symptonEventRepository;

    public SymptonEventResponseDTO createSymptomEvent(SymptonEventEntity symptonEvent) {
        var savedSymptom = this.symptonEventRepository.save(symptonEvent);

        SymptonEventResponseDTO responseDTO = SymptonEventMapper.toResponseDTO(savedSymptom);

        return responseDTO;
    }

    public List<SymptonEventResponseDTO> getPatientSympton(UUID patientId) {
        var symptons = this.symptonEventRepository.findByPatientIdOrderByStartedAtDesc(patientId);

        List<SymptonEventResponseDTO> responseDTO = SymptonEventMapper.toResponseDTO(symptons);

        return responseDTO;
    }

    public List<SymptonEventResponseDTO> getPatientSymptomsBySymptonName(UUID patientId, String symptonName) {
        var symptons = this.symptonEventRepository
                .findByPatientIdAndSymptonNameContainingIgnoreCaseOrderByStartedAtDesc(patientId, symptonName);

        List<SymptonEventResponseDTO> responseDTO = SymptonEventMapper.toResponseDTO(symptons);

        return responseDTO;
    }

    public SymptonEventResponseDTO updatePatientSympton(UUID symptonId, SymptonEventEntity symptonEvent) {
        var existingSympton = this.symptonEventRepository.findById(symptonId)
                .orElseThrow(() -> new EntityNotFoundException("Sintoma não encontrado"));

        existingSympton.setSymptonName(symptonEvent.getSymptonName());
        existingSympton.setSeverity(symptonEvent.getSeverity());

        var updatedSympton = this.symptonEventRepository.save(existingSympton);

        return SymptonEventMapper.toResponseDTO(updatedSympton);
    }

    public SymptonEventResponseDTO deleteSympton(UUID symptonId) {
        var sympton = this.symptonEventRepository.findById(symptonId)
                .orElseThrow(() -> new RuntimeException("Sintoma não encontrado"));

        SymptonEventResponseDTO symptonEventResponseDTO = SymptonEventMapper.toResponseDTO(sympton);
        this.symptonEventRepository.deleteById(symptonId);
        return symptonEventResponseDTO;
    }
}
