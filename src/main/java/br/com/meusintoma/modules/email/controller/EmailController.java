package br.com.meusintoma.modules.email.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.modules.email.dto.EmailResponseDTO;
import br.com.meusintoma.modules.email.services.EmailService;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    EmailService emailService;

    @GetMapping("/confirm")
    public ResponseEntity<Object> confirmEmail(@RequestParam String token) {
        try {
            EmailResponseDTO email = emailService.confirmEmail(token);
            return ResponseEntity.ok().body(email);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Não foi possível confirmar o e-mail");
        }
    }

    @PostMapping("/send-confirmation/{token}")
    public ResponseEntity<?> resendEmail(@PathVariable String token) {
        try {
            emailService.resendEmail(token);
            return ResponseEntity.ok("Email enviado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Não foi possível confirmar o e-mail");
        }
    }
}
