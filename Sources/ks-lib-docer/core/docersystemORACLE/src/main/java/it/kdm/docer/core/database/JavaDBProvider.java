/*
package it.kdm.docer.core.database;

import com.mysql.jdbc.NotImplemented;
import it.kdm.docer.commons.ResultSetWrapper;
import it.kdm.docer.core.tracer.Tracer.TYPE;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.lang.NotImplementedException;

import org.slf4j.Logger;

public class JavaDBProvider extends IDatabaseProvider {
	
	//private final String FRAMEWORK = "embedded";
	private final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	private final String DATABASE_NAME = "TestDB";
	private final String PROTOCOL = "jdbc:derby:";
	
	//private final String USERNAME = "admin";
	//private final String PASSWORD = "admin";
	
	private Connection conn = null;
	
	Logger logger = org.slf4j.LoggerFactory.getLogger(JavaDBProvider.class);
	
	public JavaDBProvider() throws SQLException {
		try {
			Class.forName(DRIVER).newInstance();
		} catch (InstantiationException e) {
			logger.error(e.getMessage(), e);
			throw new SQLException(e.getMessage());
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
			throw new SQLException(e.getMessage());
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
			throw new SQLException(e.getMessage());
		}
	}
	
	@Override
	public void openConnection() throws SQLException {
		try {
			if (conn == null || conn.isClosed()) {
				//Properties props = new Properties();
				//props.put("user", USERNAME);
				//props.put("password", PASSWORD);
				conn = DriverManager.getConnection(PROTOCOL + DATABASE_NAME
						+ ";create=true");//, props);
				
				if(!isInitialized()) {
					initialize();
				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	private boolean isInitialized() throws SQLException {
		ResultSet set = null;
		try {
			DatabaseMetaData metadata = conn.getMetaData();
			 set = metadata.getTables(null, null, "%", null);
			
			while(set.next()) {
				String tableName = set.getString(3); //Table Name
				if(tableName.equalsIgnoreCase("tracer")) {
					return true;
				}
			}
		} finally {
			if (set != null) {
				set.close();
			}
		}
		
		return false;
	}
	
	private void initialize() throws SQLException {
		
		Statement s = null;
		try {
			s = conn.createStatement();
			s.execute("create table tracer(message VARCHAR(5000), level VARCHAR(100), type int)");
		} finally {
			if(s != null) {
				s.close();
			}
		}
	}

	@Override
	public void closeConnection() throws SQLException {
		try {
			if(conn != null && !conn.isClosed()) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			e.getSQLState();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	@Override
	public boolean isConnected() throws SQLException {
		try {
			return conn != null && !conn.isClosed();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}
	
	@Override
	public void saveTracerLog(String message, String serviceName, String methodName, String level, TYPE type) throws SQLException {
		
		PreparedStatement s = null;
		
		try {
			s = conn.prepareStatement("insert into tracer values (?, ?, ?)");
			s.setString(1, message);
			s.setString(2, level);
			s.setInt(3, type.getValue());
			s.execute();
		} catch(SQLException e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			if(s != null) {
				s.close();
			}
		}
	}

	@Override
	public ResultSetWrapper readTracerLog() throws SQLException {
		throw new NotImplementedException();
	}

	@Override
	public void shutdown() throws SQLException {
		try {
			conn = DriverManager.getConnection(PROTOCOL + DATABASE_NAME
					+ ";shutdown=true");
			conn.close();
		} catch (SQLException e) {
			// JavaDB always throws an exception when closing
			logger.info(e);
		}
	}

}
*/