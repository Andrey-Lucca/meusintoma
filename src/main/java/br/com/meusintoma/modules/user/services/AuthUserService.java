package br.com.meusintoma.modules.user.services;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.meusintoma.exceptions.globalCustomException.CustomAccessDeniedException;
import br.com.meusintoma.modules.user.dto.AuthUserRequestDTO;
import br.com.meusintoma.modules.user.dto.AuthUserResponseDTO;
import br.com.meusintoma.modules.user.entity.UserEntity;
import br.com.meusintoma.modules.user.exceptions.UserAuthException;
import br.com.meusintoma.modules.user.repository.UserRepository;
import br.com.meusintoma.utils.helpers.RepositoryUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthUserService {
    @Value("${security.token.secret.user}")
    private String secretKey;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private void checkConfirmationEmail(UserEntity user) {
        if (!user.isEnabled()) {
            throw new CustomAccessDeniedException("O seu e-mail deve ser confirmado primeiro");
        }
    }

    private void checkPassword(AuthUserRequestDTO userRequestDTO, UserEntity user) {
        boolean passwordMatches = passwordEncoder.matches(userRequestDTO.getPassword(), user.getPassword());
        if (!passwordMatches) {
            throw new UserAuthException();
        }
    }

    private AuthUserResponseDTO generateUserResponse(UserEntity user) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        var expires_in = Instant.now().plus(Duration.ofDays(30));
        var token = JWT.create().withIssuer("meusintoma").withExpiresAt(expires_in)
                .withSubject(user.getId().toString()).withClaim("roles", Arrays.asList(user.getUserType().toString()))
                .sign(algorithm);
        return AuthUserResponseDTO.builder().acess_token(token).expires_in(expires_in.toEpochMilli()).build();
    }

    public AuthUserResponseDTO execute(AuthUserRequestDTO userRequestDTO) throws AuthenticationException {
        UserEntity user = RepositoryUtils.findOrThrow(userRepository.findByEmail(userRequestDTO.getEmail()),
                () -> new UserAuthException());

        checkPassword(userRequestDTO, user);
        checkConfirmationEmail(user);
        AuthUserResponseDTO authUserResponseDTO = generateUserResponse(user);

        return authUserResponseDTO;
    }
}
