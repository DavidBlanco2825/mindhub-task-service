package com.example.taskservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                title = "Task Service",
                version = "1.0",
                description = "API documentation for the Task Service"
        )
)
public class SwaggerConfig {
}
