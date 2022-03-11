package keysuite.docer;

import com.google.common.collect.Lists;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import keysuite.docer.client.corrispondenti.Corrispondente;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
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

    @Bean
    public Docket api() {

        //ModelRef errorModel = new ModelRef("RestApiExceptionModel");
        List<ResponseMessage> responseMessages = Arrays.asList(
                new ResponseMessageBuilder().code(401).message("Unauthorized").build());

        /*ApiSelectorBuilder builder = new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())

                ;*/
        //Docket docket = builder.build();

        Docket docket = new Docket(DocumentationType.SWAGGER_2);

        ApiInfo info = new ApiInfo("Docer Rest", "Docer Rest", "1.0", "urn:tos",
                new Contact("", "", ""), "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0", new ArrayList<VendorExtension>());

        docket.useDefaultResponseMessages(false).apiInfo(info);
        docket.globalResponseMessage(RequestMethod.POST, responseMessages)
                .globalResponseMessage(RequestMethod.PUT, responseMessages)
                .globalResponseMessage(RequestMethod.GET, responseMessages)
                .globalResponseMessage(RequestMethod.DELETE, responseMessages)
                .directModelSubstitute(URL.class, String.class)
                .directModelSubstitute(InputStreamResource.class, MultipartFile.class)
                //.directModelSubstitute(Resource.class, MultipartFile.class)
                //.directModelSubstitute(InputStream.class, MultipartFile.class)
                //.directModelSubstitute(ResponseEntity.class, MultipartFile.class)
                .ignoredParameterTypes(Map.class)
                .ignoredParameterTypes(Corrispondente.class)
                .securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()));
                //.securityContexts()

        //docket.useDefaultResponseMessages(false);

        return docket;
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("^/(documenti|anagrafiche|cartelle|files|firma|gruppi|report|utenti|titolari|system/commit|system/deleteByQuery|search|solr).*"))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(
                new SecurityReference("JWT", authorizationScopes));
    }
}
