package com.organization.walletapi.config;

import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class WalletApiConfiguration {
    @Bean
    public Docket createRestApi() {
    	return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.organization.walletapi"))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo apiInfo() {
    	return new ApiInfo(
                "Wallet Microservice",
                "Wallet Microservice running on the JVM that manages debit/credit transactions on behalf of players",
                "1.0",
                "urn:tos",
                new Contact("Geeta","Geeta","geetatech5@gmail.com"),
                "Apache LICENSE-2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                Collections.emptyList()
        );
    }
}

