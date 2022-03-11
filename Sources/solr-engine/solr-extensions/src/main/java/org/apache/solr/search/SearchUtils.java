package org.apache.solr.search;

import org.apache.solr.search.JoinQuery;
import org.apache.lucene.search.Query;

import org.apache.solr.common.util.NamedList;
import org.apache.solr.response.JSONResponseWriter;
import org.apache.solr.request.LocalSolrQueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.common.params.ModifiableSolrParams;
import java.io.StringReader;
import java.io.StringWriter;
import  org.apache.solr.request.SolrQueryRequestBase;
import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.apache.log4j.Priority;
import org.slf4j.LoggerFactory;

public class SearchUtils
{
	public static Query getJoinQuery( Query q )
	{
        JoinQuery jq = (JoinQuery) q ;

		return jq.getQuery();
	}
	
	public static String getJoinFromField( Query q )
	{
		JoinQuery jq = (JoinQuery) q ;

		return jq.fromField;
	}
	
	public static String getJoinToField( Query q )
	{
		JoinQuery jq = (JoinQuery) q ;

		return jq.toField;
	}
	
	public static String getJoinFromIndex( Query q )
	{
		JoinQuery jq = (JoinQuery) q ;

		return jq.fromIndex;
	}
	
	public static boolean isJoinQuery( Query q )
	{
		return (q instanceof JoinQuery);
	}

	

	
}