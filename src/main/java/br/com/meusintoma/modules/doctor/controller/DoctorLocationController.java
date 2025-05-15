package br.com.meusintoma.modules.doctor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.exceptions.globalCustomException.NoContentException;
import br.com.meusintoma.modules.doctor.dto.DoctorResponseDTO;
import br.com.meusintoma.modules.doctor.services.DoctorLocationService;
import br.com.meusintoma.modules.user.dto.UserNearbyLocationDTO;

@RestController
@RequestMapping("/doctor/location")
public class DoctorLocationController {

    @Autowired
    DoctorLocationService doctorLocationService;

    @GetMapping("/nearby")
    public ResponseEntity<Object> getNearbyDoctors(@RequestBody UserNearbyLocationDTO userRequest) {
        try {
            List<DoctorResponseDTO> doctors = doctorLocationService.getNearbyDoctors(userRequest.getLocation(),
                    userRequest.getDistance(), userRequest.getDoctorSpecialization());
            return ResponseEntity.ok().body(doctors);
        } catch (NoContentException e) {
            return ResponseEntity.internalServerError().body("Failed to get doctor locations");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to get doctor locations");
        }
    }
}
