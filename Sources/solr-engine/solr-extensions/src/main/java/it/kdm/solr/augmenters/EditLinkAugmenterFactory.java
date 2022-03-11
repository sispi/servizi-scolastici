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

//import it.kdm.solr.core.ProviderProxy;
import it.kdm.solr.common.DocUtils;
import it.kdm.solr.core.Schema;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.transform.DocTransformer;
import org.apache.solr.response.transform.TransformerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @since solr 4.0
 */
public class EditLinkAugmenterFactory extends TransformerFactory
{
    protected NamedList args;

    @Override
    public void init(NamedList args) {
        this.args = args;
    }


    @Override
    public DocTransformer create(String field, SolrParams params, SolrQueryRequest req) {
        return new EditLinkAugmenter( field, params, req, args );
    }
}

class EditLinkAugmenter extends DocTransformer
{
    static Logger log = LoggerFactory.getLogger(EditLinkAugmenter.class);

    final String display;
    SolrQueryRequest req;

    public EditLinkAugmenter( String display , SolrParams params, SolrQueryRequest req, NamedList args )
    {
        this.display = display;
        this.req = req;

    }

    @Override
    public String getName()
    {
        return display;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void transform(SolrDocument doc, int docid, float score) {

        Schema schema = Schema.get(req);

        String editLink = DocUtils.getSharedLink(req,doc);

        doc.setField( display, editLink );

    }
}


