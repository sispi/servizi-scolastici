package it.kdm.solr.realtime;

import org.apache.solr.handler.component.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Paolo_2 on 10/05/17.
 */
public class RealTimeQueryComponentExt extends RealTimeQueryComponent {

    private transient static Logger log = LoggerFactory.getLogger(RealTimeQueryComponentExt.class);

    @Override
    public void prepare(ResponseBuilder rb) throws IOException
    {
        super.prepare(rb);
//        rb.addMergeStrategy( new RealTimeMergeStrategy() );
//
//        /* per aggiungere implicitamente augmenters quando si chiedono tutti i campi */
//
//        String[] morefls = rb.req.getParams().getParams("implicitAugmenters");
//
//        if (rb.rsp.getReturnFields().wantsAllFields() && morefls!=null && morefls.length>0)
//        {
//            ModifiableSolrParams params = new ModifiableSolrParams(rb.req.getParams());
//            params.add(CommonParams.FL,morefls);
//            rb.req.setParams(params);
//
//            log.trace("changed fields by augmenters new:{}",morefls);
//        }
//
//        super.prepare(rb);
//
//        /* reimposta i return fields nel caso in cui "fl" della richiesta sia stata modificato da un parser */
//
//        if (rb.req.getParams().getBool("changedFl", false))
//        {
//            ReturnFields returnFields = new SolrReturnFields( rb.req );
//
//            log.trace("changed fields old:{} new:{}",rb.rsp.getReturnFields(),returnFields);
//
//            rb.rsp.setReturnFields( returnFields );
//            int flags = 0;
//            if (returnFields.wantsScore()) {
//                flags |= SolrIndexSearcher.GET_SCORES;
//            }
//
//            rb.setFieldFlags( flags );
//        }
//
//        /* potrebbero essere cambiate a causa del realtime o di rimozioni post-query dei risultati */
//        if (rb.shards_rows > -1)
//        {
//            SortSpec ss = rb.getSortSpec();
//            ss.setCount(rb.shards_rows);
//        }
    }
}
