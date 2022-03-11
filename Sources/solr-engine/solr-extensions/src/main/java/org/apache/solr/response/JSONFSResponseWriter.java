package org.apache.solr.response;

import com.google.common.base.Strings;
import it.kdm.solr.common.FieldUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.apache.solr.common.util.StrUtils;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrQueryRequestBase;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

/**
 * Created by Paolo_2 on 10/08/15.
 */
public class JSONFSResponseWriter extends JSONResponseWriter {

    protected final static ThreadLocal<Boolean> notFound = new ThreadLocal<>();

    public static final String FORMAT = "format";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String COUNT = "count";
    public static final String FACET = "facet";
    public static final String WT_VIRTUAL = "wt.virtual";
    public static final String WT_FIND = "wt.find";
    String defaultFormat = null;
    StrSubstitutor strSubstitutor = new StrSubstitutor();

    public static boolean isNotFound()
    {
        if (notFound.get()!=null)
            return notFound.get();
        else
            return false;
    }

    @Override
    public void write(Writer writer, SolrQueryRequest req, SolrQueryResponse rsp) throws IOException {
        JSONWriter w = new JSONFSWriter(writer, req, rsp);
        try {
            w.writeResponse();
        } finally {
            w.close();
        }
    }

    public void init(NamedList namedList) {
        this.defaultFormat = (String) namedList.get(FORMAT);

        //if (this.defaultFormat != null)
        //    this.defaultFormat = this.defaultFormat.replaceAll("§\\{","\\${");
    }

    public static String writeJSON( NamedList<Object> nl )
    {
        StringWriter sw = new StringWriter(32000);

        JSONResponseWriter responseWriter = new JSONResponseWriter();

        ModifiableSolrParams params = new ModifiableSolrParams();
        params.set( "json.nl" , "map" );
        params.set( "indent" , "true" );

        SolrQueryRequestBase req = new SolrQueryRequestBase(null,params) {};
        SolrQueryResponse rsp = new SolrQueryResponse();

        rsp.setAllValues( nl );

        try
        {
            responseWriter.write(sw,req,rsp);
        }
        catch( Exception e )
        {
            throw new RuntimeException(e);
        }

        //String json = sw.toString();
        return sw.toString();
    }

    class JSONFSWriter extends JSONWriter
    {
        public JSONFSWriter(Writer writer, SolrQueryRequest req, SolrQueryResponse rsp)
        {
            super(writer,req,rsp);
        }

        protected String getAlias(String field)
        {
            String fls[] = req.getParams().getParams("fl");

            List<String> fields = new ArrayList<>();

            String rf = null;

            for( int i=0; i<fls.length; i++)
            {
                fields.addAll( StrUtils.splitSmart(fls[i], ',') );
            }

            for (String f : fields)
            {
                if (f.endsWith(":"+field))
                    rf = f.split(":")[0];
                else if (f.equals(field))
                    rf = f;
            }

            if (rf==null && rsp.getReturnFields().wantsField(field))
                rf = field;

            return rf;
        }

        @Override
        public void writeResponse() throws IOException
        {
            String virtual = this.req.getParams().get(WT_VIRTUAL);

            if (virtual != null)
            {
                SolrDocumentList list = new SolrDocumentList();

                List<String> names = org.apache.solr.common.util.StrUtils.splitSmart(virtual,',');

                for (String name : names )
                {
                    SolrDocument doc = new SolrDocument();

                    String alias = getAlias(ID);

                    if (alias!=null)
                        doc.setField( alias ,name);

                    alias = getAlias(NAME);

                    if (alias!=null)
                        doc.setField( alias ,name);

                    String key = getAlias( "["+ FieldUtils.VIRTUALPATH+"]" );

                    if (key!=null)
                        doc.setField(key, FieldUtils.buildVirtualPATH(req, name) );

                    list.add(doc);
                }

                list.setNumFound(names.size());

                NamedList<Object> nl = rsp.getValues();
                nl.remove("response");

                nl.add("response",list);
            }

            //String[] fields = this.req.getParams().getParams("facet.field");

            NamedList val = rsp.getValues();

            //if (fields != null && fields.length>0)
            //{
            NamedList facetFields = (NamedList) val.findRecursive( "facet_counts" , "facet_fields" );

            SolrDocumentList response = (SolrDocumentList) val.get("response");

            String nameField = getAlias(NAME);
            String idField = getAlias(ID);
            String countField = getAlias(COUNT);
            String pathField = getAlias("[" + FieldUtils.VIRTUALPATH + "]");

            /*if (Strings.isNullOrEmpty(idField))
                idField = ID;
            if (Strings.isNullOrEmpty(nameField))
                nameField = NAME;
            if (Strings.isNullOrEmpty(countField))
                countField = COUNT;
            if (Strings.isNullOrEmpty(pathField))
                countField = "[" + FieldUtils.VIRTUALPATH + "]";*/

            if (facetFields!=null && facetFields.size() > 0 /*&& (response==null || response.size()==0)*/ )
                {
                    if (response==null){
                        response = new SolrDocumentList();
                        val.add("response", response);
                    }

                    for ( int fidx=0; fidx< facetFields.size(); fidx++) {

                        String field = facetFields.getName(fidx);

                        String format = this.req.getParams().getFieldParam(field,FORMAT,"%s (%s)");

                        NamedList facetValues = (NamedList) facetFields.getVal(fidx);

                        for (int i = 0; i < facetValues.size(); i++) {
                            SolrDocument doc = new SolrDocument();

                            String name = facetValues.getName(i);
                            Object count = facetValues.getVal(i);

                            name = String.format(format,name,count);

                            doc.setField(idField, name);

                            /*if (format != null) {
                                if (format.contains("${")){
                                    Map<String, String> map = new HashMap<>();
                                    //map.put(key, value);
                                    map.put(NAME, name);
                                    map.put(COUNT, count.toString());

                                    name = strSubstitutor.replace(format, map);
                                } else if (format.contains("%")){
                                    name = String.format(format,name,count);
                                }
                            }*/

                            if (!Strings.isNullOrEmpty(countField))
                                doc.setField(countField, count);

                            if (!Strings.isNullOrEmpty(nameField))
                                doc.setField(nameField,name);

                            if (!Strings.isNullOrEmpty(pathField))
                                doc.setField(pathField, FieldUtils.buildVirtualPATH(req, name));

                            response.add(doc);
                            response.setNumFound(response.getNumFound()+1);

                        }
                    }

                    //val.add("response", list);
                }
            //}

            String jspath = this.req.getParams().get(WT_FIND);

            if (!Strings.isNullOrEmpty(jspath))
            {
                if (jspath.startsWith("/"))
                    jspath = jspath.substring(1);

                String[] args = jspath.split("/");
                NamedList<Object> nl = rsp.getValues();

                NamedList<?> currentList = null;
                Object value = null;
                for (int i = 0; i < args.length; i++) {
                    String key = args[i];

                    if (currentList == null) {
                        currentList = nl;
                    } else {

                        if (value instanceof SolrDocumentList)
                        {
                            SimpleOrderedMap som = new SimpleOrderedMap<>();
                            som.add("docs", new ArrayList((List) value)); 
                            value = (NamedList<?>) som;
                        }

                        if (value instanceof NamedList) {
                            currentList = (NamedList<?>) value;
                        } else {
                            value = null;
                            break;
                        }
                    }
                    value = currentList.get(key, 0);
                }

                int status = (int) rsp.getResponseHeader().get("status");

                if (status!=0)
                {
                    nl.remove("responseHeader");
                    super.writeResponse();

                }
                else if (value==null)
                {
                    NamedList el = new SimpleOrderedMap();
                    el.add("code",404);
                    el.add("msg", "Not found"); 
                    nl.clear();
                    nl.add("error", el);
                    super.writeResponse();
                    notFound.set(true);
                }
                else
                {
                    writeVal(null, value);
                    writer.write('\n');

                }

                return;
            }

            super.writeResponse();
        }
    }

}
