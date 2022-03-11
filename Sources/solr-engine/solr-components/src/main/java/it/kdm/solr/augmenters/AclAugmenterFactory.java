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

import com.google.common.base.Strings;
import it.kdm.solr.client.CoreClient;
import it.kdm.solr.common.DocUtils;
import it.kdm.solr.common.FieldUtils;
import it.kdm.solr.core.ContentCacheManager;
import it.kdm.solr.core.Schema;
import it.kdm.solr.core.Schema.Fields;
import it.kdm.solr.core.Schema.Params;
import it.kdm.solr.core.Session;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.StrUtils;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.transform.DocTransformer;
import org.apache.solr.response.transform.TransformerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

//import org.apache.solr.handler.component.RealTimeGetComponent;

/**
 *
 * @since solr 4.0
 */
public class AclAugmenterFactory extends TransformerFactory
{
	protected NamedList args;

    static Logger log = LoggerFactory.getLogger(AclAugmenterFactory.class);
	
	//private static final String ACL_CACHE = "aclCache";

	@Override
	public void init(NamedList args) {
		this.args = args;
	}
  
  
	@Override
	public DocTransformer create(String field, SolrParams params, SolrQueryRequest req) {
		return new AclAugmenter( field, params, req, args );
	}

    public static Map<String, Object> getInherited( SolrDocument doc ) throws SolrServerException
    {
        Map<String, Object> map = new HashMap<>();

        String id = (String) doc.getFieldValue( Fields.ID );

        Boolean inherits = FieldUtils.parseBool(doc.getFieldValue(Fields.ACL_INHERITS), false);

        Object acls = doc.getFieldValues( Fields.ACL_EXPLICIT );

        if (acls!=null)
            map.put( id, acls );

        SolrDocument doc1 = doc;

        CoreClient client = CoreClient.getInstance();

        while (inherits)
        {
            String parent = (String) doc1.getFieldValue(Fields.PARENT);

            if (Strings.isNullOrEmpty(parent))
                break;

            SolrQuery params = new SolrQuery();
            params.set( Params.TICKET , Session.ROOTTICKET );
            params.set( Params.SHORTCUT , true );
            params.set( Fields.ID , parent );
            //params.set( ShardParams._ROUTE_ , (String) doc1.getFieldValue( schema.getRouteField() ) );
            params.setFields( Fields.COMMON_FIELDS );

            doc1 = client.get( params );

            inherits = FieldUtils.parseBool(doc1.getFieldValue(Fields.ACL_INHERITS), false);

            acls = doc1.getFieldValues( Fields.ACL_EXPLICIT );

            if (acls != null)
                map.put( parent, acls );
        }

        Collection<Object> acl_parents = doc.getFieldValues( Fields.ACL_PARENTS );

        if (acl_parents != null)
        {
            List<Object> ids = new ArrayList<>( acl_parents );

            if (ids.size()>0){
                SolrQuery params = new SolrQuery();
                params.set("ticket" , Session.ROOTTICKET );
                params.set( "ids" , StrUtils.join( ids , ',' ) );
                params.setFields( Fields.ID, Fields.ACL_EXPLICIT );
                params.set( Params.SHORTCUT , true );
                params.set( "qt" , "/get" );
                params.set( "rows" , 1000 );

                SolrDocumentList parents = null;
                try {
                    parents = CoreClient.getInstance().query( params ).getResults();
                } catch (IOException e) {
                    log.error("error getting acl_parents",e);
                    return map;
                }

                for ( SolrDocument p : parents )
                {
                    acls = p.getFieldValues( Fields.ACL_EXPLICIT );
                    id = (String) p.getFieldValue(Fields.ID);

                    if (acls != null)
                        map.put( id, acls );
                }
            }
        }

        return map;
    }

    public static SolrDocument getAclParent( SolrDocument doc ) throws SolrServerException
    {
        CoreClient client = CoreClient.getInstance();

        while (true)
        {
            String parent = (String) doc.getFieldValue(Fields.PARENT);
            Boolean inherits = FieldUtils.parseBool(doc.getFieldValue(Fields.ACL_INHERITS), false);

            if (Strings.isNullOrEmpty(parent) || !inherits)
                return doc;

            SolrQuery params = new SolrQuery();
            params.set( Params.TICKET , Session.ROOTTICKET );
            params.set( Params.SHORTCUT , true );
            params.set( Fields.ID , parent );
            //params.set( ShardParams._ROUTE_ , (String) doc1.getFieldValue( schema.getRouteField() ) );
            params.setFields( Fields.ID, Fields.PARENT, Fields.SEQUENCE, Fields.ACL_INHERITS );

            doc = client.get(params);
        }
    }
	
	public static String[] resolveRights( SolrQueryRequest req , String type , int mask )
	{
		Schema schema = Schema.get(req);
		
		Set<String> cprofiles = schema.getProfiles(type);
		
		Map<String,Integer> profiles = new HashMap<>();

		main:
		for ( String cprofile : cprofiles )
		{
			if (cprofile.equals(Fields.CREATED_BY))
				continue;
			
			Integer crights = schema.getProfileRight( type, cprofile );
			
			if ((mask & crights)==crights)
			{
				java.util.Iterator<String> it = profiles.keySet().iterator();
				
				while( it.hasNext() )
				{
					String profile = it.next();
					
					Integer rights = profiles.get(profile) ;

					if ( (crights & rights) == rights && (!rights.equals(crights)) )
						it.remove();
					else if ( (crights & rights) == crights )
						continue main;
				}	

				profiles.put( cprofile, crights);
			}
		}

        Set<String> var = profiles.keySet();
        return var.toArray(new String[var.size()]);
	}
	
	/*public static Integer calcRights(  Map<String,Integer> acl ) 
	{
		Session session = Session. get();
		
		Integer rights = session.globalacl;
		
		for (Map.Entry<String, Integer> entry : acl.entrySet())
		{
			if (rights == -1)
				return -1;
			
			Integer right = entry.getValue();
			
			if ( (rights & right) == right )
				continue;
				
			if (session.roles.contains( entry.getKey() ))
				rights |= right;
		}
		
		return rights;
	}*/
	

}

class AclAugmenter extends DocTransformer
{
	static Logger log = LoggerFactory.getLogger(AclAugmenter.class);
	
	protected String part;
	final String display;
	final Schema schema;
	SolrQueryRequest req;
	//protected int mask;

	public AclAugmenter( String display , SolrParams params, SolrQueryRequest req, NamedList args )
	{
		this.req = req;
		this.schema = Schema.get(req);
		this.part = params.get( "part" , (String) args.get( "part" ) );
		//this.mask = params.getInt( "mask" , -1 );
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
	 
		Object val = null;
		
		DocUtils.convertIndexableFields(req, doc, Fields.COMMON_FIELDS);
		String id = (String) doc.getFieldValue( Fields.ID );

		try
		{
            if ("acl_sequence".equals( this.part ))
            {
                SolrDocument aclParent = AclAugmenterFactory.getAclParent(doc);
                val = aclParent.getFieldValue( Fields.SEQUENCE);
            }
            else if ("acl_parent".equals( this.part ))
            {
                SolrDocument aclParent = AclAugmenterFactory.getAclParent(doc);
                val = aclParent.getFieldValue( Fields.ID);
            }
            else if ("acl_inherited".equals( this.part ) )
			{
                val = AclAugmenterFactory.getInherited(doc);
			}
			else if ("acl_read".equals( this.part ))
			{
				val = doc.getFieldValues( Fields.ACL_READ );
			}
            else
			{
				Map<String,Integer> acl = DocUtils.getAcl(req,doc);

                switch (this.part) {
                    case "acl_profiles": {
                        String type = id.split("@")[1];

                        Map<Integer, String[]> profMap = new HashMap<>();
                        for (Integer rights : acl.values()) {
                            String[] profiles = AclAugmenterFactory.resolveRights(req, type, rights);

                            profMap.put(rights, profiles);
                        }
                        val = profMap;
                        break;
                    }
                    case "acl_actors":
                        Map<String, String> actors = new HashMap<>();

                        Collection<String> ids = new LinkedHashSet<>();

                        Map<String, Object> inherited = AclAugmenterFactory.getInherited(doc);

                        ids.addAll(acl.keySet());
                        ids.addAll(inherited.keySet());

                        for (String actor : ids) {
                            ContentCacheManager.CacheEntry pc = ContentCacheManager.getInstance().getCacheEntry(req, actor);

                            if (pc != null) {
                                actors.put(actor, pc.getName());
                            } else /* potrebbe essere un id fittizio relativo ad un <sid>@<user|group> */ {
                                String sid = actor.split("@")[0];


                                actors.put(actor, actor.split("@")[0]);
                            }

                        }

                        val = actors;
                        break;
                    case "acl_rights":
                        val = acl;
                        break;
                    default: {
                        //int rights = AclAugmenterFactory.calcRights(acl);

                        String userId = req.getParams().get("userId");

                        int rights = 0;

                        if (userId != null)
                            rights = DocUtils.getUserRights(req, doc, -1, userId);
                        else
                            rights = DocUtils.getUserRights(req, doc, -1);

					/*String modified_by = (String) doc.getFieldValue( Fields.MODIFIED_BY );

					Date lock_to = (Date) doc.getFieldValue( Fields.LOCK_TO );

					//Session session = Session. get();

					if (lock_to!=null && lock_to.after( new Date() ) && !modified_by.equals( Session.getId() ) )
						rights = rights & Schema.Rights.readonlymask | Session.getGlobalAcl();*/

                        String type = id.split("@")[1];

                        if ("user_rights".equals(this.part))
                            val = rights;
                        else if ("user_profiles".equals(this.part))
                            val = AclAugmenterFactory.resolveRights(req, type, rights);
                        break;
                    }
                }
			}
				
				
			
				/*Map<String,Integer> acl = AclAugmenterFactory.getAcl(doc);

				Map<String, Integer> allrights = new HashMap<String, Integer>();

				for (Map.Entry<String, Integer> entry : acl.entrySet())
				{
					String actor = entry.getKey();
					int right = entry.getValue();  & this.mask;
					
					if (right!=0)
						allrights.put( actor , right );
				}
				val = allrights;
			}
			else if ( "aclProfiles".equals( this.part ) )
			{
				Map<String,Integer> acl = AclAugmenterFactory.getAcl(doc);

				Map<String, String[]> allprofiles = new HashMap<String,String[]>();

				for (Map.Entry<String, Integer> entry : acl.entrySet())
				{
					//allprofiles.put( entry.getKey() , AclAugmenterFactory.resolveRights( doc, entry.getValue() ) );
					
					String actor = entry.getKey();
					int right = entry.getValue() & this.mask;
					
					if (right!=0)
					{
						String[] x = AclAugmenterFactory.resolveRights( doc, right );
						
						allprofiles.put( actor , x );
					}
				}
				val = allprofiles;
			}*/
		}
		catch( Exception e )
		{
			log.error("transform error",e);
		}
	 
		doc.setField( display, val );
	}
}



