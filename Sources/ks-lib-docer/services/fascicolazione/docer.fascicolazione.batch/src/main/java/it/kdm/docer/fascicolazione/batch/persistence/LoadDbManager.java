package it.kdm.docer.fascicolazione.batch.persistence;

import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by SL_550 on 12/01/2016.
 */
public class LoadDbManager {

    private static Logger log = org.slf4j.LoggerFactory.getLogger(LoadDbManager.class);

    /**
     *
     * @param metadata
     * @param conn
     * @throws SQLException
     */
    public void insert(Map<String, String> metadata, Connection conn) throws Exception {

        if (conn == null || conn.isClosed())
            conn = ConnectionManager.INSTANCE.getConnection();

        PreparedStatement pstmt = null;
        StringBuilder sqlInsert = new StringBuilder("INSERT INTO load_fascicolo(");
        StringBuilder sqlValues = new StringBuilder(" VALUES(");

        int n = 1;
        for (String key : metadata.keySet()) {
            if (n++ == 1) {
                sqlInsert.append(key);
                sqlValues.append("?");
            } else {
                sqlInsert.append(", " + key);
                sqlValues.append(", ?");
            }
        }
        sqlInsert.append(")");
        sqlValues.append(");");

        pstmt = conn.prepareStatement(sqlInsert.toString() + sqlValues.toString());
        int p = 1;
        for (String key : metadata.keySet()) {
            pstmt.setString(p++, metadata.get(key));
        }

        pstmt.executeUpdate();
        commit();
    }

    /**
     *
     * @param conn
     * @throws Exception
     */
    public void truncateFascicoli(Connection conn) throws Exception {

        if (conn == null || conn.isClosed())
            conn = ConnectionManager.INSTANCE.getConnection();

        PreparedStatement pstmt = null;
        String sql = null;

        try {
            sql = "DELETE FROM load_fascicolo;";
            pstmt = conn.prepareStatement(sql);
            pstmt.execute();

            sql = "DELETE FROM load_execution_data;";
            pstmt = conn.prepareStatement(sql);
            pstmt.execute();

            sql = "DELETE FROM load_execution;";
            pstmt = conn.prepareStatement(sql);
            pstmt.execute();

            commit();

        } catch (Exception ex) {
            log.error("Truncate load_fascicolo  - Errore durante l'esecuzione " + sql + ": " + ex.getMessage());
            throw ex;
        }
    }

    /**
     * @return
     */
    public int getCountFascicoli() {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        try {
            conn = ConnectionManager.INSTANCE.getConnection();

            sql = "SELECT count(*) AS numero  FROM load_fascicolo";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()){
                return rs.getInt("numero");
            }

        } catch (Exception ex) {
            log.error("Errore durante l'esecuzione " + sql + ": " + ex.getMessage(), ex);
        }

        return -1;
    }

    public void closeConnection() throws SQLException {
        ConnectionManager.INSTANCE.closeConnection();
    }

    public void commit() throws SQLException {
        ConnectionManager.INSTANCE.commitTransaction();
    }
}
