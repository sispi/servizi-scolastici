package it.filippetti.ks.api.bpm;

import it.filippetti.ks.api.bpm.repository.BaseRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {
    SecurityAutoConfiguration.class,
    ManagementWebSecurityAutoConfiguration.class,
    UserDetailsServiceAutoConfiguration.class,
    FreeMarkerAutoConfiguration.class,
    JmsAutoConfiguration.class,
    ActiveMQAutoConfiguration.class,
    SolrAutoConfiguration.class
})
@EnableAutoConfiguration
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class)
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
