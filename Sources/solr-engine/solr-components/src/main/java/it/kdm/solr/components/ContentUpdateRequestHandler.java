package it.kdm.solr.components;

//import org.apache.solr.handler.UpdateRequestHandler;

import it.kdm.solr.common.QueueUtils;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.params.UpdateParams;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.handler.RequestHandlerUtils;
import org.apache.solr.handler.loader.ContentStreamLoader;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.update.processor.UpdateRequestProcessor;
import org.apache.solr.update.processor.UpdateRequestProcessorChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;


public class ContentUpdateRequestHandler extends org.apache.solr.handler.UpdateRequestHandler {
	
	public static Logger log = LoggerFactory.getLogger(ContentUpdateRequestHandler.class);
	
	static Set<String> ctypes = null;

    public static boolean hasLoader( String type )
	{
		if (type==null)
			return false;
	
		int idx = type.indexOf(';');
       if(idx>0) {
          type = type.substring(0,idx);
		}
		
		return ctypes.contains(type);
		
	}
  
  @Override
  @SuppressWarnings("unchecked")
		
  public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp) throws Exception {
  
	if (ctypes==null)
	{
		ctypes = createDefaultLoaders(null).keySet();
	}
  
    SolrParams params = req.getParams();
    UpdateRequestProcessorChain processorChain =
            req.getCore().getUpdateProcessingChain(params.get(UpdateParams.UPDATE_CHAIN));

    UpdateRequestProcessor processor = processorChain.createProcessor(req, rsp);

    try {
      ContentStreamLoader documentLoader = newLoader(req, processor);


      Iterable<ContentStream> streams = req.getContentStreams();
      if (streams == null) {
        if (!RequestHandlerUtils.handleCommit(req, processor, params, false) && !RequestHandlerUtils.handleRollback(req, processor, params, false)) {
          throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "missing content stream");
        }
      } else {

        for (ContentStream stream : streams) {
		
			try
			{
				documentLoader.load(req, rsp, stream, processor);
			}
			catch(  SolrException exc )
			{
				if (exc.code() != SolrException.ErrorCode.UNSUPPORTED_MEDIA_TYPE.code )
					throw exc;
			}
        }
		
		java.util.Map ctx = req.getContext();
		
		//while (ctx.containsKey(PRE_QUEUE) || ctx.containsKey(POST_QUEUE) || ctx.containsKey(POST_QUEUE_DELAY))
		//{
        UpdateRequest preQueue = (UpdateRequest) ctx.remove(QueueUtils.PRE_QUEUE);

        if (preQueue != null)
        {
            if (log.isTraceEnabled())
                log.trace("processing queue deletes \n{}", preQueue.getXML());

            documentLoader.load(req, rsp, preQueue.getContentStreams().iterator().next() , processor);
        }

        UpdateRequest postQueue = (UpdateRequest) ctx.remove(QueueUtils.POST_QUEUE);

        if (postQueue != null)
        {
            if (log.isTraceEnabled())
                log.trace("processing queue children clean \n{}", postQueue.getXML());

            if (preQueue != null)
            {
                log.debug("wait for preQueue queue to propagate for {}ms", QueueUtils.QUEUE_DELAY);
                Thread.sleep(QueueUtils.QUEUE_DELAY);
                preQueue = null;
            }

            documentLoader.load(req, rsp, postQueue.getContentStreams().iterator().next() , processor);
        }

        UpdateRequest postQueueDelay = (UpdateRequest) ctx.remove(QueueUtils.POST_QUEUE_DELAY);

        if (postQueueDelay != null)
        {
            if (log.isTraceEnabled())
                log.trace( "processing queue parent clean \n{}" , postQueueDelay.getXML() );

            if (postQueue != null || preQueue != null)
            {
                log.debug("wait for previous queue to propagate {}ms", QueueUtils.QUEUE_DELAY);
                Thread.sleep(QueueUtils.QUEUE_DELAY);
            }

            documentLoader.load(req, rsp, postQueueDelay.getContentStreams().iterator().next() , processor);
        }
		//}

        // Perhaps commit from the parameters
        RequestHandlerUtils.handleCommit(req, processor, params, false);
        RequestHandlerUtils.handleRollback(req, processor, params, false);
      }
    } 
	catch(Exception e)
	{
		log.error( "Problem executing handler: \n"+e.getMessage(), e);
		throw e;
	}
	finally {
      // finish the request
      processor.finish();
    }
  }
  
}