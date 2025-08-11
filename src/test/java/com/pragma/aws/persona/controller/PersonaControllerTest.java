package com.pragma.aws.persona.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.aws.persona.controller.dto.request.SavePersonaRequestDTO;
import com.pragma.aws.persona.controller.dto.response.FindPersonaResponse;
import com.pragma.aws.persona.controller.dto.response.SavePersonaResponseDTO;
import com.pragma.aws.persona.controller.exception.ErrorHandler;
import com.pragma.aws.persona.service.IPersonaService;
import com.pragma.aws.persona.service.exception.DataAlreadyExists;
import com.pragma.aws.persona.service.exception.DataNotFoundException;
import com.pragma.aws.persona.service.exception.Mesagges;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PersonaControllerTest {

    private MockMvc mockMvc;
    private IPersonaService personaService; // mock “manual”, NO @MockBean
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        personaService = Mockito.mock(IPersonaService.class);
        objectMapper = new ObjectMapper();

        // Bean Validation
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders
                .standaloneSetup(new PersonaController(personaService))
                .setControllerAdvice(new ErrorHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .setValidator(validator)
                .build();
    }

    @Nested
    @DisplayName("GET /personas")
    class GetPersonas {

        @Test
        @DisplayName("200 OK cuando existe")
        void ok() throws Exception {
            String email = "john@example.com";
            when(personaService.findPersonaByEmail(email))
                    .thenReturn(new FindPersonaResponse(1L, "John Doe"));

            mockMvc.perform(get("/personas").param("email", email))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.name", is("John Doe")));
        }

        @Test
        @DisplayName("404 cuando no existe (DataNotFoundException → ErrorHandler)")
        void notFound() throws Exception {
            String email = "nope@example.com";
            given(personaService.findPersonaByEmail(email))
                    .willThrow(new DataNotFoundException(Mesagges.PERSONA_NOT_FOUND.name()));

            mockMvc.perform(get("/personas").param("email", email))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().string(containsString("SOME_DATA_COULD_NOT_BE_FOUND")));
        }

        @Test
        @DisplayName("400 cuando falta el parámetro email (Spring)")
        void missingParam() throws Exception {
            mockMvc.perform(get("/personas"))
                    .andExpect(status().isBadRequest());
            verifyNoMoreInteractions(personaService);
        }
    }

    @Nested
    @DisplayName("POST /personas")
    class PostPersonas {

        @Test
        @DisplayName("201 Created con body válido")
        void created() throws Exception {
            SavePersonaRequestDTO req = new SavePersonaRequestDTO("John Doe", "john@example.com");
            given(personaService.savePersona(req))
                    .willReturn(new SavePersonaResponseDTO(1L, "John Doe", "john@example.com"));

            mockMvc.perform(post("/personas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.name", is("John Doe")))
                    .andExpect(jsonPath("$.email", is("john@example.com")));
        }

        @Test
        @DisplayName("400 por Bean Validation (email inválido) → ErrorHandler")
        void invalidEmail() throws Exception {
            SavePersonaRequestDTO req = new SavePersonaRequestDTO("John Doe", "not-an-email");

            mockMvc.perform(post("/personas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().string(containsString("SOME_INVALID_FIELDS")))
                    .andExpect(content().string(containsString("email")));
        }

        @Test
        @DisplayName("400 cuando ya existe (DataAlreadyExists) → ErrorHandler")
        void alreadyExists() throws Exception {
            SavePersonaRequestDTO req = new SavePersonaRequestDTO("Jane", "jane@example.com");
            given(personaService.savePersona(req))
                    .willThrow(new DataAlreadyExists(Mesagges.PERSONA_ALREADY_EXISTS.name()));

            mockMvc.perform(post("/personas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().string(containsString("SOME_DATA_IS_ALREADY_REGISTERED")));
        }

        @Test
        @DisplayName("400 por Bean Validation (name en blanco) → ErrorHandler")
        void blankName() throws Exception {
            SavePersonaRequestDTO req = new SavePersonaRequestDTO("", "john@example.com");

            mockMvc.perform(post("/personas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().string(containsString("SOME_INVALID_FIELDS")))
                    .andExpect(content().string(containsString("name")));
        }
    }
}
