package it.kdm.docer.management.batch;

import it.kdm.docer.businesslogic.BusinessLogic;
import it.kdm.docer.management.batch.persistence.model.CMDocumento;
import it.kdm.docer.management.batch.util.IConst;
import it.kdm.docer.sdk.EnumACLRights;
import it.kdm.docer.sdk.EnumSearchOrder;
import it.kdm.docer.sdk.classes.KeyValuePair;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.docer.sdk.interfaces.ISearchItem;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vaio
 * Date: 29/10/15
 * Time: 9.12
 * To change this template use File | Settings | File Templates.
 */
public class BLFacade {

    private BusinessLogic bl;
    private Properties properties;

    private String user;
    private String password;
    private String ente;
    private int searchMaxRows;

    private static final String TYPE_FASCICOLO = "fascicolo";
    private static final String TYPE_TITOLARIO = "titolario";

    private static Logger log = org.slf4j.LoggerFactory.getLogger(BLFacade.class);

    public BLFacade(String user, String password, String ente, int searchMaxRows, String configPath) {
        try {
            //if (properties == null)
              //  properties = loadConfiguration();

            this.user = user;
            this.password = password;
            this.ente = ente;

            this.searchMaxRows = searchMaxRows;
            Configuration.init(configPath);
            BusinessLogic.setConfigPath(Configuration.getInstance().getProperty("service.ws.configdir"));

            bl = new BusinessLogic(Configuration.getInstance().getProperty("bl.provider.class"), searchMaxRows);

        } catch(Exception ex) {
            log.error("Errore nemm'inizializzazione della BusinessLogic: " + ex.getMessage(), ex);
        }

    }

    /**
     * Effettua il login e restituisce il token
     * @return
     * @throws DocerException
     */
    public String login() throws DocerException{
        return bl.login(ente, user, password);
    }

    /**
     * Preleva un chunk di documenti
     * @param token
     * @param searchCriteria
     * @return
     * @throws DocerException
     */
    public List<ISearchItem> getDocumentiChunk(String token, Map<String, List<String>> searchCriteria) throws DocerException {
        Map<String, EnumSearchOrder> orderbyMap = new HashMap<String, EnumSearchOrder>();
        orderbyMap.put(IConst.FLD_CREATED, EnumSearchOrder.ASC);
        List<String> keywords = new ArrayList<String>();

        return bl.searchDocuments(token, searchCriteria, keywords, searchMaxRows, orderbyMap);
    }

    /**
     * Preleva un chunk di fascicoli
     * @param token
     * @param searchCriteria
     * @return
     * @throws DocerException
     */
    public List<ISearchItem> getFascicoliChunk(String token, Map<String, List<String>> searchCriteria) throws DocerException {
        Map<String, EnumSearchOrder> orderbyMap = new HashMap<String, EnumSearchOrder>();
        orderbyMap.put(IConst.FLD_CREATED, EnumSearchOrder.ASC);

        return bl.searchAnagraficheEstesa(token, TYPE_FASCICOLO, searchCriteria, searchMaxRows, orderbyMap);
    }

    /**
     * Preleva un chunk di titolari
     * @param token
     * @param searchCriteria
     * @return
     * @throws DocerException
     */
    public List<ISearchItem> getTitolariChunk(String token, Map<String, List<String>> searchCriteria) throws DocerException {
        Map<String, EnumSearchOrder> orderbyMap = new HashMap<String, EnumSearchOrder>();
        orderbyMap.put(IConst.FLD_CREATED, EnumSearchOrder.ASC);

        return bl.searchAnagraficheEstesa(token, TYPE_TITOLARIO, searchCriteria, searchMaxRows, orderbyMap);
    }

    /**
     * Scrive su Solr le Acl del documento
     * @param token
     * @param docId
     * @param mapAcl
     */
    public boolean setAclDocumento(String token, String docId, Map<String, EnumACLRights> mapAcl) {
        try {
            bl.setACLDocument(token, docId, mapAcl);
            return true;
        } catch(Exception ex) {
            log.error("Errore in scrittura dei permessi per il documento " + docId + ": " + ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Scrive su Solr le Acl del fascicolo
     * @param token
     * @param codEnte
     * @param codAoo
     * @param anno
     * @param progr
     * @param classifica
     * @param mapAcl
     */
    public boolean setAclFascicolo(String token, String codEnte, String codAoo, String anno, String progr, String classifica, String pianoClass, Map<String, EnumACLRights> mapAcl) {
        try {
            Map<String, String> mapFascicoloId = new HashMap<String, String>();
            mapFascicoloId.put(IConst.FLD_COD_ENTE, codEnte);
            mapFascicoloId.put(IConst.FLD_COD_AOO, codAoo);
            mapFascicoloId.put(IConst.FLD_ANNO_FASCICOLO, anno);
            mapFascicoloId.put(IConst.FLD_PROGR_FASCICOLO, progr);
            mapFascicoloId.put(IConst.FLD_CLASSIFICA, classifica);
            if (!StringUtils.isEmpty(pianoClass))
                mapFascicoloId.put(IConst.FLD_PIANO_CLASS, pianoClass);

            bl.setACLFascicolo(token, mapFascicoloId, mapAcl);
            return true;
        } catch(Exception ex) {
            log.error("Errore in scrittura dei permessi per il fascicolo " + codEnte + "!" + codAoo + "!" + classifica + "$" + pianoClass + "|" + anno + "|" + progr + ": " + ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Scrive su Sorl le Acl del titolario
     * @param token
     * @param codEnte
     * @param codAoo
     * @param classifica
     * @param mapAcl
     */
    public boolean setAclTitolario(String token, String codEnte, String codAoo, String classifica, String pianoClass, Map<String, EnumACLRights> mapAcl) {
        try {
            Map<String, String> mapTitolarioId = new HashMap<String, String>();
            mapTitolarioId.put(IConst.FLD_COD_ENTE, codEnte);
            mapTitolarioId.put(IConst.FLD_COD_AOO, codAoo);
            mapTitolarioId.put(IConst.FLD_CLASSIFICA, classifica);
            if (!StringUtils.isEmpty(pianoClass))
                mapTitolarioId.put(IConst.FLD_PIANO_CLASS, pianoClass);

            bl.setACLTitolario(token, mapTitolarioId, mapAcl);
            return true;
        } catch(Exception ex) {
            log.error("Errore in scrittura dei permessi per il titolario " + codEnte + "!" + codAoo + "!" + classifica + "$" + pianoClass + ": " + ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * @param token
     * @param groupId
     * @return
     */
    public boolean checkGroupExists(String token, String groupId) {

        try {
            Map<String, String> group = bl.getGroup(token, groupId);
            if (group != null)
                return true;
        } catch(Exception ex) {
            log.error("Errore in lettura: " + ex.getMessage(), ex);
        }

        return false;
    }

    /*public static void main(String[] args) {
        try {
            System.out.println("Inizio elaborazione...");

            Configuration.init(null);
            BLFacade facade =  new BLFacade("ENTEPROVA", "admin", "admin", 2);

            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

            List<String> enteList = new ArrayList<String>();
            enteList.add("ENTEPROVA");
            searchCriteria.put("COD_ENTE", enteList);
            List<String> aclList = new ArrayList<String>();
            aclList.add("admin@*");
            searchCriteria.put("ACL_EXPLICIT", aclList);

            //List<ISearchItem> resDocumenti = facade.getDocumentiChunk(searchCriteria);
            //List<ISearchItem> resFascicoli = facade.getFascicoliChunk(facade.login(), searchCriteria);
            List<ISearchItem> resTitolari = facade.getTitolariChunk(facade.login(), searchCriteria);
            System.out.println("Aggia fatt...");

        } catch(Exception ex) {
            System.out.println("Errore nell'elaborazione: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    */

}
