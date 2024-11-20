package org.etutoria.cards;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.etutoria.cards.dto.CardsContactInfoDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@RefreshScope
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableConfigurationProperties(value = {CardsContactInfoDto.class})
@OpenAPIDefinition(
        info = @Info(
                title = "Cards microservice REST API Documentation",
                description = "Ismagi Cards microservice REST API Documentation",
                version = "v1",
                contact = @Contact(
                        name = "Ousmane MBACKE",
                        email = "usmanembacke@gmail.com"
                ),
                license = @License(
                        name = "Version 2.0",
                        url = "usmanembaccke@gmail.com"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Ismagi Cards microservice REST API Documentation",
                url = "usmanembacke@gmail.com"
        )
)
public class CardsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardsApplication.class, args);
    }

}
