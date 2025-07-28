package com.backend.adapter.inbound.web.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI api() {
        Contact contact = new Contact();
        contact.setName("Backend");
        contact.setUrl("none");
        contact.setEmail("backend.com@email.com.br");

        return new OpenAPI().
                info(new Info().title("backend-project")
                        .description("Endpoint Documentation")
                        .version("v1")
                        .contact(contact))
                .components(new Components());
    }
}
