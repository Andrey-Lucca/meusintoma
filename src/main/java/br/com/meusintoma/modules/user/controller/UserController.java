package br.com.meusintoma.modules.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.meusintoma.modules.user.dto.CreateUserDTO;
import br.com.meusintoma.modules.user.entity.UserEntity;
import br.com.meusintoma.modules.user.exceptions.UserAlreadyRegistered;
import br.com.meusintoma.modules.user.services.CreateUserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor

public class UserController {

    private final CreateUserService createUserService;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody CreateUserDTO userDTO) {
        try {
            UserEntity user = this.createUserService.execute(userDTO);
           
            return ResponseEntity.ok().body(user);
        } catch (UserAlreadyRegistered e) {
            throw e;
        } catch (Exception error) {
            error.printStackTrace();
            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }

}
