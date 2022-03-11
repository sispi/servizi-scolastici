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

import com.google.common.base.Strings;
import it.kdm.solr.common.DocUtils;
import it.kdm.solr.common.FieldUtils;
import it.kdm.solr.core.ContentCacheManager;
import it.kdm.solr.core.Schema.Fields;
import it.kdm.solr.realtime.RealTimeRuleBasedQParserPlugin;
import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.transform.DocTransformer;
import org.apache.solr.response.transform.TransformerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import it.kdm.solr.realtime.RealTimeCacheManager;

/**
 *
 * @since solr 4.0
 */
public class PATHAugmenterFactory extends TransformerFactory
{
    protected NamedList args;

    @Override
    public void init(NamedList args) {
        this.args = args;
    }


    @Override
    public DocTransformer create(String field, SolrParams params, SolrQueryRequest req) {
        return new PATHAugmenter( field, params, req, args );
    }
}

class PATHAugmenter extends DocTransformer
{
    static Logger log = LoggerFactory.getLogger(PATHAugmenter.class);

    final String display;
    SolrQueryRequest req;
    NamedList args;
    Map<String,String> rules;
    //FieldSubstitutor substitutor;

    public PATHAugmenter( String display , SolrParams params, SolrQueryRequest req, NamedList args )
    {
        this.req = req;
        this.display = display;
        this.args = args;
        List<String> argRules = (List<String>) this.args.getAll("rule");

        rules = new HashMap<>();

        for( String rule : argRules ) {

            /*int idx1 = rule.indexOf("/");
            int idx = rule.indexOf("/",idx1+1);
            int idx2 = -1;

            while (idx1!=-1 && idx!=-1 && idx2==-1 )
            {
                if (rule.charAt(idx-1) != '\\')
                    idx2 = idx;
                else
                    idx = rule.indexOf("/",idx+1);
            }

            if (idx1!=-1 && idx2!=-1)*/
            if (rule.contains(">"))
            {
                String regex = rule.split(">")[0];
                String replace = rule.split(">")[1];

                regex = RealTimeRuleBasedQParserPlugin.rewriteRegex2(regex);

                rules.put(regex,replace);

                log.trace("rule regex:{} replace:{}",regex,replace);
            }
            else
            {
                log.warn("invalid rule syntax rule:{}", rule);
            }
        }

        //this.substitutor = new FieldSubstitutor();
    }

    @Override
    public String getName()
    {
        return display;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void transform(SolrDocument doc, int docid, float boost) {

        try
        {
            //String path = null;

            Boolean virtual = this.args.getBooleanArg("virtual");
            Boolean isName = this.args.getBooleanArg("name");

            if (virtual != null && virtual)
            {
                String virtualpath = req.getParams().get(FieldUtils.VIRTUALPATH);

                if ( !Strings.isNullOrEmpty(virtualpath) )
                {
                    String name = (String) DocUtils.convertIndexableField( req, doc, Fields.NAME );

                    virtualpath = FieldUtils.buildVirtualPATH(req, name) ;

                    log.info("virtual path from request:{}",virtualpath);

                    doc.setField( display, virtualpath );
                }
                else
                {
                    //List<String> rules = this.args.getAll("rule");

                    String id = (String) DocUtils.convertIndexableField( req, doc, Fields.ID );

                    String phisical = ContentCacheManager.getInstance().buildPATH(req, id, null);

                    doc.setField( display, phisical );

                    for( String regex : rules.keySet() )
                    {
                        String replace = rules.get(regex);

                        log.trace("rule regex:{} replace:{}",regex,replace);

                        final Matcher matcher = Pattern.compile(regex).matcher( phisical );

                        if (matcher.matches())
                        {
                            StrSubstitutor strSubstitutor = new StrSubstitutor();

                            StrLookup lookup = new StrLookup() {
                                @Override
                                public String lookup(String s) {

                                    //in ordine si cerca in regex match, map e system properties

                                    try
                                    {
                                        if (s.matches("\\d"))
                                            return matcher.group(Integer.parseInt(s));
                                        else
                                            return matcher.group(s);
                                    }
                                    catch( IllegalArgumentException iae )
                                    {
                                        String v = (String) DocUtils.convertIndexableField(req, doc, s);
                                        if (v==null)
                                            throw new RuntimeException("field "+s+" not found");
                                        return v;
                                    }
                                }
                            };

                            strSubstitutor.setVariableResolver(lookup);

                            replace = replace.replaceAll("\\$(\\d)","\\${$1}");

                            String sval = strSubstitutor.replace(replace);

                            doc.setField( display, sval );

                            log.info("virtual path by matched regex rule:{} phisical:{}",regex,phisical);

                            return;
                        }
                    }
                    log.info("no rule matched for virtual path. phisical path:{}",phisical);
                }


            }
            else
            {
                String id = (String) DocUtils.convertIndexableField(req, doc, Fields.ID);

                log.trace("PATH for id:{}",id);

                String phisical = ContentCacheManager.getInstance().buildPATH(req, id, null);

                log.info("virtual path from phisical path:{}",phisical);

                doc.setField( display, phisical );
            }

            if (isName!=null && isName){
                String path = (String) doc.getFieldValue(display);
                if (path!=null){
                    int lio = path.lastIndexOf("/");
                    doc.setField(display,path.substring(lio+1));
                }
            }
        }
        catch(Exception e)
        {
            //e.printStackTrace();
            log.warn("transform error for id:{}",doc.getFieldValue(Fields.ID),e);
        }

    }
}


