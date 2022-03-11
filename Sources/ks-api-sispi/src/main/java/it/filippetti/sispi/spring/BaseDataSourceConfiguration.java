package it.filippetti.sispi.spring;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.zaxxer.hikari.HikariDataSource;

public abstract class BaseDataSourceConfiguration {

	@Autowired
	private JpaProperties jpaProperties;

	protected Map<String, String> additionalProperties() {
		return jpaProperties.getProperties();
	}

	protected EntityManagerFactoryBuilder createEntityManagerFactoryBuilder() {
		final JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		return new EntityManagerFactoryBuilder(jpaVendorAdapter, jpaProperties.getProperties(), null);
	}

	protected DataSource getDataSource(BaseDataSourceProperties dsProps) {
		return DataSourceBuilder.create().driverClassName(dsProps.getDriverClassName()).username(dsProps.getUsername())
				.password(dsProps.getPassword()).url(dsProps.getUrl()).type(HikariDataSource.class).build();
	}
}
