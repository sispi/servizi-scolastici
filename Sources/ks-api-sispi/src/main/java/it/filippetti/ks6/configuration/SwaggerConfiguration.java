/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks6.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import it.filippetti.ks6.dto.VoidDTO;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author marco.mazzocchetti
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

	private static final String AUTH_HEADER = "Authorization";

	@Bean
	public Docket docket(@Value("${swagger.version}") String appVersion) {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("it.filippetti.sispi.rest")).paths(PathSelectors.regex("/.*"))
				.build().pathMapping("/").useDefaultResponseMessages(false)
				.directModelSubstitute(VoidDTO.class, Void.class).genericModelSubstitutes(ResponseEntity.class)
				.forCodeGeneration(true).securityContexts(List.of(securityContext())).securitySchemes(List.of(apiKey()))
				.apiInfo(apiInfo(appVersion)).globalRequestParameters(globalParameters());
	}

//	private SecurityContext securityContext() {
//		return SecurityContext.builder().securityReferences(List.of(securityReference()))
//				.forPaths(PathSelectors.regex("/.*")).build();
//	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(List.of(securityReference())).operationSelector(i -> true)
				.build();
	}

	private SecurityReference securityReference() {
		return new SecurityReference("Bearer Authentication",
				new AuthorizationScope[] { new AuthorizationScope("global", "accessEverything") });
	}

	private ApiKey apiKey() {
		return new ApiKey("Bearer Authentication", AUTH_HEADER, "header");
	}

	private ApiInfo apiInfo(String appVersion) {
		return new ApiInfoBuilder().title("KeySuite SISPI API v1").version(appVersion).build();
	}

	private List<RequestParameter> globalParameters() {
		final RequestParameterBuilder aParameterBuilder = new RequestParameterBuilder();
		aParameterBuilder.name(AUTH_HEADER).query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
				.in(ParameterType.HEADER).required(false).build();
		final List<RequestParameter> aParameters = new ArrayList<>();
		aParameters.add(aParameterBuilder.build());
		return aParameters;

	}
}
