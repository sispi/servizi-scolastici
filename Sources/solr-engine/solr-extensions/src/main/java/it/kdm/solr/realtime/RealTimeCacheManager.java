
package it.kdm.solr.realtime;

//import org.apache.solr.util;

import it.kdm.solr.client.CoreClient;
import it.kdm.solr.common.FieldUtils;
import it.kdm.solr.core.ContentCacheManager;
import it.kdm.solr.core.Schema;
import it.kdm.solr.core.Session;
import org.apache.solr.common.SolrException;

import org.apache.solr.common.util.StrUtils;
import org.apache.solr.request.SolrQueryRequest;


import it.kdm.solr.core.Schema.Fields;


import org.apache.solr.common.SolrDocumentList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.solr.common.SolrDocument;

import org.apache.solr.search.SolrCache;

import it.kdm.solr.core.Schema.Params;

public class RealTimeCacheManager extends ContentCacheManager {

	public static Logger log = LoggerFactory.getLogger(RealTimeCacheManager.class);

    protected static final int MAXCHILDREN = 10000;
    public static final CacheEntry EXPIRED = new CacheEntry(null);

    protected final String NAMECACHE = "nameCache";
	
	/*protected static RealTimeCacheManager instance = null;
	
	public RealTimeCacheManager(String cache)
	{
		super(cache);
		
		log.info("realtime services are activated");
	}*/
	
	public static RealTimeCacheManager getInstance()
	{
        return (RealTimeCacheManager) ContentCacheManager.getInstance();
		/*if (instance==null)
		{
			instance = new RealTimeCacheManager( "nameCache" );
		}
		
		return instance;*/
	}

    protected SolrCache getCache( SolrQueryRequest req )
    {
        return req.getSearcher().getCache(NAMECACHE);
    }

    public Boolean supportCache()
    {
        return true;
    }

	@Override
	@SuppressWarnings("unchecked")
	public ContentCacheManager.CacheEntry getCacheEntry( SolrQueryRequest req, String id )
	{
        log.info("get from cache {}", id);

		Schema schema = Schema.get(req);
		
		SolrCache cache = getCache(req);
		
		CacheEntry entry = (CacheEntry) cache.get(id);
		
		if (entry != null && !entry.isExpired() )
        {
            log.debug("cache entry retrieved from cache {}\n{}",id,entry);
			return entry;
        }

        entry = super.getCacheEntry(req, id);

        updateCacheEntry(req, entry);

        return entry;
		
		/*log.trace( "getCacheEntry get from core:{} id:{}\n{}",req.getCore().getName(), id, req );
		
		SolrQuery params = new SolrQuery();
		
		try
		{
			params.set( Params.TICKET , Session.ROOTTICKET );
			params.set( Params.SHORTCUT , true );
			params.set( Fields.ID , id );
			params.setFields( CACHE_FIELDS );
			//params.addField( schema.getRouteField() );
			//params.set( ShardParams.SHARDS , shard );
					
			SolrDocument doc = CoreClient.getInstance().get( params );

			return updateCacheEntry( req, doc );	
		}
		catch( Exception e )
		{
			log.error( "getCacheEntry: id:{} params:{}", id, params , e );
			return null;
		}*/
	}

    @Override
    public void clear( SolrQueryRequest req )
    {
        log.info("clear core:{}",req.getCore().getName());

        SolrCache cache = getCache(req);

        synchronized (cache)
        {
            cache.clear();
        }
    }

    @Override
	@SuppressWarnings("unchecked")
	public void deleteCacheEntry( SolrQueryRequest req, String id )
	{
        log.info("delete key {}",id);

        if (log.isTraceEnabled())
            log.trace("delete key:{} core:{}",id,req.getCore().getName());

		SolrCache cache = getCache(req);
		
		CacheEntry entry = (CacheEntry) cache.get(id);
		
		if (entry!=null)
		{
			String key = "/"+ entry.getName().toLowerCase();
		
			if (entry.getParent() !=null)
				key = entry.getParent() + key;
			
			cache.put( key , EXPIRED );
            cache.put( id , EXPIRED );

            log.debug("removed from cache {} and {}",id,key);
		}
	}

    @Override
    @SuppressWarnings("unchecked")
    public void updateCacheEntry( SolrQueryRequest req, SolrDocument doc )
    {
        updateCacheEntry( req, new CacheEntry(doc) );
    }

    @Override
	@SuppressWarnings("unchecked")
	public void updateCacheEntry( SolrQueryRequest req, CacheEntry entry )
	{
        if (entry==null)
        {
            log.info("update key {null}");
            return;
        }

        if (entry.containsKey(Fields.UNCOMMITTED) && "false".equalsIgnoreCase(entry.get(Fields.UNCOMMITTED).toString()))
        {
            log.info("uncommitted==false not cached id:{} core:{}",entry.get(Schema.Fields.ID),req.getCore().getName());
            return;
        }

        log.info("update key {}", entry.getId());

        if (log.isTraceEnabled())
            log.trace("update key:{} core:{} entry:\n{} ",entry.getId(),req.getCore().getName(),entry);

		//CacheEntry entry = super.updateCacheEntry(req,doc);
		
		String key = "/"+ entry.getName().toLowerCase();

		if (entry.getParent() !=null)
			key = entry.getParent() + key;
		
		SolrCache cache = getCache(req);
		
		cache.put(entry.getId(), entry );
		
		cache.put( key , entry );

        log.debug("update cache {} and {}\n{}", entry.getId(),key,entry);
		
		if (entry.containsKey(Fields._OLD_NAME_))
		{
			String oldkey = (String) entry.remove(Fields._OLD_NAME_);
			String oldParent = (String) entry.remove(Fields._OLD_PARENT_);
		
			oldkey = "/"+oldkey.toLowerCase();

			if (oldParent!=null)
				oldkey = oldParent + oldkey;
			
			cache.put(oldkey , EXPIRED );

            log.debug("removed orphan cache {}",oldkey);
		}
		
		//return entry;
	}
	
	@SuppressWarnings("unchecked")
	private List<String> searchByParent( SolrQueryRequest req, String parent, String name ) throws Exception
	{
        log.info("get from cache {}/{}", parent, name);

		//SolrCache cache = getCache(req);
		//Schema schema = Schema.get(req);
		
		if ("".equals(parent))
			parent = null;

        if (parent != null && !Schema.get(req).canHaveChildren(parent.split("@")[1]))
            return Collections.emptyList();

        if (parent == null && name.equals("?"))
            return StrUtils.splitSmart(System.getProperty("locations"), ',');

        SolrCache cache = getCache(req);
		
		String key = "/"+name.toLowerCase();
		
		if (parent!=null)
        key = parent + key;
			
		CacheEntry entry = (CacheEntry) cache.get( key );
		
		ArrayList<String> list = new ArrayList<>();
		
		if (entry==null || entry.isExpired() )
		{
			CoreClient.Query params = new CoreClient.Query();
			params.set( Params.TICKET , Session.ROOTTICKET );
			params.set( Params.SHORTCUT , true );
			
			String fq = params.makeClause( Fields.PARENT , parent ) + params.makeClause( Fields.NAME , name );

            /* ai fini del PATH è più efficiente filtrare */
            //if (parent==null)
            //    fq += params.makeClause(Fields.TYPE, Fields.LOCATION) ;
			
			params.addFilterQuery( fq );
			
			//params.addFilterQuery( fq );
			//params.set("fl" , StringUtils.join( CACHE_FIELDS , "," ) );
			params.setFields( CACHE_FIELDS );
			//params.addField( schema.getRouteField() );
			params.setRows( MAXCHILDREN );
			params.set( Params.PROUTE , parent );
		
			SolrDocumentList docs = CoreClient.getInstance().query( params ).getResults();

            if (docs.size()==0)
            {
                log.info("parentQuery empty result params:{}", params);
            }
			
			for( SolrDocument doc : docs )
			{
				String id = (String) doc.getFieldValue(Fields.ID); 
				list.add( id );

                entry = new CacheEntry(doc);

				updateCacheEntry( req, entry );
				
				if (name.equals("**"))
					list.addAll( searchByParent( req, id, name ) );
			}
		}
		else
		{
            log.debug("cache entry retrieved from cache {}/{}\n{}",parent,name,entry);
			list.add(entry.getId());
		}
		
		return list;
	}

    @SuppressWarnings("unchecked")
    public List<String> deepSearchByParent( SolrQueryRequest req, String parent )
    {
        try {

            return searchByParent(req,parent,"**");
        }

        catch( Exception e )
        {
            throw new SolrException( SolrException.ErrorCode.SERVER_ERROR, e);
        }

    }

    @Override
	public List<String> searchByPATH( SolrQueryRequest req, String PATH )
	{
		try
		{
            log.info("PATH:{}",PATH);

			long startTime = System.currentTimeMillis();
		
			List<String> results = new ArrayList<>(1);
			
			if ("".equals(PATH))
				return results;
			
			String[] parts = PATH.split("/",-1);
			
			if (parts.length==1)
			{
				results.add(PATH);
				return results;
			}
			
			List<String> ids = searchByParent( req, parts[0], parts[1] );

            if (ids.size() == 0)
            {
                log.debug("path path: {} empty parts0:{} parts1:{}", PATH, parts[0], parts[1]);
            }
			
			if (parts.length==2)
				return ids;
				
			PATH = PATH.substring( parts[0].length() + parts[1].length() + 1 );
			
			for( String id : ids )
				results.addAll( searchByPATH( req, id+PATH ) );
			
			long stopTime = System.currentTimeMillis();
			long elapsedTime = stopTime - startTime;
			
			log.trace("path path: {} elapsedTime:{}" ,PATH,elapsedTime );
				
			return results;
			

		}
		catch( Exception e )
		{
			throw new SolrException( SolrException.ErrorCode.SERVER_ERROR, e);
		}
	}

    @Override
	public String buildPATH( SolrQueryRequest req,  String id , String PATH ) throws Exception
	{
        if (PATH==null)
			PATH = "";
		
		CacheEntry entry = getCacheEntry(req, id);

        if (entry==null)
            throw new SolrException( SolrException.ErrorCode.CONFLICT, "entry not found" );
		
		PATH = "/" + entry.getName() + PATH ;
		
		if ( entry.getParent() == null )
			return PATH;
		else //if (entry.id.equals(entry.acl_parent))
			return buildPATH( req, entry.getParent(), PATH );
		/*else
		{
			Schema schema = Schema.get(req);
			
			DocCollection coll = CoreClient.getInstance().getZkStateReader().getClusterState().getCollection( schema.collectionName );
		
			shard = Schema.getRouter().getTargetSlice( entry.route , null , null , coll ).getName();
			
			return buildPATH( req, entry.parent, shard, PATH );
		}*/
	}
	
	
	
}

