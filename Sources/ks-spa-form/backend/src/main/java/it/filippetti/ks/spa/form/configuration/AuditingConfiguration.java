/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.configuration;

import it.filippetti.ks.spa.form.authentication.AuthenticationContextHolder;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.AuthenticationException;

/**
 *
 * @author marco.mazzocchetti
 */
@Configuration
public class AuditingConfiguration {
    
    @Autowired
    ApplicationProperties applicationProperties;
    
    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAware<String>() {
            @Override
            public Optional<String> getCurrentAuditor() {
                try {
                    return Optional.of(AuthenticationContextHolder.get().getUserId());
                } catch (AuthenticationException e) {
                    return Optional.of(applicationProperties.systemUser());
                }
            }
        };
    }
}
