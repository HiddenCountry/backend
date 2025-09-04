package com.example.hiddencountry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication
@OpenAPIDefinition(
	info = @Info(title = "HiddenCountry", version = "v1", description = "숨은나라찾기 API 명세서"),security = {@SecurityRequirement(name = "bearerAuth")}
)
@SecurityScheme(
	name = "bearerAuth",
	type = SecuritySchemeType.HTTP,
	scheme = "bearer",
	bearerFormat = "JWT"
)
public class HiddencountryApplication {

	public static void main(String[] args) {
		SpringApplication.run(HiddencountryApplication.class, args);
	}

}
