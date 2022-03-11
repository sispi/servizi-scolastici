package it.kdm.utils.json;

import it.kdm.utils.DataRow;
import it.kdm.utils.DataTable;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class StringDataTableSerializer implements JsonSerializer<DataTable<String>> {

	public JsonElement serialize(DataTable<String> src, Type typeOfSrc,
			JsonSerializationContext context) {
		
		JsonArray table = new JsonArray();
		
		JsonArray columnNames = new JsonArray();
		for(String columnName : src.getColumnNames()) {
			columnNames.add(new JsonPrimitive(columnName));
		}
		table.add(columnNames);
		
		JsonArray rows = new JsonArray();
		for(DataRow<String> row : src.getRows()) {
			JsonObject jsonRow = new JsonObject();
			for(String column : src.getColumnNames()) {
				String value = row.get(column);
				if(value != null) {
					jsonRow.add(column, new JsonPrimitive(value));
				}
			}
			rows.add(jsonRow);
		}
		table.add(rows);
		
		return table;
	}

}
