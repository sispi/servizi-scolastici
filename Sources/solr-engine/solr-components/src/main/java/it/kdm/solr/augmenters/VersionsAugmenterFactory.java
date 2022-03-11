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

import it.kdm.solr.core.Schema;
import it.kdm.solr.core.ProviderProxy;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.transform.DocTransformer;
import org.apache.solr.response.transform.TransformerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 *
 * @since solr 4.0
 */
public class VersionsAugmenterFactory extends TransformerFactory
{
	protected NamedList args; 

	@Override
	public void init(NamedList args) {
		this.args = args;
   }
  
  
  @Override
  public DocTransformer create(String field, SolrParams params, SolrQueryRequest req) {
    return new VersionsAugmenter( field, params, req, args );
  }
}

class VersionsAugmenter extends DocTransformer
{
	static Logger log = LoggerFactory.getLogger(VersionsAugmenter.class);
	
	private final String display;
	private SolrQueryRequest req;

  public VersionsAugmenter( String display , SolrParams params, SolrQueryRequest req, NamedList args )
  {
	this.display = display;
	this.req = req;
	
  }

  @Override
  public String getName()
  {
    return display;
  }


  @SuppressWarnings("unchecked")
  public void transform(SolrDocument doc, int docid, float score) {
	
	Schema schema = Schema.get(req);
	
	Collection<Integer> versions = new ProviderProxy(doc,req).listVersions();
	
	doc.setField( display, versions );
	
  }
}


