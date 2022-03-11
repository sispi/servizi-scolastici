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
public class ShardAugmenterFactory extends TransformerFactory
{
	protected NamedList args; 

	@Override
	public void init(NamedList args) {
		this.args = args;
   }
  
  
  @Override
  public DocTransformer create(String field, SolrParams params, SolrQueryRequest req) {
    return new ShardAugmenter( field, params, req, args );
  }
}

class ShardAugmenter extends DocTransformer
{
	static Logger log = LoggerFactory.getLogger(ShardAugmenter.class);
	
	private final String display;
	private final SolrQueryRequest req;
	

  public ShardAugmenter( String display , SolrParams params, SolrQueryRequest req, NamedList args )
  {
	this.display = display;
	this.req = req;
	
  }

  @Override
  public String getName()
  {
    return display;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void transform(SolrDocument doc, int docid, float score) {
	
	String shard = req.getCore().getCoreDescriptor().getCloudDescriptor().getShardId() ;
	doc.setField( display, shard );
	
  }
}


