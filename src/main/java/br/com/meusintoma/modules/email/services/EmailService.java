package br.com.meusintoma.modules.email.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.email.dto.EmailResponseDTO;
import br.com.meusintoma.modules.email.entity.EmailConfirmationTokenEntity;
import br.com.meusintoma.modules.email.exception.TokenGenerationException;
import br.com.meusintoma.modules.email.repository.EmailConfirmationRepository;
import br.com.meusintoma.modules.user.entity.UserEntity;
import br.com.meusintoma.utils.CryptoUtils;
import br.com.meusintoma.utils.RepositoryUtils;
import br.com.meusintoma.utils.SystemClockUtils;
import jakarta.transaction.Transactional;

@Service
public class EmailService {

    @Autowired
    private EmailConfirmationRepository emailConfirmationRepository;

    @Autowired
    private EmailAsyncService emailAsyncService;

    @Autowired
    private CryptoUtils cryptoUtils;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${app.test.url}")
    private String url;

    @Transactional
    public void generateAndSendConfirmation(UserEntity user) {
        EmailConfirmationTokenEntity email = generateUniqueEmailToken(user);
        emailConfirmationRepository.save(email);
        emailAsyncService.sendConfirmationEmail(user.getEmail(), generateBodyAndSubject(email).get("SUBJECT"),
                generateBodyAndSubject(email).get("BODY"));
    }

    public void resendEmail(String userEmail) {
        EmailConfirmationTokenEntity email = RepositoryUtils.findOrThrow(
                emailConfirmationRepository.findByEmail(userEmail),
                () -> new NotFoundException("Email"));

        EmailUtilsService.checkIsValidConfirmation(email);

        emailAsyncService.sendConfirmationEmail(email.getUser().getEmail(),
                generateBodyAndSubject(email).get("SUBJECT"),
                generateBodyAndSubject(email).get("BODY"));
    }

    public EmailResponseDTO confirmEmail(String cryptoToken) {
        String token = cryptoUtils.decrypt(cryptoToken);
        EmailConfirmationTokenEntity email = RepositoryUtils.findOrThrow(emailConfirmationRepository.findByToken(token),
                () -> new NotFoundException("E-mail"));
        EmailUtilsService.checkIsValidConfirmation(email);
        email.getUser().setEnabled(true);
        email.setConfirmedAt(SystemClockUtils.getCurrentDateTime());
        emailConfirmationRepository.save(email);

        EmailResponseDTO emailResponse = EmailUtilsService.EmailResponseDTO(email);

        return emailResponse;
    }

    private EmailConfirmationTokenEntity generateUniqueEmailToken(UserEntity user) {
        int maxAttempts = 5;
        for (int i = 0; i < maxAttempts; i++) {
            String token = UUID.randomUUID().toString();
            boolean exists = emailConfirmationRepository.findByToken(token).isPresent();
            if (!exists) {
                LocalDateTime expiresAt = LocalDateTime.now().plusHours(1);
                return EmailUtilsService.createEmailConfirmationToken(token, expiresAt, user);
            }
        }
        throw new TokenGenerationException();
    }

    private Map<String, String> generateBodyAndSubject(EmailConfirmationTokenEntity email) {
        Map<String, String> bodySubject = new HashMap<>();
        String confirmationUrl = url + "/email/confirm?token=" + cryptoUtils.encrypt(email.getToken());
        String subject = "Confirmação de cadastro - APP Meu Sintoma";

        String body = "Clique no link para confirmar seu cadastro: " + confirmationUrl;
        bodySubject.put("SUBJECT", subject);
        bodySubject.put("BODY", body);
        return bodySubject;
    }

}
