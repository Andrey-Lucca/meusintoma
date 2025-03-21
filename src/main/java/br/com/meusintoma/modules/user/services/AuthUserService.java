package br.com.meusintoma.modules.user.services;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.meusintoma.exceptions.UserNotFoundException;
import br.com.meusintoma.modules.user.dto.AuthUserRequestDTO;
import br.com.meusintoma.modules.user.dto.AuthUserResponseDTO;
import br.com.meusintoma.modules.user.repository.UserRepository;

@Service
public class AuthUserService {
    @Value("${security.token.secret.user}")
    private String secretKey;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthUserResponseDTO execute(AuthUserRequestDTO userRequestDTO) throws AuthenticationException {
        var user = userRepository.findByEmail(userRequestDTO.getEmail()).orElseThrow(() -> {
            throw new UserNotFoundException();
        });

        var passwordMatches = passwordEncoder.matches(userRequestDTO.getPassword(), user.getPassword());
        if (!passwordMatches) {
            throw new AuthenticationException();
        }
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        var expires_in = Instant.now().plus(Duration.ofDays(30));
        var token = JWT.create().withIssuer("meusintoma").withExpiresAt(expires_in)
                .withSubject(user.getId().toString()).withClaim("roles", Arrays.asList(user.getUserType().toString()))
                .sign(algorithm);
        AuthUserResponseDTO authUserResponseDTO = AuthUserResponseDTO.builder().acess_token(token)
                .expires_in(expires_in.toEpochMilli()).build();
        return authUserResponseDTO;
    }
}
