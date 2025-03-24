package br.com.meusintoma.modules.symptonEvent.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.meusintoma.modules.symptonEvent.entity.SymptonEventEntity;

public interface SymptonEventRepository extends JpaRepository<SymptonEventEntity, UUID> {

}
