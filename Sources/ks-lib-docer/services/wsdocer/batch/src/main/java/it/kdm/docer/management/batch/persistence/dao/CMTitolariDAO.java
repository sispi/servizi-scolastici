package it.kdm.docer.management.batch.persistence.dao;

import it.kdm.docer.management.batch.persistence.ConnectionManager;
import it.kdm.docer.management.batch.persistence.model.CMTitolario;
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
 * Time: 19.33
 * To change this template use File | Settings | File Templates.
 */
public class CMTitolariDAO {

    private static Logger log = org.slf4j.LoggerFactory.getLogger(CMTitolariDAO.class);

    /**
     * @return
     */
    public List<CMTitolario> getAllByRule(String ruleId){

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;
        List<CMTitolario> listaTitolari = new ArrayList<CMTitolario>();

        try {
            conn = ConnectionManager.getLocalConnection();

            sql = "SELECT cod_ente, cod_aoo, classifica, acl_current, acl_modified, rules FROM cm_titolari";
            if (!StringUtils.isEmpty(ruleId))
                sql += " WHERE rules LIKE ?;";
            else
                sql += ";";

            pstmt = conn.prepareStatement(sql);
            if (!StringUtils.isEmpty(ruleId))
                pstmt.setString(1, "%" + ruleId + "%");

            rs = pstmt.executeQuery();
            log.debug("GET Titolari eseguita con successo");

            while(rs.next()){
                CMTitolario titolario = new CMTitolario();
                titolario.setCodEnte(rs.getString("cod_ente"));
                titolario.setCodAoo(rs.getString("cod_aoo"));
                titolario.setClassifica(rs.getString("classifica"));
                titolario.setAclCurrent(rs.getString("acl_current"));
                titolario.setAclModified(rs.getString("acl_modified"));
                titolario.setRules(rs.getString("rules"));

                listaTitolari.add(titolario);
            }
        } catch (SQLException e) {
            log.error("getByRule cm_titolari  - Errore durante l'esecuzione " + sql + " " + e, e);
        } catch (Exception ex) {
            log.error("getByRule cm_titolari  - Errore durante l'esecuzione " + sql + " " + ex, ex);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error("errore durante la chiusura della connessione", e);
            }
        }

        return listaTitolari;
    }

    public CMTitolario get(String codEnte, String codAoo, String classifica, Connection conn) throws Exception {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        try {
            sql = "SELECT cod_ente, cod_aoo, classifica, acl_current, acl_modified, rules  FROM cm_titolari"
                    + " WHERE cod_ente=? AND cod_aoo=? AND classifica=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codEnte);
            pstmt.setString(2, codAoo);
            pstmt.setString(3, classifica);
            rs = pstmt.executeQuery();

            if (rs.next()){
                CMTitolario titolario = new CMTitolario();
                titolario.setCodEnte(rs.getString("cod_ente"));
                titolario.setCodAoo(rs.getString("cod_aoo"));
                titolario.setClassifica(rs.getString("classifica"));
                titolario.setAclCurrent(rs.getString("acl_current"));
                titolario.setAclModified(rs.getString("acl_modified"));
                titolario.setRules(rs.getString("rules"));

                return titolario;
            }

        } catch (Exception ex) {
            log.error("select cm_titolari - Errore durante l'esecuzione " + sql + ": " + ex.getMessage(), ex);
        }

        return null;
    }

    /**
     * @return
     */
    public int getCountTitolari() {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        try {
            conn = ConnectionManager.getLocalConnection();

            sql = "SELECT count(*) AS numero  FROM cm_titolari";
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
     * @param titolario
     * @param conn
     * @throws Exception
     */
    public void insert(CMTitolario titolario, Connection conn) throws Exception {

        PreparedStatement pstmt = null;
        String sql = null;

        try {
            sql = "INSERT INTO cm_titolari (cod_ente, cod_aoo, classifica, acl_current, acl_modified, rules) VALUES (?,?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, titolario.getCodEnte());
            pstmt.setString(2, titolario.getCodAoo());
            pstmt.setString(3, titolario.getClassifica());
            pstmt.setString(4, titolario.getAclCurrent());
            pstmt.setString(5, titolario.getAclModified());
            pstmt.setString(6, titolario.getRules());

            pstmt.executeUpdate();

        } catch (Exception ex) {
            log.error("insert cm_titolari  - Errore durante l'esecuzione " + sql + ": " + ex.getMessage());
            throw ex;
        }
    }

    /**
     * @param listTitolari
     */
    public int insert(List<CMTitolario> listTitolari, boolean truncateOld) {
        Connection conn = null;
        int counter = 0;

        try {
            conn = ConnectionManager.getLocalConnection();
            conn.setAutoCommit(false);

            // Svuoto la tabella su db
            if (truncateOld)
                truncateTable(conn);

            for (CMTitolario tit : listTitolari) {
                CMTitolario oldTit = get(tit.getCodEnte(), tit.getCodAoo(), tit.getClassifica(), conn);
                if (oldTit == null) {
                    insert(tit, conn);
                    counter++;
                } else {
                    // Accodo la nuova rule
                    tit.setRules(oldTit.getRules() + tit.getRules());
                    update(tit, conn);
                }
            }
            conn.commit();
            log.debug("Inserimento eseguito con successo");

        } catch (Exception ex) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                System.out.println("Errore durante il rollback dell'operazione");
                e.printStackTrace();
            }
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Errore durante la chiusura della connessione");
                e.printStackTrace();
            }
        }

        return counter;
    }

    /**
     * @param titolario
     * @param conn
     * @return
     */
    public int update(CMTitolario titolario, Connection conn) throws Exception {

        PreparedStatement pstmt = null;
        String sql = null;
        int result = 0;
        try {
            sql = "UPDATE cm_titolari SET acl_modified=?, rules=? WHERE cod_ente=? AND cod_aoo=? AND classifica=?;";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, titolario.getAclModified());
            pstmt.setString(2, titolario.getRules());
            pstmt.setString(3, titolario.getCodEnte());
            pstmt.setString(4, titolario.getCodAoo());
            pstmt.setString(5, titolario.getClassifica());

            result = pstmt.executeUpdate();

        } catch (Exception ex) {
            log.error("update cm_titolari  - Errore durante l'esecuzione " + sql + ": " + ex.getMessage());
            throw ex;
        }

        return result;
    }

    /**
     * @param listTitolari
     * @return
     */
    public int update(List<CMTitolario> listTitolari) {
        Connection conn = null;
        int counter = 0;
        try {
            conn = ConnectionManager.getLocalConnection();
            conn.setAutoCommit(false);

            for (CMTitolario titolario : listTitolari) {
                counter += update(titolario, conn);
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
     * @param titolario
     * @param value
     * @throws Exception
     */
    public void flagInError(CMTitolario titolario, int value) {

        PreparedStatement pstmt = null;
        String sql = null;
        try {
            sql = "UPDATE cm_titolari SET in_error=? WHERE cod_ente=? AND cod_aoo=? AND classifica=?;";
            Connection conn = ConnectionManager.getLocalConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, value);
            pstmt.setString(2, titolario.getCodEnte());
            pstmt.setString(3, titolario.getCodAoo());
            pstmt.setString(4, titolario.getClassifica());

            pstmt.executeUpdate();

        } catch (Exception ex) {
            log.error("update cm_titolari  - Errore durante l'esecuzione " + sql + ": " + ex.getMessage());
        }
    }

    /**
     *
     */
    public void truncateTable(Connection conn) throws Exception {
        PreparedStatement pstmt = null;
        String sql = null;

        try {
            sql = "DELETE FROM cm_titolari;";
            pstmt = conn.prepareStatement(sql);
            pstmt.execute();
            log.debug("Truncate eseguita con successo");

        } catch (Exception ex) {
            log.error("Truncate cm_titolari  - Errore durante l'esecuzione " + sql + ": " + ex.getMessage());
            throw ex;
        }
    }

}
