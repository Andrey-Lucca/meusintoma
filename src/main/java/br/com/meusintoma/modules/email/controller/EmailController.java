package br.com.meusintoma.modules.email.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.modules.email.dto.EmailResponseDTO;
import br.com.meusintoma.modules.email.dto.PasswordEmailResetRequestDTO;
import br.com.meusintoma.modules.email.dto.ResendEmailDTO;
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
    public ResponseEntity<EmailResponseDTO> confirmEmail(@RequestParam String token) {
        EmailResponseDTO email = emailService.confirmEmail(token);
        return ResponseEntity.ok().body(email);
    }

    @PostMapping("/resend-confirmation")
    public ResponseEntity<Void> resendEmail(@RequestBody ResendEmailDTO resendDTO) {
        emailService.resendEmail(resendDTO.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("send-reset-password")
    public ResponseEntity<Void> sendEmailResetPassword(@RequestBody PasswordEmailResetRequestDTO requestDTO) {
        resetPasswordService.sendEmailResetPassword(requestDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody PasswordEmailResetRequestDTO requestDTO,
            @RequestParam String token) {
        resetPasswordService.resetPassword(requestDTO, token);
        return ResponseEntity.ok().build();
    }
}
