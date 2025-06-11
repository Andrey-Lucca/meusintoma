package br.com.meusintoma.modules.symptomEvent.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.modules.symptomEvent.dto.SymptomEventRequestDTO;
import br.com.meusintoma.modules.symptomEvent.dto.SymptomEventResponseDTO;
import br.com.meusintoma.modules.symptomEvent.services.SymptomService;
import lombok.RequiredArgsConstructor;

@RestController
@PreAuthorize("hasRole('PATIENT')")
@RequestMapping("/symptoms")
@RequiredArgsConstructor
public class SymptomEventController {

    private final SymptomService symptomService;

    @PostMapping("/create")
    public ResponseEntity<SymptomEventResponseDTO> create(@RequestBody SymptomEventRequestDTO symptomEventDTO) {
        SymptomEventResponseDTO symptom = symptomService.createSymptomEvent(symptomEventDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(symptom);
    }

    @GetMapping
    public ResponseEntity<List<SymptomEventResponseDTO>> getAllSymptoms() {
        List<SymptomEventResponseDTO> symptoms = symptomService.getPatientSymptom();
        return ResponseEntity.status(HttpStatus.OK).body(symptoms);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SymptomEventResponseDTO>> getSymptomByName(@RequestParam String symptomName) {
        List<SymptomEventResponseDTO> symptoms = symptomService.getPatientSymptomsBySymptomName(symptomName);
        return ResponseEntity.status(HttpStatus.OK).body(symptoms);
    }

    @PutMapping("/{symptomId}")
    public ResponseEntity<SymptomEventResponseDTO> updateSymptom(
            @PathVariable UUID symptomId,
            @RequestBody SymptomEventRequestDTO symptomEventDTO) {
        SymptomEventResponseDTO updatedSymptom = symptomService.updatePatientSymptom(symptomId, symptomEventDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedSymptom);
    }

    @DeleteMapping
    public ResponseEntity<SymptomEventResponseDTO> deleteSymptom(@RequestParam UUID symptomId) {
        SymptomEventResponseDTO response = this.symptomService.deleteSymptom(symptomId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}