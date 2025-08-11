package com.pragma.aws.persona.controller.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Solicitud de creación de Persona")
public class SavePersonaRequestDTO {
    @NotBlank
    @Schema(description = "Nombre de la Persona", example = "John Doe")
    private String name;

    @NotBlank
    @Email
    @Schema(description = "Email de la Persona", example = "mail@example.com")
    private String email;
}
