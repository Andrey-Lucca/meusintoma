package br.com.meusintoma.modules.consultation.dto;

import br.com.meusintoma.modules.consultation.enums.ConsultationStatus;
import lombok.Data;

@Data
public class ChangeConsultationStatusDTO {
        private ConsultationStatus status;

}
