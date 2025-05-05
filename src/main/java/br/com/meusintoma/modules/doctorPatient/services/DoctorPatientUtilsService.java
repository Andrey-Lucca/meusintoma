package br.com.meusintoma.modules.doctorPatient.services;

import java.util.ArrayList;
import java.util.List;

import br.com.meusintoma.modules.doctorPatient.enums.DoctorPatientStatus;
import br.com.meusintoma.modules.doctorPatient.exceptions.DoctorPatientNotValidStatusException;

public class DoctorPatientUtilsService {

    public static void checkPatientStatus(DoctorPatientStatus status) {
        List<DoctorPatientStatus> statuses = new ArrayList<>(List.of(DoctorPatientStatus.ACCEPTED, DoctorPatientStatus.REJECTED, DoctorPatientStatus.DISASSOCIATE));
        if (!statuses.contains(status)) {
            throw new DoctorPatientNotValidStatusException("Uso indevido dos estados");
        }
    }

    public static void checkDoctorStatus(DoctorPatientStatus status) {
        List<DoctorPatientStatus> statuses = new ArrayList<>(List.of(DoctorPatientStatus.DISASSOCIATE, DoctorPatientStatus.RECONCILE));
        if (!statuses.contains(status)) {
            throw new DoctorPatientNotValidStatusException("Uso indevido dos estados");
        } 
    }
}
