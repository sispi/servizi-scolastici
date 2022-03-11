package keysuite.docer.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@Configuration
@EnableWebSecurity
//@PropertySource(value = "security.properties",ignoreResourceNotFound = true, name = "security.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    //@Autowired
    //private KeycloakProperties keycloakProperties;

    @Autowired
    private Environment env;

    /*@Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter() {
            @Override
            protected boolean shouldLog(HttpServletRequest request) {
                return true;
            }
        };
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setMaxPayloadLength(64000);
        return loggingFilter;
    }*/

    /*PreSecurityFilter preSecurityFilter = null;

    PreSecurityFilter getPreSecurityFilter(){
        if (preSecurityFilter==null && keycloakProperties.isEnabled()){
            Map config = keycloakProperties.getConfig();
            if (config!=null && config.size()>0){
                preSecurityFilter = new PreSecurityFilter(env, config);
                System.setProperty("keycloakFilter","enabled");
            }
        }
        return preSecurityFilter;
    }*/

    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        HttpSecurity conf = http
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().headers().addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                .and()
            .csrf().disable()
                ;

        /*PreSecurityFilter preSecurityFilter = getPreSecurityFilter();

        if (preSecurityFilter!=null){
            conf = conf.addFilterBefore(preSecurityFilter, RequestHeaderAuthenticationFilter.class);
        }*/

        conf
            .authorizeRequests()
            .anyRequest().permitAll()
            ;
    }
}
