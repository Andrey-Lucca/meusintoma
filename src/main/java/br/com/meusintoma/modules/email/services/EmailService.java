package br.com.meusintoma.modules.email.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.email.dto.EmailResponseDTO;
import br.com.meusintoma.modules.email.entity.EmailConfirmationTokenEntity;
import br.com.meusintoma.modules.email.repository.EmailConfirmationRepository;
import br.com.meusintoma.modules.user.entity.UserEntity;
import br.com.meusintoma.utils.helpers.CryptoUtils;
import br.com.meusintoma.utils.helpers.RepositoryUtils;
import br.com.meusintoma.utils.helpers.SystemClockUtils;
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
        String rawToken = UUID.randomUUID().toString(); 
        String cryptoToken = cryptoUtils.encrypt(rawToken);
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(1);

        EmailConfirmationTokenEntity emailToken = EmailUtilsService.createEmailConfirmationToken(rawToken, expiresAt,
                user);
        emailConfirmationRepository.save(emailToken);

        String confirmationUrl = url + "/email/confirm?token=" + cryptoToken;
        String subject = "Confirmação de cadastro - APP Meu Sintoma";

        String body = EmailUtilsService.buildEmailHtml(
                "Confirmação de Cadastro",
                "Clique no botão abaixo para confirmar seu e-mail.",
                "Confirmar Cadastro",
                confirmationUrl);

        emailAsyncService.sendHtmlEmail(user.getEmail(), subject, body);
    }

    public void resendEmail(String userEmail) {
        EmailConfirmationTokenEntity emailToken = RepositoryUtils.findOrThrow(
                emailConfirmationRepository.findByEmail(userEmail),
                () -> new NotFoundException("Email não encontrado"));

        EmailUtilsService.checkIsValidConfirmation(emailToken);

        String confirmationUrl = url + "/email/confirm?token=" + cryptoUtils.encrypt(emailToken.getToken());
        String subject = "Confirmação de cadastro - APP Meu Sintoma";

        String body = EmailUtilsService.buildEmailHtml(
                "Confirmação de Cadastro",
                "Clique no botão abaixo para confirmar seu e-mail.",
                "Confirmar Cadastro",
                confirmationUrl);

        emailAsyncService.sendHtmlEmail(emailToken.getUser().getEmail(), subject, body);
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
}
