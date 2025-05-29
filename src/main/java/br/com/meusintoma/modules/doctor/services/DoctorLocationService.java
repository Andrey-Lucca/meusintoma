package br.com.meusintoma.modules.doctor.services;

import java.util.List;
import java.util.Optional;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.BadRequestException;
import br.com.meusintoma.exceptions.globalCustomException.NoContentException;
import br.com.meusintoma.modules.doctor.dto.DoctorResponseDTO;
import br.com.meusintoma.modules.doctor.entity.DoctorEntity;
import br.com.meusintoma.modules.doctor.mapper.DoctorMapper;
import br.com.meusintoma.modules.doctor.repository.DoctorRepository;
import br.com.meusintoma.modules.user.dto.LocationDTO;
import br.com.meusintoma.utils.helpers.GeoUtils;
import br.com.meusintoma.utils.helpers.RepositoryUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class DoctorLocationService {

    private final DoctorRepository doctorRepository;

    public List<DoctorResponseDTO> getNearbyDoctors(LocationDTO patientLocation, Double distance,
            String specialization) {
        Point userLocation = GeoUtils.createPoint(patientLocation.getLatitude(), patientLocation.getLongitude());
        List<DoctorEntity> doctors = RepositoryUtils.findOrThrow(
                Optional.ofNullable(doctorRepository.getNearbyDoctors(userLocation, distance, specialization)),
                () -> new BadRequestException());
        if (doctors.isEmpty()) {
            throw new NoContentException("Médicos próximos");
        }

        return doctors.stream().map(DoctorMapper::toDoctorResponseDTO).toList();
    }
}
