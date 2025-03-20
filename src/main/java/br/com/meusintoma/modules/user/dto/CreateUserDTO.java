package br.com.meusintoma.modules.user.dto;

import br.com.meusintoma.modules.user.entity.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDTO {
    private String name;
    private String email;
    private String password;
    private UserType userType;

    private String crm;            
    private String specialization; 
    private String phoneNumber;
}
