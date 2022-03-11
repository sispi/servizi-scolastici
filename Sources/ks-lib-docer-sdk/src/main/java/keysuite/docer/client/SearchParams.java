package keysuite.docer.client;

import keysuite.docer.query.ISearchParams;

import java.util.LinkedHashMap;

public class SearchParams extends LinkedHashMap<String,String[]> implements ISearchParams {

    protected static String term( Object arg )
    {
        if ("".equals(arg))
            return "\"\"";
        else
            return arg.toString().replaceAll("([\\(\\)\\s\\+\\-\\&\\!\\{\\}\\[\\]\\^\\~\\?\\:\\\\\\/])", "\\\\$1");
    }

    public static String clause( String field, Object... args )
    {
        String clause;

        if (args == null || args.length==0 || args[0]==null)
            clause = String.format( " +( *:* -%s:* ) " , field );
        else if (args.length==1)
            clause = String.format( " +%s:%s " , field, term( args[0] ) );
        else
        {
            String clauses = "";
            for( Object arg : args )
                clauses += String.format( " %s " , term(arg) );

            clause = String.format( " +%s:(%s) " , field, clauses );
        }
        return clause;
    }

    @Override
    public String[] get(String param) {
        return super.get(param);
    }

    @Override
    public String getFirst(String param) {
        String[] values = super.get(param);
        if (values==null || values.length==0)
            return null;
        return values[0];
    }

    public SearchParams addFq(String param, Object... values) {
        this.add("fq",clause(param,values));
        return this;
    }

    public SearchParams setQ(String param, Object... values) {
        this.set("q",clause(param,values));
        return this;
    }

    public SearchParams addFulltext(String fulltext) {
        this.add("fq",fulltext);
        return this;
    }

    @Override
    public SearchParams add(String param, String... values){
        String[] old = this.get(param);
        if( old != null && old.length>0 ) {
            if (values == null || values.length == 0) {
                String[] both = new String[old.length + 1];
                System.arraycopy(old, 0, both, 0, old.length);
                both[old.length] = null;
                this.set(param, both);
            } else {
                String[] both = new String[old.length + values.length];
                System.arraycopy(old, 0, both, 0, old.length);
                System.arraycopy(values, 0, both, old.length, values.length);
                this.set(param, both);
            }
        } else {
            this.set(param, values);
        }
        return this;
    }

    @Override
    public SearchParams set(String param, String... values) {
        super.put(param,values);
        return this;
    }

    @Override
    public SearchParams set(String param, int value) {
        return this.set(param,String.valueOf(value));
    }

    @Override
    public SearchParams set(String param, boolean value) {
        return this.set(param,String.valueOf(value));
    }

    public SearchParams rows(Integer rows){
        return this.set("rows",rows);
    }

    public SearchParams sort(String... sorts){
        return this.set("sort",sorts);
    }

    public SearchParams start(Integer start){
        return this.set("start",start);
    }



}
