package br.com.meusintoma.modules.user.controller;

import javax.naming.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.modules.user.dto.AuthUserRequestDTO;
import br.com.meusintoma.modules.user.services.AuthUserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthUserController {

    private final AuthUserService authUserService;

    @PostMapping("/auth")
    public ResponseEntity<Object> authenticate(@RequestBody AuthUserRequestDTO authUserRequestDTO)
            throws AuthenticationException {
        var result = authUserService.execute(authUserRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
