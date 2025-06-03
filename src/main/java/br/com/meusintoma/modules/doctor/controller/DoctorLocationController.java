package br.com.meusintoma.modules.doctor.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.modules.doctor.dto.DoctorResponseDTO;
import br.com.meusintoma.modules.doctor.services.DoctorLocationService;
import br.com.meusintoma.modules.user.dto.UserNearbyLocationDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/doctor/location")
@RequiredArgsConstructor

public class DoctorLocationController {

    private final DoctorLocationService doctorLocationService;

    @GetMapping("/nearby")
    public ResponseEntity<Object> getNearbyDoctors(@RequestBody UserNearbyLocationDTO userRequest) {
        List<DoctorResponseDTO> doctors = doctorLocationService.getNearbyDoctors(userRequest.getLocation(),
                userRequest.getDistance(), userRequest.getDoctorSpecialization());
        return ResponseEntity.ok().body(doctors);
    }
}
