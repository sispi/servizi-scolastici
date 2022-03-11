package it.kdm.utils;

import it.kdm.utils.exceptions.DataTableException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * DataTable implementation that mimics the behaviour of the corresponding C# class
 * @author Lorenzo Lucherini
 *
 * @param <T>
 */
public class DataTable<T> implements Iterable<DataRow<T>> {
	
	private List<String> columns; 
	private List<DataRow<T>> rows;
	
	public DataTable() {
		this.columns = new ArrayList<String>();
		this.rows = new ArrayList<DataRow<T>>(); 
	}
	
	public void addColumn(String column) {
		this.columns.add(column);
	}
	
	public String getColumn(int index) {
		return this.columns.get(index);
	}
	
	public Collection<String> getColumnNames() {
		return this.columns;
	}
	
	public void addRow(DataRow<T> row) throws DataTableException {
		if(row.parentDataTable != this)
			throw new DataTableException("DataRow created with a DataTable different than this");
		this.rows.add(row);
	}
	
	public DataRow<T> newRow() {
		return new DataRow<T>(this.columns, this);
	}
	
	public DataRow<T> newRow(T defaultColumnValue) {
		return new DataRow<T>(this.columns, this, defaultColumnValue);
	}
	
	public DataRow<T> getRow(int index) {
		return this.rows.get(index);
	}
	
	public Collection<DataRow<T>> getRows() {
		return rows;
	}
	
	public String toString() {
		return this.rows.toString();
	}

	public Iterator<DataRow<T>> iterator() {
		return this.rows.iterator();
	}
	
	public Document getXml() throws DataTableException {
		return this.getXml("table", "row");
	}

	public Document getXml(String rootName, String rowName) throws DataTableException {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = builder.newDocument();
			
			Element table = document.createElement(rootName);
			
				for(DataRow<T> dataRow : this.rows) {
					Element row = document.createElement(rowName);
					table.appendChild(row);
					
					for(String columnName : this.getColumnNames()) {
						Element column = document.createElement(columnName);
						T value = dataRow.get(columnName); 
						if(value != null) {
							column.setTextContent(dataRow.get(columnName).toString());
						}
						row.appendChild(column);
					}
				}
			
			document.appendChild(table);

			return document;
			
		} catch (Exception e) {
			throw new DataTableException(e);
		}
	}
}
