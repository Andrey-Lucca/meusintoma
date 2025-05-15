package br.com.meusintoma.modules.email.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailResponseDTO {

    private String email;
    private boolean confirmed;
    private LocalDateTime confirmedAt;

}
