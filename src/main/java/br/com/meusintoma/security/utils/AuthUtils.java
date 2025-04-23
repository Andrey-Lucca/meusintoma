package br.com.meusintoma.security.utils;

import java.util.UUID;

import br.com.meusintoma.exceptions.globalCustomException.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;

public class AuthUtils {
    public static UUID getAuthenticatedUserId(HttpServletRequest request) {
        var patientId = request.getAttribute("user_id");
        if (patientId == null) {
            throw new UnauthorizedException();
        }
        return UUID.fromString(patientId.toString());
    }
}
