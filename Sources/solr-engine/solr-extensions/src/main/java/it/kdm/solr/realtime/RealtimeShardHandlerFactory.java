package it.kdm.solr.realtime;

import it.kdm.solr.client.CoreClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.LBHttpSolrClient;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.common.util.NamedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import org.apache.solr.handler.component.*;
import org.apache.solr.common.SolrException;
import org.apache.solr.core.CoreContainer;
import java.util.Collection;
import java.util.regex.*;

public class RealtimeShardHandlerFactory extends HttpShardHandlerFactory  {
  
	private transient static Logger log = LoggerFactory.getLogger(RealtimeShardHandlerFactory.class);
	
	private final static Pattern corePattern = Pattern.compile( ".*/([^/]+)/$" );
	public LBHttpSolrClient.Rsp makeLoadBalancedRequest(final QueryRequest req, List<String> urls) throws SolrServerException, IOException {
    
		boolean shortcut = req.getParams().getBool("shortcut",false);

        /*String datacenter = req.getParams().get("datacenter");

        if (datacenter != null)
        {
            log.info("Requested remote datacenter '{}'",datacenter);

            for (int i=0; i<urls.size(); i++) {

                String url = urls.get(i);

                Matcher m = corePattern.matcher(url);

                String dc_url = null;

                String corename = null;

                if (m.find())
                {
                    corename = m.group(1);

                    String host = System.getProperty( "DC." + datacenter + "." + corename , System.getProperty("DC." + datacenter) );

                    if (host != null)
                        dc_url = url.replaceFirst("//[^/]+:","//"+host+":");
                }

                if (dc_url != null)
                {
                    urls.set(i,dc_url);
                    log.debug("url rewritten to '{}'",dc_url);
                }
                else
                {
                    String msg = String.format( "No url found for core '%s' on datacenter '%s'", corename, datacenter  );
                    throw new SolrException( SolrException.ErrorCode.SERVER_ERROR , msg );
                }
            }
        }
		else*/

		if (shortcut)
		{
            log.info("Requested shortcut for this request.\n{}",req);

            String coreName = null;

			CoreContainer coreContainer = CoreClient.getInstance().getCoreContainer();
			
			if (coreContainer!=null)
			{
				Collection<String> coreNames = coreContainer.getAllCoreNames();

                for (String url : urls) {

                    Matcher m = corePattern.matcher(url);

                    if (m.find()) {
                        String name = m.group(1);

                        if (coreNames.contains(name)) {
                            coreName = name;
                            break;
                        }
                    }
                }
			}

            if ( coreName != null )
            {
                log.trace( "makeLoadBalancedRequest direct request core:{} \nreq:{} \nurls:{}" , coreName, req.getParams(), urls );

                final String core = coreName;
                LBHttpSolrClient.Rsp rsp = new LBHttpSolrClient.Rsp()
                {
                    @Override
                    public NamedList<Object> getResponse() {

                        try
                        {
                            NamedList<Object> result = CoreClient.getInstance().coreRequest( core , req );

                            return result;
                        }
                        catch( SolrServerException e )
                        {
                            throw new SolrException( SolrException.ErrorCode.SERVER_ERROR , e );
                        }
                    }

                    @Override
                    public String getServer() {
                        return core;
                    }
                };
                return rsp;
            }
		}

		log.trace( "makeLoadBalancedRequest http request \nreq:{} \nurls:{}" , req.getParams() , urls );
		return super.makeLoadBalancedRequest(req,urls);

	}
		
	/*public List<String> makeURLList(String shard) {
		
		List<String> urls = super.makeURLList(shard);

		if (urls.size()==1)
			urls.add(0,null);
		
		return urls;
	}*/
  
}