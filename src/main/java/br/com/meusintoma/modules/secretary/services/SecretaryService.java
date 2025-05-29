package br.com.meusintoma.modules.secretary.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.meusintoma.exceptions.globalCustomException.NotFoundException;
import br.com.meusintoma.modules.secretary.entity.SecretaryEntity;
import br.com.meusintoma.modules.secretary.repository.SecretaryRepository;
import br.com.meusintoma.utils.helpers.RepositoryUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class SecretaryService {

    private final SecretaryRepository secretaryRepository;

    public SecretaryEntity findSecretaryById(UUID secretaryId) {
        return RepositoryUtils.findOrThrow(secretaryRepository.findById(secretaryId),
                () -> new NotFoundException("Secretária"));
    }
}
