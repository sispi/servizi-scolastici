package keysuite.docer.utils;

import it.kdm.doctoolkit.zookeeper.ApplicationProperties;
import it.kdm.orchestratore.beans.GenericResultSet;
import java.io.File;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteJDBCLoader;

public class DBManager {

    public enum DbType{
        mysql, oracle, mssql, sqlite;
    }

    Logger log = LoggerFactory.getLogger(DBManager.class);

    public static final String PROP_DB_JNDI_DATASOURCE="db.jndi";
    public static final String PROP_DB_TYPE="db.type";

    public static final String SQLITE = "org.sqlite.JDBC";

    private static final String SQLITE_CONNECTION_STRING = "jdbc:sqlite:%s";

    public static final String DB_FILEPATH="filepath";


    private static HashMap<String, DBManager> databases = new HashMap<String, DBManager>();

    private Connection connection;

    private HashMap<String, String> property = null;
    private String className = null;
    private String jndi=null;
    public static DBManager getInstance(String type, HashMap<String, String> property)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        if(databases.get(type) == null){
            databases.put(type, new DBManager(type, property));
        }
        if(databases.get(type) != null && ! databases.get(type).isSameProperties(property)){
            databases.put(type, new DBManager(type, property));
        }
        return databases.get(type);
    }

    public static DBManager getInstance(HashMap<String, String> property)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        String jndi = property.get(PROP_DB_JNDI_DATASOURCE);
        if(databases.get(jndi) == null){
            databases.put(jndi, new DBManager(property));
        }
        if(databases.get(jndi) != null && ! databases.get(jndi).isSameProperties(property)){
            databases.put(jndi, new DBManager(property));
        }
        return databases.get(jndi);
    }

    private void setProperty(HashMap<String, String> property){
        this.property = property;
    }
    private void setClassName(String className){
        this.className = className;
    }

    private boolean isSameProperties(HashMap<String, String> property){
        boolean result = true;
        if(this.property.keySet().size() != property.keySet().size()) {
            result = false;
        }else{
            for(String key: this.property.keySet()){
                if(!this.property.get(key).equals(property.get(key))){
                    result = false;
                    break;
                }
            }
        }
        return result;

    }


    private DBManager(String type, HashMap<String, String> property)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        super();
        setProperty(property);
        if(type.equals(DbType.sqlite.toString()))
            setClassName(SQLITE);
        connect();
    }
    private DBManager(HashMap<String, String> property)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        super();
        setProperty(property);
        jndi = property.get(PROP_DB_JNDI_DATASOURCE);
        connect(jndi);
    }

    private Connection getConnection(String jndi) {
        Connection connection = null;
        try {
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup(jndi);
            connection = dataSource.getConnection();
        } catch (NamingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return connection;
    }

    private void connect()
            throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        try {
            Class.forName(className).newInstance();
        } catch (InstantiationException e) {
            throw e;
        } catch (IllegalAccessException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            throw e;
        }

        //Verifico se è attiva la connessione ed eventualmente la chiudo
        if(connection != null && !connection.isClosed()){
            closeConnection();
        }

        if(connection == null || (connection != null && connection.isClosed())) {
            try {
                if (className.equals(SQLITE)) {
                    String filePath = property.get(DB_FILEPATH) != null ? property.get(DB_FILEPATH) : "default.db";
                    if (new File(filePath).exists()) {
                        String connectionString = String.format(SQLITE_CONNECTION_STRING, filePath);

                        try{
                            String dbFolder = ApplicationProperties.get("db.folder");
                            System.setProperty("org.sqlite.tmpdir", dbFolder);
                            SQLiteJDBCLoader.initialize();
                        }catch(Exception exf){
                            exf.printStackTrace();
                                }
                        connection =
                                DriverManager.getConnection(connectionString);
                    }
                }


            } catch (SQLException ex) {
                log.error("SQLException: " + ex.getMessage());
                log.error("SQLState: " + ex.getSQLState());
                log.error("VendorError: " + ex.getErrorCode());
                throw ex;
            }
        }
    }

    private void connect(String jndi)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        connection = getConnection(jndi);
        //Verifico se è attiva la connessione ed eventualmente la chiudo
        property.put(PROP_DB_TYPE,connection.getMetaData().getDatabaseProductName().toLowerCase());
    }


    private void closeConnection()throws SQLException {
        try {
            connection.close();
            connection = null;
        } catch (SQLException e) {
            log.error("SQLException: " + e.getMessage());
            log.error("SQLState: " + e.getSQLState());
            log.error("VendorError: " + e.getErrorCode());
            throw e;
        }
    }


    public void forceReconnect(String className, HashMap<String, String> property)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        connect();
    }
    public GenericResultSet executeSelect(String query, List<Object> param) throws SQLException {
        return executeSelect(query, param, -1);
    }

    public GenericResultSet executeSelect(String query, List<Object> param, int maxRow) throws SQLException {


        GenericResultSet resultSet;

        PreparedStatement prepStmt = null;
        ResultSet rs = null;

        try{
            if(connection == null || (connection != null && connection.isClosed())){
                if(className != null){
                    connect();
                }else{
                    connect(jndi);
                }
            }
               // connect();
        }catch (Exception e){

            e.printStackTrace();
        }

        try {

            prepStmt = connection.prepareStatement(query);
            if(maxRow>-1)
                prepStmt.setMaxRows(maxRow);

            if (param != null) {
                int i = 1;
                for (Object o : param) {
                    prepStmt.setObject(i, o);
                    i++;
                }
            }


            if (prepStmt.execute()) {
                rs = prepStmt.getResultSet();
            }

            ResultSetMetaData rsmd = rs.getMetaData();
            List<String> colNames = new ArrayList<>();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                colNames.add(rsmd.getColumnName(i));
            }

            resultSet = new GenericResultSet();
            resultSet.setColumnNames(colNames.toArray(new String[colNames.size()]));

            List<Object[]> valori = new ArrayList<Object[]>();

            while (rs.next()) {
                Object[] record = new Object[rsmd.getColumnCount()];
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    Object val = rs.getObject(i);
                    val = val != null ? val : null;
                    record[i - 1] = val;
                }
                valori.add(record);
            }
            resultSet.setValori(valori);
        } catch (SQLException ex) {
            log.error("SQLException: " + ex.getMessage());
            log.error("SQLState: " + ex.getSQLState());
            log.error("VendorError: " + ex.getErrorCode());
            throw ex;
        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore

                rs = null;
            }

            if (prepStmt != null) {
                try {
                    prepStmt.close();
                } catch (SQLException sqlEx) {
                } // ignore

                prepStmt = null;
            }
            closeConnection();
        }

        return resultSet;
    }

    public Boolean executeUpdate(String query, List<Object> param) throws SQLException {

        try{
            if(connection == null || (connection != null && connection.isClosed())){
                if(className != null){
                    connect();
                }else{
                    connect(jndi);
                }
            }
            // connect();
        }catch (Exception e){

            e.printStackTrace();
        }

        PreparedStatement prepStmt = null;
        Boolean updated = false;
        try {

            prepStmt = connection.prepareStatement(query);

            if (param != null) {
                int i = 1;
                for (Object o : param) {
                    if(o instanceof Date){
                        Date dt = (Date) o;
                        java.sql.Date dtToSet = new java.sql.Date(((Date) o).getTime());
                        prepStmt.setDate(i,dtToSet);
                    }else{
                        prepStmt.setObject(i, o);
                    }

                    i++;
                }
            }


            if (prepStmt.execute()) {
                updated = true;
            }


        } catch (SQLException ex) {
            log.error("SQLException: " + ex.getMessage());
            log.error("SQLState: " + ex.getSQLState());
            log.error("VendorError: " + ex.getErrorCode());
            throw ex;
        } finally {

            if (prepStmt != null) {
                try {
                    prepStmt.close();

                } catch (SQLException sqlEx) {
                } // ignore

                prepStmt = null;
            }
            closeConnection();
        }
        return updated;
    }

    public Boolean executeUpdateRaw(String query, List<Object> param) throws SQLException {

        try{
            if(connection == null || (connection != null && connection.isClosed())){
                if(className != null){
                    connect();
                }else{
                    connect(jndi);
                }
            }
            // connect();
        }catch (Exception e){

            e.printStackTrace();
        }

        PreparedStatement prepStmt = null;
        Boolean updated = false;
        try {

            if(param != null){
                for(Object o: param){
                    query = query.replaceFirst("\\?", o.toString());
                }
            }

            prepStmt = connection.prepareStatement(query);

//            if (param != null) {
//                int i = 1;
//                for (Object o : param) {
//                    prepStmt.setObject(i, o);
//                    i++;
//                }
//            }


            if (prepStmt.execute()) {
                updated = true;
            }


        } catch (SQLException ex) {
            log.error("SQLException: " + ex.getMessage());
            log.error("SQLState: " + ex.getSQLState());
            log.error("VendorError: " + ex.getErrorCode());
            throw ex;
        } finally {

            if (prepStmt != null) {
                try {
                    prepStmt.close();

                } catch (SQLException sqlEx) {
                } // ignore

                prepStmt = null;
            }
            closeConnection();
        }
        return updated;
    }


    public String getDbType(){
        if(property != null){
            return property.get(PROP_DB_TYPE);
        }

        return "default";
    }
    public String getNativeQuery(String queryName, String orderBy) throws Exception {
        String query="";
        InputStream schemaIS = DBManager.class.getClassLoader().getResourceAsStream("META-INF/NativeQueries.xml");
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder =  builderFactory.newDocumentBuilder();
        org.w3c.dom.Document xmlDocument = builder.parse(schemaIS);
        javax.xml.xpath.XPath xPath =  XPathFactory.newInstance().newXPath();
        query = xPath.compile("/queries/named-native-query[@name='"+queryName+"']/query").evaluate(xmlDocument);
        if(orderBy!=null && !orderBy.equals("")){
            if(query!= null && !query.equals("")){
                query = query + " order by "+ orderBy;
            }
        }
        return query;

    }

}
