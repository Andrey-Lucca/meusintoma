package br.com.meusintoma.modules.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.modules.user.entity.UserEntity;
import br.com.meusintoma.modules.user.repository.UserRepository;

@Service
public class UserConfirmService {

    @Autowired
    UserRepository userRepository;
    
    public void confirmUser(UserEntity user){
        user.setEnabled(true);
        userRepository.save(user);
    }
}
