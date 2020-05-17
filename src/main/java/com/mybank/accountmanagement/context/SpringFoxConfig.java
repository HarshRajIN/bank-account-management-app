package com.mybank.accountmanagement.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.function.Predicate;

@Configuration
@EnableSwagger2WebMvc
@Import(SpringDataRestConfiguration.class)
public class SpringFoxConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select().apis(exactPackages("com.mybank.accountmanagement.controllers", "com.mybank.accountmanagement.models"))
                .paths(PathSelectors.any()).build();
    }
    private static Predicate<RequestHandler> exactPackages(final String... pkgs) {
        return input -> {
            String currentPkg =
                    input.declaringClass().getPackage().getName();
            for (String pkg : pkgs) {
                if (pkg.equals(currentPkg)) {
                    return true;
                }
            }
            return false;
        };
    }
}