package br.com.meusintoma.modules.chronicdisease.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.exceptions.globalCustomException.ForbiddenException;
import br.com.meusintoma.modules.chronicdisease.dto.DiseaseTypeRequestDTO;
import br.com.meusintoma.modules.chronicdisease.dto.DiseaseTypeResponseDTO;
import br.com.meusintoma.modules.chronicdisease.services.DiseaseTypeService;

@RestController
@RequestMapping("/disease-type")
public class DiseaseTypeController {

    @Autowired
    DiseaseTypeService diseaseTypeService;

    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Object> createDiseaseType(@RequestBody DiseaseTypeRequestDTO diseaseTypeRequest) {
        try {
            DiseaseTypeResponseDTO disease = diseaseTypeService.createDiseaseType(diseaseTypeRequest);
            return ResponseEntity.status(201).body(disease);
        } catch (ForbiddenException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Houve um erro na criação do tipo de doença");
        }
    }
}
