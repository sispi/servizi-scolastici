package org.apache.solr.handler.dataimport;

import java.sql.Connection;

public class DBWriterUtils {

    private static JdbcDataSource getDataSource(String name, Context ctx) throws Exception{
        DataImporter imp = getImporter(ctx);

        JdbcDataSource ds = (JdbcDataSource) imp.getDataSourceInstance(null,name,ctx);
        return ds;
    }

    public static Connection getConnection(String name, Context ctx) throws Exception{
        JdbcDataSource ds = getDataSource(name,ctx);
        Connection c = ds.factory.call();
        ds.close();
        return c;
    }

    public static DocBuilder getBuilder(Context context){
        ContextImpl impl = (ContextImpl) context;

        return impl.getDocBuilder();
    }

    public static DataImporter getImporter(Context context){
        ContextImpl impl = (ContextImpl) context;

        return getBuilder(context).dataImporter;
    }
}
