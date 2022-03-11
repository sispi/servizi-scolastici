package it.kdm.solr.core;

import org.apache.solr.search.CacheRegenerator;
import org.apache.solr.search.SolrCache;
import org.apache.solr.search.SolrIndexSearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;

@SuppressWarnings("unchecked")
public class ExpirableRegenerator implements CacheRegenerator {

	public static interface Expirable
	{
		boolean isExpired();
		Object getObject();
	}
	
	public static class CacheEntry implements Expirable
	{
		Object obj;
		Date expires_on;
        Date inserted_on;
		
		public CacheEntry( Object obj , int seconds )
		{
			this.obj = obj;
			this.expires_on = new Date( System.currentTimeMillis() + (long) seconds * 1000L );
            this.inserted_on = new Date (System.currentTimeMillis());
		}
		
		@Override
		public String toString()
		{
			if (isExpired())
				return "EXPIRED";
			else
				return obj.toString();
		}
		
		public boolean isExpired()
		{
			return expires_on.before(new Date());
		}

        public Date getInsertedOn()
        {
            return this.inserted_on;
        }
		
		public Object getObject()
		{
			return this.obj;
		}
	}

	private transient static Logger log = LoggerFactory.getLogger(ExpirableRegenerator.class);
	
    @Override
    public boolean regenerateItem(SolrIndexSearcher newSearcher, SolrCache newCache, SolrCache oldCache, Object oldKey, Object oldVal) throws IOException {
		  
		if (oldVal instanceof ExpirableRegenerator.Expirable)
		{
			if ( ((ExpirableRegenerator.Expirable) oldVal).isExpired() )
				return true;
		}
		
		newCache.put(oldKey,oldVal);
		return true;
		
	}
}
	  
	  /*if (oldVal instanceof Session)
	  {
		Session ticket = (Session) oldVal;
		
		if (ticket.isExpired())
			return true;
	  }*/
	  
		/*if (oldVal instanceof NamedList)
		{
			Object expires_on = ((NamedList)oldVal).get("expires_on");
					
			if (expires_on != null && expires_on instanceof Date && ((Date) expires_on).before(new Date()) )
				return true;
		}*/
	  
		
	  
	  /*if (oldVal instanceof ContentCacheManager. getInstance().CacheEntry)
	  {
		Date expires_on = (Date) ((ContentCacheManager. getInstance().CacheEntry)oldVal).expiration;
				
		if (expires_on != null && new Date().getTime() >= expires_on.getTime() )
			return true;
	  }*/

	  //log.info("******** item regenerated ********" ) ;
      
