package br.com.meusintoma.modules.symptomEvent.dto;

import br.com.meusintoma.utils.common.Severity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SymptomEventRequestDTO {
    String symptomName;
    Severity severity;
}
