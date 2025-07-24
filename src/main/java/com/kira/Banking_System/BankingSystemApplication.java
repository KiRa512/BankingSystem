package com.kira.Banking_System;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Banking System API",
				version = "1.0",
				description = "API for managing banking operations",
				contact = @Contact(
						name = "Kira",
						email = "khaledhamza509@gmail.com",
						url = "https://github.com/KiRa512/Banking_System"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "GitHub Repository",
				url = "https://github.com/KiRa512/Banking_System"
		)
)
public class BankingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingSystemApplication.class, args);
	}

}
