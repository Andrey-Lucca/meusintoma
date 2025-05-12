package br.com.meusintoma.modules.user.dto;

import br.com.meusintoma.modules.user.entity.UserType;
import lombok.Data;

@Data
public class UserResponseDTO {
    private String name;
    private String email;
    private UserType userType;
    private LocationDTO location;
}
