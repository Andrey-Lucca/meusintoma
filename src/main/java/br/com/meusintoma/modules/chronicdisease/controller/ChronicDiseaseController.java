package br.com.meusintoma.modules.chronicdisease.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.modules.chronicdisease.dto.ChronicDiseaseRequestDTO;
import br.com.meusintoma.modules.chronicdisease.dto.ChronicDiseaseResponseDTO;
import br.com.meusintoma.modules.chronicdisease.services.ChronicDiseaseService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chronic-disease")
public class ChronicDiseaseController {

    private final ChronicDiseaseService chronicDiseaseService;

    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Object> createDiseases(@RequestBody ChronicDiseaseRequestDTO requestDTO) {
        List<ChronicDiseaseResponseDTO> chronicalDiseases = chronicDiseaseService
                .createPatientChronicDiseases(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(chronicalDiseases);

    }
}
