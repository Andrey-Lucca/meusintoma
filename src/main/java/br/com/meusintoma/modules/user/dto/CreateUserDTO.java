package br.com.meusintoma.modules.user.dto;



import br.com.meusintoma.modules.doctor.enums.DoctorSpecialization;
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
    private Double latitude;
    private Double longitude;
    private String crm;            
    private DoctorSpecialization specialization; 
    private String phoneNumber;
}
