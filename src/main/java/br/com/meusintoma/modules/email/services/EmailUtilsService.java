package br.com.meusintoma.modules.email.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.ForbiddenException;
import br.com.meusintoma.modules.email.dto.EmailResponseDTO;
import br.com.meusintoma.modules.email.entity.EmailConfirmationTokenEntity;
import br.com.meusintoma.modules.user.entity.UserEntity;
import br.com.meusintoma.utils.helpers.SystemClockUtils;

@Service
public class EmailUtilsService {

    public static EmailConfirmationTokenEntity createEmailConfirmationToken(String token, LocalDateTime expiresAt,
            UserEntity user) {
        return EmailConfirmationTokenEntity.builder()
                .token(token)
                .expirationDate(expiresAt)
                .user(user)
                .confirmedAt(null)
                .build();
    }

    public static EmailResponseDTO EmailResponseDTO(EmailConfirmationTokenEntity email) {
        return EmailResponseDTO.builder()
                .confirmed(true)
                .confirmedAt(email.getConfirmedAt())
                .email(email.getUser().getEmail())
                .build();
    }

    public static String buildEmailHtml(String title, String message, String buttonText, String actionUrl) {
        return """
                <html>
                <body style="font-family: Arial, sans-serif; color: #333;">
                    <h2 style="color: #2E86C1;">%s</h2>
                    <p>Olá,</p>
                    <p>%s</p>
                    <a href="%s" style="
                        background-color: #2E86C1;
                        color: white;
                        padding: 10px 20px;
                        text-decoration: none;
                        border-radius: 5px;
                        display: inline-block;
                        margin-top: 10px;
                    ">%s</a>
                    <p style="margin-top: 20px;">Ou copie e cole o link no navegador:</p>
                    <p><a href="%s">%s</a></p>
                </body>
                </html>
                """.formatted(title, message, actionUrl, buttonText, actionUrl, actionUrl);
    }

    public static void checkIsValidConfirmation(EmailConfirmationTokenEntity email) {
        if (email.getConfirmedAt() != null)
            throw new ForbiddenException("O e-mail já foi confirmado");

        if (SystemClockUtils.getCurrentDateTime().isAfter(email.getExpirationDate()))
            throw new ForbiddenException("Confirmação de e-mail expirada! Solicite uma nova");

    }
}
