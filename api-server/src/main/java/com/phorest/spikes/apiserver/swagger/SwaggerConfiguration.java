package com.phorest.spikes.apiserver.swagger;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.UiConfiguration;

import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.schema.AlternateTypeRules.newRule;

@Configuration
public class SwaggerConfiguration {

  @Autowired
  private TypeResolver typeResolver;

  @Bean
  public Docket petApi() {
    return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any()).paths(PathSelectors.any()).paths(not(regex("/error.*"))).build().pathMapping("/")
      .alternateTypeRules(newRule(typeResolver.resolve(DeferredResult.class, typeResolver.resolve(ResponseEntity.class, WildcardType.class)), typeResolver.resolve(WildcardType.class)))
      .useDefaultResponseMessages(false)
      .globalResponseMessage(RequestMethod.GET, newArrayList(new ResponseMessageBuilder().code(500).message("500 message").responseModel(new ModelRef("Error")).build()))
      .globalOperationParameters(newArrayList(getAuthorizationHeaderParam()));

  }

  private Parameter getAuthorizationHeaderParam() {
    return new ParameterBuilder().name("Authorization").description("Authorization Header").defaultValue("Bearer ").modelRef(new ModelRef("header")).parameterType("header").build();
  }

  @Bean
  SecurityConfiguration security() {
    return new SecurityConfiguration("test-app-client-id", "test-app-client-secret", "test-app-realm", "test-app", "api_key", ApiKeyVehicle.HEADER, ",");
  }

  @Bean
  UiConfiguration uiConfig() {
    return new UiConfiguration("validatorUrl");
  }

}
