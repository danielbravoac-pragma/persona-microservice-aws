package com.pragma.aws.persona.controller;


import com.pragma.aws.persona.controller.dto.request.SavePersonaRequestDTO;
import com.pragma.aws.persona.controller.dto.response.FindPersonaResponse;
import com.pragma.aws.persona.controller.dto.response.SavePersonaResponseDTO;
import com.pragma.aws.persona.controller.exception.Info;
import com.pragma.aws.persona.service.IPersonaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/personas", produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Persona", description = "Gestión de Personas.")
public class PersonaController {

    private final IPersonaService personaService;

    @Operation(
            summary = "Encontrar Persona",
            description = "Encontrar Persona por Email.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Persona encontrada con éxito.",
                            content = @Content(schema = @Schema(implementation = FindPersonaResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Persona no encontrada.",
                            content = @Content(schema = @Schema(implementation = Info.class))),
                    @ApiResponse(responseCode = "500", description = "Error Interno.",
                            content = @Content(schema = @Schema(implementation = Info.class)))
            }
    )
    @GetMapping
    public ResponseEntity<FindPersonaResponse> findPersonaByEmail(@RequestParam(name = "email") String email) {
        return new ResponseEntity<>(personaService.findPersonaByEmail(email), HttpStatus.OK);
    }

    @Operation(
            summary = "Registrar Persona",
            description = "Registrar a una persona.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Persona registrada con éxito.",
                            content = @Content(schema = @Schema(implementation = FindPersonaResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Parámetros inválidos o Persona ya existe.",
                            content = @Content(schema = @Schema(implementation = Info.class))),
                    @ApiResponse(responseCode = "500", description = "Error Interno.",
                            content = @Content(schema = @Schema(implementation = Info.class)))
            }
    )
    @PostMapping
    public ResponseEntity<SavePersonaResponseDTO> savePersona(@RequestBody @Valid SavePersonaRequestDTO savePersonaRequestDTO) {
        return new ResponseEntity<>(personaService.savePersona(savePersonaRequestDTO), HttpStatus.CREATED);
    }

}
