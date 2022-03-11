package it.kdm.docer.fascicolazione.batch.persistence;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ResultSetMap extends HashMap<String, String> {

	private static final long serialVersionUID = 1L;

	public ResultSetMap(ResultSet rs, String... fieldNames) throws SQLException {
		List<String> fieldNameList = Arrays.asList(fieldNames);
		ResultSetMetaData rsmd = rs.getMetaData();
		for (int i = 1, l = rsmd.getColumnCount(); i <= l; i++) {
			String name = rsmd.getColumnName(i);
			if (fieldNameList.contains(name)) {
				Object value = rs.getObject(i);
				if (value != null)
					put(name, value.toString());
			}
		}
	}

    public ResultSetMap(ResultSet rs, String[] fieldNames, boolean avoidFields) throws SQLException {
        List<String> fieldNameList = Arrays.asList(fieldNames);
        ResultSetMetaData rsmd = rs.getMetaData();

		for (int i = 1, l = rsmd.getColumnCount(); i <= l; i++) {
            String name = rsmd.getColumnName(i);
            if (!fieldNameList.contains(name)) {
                Object value = rs.getObject(i);
				if (value != null)
					put(name, value.toString());
            }
        }
    }


}
