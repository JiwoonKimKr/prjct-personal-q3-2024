package com.givemetreat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerUiConfig {
	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
				.components(new Components())
				.info(apiInfo());
	};
	
	private Info apiInfo() {
		return new Info()
				.title("givemetreat")
				.description("personal project 3Q 2024_Mega It Gangnam")
				.version("0.9.0");
	}
}
