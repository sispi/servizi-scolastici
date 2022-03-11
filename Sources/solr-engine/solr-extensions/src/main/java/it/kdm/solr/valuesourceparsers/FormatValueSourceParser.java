package it.kdm.solr.valuesourceparsers;

import it.kdm.solr.common.FieldUtils;
import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.lucene.document.Document;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.queries.function.FunctionValues;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.search.FunctionQParser;
import org.apache.solr.search.SyntaxError;
import org.apache.solr.search.ValueSourceParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Created by Paolo_2 on 11/08/15.
 */

public class FormatValueSourceParser extends ValueSourceParser {

    public static final String NAME_WITHOUTEXTENSION = "name-withoutextension";
    public static final String NAME_EXTENSION = "name-extension";
    public static final String NAME_COMPLETE = "name-complete";
    public static final String PARENT_PATH = "parent-path";
    public static final String NAME_PATH = "name-path";
    public static final String NAME = "name";
    private transient static Logger log = LoggerFactory.getLogger(FormatValueSourceParser.class);

    private static final int hcode = FormatFunction.class.hashCode();

    public void init(NamedList namedList) {
    }

    public ValueSource parse(FunctionQParser fp) throws SyntaxError {

        FormatFunction ff = new FormatFunction(fp);
        ff.init();
        return ff;
    }

    class DocLookup extends StrLookup {

        Document ldoc;
        SolrParams params;

        DocLookup( Document ldoc , SolrParams params)
        {
            this.ldoc = ldoc;
            this.params = params;
        }

        @Override
        public String lookup(String s) {

            if (s.equals(NAME_COMPLETE))
            {
                s = NAME;
            }
            else if (s.equals(NAME_WITHOUTEXTENSION) || s.equals(NAME_EXTENSION))
            {
                String name = ldoc.get(NAME);
                if (name != null)
                {
                    int idx = name.lastIndexOf('.');
                    if (s.equals(NAME_WITHOUTEXTENSION))
                        return idx!=-1 ? name.substring(0,idx) : name;
                    else
                        return idx!=-1 ? name.substring(idx) : "";
                }
            }
            else if (s.equals(NAME_PATH))
            {
                String vp = params.get(FieldUtils.VIRTUALPATH,"");

                vp = vp.substring(vp.lastIndexOf("/")+1);

                return vp;
            }
            else if (s.equals(PARENT_PATH))
            {
                String vp = params.get(FieldUtils.VIRTUALPATH,"");

                vp = vp.replaceFirst("/[^/]+$","");

                return vp;
            }

            return ldoc.get(s);
        }
    }

    class FormatFunction extends ValueSource {

        protected final FunctionQParser fp;
        protected final String fval;
        protected final Set<String> fields;

        StrLookup lookup = new StrLookup() {
            @Override
            public String lookup(String s) {

                if (s.equals(NAME_WITHOUTEXTENSION) || s.equals(NAME_EXTENSION) || s.equals(NAME_COMPLETE))
                    fields.add(NAME);
                else
                    fields.add(s);

                return s;
            }
        };

        public FormatFunction(FunctionQParser fp) throws SyntaxError {

            this.fp = fp;
            this.fval = fp.parseArg();
            this.fields = new LinkedHashSet<>();
        }

        void init()
        {
            StrSubstitutor strSubstitutor = new StrSubstitutor(lookup);
            strSubstitutor.replace(fval);


        }

        public FunctionValues getValues(Map context, final LeafReaderContext reader) throws IOException {

            //String fieldname = fval;

            //SchemaField f = fp.getReq().getSchema().getField(fieldname);

            //ValueSource source = f.getType().getValueSource(f, fp);

            //final FunctionValues vals =  source.getValues(context, reader);

            return new FunctionValues()  {

                public float floatVal(int doc) {
                    return Float.parseFloat(strVal(doc));
                }
                public int intVal(int doc) {
                    return (int)floatVal(doc);
                }
                public long longVal(int doc) {
                    return (long)floatVal(doc);
                }
                public double doubleVal(int doc) {
                    return (double)floatVal(doc);
                }
                public String objectVal(int doc) {
                    return strVal(doc);
                }

                public String strVal(int doc)  {

                    try
                    {
                        StrLookup lookup = new DocLookup( reader.reader().document(doc, fields ) , fp.getParams() );

                        StrSubstitutor strSubstitutor = new StrSubstitutor( lookup );

                        return strSubstitutor.replace(fval);
                    }
                    catch( IOException io )
                    {
                        log.warn("doc not found {}",doc);
                        return null;
                    }

                    //return vals.strVal(doc);
                }



                public String toString(int doc) {
                    return description();
                }
            };
        }

        @Override
        public String description() {
            return "format(" + fval + ')';
        }

        @Override
        public boolean equals(Object o) {
            return o != null && o.getClass() == FormatFunction.class && this.fval.equals(((FormatFunction)o).fval);
        }


        @Override
        public int hashCode() {
            return hcode + fval.hashCode();
        }

    // boilerplate methods omitted
    }
}