package br.com.meusintoma.modules.email.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.email.dto.EmailResponseDTO;
import br.com.meusintoma.modules.email.entity.EmailConfirmationTokenEntity;
import br.com.meusintoma.modules.email.repository.EmailConfirmationRepository;
import br.com.meusintoma.modules.user.entity.UserEntity;
import br.com.meusintoma.modules.user.services.UserConfirmService;
import br.com.meusintoma.utils.RepositoryUtils;
import br.com.meusintoma.utils.SystemClockUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailConfirmationRepository emailConfirmationRepository;

    @Autowired
    private UserConfirmService userConfirmService;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${app.test.url}")
    private String url;

    public void sendConfirmationEmail(String to, String subject, String confirmationUrl) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(username);
            helper.setTo(to);
            helper.setSubject(subject);

            String htmlContent = """
                    <html>
                    <body style="font-family: Arial, sans-serif; color: #333;">
                        <h2 style="color: #2E86C1;">Confirmação de Cadastro</h2>
                        <p>Olá,</p>
                        <p>Obrigado por se cadastrar! Clique no botão abaixo para confirmar seu cadastro:</p>
                        <a href="%s" style="
                            background-color: #2E86C1;
                            color: white;
                            padding: 10px 20px;
                            text-decoration: none;
                            border-radius: 5px;
                            display: inline-block;
                            margin-top: 10px;
                        ">Confirmar Cadastro</a>
                        <p style="margin-top: 20px;">Ou copie e cole o link no navegador:</p>
                        <p><a href="%s">%s</a></p>
                    </body>
                    </html>
                    """.formatted(confirmationUrl, confirmationUrl, confirmationUrl);

            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao enviar e-mail de confirmação");
        }
    }

    @Transactional
    public void generateAndSendConfirmation(UserEntity user) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(1);

        EmailConfirmationTokenEntity email = createEmailConfirmationToken(token, expiresAt, user);

        Optional<EmailConfirmationTokenEntity> existingToken = emailConfirmationRepository
                .findByToken(email.getToken());
        if (existingToken.isPresent()) {
            System.out.println("Token já existe!");
            return;
        }

        System.out.println("ID" + email.getUser().getId());
        emailConfirmationRepository.save(email);
        String confirmationUrl = url + "/email/confirm?token=" + email.getToken();
        String subject = "Confirmação de cadastro - APP Meu Sintoma";
        String body = "Clique no link para confirmar seu cadastro: " + confirmationUrl;

        sendConfirmationEmail(user.getEmail(), subject, body);
    }

    public EmailResponseDTO confirmEmail(String token) {
        EmailConfirmationTokenEntity email = RepositoryUtils.findOrThrow(emailConfirmationRepository.findByToken(token),
                () -> new NotFoundException("E-mail"));

        userConfirmService.confirmUser(email.getUser());
        email.setConfirmedAt(SystemClockUtils.getCurrentDateTime());

        EmailResponseDTO emailResponse = EmailResponseDTO(email);

        return emailResponse;
    }

    public void resendEmail(String token) {
        EmailConfirmationTokenEntity email = RepositoryUtils.findOrThrow(emailConfirmationRepository.findByToken(token),
                () -> new NotFoundException("Email"));
        String confirmationUrl = url + "/email/confirm?token=" + email.getToken();
        String subject = "Confirmação de cadastro - APP Meu Sintoma";
        String body = "Clique no link para confirmar seu cadastro: " + confirmationUrl;
        sendConfirmationEmail(email.getUser().getEmail(), subject, body);

    }

    private EmailConfirmationTokenEntity createEmailConfirmationToken(String token, LocalDateTime expiresAt,
            UserEntity user) {
        return EmailConfirmationTokenEntity.builder()
                .token(token)
                .expirationDate(expiresAt)
                .user(user)
                .confirmedAt(null)
                .build();
    }

    private EmailResponseDTO EmailResponseDTO(EmailConfirmationTokenEntity email) {
        return EmailResponseDTO.builder()
                .confirmed(true)
                .confirmedAt(email.getConfirmedAt())
                .email(email.getUser().getEmail())
                .build();
    }

}
