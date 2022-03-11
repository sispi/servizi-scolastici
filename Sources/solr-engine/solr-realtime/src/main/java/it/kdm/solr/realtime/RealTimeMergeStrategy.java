package it.kdm.solr.realtime;

import org.apache.solr.client.solrj.SolrResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.FacetParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.apache.solr.handler.component.MergeStrategy;
import org.apache.solr.handler.component.ResponseBuilder;
import org.apache.solr.handler.component.ShardRequest;
import org.apache.solr.handler.component.ShardResponse;
import org.apache.solr.search.SolrIndexSearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;


class RealTimeMergeStrategy implements MergeStrategy
{
    private transient static Logger log = LoggerFactory.getLogger(RealTimeMergeStrategy.class);

	static final String REALTIME_RESPONSE = "realtimeResponse";
	static final String REALTIME_FILTERS = "realtimeFilters";


	public boolean mergesIds() { return false; }

	public boolean handlesMergeFields() { return false; }

	public int getCost() { return 100; }

	public void  handleMergeFields(ResponseBuilder rb, SolrIndexSearcher searcher) {}

	@SuppressWarnings("unchecked")
	public void merge(ResponseBuilder rb, ShardRequest shardRequest) {

		long RTTime = 0;
		int RTFound = 0;
		int RTDocs = 0;
		NamedList filters = new NamedList();
		
		for( int i=0; i< shardRequest.responses.size() ; i++)
		{
			ShardResponse srsp = shardRequest.responses.get(i);

            SolrResponse solrResp = srsp.getSolrResponse();

            if (solrResp==null)
                continue;

            NamedList nl = solrResp.getResponse();

            if (nl == null)
                continue;

			String shard = srsp.getShard();

			SimpleOrderedMap<String> rt_filters = (SimpleOrderedMap<String>) nl.get(REALTIME_FILTERS);

			if (rt_filters!=null && rt_filters.size()>0){
				filters.add(shard.split("/")[4],rt_filters);
			}

            nl = (NamedList) nl.get(REALTIME_RESPONSE);

			if (nl != null)
			{
                if (rb.req.getParams().getBool(FacetParams.FACET, false)) {

                    NamedList<Object> facet_counts = new SimpleOrderedMap<>();
                    nl.add("facet_counts", facet_counts);
                }


                //nl.add("facet_counts", new NamedList() );
				
				shard = shard.replace("http://" , "http://realtime@" );
			
				ShardResponse shardResponse = new ShardResponse();
				shardResponse.setShardRequest(shardRequest);


                try {
                    Field shardField = ShardResponse.class.getDeclaredField("shard");
                    shardField.setAccessible(true);
                    shardField.set(shardResponse,shard);

                    Field addressField = ShardResponse.class.getDeclaredField("shardAddress");
                    addressField.setAccessible(true);
                    addressField.set(shardResponse,srsp.getShardAddress());

                } catch (NoSuchFieldException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                //RealTimeAccessUtils.ShardResponse.setShard(shardResponse,shard);
				//RealTimeAccessUtils.ShardResponse.setShardAddress(shardResponse, srsp.getShardAddress() );
				
				RealTimeSolrResponse ssr = new RealTimeSolrResponse();
				ssr.setResponse(nl);

                log.trace("realtime response shard:{} address:{} :\n{}",shard,srsp.getShardAddress(),ssr);
				
				RTTime += ssr.getElapsedTime();
				RTFound += ssr.getNumFound();
				RTDocs += ssr.getNumDocs();
				
				shardResponse.setSolrResponse(ssr);
				
				//Questo per dare priorità agli elementi realtime che nella priority queue vengono presi in ordine (solo il primo a parità di chiave)
				shardRequest.responses.add(0, shardResponse );
				i++;
			}
		}
		
		SimpleOrderedMap<Object> rtHeader = new SimpleOrderedMap<>();
		
		rtHeader.add("QTime", RTTime);
		rtHeader.add("numFound", RTFound);
		rtHeader.add("numDocs", RTDocs);
		if (filters.size()>0)
			rtHeader.add("filters", filters);

		rb.rsp.add("realtimeHeader" , rtHeader );
		
		
	}
	
	public static class RealTimeSolrResponse extends SolrResponse {
	
		long elapsedTime = 0;
		int numFound = 0;
		int memDocs = 0;

		NamedList<Object> nl = null;
		
		@Override
		public long getElapsedTime() {
			return elapsedTime;
		}
		
		public int getNumFound() {
			return numFound;
		}
		
		public int getNumDocs()
		{
			return memDocs;
		}

		@Override
		public NamedList<Object> getResponse() {
			return nl;
		}

		@Override
		public void setResponse(NamedList<Object> rsp) {
		
			Object et = rsp.get("elapsedTime");
			Object md = rsp.get("RTDocs");
			Object filters = rsp.get("filters");

			if (et instanceof Number)
				elapsedTime = ((Number)et).longValue();
			else if (et!=null)
				elapsedTime = Long.parseLong( et.toString() );
				
			if (md instanceof Number)
				memDocs = ((Number)md).intValue();
			else if (et!=null)
				memDocs = Integer.parseInt( md.toString() );
				
			SolrDocumentList list = (SolrDocumentList) rsp.get("response");

			if (list!=null)
				numFound = ( (Long) list.getNumFound()).intValue();
				
			nl = rsp;
		}

        @Override
        public void setElapsedTime(long elapsedTime) {
            this.elapsedTime = elapsedTime;
        }
    }
}