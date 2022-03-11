package it.kdm.solr.common;

import com.google.common.base.Strings;
import it.kdm.solr.core.Schema;
import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.solr.request.SolrRequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.solr.common.SolrInputDocument;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Created by Paolo_2 on 29/08/15.
 */
public class FieldSubstitutor extends StrSubstitutor {

    private static Logger log = LoggerFactory.getLogger(FieldSubstitutor.class);
    public final static String ESCAPE_MARK = "__ESCMK__";

    private StrLookup lookup = new StrLookup() {

        @Override
        public String lookup(String key)
        {
            log.debug("key:{}",key);

            boolean encode = false;
            boolean decode = false;
            boolean isId = false;
            int hash = 0;

            if (key.charAt(0) == FieldUtils.encFsChar)
            {
                encode=true;
                isId = false;
                key = key.substring(1,key.length());
            }

            if (key.charAt(0) == FieldUtils.encIdChar)
            {
                encode=true;
                isId = true;
                key = key.substring(1,key.length());
            }

            if (key.charAt(0) == '~')
            {
                /*if (cm == null)
                    throw new RuntimeException("invalid syntax without cm:"+key);

                hash = cm.sequence.hashCode();*/

                if (sequence == null)
                    throw new RuntimeException("invalid syntax without sequence:"+key);

                hash = sequence.hashCode();

                key = key.substring(1,key.length());


            }

            if (key.charAt(key.length() -1) == FieldUtils.encPerc)
            {
                decode=true;
                key = key.substring(0,key.length()-1);
            }

            log.trace("key:{} encode:{} decode:{} isId:{} hash:{}", key, encode, decode, isId, hash);

            key = FieldSubstitutor.this.replace(key);

            log.trace("after substitutor:{}", key);

            String def = "__NULL__";
            String regex = null;
            String replace = null;

            int idx = key.indexOf("/");

            if (idx != -1)
            {
                    /* la forma attesa è <key>/<regex>/<replace>[:def] */

                    /* cerco la fine del blocco /...../ saltando gli slash escapati */
                int idx2 = key.indexOf("/",idx+1);
                if (idx2 > 0)
                {
                    for(int i=idx2;i<key.length();i++)
                    {
                        idx2 = -1;
                        if (key.charAt(i)=='/' && key.charAt(i-1)!='\\' )
                        {
                            idx2 = i;
                            break;
                        }
                    }
                }

                if (idx2>0)
                {
                    regex = key.substring(idx+1,idx2);
                    replace = key.substring(idx2+1);
                    key = key.substring(0,idx);
                }
                else
                {
                    throw new RuntimeException("invalid syntax:"+key);
                }

                idx = replace.indexOf(":");

                if (idx != -1)
                {
                    def = replace.substring(idx+1);
                    replace = replace.substring(0,idx);
                }
            }
            else
            {
                    /* <key[:def> */
                idx = key.indexOf(":");

                if (idx != -1)
                {
                    def = key.substring(idx+1);
                    key = key.substring(0,idx);
                }
            }

            Collection<Object> vals = null;

            if ("_prefix_".equals(key))
            {
                def = "";
                if (SolrRequestInfo.getRequestInfo()!=null)
                    vals = Collections.singletonList((Object) SolrRequestInfo.getRequestInfo().getReq().getParams().get("_prefix_", System.getProperty("_prefix_", "")));
                else
                    vals = Collections.singletonList((Object) System.getProperty("_prefix_", "") );
            }
            else if (FieldSubstitutor.this.doc != null)
                vals = FieldSubstitutor.this.doc.getFieldValues(key);

            log.trace("substiturion key:{} regex:{} replace:{} def:{} vals:",key,regex,replace,def,vals);

            if (vals==null || vals.size()==0)
                return def;

            String ret = "";

            for( Object val : vals )
            {
                if (val!=null)
                {
                    if (val instanceof java.util.Date)
                        val = FieldUtils.formatDate((java.util.Date) val);

                    if (regex!=null)
                    {
                        if (replace.startsWith("?"))
                            val = val.toString().replaceAll( regex, replace.substring(1) );
                        else if (val.toString().matches(regex))
                            val = val.toString().replaceAll( regex, replace );
                        else
                            val = "";
                    }

                    if (!Strings.isNullOrEmpty(ret))
                        ret += ",";

                    ret += val;
                }
            }

            if (encode && !ret.equals(""))
                ret = FieldUtils.encode(ret, hash, isId);

            if (decode)
                ret = FieldUtils.decode(ret);

            log.trace("ret:{} def:{}",ret,def);

            if (ret.equals(""))
                return def;
            else
                return ret.replaceAll("\\{", ESCAPE_MARK);
        }
    };

    //private final ContentManager cm;

    final SolrInputDocument doc;
    Long sequence;

    /*public FieldSubstitutor(SolrInputDocument doc, long sequence)
    {
        super();

        this.setEscapeChar('\\');
        this.setVariablePrefix("{");
        this.setVariableSuffix("}");
        this.setVariableResolver(lookup);
        this.setEnableSubstitutionInVariables(true);

        //this.doc = cm.doc;
        this.doc = doc;
        //this.cm = cm;
        this.sequence = sequence;
    }*/

    public void setSequence(long sequence)
    {
        this.sequence = sequence;
    }

    public FieldSubstitutor(SolrInputDocument doc)
    {
        super();

        this.setEscapeChar('\\');
        this.setVariablePrefix("{");
        this.setVariableSuffix("}");
        this.setVariableResolver(lookup);
        this.setEnableSubstitutionInVariables(true);

        this.doc = doc;
        //this.cm = null;
        this.sequence = (Long) this.doc.getFieldValue(Schema.Fields.SEQUENCE);
    }

    public FieldSubstitutor(Map<String,Object> docMap)
    {
        super();

        this.setEscapeChar('\\');
        this.setVariablePrefix("{");
        this.setVariableSuffix("}");
        this.setVariableResolver(lookup);
        this.setEnableSubstitutionInVariables(true);

        this.doc = new SolrInputDocument();
        for( String name : docMap.keySet() ) {
            doc.addField( name, docMap.get(name), 1.0f );
        }

        //this.cm = null;
        this.sequence = (Long) this.doc.getFieldValue(Schema.Fields.SEQUENCE);
    }

}
