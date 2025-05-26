package br.com.meusintoma.modules.email.dto;

import lombok.Data;

@Data
public class PasswordEmailResetRequestDTO {
    private String email;
    private String password;
}
