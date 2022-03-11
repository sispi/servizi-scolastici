package it.kdm.solr.common;

//import org.apache.poi.openxml4j.exceptions.InvalidOperationException;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrRequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.*;
import java.util.*;

public class ExpressionBindings implements Bindings
{
    private static Logger log = LoggerFactory.getLogger(ExpressionBindings.class);
    private SolrDocument doc;
    private SolrQueryRequest req;
    private Map<String,Object> map;

    public ExpressionBindings(SolrDocument doc, SolrQueryRequest req)
    {
        this.doc = doc;
        this.req = req;
        this.map = new HashMap<>();
    }

    public ExpressionBindings(Map<String, Object> map)
    {
        this.doc = new SolrDocument();
        this.req = null;
        this.map = map;
    }

    public static Object eval(SolrDocument doc, SolrQueryRequest req, String expr) {
        return eval( (Map<String,Object>) doc,req,expr);
    }

    public static Object eval(SolrDocument doc, String expr) {
        return eval( (Map<String,Object>) doc,expr);
    }

    public static Object eval(Map<String,Object> doc, String expr) {
        if (SolrRequestInfo.getRequestInfo()!=null)
            return eval(doc,SolrRequestInfo.getRequestInfo().getReq(),expr);
        else
            return eval(doc,null,expr);
    }

    private static Object eval(Map<String,Object> doc, SolrQueryRequest req, String expr) {

        if (expr==null)
            return null;

        if (expr.startsWith("tmpl:"))
        {
            expr = expr.substring("tmpl:".length());

            FieldSubstitutor substitutor = new FieldSubstitutor(doc);
            return substitutor.replace(expr);
        }
        else
        {
            Boolean isJs = false;

            if (expr.startsWith("javascript:"))
            {
                isJs = true;
                expr = expr.substring("javascript:".length());
            }

            ExpressionBindings bindings;

            if (doc instanceof SolrDocument)
                bindings = new ExpressionBindings( (SolrDocument) doc,req);
            else
                bindings = new ExpressionBindings(doc);

            ScriptContext newContext = new SimpleScriptContext();

            newContext.setBindings(bindings, ScriptContext.ENGINE_SCOPE);

            ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");

            try {
                return engine.eval(expr, newContext);
            } catch (ScriptException e) {

                if (isJs)
                    throw new RuntimeException(e);

                FieldSubstitutor substitutor = new FieldSubstitutor(doc);
                Object res = substitutor.replace(expr);

                if (expr.equals(res))
                    throw new RuntimeException(e);

                return res;
            }
        }
    }

    public boolean isEmpty()
    {
        return doc.isEmpty() && map.isEmpty();
    }

    public void putAll(Map map)
    {
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = (String)it.next();
            if (doc.containsKey(key))
                throw new IllegalStateException("Can't modify document adding key '"+key+"' in document");
        }

        this.map.putAll(map);
    }

    public int size()
    {
        return doc.size()+this.map.size();
    }

    public boolean containsValue(Object val)
    {
        return doc.containsValue(val) || this.map.containsValue(val);
    }

    public boolean containsKey(Object key)
    {
        return doc.containsKey(key) || map.containsKey(key);
    }

    public void clear()
    {
        map.clear();
    }

    public Set<String> keySet()
    {
        HashSet<String> set = new HashSet<>();
        set.addAll(doc.keySet());
        set.addAll(map.keySet());

        return set;
    }

    @Override
    public java.util.Collection<java.lang.Object> values()
    {
        java.util.Collection<java.lang.Object> coll = new ArrayList<>();

        coll.addAll(doc.values());
        coll.addAll(map.values());

        return coll;
    }

    @Override
    public Object put(String s, Object o) {

        if (doc.containsKey(s))
            throw new IllegalStateException("Can't modify document adding key '"+s+"'");

        return map.put(s,o);
    }

    @Override
    public Object get(Object s) {

        if (s==null)
            return null;

        if (doc.containsKey(s) && req!=null)
            return DocUtils.convertIndexableField(req, doc, s.toString());
        else
            return map.get(s);
    }

    @Override
    public Object remove(Object s) {

        if (doc.containsKey(s))
            throw new IllegalStateException("Can't modify document removing key '"+s.toString()+"'");

        return map.remove(s);
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {

        HashSet<Entry<String, Object>> set = new HashSet<>();
        set.addAll(map.entrySet());
        set.addAll(doc.getFieldValueMap().entrySet());

        return set;
    }
}
