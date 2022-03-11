package it.kdm.solr.realtime;
import com.google.common.base.Strings;

import it.kdm.solr.client.CoreClient;
import it.kdm.solr.client.SolrClient;
import it.kdm.solr.core.ExpirableRegenerator;
import it.kdm.solr.core.Schema;
import it.kdm.solr.core.Session;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.apache.solr.common.util.StrUtils;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.search.SolrCache;
import java.util.*;

import org.apache.solr.handler.component.*;
import org.apache.solr.common.params.ShardParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.solr.common.cloud.DocCollection;
import org.apache.solr.common.cloud.CompositeIdRouter;
import org.apache.solr.common.cloud.Slice;
import org.apache.solr.common.cloud.DocRouter.Range;

import javax.servlet.http.HttpServletRequest;

public class RealTimeSearchHandler extends SearchHandler 
{
    //static final int slowQueryThresholdMillis = 1000;
	
	private static Logger log = LoggerFactory.getLogger(RealTimeSearchHandler.class);

    static final String DATACENTER = "datacenter";

	@Override
	protected List<String> getDefaultComponents()
	{
		ArrayList<String> names = new ArrayList<>(6);

        String qc = System.getProperty("realtimequerycomponent");

        if (qc!=null)
            names.add( "realtimequery" );
        else
            names.add( QueryComponent.COMPONENT_NAME);

		names.add( FacetComponent.COMPONENT_NAME );
		//names.add( MoreLikeThisComponent.COMPONENT_NAME );
		//names.add( HighlightComponent.COMPONENT_NAME );
		//names.add( StatsComponent.COMPONENT_NAME );
		names.add( DebugComponent.COMPONENT_NAME );
		//names.add( ExpandComponent.COMPONENT_NAME);
		return names;
	}

    protected void remoteSearch(String datacenter, SolrQueryRequest req, SolrQueryResponse rsp) throws Exception
    {
        ModifiableSolrParams newparams = new ModifiableSolrParams(req.getOriginalParams());
        try
        {
            //String datacenter = req.getParams().get(DATACENTER);

            //newparams.remove(DATACENTER);

            QueryRequest qr = new QueryRequest( newparams );

            SolrClient dc = SolrClient.getInstance(datacenter);

            NamedList<Object> response = dc.request(qr);

            NamedList<Object> remoteHeader = (NamedList<Object>) response.remove("responseHeader");
            NamedList<Object> localHeader = (NamedList<Object>) rsp.getValues().remove("responseHeader");

            localHeader.add("remote-host", SolrClient.getZkHost(datacenter));

            rsp.getValues().clear();

            rsp.getValues().add("responseHeader", localHeader);
            rsp.getValues().add("remoteHeader", remoteHeader);
            rsp.getValues().addAll( response );

            /*ModifiableSolrParams changedParams = new ModifiableSolrParams(req.getParams());
            changedParams.set("remote-host", SolrClient.getZkHost(datacenter) );
            req.setParams(changedParams);*/

        }
        catch( Exception e )
        {
            log.error("remoteSearch to:{} request:{}",datacenter,newparams);
            throw e;
        }


    }
	
	@Override
	@SuppressWarnings("unchecked")
	public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp) throws Exception
	{
        if (log.isTraceEnabled())
            log.trace("handleRequestBody params:{}", req.getParams());

		//final double maxage = req.getParams().getDouble("max-age",0);

        String shardParam = req.getParams().get(ShardParams.SHARDS);

        if ( !Strings.isNullOrEmpty(shardParam) )
        {
            String newParam = shardParam;

            if ("ALL".equals(shardParam))
                newParam = System.getProperty(Schema.DC_ALIVE);

            String[] shards = newParam.split(",");

            List<String> allshards = new ArrayList<>();

            for( String shard : shards )
            {
                String subshards = System.getProperty( "DC_"+shard+"_SHARDS", shard );
                allshards.addAll(StrUtils.splitSmart(subshards, ',') );
            }

            newParam = StrUtils.join( allshards, ',' );

            if (!newParam.equals(shardParam))
            {
                ModifiableSolrParams newparams = new ModifiableSolrParams(req.getParams());
                newparams.set(ShardParams.SHARDS, newParam  );
                req.setParams(newparams);

                log.info("search modified on shards={}",newparams );
            }
        }
		
		SolrCache cache = null; 
		
		String cachekey = null;
		
		boolean distrib = req.getParams().getBool( "distrib" , true );

        //boolean trace = req.getParams().getBool( "trace" , false );

        String route = req.getParams().get( ShardParams._ROUTE_ );
        String proute = req.getParams().get(Schema.Params.PROUTE);
		
		boolean foundInCache = false;
		
		if (distrib)
		{
            HttpServletRequest httpreq = (HttpServletRequest) req.getContext().get( "httpRequest" );

            if (httpreq!=null)
            {
                ModifiableSolrParams newparams= new ModifiableSolrParams(req.getParams());

                String remoteAddr = httpreq.getRemoteAddr();
                String remoteHeader = httpreq.getHeader(Schema.AUTH_HEADER);

                newparams.set("remoteAddr",remoteAddr);

                if (remoteHeader!=null)
                    newparams.set("remoteHeader",remoteHeader);

                req.setParams(newparams);
            }

            String datacenter = req.getParams().get(DATACENTER);

            if (datacenter!=null && !datacenter.equals(System.getProperty(Schema.Fields.LOCATION)))
            {
                remoteSearch(datacenter,req,rsp);

                foundInCache = true;
            }
            else
            {
                cache = req.getSearcher().getCache(Schema.RESULTS_CACHE);
                ModifiableSolrParams newparams = new ModifiableSolrParams(req.getParams());
                newparams.remove("NOW");
                cachekey = newparams.toString();

                ExpirableRegenerator.Expirable results = (ExpirableRegenerator.Expirable) cache.get(cachekey);

                if (results!=null && !results.isExpired() )
                {
                    foundInCache = true;
                    rsp.getValues().addAll( (NamedList<Object>) results.getObject() );
                }

                if (foundInCache)
                    log.debug("key {} found in cache", cachekey);
                else
                    log.debug("key {} not found in cache", cachekey);
            }
			

		}
		
		if (!foundInCache)
		{
			//TODO si potrebbe fare gestione implicita di shards_rows in caso di skipDeleted o skipEmpty

            if (proute != null /* && route.indexOf("@") != -1 && route.indexOf("/8!") == -1 */ )
            {
                if (route!=null)
                    throw new SolrException( SolrException.ErrorCode.BAD_REQUEST , "route and proute params can't be set together:"+req ) ;

                Schema schema = Schema.get(req);

                route = schema.getDivision(proute);

                if (route.endsWith("!")) //la division rappresenta un routing valido
                {
                    ModifiableSolrParams newparams= new ModifiableSolrParams(req.getParams());

                    /* per non sporcare il secondo byte del range di routing */
                    if ( route.indexOf("!")==(route.length()-1) )
                        route = route.replace("!","/8!") ;

                    newparams.set( ShardParams._ROUTE_ , route );
                    newparams.remove(Schema.Params.PROUTE );

                    req.setParams(newparams);
                }

                log.debug("route by parent. proute:{} route:{}",proute,route );


                /*int seps = StringUtils.countMatches(proute, "!");
                int dots = StringUtils.countMatches(proute, ".");

                if ( dots>0 && seps>0 )
                {
                    ModifiableSolrParams newparams= new ModifiableSolrParams(req.getParams());

                    if (seps>1)
                    {
                        route = proute;
                    }
                    else
                    {
                        route = proute.replace("!","/8!") ;
                    }

                    newparams.set( ShardParams._ROUTE_ , route );

                    req.setParams(newparams);

                }*/




                /*if ( seps==0 || seps==1 || dots==0 )
                {
                    ModifiableSolrParams newparams= new ModifiableSolrParams(req.getParams());

                    if (seps==0 || dots==0 )
                    {
                        newparams.remove(ShardParams._ROUTE_);
                        log.debug("removed _route_ param:{}",route );
                    }
                    else
                    {
                        String newRoute = route.replace("!","/8!");
                        newparams.set( ShardParams._ROUTE_ , newRoute );
                        log.debug("replaced _route_ param old:{} new:{}",route,newRoute );
                    }

                    req.setParams(newparams);

                    route = req.getParams().get( ShardParams._ROUTE_ );
                }*/
            }

            //TODO si potrebbe ottimizzare la gestione dei PATH in questo modo :
            /*
             * 1) Se il path è di tipo /..../name va risolto in un realtimeget qui
             * 2) Se il path è di tipo /..../* va risolto in un parent:... con proute:...
             * 3) Se il path è di tipo /..../name* va risolto in un name:... AND parent:... con proute:...
             * */


            try
            {
                super.handleRequestBody(req,rsp);
            }
            catch(Exception e)
            {
                log.error("originals:{} params:{}",req.getOriginalParams(), req.getParams(),e);
                throw e;
            }

            String post_datacenter = req.getParams().get(DATACENTER);

            if (distrib && post_datacenter!=null && !post_datacenter.equals(System.getProperty(Schema.Fields.LOCATION)))
            {
                remoteSearch(post_datacenter,req,rsp);
            }

            if (req.getParams().getBool( "session.info" , false ) )
                rsp.add( "session" , Session.get(req) );

            if (req.getParams().getBool( "routing.info" , false ) )
            {
                SimpleOrderedMap<Object> som = new SimpleOrderedMap<>();

                som.add("proute",proute);
                som.add("route",route);

                if (route!=null)
                {
                    Schema schema = Schema.get(req);

                    DocCollection coll = CoreClient.getInstance().getZkStateReader().getClusterState().getCollection( schema.getCollection() );

                    CompositeIdRouter cir = (CompositeIdRouter) coll.getRouter();

                    int hash = cir.sliceHash( route , null, null , null )  ;
                    Range range = cir.keyHashRange( route );

                    Slice target = cir.getTargetSlice( route , null, null, null , coll );
                    Collection<Slice> slices = cir.getSearchSlicesSingle( route , req.getParams() , coll );

                    List<String> shards = new ArrayList<>();

                    for ( Slice s : slices )
                        shards.add( s.getName() );

                    som.add("hash", Integer.toHexString(hash) );
                    som.add("range", range.toString() );
                    som.add("target",target.getName());
                    som.add("shards",shards);

                    log.trace( "routed query _route_:{} hash:{} range:{} \ntarget:{} \nshards:{} \nparams:{}" , route, Integer.toHexString(hash) , range , target.getName() , shards , req.getParams() );

                }

                rsp.add( "routing" , som );
            }


            final double maxage = req.getParams().getDouble("max-age",0);

            if (cachekey!=null && maxage>0)
			{
				final Object results = rsp.getValues();
				
				ExpirableRegenerator.Expirable cachable = new ExpirableRegenerator.CacheEntry( results , (int) maxage );
				
				cache.put(cachekey,cachable);

				log.debug("results added in cache for key {}" , cachekey );
			}
		}	


		
		/*final int qtime = (int)( System.currentTimeMillis() - req.getStartTime());
		
		if ( trace && distrib || slowQueryThresholdMillis >= 0 && qtime >= slowQueryThresholdMillis) {
			
			String message = rsp.getToLogAsString("debug query: "+qtime+"ms"+"\n");
			
			if (slowQueryThresholdMillis >= 0 && qtime >= slowQueryThresholdMillis && log.isWarnEnabled())
				log.warn( message + "\n{}" , rsp.getValues() );
			else if (log.isDebugEnabled())
				log.debug( message + "\n{}" , rsp.getValues() );
		}*/
		
	}
}