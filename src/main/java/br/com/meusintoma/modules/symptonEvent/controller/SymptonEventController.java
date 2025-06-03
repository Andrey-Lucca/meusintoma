package br.com.meusintoma.modules.symptonEvent.controller;

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

import br.com.meusintoma.modules.patient.repository.PatientRepository;
import br.com.meusintoma.modules.symptonEvent.dto.SymptonEventRequestDTO;
import br.com.meusintoma.modules.symptonEvent.mapper.SymptonEventMapper;
import br.com.meusintoma.modules.symptonEvent.services.SymptonService;
import br.com.meusintoma.security.utils.AuthUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/symptoms")
@RequiredArgsConstructor

public class SymptonEventController {

    private final SymptonService symptonService;

    private final PatientRepository patientRepository;

    @PostMapping("/create")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Object> create(@RequestBody SymptonEventRequestDTO symptonEventDTO,
            HttpServletRequest request) {
        UUID patientId = AuthUtils.getAuthenticatedUserId(request);
        var patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        var symptonEventEntity = SymptonEventMapper.toEntity(symptonEventDTO, patient);
        var response = this.symptonService.createSymptomEvent(symptonEventEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Object> getAllSymptons(HttpServletRequest request) {
        UUID patientId = AuthUtils.getAuthenticatedUserId(request);
        var response = this.symptonService.getPatientSympton(patientId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Object> getSymptonByName(@RequestParam String symptonName, HttpServletRequest request) {
        UUID patientId = AuthUtils.getAuthenticatedUserId(request);
        var response = this.symptonService.getPatientSymptomsBySymptonName(patientId, symptonName);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{symptonId}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Object> updateSympton(
            @PathVariable UUID symptonId,
            @RequestBody SymptonEventRequestDTO symptonEventDTO,
            HttpServletRequest request) {
        UUID patientId = AuthUtils.getAuthenticatedUserId(request);
        var patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        var symptonEventEntity = SymptonEventMapper.toEntity(symptonEventDTO, patient);

        var response = this.symptonService.updatePatientSympton(symptonId, symptonEventEntity);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Object> deleteSympton(@RequestParam String symptonId, HttpServletRequest request) {
        var patientIdObj = request.getAttribute("user_id");
        if (patientIdObj == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
        }
        UUID sympton = UUID.fromString(symptonId.toString());
        var response = this.symptonService.deleteSympton(sympton);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}