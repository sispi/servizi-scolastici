package it.kdm.solr.realtime;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import it.kdm.solr.client.CoreClient;
import it.kdm.solr.common.QueryUtils;
import it.kdm.solr.core.Session;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.BytesRefBuilder;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.FieldType;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QParserPlugin;
import org.apache.solr.search.SyntaxError;


import org.apache.lucene.search.*;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import org.apache.solr.common.params.ModifiableSolrParams;


public class RealTimeCloudJoinQParserPlugin extends QParserPlugin {
  public static final String NAME = "acl";
  
  	private transient static Logger log = LoggerFactory.getLogger(RealTimeCloudJoinQParserPlugin.class);
  

  
	@Override
	public QParser createParser( String qstr, SolrParams localParams, SolrParams params, SolrQueryRequest req) {

	return new QParser(qstr, localParams, params, req) {
		
		@Override
		@SuppressWarnings("unchecked")
		public Query parse() throws SyntaxError {
			
			ModifiableSolrParams params = new ModifiableSolrParams(localParams);
			
			String from = params.get("from");
			String to = params.get("to");
			String q = params.get("v").trim();
			
			params.remove("from");
			params.remove("to");
			params.remove("v");
			
			Object response;

            Session session = Session.get(req);

            params.set( "ticket" , session.getTicket() );
            params.set( "shortcut" , true );
			
			int limit = params.getInt("terms.limit" , 1000000);
			
			try
			{	
				//facet method
				if ( q.length() > 0)
				{
					
					params.set( "q" , q );
					params.set( "facet" ,true);
					params.add( "facet.field" , from );
					params.set( "facet.mincount" , 1 );
					params.set( "facet.limit" , limit );
					params.set( "rows" , 0 );
					
					NamedList<Object> nl = CoreClient.getInstance().query( params ).getResponse();
					
					response = nl.findRecursive( "facet_counts" , "facet_fields" , from );
				}
				else //terms
				{
					params.set( "qt" , "/terms");
					params.set( "terms.fl", from );
					params.set( "terms.sort" , "index");
					params.set( "terms.style" , "bytesref" );
					params.set( "terms.limit" , limit );
					params.set( "shards.qt" , "/terms" );
					
					
					NamedList<Object> nl = CoreClient.getInstance().query( params ).getResponse();
					
					response = nl.findRecursive( "terms" , from );
				}
			}
			catch(Exception e )
			{
				throw new RuntimeException(e);
			}

			Query parent_filter;
			
			if (response instanceof BytesRef[])
			{
				BytesRef[] bytesRefs = (BytesRef[]) response;
				
				if (bytesRefs.length==0)
					return new MatchNoDocsQuery();
				
				parent_filter = QueryUtils.Filters.docValuesTermsFilter.makeFilter( to , bytesRefs );
			}
			else
			{
				NamedList<Object> nl = (NamedList<Object>) response;
				
				if (nl.size()==0)
                    return new MatchNoDocsQuery();
				
				FieldType ft = req.getSchema().getFieldType(from);
				
				/*SchemaField sf = req.getSchema().getField(from);
				
				if ( sf.hasDocValues() && ft.getNumericType() == org.apache.lucene.document.FieldType.NumericType.LONG )
				{
					Collection<Long> longs = new ArrayList<>();
					BooleanQuery bq = new BooleanQuery();
					
					for (int i=0; i< nl.size(); i++) {
						long key = Long.parseLong( nl.getName(i) );
						longs.add( key );
						bq.add(  org.apache.lucene.search.NumericRangeQuery.newLongRange(Fields.ACL_SEQUENCE, 100 , key, key, true, true) , BooleanClause.Occur.SHOULD);
				
					}
					return bq;
					//return new ConstantScoreQuery(new KeyFilter( to, longs ) );
				}*/
			
				List<BytesRef> bytesRefs = new ArrayList<>();
				
				for (int i=0; i< nl.size(); i++) {

                    BytesRefBuilder term = new BytesRefBuilder();
					
					String name = nl.getName(i);
					
					ft.readableToIndexed( name , term);
					
					bytesRefs.add( term.toBytesRef() );
				}
				
				parent_filter = QueryUtils.Filters.termsFilter.makeFilter( to , bytesRefs );
			}
			
			return new ConstantScoreQuery(parent_filter);
			
		}
	};
	}
}