package it.kdm.docer.management.batch.persistence.dao;

import it.kdm.docer.management.batch.persistence.ConnectionManager;
import it.kdm.docer.management.batch.persistence.model.CMDocumento;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vaio
 * Date: 02/11/15
 * Time: 17.12
 * To change this template use File | Settings | File Templates.
 */
public class CMDocumentiDAO {

    private static Logger log = org.slf4j.LoggerFactory.getLogger(CMDocumentiDAO.class);

    /**
     * @return
     */
    public List<CMDocumento> getByRule(String ruleId){

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;
        List<CMDocumento> listaDoc = new ArrayList<CMDocumento>();

        try {
            conn = ConnectionManager.getLocalConnection();

            sql = "SELECT cod_ente, cod_aoo, docnum, acl_current, acl_modified, rules  FROM cm_documenti";
            if (!StringUtils.isEmpty(ruleId))
                sql += " WHERE rules LIKE ?;";
            else
                sql += ";";

            pstmt = conn.prepareStatement(sql);
            if (!StringUtils.isEmpty(ruleId))
                pstmt.setString(1, "%" + ruleId + "%");

            rs = pstmt.executeQuery();
            log.debug("getByRule all eseguita con successo");

            while(rs.next()){
                CMDocumento doc = new CMDocumento();
                doc.setAclCurrent(rs.getString("acl_current"));
                doc.setAclModified(rs.getString("acl_modified"));
                doc.setCodAoo(rs.getString("cod_aoo"));
                doc.setCodEnte(rs.getString("cod_ente"));
                doc.setDocnum(rs.getString("docnum"));
                doc.setRules(rs.getString("rules"));
                listaDoc.add(doc);
            }
        } catch (SQLException e) {
            log.error("getByRule cm_documenti  - Errore durante l'esecuzione " + sql + " " + e, e);
        } catch (Exception ex) {
            log.error("getByRule cm_documenti  - Errore durante l'esecuzione " + sql + " " + ex, ex);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error("errore durante la chiusura della connessione", e);
            }
        }

        return listaDoc;
    }

    /**
     *
     * @param codEnte
     * @param codAoo
     * @param docnum
     * @param conn
     * @return
     * @throws Exception
     */
    public CMDocumento get(String codEnte, String codAoo, String docnum, Connection conn) throws Exception {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        try {
            sql = "SELECT cod_ente, cod_aoo, docnum, acl_current, acl_modified, rules  FROM cm_documenti"
                + " WHERE cod_ente=? AND cod_aoo=? AND docnum=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codEnte);
            pstmt.setString(2, codAoo);
            pstmt.setString(3, docnum);
            rs = pstmt.executeQuery();

            if (rs.next()){
                CMDocumento doc = new CMDocumento();
                doc.setAclCurrent(rs.getString("acl_current"));
                doc.setAclModified(rs.getString("acl_modified"));
                doc.setCodAoo(rs.getString("cod_aoo"));
                doc.setCodEnte(rs.getString("cod_ente"));
                doc.setDocnum(rs.getString("docnum"));
                doc.setRules(rs.getString("rules"));

                return doc;
            }

        } catch (Exception ex) {
            log.error("select cm_documenti  - Errore durante l'esecuzione " + sql + ": " + ex.getMessage(), ex);
        }

        return null;
    }

    /**
     * @return
     */
    public int getCountDocumenti() {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        try {
            conn = ConnectionManager.getLocalConnection();

            sql = "SELECT count(*) AS numero  FROM cm_documenti";
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

    /**
     * @param documento
     */
    public void insert(CMDocumento documento, Connection conn) throws Exception {

        PreparedStatement pstmt = null;
        String sql = null;

        try {
            sql = "INSERT INTO cm_documenti (cod_ente, cod_aoo, docnum, acl_current, acl_modified, rules) VALUES (?,?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, documento.getCodEnte());
            pstmt.setString(2, documento.getCodAoo());
            pstmt.setString(3, documento.getDocnum());
            pstmt.setString(4, documento.getAclCurrent());
            pstmt.setString(5, documento.getAclModified());
            pstmt.setString(6, documento.getRules());

            pstmt.executeUpdate();

        } catch (Exception ex) {
            log.error("insert cm_documenti  - Errore durante l'esecuzione " + sql + ": " + ex.getMessage());
            throw ex;
        }
    }

    /**
     * @param listDoc
     */
    public int insert(List<CMDocumento> listDoc, boolean truncateOld) {
        Connection conn = null;
        int counter = 0;

        try {
            conn = ConnectionManager.getLocalConnection();
            conn.setAutoCommit(false);

            // Svuoto la tabella su db
            if (truncateOld)
                truncateTable(conn);

            for (CMDocumento doc : listDoc) {
                // Controllo se esiste il record, se sì effettuo l'update
                CMDocumento oldDoc = get(doc.getCodEnte(), doc.getCodAoo(), doc.getDocnum(), conn);
                if (oldDoc == null) {
                    insert(doc, conn);
                    counter++;
                } else {
                    // Accodo la nuova rule
                    doc.setRules(oldDoc.getRules() + doc.getRules());
                    update(doc, conn);
                }
            }
            conn.commit();
            log.debug("Inserimento eseguito con successo");

        } catch (Exception ex) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                log.error("Errore durante il rollback dell'operazione", e);
            }
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error("Errore durante la chiusura della connessione", e);
            }
        }

        return counter;
    }

    /**
     * @param doc
     * @return
     */
    public int update(CMDocumento doc, Connection conn) throws Exception {

        PreparedStatement pstmt = null;
        String sql = null;
        int result = 0;

        try {
            sql = "UPDATE cm_documenti SET acl_modified=?, rules=? WHERE cod_ente=? AND cod_aoo=? AND docnum=?;";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, doc.getAclModified());
            pstmt.setString(2, doc.getRules());
            pstmt.setString(3, doc.getCodEnte());
            pstmt.setString(4, doc.getCodAoo());
            pstmt.setString(5, doc.getDocnum());
            result = pstmt.executeUpdate();

        } catch (Exception ex) {
            log.error("Update cm_documenti  - Errore durante l'esecuzione " + sql + ": " + ex.getMessage());
            throw ex;
        }

        return result;
    }

    /**
     * @param listDoc
     */
    public int update(List<CMDocumento> listDoc) {
        Connection conn = null;
        int counter = 0;
        try {
            conn = ConnectionManager.getLocalConnection();
            conn.setAutoCommit(false);

            for (CMDocumento doc : listDoc) {
                counter += update(doc, conn);
            }
            conn.commit();
            log.debug("Update eseguito con successo");

            return counter;

        } catch (Exception ex) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                log.error("Errore durante il rollback dell'operazione", e);
            }
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error("Errore durante la chiusura della connessione", e);
            }
        }

        return counter;
    }

    /**
     * @param doc
     * @param value
     * @throws Exception
     */
    public void flagInError(CMDocumento doc, int value)  {

        PreparedStatement pstmt = null;
        String sql = null;

        try {
            sql = "UPDATE cm_documenti SET in_error=? WHERE cod_ente=? AND cod_aoo=? AND docnum=?;";
            Connection conn = ConnectionManager.getLocalConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, value);
            pstmt.setString(2, doc.getCodEnte());
            pstmt.setString(3, doc.getCodAoo());
            pstmt.setString(4, doc.getDocnum());

            pstmt.executeUpdate();

        } catch (Exception ex) {
            log.error("Update cm_documenti  - Errore durante l'esecuzione " + sql + ": " + ex.getMessage());
        }
    }

    /**
     *
     */
    public void truncateTable(Connection conn) throws Exception {
        PreparedStatement pstmt = null;
        String sql = null;

        try {
            sql = "DELETE FROM cm_documenti;";
            pstmt = conn.prepareStatement(sql);
            pstmt.execute();
            log.debug("Truncate eseguita con successo");

        } catch (Exception ex) {
            log.error("Truncate cm_documenti  - Errore durante l'esecuzione " + sql + ": " + ex.getMessage());
            throw ex;
        }
    }

}
