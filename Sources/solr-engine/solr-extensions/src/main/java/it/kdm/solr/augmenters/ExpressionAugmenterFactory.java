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


import it.kdm.solr.common.ExpressionBindings;
import org.apache.solr.response.transform.*;


import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.solr.common.util.NamedList;

import javax.script.*;

/**
 *
 * @since solr 4.0
 */
public class ExpressionAugmenterFactory extends TransformerFactory
{
    protected NamedList args;
    ScriptEngine engine;

    @Override
    public void init(NamedList args) {
        this.args = args;
        this.engine = new ScriptEngineManager().getEngineByName("JavaScript");
    }


    @Override
    public DocTransformer create(String field, SolrParams params, SolrQueryRequest req) {
        return new ExpressionAugmenter( field, params, req, args , engine );
    }
}



class ExpressionAugmenter extends DocTransformer
{
    static Logger log = LoggerFactory.getLogger(ExpressionAugmenter.class);

    final String display;
    SolrQueryRequest req;
    NamedList args;
    SolrParams params;
    //ScriptEngine engine;

    public ExpressionAugmenter( String display , SolrParams params, SolrQueryRequest req, NamedList args , ScriptEngine engine )
    {
        this.req = req;
        this.display = display;
        this.args = args;
        this.params = params;
        //this.engine = engine;
    }

    @Override
    public String getName()
    {
        return display;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void transform(final SolrDocument doc, int docid, float boost) {

        try
        {
            String expr = (String) params.get("expr");

            if (expr==null)
                expr = (String) args.get("expr");

            if (expr==null)
                return;

            /*ScriptContext newContext = new SimpleScriptContext();

            Bindings bindings = new ExpressionBindings(doc,req);

            newContext.setBindings(bindings, ScriptContext.ENGINE_SCOPE);

            Object res = engine.eval(expr,newContext);*/

            Object res = ExpressionBindings.eval(doc,req,expr);

            doc.setField( display, res );
        }
        catch(Exception e)
        {
            log.error("transform error",e);
        }

    }
}


