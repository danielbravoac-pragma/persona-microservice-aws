package com.pragma.aws.persona.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Respuesta al registrar a una Persona")
public class SavePersonaResponseDTO {
    @Schema(description = "Id interno de la Persona", example = "1")
    private Long id;

    @Schema(description = "Nombre de la Persona", example = "John Doe")
    private String name;

    @Schema(description = "Email de la Persona", example = "mail@example.com")
    private String email;
}
