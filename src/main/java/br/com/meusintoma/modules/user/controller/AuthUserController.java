package br.com.meusintoma.modules.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.exceptions.globalCustomException.CustomAccessDeniedException;
import br.com.meusintoma.modules.user.dto.AuthUserRequestDTO;
import br.com.meusintoma.modules.user.exceptions.UserAuthException;
import br.com.meusintoma.modules.user.services.AuthUserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthUserController {

    private final AuthUserService authUserService;

    @PostMapping("/auth")
    public ResponseEntity<Object> authenticate(@RequestBody AuthUserRequestDTO authUserRequestDTO) {
        try {
            var result = this.authUserService.execute(authUserRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (UserAuthException e) {
            throw e;
        } catch (CustomAccessDeniedException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}
