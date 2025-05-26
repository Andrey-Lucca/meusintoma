package br.com.meusintoma.modules.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.user.entity.UserEntity;
import br.com.meusintoma.modules.user.repository.UserRepository;
import br.com.meusintoma.utils.helpers.RepositoryUtils;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public UserEntity findUserByEmail(String email) {
        return RepositoryUtils.findOrThrow(userRepository.findByEmail(email), () -> new NotFoundException("E-mail"));
    }
}
