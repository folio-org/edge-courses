package com.ebsco.edgecourses.config;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@ControllerAdvice
@Configuration
@EnableSwagger2
public class SwaggerConfig {

  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String DEFAULT_INCLUDE_PATTERN = "/.*";

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.any())
        .paths(PathSelectors.regex("/error.*").negate()) // Exclude Spring error controllers
        .paths(PathSelectors.regex("/_/tenant.*").negate()) // Exclude Tenant controllers
        .paths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN))
        .build()
        .securityContexts(Lists.newArrayList(securityContext()))
        .securitySchemes(Lists.newArrayList(apiKey()))
        .apiInfo(apiInfo())
        .tags(
            new Tag("Edge courses", "Returns the courses/reserves from mod-courses. Authorization made by provided apiKey"));
  }

  private ApiInfo apiInfo() {
    return new ApiInfo(
        "Edge courses API",
        "API to integrate external modules with folio mod-courses .<br/><br /> <strong>Please Note: There are no response models due to the use of dynamic response templates</strong><br/><br />",
        "v1",
        "Terms of service", new Contact("", "", ""),
        "License of API",
        "API license URL",
        Collections.emptyList());
  }


  private ApiKey apiKey() {
    return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
  }

  private SecurityContext securityContext() {
    return SecurityContext
        .builder()
        .securityReferences(defaultAuth())
        .build();
  }

  List<SecurityReference> defaultAuth() {
    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    return Lists.newArrayList(new SecurityReference("JWT", authorizationScopes));
  }

}
