package it.kdm.solr.realtime;

import it.kdm.solr.client.SolrClient;
import it.kdm.solr.core.ExpirableRegenerator;
import org.apache.solr.core.AbstractSolrEventListener;
import org.apache.solr.core.QuerySenderListener;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrRequestInfo;
import org.apache.solr.search.SolrCache;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.update.DirectUpdateHandler2;
import org.apache.solr.update.UpdateLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by Paolo_2 on 29/04/17.
 */
public class RealTimeListener extends AbstractSolrEventListener {

    public static final Logger log = LoggerFactory.getLogger(RealTimeIndex.class);

    public RealTimeListener(SolrCore core) {
        super(core);
    }

    @Override
    public void newSearcher(final SolrIndexSearcher newSearcher, SolrIndexSearcher currentSearcher) {
        log.info("warmup RTI for core:{}", newSearcher.getCore().getName());
    }
}
