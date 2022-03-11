package it.kdm.docer.fascicolazione.batch.persistence;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import it.kdm.docer.fascicolazione.batch.Configuration;

public class ConnectionManager {

	public static final ConnectionManager INSTANCE = new ConnectionManager();

	private String url;
	private String user;
	private String password;

	private Connection c;

	public Query getQuery(String sql) throws SQLException {
		return new Query(getConnection(), sql);
	}	
	
	public Query getQuery(String sql, boolean updatableResult) throws SQLException {
		return new Query(getConnection(), sql, updatableResult);
	}
	
	public void closeConnection() throws SQLException {
		if (c != null && !c.isClosed())
			c.close();
		//getConnection().close();
	}
	
	public void beginTransaction() throws SQLException {
		getConnection().setAutoCommit(false);
	}

	public void commitTransaction() throws SQLException {
		getConnection().commit();
	}

	public void rollbackTransaction() throws SQLException {
		getConnection().rollback();
	}
	
	public Connection getConnection() throws SQLException {
		if (c == null || c.isClosed()) {
			c = DriverManager.getConnection(url, user, password);
		}
        c.setAutoCommit(false);
		return c;
	}

	private ConnectionManager() {
		try {
			Driver driver = (Driver) Class.forName(Configuration.getInstance().getProperty("rdbms.driver"))
					.newInstance();
			DriverManager.registerDriver(driver);
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid driver", e);
		}
		url = Configuration.getInstance().getProperty("rdbms.url");
		user = Configuration.getInstance().getProperty("rdbms.user");
		password = Configuration.getInstance().getProperty("rdbms.password");
	}

}
