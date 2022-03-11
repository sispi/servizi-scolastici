package it.kdm.solr.providers;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HashUpdateProcessorFactory
        extends UpdateRequestProcessorFactory
        implements SolrCoreAware {

    private boolean enabled = true;

    private static Logger log = LoggerFactory.getLogger(HashUpdateProcessorFactory.class);

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

        public UpdateCacheProcessor(SolrQueryRequest req, SolrQueryResponse rsp, HashUpdateProcessorFactory factory, UpdateRequestProcessor next) {
            super(next);
            this.req = req;
        }

        @Override
        public void processAdd(AddUpdateCommand cmd) throws IOException {

            if (enabled)
            {
                SolrInputDocument doc = cmd.getSolrInputDocument();

                Object hash = req.getContext().get(FileSystemProviderExt.CONTENT_HASH);

                if (hash!=null)
                    doc.setField(FileSystemProviderExt.CONTENT_HASH, hash );

            }

            super.processAdd(cmd);
        }
    }
}