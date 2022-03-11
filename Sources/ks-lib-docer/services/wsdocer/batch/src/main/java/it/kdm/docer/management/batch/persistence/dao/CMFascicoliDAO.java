package it.kdm.docer.management.batch.persistence.dao;

import it.kdm.docer.management.batch.persistence.ConnectionManager;
import it.kdm.docer.management.batch.persistence.model.CMFascicolo;
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
 * Time: 19.24
 * To change this template use File | Settings | File Templates.
 */
public class CMFascicoliDAO {

    private static Logger log = org.slf4j.LoggerFactory.getLogger(CMFascicoliDAO.class);

    /**
     * @return
     */
    public List<CMFascicolo> getAllByRule(String ruleId){

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;
        List<CMFascicolo> listaFascicoli = new ArrayList<CMFascicolo>();

        try {
            conn = ConnectionManager.getLocalConnection();

            sql = "SELECT cod_ente, cod_aoo, classifica, anno_fascicolo, progr_fascicolo, acl_current, acl_modified, rules  FROM cm_fascicoli";
            if (!StringUtils.isEmpty(ruleId))
                sql += " WHERE rules LIKE ?;";
            else
                sql += ";";

            pstmt = conn.prepareStatement(sql);
            if (!StringUtils.isEmpty(ruleId))
                pstmt.setString(1, "%" + ruleId + "%");

            rs = pstmt.executeQuery();
            log.debug("GET Fascicoli eseguita con successo");

            while(rs.next()){
                CMFascicolo doc = new CMFascicolo();
                doc.setCodEnte(rs.getString("cod_ente"));
                doc.setCodAoo(rs.getString("cod_aoo"));
                doc.setClassifica(rs.getString("classifica"));
                doc.setAnnoFascicolo(rs.getString("anno_fascicolo"));
                doc.setProgrFascicolo(rs.getString("progr_fascicolo"));
                doc.setAclCurrent(rs.getString("acl_current"));
                doc.setAclModified(rs.getString("acl_modified"));
                doc.setRules(rs.getString("rules"));

                listaFascicoli.add(doc);
            }
        } catch (SQLException e) {
            log.error("getByRule cm_fascicoli  - Errore durante l'esecuzione " + sql + " " + e, e);
        } catch (Exception ex) {
            log.error("getByRule cm_fascicoli  - Errore durante l'esecuzione " + sql + " " + ex, ex);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error("errore durante la chiusura della connessione", e);
            }
        }

        return listaFascicoli;
    }

    /**
     *
     * @param codEnte
     * @param codAoo
     * @param classifica
     * @param anno
     * @param progr
     * @param conn
     * @return
     * @throws Exception
     */
    public CMFascicolo get(String codEnte, String codAoo, String classifica, String anno, String progr, Connection conn) throws Exception {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        try {
            sql = "SELECT cod_ente, cod_aoo, classifica, anno_fascicolo, progr_fascicolo, acl_current, acl_modified, rules  FROM cm_fascicoli"
                    + " WHERE cod_ente=? AND cod_aoo=? AND classifica=? AND anno_fascicolo=? AND progr_fascicolo=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codEnte);
            pstmt.setString(2, codAoo);
            pstmt.setString(3, classifica);
            pstmt.setString(4, anno);
            pstmt.setString(5, progr);
            rs = pstmt.executeQuery();

            if (rs.next()){
                CMFascicolo fasc = new CMFascicolo();
                fasc.setCodEnte(rs.getString("cod_ente"));
                fasc.setCodAoo(rs.getString("cod_aoo"));
                fasc.setClassifica(rs.getString("classifica"));
                fasc.setAnnoFascicolo(rs.getString("anno_fascicolo"));
                fasc.setProgrFascicolo(rs.getString("progr_fascicolo"));
                fasc.setAclCurrent(rs.getString("acl_current"));
                fasc.setAclModified(rs.getString("acl_modified"));
                fasc.setRules(rs.getString("rules"));

                return fasc;
            }

        } catch (Exception ex) {
            log.error("select cm_fascicoli - Errore durante l'esecuzione " + sql + ": " + ex.getMessage(), ex);
        }

        return null;
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
            conn = ConnectionManager.getLocalConnection();

            sql = "SELECT count(*) AS numero  FROM cm_fascicoli";
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
     * @param fascicolo
     */
    public void insert(CMFascicolo fascicolo, Connection conn) throws Exception {

        PreparedStatement pstmt = null;
        String sql = null;

        try {
            sql = "INSERT INTO cm_fascicoli (cod_ente, cod_aoo, classifica, anno_fascicolo, progr_fascicolo, acl_current, acl_modified, rules) VALUES (?,?,?,?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, fascicolo.getCodEnte());
            pstmt.setString(2, fascicolo.getCodAoo());
            pstmt.setString(3, fascicolo.getClassifica());
            pstmt.setString(4, fascicolo.getAnnoFascicolo());
            pstmt.setString(5, fascicolo.getProgrFascicolo());
            pstmt.setString(6, fascicolo.getAclCurrent());
            pstmt.setString(7, fascicolo.getAclModified());
            pstmt.setString(8, fascicolo.getRules());

            pstmt.executeUpdate();

        } catch (Exception ex) {
            log.error("Insert cm_fascicoli  - Errore durante l'esecuzione " + sql + ": " + ex.getMessage());
            throw ex;
        }
    }

    /**
     * @param listFascicoli
     */
    public int insert(List<CMFascicolo> listFascicoli, boolean truncateOld) {
        Connection conn = null;
        int counter = 0;

        try {
            conn = ConnectionManager.getLocalConnection();
            conn.setAutoCommit(false);

            // Svuoto la tabella su db
            if (truncateOld)
                truncateTable(conn);

            for (CMFascicolo fasc : listFascicoli) {
                CMFascicolo oldFasc = get(fasc.getCodEnte(), fasc.getCodAoo(), fasc.getClassifica(), fasc.getAnnoFascicolo(), fasc.getProgrFascicolo(), conn);
                if (oldFasc == null) {
                    insert(fasc, conn);
                    counter++;
                } else {
                    // Accodo la nuova rule
                    fasc.setRules(oldFasc.getRules() + fasc.getRules());
                    update(fasc, conn);
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
                log.error("Errore durante la chiusura della connessione");
            }
        }

        return counter;
    }

    /**
     * @param fascicolo
     * @return
     */
    public int update(CMFascicolo fascicolo, Connection conn) throws Exception {

        PreparedStatement pstmt = null;
        String sql = null;
        int result = 0;
        try {
            sql = "UPDATE cm_fascicoli SET acl_modified=?, rules=? WHERE cod_ente=? AND cod_aoo=? AND classifica=? AND anno_fascicolo=? AND progr_fascicolo=?;";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, fascicolo.getAclModified());
            pstmt.setString(2, fascicolo.getRules());
            pstmt.setString(3, fascicolo.getCodEnte());
            pstmt.setString(4, fascicolo.getCodAoo());
            pstmt.setString(5, fascicolo.getClassifica());
            pstmt.setString(6, fascicolo.getAnnoFascicolo());
            pstmt.setString(7, fascicolo.getProgrFascicolo());

            result = pstmt.executeUpdate();

        } catch (Exception ex) {
            log.error("update cm_fascicoli - Errore durante l'esecuzione " + sql + ": " + ex.getMessage());
            throw ex;
        }

        return result;
    }

    /**
     * @param listFascicoli
     * @return
     */
    public int update(List<CMFascicolo> listFascicoli) {
        Connection conn = null;
        int counter = 0;
        try {
            conn = ConnectionManager.getLocalConnection();
            conn.setAutoCommit(false);

            for (CMFascicolo fascicolo : listFascicoli) {
                counter += update(fascicolo, conn);
            }
            conn.commit();
            log.debug("Update eseguito con successo");

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
     * @param fascicolo
     * @param value
     * @throws Exception
     */
    public void flagInError(CMFascicolo fascicolo, int value) {

        PreparedStatement pstmt = null;
        String sql = null;
        try {
            sql = "UPDATE cm_fascicoli SET in_error=? WHERE cod_ente=? AND cod_aoo=? AND classifica=? AND anno_fascicolo=? AND progr_fascicolo=?;";
            Connection conn = ConnectionManager.getLocalConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, value);
            pstmt.setString(2, fascicolo.getCodEnte());
            pstmt.setString(3, fascicolo.getCodAoo());
            pstmt.setString(4, fascicolo.getClassifica());
            pstmt.setString(5, fascicolo.getAnnoFascicolo());
            pstmt.setString(6, fascicolo.getProgrFascicolo());

            pstmt.executeUpdate();

        } catch (Exception ex) {
            log.error("update cm_fascicoli - Errore durante l'esecuzione " + sql + ": " + ex.getMessage());
        }
    }

    /**
     *
     */
    public void truncateTable(Connection conn) throws Exception {
        PreparedStatement pstmt = null;
        String sql = null;

        try {
            sql = "DELETE FROM cm_fascicoli;";
            pstmt = conn.prepareStatement(sql);
            pstmt.execute();
            log.debug("Truncate eseguita con successo");

        } catch (Exception ex) {
            log.error("Truncate cm_fascicoli - Errore durante l'esecuzione " + sql + ": " + ex.getMessage());
            throw ex;
        }
    }


}
