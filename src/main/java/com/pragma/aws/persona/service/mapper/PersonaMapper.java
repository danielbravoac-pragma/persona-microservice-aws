package com.pragma.aws.persona.service.mapper;

import com.pragma.aws.persona.controller.dto.request.SavePersonaRequestDTO;
import com.pragma.aws.persona.controller.dto.response.FindPersonaResponse;
import com.pragma.aws.persona.controller.dto.response.SavePersonaResponseDTO;
import com.pragma.aws.persona.repository.jpa.model.PersonaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface PersonaMapper {
    FindPersonaResponse toFindPersonaResponse(PersonaEntity personaEntity);

    SavePersonaResponseDTO toSavePersonaResponseDTO(PersonaEntity personaEntity);

    PersonaEntity toPersonaEntity(SavePersonaRequestDTO savePersonaRequestDTO);
}
