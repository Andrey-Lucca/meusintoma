package br.com.meusintoma.modules.symptonEvent.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.meusintoma.modules.symptonEvent.enums.Severity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SymptonEventResponseDTO {
    private UUID id;
    private String symptonName;
    private LocalDateTime startedAt;
    private Severity severity;
    private LocalDateTime updatedAt;
}
