package it.kdm.solr.interfaces;

import org.apache.solr.request.SolrQueryRequest;

import java.util.Map;

public interface IProvider {
	public void setConfig( Map<String,Object> config );
	public void setRequest( SolrQueryRequest req );
	public String getName();
}
