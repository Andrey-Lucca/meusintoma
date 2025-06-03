package br.com.meusintoma.modules.doctorPatient.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.exceptions.globalCustomException.CustomAccessDeniedException;
import br.com.meusintoma.exceptions.globalCustomException.ForbiddenException;
import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.doctorPatient.dto.DoctorPatientNoteDTO;
import br.com.meusintoma.modules.doctorPatient.dto.DoctorPatientNoteGetDTO;
import br.com.meusintoma.modules.doctorPatient.dto.DoctorPatientNoteRequestDTO;
import br.com.meusintoma.modules.doctorPatient.dto.DoctorPatientUpdateNoteDTO;
import br.com.meusintoma.modules.doctorPatient.services.DoctorPatientNotesService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor

public class DoctorPatientNoteController {

    private final DoctorPatientNotesService doctorPatientNotesService;

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
            throw e;
        }
    }

    @GetMapping("relationship/{relationshipId}")
    public ResponseEntity<Object> getNotes(@PathVariable UUID relationshipId) {
        try {
            List<DoctorPatientNoteGetDTO> notes = doctorPatientNotesService.getNotes(relationshipId);
            return ResponseEntity.ok().body(notes);
        } catch (NotFoundException e) {
            throw e;
        } catch (CustomAccessDeniedException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    @PutMapping("relationship/{relationshipId}/note/{noteId}")
    // @PreAuthorize("hasRole('DOCTOR') || hasRole('SECRETARY')")
    public ResponseEntity<Object> changeNotes(@PathVariable UUID relationshipId, @PathVariable UUID noteId,
            @RequestBody DoctorPatientUpdateNoteDTO notes) {
        try {
            DoctorPatientNoteGetDTO note = doctorPatientNotesService.updateNote(relationshipId, noteId,
                    notes.getNotes());
            return ResponseEntity.ok().body(note);
        } catch (ForbiddenException e) {
            throw e;
        } catch (NotFoundException e) {
            throw e;
        } catch (CustomAccessDeniedException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }
}
