package dev.devdreamer.ecommerce.basketservice.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfigurarion {
    @Bean
    public OpenAPI getOpenAPI (){
        final String securitySchemeName = "bearerAuth";
        Contact contact = new Contact();
        contact.email("leonardoguidorizzi.moreira@gmail.com");
        Info info = new Info();
        info.title("Ecommerce's basket");
        info.description("Application for ecommerce's basket");
        info.version("v1");
        info.contact(contact);
        return new OpenAPI().info(info).components(new Components()
                .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                )).addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
    }
}
