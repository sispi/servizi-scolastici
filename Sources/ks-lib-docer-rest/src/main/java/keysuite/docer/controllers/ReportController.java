package keysuite.docer.controllers;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiImplicitParam;
import it.kdm.orchestratore.session.Session;
import it.kdm.orchestratore.session.UserInfo;
import keysuite.cache.ClientCacheAuthUtils;
import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;
import keysuite.desktop.exceptions.KSRuntimeException;
import keysuite.docer.client.Anagrafica;
import keysuite.docer.client.ClientUtils;
import keysuite.docer.client.DocerBean;
import keysuite.docer.query.ISearchResponse;
import keysuite.docer.server.BaseService;
import keysuite.docer.server.DocumentiService;
import keysuite.docer.server.ServerUtils;
import keysuite.solr.QueryParams;
import keysuite.solr.SolrSimpleClient;
import org.apache.commons.codec.binary.Base64;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.RangeFacet;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.IOUtils;
import org.apache.solr.common.util.NamedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.*;

import static keysuite.docer.client.ClientUtils.throwKSExceptionDefBadRequest;

@RestController
@CrossOrigin(origins = "*")
public class ReportController extends BaseController {

    @Autowired
    DocumentiService documentiService;

    @GetMapping("/search")
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public ISearchResponse search(HttpServletRequest request) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        String qs = request.getQueryString();
        QueryParams params = new QueryParams(qs);
        return documentiService.solrSimpleSearch(params);
    }

    @GetMapping("/solr/{qt}")
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public Object solr(@PathVariable String qt, HttpServletRequest request) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {

        String qs = request.getQueryString();
        QueryParams params = new QueryParams(qs);
        params.setQt("/"+qt);

        QueryResponse solrResponse = documentiService.solrSelect(params);

        SolrDocumentList data = solrResponse.getResults();
        Integer recordCount = null;

        if (data==null) {
            data = new SolrDocumentList();
            recordCount = 0;
        } else {
            recordCount = ((Long)solrResponse.getResults().getNumFound()).intValue();
        }

        Map<String, Map<String, Integer>> facets = new LinkedHashMap<String, Map<String, Integer>>();

        List<FacetField> facetFields = solrResponse.getFacetFields();
        if (facetFields!=null&&facetFields.size()>0){

            for( FacetField facetField : facetFields ){

                List<FacetField.Count> values = facetField.getValues();

                if (values!=null){
                    Map<String,Integer> facetValues = new LinkedHashMap<>();
                    facets.put( facetField.getName() , facetValues );
                    for( int i=0; i<values.size(); i++){
                        FacetField.Count count = values.get(i);
                        facetValues.put( count.getName() , new Long(count.getCount()).intValue() );
                    }
                }
            }
        }

        facetFields = solrResponse.getFacetDates();
        if (facetFields!=null&&facetFields.size()>0){

            for( FacetField facetField : facetFields ){

                List<FacetField.Count> values = facetField.getValues();

                if (values!=null){
                    Map<String,Integer> facetValues = new LinkedHashMap<>();
                    facets.put( facetField.getName() , facetValues );
                    for( int i=0; i<values.size(); i++){
                        FacetField.Count count = values.get(i);
                        facetValues.put( count.getName() , new Long(count.getCount()).intValue() );
                    }
                }
            }
        }

        List<RangeFacet> facetRanges = solrResponse.getFacetRanges();
        if (facetRanges!=null&&facetRanges.size()>0){

            for( RangeFacet facetField : facetRanges ){

                List<FacetField.Count> values = facetField.getCounts();

                if (values!=null){
                    Map<String,Integer> facetValues = new LinkedHashMap<>();
                    facets.put( facetField.getName() , facetValues );
                    for( int i=0; i<values.size(); i++){
                        FacetField.Count count = values.get(i);
                        facetValues.put( count.getName() , new Long(count.getCount()).intValue() );
                    }
                }
            }
        }



        Map<String,Object> result = new HashMap<>();
        result.put("data",data);
        result.put("facets",facets);
        result.put("recordCount",recordCount);

        return execute(params,result,request);
    }

    @GetMapping({"/reports/{report}","/report"})
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public Object report(@PathVariable(required = false) String report, HttpServletRequest request) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        String qs = request.getQueryString();
        QueryParams params = new QueryParams(qs);

        return report(report, params, request);
    }

    @PostMapping( path ={"/reports/{report}","/report"} )
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public Object reportform(@PathVariable(required = false) String report, HttpServletRequest request) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {

        String ct = request.getContentType();
        QueryParams params;
        if ("text/x-java-properties".equals(ct)){
            try {
                Properties props = new Properties();
                props.load(request.getInputStream());
                params = new QueryParams();
                for( String k : props.stringPropertyNames() )
                    params.set(k, props.getProperty(k));
            } catch (Exception e ) {
                throw new KSRuntimeException(e);
            }
        } else if ("application/x-www-form-urlencoded".equals(ct)) {
            Map<String, String[]> r = request.getParameterMap();
            params = new QueryParams(r);
        } else if ("application/json".equals(ct)){
            try {
                String str = StreamUtils.copyToString(request.getInputStream(), Charset.defaultCharset());
                Map<String,Object> m = ServerUtils.OM.readValue(str, Map.class);
                params = new QueryParams();
                for( String k : m.keySet() )
                    if (m.get(k)!=null)
                        params.set(k, m.get(k).toString());
            } catch (Exception e ) {
                throw new KSRuntimeException(e);
            }
        } else {
            throw new KSExceptionBadRequest("invalid content type:"+ct);
        }

        return report(report, params, request);
    }

    public Object report(@PathVariable(required = false) String report,QueryParams params, HttpServletRequest request) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {

        UserInfo ui = Session.getUserInfoNoExc();
        String ticket = ui.getPrefixedUsername();

        String base64 = params.get("base64");
        if (base64!=null){
            boolean valid = false;
            params.remove("base64");

            if (base64.length()>32){
                String checksum1 = base64.substring(base64.length()-32);
                base64 = base64.substring(0,base64.length()-32);
                String checksum2 = ClientCacheAuthUtils.generateMD5(base64);

                byte[] data = Base64.decodeBase64(base64);

                String qs2 = new String(data);

                QueryParams params2 = new QueryParams(qs2);
                params.add(params2);

                valid = checksum1.equalsIgnoreCase(checksum2);
            }
            if (!valid)
                throw new KSExceptionBadRequest("invalid base64 checksum");
        }

        QueryParams origParams = new QueryParams(params);

        params.set("ticket",ticket);
        params.set("codiceAoo",ui.getCodAoo());
        params.set("codiceEnte",ui.getCodEnte());

        if (report!=null)
            params.set("report",report);

        if (params.get("report")==null && !params.get("qt","/select").startsWith("/"))
            params.set("report",params.get("qt"));

        params.set("qt","/report");

        //if (qt==null)
        //    throw new KSExceptionBadRequest("report non specificato tramite path, qt o report");

        Map result;
        try {
            NamedList nl = new SolrSimpleClient().getServer().query(params).getResponse();
            result = (Map) nl.get("results");
        } catch (Exception e) {
            throw throwKSExceptionDefBadRequest(e);
        }

        return execute(origParams,result,request);

    }

    private Map<String,Object> execute(QueryParams params, Map<String,Object> response, HttpServletRequest request) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden{
        try {

            String wt = params.get("wt","report");

            String oType = params.get("output.type");
            boolean bean = false;
            if ("bean".equals(oType)){
                bean = true;
                params.set("output.type","map");
            }

            if (bean){

                List data = (List) response.get("data");
                List<DocerBean> results = new ArrayList<>();

                for(Object item : data ){

                    Collection<String> keys;

                    if (item instanceof List){
                        keys = (Collection<String>) response.get("columns");
                    } else {
                        keys = ((Map)item).keySet();
                    }

                    SolrDocument doc = new SolrDocument();

                    int idx = 0;
                    for( String key : keys ){
                        Object value;
                        if (item instanceof List)
                            value = ((List)item).get(idx);
                        else
                            value = ((Map)item).get(key);

                        idx++;

                        doc.setField(key,value);
                    }

                    Class cls = Anagrafica.class;
                    String id = (String) doc.get("id");
                    if (id!=null && id.contains("@")){
                        cls = ClientUtils.getClassForBean(id);
                        /*String type = doc.get("id").toString().split("@")[1];

                        try {
                            cls = Class.forName("keysuite.docer.client."+ StringUtils.capitalize(type));
                        } catch (ClassNotFoundException e) {
                            if ("aoo".equals(type) || "ente".equals(type))
                                cls = Group.class;
                            else
                                cls = Anagrafica.class;
                        }*/
                    }

                    ServerUtils.removePrefix(doc);
                    results.add( BaseService.evalPermissions(doc, ClientUtils.toClientBean(doc,cls)));
                }
                response.put("data",results);
            }

            if (!wt.equals("json")){

                if(!wt.equals("data") && !wt.equals("csv") && !wt.equals("report") && !wt.equals("print")){
                    throw new KSExceptionBadRequest("invalid wt:"+wt);
                }

                response = getReportResults(response,params,request);
            }

            return response;
        } catch (Exception e) {
            throw throwKSExceptionDefBadRequest(e);
        }
    }

    public static Map<String,Object> getReportResults(Map<String,Object> response, QueryParams qparams,HttpServletRequest request) throws Exception {

        Map<String,Object> ret = _getReportModel(response,qparams,request);

        for( String key : ret.keySet().toArray(new String[0]) ){
            Object val = ret.get(key);
            if (val==null){
                ret.remove(key);
                continue;
            }
        }

        return ret;
    }

    private static Map<String,Object> _getReportModel(Map<String,Object> retCall, QueryParams qparams, HttpServletRequest request) throws Exception {

        String qt = qparams.get("qt");
        Map<String,String> properties = null;

        if (retCall.containsKey("buffers"))
            properties = (Map<String,String>) ((Map<String,Object>)retCall.get("buffers")).get("properties");

        if (properties==null)
            properties = new HashMap<>();

        QueryParams changeParams = new QueryParams(qparams);
        changeParams.remove("orderBy");
        changeParams.remove("sort");
        changeParams.remove("pageNumber");
        changeParams.remove("echoProperties");

        List<String> pars = new ArrayList<>(properties.keySet());

        //pars.removeAll(QueryParams.knownPars);

        for( String p : pars ){

            if (QueryParams.isKnown(p))
                continue;

            String v = properties.get(p);
            if (v!=null && !qparams.contains(p)){
                if ("ACTOR".equals(v) || "CURRENT".equals(v)){
                    v = Session.getUserInfoNoExc().getUsername();
                }
                qparams.setSplitted(p,v);
                //changeParams.setSplitted(p,v);
            }
        }

        Set<String> facets = new LinkedHashSet<>();
        Map<String,Boolean> combos = new LinkedHashMap<>();

        //String facetConfig = ToolkitConnector.getGlobalProperty(String.format("report.%s.facets.inline",qt) ,"");

        //List<String> facetlist = Arrays.asList( facetConfig.split(",") );

        Map<String,Map<String,Integer> > allFacetsNums = new LinkedHashMap<>();

        for( String echo : properties.keySet() ){
            if (echo.startsWith("facet.") && echo.endsWith(".options") && properties.get(echo).contains("inline")){
                String facet = echo.split("\\.")[1];
                allFacetsNums.put(facet,new HashMap<String,Integer>() );
            }
        }

        allFacetsNums.putAll( (Map) retCall.get("facets"));

        String businessState = properties.get("facet.businessState");
        String processName=null;

        if (Strings.isNullOrEmpty(businessState))
            businessState = "businessState:processName";

        String[] parts = businessState.split(":");

        businessState = parts[0];
        if (parts.length>1){
            String[] x = qparams.getParams(parts[1]);
            if (x!=null && x.length==1){
                processName = x[0];
            }
        }

        List<String> actors = Session.getUserInfoNoExc().getGroups();
        actors.add(Session.getUserInfoNoExc().getUsername());

        String actor = Session.getUserInfoNoExc().getUsername();

        //List<String> hide = Arrays.asList(resplit(qparams.getParams("hide")));

        boolean reset = qparams.getParameterNames().size()>0;

        for( String facet : allFacetsNums.keySet() ){

            if (facet.equals(businessState) && Strings.isNullOrEmpty(processName))
                continue;

            //if (qparams.contains(facet))
            //    reset = true;

            //if (hide.contains(facet))
            //	continue;


            //String valS = ToolkitConnector.getGlobalProperty( String.format("report.%s.%s.values",qt,facet) );

            String optionS = properties.get( String.format("facet.%s.options",facet) );

            if (optionS==null)
                optionS = "";

            List<String> optionL = Arrays.asList(optionS.toLowerCase().split(","));

            boolean inline = optionL.contains("inline");
            boolean multivalue = optionL.contains("multivalue");
            boolean hidden = optionL.contains("hidden");
            //boolean actor = optionL.contains("actor");

            String valS = properties.get( String.format("facet.%s.values",facet) );

            Map<String,Integer> counts = allFacetsNums.get(facet);

            if (!Strings.isNullOrEmpty(valS)){

                Map<String,Integer> counts0 = counts;
                counts = new LinkedHashMap<>();

                allFacetsNums.put(facet, counts);

                String[] vals = valS.split(",");
                for( int i=0; i<vals.length; i++){
                    counts.put(vals[i], counts0.get(vals[i]) );
                }

                for ( String key : counts0.keySet() ){
                    if (!counts.containsKey(key))
                        counts.put(key,counts0.get(key));
                    //counts.put(ActorsCache.getDisplayName(key),counts0.get(key));
                }
            } /*else {
                counts =  allFacetsNums.get(facet);
                for ( String key : counts0.keySet() ){
                    counts.put(key,counts0.get(key));
                    //counts.put(ActorsCache.getDisplayName(key),counts0.get(key));
                }
            }*/

            String facetValue = qparams.get(facet);

            if ( hidden || counts.size() == 0 || (counts.size() == 1 && Strings.isNullOrEmpty(facetValue)) )
                continue;

            if ("CURRENT".equals(facetValue) || "ACTOR".equals(facetValue) ){
                String v = counts.keySet().iterator().next();
                qparams.set(facet,v);
                changeParams.set(facet,v);
            } else if ("ACTORS".equals(facetValue)){

                List<String> vals = new ArrayList<>(actors);
                vals.retainAll(counts.keySet());

                qparams.set(facet, vals.toArray(new String[0]) );
                changeParams.set(facet, vals.toArray(new String[0]) );
            }

            String[] facetValues = qparams.getParams(facet);

            if (!inline && multivalue && facetValues!=null && facetValues.length>0 ){

                Map<String,Integer> counts0 = counts;
                counts = new LinkedHashMap<>();

                allFacetsNums.put(facet, counts);

                for( int i=0; i<facetValues.length; i++){
                    counts.put(facetValues[i], counts0.get(facetValues[i]) );
                }

                for ( String key : counts0.keySet() ){
                    if (!counts.containsKey(key))
                        counts.put(key,counts0.get(key));
                }
            }

            //allFacetsNums.put(facet, counts);

            if (inline)
                facets.add(facet);
            else
                combos.put(facet,multivalue);
        }

        String joinClause = qparams.get("join.field",properties.get("join.field"));

        String arrayField = null;

        if (!Strings.isNullOrEmpty(joinClause) && joinClause.contains(":")){
            if (joinClause.endsWith("-*") || joinClause.toLowerCase().endsWith("-n"))
                arrayField = joinClause.split(":")[0];
        }

        String wt = qparams.get("wt",properties.get("wt"));

        Map<String,Object> model = new LinkedHashMap<>();

        if ("json".equals(wt) || "data".equals(wt) || "csv".equals(wt) ){

            //String content;
            //Writer writer = response.getWriter();

            if ("csv".equals(wt)) {
                //response.setContentType("text/plain; charset=utf-8");

                model.put("columns", retCall.get("columns"));
                model.put("data", retCall.get("data") );
                model.put("arrayField", arrayField);
                model.put("content-type", "text/csv; charset=utf-8");

                model.put("page", "csv" );

                //content = KDMUtils.ftlHandler("csv", model.getModel() );
                //model.setViewName("csv");
            } else {
                //response.setContentType("application/json; charset=utf-8");
                String content;

                if ("json".equals(wt)){
                    model.put("content",retCall);
                } else {
                    model.put("content",retCall.get("data"));
                    //content = new ObjectMapper().writeValueAsString(retCall.getData());
                }

                model.put("content-type", "application/json; charset=utf-8");

                model.put("page", "json" );

                //model.setViewName("json");

                //content = new ObjectMapper().writeValueAsString( "json".equals(wt)  ? retCall : retCall.getData() );
            }

            //writer.write(content);
            //writer.flush();

            return model;
        }

        List<String> renderers;

        String resultFtl = qparams.get("ftl",properties.get("ftl"));

        String eRenderers = properties.get("ftls");

        if (Strings.isNullOrEmpty(eRenderers))
            renderers = Lists.newArrayList();
        else
            renderers = Lists.newArrayList(eRenderers.split(","));

        if (Strings.isNullOrEmpty(resultFtl)){
            if (renderers.isEmpty())
                resultFtl = "results";
            else
                resultFtl = renderers.get(0);
        }

        if (!renderers.contains(resultFtl))
            renderers.add(0,resultFtl);

        Map buffers = new HashMap<>();
        if (retCall.containsKey("buffers"))
            buffers.putAll( (Map) retCall.get("buffers"));

        Map<String,Integer> idxs = new LinkedHashMap<>();

        if (retCall.containsKey("columns")) {
            List<String> columns = (List) retCall.get("columns");
            for (int i = 0; i < columns.size(); i++) {
                idxs.put(columns.get(i), i);
            }
        }

        boolean showStats = !"false".equals(properties.get("showStats"));

        String changeQuerystring = "&"+changeParams.toString();

        model.put("action",properties.get("action"));
        //model.addObject("response",retCall);
        model.put("qt", qt);
        model.put("title", retCall.get("title"));
        model.put("subtitle", properties.get("subtitle") );
        model.put("form", properties.get("form") );
        model.put("altftls",new LinkedHashSet<>(renderers));
        model.put("ftls",new LinkedHashSet<>(renderers));

        model.put("ftl",resultFtl);

        model.put("actorId",actor);
        model.put("actors",actors);
        model.put("isAdmin", Session.getUserInfo().isAdmin());
        //model.addObject("context",request.getContextPath());
        model.put("params", properties);
        model.put("properties", properties);
        //model.addObject("req",qparams.toMap());
        model.put("lreq",qparams.toListMap());
        model.put("req",qparams.toMap());
        model.put("querystringParams",changeQuerystring);

        model.put("showStats",showStats);
        model.put("totResults",retCall.get("recordCount"));
        model.put("recordCount",retCall.get("recordCount"));
        model.put("elapsed",retCall.get("elapsed"));
        model.put("totPage",retCall.get("pageCount"));
        model.put("pageCount",retCall.get("pageCount"));
        model.put("pageNumber",retCall.get("pageNumber"));
        model.put("orderBy",retCall.get("orderBy"));

        model.put("moreItems", "true".equals(properties.get("facet.moreItems")) );
        model.put("facets",facets);
        model.put("combos",combos);
        model.put("counts",allFacetsNums);
        model.put("reset", reset);
        model.put("businessState",businessState);
        model.put("processName",processName);

        model.put("buffers",buffers);
        model.put("sortSpecs",retCall.get("sortSpecs"));
        model.put("data",retCall.get("data"));
        //model.addObject("records",retCall.getData());
        model.put("series",retCall.get("series"));
        model.put("columns",retCall.get("columns"));
        model.put("groups",retCall.get("colGroups"));
        model.put("colGroups",retCall.get("colGroups"));
        model.put("spans",retCall.get("colGroupsSpan"));
        model.put("colGroupsSpan",retCall.get("colGroupsSpan"));
        model.put("colIndexes",idxs);
        model.put("arrayField",arrayField);
        model.put("joinField",arrayField);

        model.put("pathIdx", Session.getUserInfo().getCurrentTreeviewProfile().length()+1);
        //model.addObject("utils",new TemplateUtils());

        model.put("parameters", retCall.get("parameters"));

        String page = qparams.get("page",properties.get("page"));

        if (Strings.isNullOrEmpty(page))
            page = "report";

        model.put("view",resultFtl);
        model.put("page",page);
        //model.setViewName(page);

        if (request!=null){

            if (request.getServletPath().endsWith("/"+qt))
                changeParams.remove("qt");

            request.setAttribute("properties",properties);

            //model.addObject("baseUrl",request.getServletPath());

            String xref = request.getHeader("X-baseurl");

            //retrocompatibilità vecchi template ftl
            if (!Strings.isNullOrEmpty(xref)) {
                String[] xparts = xref.split("\\?");
                //model.addObject("baseUrl", xparts[0]);
                //dai qs params rimuovi quelli di base
                if (xparts.length>1) {
                    //changeQuerystring
                    String querystringParams = changeQuerystring; // (String) model.get("querystringParams");
                    if (!Strings.isNullOrEmpty(querystringParams)) {
                        querystringParams = querystringParams.replace(xparts[1],"").replaceAll("&+","&");
                        model.put("querystringParams", querystringParams);
                    }
                }
            }
        }

        return model;

    }
}
