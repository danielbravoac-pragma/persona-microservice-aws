package com.pragma.aws.persona.service;

import com.pragma.aws.persona.controller.dto.request.SavePersonaRequestDTO;
import com.pragma.aws.persona.controller.dto.response.FindPersonaResponse;
import com.pragma.aws.persona.controller.dto.response.SavePersonaResponseDTO;
import com.pragma.aws.persona.repository.jpa.model.PersonaEntity;
import com.pragma.aws.persona.repository.jpa.repository.IPersonaRepository;
import com.pragma.aws.persona.service.exception.DataAlreadyExists;
import com.pragma.aws.persona.service.exception.DataNotFoundException;
import com.pragma.aws.persona.service.exception.Mesagges;
import com.pragma.aws.persona.service.mapper.PersonaMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonaServiceImplTest {

    @Mock
    private IPersonaRepository personaRepository;

    @Mock
    private PersonaMapper personaMapper;

    @InjectMocks
    private PersonaServiceImpl service;

    private SavePersonaRequestDTO buildRequest(String name, String email) {
        SavePersonaRequestDTO dto = new SavePersonaRequestDTO();
        dto.setName(name);
        dto.setEmail(email);
        return dto;
    }

    @Nested
    @DisplayName("savePersona")
    class SavePersonaTests {

        @Test
        @DisplayName("guarda cuando el email no existe")
        void savePersona_ok() {
            // Arrange
            SavePersonaRequestDTO request = buildRequest("John Doe", "john@example.com");
            given(personaRepository.findByEmail("john@example.com")).willReturn(Optional.empty());

            PersonaEntity toSave = new PersonaEntity();
            toSave.setName("John Doe");
            toSave.setEmail("john@example.com");

            PersonaEntity saved = new PersonaEntity();
            saved.setId(1L);
            saved.setName("John Doe");
            saved.setEmail("john@example.com");

            given(personaMapper.toPersonaEntity(request)).willReturn(toSave);
            given(personaRepository.save(toSave)).willReturn(saved);

            SavePersonaResponseDTO mapped =
                    new SavePersonaResponseDTO(1L, "John Doe", "john@example.com");
            given(personaMapper.toSavePersonaResponseDTO(saved)).willReturn(mapped);

            // Act
            SavePersonaResponseDTO result = service.savePersona(request);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getName()).isEqualTo("John Doe");
            assertThat(result.getEmail()).isEqualTo("john@example.com");

            verify(personaRepository).findByEmail("john@example.com");
            verify(personaMapper).toPersonaEntity(request);
            verify(personaRepository).save(toSave);
            verify(personaMapper).toSavePersonaResponseDTO(saved);
            verifyNoMoreInteractions(personaRepository, personaMapper);
        }

        @Test
        @DisplayName("lanza DataAlreadyExists si el email ya existe")
        void savePersona_emailExists() {
            // Arrange
            SavePersonaRequestDTO request = buildRequest("Jane", "jane@example.com");
            PersonaEntity existing = new PersonaEntity();
            existing.setId(99L);
            existing.setName("Jane");
            existing.setEmail("jane@example.com");
            given(personaRepository.findByEmail("jane@example.com"))
                    .willReturn(Optional.of(existing));

            // Act + Assert
            assertThatThrownBy(() -> service.savePersona(request))
                    .isInstanceOf(DataAlreadyExists.class)
                    .hasMessage(Mesagges.PERSONA_ALREADY_EXISTS.name());

            verify(personaRepository).findByEmail("jane@example.com");
            verifyNoMoreInteractions(personaRepository);
            verifyNoInteractions(personaMapper);
        }
    }

    @Nested
    @DisplayName("findPersonaByEmail")
    class FindPersonaByEmailTests {

        @Test
        @DisplayName("devuelve DTO cuando existe la persona")
        void findPersona_ok() {
            // Arrange
            String email = "exists@example.com";
            PersonaEntity entity = new PersonaEntity();
            entity.setId(42L);
            entity.setName("Alice");
            entity.setEmail(email);

            given(personaRepository.findByEmail(email)).willReturn(Optional.of(entity));

            FindPersonaResponse response = new FindPersonaResponse(42L, "Alice");
            given(personaMapper.toFindPersonaResponse(entity)).willReturn(response);

            // Act
            FindPersonaResponse result = service.findPersonaByEmail(email);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(42L);
            assertThat(result.getName()).isEqualTo("Alice");

            verify(personaRepository).findByEmail(email);
            verify(personaMapper).toFindPersonaResponse(entity);
            verifyNoMoreInteractions(personaRepository, personaMapper);
        }

        @Test
        @DisplayName("lanza DataNotFoundException cuando no existe")
        void findPersona_notFound() {
            // Arrange
            String email = "nope@example.com";
            given(personaRepository.findByEmail(email)).willReturn(Optional.empty());

            // Act + Assert
            assertThatThrownBy(() -> service.findPersonaByEmail(email))
                    .isInstanceOf(DataNotFoundException.class)
                    .hasMessage(Mesagges.PERSONA_NOT_FOUND.name());

            verify(personaRepository).findByEmail(email);
            verifyNoMoreInteractions(personaRepository);
            verifyNoInteractions(personaMapper);
        }
    }
}
