package br.com.meusintoma.modules.symptonEvent.dto;

import br.com.meusintoma.modules.symptonEvent.enums.Severity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SymptonEventRequestDTO {
    String symptonName;
    Severity severity;
}
