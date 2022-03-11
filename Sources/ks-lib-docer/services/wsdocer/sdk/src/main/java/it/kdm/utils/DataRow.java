package it.kdm.utils;

import it.kdm.utils.exceptions.DataTableException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DataRow<T> implements Iterable<String> {
	private Map<String, T> value;
	
	protected DataTable<T> parentDataTable;
	
	protected DataRow(List<String> columns, DataTable<T> dt) {
		
		this.parentDataTable = dt;
		
		value = new HashMap<String, T>();
		
		for(String col : columns) {
			value.put(col, null);
		}
	}

	protected DataRow(List<String> columns, DataTable<T> dt, T defaultColumnValue) {
		
		this.parentDataTable = dt;
		
		value = new HashMap<String, T>();
		
		for(String col : columns) {
			value.put(col, defaultColumnValue);
		}
	}
	
	public void put(String nomeColonna, T value) throws DataTableException {
		if(!this.value.containsKey(nomeColonna))
			throw new DataTableException("Column " + nomeColonna + " not defined in this DataRow");
		
		this.value.put(nomeColonna, value);
	}
	
	public T get(String nomeColonna) {
		return this.value.get(nomeColonna);
	}
	
	public int getSize() {
		return this.value.size();
	}
	
	public Collection<T> getValues() {
		return this.value.values();
	}
	
	public String toString() {
		return this.value.toString();
	}

	public Iterator<String> iterator() {
		return value.keySet().iterator();
	}
	
	public Document getXml() throws DataTableException {
		return this.getXml("row");
	}
	
	public Document getXml(String rootName) throws DataTableException {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = builder.newDocument();
			
			Element row = document.createElement(rootName);
			document.appendChild(row);
			
			for(String columnName : this.parentDataTable.getColumnNames()) {
				Element column = document.createElement(columnName);
				Object value = this.get(columnName);
				if(value != null) {
					column.setTextContent(value.toString());
				}
				row.appendChild(column);
			}
			
			return document;
			
		} catch (Exception e) {
			throw new DataTableException(e);
		}
	}
}
