package br.com.meusintoma.modules.doctor.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.doctor.dto.DoctorResponseDTO;
import br.com.meusintoma.modules.doctor.services.DoctorService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/doctor")
@RequiredArgsConstructor

public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping("/{doctorId}")
    public ResponseEntity<Object> getDoctorInfo(@PathVariable UUID doctorId) {
        try {
            DoctorResponseDTO doctor = doctorService.getDoctorInfo(doctorId);
            return ResponseEntity.ok().body(doctor);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Não foi possível carregar as informações desse médico");
        }
    }
}
