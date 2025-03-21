package br.com.meusintoma.modules.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.modules.user.dto.AuthUserRequestDTO;
import br.com.meusintoma.modules.user.services.AuthUserService;

@RestController
@RequestMapping("/user")
public class AuthUserController {

    @Autowired
    private AuthUserService authUserService;

    @PostMapping("/auth")
    public ResponseEntity<Object> authenticate(@RequestBody AuthUserRequestDTO authUserRequestDTO) {
        try {
            var result = this.authUserService.execute(authUserRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}
