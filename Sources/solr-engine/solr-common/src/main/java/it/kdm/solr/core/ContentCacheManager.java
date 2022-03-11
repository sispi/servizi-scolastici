
package it.kdm.solr.core;

//import org.apache.solr.util;

import it.kdm.solr.client.CoreClient;
import it.kdm.solr.common.FieldUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.request.SolrQueryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;



public class ContentCacheManager {

	public static Logger log = LoggerFactory.getLogger(ContentCacheManager.class);
	
	//TODO NTH si potrebbe rivedere lo schema per migliorare le performance del getCacheEntry utilizzando i DocValues
    protected static final String[] CACHE_FIELDS = new String[] { Schema.Fields.ID, Schema.Fields.SEQUENCE, Schema.Fields._VERSION_ , Schema.Fields.NAME , Schema.Fields.DISPLAY_NAME, Schema.Fields.PARENT, Schema.Fields.ENABLED , Schema.Fields.HIDDEN , Schema.Fields.ACL_PARENT , Schema.Fields.ACL_SEQUENCE , Schema.Fields.LOCATION } ;

    protected static final int DURATION = 60*60*1000;

	protected static ContentCacheManager instance = null;
	
	public ContentCacheManager()
	{
		//this.NAMECACHE = cache;
		//CACHE_FIELDS = new String[] { Schema.Fields.ID, Schema.Fields.DIVISION, Schema.Fields.SEQUENCE, Schema.Fields._VERSION_ , Schema.Fields.NAME , Schema.Fields.PARENT, Schema.Fields.ENABLED , Schema.Fields.ACL_PARENT , Schema.Fields.ACL_SEQUENCE , Schema.Fields.LOCATION } ;
	}

	public static boolean isCachedField(String field)
    {
        return Arrays.asList(CACHE_FIELDS).contains(field);
    }
	
	@SuppressWarnings("unchecked")
	public static ContentCacheManager getInstance()
	{
		if (instance==null)
		{
            try
            {
                Class cls = Class.forName( System.getProperty("cachemanager" , ContentCacheManager.class.getName() ) );
                instance = (ContentCacheManager) cls.newInstance();
                log.info("CacheManager instantiated by class {}",cls);
            }
            catch( Exception e )
            {
                log.error("Could not get cache manager instance",e);
                throw new RuntimeException(e);
            }

			/*try
			{
				Class cls = Class.forName( "RealTimeCacheManager" );
				
				instance = (ContentCacheManager) cls.getMethod("getInstance").invoke(null);
			}
			catch( Exception e )
			{				
				instance = new ContentCacheManager( "nameCache" );
			}*/
		}
		
		return instance;
	}

    public void deleteCacheEntry( SolrQueryRequest req, String id )
    {
    }

    public void updateCacheEntry( SolrQueryRequest req, SolrDocument doc )
    {
    }

    public void updateCacheEntry( SolrQueryRequest req, CacheEntry entry )
    {
    }

    public List<String> searchByPATH( SolrQueryRequest req, String PATH )
    {
        throw new UnsupportedOperationException("'searchByPath' is not supported by current profile");
    }

    public String buildPATH( SolrQueryRequest req,  String id , String PATH ) throws Exception
    {
        return null;
    }

    public void clear( SolrQueryRequest req )
    {
    }

    @SuppressWarnings("unchecked")
	public ContentCacheManager.CacheEntry getCacheEntry( SolrQueryRequest req, String id )
	{
        log.info("getCacheEntry get from core:{} id:{}\n{}", req.getCore().getName(), id, req);
 		SolrQuery params = new SolrQuery();
		
		try
		{
			params.set( Schema.Params.TICKET , Session.ROOTTICKET );
			params.set( Schema.Params.SHORTCUT , true );
			params.set( Schema.Fields.ID , id );
			params.setFields( CACHE_FIELDS );
					
			SolrDocument doc = CoreClient.getInstance().get( params );

            return new CacheEntry(doc);
			//return updateCacheEntry( req, doc );
		}
		catch( Exception e )
		{
            log.error( "getCacheEntry: id:{} params:{}", id, params , e );
			return null;
		}
	}
	
	/*@SuppressWarnings("unchecked")
	public ContentCacheManager.CacheEntry updateCacheEntry( SolrQueryRequest req, SolrDocument doc )
	{
		CacheEntry entry = new CacheEntry();
		

		
		return entry;
	}*/

	
	public List<String> getParents( SolrQueryRequest req, String id ) throws Exception 
	{
        log.info("requested parents for {}",id);
		if (id == null)
			return null;
		else 
			return getParents(req,id, new ArrayList<String>(1) );
	}
	
	@SuppressWarnings("unchecked")
	private List<String> getParents( SolrQueryRequest req, String id , List<String> parentIds ) throws Exception
	{
		ContentCacheManager.CacheEntry entry = this.getCacheEntry(req, id);
		
		if (entry!=null && entry.getParent() ==null)
			return parentIds;
		
		if (entry==null)
		{
			log.error("invalid cache entry null id:{} parentIds:{} " ,id,parentIds);
			return parentIds;
		}
		
		if (parentIds.contains(entry.getParent()))
		{
			log.error("recursive parents!!!!! id:{} parentIds:{} parent:{}" ,id,parentIds, entry.getParent());
			return parentIds;
		}
		
		parentIds.add(0, entry.getParent());
		
		if (parentIds.size() > 20)
			log.warn("too deep node {}",id);
				
		return getParents( req, entry.getParent(), parentIds );
	}
	
	public boolean isAttached(SolrQueryRequest req, String id)
	{
		ContentCacheManager.CacheEntry entry = this.getCacheEntry(req,id);

        if (entry==null || !entry.isEnabled())
			return false;

        return entry.getParent() == null || isAttached(req, entry.getParent());
	}
	
	public static class CacheEntry extends SolrDocument implements ExpirableRegenerator.Expirable
	{
		/*private final String id;
		private final String name;
		private final String acl_parent;
		private final Long acl_sequence;
		private final String parent;
		private final Boolean enabled;
		private final String division;

        private final Long sequence;
        private final String location;*/

        private final Date expires_on;

        public CacheEntry( SolrDocument doc )
        {
            //this.id = (String) doc.getFieldValue(Schema.Fields.ID);
            //this.parent = (String) doc.getFieldValue(Schema.Fields.PARENT);
            //this.name = (String) doc.getFieldValue(Schema.Fields.NAME);
            //this.enabled = FieldUtils.parseBool( doc.getFieldValue(Schema.Fields.ENABLED) , true );
            //this.location = (String) doc.getFieldValue(Schema.Fields.LOCATION);
            //this.division = (String) doc.getFieldValue(Schema.Fields.DIVISION);
            //this.acl_sequence = (Long) doc.getFieldValue(Schema.Fields.ACL_SEQUENCE);
            //this.sequence = (Long) doc.getFieldValue(Schema.Fields.SEQUENCE);
            //this.acl_parent = (String) doc.getFieldValue(Schema.Fields.ACL_PARENT);

            if (doc!=null)
            {
                this.putAll(doc);
                this.expires_on = new Date( System.currentTimeMillis() + DURATION );
            }
            else
            {
                this.expires_on = new Date( System.currentTimeMillis() -1 );
            }
        }
		
		@Override
		public String toString()
		{
			if (isExpired())
				return "EXPIRED";
			else
				return String.format("id:%s parent:%s name:%s enabled:%s hidden:%s expires_on:%s", getId(),getParent(), getName(), isEnabled(), isHidden(), expires_on );
		}
		
		public boolean isExpired()
		{
			return expires_on.before(new Date());
		}
		
		public Object getObject()
		{
			return this;
		}

        public String getId() {
            return (String) getFieldValue(Schema.Fields.ID);
        }

        public String getName() {
            return (String) getFieldValue(Schema.Fields.NAME);
        }

        public String getAclParent() {
            return (String) getFieldValue(Schema.Fields.ACL_PARENT);
        }

        public String getParent() {
            return (String) getFieldValue(Schema.Fields.PARENT);
        }

        public Boolean isEnabled() {
            return FieldUtils.parseBool(getFieldValue(Schema.Fields.ENABLED), true);
        }

        public Boolean isHidden() {
            return FieldUtils.parseBool(getFieldValue(Schema.Fields.HIDDEN), false);
        }

        public String getDisplayName() {
            return (String) getFieldValue(Schema.Fields.DISPLAY_NAME);
        }

        /*public String getDivision() {
            return (String) getFieldValue(Schema.Fields.DIVISION);
        }*/

        public Long getAclSequence() {
            return (Long) getFieldValue(Schema.Fields.ACL_SEQUENCE);
        }

        public Long getSequence() {
            return (Long) getFieldValue(Schema.Fields.SEQUENCE);
        }
    }
	
}

