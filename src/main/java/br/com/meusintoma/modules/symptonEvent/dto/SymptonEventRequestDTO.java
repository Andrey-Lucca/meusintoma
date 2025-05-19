package br.com.meusintoma.modules.symptonEvent.dto;

import br.com.meusintoma.utils.common.Severity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SymptonEventRequestDTO {
    String symptonName;
    Severity severity;
}
