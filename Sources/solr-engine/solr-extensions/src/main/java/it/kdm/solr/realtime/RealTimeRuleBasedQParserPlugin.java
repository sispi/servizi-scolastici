package it.kdm.solr.realtime;

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

import it.kdm.solr.client.SolrClient;
import it.kdm.solr.common.DocUtils;
import it.kdm.solr.common.FieldUtils;
import it.kdm.solr.common.QueryUtils;
import it.kdm.solr.core.ContentCacheManager;
import it.kdm.solr.core.Schema;
import it.kdm.solr.core.Schema.Fields;
import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.lucene.search.*;
import org.apache.lucene.util.BytesRef;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QParserPlugin;
import org.apache.solr.search.SyntaxError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Paolo_2 on 09/08/15.
 */
public class RealTimeRuleBasedQParserPlugin extends QParserPlugin {

    private static final String REGEX = "regex";
    private static final String REGEX2 = "regex2";
    private static final String IF = "if";
    public static final String PARSEDPATH = "FINAL-ARGUMENT";
    public static final String PATH_QUERY = "FINAL-QUERY";
    //public static final String PATH = "PATH";
    private transient static Logger log = LoggerFactory.getLogger(RealTimeRuleBasedQParserPlugin.class);

    private Map<String, SolrParams > rules;

    private static final int maxpathclauses = 1;

    @Override
    public void init( NamedList args )
    {
        super.init(args);

        this.rules = new LinkedHashMap<>();

        for( int i=0; i<args.size(); i++)
        {
            String name = args.getName(i);

            int idx = name.indexOf('.');

            if ( idx > 0 )
            {
                String rule = name.substring(0,idx);
                String param = name.substring(idx + 1);
                String ifConf = null;

                idx = param.indexOf('/');

                if (idx>0){
                    ifConf = param.substring(0,idx);
                    rule = param.substring(idx + 1);
                }

                String value = args.getVal(i).toString();

                //if (value.contains("§{"))
                //    value = value.replaceAll("§\\{","\\${");

                ModifiableSolrParams params = (ModifiableSolrParams) rules.get(rule);

                if (params==null)
                {
                    params = new ModifiableSolrParams();
                    rules.put(rule, params);

                    if (ifConf != null)
                        params.set( IF , ifConf );
                }

                if (param.equals(REGEX2)){
                    value = rewriteRegex2(value);
                    param = REGEX;
                }

                params.set( param, value );
            }
        }

        Set<String> keys = new HashSet<>(rules.keySet());

        for( String rule : keys )
        {
            String regex = rules.get(rule).get(REGEX);

            if ( regex == null )
            {
                log.warn("no regex for rule '{}'", rule);
                rules.remove(rule);
            }
            else
            {
                Pattern.compile(regex);
                log.info("Compiled regex for rule '{}'", rule);
            }
        }
    }

    private Query parsePATHQuery(String PATH,SolrQueryRequest req) throws SyntaxError
    {
        log.info("pathquery PATH:{}",PATH);

        PATH = PATH.trim();

        int lio = PATH.lastIndexOf("/");

        String name = PATH.substring(lio+1);

        Query query;

        if ( name.equals("*") || name.equals("") )
        {
            if (lio==0)
            {
                //nella forma "/*" o "/"
                String nq = SolrClient.Query.makeClause(Fields.PARENT, (Object) null) ;
                query = QParser.getParser( nq, null, req ).getQuery();
            }
            else
            {
                //nella forma  "/..../*" o "/...../"
                String parentPath = PATH.substring(0, lio ) ;

                List<String> parents = ContentCacheManager.getInstance().searchByPATH( req, parentPath ) ;

                if (parents.size()==0)
                {
                    log.warn("PATH Query parents are empty!!! parentPath:{}",parentPath);
                    return new MatchNoDocsQuery();
                }


                BytesRef[] refs = DocUtils.bytesRefs( req, Fields.PARENT, parents );

                query = new ConstantScoreQuery( QueryUtils.Filters.termsFilter.makeFilter( Fields.PARENT , refs ) );
            }
        }
        else if ( name.equals("**") && PATH.indexOf("*") >= lio )
        {
            if (lio==0)
            {
                //nella forma "/**"
                query = new MatchAllDocsQuery();
            }
            else
            {
                //nella forma  "/..../**"
                String parentPath = PATH.substring(0, lio ) ;

                Schema schema = Schema.get(req);

                List<String> parents = RealTimeCacheManager.getInstance().searchByPATH( req, parentPath ) ;

                if (parents.size() == 0)
                    return new MatchNoDocsQuery();

                assert(parents.size()==1);

                String parent = parents.iterator().next();

                List<String> ids = RealTimeCacheManager.getInstance().deepSearchByParent(req,parent) ;

                if (ids.size()==0)
                    return new MatchNoDocsQuery();

                if ( ids.size() > maxpathclauses )
                {
                    parents = new ArrayList<>();
                    parents.add(parent);

                    for( String id : ids )
                    {
                        String type = id.split("@")[1];

                        if (schema.canHaveChildren(type))
                            parents.add(id);
                    }

                    BytesRef[] refs = DocUtils.bytesRefs( req, Fields.PARENT, parents );

                    query = new ConstantScoreQuery( QueryUtils.Filters.termsFilter.makeFilter( Fields.PARENT , refs ) );
                }
                else
                {
                    BytesRef[] refs = DocUtils.bytesRefs(req, Fields.ID, ids);

                    query = new ConstantScoreQuery( QueryUtils.Filters.termsFilter.makeFilter( Fields.ID , refs ) );
                }


            }
        }
        else
        {
            // nella forma "...../name*" o "...../name"
            List<String> ids = ContentCacheManager.getInstance().searchByPATH( req, PATH ) ;

            if (ids.size()==0)
            {
                log.warn("PATH Query PATH is empty!!! path:{}",PATH);
                return new MatchNoDocsQuery();
            }


            BytesRef[] refs = DocUtils.bytesRefs( req, Fields.ID, ids );

            query = new ConstantScoreQuery( QueryUtils.Filters.termsFilter.makeFilter( Fields.ID , refs ) );
        }

        log.debug("PATH:{} query:{}",PATH,query);

        return query;

    }

    public static String rewriteRegex2(String regex){
        if (!regex.startsWith("^"))
            regex = "^" + regex;

        if (!regex.endsWith("$"))
            regex += "$";

        regex = regex.replaceAll("\\*\\*",".*");

        regex = regex.replace("/*$" , "/\\*?$");
        regex = regex.replace("/*)$" , "/\\*?)$");
        regex = regex.replace("/*$)" , "/\\*?$)");

        regex = regex.replaceAll("\\.\\.","[^/]+");

        regex = regex.replaceAll("\\/\\*\\/","/[^/]+/");
        regex = regex.replaceAll("\\/\\*\\/","/[^/]+/");

        regex = regex.replaceAll("\\/\\(\\*\\)\\/","/([^/]+)/");
        regex = regex.replaceAll("\\/\\(\\*\\)\\/","/([^/]+)/");

        return regex;
    }

    @Override
    public QParser createParser(String qstr, SolrParams localParams, SolrParams params, SolrQueryRequest req) {

        final StrSubstitutor strSubstitutor = new StrSubstitutor();
        strSubstitutor.setEnableSubstitutionInVariables(true);
        strSubstitutor.setEscapeChar('\\');

        return new QParser(qstr, localParams, params, req) {

            @Override
            public Query parse() throws SyntaxError {

                final ModifiableSolrParams newparams = new ModifiableSolrParams(localParams);
                ModifiableSolrParams newrequest = new ModifiableSolrParams(params);

                Set<String> matchedRules = new LinkedHashSet<>();

                //boolean changedFl = false;

                String virtualpath = localParams.get("v");
                boolean virtual = false;

                for( String rule : rules.keySet() )
                {
                    SolrParams ruleParams = rules.get(rule);

                    String regex = ruleParams.get(REGEX);

                    String current_virtualpath = newparams.get("v");

                    final Matcher matcher = Pattern.compile(regex).matcher( current_virtualpath );

                    if (matcher.matches())
                    {
                        String ifConf = ruleParams.get(IF);

                        if (ifConf!=null && !matchedRules.contains(ifConf) && newparams.get(ifConf)==null)
                        {
                            log.trace("rule '{}' skipped, it doesn't matches 'if' condition '{}' for value '{}'", rule, ifConf, current_virtualpath );
                            continue;
                        }

                        matchedRules.add(rule);

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
                                    return newparams.get(s , System.getProperty(s,"") );
                                }
                            }
                        };

                        strSubstitutor.setVariableResolver(lookup);

                        Iterator<String> iterator = ruleParams.getParameterNamesIterator();

                        while (iterator.hasNext())
                        {
                            String key = iterator.next();

                            if (key.equals(REGEX) || key.equals(IF))
                                continue;

                            String value = ruleParams.get(key);

                            value = value.replaceAll("\\$(\\d)","\\${$1}");

                            value = strSubstitutor.replace(value);

                            if (key.equals("PATH")){
                                key = "v";
                            }

                            if (key.equals("virtual")){

                                String name;

                                if (virtualpath.startsWith("/"))
                                {
                                    int lio = virtualpath.lastIndexOf("/");
                                    name = virtualpath.substring(lio+1);
                                } else {
                                    name = value;
                                }

                                if (newparams.get(CommonParams.FL) == null){
                                    newparams.set(CommonParams.FL,params.getParams(CommonParams.FL));
                                }
                                //newparams.set(CommonParams.FL, Schema.Fields.ID);
                                newparams.add(CommonParams.FL,"name:[value v='"+name+"']");
                                //newparams.add(CommonParams.FL,"type:[value v='virtual']");

                                String[] parts = value.split(":");

                                newparams.add(CommonParams.FL,"facet:[value v='true']");
                                newparams.add(CommonParams.FL,"facet.field:[value v='"+parts[0]+"']");

                                //String phys_path = PATH.substring(0,PATH.lastIndexOf("/"));
                                //newparams.add(CommonParams.FL,"PHYSICAL_PATH:[value v='"+phys_path+"']");

                                //String field = parts[0];

                                if (parts.length>1){
                                    //newparams.add(CommonParams.FL,field + ":[value v='"+val+"']");
                                    newparams.add(CommonParams.FL,"f."+parts[0]+".value:[value v='"+parts[1]+"']");

                                    if (parts.length>2)
                                        newparams.add(CommonParams.FL,"f."+parts[0]+".count:[value v='"+parts[2]+"']");
                                }
                                /*if (parts.length>2){
                                    newparams.add(CommonParams.FL,"count:[value v='"+parts[2]+"']");
                                }*/

                                virtual = true;
                            }

                            if (key.startsWith(CommonParams.FL+".")){
                                String fl = key.substring(3);
                                key = CommonParams.FL+"+";

                                if (value.startsWith("\"") && value.endsWith("\""))
                                    value = "[value v=" + value + "]";

                                value = fl+":"+value;
                            }

                            if (key.endsWith("+"))
                            {
                                key = key.substring(0,key.length()-1);

                                if (newparams.get(key) == null){
                                    newparams.set(key,params.getParams(key));
                                }

                                newparams.add(key, value);

                                log.trace("rule '{}' {}+={}", rule, key, value);
                            }
                            else if(key.endsWith("-"))
                            {
                                key = key.substring(0,key.length()-1);

                                if ("".equals(value))
                                {
                                    newparams.remove(key);
                                    newrequest.remove(key);
                                    log.trace("rule '{}' remove {}", rule, key);
                                }
                                else
                                {
                                    newparams.remove(key,value);
                                    newrequest.remove(key,value);
                                    log.trace("rule '{}' {}-={}", rule, key, value);
                                }
                            }
                            else
                            {
                                log.trace("rule '{}' {} {}->{}", rule, key, newparams.getParams(key),value);
                                newparams.set(key, value);
                            }

                            if (key.equals(CommonParams.FL))
                                newparams.set(Schema.CHANGED_FL, true);
                        }

                        log.debug("new params after appylying rule '{}'\n{}", rule, newparams);
                    }
                }

                String v = newparams.remove("v")[0]; //one and only one

                if (virtual){
                    newparams.set("facet",false);
                    newparams.remove("fq");

                    if (v.equals(virtualpath))
                        v = "/" ;
                }

                Iterator<String> iterator = newparams.getParameterNamesIterator();

                if (iterator.hasNext())
                {
                    while (iterator.hasNext())
                    {
                        String key = iterator.next();

                        /*if (key.equals(CommonParams.FQ))
                            newrequest.add(key, newparams.getParams(key) );
                        else*/
                            newrequest.set(key, newparams.getParams(key) );
                    }

                    if (log.isTraceEnabled())
                        log.trace("request modified by rules:{}\noriginal:{}\nnew params:{}\nnew request:{}",matchedRules,params,newparams,newrequest);
                    else
                        log.info("request modified by rules:{}",matchedRules);
                }

                if (virtualpath.startsWith("/"))
                    newrequest.set(FieldUtils.VIRTUALPATH,virtualpath);

                req.setParams(newrequest);



                String unionQuery = req.getParams().get("uq");

                Query q;

                if ( v.startsWith("\\") )
                {
                    v = v.replace('\\','/');
                }

                if ( v.startsWith("/") )
                {
                    q = parsePATHQuery( v, req );
                }
                else
                {
                    q = QParser.getParser( v, null, req ).getQuery();
                }

                if (unionQuery!=null && q!=null)
                {
                    Query uq = QParser.getParser( unionQuery, null, req ).getQuery();

                    BooleanQuery.Builder builder = new BooleanQuery.Builder();

                    builder.add(q, BooleanClause.Occur.SHOULD);
                    builder.add(uq, BooleanClause.Occur.SHOULD);

                    BooleanQuery bq = builder.build();

                    q = bq;
                }

                if (q==null)
                    q = new MatchNoDocsQuery();

                if (CommonParams.EchoParamStyle.ALL.toString().equals(newrequest.get(CommonParams.HEADER_ECHO_PARAMS)))
                {
                    if (!v.equals("*:*"))
                        newrequest.add(PARSEDPATH,v);

                    if (!(q instanceof MatchAllDocsQuery))
                        newrequest.add(PATH_QUERY,q.toString());
                }

                String datacenter = req.getParams().get(RealTimeSearchHandler.DATACENTER);

                if (datacenter!=null && !datacenter.equals(System.getProperty(Schema.Fields.LOCATION)) )
                {
                    if (CommonParams.EchoParamStyle.ALL.toString().equals(newrequest.get(CommonParams.HEADER_ECHO_PARAMS)))
                        newrequest.add("remote-host",SolrClient.getZkHost(datacenter));
                    return new MatchNoDocsQuery();
                }

                return q;
            }
        };
    }
}
