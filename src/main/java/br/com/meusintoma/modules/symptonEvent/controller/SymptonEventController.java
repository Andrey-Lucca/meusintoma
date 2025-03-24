package br.com.meusintoma.modules.symptonEvent.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.modules.patient.repository.PatientRepository;
import br.com.meusintoma.modules.symptonEvent.dto.SymptonEventRequestDTO;
import br.com.meusintoma.modules.symptonEvent.mapper.SymptonEventMapper;
import br.com.meusintoma.modules.symptonEvent.services.SymptonService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/symptoms")
public class SymptonEventController {

    @Autowired
    SymptonService symptonService;

    @Autowired
    PatientRepository patientRepository;

    @PostMapping("/create")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Object> create(@RequestBody SymptonEventRequestDTO symptonEventDTO,
            HttpServletRequest request) {
        try {
            var patientIdObj = request.getAttribute("user_id");
            if (patientIdObj == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
            }

            String patientId = patientIdObj.toString();
            var patient = patientRepository.findById(UUID.fromString(patientId))
                    .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

            var symptonEventEntity = SymptonEventMapper.toEntity(symptonEventDTO, patient);
            var response = this.symptonService.createSymptomEvent(symptonEventEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não foi possível registrar o sintoma");
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Object> getAllSymptons(HttpServletRequest request){
        try {
            var patientIdObj = request.getAttribute("user_id");
            if (patientIdObj == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
            }
        
            UUID patientId = UUID.fromString(patientIdObj.toString());
            var response = this.symptonService.getPatientSympton(patientId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não foi possível obter os registros do usuário");
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Object> getSymptonByName(@RequestParam String symptonName, HttpServletRequest request){
        try {
            var patientIdObj = request.getAttribute("user_id");
            if (patientIdObj == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
            }
        
            UUID patientId = UUID.fromString(patientIdObj.toString());
            var response = this.symptonService.getPatientSymptomsBySymptonName(patientId, symptonName);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não foi possível encontrar o sintoma em específico");
        }
    }

}
