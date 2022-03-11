package it.filippetti.sispi.spring;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration(proxyBeanMethods = false)
@EnableJpaRepositories(basePackages = "it.filippetti.psso", entityManagerFactoryRef = "pssoEntityManager", transactionManagerRef = "pssoTransactionManager")
@EnableTransactionManagement
public class PssoEntityManagerFactoryConfiguration extends BaseDataSourceConfiguration {

	@Autowired
	private PssoProperties pssoProps;

	@Bean
	public LocalContainerEntityManagerFactoryBean pssoEntityManager() {
		final EntityManagerFactoryBuilder builder = createEntityManagerFactoryBuilder();
		final LocalContainerEntityManagerFactoryBean em = builder.dataSource(pssoDataSource())
				.packages("it.filippetti.psso").persistenceUnit("pssoDs").properties(additionalProperties()).build();

		return em;
	}

	@Bean
	public DataSource pssoDataSource() {
		return getDataSource(pssoProps);
	}

	@Bean
	public PlatformTransactionManager pssoTransactionManager() {
		final JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(pssoEntityManager().getObject());
		transactionManager.setPersistenceUnitName("pssoDs");
		return transactionManager;
	}

}
