package com.pragma.aws.persona.repository.jpa.repository;

import com.pragma.aws.persona.repository.jpa.model.PersonaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPersonaRepository extends JpaRepository<PersonaEntity, Long> {
    Optional<PersonaEntity> findByEmail(String email);
}
