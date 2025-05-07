package br.com.meusintoma.modules.doctorPatient.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.exceptions.globalCustomException.CustomAccessDeniedException;
import br.com.meusintoma.exceptions.globalCustomException.ForbiddenException;
import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.doctorPatient.dto.DoctorPatientNoteDTO;
import br.com.meusintoma.modules.doctorPatient.dto.DoctorPatientNoteRequestDTO;
import br.com.meusintoma.modules.doctorPatient.services.DoctorPatientNotesService;

@RestController
@RequestMapping("/notes")
public class DoctorPatientNoteController {

    @Autowired
    DoctorPatientNotesService doctorPatientNotesService;

    @PostMapping("relationship/{relationshipId}/consultation/{consultationId}")
    @PreAuthorize("hasRole('DOCTOR') || hasRole('SECRETARY')")
    public ResponseEntity<Object> createNote(@PathVariable UUID relationshipId, @PathVariable UUID consultationId,
            @RequestBody DoctorPatientNoteRequestDTO requestDTO) {
        try {
            DoctorPatientNoteDTO responseNotes = doctorPatientNotesService.createNotes(relationshipId, consultationId,
                    requestDTO);
            return ResponseEntity.ok().body(responseNotes);
        } catch (DataIntegrityViolationException e) {
            throw new ForbiddenException("Já existe nota cadastrada para esta consulta.");
        } catch (NotFoundException e) {
            throw e;
        } catch (ForbiddenException e) {
            throw e;
        } catch (CustomAccessDeniedException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("It was not possible create notes");
        }
    }
}
