/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks6.configuration;

import it.filippetti.ks6.authentication.AuthenticationEntryPoint;
import it.filippetti.ks6.authentication.AuthenticationFilter;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 *
 * @author marco.mazzocchetti
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationEntryPoint authenticationEntryPoint;

	@Override
	public void configure(WebSecurity webSecurity) throws Exception {

		webSecurity.ignoring()
                        // actuator
                        .antMatchers(
                            "/actuator/**")
                        // swagger
                        .antMatchers(
                            "/v2/api-docs",
                            "/configuration/ui",
                            "/swagger-resources/**",
                            "/configuration/security",
                            "/swagger-ui.html",
                            "/swagger-ui/**",
                            "/webjars/**");                                    
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {

		httpSecurity
                        .csrf().disable()
                        .formLogin().disable()
                        .httpBasic().disable()
                        .cors().and()
                        .authorizeRequests().anyRequest().authenticated().and()
                        .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and()
                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                        .addFilterAfter(new AuthenticationFilter(), ExceptionTranslationFilter.class);
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("*"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManager() throws Exception {
		// required to prevent spring boot auto-configuration
		return super.authenticationManagerBean();
	}
}
