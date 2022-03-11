package keysuite.desktop;

import it.kdm.orchestratore.utils.ResourceUtils;
import keysuite.desktop.security.SecurityConfig;
import keysuite.desktop.zuul.*;
import keysuite.docer.client.ClientUtils;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.post.LocationRewriteFilter;
import org.springframework.cloud.netflix.zuul.filters.post.ZuulLocationRewriteFilter;
import org.springframework.cloud.netflix.zuul.filters.pre.FormBodyWrapperFilter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Rappresenta una spring boot application base<br>
 * che usa il custom resource loader {@link DesktopResourceLoader}<br>
 * Imposta inoltre il web context di default a /AppDoc leggendo la variabile di ambiente server.servlet.context-path<br>
 * Ereditare questa classe per acquisire i comportamenti base
 */
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        SolrAutoConfiguration.class,
        ThymeleafAutoConfiguration.class,
        ErrorMvcAutoConfiguration.class
})
@EnableZuulProxy
@EnableConfigurationProperties(KeycloakSpringBootProperties.class)

public class DesktopApplication {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";

    public static String GREEN(String msg){
        return ANSI_GREEN+msg+ANSI_RESET;
    }

    public final static DesktopResourceLoader loader = new DesktopResourceLoader();

    public static void main(String[] args) {

        System.setProperty("nashorn.args","--no-deprecation-warning=true --language=es6");
        System.setProperty("java.net.preferIPv4Stack","true");

        ClientUtils.getConfigHome();
        //System.setProperty("server.servlet.context-path", System.getProperty("server.servlet.context-path","/") );
        //System.setProperty("java.net.preferIPv4Stack","true");

        SpringApplication app =  new SpringApplicationBuilder(DesktopApplication.class)
                .resourceLoader(loader)
                .build();

        ConfigurableApplicationContext context = app.run(args);

        System.out.println(ANSI_GREEN);

        System.out.println("spring.profiles.active:"+ context.getEnvironment().getProperty("spring.profiles.active",""));
        System.out.println("KEYSUITE_CONFIG="+System.getProperty("KEYSUITE_CONFIG"));
        System.out.println("resource folder: "+ ResourceUtils.getResourceRoot(null));
        System.out.println("server.port: "+ context.getEnvironment().getProperty("server.port"));

        if (System.getProperty("host.default")==null){
            System.out.println(ANSI_RED);
            System.out.println("ERROR: host.default non impostato");
        } else {
            System.out.println("host.default: "+ System.getProperty("host.default"));
        }

        System.out.println("keycloak:"+ System.getProperty("keycloakFilter","disabled"));
        System.out.println("static.patterns: "+ context.getEnvironment().getProperty("static.patterns", SecurityConfig.STATIC_PATTERNS));
        System.out.println("public.patterns: "+ context.getEnvironment().getProperty("public.patterns", SecurityConfig.PUBLIC_PATTERNS));
        System.out.println("authorized.patterns: "+ context.getEnvironment().getProperty("authorized.patterns", SecurityConfig.AUTHORIZED_PATTERNS));

        System.out.println(ANSI_RESET);

    }
    @Bean
    ZuulRouteFilter zuulRouteFilter(){
        return new ZuulRouteFilter();
    }
    @Bean
    ZuulPostFilter zuulPostFilter(){
        return new ZuulPostFilter();
    }
    @Bean
    ZuulPreFilter zuulPreFilter(){
        return new ZuulPreFilter();
    }
    @Bean
    ZuulErrorFilter zuulErrorFilter(){
        return new ZuulErrorFilter();
    }

    @Bean
    ZuulLocationRewriteFilter locationRewriteFilter() {
        return new ZuulLocationRewriteFilter();
    }

    @Bean
    FormBodyWrapperFilter formBodyWrapperFilter(){
        return new ZuulFormPreFitler();
    }

    /*@Bean
    public ZuulHandlerMapping zuulHandlerMapping(RouteLocator routes,
                                                 ZuulController zuulController) {
        ZuulHandlerMapping mapping = new ZuulHandlerMapping(routes, zuulController);
        return mapping;
    }*/

    /*@Bean("configApps")
    Map<String, Map> configAppBean() {
        return ConfigAppBean.getApps();
    }*/


}