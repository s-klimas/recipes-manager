package pl.sebastianklimas.recipesmenager.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        tags = {
                @Tag(name = "Auth"),
                @Tag(name = "AI Logic"),
                @Tag(name = "Recipes"),
                @Tag(name = "Users")

        })
public class SwaggerConfig {
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().addSecurityItem(new SecurityRequirement().
                        addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes
                        ("Bearer Authentication", createAPIKeyScheme()))
                .info(
                        new Info().title("Recipe Manager REST API")
                        .description("Here you can try out and better prepare to integrate with frontend.")
                        .version("1.0").contact(
                                new Contact()
                                        .name("Sebastian Klimas")
                                        .email( "sebastianklimas@op.pl")
                                        .url("https://sebastianklimas.pl")));
    }
}
