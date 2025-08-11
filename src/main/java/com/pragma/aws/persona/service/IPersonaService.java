package com.pragma.aws.persona.service;

import com.pragma.aws.persona.controller.dto.request.SavePersonaRequestDTO;
import com.pragma.aws.persona.controller.dto.response.FindPersonaResponse;
import com.pragma.aws.persona.controller.dto.response.SavePersonaResponseDTO;

public interface IPersonaService {
    SavePersonaResponseDTO savePersona(SavePersonaRequestDTO savePersonaRequestDTO);

    FindPersonaResponse findPersonaByEmail(String email);
}
