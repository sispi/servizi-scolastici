package it.kdm.solr.realtime;

import it.kdm.solr.common.FieldUtils;
import it.kdm.solr.core.ExpirableRegenerator;
import org.apache.commons.lang.ObjectUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.search.*;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.component.ResponseBuilder;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.*;
import org.apache.solr.search.function.ValueSourceRangeFilter;
import org.apache.solr.update.DocumentBuilder;
import org.apache.solr.update.VersionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class RealTimeIndex {

	public static final Logger log = LoggerFactory.getLogger(RealTimeIndex.class);

    //static String UPDATELOG_CACHE = "updateLog";
    private final static String UNCOMMITTED = "uncommitted";

    private static RealTimeIndex instance = null;

    public static RealTimeIndex getInstance()
	{
		if (instance==null)
        {
            instance = new RealTimeIndex();
        }

		return instance;
	}

    protected int lifetime = -1;
    protected int tolerance = -1;

    public void cleanUp( final SolrCore core, final Boolean warmup )
    {
        if (tolerance==-1)
        {
            int autoSoftCommmit = core.getSolrConfig().getUpdateHandlerInfo().autoSoftCommmitMaxTime;
            int autoCommit = core.getSolrConfig().getUpdateHandlerInfo().autoCommmitMaxTime;

            if (!core.getSolrConfig().getUpdateHandlerInfo().openSearcher)
                autoCommit = autoSoftCommmit;
            else if (autoSoftCommmit>0)
                autoCommit = Math.min(autoCommit,autoSoftCommmit);

            if (autoCommit==-1)
            {
                log.warn("autoCommit+openSearcher or autoSoftCommit must be valued to clean up properly the RTI");

                lifetime = -1;
                tolerance = 10000;
            }
            else
            {
                lifetime = ((Double) Math.ceil( autoCommit * 1.3 )).intValue() ;
                tolerance = ((Double) Math.ceil( autoCommit * 0.5 )).intValue() ;
            }

            log.info("Initialized cleanup params lifetime:{} tolerance:{}",lifetime,tolerance);
        }

        final int lifetime_final = lifetime ;
        final int tolerance_final = tolerance ;
        final long now = System.currentTimeMillis();

        synchronized (timestamps)
        {
            //expiration
            if (!warmup && lifetime_final>0)
            {
                Long ts = timestamps.get(core.toString());

                if (ts!=null && (now-ts)<lifetime_final )
                {
                    log.debug("Skipped RTI clean thread (too early) lifetime:{}",lifetime_final);
                    return;
                }
            }

            timestamps.put(core.toString(),now);
        }

        final Map<String,ExpirableRegenerator.CacheEntry> updateLog = getUpdateLog(core);

        if (updateLog.size()==0)
        {
            log.debug("Skipped RTI clean thread (empty)");
            return;
        }

        TimerTask action = new TimerTask() {
            public void run() {

                log.info("Started RTI clean thread core:{} warmup:{} count:{} lifetime:{} tolerance:{}",core.getName(),  warmup,updateLog.size(),lifetime_final,tolerance_final);

                synchronized (updateLog)
                {
                    Collection<String> oldKeys = new ArrayList<>();

                    for( String key: updateLog.keySet() )
                    {
                        ExpirableRegenerator.CacheEntry entry = updateLog.get(key);

                        long insertedOn = entry.getInsertedOn().getTime();

                        if ( warmup && (now-insertedOn)>tolerance_final || lifetime_final>0 && (now-insertedOn)>lifetime_final)
                            oldKeys.add(key);
                    }

                    if (oldKeys.size()>0)
                    {
                        for( String key : oldKeys )
                        {
                            updateLog.remove(key);
                            log.debug("RTI (warmup) entry expired {}", key);
                        }
                    }
                    log.info("Finished RTI clean thread core:{} warmup:{} cleaned:{}",core.getName(),warmup,oldKeys.size());
                }
            }
        };

        new Timer().schedule(action, 1);
    }

    static Map<String, Map<String,ExpirableRegenerator.CacheEntry>> logs = new HashMap<>();
    static Map<String, Long > timestamps = new HashMap<>();

    protected Map<String,ExpirableRegenerator.CacheEntry> getUpdateLog( SolrCore core )
    {
        Map<String,ExpirableRegenerator.CacheEntry> cache = logs.get(core.getName());

        if (cache==null)
            synchronized (logs)
            {
                logs.put(core.getName(),new HashMap<String, ExpirableRegenerator.CacheEntry>());
            }

        return logs.get(core.getName());
    }

    /*@SuppressWarnings("unchecked")
	private Map<String,ExpirableRegenerator.CacheEntry> getAllEntries( SolrQueryRequest req )
	{
		try
		{
			SolrCache updateLog = req.getSearcher().getCache(UPDATELOG_CACHE);

            if (updateLog==null)
                return new HashMap<>();

            Map<String,ExpirableRegenerator.CacheEntry> updateLog = getUpdateLog(req);

            Class drc = Class.forName("org.apache.solr.search.FastLRUCache");
			java.lang.reflect.Field field = drc.getDeclaredField("cache");

			field.setAccessible(true);
			ConcurrentLRUCache cache = (ConcurrentLRUCache) field.get(updateLog);

            return (Map<String,Object>) cache.getLatestAccessedItems( Integer.MAX_VALUE );

            return updateLog;
		}
		catch( Exception e )
		{
			throw new RuntimeException(e);
		}
	
	}*/
	
	@SuppressWarnings("unchecked")
	public RealTimeDocument removeDocument( SolrQueryRequest req , String keyValue )
	{
        /*SolrCache updateLog = req.getSearcher().getCache(UPDATELOG_CACHE);

        if (updateLog==null)
            return null;*/

        Map<String,ExpirableRegenerator.CacheEntry> updateLog = getUpdateLog(req.getCore());

        ExpirableRegenerator.CacheEntry entry = new ExpirableRegenerator.CacheEntry( RealTimeDocument.EmptyDocument , 0 );

        synchronized (updateLog)
        {
            updateLog.put( keyValue , entry );
        }

		String shard = req.getCore().getCoreDescriptor().getCloudDescriptor().getShardId() ;
		String coreName = req.getCore().getCoreDescriptor().getCloudDescriptor().getCoreNodeName() ;
		
		log.debug("removed document from memoryIndex: "+keyValue + " shard:" + shard + " core:"+ coreName );
		
		return (RealTimeDocument) entry.getObject();
	}
	
	@SuppressWarnings("unchecked")
	public RealTimeDocument addDocument( SolrQueryRequest req ,  SolrInputDocument doc )
	{
        if (doc.containsKey(UNCOMMITTED) && "false".equalsIgnoreCase(doc.getFieldValue(UNCOMMITTED).toString()))
        {
            log.info("uncommitted==false not added in RTI id:{} core:{}",doc.getFieldValue("id"),req.getCore().getName());
            return null;
        }

        Map<String,ExpirableRegenerator.CacheEntry> updateLog = getUpdateLog(req.getCore());

        SolrInputDocument clone = new SolrInputDocument();

        for( org.apache.solr.common.SolrInputField sif : doc )
		{
            Collection<Object> values = sif.getValues();
            String field = sif.getName();

            if (values!=null && values.size()==1)
            {
                int maxRTFieldLength = Integer.parseInt(System.getProperty("maxlengthRT" + field, "-1"));
                if ( maxRTFieldLength>0 )
                {
                    String firstValue = sif.getFirstValue().toString();
                    if (firstValue.length() > maxRTFieldLength )
                    {
                        log.warn("field '{}' ignored in rt index:{}",field,firstValue.length());
                        continue;
                    }
                }
            }

            clone.setField(field,values );
		}
		
		clone.setField( UNCOMMITTED , true);

        /* gestisce anche i copy fields */
		Document luceneDoc = DocumentBuilder.toDocument(clone, req.getSchema());
		
		String keyField = req.getSchema().getUniqueKeyField().getName();

        String keyValue = (String) doc.getFieldValue(keyField);

        ExpirableRegenerator.CacheEntry entry = new ExpirableRegenerator.CacheEntry( new RealTimeDocument(req.getSchema() , luceneDoc), 0 );

        log.debug("added memory({}) doc:{} \n{}", updateLog.size(), keyValue, entry.getObject());
		
		synchronized (updateLog)
        {
            updateLog.put(keyValue, entry);
        }

        if (log.isDebugEnabled())
        {
            String shard = req.getCore().getCoreDescriptor().getCloudDescriptor().getShardId() ;
            String coreName = req.getCore().getCoreDescriptor().getCloudDescriptor().getCoreNodeName() ;

            log.debug( "added document to memoryIndex:{} shard:{} core:{} \n{}" , keyValue, shard, coreName , entry );
        }
		
		return (RealTimeDocument) entry.getObject();
	}
	
	public void removeByQuery( SolrQueryRequest req , String query , long version )
    {
        /*SolrCache updateLog = req.getSearcher().getCache(UPDATELOG_CACHE);

        if (updateLog==null)
            return;*/


        String shard = req.getCore().getCoreDescriptor().getCloudDescriptor().getShardId() ;
        String coreName = req.getCore().getCoreDescriptor().getCloudDescriptor().getCoreNodeName() ;

        log.debug("query:{} core:{}", query, coreName);

        Query f;
        try
        {
            f = QParser.getParser(query,"lucene",req).getQuery();
        }
        catch( SyntaxError se )
        {
            log.error("syntax error:{}",query);
            throw new RuntimeException(se);
        }

        f = QueryUtils.makeQueryable(f);

        VersionInfo vi = req.getCore().getUpdateHandler().getUpdateLog().getVersionInfo();

        SchemaField sf = vi.getVersionField();

        ValueSource vs = sf.getType().getValueSource(sf, null);

        if (version == 0 || version == -Long.MAX_VALUE)
        {
            log.warn("dovrebbe essere stato corretto nel DistributedUpdateProcessor q:{} version:{}",f,version);
            // lo imposto all'istante corrente
            version = System.currentTimeMillis() << 20;
        }

        //BooleanQuery.Builder builder = new BooleanQuery.Builder();
        String uuid = UUID.randomUUID().toString();

        Query range;

        if (sf.hasDocValues()){
            ValueSourceRangeFilter filt = new ValueSourceRangeFilter(vs, Long.toString(Math.abs(version)), null, false, true);

            range = new FunctionRangeQuery(filt);
        } else {
            //range = LegacyNumericRangeQuery.newLongRange( "indexed_on", 6, null, version >>> 20, true, true);
            String versionDate = FieldUtils.formatDate(new Date(version >>> 20));

            String rq = "indexed_on:[ * TO "+versionDate+" ]";
            try {

                QParser queryParser = QParser.getParser(rq, null, req);
                range = queryParser.getQuery();
            } catch (SyntaxError syntaxError) {
                throw new RuntimeException(syntaxError);
            }
        }

        Query filter;

        if (f instanceof MatchAllDocsQuery)
        {
            /* crea il filtro inverso rispetto a tutti i documenti (*:*) fino al momento della sua esecuzione

            fq = +_version_:[ * TO NOW ] +*:* = +_version_:[ * TO NOW ]   che invertita è:

            fq = -_version_:[ * TO NOW ] = +_version:[ NOW TO * ]

            */

            //builder.add(range, BooleanClause.Occur.MUST);
            filter = range;
        }
        else
        {
            /* crea il filtro inverso rispetto ai documenti cancellati dalla query fino al momento della sua esecuzione e gestiso la PNQ

            fq = +_version_:[ * TO NOW ] +<query>   che invertita è:

            fq = -(+_version_:[ * TO NOW ] +<query>) = +*:* -(+_version_:[ * TO NOW ] +<query>)

             */

            BooleanQuery.Builder builder0 = new BooleanQuery.Builder();
            builder0.add(f, BooleanClause.Occur.MUST);
            builder0.add(range, BooleanClause.Occur.MUST);

            filter = builder0.build();

            // correzione PNQ

            //builder.add(new MatchAllDocsQuery() , BooleanClause.Occur.MUST);
            //builder.add(builder0.build(), BooleanClause.Occur.MUST_NOT );

        }

        //BooleanQuery bq = builder.build();

        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        builder.add(new MatchAllDocsQuery() , BooleanClause.Occur.MUST);
        builder.add(filter, BooleanClause.Occur.MUST_NOT );

        BooleanQuery nbq = builder.build();

        Map<String,ExpirableRegenerator.CacheEntry> updateLog = getUpdateLog(req.getCore());

        synchronized (updateLog)
        {
            updateLog.put(uuid , new ExpirableRegenerator.CacheEntry( nbq, 0 ) );

            log.trace("added deleteByQuery query:{} UUID:{} filter:{}",query,uuid,nbq);

            for( String id : updateLog.keySet() )
            {
                ExpirableRegenerator.CacheEntry entry0 = updateLog.get(id);

                if (entry0.getObject() instanceof Query)
                    continue;

                RealTimeDocument entry = (RealTimeDocument) entry0.getObject();

                synchronized (entry) {

                    if (!entry.isEmpty())
                    {
                        float match = entry.search(filter);

                        if (match==0)
                            continue;

                        synchronized (updateLog)
                        {
                            updateLog.put( id , new ExpirableRegenerator.CacheEntry( RealTimeDocument.EmptyDocument , 0 ) );
                        }

                        log.debug("removed document from memoryIndex: "+id + " shard:" + shard + " core:"+ coreName );
                    }
                }
            }
        }
    }
	
	@SuppressWarnings("unchecked")
	public NamedList modifyRequest( ResponseBuilder rb )
	{
        /*
            Una importante miglioria si potrebbe fare sfruttando il multi-threading per il table scan
            I tempi di verifica sono ovviamente proporzionali ai documenti contenuti nel RTI.
            Per controllare questa dimensione è opportuno impostare l'autocommit (soft o hard) per numero di documenti (oltre quello temporale)
        */

        /*SolrCache updateLog = rb.req.getSearcher().getCache(UPDATELOG_CACHE);

        if (updateLog==null)
            return null;*/


        SchemaField idField = rb.req.getCore().getLatestSchema().getUniqueKeyField();

        try
        {
            long startTime = System.nanoTime();

            /* estraggo informazioni nella richiesta riguardo all'ordinamento */

            final SortField[] sortFields;

            Sort sort = rb.getSortSpec().getSort();

            if (sort!=null && rb.req.getParams().getBool(ResponseBuilder.FIELD_SORT_VALUES,false))
                sortFields = sort.getSort();
            else
                sortFields = new SortField[0];

            boolean shardQueryIncludeScore = (rb.getFieldFlags() & SolrIndexSearcher.GET_SCORES) != 0 || rb.getSortSpec().includesScore();


            /* estraggo della richiesta la query e i filtri */

            Query query = rb.getQuery();

            List<Query> filters = rb.getFilters();

            ArrayList<Query> queries = new ArrayList<>();

            queries.add(QueryUtils.makeQueryable(query));

            ArrayList filters2 = new ArrayList<>();

            if (filters != null)
            {
                for( Query filter : filters )
                {
                    filter = QueryUtils.makeQueryable(filter);
                    queries.add(filter);
                    filters2.add(filter);
                }
            }

            filters = filters2;


            BooleanQuery.Builder posQueryBuilder = new BooleanQuery.Builder();
            BooleanQuery.Builder negQueryBuilder = new BooleanQuery.Builder();
            List<Query> deleteByQueries = new ArrayList<>();

            Float maxScore = 0.0f;
            SolrDocumentList moreDocs = new SolrDocumentList();

            Map<String,ExpirableRegenerator.CacheEntry> updateLog = getUpdateLog(rb.req.getCore());

            synchronized (updateLog)
            {

                for( String id : updateLog.keySet() )
                {
                    ExpirableRegenerator.CacheEntry entry = updateLog.get(id);

                    if (entry.getObject() instanceof Query)
                    {
                        deleteByQueries.add((Query) entry.getObject());
                        continue;
                    }

                    RealTimeDocument rtDoc = (RealTimeDocument) entry.getObject();

                    synchronized (rtDoc) {

                        float score = 1;

                        for ( Query f : queries )
                        {
                            if (f instanceof MatchAllDocsQuery )
                            {
                                log.trace("MatchAllDocsQuery id:{}",id);
                                continue;
                            }
                            else if (rtDoc.isEmpty())
                            {
                                score = 0;
                                log.trace("empty id:{} skipped",id);
                            }
                            else
                            {
                                //TODO lo score è sempre 1 . si potrebbe impostare ad altro numero fisso
                                score *= rtDoc.search(f);
                                log.trace("id:{} filter:{} score:{}",id,f,score);
                            }

                            if (score == 0)
                                break;
                        }

                        if (score > 0.0f)
                        {
                            posQueryBuilder.add( new TermQuery(new Term("id",id)),BooleanClause.Occur.SHOULD );

                            SolrDocument moreDoc = new SolrDocument();
                            moreDoc.setField( idField.getName() , id );

                            if (shardQueryIncludeScore)
                                moreDoc.setField( "score" , score );

                            /* se sono richiesti i field sort values occorre renderli disponibili per dopo */

                            for (SortField sortField : sortFields) {

                                String field = sortField.getField();
                                moreDoc.setField( field , rtDoc.getFirstValue(field) );
                            }

                            maxScore = Math.max( maxScore, score );

                            moreDocs.add( moreDoc );
                        }
                        else
                            negQueryBuilder.add( new TermQuery(new Term("id",id)),BooleanClause.Occur.MUST_NOT );
                    }
                }
            }

            if ( "newSearcher".equals(rb.req.getParams().get("event")))
                cleanUp(rb.req.getCore(),true);
            else
                cleanUp(rb.req.getCore(),false);

            BooleanQuery posQuery = posQueryBuilder.build();
            BooleanQuery negQuery = negQueryBuilder.build();

            /* modifica dei filtri e query */

            assert (posQuery.clauses().isEmpty() == (moreDocs.size()==0) );

            SimpleOrderedMap rt_filters = new SimpleOrderedMap();
            rb.rsp.add( RealTimeMergeStrategy.REALTIME_FILTERS , rt_filters );

            if (!posQuery.clauses().isEmpty() )
            {
                /* i match positivi vanno mergiato con tutti i filtri e diventano aggiunte (OR) */

                log.debug("merging positive filter:" + posQuery.toString());

                BooleanQuery.Builder builder = new BooleanQuery.Builder();

                builder.add(posQuery, BooleanClause.Occur.SHOULD);
                builder.add( query , BooleanClause.Occur.SHOULD );

                BooleanQuery bq = builder.build();

                log.debug("modified query:" + bq.toString());
                rt_filters.add("q",bq.toString());

                rb.setQuery( bq );

                rb.rsp.addToLog( "q", bq.toString() );

                if (filters.size() > 0 )
                {
                    ArrayList<Query> newfilters = new ArrayList<>( filters.size() );
                    for ( Query f : filters )
                    {
                        BooleanQuery.Builder builder2 = new BooleanQuery.Builder();

                        builder2.add( posQuery , BooleanClause.Occur.SHOULD );
                        builder2.add( f , BooleanClause.Occur.SHOULD );

                        BooleanQuery bqf = builder2.build();

                        newfilters.add(bqf);

                        log.debug("modified filter:" + bqf.toString());
                        rt_filters.add("fq",bqf.toString());

                        rb.rsp.addToLog( "fq", bqf.toString() );
                    }
                    filters = newfilters;
                    //rb.setFilters( filters );

                }
            }

            if (!negQuery.clauses().isEmpty() )
            {
                /* i match negativi diventano filtri agiunti per esclusione */

                filters.add(negQuery);
                //rb.setFilters( filters );

                log.debug("added negative filter:"+negQuery.toString());
                rt_filters.add("negative-fq",negQuery.toString());
            }

            /* tutte le deleteByQueries diventano filtri aggiuntivi */

            if (deleteByQueries.size()>0)
            {
                filters.addAll(deleteByQueries);

                for(Query dbq : deleteByQueries)
                    rt_filters.add("deleteByQuery",dbq.toString());

                //rb.setFilters( filters );

                log.debug("added deleteByQuery filters:{}",deleteByQueries);
            }

            if (filters.size()>0) {
                rb.setFilters(filters);
            }

            /* se non ci sono match positivi bastano i filtri e non c'è risposta nel realtime shard */
            if (moreDocs.size()==0)
                return null;

            /* preparazione risposta */

            moreDocs.setMaxScore( maxScore );
            moreDocs.setNumFound( moreDocs.size() );

            NamedList nl = new NamedList();
            nl.add("response" , moreDocs );

            NamedList<Object[]> sortVals = doSortValues(moreDocs, sortFields);

            nl.add("sort_values", sortVals);

            long elapsedTime = TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
            nl.add("elapsedTime" , elapsedTime);
            nl.add("RTDocs" , updateLog.size() );

            /* i documenti aggiuntivi vengono messi in un campo speciale della risposta in modo che possano essere mergiati dal RealtimeMergeStrategy */

            rb.rsp.add( RealTimeMergeStrategy.REALTIME_RESPONSE , nl );

            return nl;
        }
        catch(Exception e)
        {
            log.error("req:{}",rb.req.getParams(),e);
            throw e;
        }
	}

    NamedList<Object[]> doSortValues( SolrDocumentList moreDocs, final SortField[] sortFields )
    {
        /* se sono richiesti i field sort values occorre
         *  innanzitutto ordinare i documenti aggiuntivi
         * */

         if (sortFields.length>0)
        {
            Collections.sort( moreDocs ,new Comparator<SolrDocument>(){
                public int compare(SolrDocument doc1,SolrDocument doc2){

                    for (SortField sortField : sortFields) {
                        String field = sortField.getField();

                        int reverse = sortField.getReverse() ? -1 : 1;

                        Comparable val1 = (Comparable) doc1.getFieldValue(field);
                        Comparable val2 = (Comparable) doc2.getFieldValue(field);

                        int c = ObjectUtils.compare(val1, val2);

                        if (c != 0)
                            return c * reverse;
                    }

                    return 0;
                }});
        }

        NamedList<Object[]> sortVals = new NamedList<>();

                /* occorre poi impacchettare i valori nel modo in cui se li aspetta il QueryComponent */

        for (SortField sortField : sortFields) {
            //SchemaField schemaField = schemaFields.get(fld);
            //FieldType ft = null == schemaField? null : schemaField.getType();

            Object[] vals = new Object[moreDocs.size()];

            String field = sortField.getField();

            for (int i = 0; i < vals.length; i++) {
                vals[i] = moreDocs.get(i).getFieldValue(field);
            }

            sortVals.add(sortField.getField(), vals);
        }

        return sortVals;
    }


}