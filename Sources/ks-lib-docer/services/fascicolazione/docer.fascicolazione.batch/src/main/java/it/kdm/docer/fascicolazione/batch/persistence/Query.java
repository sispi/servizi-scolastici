package it.kdm.docer.fascicolazione.batch.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Query {

	private PreparedStatement _ps;
	private ResultSet _rs;
	private int _updateCount;
	private boolean _udpate;

	public Query(Connection c, String sql) throws SQLException {
		this(c, sql, false);
	}

	public Query(Connection c, String sql, boolean updatableResult) throws SQLException {
		_udpate = !sql.toLowerCase().trim().startsWith("select");
		if (updatableResult) {
			_ps = c.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		} else {
			_ps = c.prepareStatement(sql);
		}
	}

	public void close() {
		closeResultSet();
		if (_ps != null) {
			try {
				_ps.close();
			} catch (SQLException e) {
			}
		}
	}

	public ResultSet getResult() {
		return _rs;
	}

	public int getUpdateCount() {
		return _updateCount;
	}

	public Query execute() throws SQLException {
		closeResultSet();
		if (_udpate) {
			_updateCount = _ps.executeUpdate();
		} else {
			_rs = _ps.executeQuery();
		}
		return this;
	}

	public Query setMaxResults(int max) throws SQLException {
		_ps.setMaxRows(max);
		return this;
	}

	public Query setParameter(int index, Object value) throws SQLException {
		_ps.setObject(index, value);
		return this;
	}

	public Query setParameter(int index, Object value, int jdbcType) throws SQLException {
		_ps.setObject(index, value, jdbcType);
		return this;
	}

	public Query setParameter(int index, Object value, int jdbcType, int scale) throws SQLException {
		_ps.setObject(index, value, jdbcType, scale);
		return this;
	}

	private void closeResultSet() {
		if (_rs != null) {
			try {
				_rs.close();
			} catch (SQLException e) {
			}
		}
	}

}
