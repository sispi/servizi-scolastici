package it.filippetti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class,
		UserDetailsServiceAutoConfiguration.class, DataSourceAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class })
@EnableAutoConfiguration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class SispiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SispiApplication.class, args);
	}

}
