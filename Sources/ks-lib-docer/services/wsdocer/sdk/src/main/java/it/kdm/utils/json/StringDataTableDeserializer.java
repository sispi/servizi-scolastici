package it.kdm.utils.json;

import java.lang.reflect.Type;

import it.kdm.utils.DataRow;
import it.kdm.utils.DataTable;
import it.kdm.utils.exceptions.DataTableException;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class StringDataTableDeserializer implements JsonDeserializer<DataTable<String>> {

	public DataTable<String> deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		
		try {
			DataTable<String> dataTable = new DataTable<String>();
			
			JsonArray table = json.getAsJsonArray();
			JsonArray columnNames = table.get(0).getAsJsonArray();
			
			for(JsonElement elem : columnNames) {
				dataTable.addColumn(elem.getAsString());
			}
			
			JsonArray rows = table.get(1).getAsJsonArray();
			for(JsonElement row : rows) {
				DataRow<String> dataRow = dataTable.newRow();
				JsonObject rowObj = row.getAsJsonObject();
				
				for(String columnName : dataTable.getColumnNames()) {
					if(rowObj.has(columnName)) {
						dataRow.put(columnName, rowObj.get(columnName).getAsString());
					}
				}
				
				dataTable.addRow(dataRow);
			}
			
			
			return dataTable;
		} catch (DataTableException e) {
			throw new JsonParseException(e);
		}
	}

}
