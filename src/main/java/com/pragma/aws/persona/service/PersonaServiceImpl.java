package com.pragma.aws.persona.service;

import com.pragma.aws.persona.controller.dto.request.SavePersonaRequestDTO;
import com.pragma.aws.persona.controller.dto.response.FindPersonaResponse;
import com.pragma.aws.persona.controller.dto.response.SavePersonaResponseDTO;
import com.pragma.aws.persona.repository.jpa.repository.IPersonaRepository;
import com.pragma.aws.persona.service.exception.DataAlreadyExists;
import com.pragma.aws.persona.service.exception.DataNotFoundException;
import com.pragma.aws.persona.service.exception.Mesagges;
import com.pragma.aws.persona.service.mapper.PersonaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonaServiceImpl implements IPersonaService {

    private final IPersonaRepository personaRepository;
    private final PersonaMapper personaMapper;

    @Override
    public SavePersonaResponseDTO savePersona(SavePersonaRequestDTO savePersonaRequestDTO) {
        if (personaRepository.findByEmail(savePersonaRequestDTO.getEmail()).orElse(null) != null) {
            throw new DataAlreadyExists(Mesagges.PERSONA_ALREADY_EXISTS.name());
        }

        return personaMapper.toSavePersonaResponseDTO(
                personaRepository.save(personaMapper.toPersonaEntity(savePersonaRequestDTO)
                ));
    }

    @Override
    public FindPersonaResponse findPersonaByEmail(String email) {
        return personaMapper.toFindPersonaResponse(
                personaRepository.findByEmail(email).
                        orElseThrow(() -> new DataNotFoundException(Mesagges.PERSONA_NOT_FOUND.name()))
        );
    }
}
