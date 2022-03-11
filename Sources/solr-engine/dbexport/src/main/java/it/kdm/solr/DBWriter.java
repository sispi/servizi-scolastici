package it.kdm.solr;

import com.google.common.base.Strings;
import it.kdm.solr.common.FieldUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.ddlutils.PlatformFactory;
import org.apache.ddlutils.io.DatabaseIO;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.PlatformImplBase;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.cloud.SolrZkClient;
import org.apache.solr.handler.dataimport.*;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.update.processor.UpdateRequestProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class DBWriter extends DIHWriterBase implements DIHWriter {

    private static final Logger log = LoggerFactory.getLogger(DBWriter.class);

    //private final UpdateRequestProcessor processor;

    SolrQueryRequest req;
    Connection conn;
    PlatformImplBase platform;
    Database dataBase;
    //Statement statement;
    //JdbcDataSource dataSource;
    //String docTable;

    int counter = 0;
    int chunk;

    public DBWriter(UpdateRequestProcessor processor, SolrQueryRequest req) {
        //this.processor = processor;
        this.req = req;
    }

    @Override
    public void commit(boolean optimize) {
        log.info("commit optimize:{}",optimize);

        try {
            //statement.executeBatch();
            conn.commit();
            //statement.close();
            //statement = conn.createStatement();
        } catch (Exception e) {
            throw new DataImportHandlerException(DataImportHandlerException.SEVERE,
                    "Unable to commit transaction", e);
        }
    }

    @Override
    public void close() {

        log.info("close");

        try {
            conn.close();
        } catch (Exception e) {
            throw new DataImportHandlerException(DataImportHandlerException.SEVERE,
                    "Unable to close connection", e);
        } finally {
            deltaKeys = null;

            /*try {
                if (statement!=null)
                    statement.close();
            } catch (Exception e) {
                SolrException.log(log, e);
            }*/

            /*try {
                if (dataSource!=null)
                    dataSource.close();
            } catch (Exception e) {
                SolrException.log(log, e);
            }*/
        }
    }

    @Override
    public void rollback() {
        log.info("rollback");

        try {
            conn.rollback();
            //statement.close();
        } catch (Exception e) {
            throw new DataImportHandlerException(DataImportHandlerException.SEVERE,
                    "Unable to rollback transaction", e);
        }
    }

    @Override
    public void deleteByQuery(String sql) {
        //chiamata da post/preImportDeleteQuery e da $deleteByQuery in row
        //deve essere SQL

        log.info("deleteByQuery sql={}",sql);

        try {
            Statement statement = conn.createStatement();
            statement.execute(sql);
            //statement.addBatch(sql);
        } catch (SQLException e) {
            throw new DataImportHandlerException(DataImportHandlerException.SEVERE,
                    "Unable to delete by query:"+sql);
        }
    }

    @Override
    public void doDeleteAll() {
        //chiamato in caso di clean

        log.info("doDeleteAll");

        Table[] tables = dataBase.getTables();

        try {

            Statement statement = conn.createStatement();

            for( int i=0; i<tables.length; i++){
                    String sql = "truncate "+tables[i].getName();
                    statement.addBatch(sql);
            }

            statement.executeBatch();

        } catch (SQLException e) {
            throw new DataImportHandlerException(DataImportHandlerException.SEVERE,
                    "Unable to truncate table",e);
        }
    }

    @Override
    public void deleteDoc(Object key) {
        log.info("deleteDoc key:{}",key);
        //chiamato in caso di $deleteById in row (ma non c'è mai deltaQuery)
        //può essere l'id oppure "table:<id>"

        String id = key.toString();
        String table = "document";

        if (id.contains(":")){
            table = id.split(":")[0];
            id = id.split(":")[1];
        }

        try {
            Statement statement = conn.createStatement();
            deleteDoc(statement, table, id);
            statement.executeBatch();
        } catch (SQLException e) {
            throw new DataImportHandlerException(DataImportHandlerException.SEVERE,
                    "Unable to delete document:" + id,e);
        }
    }

    private void deleteDoc(Statement statement, String table, String id) {
        log.info("internal deleteDoc id:{} table:{}",id,table);

        if (dataBase.findTable(table,true)==null)
            table = "document";

        String quote = platform.getPlatformInfo().getValueQuoteToken();

        String sql1 = String.format("delete from "+ table +" where id = %s%s%s " ,
                    quote, id, quote );

        String sql2 = String.format("delete from fields where id = %s%s%s " ,
                quote, id, quote );

        try {
            statement.addBatch(sql1);
            statement.addBatch(sql2);
        } catch (SQLException e) {
            throw new DataImportHandlerException(DataImportHandlerException.SEVERE,
                    "Unable to delete:"+id,e);
        }
    }

    private String escape(String value){
        //return value;
        return value.replaceAll("'","''");
    }


    @Override
    public boolean upload(SolrInputDocument doc) {
        log.info("Document:{}",doc);

        String id = (String) doc.getFieldValue("id");
        String table = (String) doc.getFieldValue("[table]");

        if (Strings.isNullOrEmpty(table))
            table = "document";

        Table document = dataBase.findTable(table,true);
        Table fields = dataBase.findTable("fields",true);

        if (document==null)
            document = dataBase.findTable("document",true);

        if (document==null)
            throw new DataImportHandlerException(DataImportHandlerException.SEVERE,
                    "Invalid db schema. Missing table 'document'");

        if (fields==null)
            throw new DataImportHandlerException(DataImportHandlerException.SEVERE,
                    "Invalid db schema. Missing table 'fields'");

        doc.setField("[table]",document.getName());

        DynaBean docBean = dataBase.createDynaBeanFor(document.getName(), false);

        List<DynaBean> fieldBeans = new ArrayList<>();

        for( String field : doc.getFieldNames()){

            Column col = document.findColumn(field,true);
            Collection<Object> values = doc.getFieldValues(field);

            if (col==null){

                for ( Object value : values ){
                    DynaBean fieldBean = dataBase.createDynaBeanFor(fields.getName(), false);
                    fieldBean.set("id", id);
                    fieldBean.set("name" , field);

                    if (value instanceof Date)
                        value = FieldUtils.formatDate( (Date) value);

                    fieldBean.set("value" , escape(value.toString()));

                    fieldBeans.add(fieldBean);
                }

            } else {
                Object v = doc.getFieldValue(field);
                if (v instanceof Date)
                    docBean.set(field,FieldUtils.formatDate( (Date) v));
                else if (v instanceof String)
                    docBean.set(field,escape((String)v));
                else
                    docBean.set(field,v);
            }
        }


        String sql = null;
        try {

            Statement statement = conn.createStatement();

            deleteDoc(statement,table,id);

            sql = platform.getInsertSql(dataBase,docBean);
            statement.addBatch(sql);

            for( DynaBean fieldBean : fieldBeans)
            {
                sql = platform.getInsertSql(dataBase,fieldBean);
                statement.addBatch(sql);
            }

            statement.executeBatch();
        } catch (SQLException e) {
            log.error("unable to add document:{}",id,e);
            return false;
        }

        counter++;

        if (counter>=chunk){
            counter = 0;

            try {
                conn.commit();
            } catch (SQLException e) {
                throw new DataImportHandlerException(DataImportHandlerException.SEVERE,
                        "unable to commit chunk",e);
            }
        }

        return true;
    }

    private Database updateDatabase(Context context) throws Exception{

        String catalog = conn.getCatalog();

        SolrZkClient zkClient = context.getSolrCore().getCoreContainer().getZkController().getZkClient();

        String collection = context.getSolrCore().getCoreDescriptor().getCollectionName();

        String path = "/configs/" + collection + "/ddl/" + catalog + ".xml";
        StringReader reader;
        try {
            byte[] data = zkClient.getData(path, null, null, true);
            if (data != null) {
                reader = new StringReader(new String(data, StandardCharsets.UTF_8));
            } else {
                throw new DataImportHandlerException(DataImportHandlerException.SEVERE,
                        "Unable to find ddl config from zk");
            }
        } catch ( Exception e ){
            throw new DataImportHandlerException(DataImportHandlerException.SEVERE,
                    "Unable to read ddl config from zk", e);
        }

        DatabaseIO dbIO = new DatabaseIO();
        Database db = dbIO.read(reader);
        platform.alterModel(conn,dataBase,db,false);
        log.warn("db schema updated succesfully");
        return db;
    }

    @Override
    public void init(Context context) {
        log.info("Init {}",context);

        DocBuilder builder = DBWriterUtils.getBuilder(context);

        context = new ContextImpl(null, new VariableResolver(), null, null, context.getRequestParameters(), null, builder);

        String datasource = req.getParams().get("datasource");
        chunk = req.getParams().getInt("chunk",1000);

        try {
            //this.dataSource = DBWriterUtils.getDataSource(datasource,context);

            this.conn = DBWriterUtils.getConnection(datasource,context);

            this.platform = (PlatformImplBase) PlatformFactory.createNewPlatformInstance(conn.getMetaData().getDriverName(), conn.getMetaData().getURL());
            this.dataBase = platform.getModelReader().getDatabase(this.conn, null);

            if (req.getParams().getBool("updateDDL",false)){
                this.dataBase = updateDatabase(context);
            }

            //this.statement = conn.createStatement();
        } catch (Exception e) {
            throw new DataImportHandlerException(DataImportHandlerException.SEVERE,
                    "Unable to create sql helper", e);
        }
    }
}
