package br.com.meusintoma.modules.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthUserRequestDTO {
    private String email;
    private String password;
}
