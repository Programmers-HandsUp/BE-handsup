package dev.handsup.common.config;

import static org.springframework.http.HttpHeaders.*;

import java.util.List;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@OpenAPIDefinition(
	info = @Info(
		title = "Hands-Up API",
		description = "경매 중고 거래 API 명세서",
		version = "v.0.1"))
@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		SecurityScheme securityScheme = new SecurityScheme()
			.type(SecurityScheme.Type.HTTP)
			.scheme("bearer")
			.bearerFormat("JWT")
			.in(SecurityScheme.In.HEADER)
			.name(AUTHORIZATION);

		SecurityRequirement securityRequirement = new SecurityRequirement().addList(AUTHORIZATION);

		return new OpenAPI()
			.components(new Components().addSecuritySchemes(AUTHORIZATION, securityScheme))
			.security(List.of(securityRequirement));
	}

	@Bean
	public GroupedOpenApi chatOpenApi() {
		String[] paths = {"/api/**"};

		return GroupedOpenApi.builder()
			.group("API v.0.1")
			.pathsToMatch(paths)
			.build();
	}
}
