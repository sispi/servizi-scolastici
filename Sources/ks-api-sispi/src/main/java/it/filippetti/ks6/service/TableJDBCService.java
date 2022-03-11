package it.filippetti.ks6.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableJDBCService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> findAll(String entity) {
		return findAll(entity, null, null, -1);
	}

	public List<Map<String, Object>> findAll(String entity, String where) {
		return findAll(entity, where, null, -1);
	}

	public List<Map<String, Object>> findAll(String entity, String where, String orderBy) {
		return findAll(entity, where, orderBy, -1);
	}

	public List<Map<String, Object>> findAll(String entity, String where, String orderBy, Integer maxRows) {
		String query = "SELECT * FROM " + entity;
		if (where != null) {
			query += " WHERE " + where;
		}
		if (orderBy != null) {
			query += " ORDER BY " + orderBy;
		}
		jdbcTemplate.setMaxRows(-1);
		if (maxRows != null) {
			jdbcTemplate.setMaxRows(maxRows);
		}
		return jdbcTemplate.queryForList(query);
	}
}
