package it.kdm.solr.components;

import it.kdm.solr.client.CoreClient;
import it.kdm.solr.common.FieldUtils;
import it.kdm.solr.client.SolrClient;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.servlet.SolrRequestParsers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Paolo_2 on 07/07/15.
 */
public class ScheduledTask extends TimerTask {

    private transient static Logger log = LoggerFactory.getLogger(ScheduledTask.class);

    private long start;
    private long end;

    long period;
    Timer timer;

    private String key;

    private CoreClient.Query query;

    public ScheduledTask( String key, String period, String handler)
    {
        FieldUtils.DateInterval dateInterval = FieldUtils.parseInterval(period);

        this.start = dateInterval.start;
        this.end = dateInterval.end;
        this.period = dateInterval.period;
        this.key = key;

        query = new SolrClient.Query();

        SolrParams params;

        int idx = handler.indexOf("?");

        if (idx>0)
        {
            String qs = handler.substring(idx+1);
            handler = handler.substring(0,idx);
            params = SolrRequestParsers.parseQueryString(qs);

            query.add(params);
        }

        if (!handler.startsWith("/"))
            handler = "/" + handler;

        query.setRequestHandler(handler);
    }

    public void schedule(long mindelay)
    {
        Timer timer = new Timer();

        long now = FieldUtils.getTime(new Date());

        long delay = start - now;

        while (delay<mindelay)
            delay += period;

        timer.scheduleAtFixedRate(this,delay,period);

        String next = FieldUtils.formatDate(new Date(now + delay)).substring(11, 19);
        String period_s = FieldUtils.formatDate(new Date(period)).substring(11,19);
        if (period_s.equals("00:00:00"))
            period_s = "24:00:00";

        log.warn("task '{}' scheduled at {} with period {}\n{}",key,next,period_s,this);

        this.timer = timer;
    }

    @Override
    public boolean cancel()
    {
        this.timer.cancel();
        log.warn("task '{}' cancelled", key);
        return super.cancel();
    }

    @Override
    public void run() {

        long tick = FieldUtils.getTime(new Date());

        if (start<end && (tick<start || end<=tick) || start>end && tick<start && end<=tick  )
        {
            log.debug("task '{}' skipped because out of time range", key);
            return;
        }

        try {

            long t0 = System.currentTimeMillis();

            String corename = query.get("corename");

            NamedList<Object> response;

            if (corename!=null)
                response = CoreClient.getInstance().coreRequest(corename,new QueryRequest( query ));
            else
                response = CoreClient.getInstance().query(query).getResponse();

            long delay = System.currentTimeMillis()-t0;

            log.info("task '{}' executed correctly in {}ms. query:{} \n{}", key, delay, query, response);


        } catch (Exception e) {
            log.error( "Error executing scheduled task '{}' \n{} ",key, query, e);
        }
    }

    @Override
    public String toString(){
        return String.format("{ key:%s start:%s end:%s query:%s }" , key,start,end,query );
    }
}
