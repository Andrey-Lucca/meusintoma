package br.com.meusintoma.modules.email.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.exceptions.globalCustomException.ForbiddenException;
import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.email.dto.EmailResponseDTO;
import br.com.meusintoma.modules.email.dto.PasswordEmailResetRequestDTO;
import br.com.meusintoma.modules.email.dto.ResendEmailDTO;
import br.com.meusintoma.modules.email.exception.InvalidTokenException;
import br.com.meusintoma.modules.email.exception.TokenGenerationException;
import br.com.meusintoma.modules.email.services.EmailService;
import br.com.meusintoma.modules.email.services.ResetPasswordService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    private final ResetPasswordService resetPasswordService;

    @GetMapping("/confirm")
    public ResponseEntity<Object> confirmEmail(@RequestParam String token) {
        try {
            EmailResponseDTO email = emailService.confirmEmail(token);
            return ResponseEntity.ok().body(email);
        } catch (InvalidTokenException e) {
            throw e;
        } catch (NotFoundException e) {
            throw e;
        } catch (ForbiddenException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Não foi possível confirmar o e-mail");
        }
    }

    @PostMapping("/resend-confirmation")
    public ResponseEntity<?> resendEmail(@RequestBody ResendEmailDTO resendDTO) {
        try {
            emailService.resendEmail(resendDTO.getEmail());
            return ResponseEntity.ok("Email enviado com sucesso");
        } catch (NotFoundException e) {
            throw e;
        } catch (ForbiddenException e) {
            throw e;
        } catch (TokenGenerationException e) {
            throw e;
        } catch (Exception e) {
            //e.printStackTrace();
            return ResponseEntity.internalServerError().body("Não foi possível reenviar o e-mail");
        }
    }

    @PostMapping("send-reset-password")
    public ResponseEntity<?> sendEmailResetPassword(@RequestBody PasswordEmailResetRequestDTO requestDTO) {
        try {
            resetPasswordService.sendEmailResetPassword(requestDTO);
            return ResponseEntity.ok("Email enviado com sucesso");
        } catch (NotFoundException e) {
            throw e;
        } catch (ForbiddenException e) {
            throw e;
        } catch (TokenGenerationException e) {
            throw e;
        } catch (Exception e) {
            //e.printStackTrace();
            return ResponseEntity.internalServerError().body("Não foi possível reenviar o e-mail");
        }
    }

    @PostMapping("reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordEmailResetRequestDTO requestDTO, @RequestParam String token) {
        try {
            resetPasswordService.resetPassword(requestDTO, token);
            return ResponseEntity.ok("Senha alterada");
        } catch (NotFoundException e) {
            throw e;
        } catch (ForbiddenException e) {
            throw e;
        } catch (TokenGenerationException e) {
            throw e;
        } catch (Exception e) {
            //e.printStackTrace();
            return ResponseEntity.internalServerError().body("Não foi possível reenviar o e-mail");
        }
    }
}
