package keysuite.desktop.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Strings;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import keysuite.desktop.components.UrlRewriter;
import keysuite.desktop.proxy.ProxyFilter;
import keysuite.desktop.security.Keycloak.KeycloakAuthenticationFilter;
import keysuite.desktop.security.Keycloak.PathBasedKeycloakConfigResolver;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import static org.springframework.security.web.server.header.ClearSiteDataServerHttpHeadersWriter.CLEAR_SITE_DATA_HEADER;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    //static final String STATIC_PATTERNS = "/resources";
    //static final String PUBLIC_PATTERNS = "/error,/auth/*,/public/*,/routes,/filters";
    public static final String STATIC_PATTERNS = "/favicon.ico";
    public static final String PUBLIC_PATTERNS = "/public/**";
    public static final String AUTHORIZED_PATTERNS = "/private/**";

    //static final String ADMIN_PATTERNS =  "/admins/*";

    @Autowired
    private Environment env;

    @Autowired
    private KeycloakSpringBootProperties keycloakSpringBootProperties;

//    SecurityFilter securityFilter;

    @Autowired
    DiscoveryClientRouteLocator discoveryClientRouteLocator;

    @Autowired
    UrlRewriter urlRewriter;

    private SecurityFilter securityFilter;
    //private 'PreSecurityFilter preSecurityFilter;

//    @Bean(name = "securityFilter")
    public SecurityFilter getFilter() {
        if (securityFilter==null)
            securityFilter = new SecurityFilter(env,discoveryClientRouteLocator,urlRewriter);
        return securityFilter;
    }

    /*KeycloakAuthenticationFilter getKeycloakAuthenticationFilter(){
        KeycloakAuthenticationFilter keycloakAuthenticationFilter = null;
        InputStream securityConfigYaml =  ResourceUtils.getResourceNoExc("SecurityConfiguration.yaml");
        Map<String, Object> securityConfigMap = null;
        try{
            securityConfigMap = readYaml(securityConfigYaml);
        }catch (Exception e ){
            logger.error("Errore nella lettura della Security Config");
        }
        if(securityConfigMap != null && !securityConfigMap.isEmpty()) {
            keycloakAuthenticationFilter = new KeycloakAuthenticationFilter(env, securityConfigMap);
        }
        return keycloakAuthenticationFilter;
    }*/

    private Map<String, Object> readYaml(InputStream stream) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        return (Map<String, Object>) mapper.readValue(stream, Map.class);
    }


    ProxyFilter getProxy(){
        return new ProxyFilter();
    }

    /*ProxyMenu getProxyMenu(){
        return new ProxyMenu();
    }*/

    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        if (isKeycloakEnabled()){

            configureKeyCloak(http);

        } else {

            http
                    .cors().and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and().headers().addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                    .and()
                    .csrf().disable()
                    .addFilterBefore(getFilter(), RequestHeaderAuthenticationFilter.class);

            /*PreSecurityFilter pre = getPreSecurityFilter();
            if(pre != null){
                http.addFilterBefore(pre, SecurityFilter.class);
            }*/
        }
    }


    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedPercent(true);;
        firewall.setAllowSemicolon(true);
        return firewall;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        // @formatter:off
        web.httpFirewall(allowUrlEncodedSlashHttpFirewall());
    }

    /************ KEYCLOAK *******************/

    boolean isKeycloakEnabled(){
        return keycloakSpringBootProperties.isEnabled() && keycloakSpringBootProperties.getConfig().size()>0;
    }

    protected void configureKeyCloak(final HttpSecurity http) throws Exception {
        super.configure(http);

        http.logout()
                .addLogoutHandler(
                        (request, response, authentication) -> {SecurityHelper.cleanSession(response);}
                 )
                .logoutUrl("/auth/logout").permitAll()
                .addLogoutHandler(new LogoutHandler() {
                    @Override
                    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
                        response.setHeader(CLEAR_SITE_DATA_HEADER, "\"cookies\"");
                    }
                })
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

                        if (response.isCommitted()){
                            return;
                        }

                        String requestURL = request.getParameter("requestURL");

                        if (Strings.isNullOrEmpty(requestURL))
                            requestURL = request.getParameter("redirect_uri");
                        if (Strings.isNullOrEmpty(requestURL))
                            requestURL = "/";

                        requestURL = response.encodeURL(requestURL);

                        response.sendRedirect(requestURL);
                    }
                })
                .logoutSuccessUrl("/");

        http
                .cors().and()
                .csrf().disable()
                .addFilterAfter(getFilter(), KeycloakAuthenticationFilter.class)
                //.cors()
                //.and()
                .authorizeRequests()
                .anyRequest()
                .permitAll();
        //.anyRequest().authenticated();
        //.antMatchers("/*").hasRole("ADMIN");

        /*PreSecurityFilter pre = getPreSecurityFilter();
        if(pre != null){
            http.addFilterBefore(pre, SecurityFilter.class);
        }*/

        System.setProperty("keycloakFilter", "enabled");
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new     UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin(CorsConfiguration.ALL);
        config.addAllowedHeader(CorsConfiguration.ALL);
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);

        return source;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        if (isKeycloakEnabled()) {
            KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
            keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
            auth.authenticationProvider(keycloakAuthenticationProvider)
                    .userDetailsService(new InMemoryUserDetailsManager())
                    .passwordEncoder(new BCryptPasswordEncoder());
        }
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        //return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
        return (authentication, request, response) -> {
            /*if (authentication instanceof KeycloakAuthenticationToken) {
                logger.error("auth:" + authentication.getPrincipal());

                KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) authentication;

                OidcKeycloakAccount account = token.getAccount();
                KeycloakPrincipal principal = (KeycloakPrincipal) account.getPrincipal();
                KeycloakSecurityContext ksc = account.getKeycloakSecurityContext();

                String jwtToken = ksc.getTokenString();
                IDToken idToken = ksc.getIdToken();

                try{
                    Map<String,Object> IDClaims = DesktopUtils.parseJson(DesktopUtils.toJson(idToken));
                    IDClaims.put("principal", principal );
                    request.setAttribute("IDClaims" , IDClaims);
                } catch (Exception exc) {
                    throw new RuntimeException(exc);
                }

            }*/
        };
    }

    @Bean
    @Primary
    public KeycloakConfigResolver KeycloakConfigResolver(){
        PathBasedKeycloakConfigResolver pathBasedKeycloakConfigResolver = new PathBasedKeycloakConfigResolver();
        return pathBasedKeycloakConfigResolver;
    }

    @Bean
    @Override
    protected KeycloakAuthenticationProcessingFilter keycloakAuthenticationProcessingFilter() throws Exception {
        KeycloakAuthenticationFilter filter = new KeycloakAuthenticationFilter(authenticationManagerBean());
        filter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy());
        filter.setEnabled(isKeycloakEnabled());
        filter.setEnvironment(env);
        filter.setSecurityFilter(getFilter());
        filter.setConfig(keycloakSpringBootProperties);
        filter.setRouteLocator(discoveryClientRouteLocator);
        return filter;
    }
}