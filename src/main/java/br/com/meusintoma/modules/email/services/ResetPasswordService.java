package br.com.meusintoma.modules.email.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.email.dto.PasswordEmailResetRequestDTO;
import br.com.meusintoma.modules.email.entity.EmailConfirmationTokenEntity;
import br.com.meusintoma.modules.email.repository.EmailConfirmationRepository;
import br.com.meusintoma.modules.user.entity.UserEntity;
import br.com.meusintoma.modules.user.services.UserConfirmService;
import br.com.meusintoma.modules.user.services.UserService;
import br.com.meusintoma.utils.helpers.CryptoUtils;
import br.com.meusintoma.utils.helpers.GenericUtils;
import br.com.meusintoma.utils.helpers.RepositoryUtils;
import br.com.meusintoma.utils.helpers.SystemClockUtils;

@Service
public class ResetPasswordService {

    @Autowired
    private EmailAsyncService emailAsyncService;

    @Autowired
    private UserService userService;

    @Autowired
    private CryptoUtils cryptoUtils;

    @Autowired
    private UserConfirmService userConfirmService;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${app.test.url}")
    private String url;

    @Autowired
    private EmailConfirmationRepository emailConfirmationRepository;

    public void sendEmailResetPassword(PasswordEmailResetRequestDTO emailResetRequestDTO) {

        UserEntity user = userService.findUserByEmail(emailResetRequestDTO.getEmail());

        String token = cryptoUtils.encrypt(user.getId().toString());
        String resetPasswordUrl = url + "/email" + "/reset-password?token=" + token;

        String to = user.getEmail();
        String subject = "Redefinir sua senha";

        String htmlBody = EmailUtilsService.buildEmailHtml(
                "Redefinição de Senha",
                "Recebemos uma solicitação para redefinir sua senha. Clique no botão abaixo para continuar:",
                "Redefinir Senha",
                resetPasswordUrl);

        LocalDateTime expiresAt = LocalDateTime.now().plusHours(1);
        EmailConfirmationTokenEntity emailConfirmation = EmailUtilsService.createEmailConfirmationToken(token,
                expiresAt, user);
        emailConfirmationRepository.save(emailConfirmation);
        emailAsyncService.sendHtmlEmail(to, subject, htmlBody);
    }

    public void resetPassword(PasswordEmailResetRequestDTO emailResetRequestDTO, String token) {
        String userId = cryptoUtils.decrypt(token);
        EmailConfirmationTokenEntity email = RepositoryUtils.findOrThrow(emailConfirmationRepository.findByToken(token),
                () -> new NotFoundException("E-mail"));
        GenericUtils.compareId(UUID.fromString(userId), email.getUser().getId());
        EmailUtilsService.checkIsValidConfirmation(email);
        email.setConfirmedAt(SystemClockUtils.getCurrentDateTime());
        userConfirmService.setNewPassword(email.getUser(), emailResetRequestDTO.getPassword());
    }
}
