/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.service;

import it.filippetti.ks.api.bpm.configuration.DatastoreConfiguration;
import it.filippetti.ks.api.bpm.exception.DatastoreException;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.ddlutils.DatabaseOperationException;
import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.PlatformFactory;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.platform.PlatformImplBase;
import org.apache.ddlutils.platform.mysql.MySqlPlatform;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.OrderField;
import org.jooq.SQLDialect;
import org.jooq.SelectFieldOrAsterisk;
import org.jooq.conf.ParamType;
import org.jooq.impl.DSL;
import static org.jooq.impl.DSL.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 *
 * @author marco.mazzocchetti
 */
@Service
public class DatastoreService {

    private static final Logger log = LoggerFactory.getLogger(DatastoreService.class);

    public static final String METADATA_KEY_PREFIX = "@";
    
    @Autowired
    private DataSource localDataSource;

    @Autowired
    private PlatformTransactionManager localTransactionManager;

    @Autowired
    private DatastoreConfiguration datastoreConfiguration;

    private Map<String, Datastore> datastores;

    public DatastoreService() {
        datastores = new HashMap<>();
    }

    @PostConstruct
    public void init() {
        try {
            for (DatastoreConfiguration.DataSource configuration : datastoreConfiguration.datasources()) {
                datastores.put(configuration.name(), new Datastore(configuration));
            }
        } catch (DatastoreException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public Datastore getDatastore() throws DatastoreException {
        return DatastoreService.this.getDatastore(DatastoreConfiguration.LOCAL_DATASOURCE_NAME);
    }

    public Datastore getDatastore(String dataSourceName) throws DatastoreException {
        
        if (!datastores.containsKey(dataSourceName)) {
            throw new DatastoreException(String.format(
                "Datasource not available: %s", 
                dataSourceName));
        } else {
            return datastores.get(dataSourceName);
        }
    }

    /**
     *
     */
    public class Datastore {

        private DatastoreConfiguration.DataSource dataSourceConfiguration;
        private DataSource dataSource;
        private PlatformTransactionManager transactionManager;
        private JdbcTemplate jdbcTemplate;
        private PlatformImplBase platform;
        private Database database;
        private DSLContext sqlBuilder;

        private Datastore(DatastoreConfiguration.DataSource dataSourceConfiguration) throws DatastoreException {

            this.dataSourceConfiguration = dataSourceConfiguration;

            if (dataSourceConfiguration.isLocal()) {
                this.dataSource = localDataSource;
                this.transactionManager = localTransactionManager;
            } else {
                this.dataSource = DataSourceBuilder
                        .create()
                        .type(org.apache.commons.dbcp2.BasicDataSource.class)
                        .url(dataSourceConfiguration.url())
                        .username(dataSourceConfiguration.username())
                        .password(dataSourceConfiguration.password())
                        .driverClassName(dataSourceConfiguration.driverClassName())
                        .build();
                Stream
                        .concat(datastoreConfiguration.connection().entrySet().stream(),
                                dataSourceConfiguration.connection().entrySet().stream())
                        .forEach(p -> {
                            try {
                                BeanUtils.setProperty(this.dataSource, p.getKey(), p.getValue());
                            } catch (IllegalAccessException | InvocationTargetException e) {
                            }
                        });

                this.transactionManager = new DataSourceTransactionManager(dataSource);
            }

            this.jdbcTemplate = new JdbcTemplate(dataSource);

            try {
                this.platform = (PlatformImplBase) (dataSourceConfiguration.platform() != null
                        ? PlatformFactory.createNewPlatformInstance(dataSourceConfiguration.platform())
                        : PlatformFactory.createNewPlatformInstance(dataSource));
            } catch (RuntimeException e) {
                this.platform = null;
            }
            if (platform != null) {
                database = loadDatabaseModel();
            } else {
                if (dataSourceConfiguration.platform() != null) {
                    throw new DatastoreException(String.format(
                            "Datasource '%s' platform configuration error: '%s' not supported",
                            dataSourceConfiguration.name(),
                            dataSourceConfiguration.platform()));
                } else {
                    throw new DatastoreException(String.format(
                            "Datasource '%s' platform configuration error: autodetect failed",
                            dataSourceConfiguration.name(),
                            dataSourceConfiguration.platform()));
                }
            }
            
            this.sqlBuilder = DSL.using(SQLDialect.DEFAULT);
        }

        /**
         * 
         * @return
         * @throws DatastoreException 
         */
        public Database loadDatabaseModel() throws DatastoreException {
            synchronized (platform) {
                try {
                    return platform
                        .getModelReader()
                        .getDatabase(
                            DataSourceUtils.getConnection(dataSource),
                            platform instanceof MySqlPlatform ? 
                                null : 
                                dataSourceConfiguration.name(),
                            dataSourceConfiguration.catalog(),
                            dataSourceConfiguration.schema(),
                            null);
                } catch (CannotGetJdbcConnectionException | SQLException | DdlUtilsException e) {
                    throw new DatastoreException(String.format(
                        "Datasource '%s' database model reader error: %s",
                        dataSourceConfiguration.name(), 
                        e.getMessage()), e);
                }
            }
        }

        /**
         * 
         * @return
         * @throws SQLException 
         */
        public Database getDatabaseModel() {

            synchronized (platform) {
                return database;
            }
        }

        /**
         *
         * @param desired
         */
        public void alterDatabaseModel(Database desired) throws DatastoreException {

            synchronized (platform) {
                try {
                    platform.alterModel(
                        DataSourceUtils.getConnection(dataSource),
                        database,
                        desired,
                        false);
                    loadDatabaseModel();
                } catch (CannotGetJdbcConnectionException | DdlUtilsException e) {
                    throw new DatastoreException(String.format(
                        "Datasource '%s' database model alter error: %s",
                        e.getMessage()), e);
                }
            }
        }

        /**
         * 
         * @param sql
         * @return
         * @throws DatastoreException 
         */
        public int executeUpdate(String sql) throws DatastoreException {

            int affected;
            
            if (sql == null) {
                throw new IllegalArgumentException("Null sql");
            }
            
            TransactionStatus tx = transactionManager.getTransaction(
                new DefaultTransactionDefinition());
            try {
                try {
                    affected = jdbcTemplate.update(sql);
                } catch (DataAccessException e) {
                    throw new DatastoreException(e.getMessage(), e.getCause());
                }       
            } catch (Throwable t) {
                transactionManager.rollback(tx);
                throw t;
            }
            transactionManager.commit(tx);
            return affected;
        }

        /**
         * 
         * @param sql
         * @param maxRows
         * @return
         * @throws SQLException 
         */
        public List<Map<String,Object>> executeQuery(String sql, Integer maxRows) 
            throws DatastoreException {
            
            List<Map<String,Object>> rows;

            if (sql == null) {
                throw new IllegalArgumentException("Null sql");
            }
            
            TransactionStatus tx = transactionManager.getTransaction(
                new DefaultTransactionDefinition());
            try {
                try {
                    rows = jdbcTemplate.query(
                        sql, 
                        (PreparedStatement statement) -> {
                            if (maxRows != null) {
                                statement.setMaxRows(maxRows); 
                            }}, 
                        (ResultSet resultSet) -> extractRows(resultSet));
                } catch (DataAccessException e) {
                    throw new DatastoreException(e.getMessage(), e);
                }            
            } catch (Throwable t) {
                transactionManager.rollback(tx);
                throw t;
            }
            transactionManager.commit(tx);
            return rows;
        }

        /**
         * 
         * @param table
         * @param criteria
         * @return
         * @throws DatastoreException 
         */
        public Map<String,Object> load(String table, Map<String, Object> criteria) 
            throws DatastoreException {
            
            return load(table, criteria, null);
        }
        
        /**
         * 
         * @param table
         * @param criteria
         * @param columns
         * @return
         * @throws DatastoreException 
         */
        public Map<String,Object> load(String table, Map<String, Object> criteria, String[] columns) 
            throws DatastoreException {
            
            List<Map<String, Object>> rows;
            
            if (table == null) {
                throw new IllegalArgumentException("Null table");
            }
            
            // retain row keys only (without modify input)
            criteria = new HashMap<>(criteria);
            criteria.keySet().retainAll(List.of(database.findTable(table).getPrimaryKeyColumnNames()));
            
            rows = executeQuery(buildQuery(table, criteria, columns, null, false), 2);
            if (rows.isEmpty()) {
                return null;
            } else if (rows.size() == 1) {
                return rows.get(0);
            } else {
                throw new DatastoreException("Non unique row");
            }
        }
        
        /**
         * 
         * @param table
         * @param criteria
         * @param columns
         * @param orderBy
         * @param maxRows
         * @return
         * @throws DatastoreException 
         */
        public List<Map<String,Object>> select(String table, Map<String,Object> criteria, String[] columns, String orderBy, Integer maxRows) 
            throws DatastoreException{
            
            if (table == null) {
                throw new IllegalArgumentException("Null table");
            }
            
            return executeQuery(buildQuery(table, criteria, columns, orderBy, true), maxRows);
        }
        
        /**
         * 
         * @param table
         * @param row
         * @return
         * @throws DatastoreException 
         */
        public Map<String, Object> insert(String table, Map<String, Object> row) throws DatastoreException {

            DynaBean bean;

            if (table == null) {
                throw new IllegalArgumentException("Null table");
            }
            if (row == null) {
                throw new IllegalArgumentException("Null row");
            }
            
            TransactionStatus tx = transactionManager.getTransaction(
                new DefaultTransactionDefinition());
            try {
                bean = toBean(table, row);
                try {
                    platform.insert(DataSourceUtils.getConnection(dataSource), getDatabaseModel(), bean);
                    if (platform.lastAfftected != 1)
                        throw new DatastoreException("Invalid number of affected rows");
                } catch(CannotGetJdbcConnectionException | DatabaseOperationException e) {
                    throw new DatastoreException(e.getMessage(), e);
                }
                row = toRow(bean);
            } catch (Throwable t) {
                transactionManager.rollback(tx);
                throw t;
            }
            transactionManager.commit(tx);
            return row;
        }
        
        /**
         * 
         * @param table
         * @param row
         * @return
         * @throws DatastoreException 
         */
        public Map<String,Object> store(String table, Map<String,Object> row) throws DatastoreException {

            Map<String, Object> currentRow;
            DynaBean oldBean, newBean;

            if (table == null) {
                throw new IllegalArgumentException("Null table");
            }
            if (row == null) {
                throw new IllegalArgumentException("Null row");
            }
            
            TransactionStatus tx = transactionManager.getTransaction(
                new DefaultTransactionDefinition());
            try {
                currentRow = load(table, row);
                if (currentRow == null) {
                    return insert(table, row);
                }
                oldBean = toBean(table, currentRow);
                currentRow.putAll(row);
                newBean = toBean(table, currentRow);
                try {
                    platform.update(DataSourceUtils.getConnection(dataSource), getDatabaseModel(), oldBean, newBean);
                    if (platform.lastAfftected != 1)
                        throw new DatastoreException("Invalid number of affected rows");
                } catch(CannotGetJdbcConnectionException | DatabaseOperationException e) {
                    throw new DatastoreException(e.getMessage(), e);
                }
                currentRow = toRow(newBean);
            } catch (Throwable t) {
                transactionManager.rollback(tx);
                throw t;
            }
            transactionManager.commit(tx);
            return currentRow;
        }

        /**
         * 
         * @param table
         * @param row
         * @return
         * @throws DatastoreException 
         */
        public Map<String,Object> update(String table, Map<String,Object> row) throws DatastoreException {

            Map<String,Object> currentRow;
            DynaBean oldBean, newBean;
            
            if (table == null) {
                throw new IllegalArgumentException("Null table");
            }
            if (row == null) {
                throw new IllegalArgumentException("Null row");
            }
            
            TransactionStatus tx = transactionManager.getTransaction(
                new DefaultTransactionDefinition());
            try {
                currentRow = load(table, row);
                if (currentRow != null) {
                    oldBean = toBean(table, currentRow);
                    currentRow.putAll(row);
                    newBean = toBean(table, currentRow);
                    try {
                        platform.update(DataSourceUtils.getConnection(dataSource), getDatabaseModel(), oldBean, newBean);
                        if (platform.lastAfftected != 1) {
                            throw new DatastoreException("Invalid number of affected rows");
                        }
                    } catch(CannotGetJdbcConnectionException | DatabaseOperationException e) {
                        throw new DatastoreException(e.getMessage(), e);
                    }
                    currentRow = toRow(newBean);
                }
            
            } catch (Throwable t) {
                transactionManager.rollback(tx);
                throw t;
            }
            transactionManager.commit(tx);
            return currentRow;
        }

        /**
         * 
         * @param table
         * @param row
         * @throws DatastoreException 
         */
        public void delete(String table, Map<String, Object> row) throws DatastoreException {
            
            DynaBean bean;

            if (table == null) {
                throw new IllegalArgumentException("Null table");
            }
            if (row == null) {
                throw new IllegalArgumentException("Null row");
            }
            
            TransactionStatus tx = transactionManager.getTransaction(
                new DefaultTransactionDefinition());
            try {
                bean = toBean(table, row);
                try {
                    platform.delete(DataSourceUtils.getConnection(dataSource), getDatabaseModel(), bean);
                    if (platform.lastAfftected != 1) {
                        throw new DatastoreException("Invalid number of affected rows");
                    }
                } catch(CannotGetJdbcConnectionException | DatabaseOperationException e) {
                    throw new DatastoreException(e.getMessage(), e);
                }
            } catch (Throwable t) {
                transactionManager.rollback(tx);
                throw t;
            }
            transactionManager.commit(tx);
        }
        
        /**
         * 
         * @param table
         * @param criteria
         * @param columns
         * @param orderBy
         * @param parseCriteria
         * @return 
         */
        public String buildQuery(String table, Map<String, Object> criteria, String[] columns, String orderBy, boolean parseCriteria) {
            
            if (table == null) {
                throw new IllegalArgumentException("Null table");
            }
            
            return sqlBuilder
                .select(generateSelectFields(columns))
                .from(table(delimited(table)))
                .where(generateWhereConditions(criteria, parseCriteria))
                .orderBy(generateOrderByFields(orderBy))
                .getSQL(ParamType.INLINED);   
        }
 
        /**
         * 
         * @param table
         * @param where
         * @param columns
         * @param orderBy
         * @return 
         */
        public String buildQuery(String table, String where, String[] columns, String orderBy) {
            
            if (table == null) {
                throw new IllegalArgumentException("Null table");
            }

            return sqlBuilder
                .select(generateSelectFields(columns))
                .from(table(delimited(table)))
                .where(generateWhere(where))
                .orderBy(generateOrderByFields(orderBy))
                .getSQL(ParamType.INLINED);   
        }
        
        /**
         * 
         * @param resultSet
         * @return
         * @throws SQLException 
         */
        private List<Map<String, Object>> extractRows(ResultSet resultSet) throws SQLException {
            
            ResultSetMetaData metadata;
            Integer columns;
            List<Map<String, Object>> rows;
            Map<String, Object> row;
                    
            metadata = resultSet.getMetaData();
            columns = metadata.getColumnCount();
            rows = new ArrayList<>();
            while (resultSet.next()){
                row = new HashMap<>(columns); 
                for(int i = 1; i <= columns; ++i){
                    row.put(
                        metadata.getColumnName(i),
                        resultSet.getObject(i));
                }
                rows.add(row);
            }
            return rows;
        }
        
        /**
         * 
         * @param table
         * @param row
         * @return 
         */
        private DynaBean toBean(String table, Map<String, Object> row) {
            
            DynaBean bean;

            bean = database.createDynaBeanFor(table, false);
            row
                .keySet()
                .stream()
                .filter(k -> !k.startsWith(METADATA_KEY_PREFIX))
                .forEach(k -> bean.set(k, row.get(k)));
            return bean;
        }
        
        /**
         * 
         * @param bean
         * @return 
         */
        private Map<String,Object> toRow(DynaBean bean){

            Map<String,Object> row;
            
            row = new HashMap<>();
            Arrays
                .stream(bean.getDynaClass().getDynaProperties())
                .forEach(p -> row.put(p.getName(), bean.get(p.getName())));
            return row;
        }

        /**
         * 
         * @param columns
         * @return 
         */
        private List<SelectFieldOrAsterisk> generateSelectFields(String[] columns) {

            if (columns == null || columns.length==0) {
                return List.of(asterisk());
            } else {
                return Arrays
                    .stream(columns)
                    .map(c -> field(delimited(c)))
                    .collect(Collectors.toList());
            }
        }

        /**
         * 
         * @param criteria
         * @param parse
         * @return 
         */
        private List<Condition> generateWhereConditions(Map<String, Object> criteria, boolean parse) {
            
            List<Condition> conditions = new ArrayList<>();
            String key;
            String value;
            String[] parts;
            
            if (criteria == null) {
                criteria = Collections.EMPTY_MAP;
            }
            
            for(Map.Entry<String, Object> entry : criteria.entrySet()){
                if (entry.getKey().startsWith(METADATA_KEY_PREFIX)) {
                    continue;
                }
                else if (entry.getValue() == null) {
                    conditions.add(field(delimited(entry.getKey())).isNull());
                }
                else if (entry.getValue().getClass() == String.class && parse){
                    key = entry.getKey();
                    value = (String) entry.getValue();
                    if (value.matches("^\\s*[nN][uU][lL][lL]\\s*$")) {
                        conditions.add(field(delimited(entry.getKey())).isNull());
                    }
                    else if (value.contains("*") || value.contains("%")) {
                        conditions.add(field(delimited(key)).like(quoted(value.replace("*","%"))));
                    }
                    else if (value.startsWith("<=") || value.startsWith("=<")) {
                        conditions.add(field(delimited(key)).le(quoted(value.substring(2))));
                    }
                    else if (value.startsWith(">=") || value.startsWith("=>")) {
                        conditions.add(field(delimited(key)).ge(quoted(value.substring(2))));
                    }
                    else if (value.startsWith("<>")) {
                        conditions.add(field(delimited(key)).ne(quoted(value.substring(2))));
                    }
                    else if (value.startsWith("<")) {
                        conditions.add(field(delimited(key)).lt(quoted(value.substring(1))));
                    }
                    else if (value.startsWith(">")) {
                        conditions.add(field(delimited(key)).gt(quoted(value.substring(1))));
                    }
                    else if (value.startsWith("=")) {
                        conditions.add(field(delimited(key)).eq(quoted(value.substring(1))));
                    }
                    else if (value.startsWith("!")) {
                        if (value.substring(1).matches("^\\s*[nN][uU][lL][lL]\\s*$")) {
                            conditions.add(field(delimited(key)).isNotNull());
                        } else {
                            conditions.add(field(delimited(key)).ne(quoted(value.substring(1))));
                        }
                    }
                    else if (value.matches(".+\\s+[tT][oO]\\s+.+")) {
                        parts = value.split("[tT][oO]", 2);
                        conditions.add(field(delimited(key)).between(quoted(parts[0]), quoted(parts[1])));
                    }
                    else if ((parts = value.split(",", -1)).length > 1) {
                        conditions.add(field(delimited(key)).in(Arrays.stream(parts).map(p -> quoted(p)).collect(Collectors.toList())));
                    }
                    else {
                        conditions.add(field(delimited(key)).eq(quoted(value)));
                    }
                } else {
                    conditions.add(field(delimited(entry.getKey())).eq(quoted(entry.getValue())));
                }
            }
            return conditions;
        }

        /**
         * 
         * @param where
         * @return 
         */
        private String generateWhere(String where) {
            
            if (StringUtils.isBlank(where)) {
                return "1 = 1";
            } else {
                return where;
            }
        }
        
        /**
         * 
         * @param orderBy
         * @return 
         */
        private List<OrderField<?>> generateOrderByFields(String orderBy) {
            
            if (StringUtils.isBlank(orderBy)) {
                return Collections.EMPTY_LIST;
            } else {
                return Arrays
                    .stream(orderBy.split(",", -1))
                    .map(o -> {
                        // desc
                        if (o.matches(".+\\s[dD][eE][sS][cC]\\s*$")) {
                            return field(delimited(o.split("[dD][eE][sS][cC]")[0])).desc();
                        }
                        // asc
                        else if (o.matches(".+\\s[aA][sS][cC]\\s*$")) {
                            return field(delimited(o.split("[aA][sS][cC]")[0])).asc();
                        }
                        // default
                        else {
                            return field(delimited(o)).sortDefault();                        
                        }
                    })
                    .collect(Collectors.toList());
            }
        }
        
        /**
         * 
         * @param identifier
         * @return 
         */
        private String delimited(String identifier){

            String[] parts;
            
            // literal
            if (identifier.startsWith("'") && identifier.endsWith("'") ){
                parts = new String[]{identifier.substring(1, identifier.length() - 1)};
            // aliased    
            } else if (identifier.matches(".+\\s+[aA][sS]\\s+.+")) {
                parts = identifier.split("[aA][sS]", 2);
            // straight
            } else {
                parts = new String[]{identifier};
            }  
            // trim
            for (int i = 0; i < parts.length; i++) {
                parts[i] = parts[i].trim();
            }
            // delimit
            if (platform.isDelimitedIdentifierModeOn()) {
                parts[0] =  String.format(
                    "%s%s%s", 
                    platform.getPlatformInfo().getDelimiterToken(), 
                    parts[0],
                    platform.getPlatformInfo().getDelimiterToken());
            } 
            // format
            return String.join(" as ", parts);
        }
        
        /**
         * 
         * @param value
         * @return 
         */
        private String quoted(Object value) {
            
            if (value == null) {
                return "null";
            } else if (value.toString().startsWith("'") && value.toString().endsWith("'")) {
                return value.toString().substring(1, value.toString().length() - 1);
            } else {
                return value.toString().trim();
            }
        }      
    }
}