package it.kdm.solr.components;

import it.kdm.solr.core.Schema.Fields;
import it.kdm.solr.common.DocUtils;
import it.kdm.solr.core.ContentCacheManager;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.update.AddUpdateCommand;
import org.apache.solr.update.DeleteUpdateCommand;
import org.apache.solr.update.processor.UpdateRequestProcessor;
import org.apache.solr.update.processor.UpdateRequestProcessorFactory;
import org.apache.solr.util.plugin.SolrCoreAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CacheUpdateProcessorFactory
  extends UpdateRequestProcessorFactory 
  implements SolrCoreAware {

  private static Logger log = LoggerFactory.getLogger(CacheUpdateProcessorFactory.class);
  private boolean enabled = true;

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
	private final SolrQueryResponse rsp;
	//private String finalMessage = "";
	
    public UpdateCacheProcessor(SolrQueryRequest req,
        SolrQueryResponse rsp, CacheUpdateProcessorFactory factory,
        UpdateRequestProcessor next) {
      super(next);
      this.req = req;
	  this.rsp = rsp;
    }

    @Override
    public void processAdd(AddUpdateCommand cmd) throws IOException {

        if (enabled)
        {
            SolrInputDocument doc = cmd.getSolrInputDocument();

            long version = (long) doc.getFieldValue("_version_");

            Date indexed_on = new Date(version >>> 20);

            doc.setField(Fields.INDEXED_ON , indexed_on );

            ContentCacheManager.getInstance().updateCacheEntry( req, DocUtils.toSolrDocument(doc) );

            log.trace("processUpdate id:{} indexed_on:{} core:{}",doc.getFieldValue("id"), indexed_on,req.getCore().getName() );
        }

		super.processAdd(cmd);
    }

    @Override
    public void processDelete(DeleteUpdateCommand cmd) throws IOException {

        if (enabled)
        {
            String query = cmd.getQuery();
            String id = cmd.getId();

            if (query != null && query.contains(Fields.CACHEENTRY))
            {
                // nella forma :            __cacheEntry__:(....)
                // sfrutta il distributorUpdate per propagare l'eliminazione a tutti gli shard

                Pattern p = Pattern.compile( Fields.CACHEENTRY + ":([^\\s]+).*");
                Matcher m = p.matcher(query);

                if (!m.find())
                    throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "Invalid query:"+ query );

                String cacheEntry = m.group(1);

                ContentCacheManager.getInstance().deleteCacheEntry( req, cacheEntry );

                log.trace("processDelete deleteCacheEntry by directive from core:{} directive:{} id:{}" , req.getCore().getName(),query,cacheEntry );

                /* non deve seguire la catena dei processors */
                return;
            }
            else if (query!=null)
            {
                /* dovrei cancellare solo i documenti nella query ma non vale la pena */
                ContentCacheManager.getInstance().clear( req );

                log.trace("processDelete cached cleared from core:{} query:{}" , req.getCore().getName(),query );
            }
            else if (id!=null)
            {
                /* dovrebbe arrivare a tutti gli shard perchè anche q è impostato da prima */
                ContentCacheManager.getInstance().deleteCacheEntry( req, id );

                log.trace("processDelete deleteCacheEntry by Id from core:{} id:{}" , req.getCore().getName(),id );
            }
        }

		super.processDelete(cmd);
	}
  }

  


}