package it.kdm.solr.common;

import it.kdm.solr.client.CoreClient;
import it.kdm.solr.core.Schema;
import it.kdm.solr.core.Session;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.request.SolrQueryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by Paolo_2 on 29/08/15.
 */
public class QueueUtils {

    public final static String PRE_QUEUE = "pre-queue";
    public final static String POST_QUEUE = "post-queue";
    public final static String POST_QUEUE_DELAY = "post-queue-delay";
    public final static int QUEUE_DELAY = 650;
    private transient static Logger log = LoggerFactory.getLogger(QueueUtils.class);


    private static UpdateRequest getQueue(SolrQueryRequest req, String queue)
    {
        UpdateRequest ureq = (UpdateRequest) req.getContext().get( queue );

        if (ureq==null)
        {
            ureq = new UpdateRequest();
            ureq.setParam( Schema.Params.TICKET , Session.ROOTTICKET );
            ureq.getParams().set( Schema.Params.SHORTCUT , "true" );

            req.getContext().put( queue , ureq );
        }
        return ureq;
    }

    @SuppressWarnings("unchecked")
	public static int queuesSize(SolrQueryRequest req)
	{
		int size = 0;
		UpdateRequest ureq = (UpdateRequest) req.getContext().get( PRE_QUEUE );

		if (ureq!=null)
			size += ureq.getDeleteQuery().size();

		ureq = (UpdateRequest) req.getContext().get( POST_QUEUE );

		if (ureq!=null)
			size += ureq.getDocumentsMap().size();

		ureq = (UpdateRequest) req.getContext().get( POST_QUEUE_DELAY );

		if (ureq!=null)
			size += ureq.getDocumentsMap().size();

		return size;
	}

    public static void queueClean( SolrQueryRequest req, String id , boolean delay )
	{
		/*SimpleOrderedMap<Object> updDoc = new SimpleOrderedMap<>();
		updDoc.add( Fields.ID , id );
		updDoc.add( Fields._OPERATION_ , Operations.SYNC );
		updDoc.add( Fields.STATE , (Object) docFlags.CLEAN  );

		SimpleOrderedMap<Object> som = new SimpleOrderedMap<>();
		som.add( "doc" , updDoc );*/

		SolrInputDocument updDoc = new SolrInputDocument();
		updDoc.addField( Schema.Fields.ID , id );
		updDoc.addField( Schema.Fields._OPERATION_ , Schema.Operations.SYNC );
		updDoc.addField( Schema.Fields.STATE , Schema.docFlags.CLEAN );

        /* il delay è usato per il clean del parent dopo il clean dei figli */

		getQueue(req, delay ? POST_QUEUE_DELAY : POST_QUEUE).add(updDoc);

        log.debug("queued id:{} delay:{}",id,delay);
	}

    public static void queueDeleteCacheEntry( SolrQueryRequest req, String id )
	{
		String query = CoreClient.Query.makeClause( Schema.Fields.CACHEENTRY , id ) + " +NOW:" + new Date().getTime() ;
		getQueue(req, PRE_QUEUE).deleteByQuery(query);

        log.debug("queued delete id:{} query:{}",id,query);
	}
}
