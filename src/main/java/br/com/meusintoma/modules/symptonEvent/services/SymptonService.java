package br.com.meusintoma.modules.symptonEvent.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.modules.symptonEvent.dto.SymptonEventResponseDTO;
import br.com.meusintoma.modules.symptonEvent.entity.SymptonEventEntity;
import br.com.meusintoma.modules.symptonEvent.mapper.SymptonEventMapper;
import br.com.meusintoma.modules.symptonEvent.repository.SymptonEventRepository;

@Service
public class SymptonService {

    @Autowired
    SymptonEventRepository symptonEventRepository;

    public SymptonEventResponseDTO createSymptomEvent(SymptonEventEntity symptonEvent){
        var savedSymptom = this.symptonEventRepository.save(symptonEvent);

        SymptonEventResponseDTO responseDTO = SymptonEventMapper.toResponseDTO(savedSymptom);

        return responseDTO;
    }
}
