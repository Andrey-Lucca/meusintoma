package br.com.meusintoma.modules.consultation.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.modules.consultation.dto.ConsultationByDoctorPatientDTO;
import br.com.meusintoma.modules.consultation.entity.ConsultationEntity;
import br.com.meusintoma.modules.consultation.enums.ConsultationStatus;
import br.com.meusintoma.modules.consultation.mapper.ConsultationMapper;
import br.com.meusintoma.modules.consultation.repository.ConsultationRepository;
import br.com.meusintoma.utils.GenericUtils;

@Service
public class ConsultationRelationshipService {

    @Autowired
    ConsultationRepository consultationRepository;

    public List<ConsultationByDoctorPatientDTO> getConsultationsByDoctorAndPatient(UUID doctorId, UUID patientId) {
        List<ConsultationStatus> statuses = new ArrayList<>(
                List.of(ConsultationStatus.COMPLETED, ConsultationStatus.CONFIRMED));
        List<ConsultationEntity> consultations = consultationRepository.findAllByDoctorAndPatientRelationship(doctorId,
                patientId, statuses);
        GenericUtils.checkIsEmptyList(consultations);
        List<ConsultationByDoctorPatientDTO> consultationsResponseDTO = ConsultationMapper
                .toConsultationByDoctorPatientDTO(consultations);
        return consultationsResponseDTO;
    }
}
