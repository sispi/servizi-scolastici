package it.kdm.solr.components;

import it.kdm.solr.core.Schema;
import it.kdm.solr.core.Schema.Fields;
import it.kdm.solr.core.Schema.Params;
import it.kdm.solr.core.Schema.Rights;
import it.kdm.solr.client.CoreClient;
import it.kdm.solr.common.DocUtils;
import it.kdm.solr.client.zkClient;
import it.kdm.solr.core.ContentManager;
import it.kdm.solr.core.ProviderProxy;
import it.kdm.solr.core.Session;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.search.QParser;
import org.apache.solr.update.AddUpdateCommand;
import org.apache.solr.update.CommitUpdateCommand;
import org.apache.solr.update.DeleteUpdateCommand;
import org.apache.solr.update.RollbackUpdateCommand;
import org.apache.solr.update.processor.UpdateRequestProcessor;
import org.apache.solr.update.processor.UpdateRequestProcessorFactory;
import org.apache.solr.util.plugin.SolrCoreAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ContentUpdateProcessorFactory extends UpdateRequestProcessorFactory implements SolrCoreAware {

  //private List<String> sigFields;
  
  public static Logger log = LoggerFactory.getLogger(ContentUpdateProcessorFactory.class);

  private boolean enabled = true;

    @Override
  public void init(final NamedList args) {
    if (args != null) {
      SolrParams params = SolrParams.toSolrParams(args);
      //boolean enabled = params.getBool("enabled", true);
      this.enabled = params.getBool("enabled", true);

    }
  }

  @Override
  public void inform(SolrCore core) {
  }

  public boolean isEnabled() {
    return enabled;
  }
  
  void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public UpdateRequestProcessor getInstance(SolrQueryRequest req,
      SolrQueryResponse rsp, UpdateRequestProcessor next) {

    return new UpdateContentProcessor(req, rsp, this, next);

  }

  class UpdateContentProcessor extends UpdateRequestProcessor {
    private final SolrQueryRequest req;
	private final SolrQueryResponse rsp;
	//private String operation;
	

    public UpdateContentProcessor(SolrQueryRequest req,
        SolrQueryResponse rsp, ContentUpdateProcessorFactory factory,
        UpdateRequestProcessor next) {
      super(next);
      this.req = req;
	  this.rsp = rsp;
    }

    @Override
	@SuppressWarnings("unchecked")
    public void processAdd(AddUpdateCommand cmd) throws IOException {
		
		if (enabled) {
		  
			log.debug("processAdd start \n{} \n{}", cmd.getSolrInputDocument(), req);
			
			//log.debug("processAdd", new TraceMessage(cmd.getSolrInputDocument().toString()));
			
			//Session.setRequestInfo(req,rsp);
			
			//Schema schema = Schema.get(req);

            long t0 = System.currentTimeMillis();
			
			ContentManager workflow = new ContentManager(cmd);
			workflow.run(rsp);
			
			if (log.isTraceEnabled() )	
				log.trace( "processAdd end id:{} ts:{}ms \n{}", workflow.getId() , System.currentTimeMillis()-t0,  workflow.getUpdatedDocument() );
		}

        super.processAdd(cmd);
    }
	
	/*@Override
	@SuppressWarnings("unchecked")
	public void finish() throws IOException {

        long t0 = System.currentTimeMillis();

        log.debug( "finish start \n{}", req );

        //NamedList<Object> nl = (NamedList<Object>) req.getContext().remove(ContentUpdateRequestHandler.POST_QUEUE);
		
		UpdateRequest preQueue = (UpdateRequest) req.getContext().remove(ContentUpdateRequestHandler.PRE_QUEUE);
		UpdateRequest postQueue = (UpdateRequest) req.getContext().remove(ContentUpdateRequestHandler.POST_QUEUE);
		UpdateRequest postQueueDelay = (UpdateRequest) req.getContext().remove(ContentUpdateRequestHandler.POST_QUEUE_DELAY);
		
		if (preQueue!=null || postQueue!=null || postQueueDelay!=null)
		{
			try
			{
				CoreClient client = CoreClient.getInstance();
				if (preQueue != null)
				{
					log.trace( "processing queue deletes \n{}", preQueue.getXML() );
					client.request( preQueue );
				}
				
				if (postQueue != null)
				{
					log.trace( "processing queue children clean \n{}", postQueue.getXML() );
					
					if (preQueue != null)
						Thread.sleep(ContentUpdateRequestHandler.QUEUE_DELAY);
					
					client.request( postQueue );
				}
				
				if (postQueueDelay != null)
				{
					log.trace( "processing queue parent clean \n{}" , postQueueDelay.getXML() );
					
					if (postQueue != null)
						Thread.sleep(ContentUpdateRequestHandler.QUEUE_DELAY);
					
					client.request( postQueueDelay );
				}
			}
			catch(Exception e)
			{
				log.error("error processing post queue" , e);
			}
		}	
	
		super.finish();

        if (log.isTraceEnabled() )
            log.trace( "finish end ts:{}ms", System.currentTimeMillis()-t0 );

    }*/

    @Override
    public void processDelete(DeleteUpdateCommand cmd) throws IOException {
	
		if (enabled)
		{
            long t0 = System.currentTimeMillis();

			String query = cmd.getQuery();
            String id = cmd.getId();

            Session session = Session.get(req);

            if (query!=null && !query.contains(Fields.CACHEENTRY))
            {
                log.trace( "processDelete by query: \n{}", query );

                if ((session.getGlobalAcl() & Rights.delete) == 0)
                    throw new SolrException(SolrException.ErrorCode.FORBIDDEN, Schema.NOLOG+ "Delete global rights are needed to delete by query");

                try
                {
                    Query q = QParser.getParser(query,"lucene",req).getQuery();

                    if (q instanceof MatchAllDocsQuery)
                    {
                        long sequence = zkClient.checkCounter(0L);
                        rsp.add("sequence",sequence);
                    }
                }
                catch(Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
            else if (id!=null)
			{
                log.trace( "processDelete by id: \n{}", id );

                //TODO si potrebbe implementare verifica delle proprietà collegate *:<id> per impedire la cancellazione

                /* recupero l'intero documento */

                CoreClient client = CoreClient.getInstance();

                SolrQuery params = new SolrQuery();
                params.set( Params.TICKET , Session.ROOTTICKET );
                params.set( Params.SHORTCUT , true );
                params.set( Fields.ID , id );
                //params.set( ShardParams._ROUTE_ , req.getParams().get( ShardParams._ROUTE_ ) );
                //params.setFields( Fields.COMMON_FIELDS );

                SolrDocument oldDoc;
                try
                {
                    oldDoc = client.get( params );
                }
                catch( SolrServerException sse )
                {
                    throw new SolrException( SolrException.ErrorCode.SERVER_ERROR , sse );
                }

                /* verifico la sicurezza */

                if ((session.getGlobalAcl() & Rights.delete) == 0 )
                {
                    int rights = DocUtils.getUserRights(req, oldDoc, Rights.delete);

                    if (rights==0)
                        throw new SolrException( SolrException.ErrorCode.FORBIDDEN , "User can't delete "+id );
                }

                /* verifico se c'è un contenuto */

				String content_id = (String) oldDoc.getFieldValue( Fields.CONTENT_ID ) ;

                String version = req.getParams().get( "version_id" );

                /* se si vuole eliminare una versione non è una vera eliminazione */

                if (content_id != null && version != null)
                {
                    ProviderProxy provider = new ProviderProxy(oldDoc,req);

                    rsp.add("deleteVersion",version);
                    provider.deleteVersion( Integer.parseInt(version) );
                    return;
                }

                /* verifico che non abbia figli */


                String q = CoreClient.Query.makeClause( Fields.PARENT , id );

                params = new SolrQuery();
                params.set( Params.TICKET , Session.ROOTTICKET );
                params.set( Params.SHORTCUT , true );
                params.setQuery( q );
                params.setRequestHandler( "/selectall" );
                params.addField( Fields.ID );
                params.addField( Fields.INDEXED_ON );
                params.setRows(0);
                params.set( Params.PROUTE , id );

                try
                {
                    SolrDocumentList children = client.query( params ).getResults();

                    if (children.getNumFound() > 0)
                        throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, Schema.NOLOG+ "Node is not empty");
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }

                if (content_id != null)
                {
                    ProviderProxy provider = new ProviderProxy(oldDoc,req);

                    provider.delete();
                }

                /* questo fa sì che il comando venga distribuito ai vari shards */
				cmd.setQuery( CoreClient.Query.makeClause( Fields.ID , id ) );

			}

            if (log.isTraceEnabled() )
                log.trace("processDelete end ts:{}ms", System.currentTimeMillis() - t0);
		}

        super.processDelete(cmd);
	}

    @Override
    public void processRollback(RollbackUpdateCommand cmd) throws IOException
    {
        throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "rollback is not supported");
        /*super.processRollback(cmd);

        if (enabled)
        {
            log.info( "rollback()" );
            long value = ContentManager.initCounter();
            rsp.add("sequence",value);
        }*/
    }
	
	@Override
	public void processCommit(CommitUpdateCommand cmd) throws IOException
	{
        super.processCommit(cmd);

        if (enabled)
        {
            log.debug( "processCommit start \n{}", cmd );

            long t0 = System.currentTimeMillis();

            /* con l'optimize si reimposta anche il conter al valore massimo+1 */

            if (cmd.optimize)
            {
                long value = zkClient.initCounter();
                rsp.add("sequence",value);
            }

            if (log.isTraceEnabled() )
                log.trace( "processCommit end ts:{}ms", System.currentTimeMillis()-t0 );
        }
	}
	
  }

  


}