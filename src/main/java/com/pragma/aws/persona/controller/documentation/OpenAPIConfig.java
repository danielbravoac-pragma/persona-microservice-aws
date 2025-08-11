package com.pragma.aws.persona.controller.documentation;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customConfigurationsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Personas")
                        .version("1.0")
                        .description("Documentación de API de gestión de Personas")
                        .contact(new Contact()
                                .name("Daniel Bravo")
                                .email("daniel.bravo@pragma.com.co"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org"))
                );
    }
}
