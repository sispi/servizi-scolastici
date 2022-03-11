package keysuite.docer;

import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import keysuite.docer.client.corrispondenti.Corrispondente;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2()
public class SpringFoxConfig {


    private static final String DESCRIPTION = ""
            + "<h3>Authentication</h3>";
    
    @Bean
    public Docket api() {

        List<ResponseMessage> responseMessages = Arrays.asList(
                new ResponseMessageBuilder().code(401).message("Unauthorized").build());

        Docket docket = new Docket(DocumentationType.SWAGGER_2);

        docket.useDefaultResponseMessages(false);
        docket.globalResponseMessage(RequestMethod.POST, responseMessages)
                .globalResponseMessage(RequestMethod.PUT, responseMessages)
                .globalResponseMessage(RequestMethod.GET, responseMessages)
                .globalResponseMessage(RequestMethod.DELETE, responseMessages)
                .directModelSubstitute(URL.class, String.class)
                .directModelSubstitute(InputStream.class, String.class)
                .ignoredParameterTypes(Map.class)
                .ignoredParameterTypes(Corrispondente.class)
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .apiInfo(apiInfo());

        return docket;
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(Arrays.asList(securityReference()))
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
                .title("KeySuiteRestUtils API v1")
                .version("1.0")
                .description(DESCRIPTION)
                .build();
    }

}
