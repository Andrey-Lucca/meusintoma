package br.com.meusintoma.modules.user.dto;


import lombok.Data;

@Data
public class UserNearbyLocationDTO {
    private LocationDTO location;
    private String doctorSpecialization;
    private Double distance;
}
