/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.kdm.solr.realtime;

import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.ShardParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.handler.component.QueryComponent;
import org.apache.solr.handler.component.ResponseBuilder;
import org.apache.solr.search.ReturnFields;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.search.SolrReturnFields;
import org.apache.solr.search.SortSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class RealTimeQueryComponent extends QueryComponent
{
	public static final String RT_COMPONENT_NAME = "realtimequery";
    private static final String RT_GET= "/adminget";

    private transient static Logger log = LoggerFactory.getLogger(RealTimeQueryComponent.class);
	
	@Override
	public void prepare(ResponseBuilder rb) throws IOException
	{
        rb.addMergeStrategy( new RealTimeMergeStrategy() );

        super.prepare(rb);

        /* reimposta i return fields nel caso in cui "fl" della richiesta sia stata modificato da un parser */

        boolean changedFl = rb.req.getParams().getBool("changedFl", false);

        String[] morefls = rb.req.getParams().getParams("implicitAugmenters");
        boolean moreFields = (rb.rsp.getReturnFields().wantsAllFields() && morefls!=null && morefls.length>0);

        if (moreFields)
        {
            Collection<String> fls = new ArrayList<>();
            fls.addAll(Arrays.asList(morefls));
            fls.addAll(Arrays.asList(rb.req.getParams().getParams(CommonParams.FL)));
            String[] fla = fls.toArray(new String[fls.size()]);

            ModifiableSolrParams params = new ModifiableSolrParams(rb.req.getParams());
            params.set(CommonParams.FL,fla);

            rb.req.setParams(params);

            moreFields = true;
        }

        if (changedFl || moreFields)
        {
            ReturnFields returnFields;

            if (moreFields){

                Collection<String> fls = new ArrayList<>();
                fls.addAll(Arrays.asList(morefls));
                fls.addAll(Arrays.asList(rb.req.getParams().getParams(CommonParams.FL)));
                String[] fla = fls.toArray(new String[fls.size()]);

                returnFields = new SolrReturnFields( fla, rb.req );
            } else {
                returnFields = new SolrReturnFields( rb.req );
            }

            log.trace("changed fields old:{} new:{}",rb.rsp.getReturnFields(),returnFields);

            rb.rsp.setReturnFields( returnFields );
            int flags = 0;
            if (returnFields.wantsScore()) {
                flags |= SolrIndexSearcher.GET_SCORES;
            }

            rb.setFieldFlags( flags );
        }

        /* per aggiungere implicitamente augmenters quando si chiedono tutti i campi */


        /*if (rb.rsp.getReturnFields().wantsAllFields() && morefls!=null && morefls.length>0)
        {
            Collection<String> fls = new ArrayList<>();

            fls.addAll(Arrays.asList(rb.req.getParams().getParams(CommonParams.FL)));
            fls.addAll(Arrays.asList(morefls));
            String[] flArray = fls.toArray(new String[fls.size()]);

            SolrReturnFields returnFields = new SolrReturnFields(flArray, rb.req );

            rb.rsp.setReturnFields( returnFields );

            log.trace("changed fields by augmenters new:{}",returnFields);
        }*/

        /* potrebbero essere cambiate a causa del realtime o di rimozioni post-query dei risultati */
        if (rb.shards_rows > -1)
        {
            SortSpec ss = rb.getSortSpec();
            ss.setCount(rb.shards_rows);
        }
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void process(ResponseBuilder rb) throws IOException
	{
		RealTimeIndex.getInstance().modifyRequest( rb );

        super.process(rb);
	}
	
  
	@Override
	public int distributedProcess(ResponseBuilder rb) throws IOException {

        /* la onePassDistributedQuery si attiva implicitamente se si chiede solo id e/o score */

        /*  se no è oPDQ possiamo cambiare SHARDS_QT in modo da usare RealtimeGet (admin per ottimizzare...) ottenendo oggetti non committati */

        /*  se è oPDQ otterremmo campi vecchi (ma si chiede solo id e score...) */
        /*  il problema è che ci perdiamo i create nel RTI */

        //TODO non richiesto in DOCER ma si potrebbe forzare il doppio passaggio (almeno quando ci sono elementi nel RTI)

		if (rb.stage == ResponseBuilder.STAGE_GET_FIELDS && !rb.onePassDistributedQuery) {
			
			SolrParams params = rb.req.getParams();
				
            ModifiableSolrParams newparams= new ModifiableSolrParams(params);
            newparams.set(ShardParams.SHARDS_QT, RT_GET );

            rb.req.setParams(newparams);
		}
	
		return super.distributedProcess(rb);
	}  

}