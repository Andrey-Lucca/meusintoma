package br.com.meusintoma.modules.doctor.services;

import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.modules.doctor.dto.DoctorResponseDTO;
import br.com.meusintoma.modules.doctor.entity.DoctorEntity;
import br.com.meusintoma.modules.doctor.mapper.DoctorMapper;
import br.com.meusintoma.modules.doctor.repository.DoctorRepository;
import br.com.meusintoma.modules.user.dto.LocationDTO;

@Service
public class DoctorLocationService {

    @Autowired
    DoctorRepository doctorRepository;

    public Point createPoint(LocationDTO locationDTO) {
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coordinate = new Coordinate(locationDTO.getLongitude(), locationDTO.getLatitude());
        return geometryFactory.createPoint(coordinate);
    }

    public List<DoctorResponseDTO> getNearbyDoctors(LocationDTO patientLocation, Double distance,
            String specialization) {
        Point userLocation = createPoint(patientLocation);
        List<DoctorEntity> doctors = doctorRepository.getNearbyDoctors(userLocation, distance,
                specialization);
        return doctors.stream().map(DoctorMapper::toDoctorResponseDTO).toList();
    }
}
