package br.com.meusintoma.modules.symptomEvent.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.meusintoma.utils.common.Severity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SymptomEventResponseDTO {
    private UUID id;
    private String symptomName;
    private LocalDateTime startedAt;
    private Severity severity;
    private LocalDateTime updatedAt;
}
