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

import com.google.common.base.Strings;
import it.kdm.solr.core.Schema;
import it.kdm.solr.core.Schema.Fields;
import it.kdm.solr.core.Schema.Params;
import it.kdm.solr.core.Schema.Rights;
import it.kdm.solr.client.CoreClient;
import it.kdm.solr.common.DocUtils;
import it.kdm.solr.common.FieldUtils;
import it.kdm.solr.core.ContentCacheManager;
import it.kdm.solr.core.ProviderProxy;
import it.kdm.solr.core.Session;
import org.apache.commons.codec.net.URLCodec;
import org.apache.lucene.search.MatchNoDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrResponse;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.cloud.CloudDescriptor;
import org.apache.solr.cloud.ZkController;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.cloud.ClusterState;
import org.apache.solr.common.cloud.DocCollection;
import org.apache.solr.common.cloud.Slice;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.ShardParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.*;
import org.apache.solr.handler.component.ResponseBuilder;
import org.apache.solr.handler.component.ShardRequest;
import org.apache.solr.handler.component.ShardResponse;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;


/* il file originale ha ancora qualche bug e utility parziali */

public class RealTimeGetComponent extends org.apache.solr.handler.component.RealTimeGetComponent
{

	private transient static Logger log = LoggerFactory.getLogger(RealTimeGetComponent.class);
	
	//public static final String RT_COMPONENT_NAME = "realtimeget";

	@Override
	public int createSubRequests(ResponseBuilder rb) throws IOException {
  
		SolrParams params = rb.req.getParams();
		
		List<String> allIds = new ArrayList<>();
		
		String ids = params.get("ids");
		
		if (ids!=null)
			allIds.addAll( StrUtils.splitSmart( ids , ',' ) );
		
		String[] idArray = params.getParams(Fields.ID) ;
		
		if (idArray != null)
			allIds.addAll( Arrays.asList(idArray) );

        String PATH = params.get("PATH",params.get(Fields.ID,params.get("ids")));

        if (PATH!="" && PATH.startsWith("/"))
            allIds = ContentCacheManager.getInstance().searchByPATH(rb.req,PATH);

        if (allIds.size() == 0) {
		  return ResponseBuilder.STAGE_DONE;
		}
		
		log.debug( "createSubRequests start core:{} \n{}", rb.req.getCore().getName(), params );
		
		ZkController zkController = rb.req.getCore().getCoreContainer().getZkController();
		
		if ( zkController != null && params.get( ShardParams.SHARDS ) == null ) {
			
			CloudDescriptor cloudDescriptor = rb.req.getCore().getCoreDescriptor().getCloudDescriptor();
			String collection = cloudDescriptor.getCollectionName();
			ClusterState clusterState = zkController.getClusterState();
			DocCollection coll = clusterState.getCollection(collection);
			
			Map< Integer, List<String>> sliceToId = new HashMap<>();
			
			List<String> rbslices = Arrays.asList(rb.slices);
			
			for (String id : allIds) {
		  
				id = id.trim();
		  
				Slice slice = coll.getRouter().getTargetSlice(id, null, null, params, coll);

                if (slice==null)
                    throw new SolrException( SolrException.ErrorCode.CONFLICT , "inconsistent sharding configuration");

                int idx = rbslices.indexOf(slice.getName());

                if (idx==-1)
                    throw new SolrException( SolrException.ErrorCode.CONFLICT , "inconsistent request");

                List<String> idsForShard = sliceToId.get(idx);
				if (idsForShard == null) {
				  idsForShard = new ArrayList<>(2);
				  sliceToId.put( idx , idsForShard);
				}
				idsForShard.add(id);
			}
			
			for (Map.Entry< Integer ,List<String>> entry : sliceToId.entrySet()) {
				
				ShardRequest sreq = createShardRequest( entry.getValue() , rb.shards[entry.getKey()] , params );
				rb.addRequest(this, sreq);
			}
			
		} else {
			
			ShardRequest sreq = createShardRequest( allIds , null , params );
			rb.addRequest(this, sreq);
		}  
		
		log.debug( "createSubRequests end core:{}", rb.req.getCore().getName() );
		
		return ResponseBuilder.STAGE_DONE;
	}
  
	private ShardRequest createShardRequest( List<String> ids , String shard , SolrParams params )
	{
		ShardRequest sreq = new ShardRequest();

		String[] shards = shard != null ? new String[]{ shard } : null ; //ALL IF NULL

		sreq.shards = shards; 
		sreq.actualShards = shards;

		sreq.purpose = 1; //PRIVATE

		sreq.params = new ModifiableSolrParams(params);

		sreq.params.remove("id");
		//sreq.params.remove(ShardParams.SHARDS);
		//sreq.params.remove(ShardParams._ROUTE_);	
		sreq.params.set(ShardParams.SHARDS_QT,params.get(ShardParams.SHARDS_QT,"/get"));
		//sreq.params.set("distrib",false);
		sreq.params.set("ids", StrUtils.join(ids, ',') );

		log.trace("createShardRequest shard:{} ids:{} \n{}" , shard , ids , sreq.params );

		return sreq;
	}

	private void mergeDocuments(ResponseBuilder rb) {
		SolrDocumentList docList = new SolrDocumentList();
		
		NamedList<Object> shardInfo = null;
		if(rb.req.getParams().getBool(ShardParams.SHARDS_INFO, false)) {
			shardInfo = new SimpleOrderedMap<>();
			rb.rsp.add(ShardParams.SHARDS_INFO,shardInfo);
		}
		
		for (ShardRequest sreq : rb.finished) {
		  // if shards=shard1,shard2 was used, then  we query both shards for each id and
		  // can get more than one response
		  
			
			for (ShardResponse srsp : sreq.responses) {
				SolrResponse sr = srsp.getSolrResponse();
				NamedList nl = sr.getResponse();
				
				SolrDocumentList subList = (SolrDocumentList)nl.get("response");
				
				if(shardInfo!=null) {
					SimpleOrderedMap<Object> info = new SimpleOrderedMap<>();

					if (srsp.getException() != null) {
						Throwable t = srsp.getException();
						if(t instanceof SolrServerException) {
							t = t.getCause();
						}
						info.add("error", t.toString() );
						StringWriter trace = new StringWriter();
						t.printStackTrace(new PrintWriter(trace));
						info.add("trace", trace.toString() );
					}
					else {
						info.add("numFound", subList.getNumFound());
						info.add("shardAddress" , srsp.getShardAddress() );
					}
					if(srsp.getSolrResponse()!=null) {
						info.add("time", srsp.getSolrResponse().getElapsedTime());
					}

					shardInfo.add(srsp.getShard(), info);
				}
				if (subList!=null)
					docList.addAll(subList);
		  }
		}
		
		String[] ids = rb.req.getParams().getParams( Fields.ID );

		if (docList.size() <= 1 && ids != null && ids.length==1 ) {
		  // if the doc was not found, then use a value of null.
		  rb.rsp.add("doc", docList.size() > 0 ? docList.get(0) : null);

            boolean history = rb.req.getParams().getBool("getVersionHistory",false );

            if (history && docList.size() > 0)
            {
                SolrDocument doc = docList.get(0);

                Collection<Object> actualVersions = doc.getFieldValues( Schema.Fields.CONTENT_VERSIONS );

                if (actualVersions!=null)
                {
                    List<SimpleOrderedMap<Object>> maps = new ArrayList<>();

                    int idx = 1;
                    SimpleOrderedMap<Object> map=new SimpleOrderedMap<>();
                    for( Object version : actualVersions )
                    {
                        //Properties props = FieldUtils.getVersionProperties(version.toString());

                        String mdField = FieldUtils.getMetaDataField(idx);
                        String mdValue = (String) doc.getFirstValue( mdField );

                        Properties metaData = FieldUtils.getVersionProperties(mdValue);

                        map = new SimpleOrderedMap<>();

                        map.add("number",""+idx );
                        map.add("label", mdField );

                        for(String p : metaData.stringPropertyNames())
                            map.add(p,metaData.getProperty(p));

                        idx++;

                        maps.add(map);
                    }
                    map.add(Schema.Fields.MODIFIED_ON, doc.getFirstValue(Schema.Fields.CONTENT_MODIFIED_ON));
                    map.add(Schema.Fields.MODIFIED_BY, doc.getFirstValue(Schema.Fields.CONTENT_MODIFIED_BY));
                    map.add(Schema.Fields.MODIFIED_DN_BY, doc.getFirstValue(Schema.Fields.CONTENT_MODIFIED_DN_BY));
                    map.add(Schema.Fields.CONTENT_SIZE, doc.getFirstValue(Schema.Fields.CONTENT_SIZE));
                    map.add(Schema.Fields.CONTENT_TYPE, doc.getFirstValue(Schema.Fields.CONTENT_TYPE));
                    map.add(Schema.Fields.CONTENT_COMMENT, doc.getFirstValue(Schema.Fields.CONTENT_COMMENT));

                    rb.rsp.add("history",maps);

                }
            }
		} else {
		  docList.setNumFound(docList.size());
		  rb.rsp.add("response", docList);
		}
		rb.resultIds = new HashMap<>();

        rb.setQuery( new MatchNoDocsQuery() );
	}

  
	@Override
	public void finishStage(ResponseBuilder rb) {
	
		if (rb.stage != ResponseBuilder.STAGE_GET_FIELDS) {
		  return;
		}
		
		mergeDocuments(rb);
		
		String wt = rb.req.getParams().get("wt");
		
		if ("raw".equals(wt))
			manageStream(rb);
	}
	
	private final static String LASTVERSION = "last";
	
	private void manageStream( ResponseBuilder rb )
	{
		String versionId = rb.req.getParams().get("getVersion" , LASTVERSION );
	
		NamedList namedList = rb.rsp.getValues();
			
		SolrDocument doc = (SolrDocument) namedList.get("doc");
		
		if (doc==null)
			throw new SolrException( SolrException.ErrorCode.NOT_FOUND , "content not found");
		
		namedList.removeAll("doc");
		
		int rights = DocUtils.getUserRights(rb.req, doc, Schema.Rights.readContent);
		
		if (rights==0)
			throw new SolrException( SolrException.ErrorCode.FORBIDDEN , "User can't read content");
		
		
		ProviderProxy provider = new ProviderProxy(doc,rb.req);
		
		ContentStream stream;
		
		if (versionId.equals(LASTVERSION))
			stream = provider.downloadLastVersion();
		else
			stream = provider.downloadVersion( Integer.parseInt(versionId) );

        HttpServletRequest httpreq = (HttpServletRequest) rb.req.getContext().get( "httpRequest" );

        String byterange = rb.req.getParams().get("Range", httpreq.getHeader("Range"));
        if (byterange!=null)
        {
            try
            {
                if (byterange.startsWith("bytes="))
                    byterange = byterange.substring(6);

                InputStream is = stream.getStream();

                String[] interval = byterange.split("-");

                int start = Integer.parseInt(interval[0]);
                Long size = stream.getSize();

                int length;

                if (interval.length>1 && !Strings.isNullOrEmpty(interval[1]))
                    length = Integer.parseInt(interval[1])-start+1;
                else
                    length = size.intValue() - start;

                byte[] bytes = new byte[length];

                if (start>0)
                    is.skip(start);

                is.read(bytes);

                stream = new ContentStreamBase.ByteArrayStream(bytes,stream.getName());

                //Content-Range: bytes 0-99/928670754
                if (httpreq.getHeader("Range")!=null)
                {
                    rb.rsp.addHttpHeader("Content-Range",String.format("bytes %s-%s/%s",start,start+bytes.length,size));
                    rb.rsp.addHttpHeader("Accept-Ranges","bytes");
                }
            }
            catch(Exception e)
            {
                throw new RuntimeException(e);
            }
        }
		
		rb.rsp.add("content", stream );
		
		String name = (String) DocUtils.convertIndexableField( rb.req, doc, Fields.NAME );
		
		if (name!=null)
		{
			try
			{
				String encoded = new URLCodec().encode(name, java.nio.charset.StandardCharsets.UTF_8.name() );
					
				rb.rsp.setHttpHeader( "Content-Disposition", String.format("attachment; filename=\"%s\"; filename*=UTF-8''%s" , name, encoded ) );
				
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
		else
		{
			rb.rsp.setHttpHeader( "Content-Disposition", "attachment; filename=\"file.bin\"" );
		}
		
		/*else
		{
			try
			{
				rb.rsp.add("content", org.apache.commons.io.IOUtils.toByteArray( stream.getStream() ) );
			}
			catch(Exception e)
			{
				throw new RuntimeException(e);
			}
		}*/
	}
	
	@SuppressWarnings("unchecked")
	private Collection<String> findParents( SolrQueryRequest req, Collection<String> ids )
	{
        log.info("check who is parent:{}",ids);

		String q = CoreClient.Query.makeClause( Fields.PARENT , (Collection) ids );
		
		SolrQuery params = new SolrQuery(q);
		params.set( Params.TICKET , Session.ROOTTICKET );
		params.set( Params.SHORTCUT , true );
		params.setFields( Fields.PARENT );
		
		ids = new LinkedHashSet<>();

        SolrDocumentList docs;

        try
        {
            docs = CoreClient.getInstance().query( params ).getResults();
        }
        catch( Exception e )
        {
            throw new RuntimeException(e);
        }

        for( SolrDocument doc : docs )
            ids.add( (String) doc.getFieldValue(Fields.PARENT) );

        log.debug("found parents:{}",ids);
		
		return ids;
		
		/*String COUNT_CLAUSE = "{!join from="+Fields.ID+" to="+Fields.PARENT+"}"+CoreClient.Query.makeClause( Fields.ID , (Collection) ids ) ;
		
		SolrQuery params = new SolrQuery("*:*");
		params.set( Params.TICKET , Session.ROOTTICKET );
		params.set( Params.SHORTCUT , true );
		params.setFacet(true);
		params.addFacetField(Fields.PARENT);
		params.setFacetMinCount(1);
		params.setRows(0);
		params.addFilterQuery(COUNT_CLAUSE);
		
		NamedList nl=null;
		
		try
		{
			nl = CoreClient.getInstance().query( params ).getResponse();
			nl = (NamedList) nl.findRecursive( "facet_counts" , "facet_fields" , "parent" );
		}
		catch(Exception e)
		{
			log.error( "Error querying children count:"+COUNT_CLAUSE , e);
		}
		return nl;*/
	}

    /*@Override
    public void prepare(ResponseBuilder rb) throws IOException {
        // Set field flags

        super.prepare(rb);

        ReturnFields returnFields = rb.rsp.getReturnFields();

        boolean skipUnsecure = ( Session.get(rb.req).getGlobalAcl() & Rights.retrieve ) == 0;

        if (skipUnsecure)
        {
            List<String> newfl = new ArrayList<>();

            for( String field : Fields.COMMON_FIELDS )
                if ( !returnFields.wantsField(field) )
                    newfl.add(field);

            if (newfl.size()>0)
            {
                String[] fl = rb.req.getParams().getParams(CommonParams.FL);
                newfl.addAll(Arrays.asList(fl));
                returnFields = new SolrReturnFields( newfl.toArray(new String[0]), rb.req );
                rb.rsp.setReturnFields( returnFields );
            }


        }
    }*/
  
	@Override
	public void process(ResponseBuilder rb) throws IOException
	{
		log.debug( "process start core:{} \n{}" , rb.req.getCore().getName() , rb.req.getParams()  );

        super.process(rb);

        SolrParams params = rb.req.getParams();

        String[] idArray = params.getParams("id");

        Collection<String> ids = new LinkedHashSet<>();

        if (idArray!=null)
            ids.addAll(Arrays.asList(idArray));

        idArray = params.getParams("ids");

        if (idArray != null) {
            for (String idList : idArray) {
                ids.addAll( StrUtils.splitSmart(idList, ",", true) );
            }
        }

        NamedList namedList = rb.rsp.getValues();

        SolrDocumentList docList = (SolrDocumentList) namedList.get("response");

        if (docList == null || docList.isEmpty()) {
            docList = new SolrDocumentList();
            SolrDocument doc = (SolrDocument) namedList.get("doc");
            if (doc != null) {
                docList.add(doc);
                docList.setNumFound(1);
            }
        }

        /*Integer retryAfter = params.getInt(SolrClient.RETRY_AFTER);

        if (retryAfter != null && params.getParams("ids")==null && ids.size() == 1 && docList.size()==0)
        {
            //SolrCache updateLog = rb.req.getSearcher().getCache("updateLog");
            //if (updateLog!=null && updateLog.get(ids.iterator().next())!=null)
            //{
                try
                {
                    log.warn("retry after {}ms\n{}",retryAfter,params);
                    Thread.sleep(1000);
                    super.process(rb);
                    namedList = rb.rsp.getValues();

                    SolrDocument doc = (SolrDocument) namedList.get("doc");

                    if (doc==null)
                    {
                        Thread.sleep(2000);
                        super.process(rb);
                        doc = (SolrDocument) namedList.get("doc");
                    }

                    if (doc!=null)
                    {
                        docList.add(doc);
                        docList.setNumFound(1);
                    }
                    else
                    {
                        log.error("missing document:"+ids.iterator().next());
                    }
                }
                catch(Exception e)
                {
                    log.error("retry after error\n{}", params, e);
                }
            //}

            //if (docList.size()==0)
            //    log.error("missing document");
        }*/

        if( rb.isDebug() ) {
			/* per far funzionare il debugcomponent */
            rb.setQuery( new MatchNoDocsQuery() );
			rb.setFilters( new ArrayList<Query> () );
			
			DocListAndSet res = new DocListAndSet();
			res.docList = new DocSlice(0, 0 , new int[0] , null, 0 , 0);
			rb.setResults( res );
		}		





        int rows = params.getInt("rows", Integer.MAX_VALUE );
		
		if (docList.size() == 0 || rows == 0)
			return;



        boolean skipEmpty = params.getBool("skipEmpty",false);
		boolean skipDeleted = params.getBool("skipDeleted",false);
        boolean skipUnsecured = ( Session.get(rb.req).getGlobalAcl() & Rights.retrieve ) == 0;
		
		SolrDocumentList retList =  new org.apache.solr.common.SolrDocumentList();

        if (skipEmpty)
            ids = findParents( rb.req, ids );

		for (int i=0; i<docList.size(); i++) {
			
			if (retList.size() >= rows)
            {
                log.debug("list truncated ids:{} rows:{}", docList.size(), rows);
				break;
            }
			
			SolrDocument doc = docList.get(i);

            /*if (skipUnsecured)
                DocUtils.convertIndexableFields( rb.req, doc, Fields.COMMON_FIELDS  );
            else
                DocUtils.convertIndexableField(rb.req, doc, Fields.ID);*/

            String id = (String) DocUtils.convertIndexableField(rb.req, doc, Fields.ID);

            if (skipUnsecured)
			{
				int rights = DocUtils.getUserRights( rb.req, doc, Schema.Rights.retrieve );

                log.trace("check unsecured item:{} rights:{}", id, rights);

				if ( rights==0 )
                    continue;
			}
			
			if (skipDeleted)
			{
                boolean isAttached = ContentCacheManager.getInstance().isAttached(rb.req, id);

                log.trace("check disabled item:{} isAttached:{}",id,isAttached);

                if ( !isAttached )
                    continue;
			}
			
			if (skipEmpty)
            {
                boolean hasChildren = ids.contains(id);

                log.trace("check children item:{} hasChildren:{}", id, hasChildren);

                if ( !hasChildren )
                    continue;
            }

			retList.add(doc);
		}

		if (retList.size() < docList.size())
		{
			namedList.removeAll("doc");
			namedList.removeAll("response");

			if (rb.req.getParams().get("id") != null) {
				rb.rsp.add("doc", retList.isEmpty() ? null : retList.get(0));
			} else {
				retList.setNumFound(retList.size());
				rb.rsp.add("response", retList);
			}
			docList = retList;
		}
		
		log.trace("process end core:{} \n{}", rb.req.getCore().getName(), docList);
		
	}
	

}