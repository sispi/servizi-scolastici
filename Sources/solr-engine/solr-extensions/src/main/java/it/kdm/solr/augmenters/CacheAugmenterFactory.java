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

package it.kdm.solr.augmenters;

import it.kdm.solr.client.CoreClient;
import it.kdm.solr.common.DocUtils;
import it.kdm.solr.common.ExpressionBindings;
import it.kdm.solr.core.ContentCacheManager;
import it.kdm.solr.core.Schema;
import it.kdm.solr.core.Session;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.StrUtils;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.transform.DocTransformer;
import org.apache.solr.response.transform.TransformerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.Collection;


/**
 *
 * @since solr 4.0
 */
public class CacheAugmenterFactory extends TransformerFactory
{
    protected NamedList args;
    ScriptEngine engine;

    @Override
    public void init(NamedList args) {
        this.args = args;
        this.engine = new ScriptEngineManager().getEngineByName("JavaScript");
    }
   
/*	public static boolean isAttached( SolrQueryRequest req, String id ) throws Exception
	{
		ContentCacheManager.CacheEntry entry = ContentCacheManager.getInstance().getCacheEntry(req,id);

		if (!entry.enabled)
			return false;
		
		if (entry.parent!=null)
			return isAttached( req , entry.parent);
		else
			return true;
	}*/


    @Override
    public DocTransformer create(String field, SolrParams params, SolrQueryRequest req) {
        return new CacheAugmenter( field, params, req, args , this.engine );
    }
}

class CacheAugmenter extends DocTransformer
{
    static Logger log = LoggerFactory.getLogger(CacheAugmenter.class);

    final String display;

    SolrQueryRequest req;

    final String field;
    final String idexpr;
    ScriptEngine engine;
    Boolean cache;

    public CacheAugmenter(String display, SolrParams params, SolrQueryRequest req, NamedList args, ScriptEngine engine)
    {
        this.display = display;
        this.req = req;
        this.engine = engine;

        String fieldName = params.get("field", (String) args.get("field") );

        String sCache = params.get("usecache", (String) args.get("usecache"));

        if (fieldName==null)
            this.field = Schema.Fields.NAME;
        else
            this.field = fieldName;

        if (sCache==null)
        {
            cache = ContentCacheManager.isCachedField(fieldName);
        }
        else
        {
            cache = StrUtils.parseBool(sCache,true);
        }

        this.idexpr = params.get("id", (String) args.get("id") );

    }

    @Override
    public String getName()
    {
        return display;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void transform(SolrDocument doc, int docid, float score) {

        try
        {
            if (this.idexpr == null)
            {
                log.warn("misconfigured augmeneter {}",display);
                return;
            }

            DocUtils.convertIndexableField(req, doc, this.idexpr);

            Object values = doc.getFieldValue(this.idexpr);

            if (values==null)
            {
                /*ScriptContext newContext = new SimpleScriptContext();

                Bindings bindings = new ExpressionBindings(doc,req);

                newContext.setBindings(bindings, ScriptContext.ENGINE_SCOPE);

                values = engine.eval(idexpr,newContext).toString();*/

                values = ExpressionBindings.eval(doc,req,idexpr).toString();
            }

            boolean isArray = false;

            Collection<String> ids = new ArrayList<>();

            if (values instanceof Collection )
            {
                for( Object value : (Collection) values )
                    ids.add(value.toString());

                isArray = true;
            }
            else if (values!=null)
            {
                ids.add(values.toString());
            }

            ArrayList<Object> outVals = new ArrayList<>();

            for( String id : ids )
            {
                if (field.equals("attached"))
                {
                    outVals.add( ContentCacheManager.getInstance().isAttached(req,id) );
                }
                else
                {
                    SolrDocument entry;

                    if (cache)
                    {
                        entry = ContentCacheManager.getInstance().getCacheEntry(req, id);
                    }
                    else
                    {
                        SolrQuery params = new SolrQuery();
                        params.set( Schema.Params.TICKET , Session.ROOTTICKET );
                        params.set( Schema.Params.SHORTCUT , true );
                        params.set( Schema.Fields.ID , id );
                        params.setFields( "*" );

                        entry = CoreClient.getInstance().get(params);
                    }

                    if (entry!=null)
                    {
                        Object value = entry.getFieldValue(this.field);

                        if (value==null)
                        {
                            /*ScriptContext newContext = new SimpleScriptContext();

                            Bindings bindings = new ExpressionBindings(entry,req);

                            newContext.setBindings(bindings, ScriptContext.ENGINE_SCOPE);

                            value = engine.eval(this.field, newContext);*/

                            value = ExpressionBindings.eval( entry,req,this.field);

                        }

                        outVals.add(value);
                    }
                }
            }

            if (isArray)
                doc.setField( display , outVals );
            else if (outVals.size()>0)
                doc.setField( display , outVals.get(0) );
        }
        catch(Exception e)
        {
            log.error("transform error for id:{} display:{} idexpr:{} field:{}",doc.getFieldValue(Schema.Fields.ID), display,idexpr,field,e);
        }

    }
}


