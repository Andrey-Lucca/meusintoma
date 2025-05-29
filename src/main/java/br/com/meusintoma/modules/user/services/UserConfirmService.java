package br.com.meusintoma.modules.user.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.meusintoma.modules.user.entity.UserEntity;
import br.com.meusintoma.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserConfirmService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    
    public void confirmUser(UserEntity user){
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void setNewPassword(UserEntity user, String newPassword){
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
