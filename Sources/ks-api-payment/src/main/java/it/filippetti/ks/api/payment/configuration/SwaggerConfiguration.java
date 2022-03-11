/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.configuration;

import it.filippetti.ks.api.payment.dto.VoidDTO;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import springfox.documentation.OperationNameGenerator;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author marco.mazzocchetti
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    private static final String DESCRIPTION = ""
        + "<h3>Authentication</h3>"
        + "<p>All operations must be authenticated through a JWT token (Bearer Token Authentication) transported into the <i>Authorization</i> header.</p>"
        + "<p>For testing purposes, only in development/staging environments, and only if explicitly configured by system administrators, you can use this token</p>"
        + "<p><i>eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6IkFPT19URVNUIiwiaWF0IjoxNjA3MzUxNTE1LCJleHAiOjE2Mzg4ODc1MTV9.qwrAI_DqWcEFBa9cE2FV-qvQMCHIaHAGlPQNCRql0ItVuHugy19jvBb6Uya4GUhCEZmBFsliTi-3pgMQYQLzag</i></p>"
        + "<p>This token authorizes an administrator role for tenant <i>ENTE_TEST</i> and organization <i>AOO_TEST</i>.</p>"
            
        + "<h3>Errors</h3>"
        + "<p>All operations, in case of error, return a standard payload error described by the <b>Error</b> model schema.</p>"
        + "<p>The field <i>code</i> it's valued only for 422 errors, i.e. business errors.</p>"
        + "<p>The field <i>details</i> will contain additional error messages, if any.</p>"
            
        + "<h3>Paging</h3>"
        + "<p>GET operations on a collection can provide for paging through the <b>pageNumber</b> and <b>pageSize</b> query parameters. The page number is 1-based. The default page is the first, the default page size is 10 and the maximum page size is 4096 (configurable).</p>"
            
        + "<h3>Dynamic Fetch</h3>"
        + "<p>GET operations can provide for the dynamic loading of sub-resources (usually collections or large contents) through the <b>fetch</b> query parameter. The parameter allows you to specify a list of json properties to fetch, even at nested depth levels through the use of parentheses syntax as in the example:</p>"
        + "<p><i>GET / instances/{id}?fetch=variables(value),nodes(commands(errors),task)</i></p>"
        + "<p>In this case, you get an instance detail, including the instance variables and nodes. For each variable get the value, and for each node get the associated commands including any errors, and the task associated with the node, if any.</p>"
        + "<p>See the Model section for a complete reference of all the fetchable properties of each type.</p>"
            
        + "<h3>Sort</h3>"
        + "<p>GET operation on a collection can provide for a sorting of the results through the query parameter <b>orderBy</b> valued as in the example:</p>"
        + "<p><i>GET /instances?orderBy=status:ASC,startTs:DESC</i></p>"
        + "<p>The syntax for asc/desc is case-insensitive and <i>asc</i>is the default.</p>"
        + "<p>See the Model section for a complete reference of all the sortable properties of each type.</p>"
            
        + "<h3>Operations Idempotency</h3>"
        + "<p>To support the secure retry of API operations in a service architecture, a mechanism is needed that makes idempotent operations that by their nature are not, usually POST operations.</p>"
        + "<p>For this purpose, each non-idempotent POST operation can include the <b>operationId</b> argument in the input payload to be valued by the client with a UUID value.</p>"
        + "<p>For testability reasons the field will not be mandatory, it is therefore a feature that the client chooses to adopt.</p>"
        + "<p>It is important that the retry policy on the client side provides a waiting time between two different attempts (e.g. exponential backoff).</p>";

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("it.filippetti.ks.api.payment.endpoint"))
            .paths(PathSelectors.regex("/.*"))
            .build()
            .pathMapping("/")
            .useDefaultResponseMessages(false)
            .directModelSubstitute(VoidDTO.class, Void.class)
            .genericModelSubstitutes(ResponseEntity.class)
            .forCodeGeneration(true)
            .securityContexts(List.of(securityContext()))
            .securitySchemes(List.of(apiKey()))
            .apiInfo(apiInfo());
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
            .securityReferences(List.of(securityReference()))
            .forPaths(PathSelectors.regex("/.*"))
            .build();
    }

    private SecurityReference securityReference() {
        return new SecurityReference(
            "Bearer Authentication",
            new AuthorizationScope[]{new AuthorizationScope(
                "global",
                "accessEverything")});
    }

    private ApiKey apiKey() {
        return new ApiKey(
            "Bearer Authentication",
            "Authorization",
            "header");
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("KeySuite Payment API v1")
            .version("1.0")
            .description(DESCRIPTION)
            .build();
    }

    public static class x extends OperationBuilder {
    
        public x(OperationNameGenerator nameGenerator) {
            super(nameGenerator);
        }
    

    
    }
    
    @Component
    @Primary
    public static class CustomOperationNameGenerator extends CachingOperationNameGenerator {

        @Override
        public String startingWith(final String prefix) {
            return prefix.substring(0, prefix.indexOf("Using"));
        }
    }
}
