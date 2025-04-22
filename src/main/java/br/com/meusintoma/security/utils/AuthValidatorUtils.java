package br.com.meusintoma.security.utils;

import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthValidatorUtils {

    public static UUID getAuthenticatedUserId() {
        var attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Object userIdAttr = request.getAttribute("user_id");
        if (userIdAttr == null) {
            throw new IllegalStateException("Usuário não autenticado.");
        }

        return UUID.fromString(userIdAttr.toString());
    }

    public static String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElseThrow(() -> new AccessDeniedException("Nenhuma role definida"));
        return userRole.replace("ROLE_", "");
    }
}
