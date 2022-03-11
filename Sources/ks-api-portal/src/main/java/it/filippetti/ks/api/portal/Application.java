package it.filippetti.ks.api.portal;

import it.filippetti.ks.api.portal.repository.BaseRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {
    SecurityAutoConfiguration.class,
    ManagementWebSecurityAutoConfiguration.class,
    UserDetailsServiceAutoConfiguration.class
})
@EnableAutoConfiguration
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class)
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
