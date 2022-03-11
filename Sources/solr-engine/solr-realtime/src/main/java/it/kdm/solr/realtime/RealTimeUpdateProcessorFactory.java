package it.kdm.solr.realtime;

import java.io.IOException;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.update.AddUpdateCommand;
import org.apache.solr.core.SolrCore;
import org.apache.solr.util.plugin.SolrCoreAware;
import org.apache.solr.update.processor.UpdateRequestProcessorFactory;
import org.apache.solr.update.processor.UpdateRequestProcessor;
import org.apache.solr.update.DeleteUpdateCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RealTimeUpdateProcessorFactory
  extends UpdateRequestProcessorFactory 
  implements SolrCoreAware {

  private boolean enabled = true;
  
  private static Logger log = LoggerFactory.getLogger(RealTimeUpdateProcessorFactory.class);

      //private SolrParams params;

  @Override
  public void init(final NamedList args) {
    if (args != null) {
      SolrParams params = SolrParams.toSolrParams(args);
      this.enabled = params.getBool("enabled", true);
    }
  }

  @Override
  public void inform(SolrCore core) {
  }

  @Override
  public UpdateRequestProcessor getInstance(SolrQueryRequest req,
      SolrQueryResponse rsp, UpdateRequestProcessor next) {

    return new UpdateCacheProcessor(req, rsp, this, next);
  }

  class UpdateCacheProcessor extends UpdateRequestProcessor {
    private final SolrQueryRequest req;

    public UpdateCacheProcessor(SolrQueryRequest req, SolrQueryResponse rsp, RealTimeUpdateProcessorFactory factory, UpdateRequestProcessor next) {
      super(next);
      this.req = req;
    }

    @Override
    public void processAdd(AddUpdateCommand cmd) throws IOException {

        if (enabled)
        {
            SolrInputDocument doc = cmd.getSolrInputDocument();

            RealTimeIndex rti = RealTimeIndex.getInstance();

            rti.addDocument(req, doc);

            if (log.isTraceEnabled())
                log.trace("processUpdate add id:{} core:{} doc:\n{}",doc.getFieldValue("id"), req.getCore().getName(),doc );
        }

		super.processAdd(cmd);
    }

    @Override
    public void processDelete(DeleteUpdateCommand cmd) throws IOException {
	
		String query = cmd.getQuery();
        String id = cmd.getId();

        RealTimeIndex rti = RealTimeIndex.getInstance();
		
		if (query != null)
        {
            rti.removeByQuery(req, query, cmd.getVersion());

            if (log.isTraceEnabled())
                log.trace("processDelete deleteByQuery from core:{} query:{}" , req.getCore().getName(),query );
        }
        else if (id!=null)
        {
            rti.removeDocument(req,id);

            if (log.isTraceEnabled())
                log.trace("processDelete deleteById from core:{} id:{}" , req.getCore().getName(),id );
        }
		
		super.processDelete(cmd);
	}
  }
}