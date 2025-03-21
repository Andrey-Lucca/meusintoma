package br.com.meusintoma.modules.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AuthUserResponseDTO {
    private String acess_token;
    private Long expires_in;
}
