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
 
package it.kdm.solr.augmenters;

import it.kdm.solr.common.DocUtils;
import it.kdm.solr.core.Schema;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.transform.DocTransformer;
import org.apache.solr.response.transform.TransformerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @since solr 4.0
 */
public class HashAugmenterFactory extends TransformerFactory
{
	protected NamedList args; 

	@Override
	public void init(NamedList args) {
		this.args = args;
   }
  
  
  @Override
  public DocTransformer create(String field, SolrParams params, SolrQueryRequest req) {
    return new HashAugmenter( field, params, req, args );
  }
}

class HashAugmenter extends DocTransformer
{
	static Logger log = LoggerFactory.getLogger(HashAugmenter.class);
	
	final String display;
	
	SolrParams params;
	SolrQueryRequest req;
	
	public static String formatHash( int hash )
	{
		String hex = Integer.toHexString(hash);
	
		hex = String.format("%8s", hex ).replace(' ', '0');
		//String path = hex.substring(0,2) + "-" + hex.substring(2,4) + "-" + hex.substring(4,6) + "-" + hex.substring(6,8) ;
	
		return hex;
	}

  public HashAugmenter( String display , SolrParams params, SolrQueryRequest req, NamedList args )
  {
	this.display = display;
	
	this.req = req;
	this.params = params;
  }

  @Override
  public String getName()
  {
    return display;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void transform(SolrDocument doc, int docid, float score) {
	
	try
	{
		//Schema schema = Schema.get(req);
		
		//if (doc.containsKey( schema.getRouteField() ) )
		//{
			//ContentManager.convertIndexableFields( req, doc, Fields.ID /*, schema.getRouteField()*/ );
		
			//String route = (String) doc.getFieldValue( schema.getRouteField() );
			String id = (String) DocUtils.convertIndexableField(req, doc, Schema.Fields.ID);
			
			//CompositeIdRouter router = schema.getRouter();
			
			int hash = Schema.getRouter().sliceHash( id, null, null, null ) ;
				
			String hex = formatHash(hash);
				
			doc.setField( display, hex );
			
		//}
	}
	catch( Exception e )
	{
		log.error("transform error",e);
	}
	
  }
}


