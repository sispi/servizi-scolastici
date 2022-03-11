package keysuite.docer.bl;

//import it.kdm.orchestratore.beans.EmailBean;
//import it.kdm.orchestratore.beans.ImapMailItemList;
//import it.kdm.orchestratore.beans.ResultCall;

import keysuite.docer.client.SearchResponse;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IRestUtilsBridge <T,K>{

    Map getPDFfromHTML(Map<String,String> params) throws IOException, InterruptedException;

    K getLastImapList(Map<String, Object> conf, String lastDate, int limit);

    K getImapMail(Map<String, Object> conf, String messageId, String fileType, String alternativeDownloadPath);

    boolean clearAllTempFile()throws IOException;

    boolean clearAllMsgIdFiles(String messageId) throws IOException;

    void deleteTableValue( T dbManager, String tableName, String columnName, Object processInstanceId) throws SQLException ;

    void deleteTableValueIn( T dbManager, String tableName, String columnName, String valuesIn)throws SQLException;

    void updateStateNodeInstanceStates(Object processInstanceId, int newState,  T dbManagerMysql)throws SQLException;

    void restoreDb(String dbPath, String tableName, String processInstanceId)throws Exception;

    void backupDb(String dbTable, Object[] val, T dbManagerMysql, boolean removefile)throws Exception;

    T getSqliteDBManager(String processInstanceID, String endDate)throws Exception;

    void clearInstances(Map<String, Map<String, Object>> processInstanceIdArray, T dbManagerMysql, boolean completely, String identifierBackup);

    void deleteAllTable(T dbmanager, Object processInstanceID) throws SQLException;

    File compress(List<File> files, String password , String comment );

    List<File> uncompress(File file, String password);
}

