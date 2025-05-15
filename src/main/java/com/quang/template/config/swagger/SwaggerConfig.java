package com.quang.template.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class SwaggerConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private int serverPort;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme())
                        .addResponses("UnauthorizedError", createUnauthorizedResponse())
                        .addResponses("ForbiddenError", createForbiddenResponse())
                        .addResponses("BadRequestError", createBadRequestResponse())
                        .addResponses("NotFoundError", createNotFoundResponse())
                        .addResponses("ServerError", createServerErrorResponse())
                )
                .info(new Info()
                        .title(applicationName + " API Documentation")
                        .description("RESTful API Documentation for " + applicationName)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("API Support Team")
                                .email("contact@example.com")
                                .url("https://www.example.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .addServersItem(new Server()
                        .url("http://localhost:" + serverPort)
                        .description("Local development server"))
                .addServersItem(new Server()
                        .url("https://api.example.com")
                        .description("Production server"));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer")
                .description("Provide a JWT token. JWT token can be obtained from the Authentication API. " +
                        "Enter the token with the `Bearer: ` prefix, e.g. \"Bearer abcde12345\".");
    }

    private ApiResponse createUnauthorizedResponse() {
        return new ApiResponse()
                .description("Unauthorized - Authentication failed or token missing/expired")
                .content(new Content()
                        .addMediaType("application/json", createErrorMediaType(HttpStatus.UNAUTHORIZED.value(),
                                "Authentication failed", "Invalid credentials or token expired")));
    }

    private ApiResponse createForbiddenResponse() {
        return new ApiResponse()
                .description("Forbidden - Not enough permissions to access this resource")
                .content(new Content()
                        .addMediaType("application/json", createErrorMediaType(HttpStatus.FORBIDDEN.value(),
                                "Access denied", "You don't have permission to access this resource")));
    }

    private ApiResponse createBadRequestResponse() {
        return new ApiResponse()
                .description("Bad Request - Request validation failed")
                .content(new Content()
                        .addMediaType("application/json", createErrorMediaType(HttpStatus.BAD_REQUEST.value(),
                                "Data is invalid", "Username is already taken")));
    }

    private ApiResponse createNotFoundResponse() {
        return new ApiResponse()
                .description("Not Found - Resource not found")
                .content(new Content()
                        .addMediaType("application/json", createErrorMediaType(HttpStatus.NOT_FOUND.value(),
                                "Not found", "User not found with id: 1")));
    }

    private ApiResponse createServerErrorResponse() {
        return new ApiResponse()
                .description("Internal Server Error - Unexpected error occurred")
                .content(new Content()
                        .addMediaType("application/json", createErrorMediaType(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Error occurred", "An unexpected error occurred")));
    }

    private MediaType createErrorMediaType(int status, String message, String exampleMessage) {
        Map<String, Object> responseExample = new HashMap<>();
        responseExample.put("status", status);
        responseExample.put("message", exampleMessage);
        responseExample.put("data", null);

        Example example = new Example()
                .value(responseExample);

        Schema<?> schema = new Schema<>()
                .type("object")
                .properties(Map.of(
                        "status", new Schema<>().type("integer").example(status),
                        "message", new Schema<>().type("string").example(message),
                        "data", new Schema<>().type("object").nullable(true),
                        "timestamp", new Schema<>().type("string").format("date-time").example("2025-05-15T12:34:56.789")
                ));

        return new MediaType()
                .schema(schema)
                .addExamples("example", example);
    }
}