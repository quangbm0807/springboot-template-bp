package com.quang.template.config.swagger;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Additional OpenAPI customizations
 */
@Configuration
public class OpenApiCustomConfiguration {

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            io.swagger.v3.oas.models.tags.Tag authTag = new io.swagger.v3.oas.models.tags.Tag()
                    .name("Authentication")
                    .description("Authentication operations")
                    .externalDocs(new io.swagger.v3.oas.models.ExternalDocumentation()
                            .description("Authentication Flow Documentation")
                            .url("https://example.com/docs/auth"));
            io.swagger.v3.oas.models.tags.Tag usersTag = new io.swagger.v3.oas.models.tags.Tag()
                    .name("Users")
                    .description("User management operations");
            openApi.setTags(Arrays.asList(authTag, usersTag));
            openApi.setExternalDocs(new io.swagger.v3.oas.models.ExternalDocumentation()
                    .description("API Reference Documentation")
                    .url("https://example.com/docs/api"));
        };
    }

    /**
     * Tags for API documentation grouping
     */
    public static class ApiTags {
        @Tag(
                name = "Authentication",
                description = "Authentication operations",
                externalDocs = @ExternalDocumentation(
                        description = "Authentication Flow",
                        url = "https://example.com/docs/auth"
                )
        )
        public interface Authentication {}

        @Tag(
                name = "Users",
                description = "User management operations",
                extensions = {
                        @Extension(
                                name = "x-version",
                                properties = @ExtensionProperty(name = "deprecated", value = "false")
                        )
                }
        )
        public interface Users {}

    }
}