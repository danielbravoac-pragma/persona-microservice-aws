package com.pragma.aws.persona.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta al encontrar a una Persona")
public class FindPersonaResponse {
    @Schema(description = "Id interno de la Persona", example = "1")
    private Long id;

    @Schema(description = "Nombre de la Persona", example = "John Doe")
    private String name;
}
