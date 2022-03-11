package it.kdm.docer.alfresco.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.webservice.types.Reference;
import org.w3c.dom.Element;

import it.kdm.docer.sdk.EnumACLRights;
import it.kdm.docer.sdk.classes.LockStatus;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.utils.DataRow;
import it.kdm.utils.DataTable;
import it.kdm.utils.exceptions.DataTableException;

public class SearchResultSet {

	private List<SearchResult> searchResultSet = new ArrayList<SearchResult>();

	private DataTable<String> dataTable = new DataTable<String>();

	private int count = 0;
		
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public SearchResult get(Reference nodeRef) {

		for (SearchResult rs : searchResultSet) {
			if (rs.getReference().equals(nodeRef)) {
				return rs;
			}
		}

		return null;
	}

	public SearchResult get(DocId id) {

		for (SearchResult rs : searchResultSet) {
			if (rs.getDocId().equals(id.toString())) {
				return rs;
			}
		}

		return null;
	}

	public SearchResult getFirstAndOnly() throws DocerException {
		
		if (searchResultSet.size() == 1) {
			return searchResultSet.get(0);
		}

		throw new DocerException("SearchResultSet.getFirstAndOnly() ha restituito " +searchResultSet.size() +" risultati");
	}

//	public List<SearchResult> getAll() {
//
//		return searchResultSet;
//	}

	public List<SearchResult> getAllSearchResult() {

		return this.searchResultSet;
	}
		
	public void addSearchResult(SearchResult searchResult) throws DocerException {
	
		this.searchResultSet.add(searchResult);		
	}
	
//	public DataTable<String> getDatatable() {
//		return dataTable;
//	}

	public DataTable<String> getResultsAsDatatable() {
		return dataTable;
	}
		
	public void setDatatable(DataTable<String> dt) {
		this.dataTable= dt ;
	}
	
	public boolean containsDocId(String docId) {

		for (SearchResult sr : searchResultSet) {

			if (sr.getDocId().equals(docId)) {
				return true;
			}

		}
		return false;
	}

}