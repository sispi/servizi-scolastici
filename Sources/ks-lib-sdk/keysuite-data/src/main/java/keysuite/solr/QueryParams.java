package keysuite.solr;

import com.google.common.base.Strings;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;

import java.util.*;

public class QueryParams extends ModifiableSolrParams {

    public QueryParams(Set<Map.Entry<String,String[]>> entrySet){
        for ( Map.Entry<String,String[]> entry : entrySet){
            set(entry.getKey(),entry.getValue());
        }
    }

    public QueryParams setRows(int rows){
        return (QueryParams) set("rows",rows);
    }

    public Integer getRows(){
        return getInt("rows");
    }

    public QueryParams setPageSize(Integer pageSize){
        return (QueryParams) set("pageSize",pageSize);
    }

    public Integer getPageSize(){
        return getInt("pageSize");
    }

    public QueryParams setPageNumber(int pageNumber){
        return (QueryParams) set("pageNumber",pageNumber);
    }

    public Integer getPageNumber(){
        return getInt("pageNumber");
    }

    public QueryParams setOrderBy(String orderBy){
        return (QueryParams) set("orderBy",orderBy);
    }

    public String getOrderBy(){
        return get("orderBy");
    }

    public QueryParams setQt(String qt){
        return (QueryParams) set("qt",qt);
    }

    public String getQt(){
        return get("qt");
    }

    public QueryParams setQuery(String query){
        return (QueryParams) set("query",query);
    }

    public String getQuery(){
        return get("query");
    }

    final static List<String> knownPars = Arrays.asList(
            "actors","actorId","groups","codiceEnte","codiceAoo","nocount","title","subtitle",
            "rows","pageSize","pageNumber","orderBy","sort","query","wt","qt","buffers","ticket",
            "action","form-ftl","ftl","ftls","showStats","form","category","_","echoParams","echoProperties","export","category","form","orderBy");

    final static String knownPrefixes = "^(output|sortable|facet|joinfacet|query|join|buffer)\\..*$";

    public static final boolean isKnown(String param){
        return knownPars.contains(param) || param.matches(knownPrefixes);
    }

    private static Map<String,String[]> check(Map<String,String[]> pmap){
        Map<String,String[]> pmap2 = new LinkedHashMap<>();

        for( String key : pmap.keySet() ){
            String[] vals = pmap.get(key);

            if (key.contains("<>")) {
                String[] parts = key.split("<>");
                if (parts.length==2 && parts[1].length()>0) {
                    vals = new String[]{parts[1]};
                    key = "-"+parts[0];
                }
            }if (key.contains("<")){
                String[] parts = key.split("<");
                if (parts.length==2 && parts[1].length()>0) {
                    vals = new String[]{parts[1]};
                    key = parts[0] + "}";
                }
            } else if (key.contains(">")){
                String[] parts = key.split(">");
                if (parts.length==2 && parts[1].length()>0) {
                    vals = new String[]{parts[1]};
                    key = parts[0] + "{";
                }
            }

            if (vals!=null){
                boolean isEmpty = true;
                for( String val : vals ){
                    if (!Strings.isNullOrEmpty(val))
                        isEmpty = false;
                }
                if (!isEmpty)
                    pmap2.put(key,vals);
            }
        }
        return pmap2;
    }

    public static QueryParams create(Map<String,String[]> pmap){
        return new QueryParams(check(pmap));
    }

    public QueryParams(){
        super();
    }

    public QueryParams(Map<String, String[]> params){
        super(check(params));
    }

    public QueryParams(String querystring){
        super(check(SolrUtils.parseQueryString(querystring).getMap()));
    }

    public QueryParams(SolrParams params){
        super(params);
    }

    public boolean isNullOrEmpty(String name){
        return Strings.isNullOrEmpty(this.get(name));
    }

    public String getJoined(String name){
        return org.apache.commons.lang3.StringUtils.join( this.getParams(name) ,",");
    }

    public ModifiableSolrParams setSplitted(String name, String val){
        if (val==null) return this.set(name,null);
        return this.set(name, val.split(","));
    }

    public ModifiableSolrParams addSplitted(String name, String val){
        if (val==null) return this.add(name,null);
        return this.add(name, val.split(","));
    }

    public boolean contains(String name) {
        return this.getParams(name) != null;
    }

    public Map<String,String> toMap(){
        Map<String,String> map = new LinkedHashMap<>();
        for( String p : this.getParameterNames() )
            map.put(p,this.get(p));
        return map;
    }

    public Map<String,String> toJoinedMap(){
        Map<String,String> map = new LinkedHashMap<>();
        for( String p : this.getParameterNames() )
            map.put(p,this.getJoined(p));
        return map;
    }

    public Map<String,String[]> toArrayMap(){
        Map<String,String[]> map = new LinkedHashMap<>();
        for( String p : this.getParameterNames() )
            map.put(p,this.getParams(p));
        return map;
    }

    public Map<String,List<String>> toListMap(){
        Map<String,List<String>> map = new LinkedHashMap<>();
        for( String p : this.getParameterNames() ){
            String[] vals = this.getParams(p);
            if (vals!=null)
                map.put(p, Arrays.asList(vals));
            else
                map.put(p,null);
        }
        return map;
    }
}
