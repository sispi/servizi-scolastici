/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.wih;

import it.filippetti.ks.api.bpm.configuration.DatastoreConfiguration;
import it.filippetti.ks.api.bpm.exception.DatastoreException;
import it.filippetti.ks.api.bpm.service.DatastoreService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.mvel2.templates.TemplateRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
@WorkItemHandlerName({"Datastore Task", "DBIntegration"})
public class DatastoreTaskHandler implements WorkItemHandler, WorkItemSupport {
    
    private static final Logger log = LoggerFactory.getLogger(DatastoreTaskHandler.class);

    public static final String ENTITY_KEY = "@entity";
        
    // commands
    public final static String LOAD = "GET";
    public final static String SELECT = "SELECT";
    public final static String STORE = "INSERT_OR_UPDATE";
    public final static String INSERT = "INSERT";
    public final static String UPDATE = "UPDATE";
    public final static String DELETE = "DELETE";
    
    // parameters
    private final static String INPUT_OBJECT = "object";
    private final static String INPUT_COLLECTION = "collection";
    private final static String DATA_SOURCE_NAME = "datasource"; 
    private final static String COMMAND = "cmd_type";
    private final static String ENTITY = "entity";
    private final static String COLUMNS = "columns";
    private final static String WHERE = "where";
    private final static String ORDER_BY = "order_by";
    private final static String RAW_SQL = "raw_command";
    private final static String MAX_ROWS = "max_rows";
    private final static String THROW_ON_EMPTY = "throw_on_empty";

    // results
    private final static String OUTPUT_OBJECT = "object";
    private final static String OUTPUT_COLLECTION = "collection";
    private final static String OUTPUT_ROWS = "rows";

    @Autowired
    private DatastoreService datastoreService;
    
    @Override
    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {

        log.info(String.format(">>> executeWorkItem 'Datastore Task' (%d)", workItem.getId()));
        
        Map<String, Object> inputObject, outputObject, results;
        List<Map<String, Object>> inputCollection, outputCollection, collection;
        String dataSourceName, command, entity, where, orderBy, rawSql;
        String[] columns;
        Integer maxRows, affectedRows;
        Boolean throwOnEmpty, isRaw;
        
        DatastoreService.Datastore datastore;
        
        inputObject = getParameter(workItem, INPUT_OBJECT, Map.class);
        inputCollection = getParameter(workItem, INPUT_COLLECTION, List.class, new ArrayList<>());
        dataSourceName = getParameter(workItem, DATA_SOURCE_NAME, String.class, DatastoreConfiguration.LOCAL_DATASOURCE_NAME);
        command = getParameter(workItem, COMMAND, String.class);
        entity = getParameter(workItem, ENTITY, String.class);
        columns = getParameter(workItem, COLUMNS, String.class, "*").split(",");
        where = getParameter(workItem, WHERE, String.class);
        orderBy = getParameter(workItem, ORDER_BY, String.class);
        rawSql = getParameter(workItem, RAW_SQL, String.class);
        maxRows = getParameter(workItem, MAX_ROWS, Integer.class);
        throwOnEmpty = getParameter(workItem, THROW_ON_EMPTY, Boolean.class, false);

        if (inputObject != null) {
            inputCollection.add(inputObject);
        }
        if (!inputCollection.isEmpty()){
            // object/collection entity overrides entity
            entity = (String) inputCollection.get(0).get(ENTITY_KEY);
        }
        if (entity != null && entity.contains(".")){
            // entity prefix overrides dataSourceName
            dataSourceName = entity.split("\\.")[0];
            // entity suffix overrides entity
            entity = entity.split("\\.")[1];
        }
        if (StringUtils.isBlank(entity))  {
            throw new RuntimeException("No entity detected");
        }
        if (StringUtils.isBlank(dataSourceName) || dataSourceName.equals("default"))  {
            dataSourceName = DatastoreConfiguration.LOCAL_DATASOURCE_NAME;
        }
        
        try {
            datastore = datastoreService.getDatastore(dataSourceName);
        } catch (DatastoreException e) {
            throw new RuntimeException(e.getMessage());
        }
        
        if (!StringUtils.isBlank(where)){
            where = (String) TemplateRuntime.eval(where, workItem.getParameters());
        }
        if (!StringUtils.isBlank(rawSql)){
            isRaw = true;
            rawSql = (String) TemplateRuntime.eval(rawSql, workItem.getParameters());
        } else if (inputCollection.isEmpty()) {
            isRaw = true;
            rawSql = datastore.buildQuery(entity, where, columns, orderBy);
        } else {
            isRaw = false;
        }

        affectedRows = 0;
        outputCollection = new ArrayList<>();
        try {
            if (isRaw) {
                if (rawSql.trim().toLowerCase().startsWith("select")){
                    collection = datastore.executeQuery(rawSql, maxRows);
                    outputCollection.addAll(collection);
                    affectedRows = collection.size();
                } else {
                    affectedRows = datastore.executeUpdate(rawSql);
                }
            }
            else {
                for (Map<String, Object> object : inputCollection) {
                    switch (command) {
                        case SELECT:
                            collection = datastore.select(entity, object, columns, orderBy, maxRows);
                            affectedRows += collection.size();
                            outputCollection.addAll(collection);
                            break;
                        case LOAD:
                            object = datastore.load(entity, object, columns);
                            if (object != null)
                                affectedRows++;
                            outputCollection.add(object);
                            break;
                        case INSERT:
                            object = datastore.insert(entity, object);
                            affectedRows++;
                            outputCollection.add(object);
                            break;
                        case UPDATE:
                            object = datastore.update(entity, object);
                            affectedRows++;
                            outputCollection.add(object);
                            break;
                        case STORE:
                            object = datastore.store(entity, object);
                            affectedRows++;
                            outputCollection.add(object);
                            break;
                        case DELETE:
                            datastore.delete(entity, object);
                            affectedRows++;
                            outputCollection.add(null);
                            break;
                        default:
                            throw new IncompatibleClassChangeError();
                    }
                }
            }
            if (throwOnEmpty && affectedRows == 0) {
                 throw new RuntimeException("No affected rows");
            }
        } catch (DatastoreException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        
        for (Map<String, Object> object : outputCollection) {
            object.put(ENTITY_KEY, entity);
        }
        outputObject = !outputCollection.isEmpty() ? 
            outputCollection.get(0) : 
            null;

        results = new HashMap<>();
        results.put(OUTPUT_COLLECTION, outputCollection);
        results.put(OUTPUT_OBJECT, outputObject);
        results.put(OUTPUT_ROWS, affectedRows);

        manager.completeWorkItem(workItem.getId(), results);
    }

    @Override
    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {

        log.info(String.format(">>> abortWorkItem 'Datastore Task' (%d)", workItem.getId()));
    }    
}
