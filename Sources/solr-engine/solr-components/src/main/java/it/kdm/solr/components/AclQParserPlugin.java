package it.kdm.solr.components;

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

import it.kdm.solr.core.Schema.Fields;

import it.kdm.solr.common.DocUtils;
import it.kdm.solr.core.Session;
import org.apache.lucene.search.*;

import org.apache.lucene.util.BytesRef;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QParserPlugin;
import org.apache.solr.search.SyntaxError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;


public class AclQParserPlugin extends QParserPlugin {
  public static final String NAME = "acl";
  
  	private transient static Logger log = LoggerFactory.getLogger(AclQParserPlugin.class);
  
  @Override
  public void init(NamedList args) {
  }


  static Query makeAclQuery( SolrQueryRequest req , Collection<String> roles , String ticket )
  {
      if (roles == null || roles.size()==0)
          return new MatchNoDocsQuery();

	
	BytesRef[] refs = DocUtils.bytesRefs(req, Fields.ACL_READ, roles);
	
	//Filter child_filters = new TermsFilter( Fields.ACL_READ , refs );

    Query termsQuery = new TermInSetQuery( Fields.ACL_READ , refs );
	
    return new ConstantScoreQuery( termsQuery );
	
  }
	
  @Override
  public QParser createParser( String qstr, SolrParams localParams, SolrParams params, SolrQueryRequest req) {
    
	return new QParser(qstr, localParams, params, req) {
      @Override
      public Query parse() throws SyntaxError {
        
		Session session = Session.get(req);
		
		if ( (session.getGlobalAcl() & 1) > 0)
			return new org.apache.lucene.search.MatchAllDocsQuery();
		else
			return makeAclQuery(req, session.getRoles(), session.getTicket() );
      }
    };
  }
}