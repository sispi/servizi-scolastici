package it.filippetti.sispi.spring;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration(proxyBeanMethods = false)
@EnableJpaRepositories(basePackages = "it.filippetti.sispi", entityManagerFactoryRef = "mainEntityManager", transactionManagerRef = "mainTransactionManager")
@EnableTransactionManagement
public class MainEntityManagerFactoryConfiguration extends BaseDataSourceConfiguration {

	@Autowired
	private MainDataSourceProperties mainProps;

	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(mainDataSource());
	}

	@Bean
	@Primary
	public LocalContainerEntityManagerFactoryBean mainEntityManager() {
		final EntityManagerFactoryBuilder builder = createEntityManagerFactoryBuilder();
		final LocalContainerEntityManagerFactoryBean em = builder.dataSource(mainDataSource())
				.packages("it.filippetti.sispi").persistenceUnit("firstDs").properties(additionalProperties()).build();
		return em;
	}

	@Bean
	@Primary
	public DataSource mainDataSource() {
//		return DataSourceBuilder.create().driverClassName(mainProps.getDriverClassName())
//				.username(mainProps.getUsername()).password(mainProps.getPassword()).url(mainProps.getUrl())
//				.type(HikariDataSource.class).build();
//		
		return getDataSource(mainProps);
	}

	@Bean
	@Primary
	public PlatformTransactionManager mainTransactionManager() {
		final JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setPersistenceUnitName("firstDs");
		transactionManager.setEntityManagerFactory(mainEntityManager().getObject());
		return transactionManager;
	}

}
