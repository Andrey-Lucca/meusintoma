package br.com.meusintoma.providers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class JWTUserProvider {
    @Value("${security.token.secret.user}")
    private String secretKey;

    public DecodedJWT validateToken(String token) {
        token = token.replace("Bearer ", "");
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        try {
            var decodedToken = JWT.require(algorithm).build().verify(token);
            return decodedToken;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
