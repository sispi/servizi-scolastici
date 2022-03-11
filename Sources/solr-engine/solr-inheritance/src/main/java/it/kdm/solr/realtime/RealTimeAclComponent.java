package it.kdm.solr.realtime;
//import it.kdm.solr.components.*;

import it.kdm.solr.client.CoreClient;
import it.kdm.solr.common.DocUtils;
import it.kdm.solr.core.ExpirableRegenerator;
import it.kdm.solr.core.Schema;
import it.kdm.solr.core.Session;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.queries.TermsQuery;
import org.apache.lucene.util.*;
import org.apache.solr.common.cloud.Replica;
import org.apache.solr.handler.component.SearchComponent;
import org.apache.solr.handler.component.ResponseBuilder;
import org.apache.solr.handler.component.ShardRequest;
import org.apache.solr.handler.component.ShardResponse;

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

import java.io.IOException;
import java.util.*;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.solr.client.solrj.SolrResponse;
import org.apache.solr.cloud.CloudDescriptor;
import org.apache.solr.cloud.ZkController;
import org.apache.solr.common.cloud.ClusterState;
import org.apache.solr.common.cloud.DocCollection;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.ShardParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.schema.FieldType;
import org.apache.solr.search.SolrIndexSearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import it.kdm.solr.core.Schema.Fields;

import org.apache.lucene.index.NumericDocValues;
import org.apache.lucene.search.*;
import org.apache.solr.search.SolrCache;

/* il file originale ha ancora qualche bug e utility parziali */

public class RealTimeAclComponent extends SearchComponent
{

	private transient static Logger log = LoggerFactory.getLogger(RealTimeAclComponent.class);
	public static final String COMPONENT_NAME = "acls";
	
	private static final int TERMS_CACHE_DURATION = 60; //seconds
	public static final int ACL_CACHE_DURATION = 60; //seconds

	private void createShardRequest( ResponseBuilder rb )
	{
		SolrParams params = rb.req.getParams();
		ShardRequest sreq = new ShardRequest();
		sreq.purpose = ShardRequest.PURPOSE_PRIVATE ;
		sreq.params = new ModifiableSolrParams(params);
		sreq.params.set(ShardParams.SHARDS_QT,params.get("qt","/acls"));
		sreq.params.set("ticket" , params.get("ticket"));
		rb.addRequest(this, sreq);
		
		log.debug( "createShardRequest sreq:{}", sreq );
	}
	
	@Override
	public int distributedProcess(ResponseBuilder rb) throws IOException {
	
		if (rb.stage < ResponseBuilder.STAGE_GET_FIELDS)
			return ResponseBuilder.STAGE_GET_FIELDS;
		if (rb.stage == ResponseBuilder.STAGE_GET_FIELDS) {
			createShardRequest(rb);
			return ResponseBuilder.STAGE_DONE;
		}
		return ResponseBuilder.STAGE_DONE;
	}
	
	/*@SuppressWarnings("unchecked")
	private void finishTerms(ResponseBuilder rb)
	{
		NamedList<Object> values = new NamedList<Object>();
		
		for (ShardRequest sreq : rb.finished) {
		  
			for (ShardResponse srsp : sreq.responses) {
				SolrResponse sr = srsp.getSolrResponse();
				NamedList<Object> nl = sr.getResponse();
				
				nl = (NamedList<Object>) nl.get("values");
				
				values.addAll(nl);
		  }
		}
		
		rb.rsp.add("values", values );
	}
  
	@SuppressWarnings("unchecked")
	private void finishAcls(ResponseBuilder rb)
	{
		NamedList<Object> values = new NamedList<Object>();
		
		for (ShardRequest sreq : rb.finished) {
		  
			for (ShardResponse srsp : sreq.responses) {
				SolrResponse sr = srsp.getSolrResponse();
				NamedList<Object> nl = sr.getResponse();
				
				nl = (NamedList<Object>) nl.get("values");
				
				values.addAll(nl);
			}
		}
		
		rb.rsp.add("values", values );
	}*/

	@Override
	@SuppressWarnings("unchecked")
	public void finishStage(ResponseBuilder rb) {
	
		if (rb.stage != ResponseBuilder.STAGE_GET_FIELDS ) {
		  return;
		}

        NamedList<Object> values = new NamedList<>();

        for (ShardRequest sreq : rb.finished) {

            for (ShardResponse srsp : sreq.responses) {
                SolrResponse sr = srsp.getSolrResponse();
                NamedList<Object> nl = sr.getResponse();

                nl = (NamedList<Object>) nl.get("values");

                values.addAll(nl);
            }
        }

        rb.rsp.add("values", values );
		
		/*String purpose = rb.req.getParams().get("purpose" , "acls" );
		
		if (purpose.equals("terms"))
			finishTerms(rb);
		
		if (purpose.equals("acls"))
			finishAcls(rb);*/
	}
	
	@Override
	public void prepare(ResponseBuilder rb) throws IOException {
	}
	
	private void processTerms(ResponseBuilder rb) throws IOException
	{
        /* vengono estratti dalla filter cache i termini utilizzati per il campo acl_sequences */

		long startTime = System.currentTimeMillis();
		
		String shard_to = rb.req.getParams().get("shard.to");
		String shard_from = rb.req.getCore().getCoreDescriptor().getCloudDescriptor().getShardId();

        log.debug("start processTerms shard_from:{} shard_to:{}", shard_from, shard_to);
        final LeafReader indexReader = rb.req.getSearcher().getSlowAtomicReader();
		
		org.apache.lucene.index.Fields lfields = indexReader.fields();
		org.apache.lucene.index.Terms terms = null;

        try
        {
            if (lfields != null)
                terms = lfields.terms("acl_sequences");
        }
        catch(Exception e)
        {
            log.error("Error getting terms",e);
        }
		
		Collection<Long> longs = new HashSet<>();
		
		if (terms!=null)
		{
			org.apache.lucene.index.TermsEnum termsEnum = terms.iterator();

            CharsRefBuilder external = new CharsRefBuilder();
			
			FieldType ft = rb.req.getSchema().getFieldTypeNoEx("acl_sequences");
			
			ZkController zkController = rb.req.getCore().getCoreContainer().getZkController();
			CloudDescriptor cloudDescriptor = rb.req.getCore().getCoreDescriptor().getCloudDescriptor();
			String collection = cloudDescriptor.getCollectionName();
			ClusterState clusterState = zkController.getClusterState();
			DocCollection coll = clusterState.getCollection(collection);

            SolrIndexSearcher searcher = rb.req.getSearcher();
			
			BytesRef term = termsEnum.next();
			while (term != null) {

                ft.indexedToReadable(term, external);

                long sequence = Long.parseLong( external.toString() );

                if (!longs.contains(sequence))
                {
                    try
                    {
                        int docid = searcher.getFirstMatch(new Term("acl_sequences", term));

                        if (docid!=-1)
                        {
                            Document doc = searcher.doc(docid);
                            String acl_parent = doc.get("acl_parent");
                            String[] acl_sequences = doc.getValues("acl_sequences");

                            ArrayList<String> all_parents = new ArrayList<>();

                            if (acl_parent!=null && acl_sequences.length>0)
                            {
                                all_parents.add(acl_parent);

                                if (acl_sequences.length>1)
                                {
                                    String[] acl_parents = doc.getValues("acl_parents");

                                    if (acl_sequences.length != (acl_parents.length+1) )
                                    {
                                        String id = doc.get("id");
                                        log.warn( "parents and acl_sequences don't match for id:{}", id );
                                    }
                                    else
                                    {
                                        Collections.addAll( all_parents, acl_parents );
                                    }
                                }
                            }
                            else
                            {
                                String id = doc.get("id");
                                log.warn( "acl_parent and acl_sequences don't match for id:{}", id );
                            }

                            // IMPORTANTE!!! acl_sequences e acl_parents devono avere nello schema useDocValuesAsStored="false" se docValues attivati
                            for( int i=0; i<all_parents.size(); i++ )
                            {
                                String shard_doc = coll.getRouter().getTargetSlice( all_parents.get(i), null, null,null, coll).getName();

                                if (shard_doc.equals(shard_to))
                                {
                                    long l = Long.parseLong(acl_sequences[i]);
                                    longs.add(l);

                                    if (log.isTraceEnabled())
                                        log.trace("added sequence:{} shard_from:{} shard_to:{} \nterm:{}",l,shard_from,shard_to,sequence);
                                }

                            }
                        }
                        else
                        {
                            log.error( "Document not found for acl_sequences:{}", sequence );
                        }
                    }
                    catch(Exception e)
                    {
                        log.error("Error processing sequence:{}",sequence,e);
                    }
                }

				term = termsEnum.next();
			}
		}
		
		long elapsedTime = System.currentTimeMillis() - startTime;
		
		NamedList<Object> nl = new NamedList<>();
		nl.add(shard_from , longs );
		
		rb.rsp.add("values", nl );

        if (log.isTraceEnabled())
            log.trace("trace processTerms shard_from:{} shard_to:{} \nsequences:{}",shard_from,shard_to,longs);

        log.debug( "processTerms end core:{} elapsedTime:{} \n{}" , rb.req.getCore().getName() ,elapsedTime, longs );
	}

    private Collection<Long> processWarmOne(ResponseBuilder rb, boolean force, String shard_from, String shard_to )
    {
        String collection = rb.req.getCore().getCoreDescriptor().getCloudDescriptor().getCollectionName();

        Collection<Replica> replicas = rb.req.getCore().getCoreContainer().getZkController().getClusterState().getSlice(collection, shard_from).getReplicas();

        String shards=null;
        for( Replica r : replicas )
        {
            String url = r.getStr("base_url") + "/" + r.getStr("core") ;
            if (shards==null)
                shards = url;
            else
                shards += "|" + url;
        }

        if (shards==null)
        {
            log.error("No replicas to serve shard:{}",shard_from);
            throw new RuntimeException("No replicas to serve shard");
        }

        //String url = replica.getStr("");


        SolrCache cache = rb.req.getSearcher().getCache(Schema.RESULTS_CACHE);

        long startTime0 = System.currentTimeMillis();

        String cachekey = "terms-"+shard_from+"-"+shard_to;

        ExpirableRegenerator.Expirable cachable = (ExpirableRegenerator.Expirable) cache.get(cachekey);

        log.debug("start processWarmOne for shard_to:{} shard_from:{}",shard_to,shard_from);

        if (!force && cachable!=null && !cachable.isExpired())
        {
            Collection<Long> terms = (Collection<Long>) cachable.getObject();

            if (log.isTraceEnabled())
                log.trace("already in cache cachekey:{} \nterms:{}", cachekey, terms);

            return terms;
        }

        ModifiableSolrParams params = new ModifiableSolrParams();
        params.set( "qt" , "/acls");
        params.set( "purpose" , "terms" );
        params.set( "shard.to" , shard_to );
        params.set( "shortcut" , true );
        params.set( "shards" , shards );
        params.set( Schema.Params.TICKET , Session.ROOTTICKET );

        NamedList<Object>  nl;
        try
        {
            nl = CoreClient.getInstance().query( params ).getResponse();
        }
        catch( Exception e ){
            log.error("error processWarmOne for shard_to:{} shard_from:{}", shard_to, shard_from, e);
            throw new RuntimeException(e);
        }

        if (log.isTraceEnabled())
            log.trace("trace processWarmup cachekey:{} term \nresponse:{}",cachekey,nl);

        nl = (NamedList<Object>) nl.get("values");

        //values.add(cachekey,nl);

        Collection<Long> terms=null;

        for( int i=0; i< nl.size(); i++)
        {
            Collection<Long> lterms = (Collection<Long>) nl.getVal(i);

            if (terms==null)
                terms = lterms;
            else
                terms.addAll(lterms);
        }

        cachable = new ExpirableRegenerator.CacheEntry( terms , (int) TERMS_CACHE_DURATION );
        cache.put(cachekey,cachable);

        long elapsedTime0 = System.currentTimeMillis() - startTime0;

        log.debug("end processWarmOne for shard_to:{} shard_from:{} end:{}",shard_to,shard_from,elapsedTime0);

        return terms;
    }

    @SuppressWarnings("unchecked")
    private void processWarmup(ResponseBuilder rb)
    {
        log.debug( "processWarmup start core:{}" , rb.req.getCore().getName() );

        long startTime = System.currentTimeMillis();

        String collection = rb.req.getCore().getCoreDescriptor().getCloudDescriptor().getCollectionName();

        Collection<String> shards = rb.req.getCore().getCoreContainer().getZkController().getClusterState().getActiveSlicesMap(collection).keySet();

        String shard_to = rb.req.getCore().getCoreDescriptor().getCloudDescriptor().getShardId();

        NamedList<Object> values = new NamedList<Object>();

        rb.rsp.add("values", values);

        SolrCache cache = rb.req.getSearcher().getCache(Schema.RESULTS_CACHE);

        for( String shard_from : shards )
        {
            String cachekey = "terms-"+shard_from+"-"+shard_to;
            values.add(cachekey, processWarmOne(rb,true,shard_from, shard_to ) );
        }

        long elapsedTime = System.currentTimeMillis() - startTime;

        log.debug( "processWarmup end core:{} elapsedTime:{}" , rb.req.getCore().getName() ,elapsedTime );
    }

	@SuppressWarnings("unchecked")
	private void processAcls(ResponseBuilder rb)
	{
        /* vengono richiesti allo shard richiedente i "terms" utilizzati e vengono poi filtrati per visibilità */

        //processWarmup(rb,false);

        long startTime = System.currentTimeMillis();

        String shard_to = rb.req.getCore().getCoreDescriptor().getCloudDescriptor().getShardId().replaceAll("\\p{C}", "");

        String shard_from = rb.req.getParams().get("shard.from").replaceAll("\\p{C}", "");

        log.debug( "processAcls start core:{} shard:{} shard.from:{}" , rb.req.getCore().getName(),shard_to, shard_from   );

        Collection<Long> terms = processWarmOne(rb,false,shard_from,shard_to);

        /*
        SolrCache cache = rb.req.getSearcher().getCache(Schema.RESULTS_CACHE);

		String cachekey = "terms-"+shard_from+"-"+shard_to;

        ExpirableRegenerator.Expirable cachable = (ExpirableRegenerator.Expirable) cache.get(cachekey);
*/

        NamedList<Object> values = new NamedList<>();
        rb.rsp.add("values", values );

        /*if (cachable==null)
        {
            log.error("Acl shard cache is missing cachekey:{}",cachekey);
            return;
        }

        if (cachable.isExpired())
        {
            log.warn("Acl shard cache is expired cachekey:{}", cachekey);
        }

        Collection<Long> terms = (Collection<Long>) cachable.getObject();*/

        Session session = Session.get(rb.req);

        Collection<String> roles = session.getRoles();

        if (roles==null || roles.size()==0)
        {
            if (log.isTraceEnabled())
                log.trace("trace processAcls\n user:{} NO ROLES", session.getId() );

            log.debug( "processAcls end core:{} NO ROLES" );
            return;
        }

        if (log.isTraceEnabled())
            log.trace("trace processAcls\n user:{} roles:{}", Session.get(rb.req).getId(),  roles);


        long t1 = System.currentTimeMillis() - startTime ;

        log.trace("trace processAcls terms:{}",terms);

		BytesRef[] bytesRefs = DocUtils.bytesRefs(rb.req, Fields.ACL_READ, roles);

		/*List<BytesRef> bytesRefs = new ArrayList<BytesRef>();

		FieldType ft = rb.req.getSchema().getFieldType(Fields.ACL_READ);

		for ( String role : roles ) {

			BytesRef term = new BytesRef();

			ft.readableToIndexed( role , term);

			bytesRefs.add( term );
		}*/

		//Query filter1 = QueryUtils.Filters.termsFilter.makeFilter( Fields.ACL_READ, bytesRefs );

        Set<Long> sequences;

        if (terms instanceof HashSet)
            sequences = (HashSet<Long>) terms;
        else
            sequences = new HashSet<>( terms );

        Query aclQuery = new TermsQuery(Fields.ACL_READ, bytesRefs);

        Query sequenceQuery = new DocValuesNumbersQuery( Fields.SEQUENCE , sequences);

        BooleanQuery.Builder builder = new BooleanQuery.Builder();

        builder.add(aclQuery, BooleanClause.Occur.FILTER);
        builder.add(sequenceQuery, BooleanClause.Occur.FILTER);





		//Query query = new ConstantScoreQuery( filter1 );

        NumericDocValuesCollector results = new NumericDocValuesCollector( Fields.SEQUENCE );
		//KeyFilter filter = new KeyFilter( "sequence" , terms );





        BooleanQuery joinQuery = builder.build();

        try
		{
            //rb.req.getSearcher().search(query, filter, results);
            rb.req.getSearcher().search(joinQuery, results);

		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}

		Collection<Long> coll = results.getCollectedKeys();

		values.add(shard_to , coll);

        if (log.isTraceEnabled())
            log.trace("trace processAcls acl values:{}",values);

        long elapsedTime = System.currentTimeMillis() - startTime;
		long t2 = elapsedTime - t1 ;

		log.debug( "processAcls end core:{} elapsedTime:{}={}+{} \n{}" , rb.req.getCore().getName() ,elapsedTime,t1,t2, coll );
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void process(ResponseBuilder rb) throws IOException
	{
		String purpose = rb.req.getParams().get("purpose" , "acls" );

        log.debug("start process params:{}",rb.req.getParams());

        /*

        Il componente lavora in modo diverso in due versi.

        1)  lo shard richiedente ( shard.from, da cui si scatena la query tramite il componente in modalità "acls" )
            chiede in modo distribuito ad ogni shard i "terms" nell'altro verso

        2) ogni shard risponde i "terms" (eventualmente cachati su base TERMS_CACHE_DURATION ) con la la distinct degli acl_parents utilizzati e di competenza dello shard chiamante

        3) i terms vengono "riquerati" e filtrati con un dedicato collector per ottenere solo quelli visibili al ticket

        4) infine ogni shard richiedente "possiede" la lista degli acl_parents che gli competono e la mette in una cache su base ACL_CACHE_DURATION

         */
		
		if (purpose.equals("terms"))
			processTerms(rb);
		
		if (purpose.equals("acls"))
        {
            processAcls(rb);
        }

        if (purpose.equals("warmup"))
        {
            processWarmup(rb);
        }

		
	}
	
	public static class NumericDocValuesCollector implements Collector, LeafCollector {

		private String keyName;
		private NumericDocValues keyValues;
		HashSet<Long> collectedSet;

		public NumericDocValuesCollector(String keyName) {
			this.keyName = keyName;
			this.collectedSet = new HashSet<>();
		}

		@Override
		public void collect(int docId) throws IOException {
			long key = keyValues.get(docId);
			collectedSet.add(key);
		}

        @Override
        public LeafCollector getLeafCollector(LeafReaderContext context) throws IOException{
            this.keyValues = context.reader().getNumericDocValues(this.keyName);

            return this;
        }

		/*@Override
		public void setNextReader(AtomicReaderContext context) throws IOException {

            this.keyValues = context.reader().getNumericDocValues(this.keyName);
		}*/

		@Override
		public boolean needsScores() {
			return false;
		}

		@Override
		public void setScorer(Scorer scorer) throws IOException {
		}

		public HashSet<Long> getCollectedKeys() {
			return this.collectedSet;
		}
	}
	
	/*public static class KeyFilter extends Filter {
	
		private HashSet<Long> keySet;
		private String keyName;

		private Map<Object, DocIdSet> docSetCache = new WeakHashMap<Object, DocIdSet>();

		KeyFilter( String keyName, Collection<Long> keys ) {
			
			this.keyName = keyName;
			
			if (keys instanceof HashSet)
				this.keySet = (HashSet<Long>) keys;
			else
				this.keySet = new HashSet<Long>( keys );
		}

		@Override
		public DocIdSet getDocIdSet(LeafReaderContext context, Bits acceptDocs) throws IOException {
			LeafReader reader = context.reader();
			Object coreKey = reader.getCoreCacheKey();
			
			DocIdSet docSet = this.docSetCache.get(coreKey);
			
			if (docSet == null) {
				docSet = this.createDocIdSet(reader);
				this.docSetCache.put(coreKey, docSet);
			}
			
			return docSet;
		}

		private DocIdSet createDocIdSet(LeafReader reader) throws IOException {
			NumericDocValues keyValues = reader.getNumericDocValues(this.keyName);

            RoaringDocIdSet.Builder builder = new RoaringDocIdSet.Builder(reader.maxDoc());
			
			if (keyValues != null)
			{
				for (int docId = 0; docId < reader.maxDoc(); docId++)
				{
					long key = keyValues.get(docId);
					if (this.keySet.contains(key))
					{
                        builder.add(docId);

					}
				}
			}
            RoaringDocIdSet docBitSet = builder.build();

            //docBitSet.trimTrailingZeros();
			return docBitSet;
        }

        @Override
        public String toString(String field) {
            return field;
        }
    }*/

    @Override
    public String getDescription() {
        return "acl component";
    }

    @Override
    public String getSource() {
        return null;
    }
	
}