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
import it.kdm.solr.core.ContentCacheManager;
import it.kdm.solr.core.Schema;
import org.apache.solr.response.transform.*;


import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

import org.apache.solr.common.util.NamedList;

/**
 *
 * @since solr 4.0
 */
public class ParentIdsAugmenterFactory extends TransformerFactory
{
	protected NamedList args; 
	
	static Logger log = LoggerFactory.getLogger(ParentIdsAugmenterFactory.class);

	@Override
	public void init(NamedList args) {
		this.args = args;
	}
  
  
	@Override
	public DocTransformer create(String field, SolrParams params, SolrQueryRequest req) {
		return new ParentIdsAugmenter( field, params, req, args );
	}
  
  	
}

class ParentIdsAugmenter extends DocTransformer
{
	static Logger log = LoggerFactory.getLogger(ParentIdsAugmenterFactory.class);
	
	final String display;
	SolrQueryRequest req;

  public ParentIdsAugmenter( String display , SolrParams params, SolrQueryRequest req, NamedList args )
  {
		this.req = req;
		this.display = display;
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
		String id = (String) DocUtils.convertIndexableField(req, doc, Schema.Fields.ID);
		
		List<String> parentIds = ContentCacheManager.getInstance().getParents( req , id );
		
		//Collection<Object> parentIds = ParentIdsAugmenterFactory.getParentIds( req,  (String) doc.getFieldValue(Schema.Fields.ID) ,null);
		doc.setField( display, parentIds );
	}
	catch(Exception e)
	{
		log.error("error",e);
	}
	
  }
}


