package it.kdm.docer.management.batch.util;

import it.kdm.docer.management.model.Batch;
import it.kdm.docer.management.model.Groups;
import it.kdm.docer.management.model.Rule;
import it.kdm.docer.sdk.classes.KeyValuePair;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.docer.sdk.interfaces.ISearchItem;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vaio
 * Date: 31/10/15
 * Time: 17.08
 * To change this template use File | Settings | File Templates.
 */
public class CriteriaUtil {

    /**
     * Inizializza i search criteria
     * @throws Exception
     */
    public static Map<String, List<String>> initSearchCriteria(Rule rule, String ente, String aoo) throws DocerException {

        Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

        // Aggiungo i criteri relativi ai gruppi
        if ( (rule.getSource().getGroups() == null || rule.getSource().getGroups().size() == 0)
                && (rule.getTarget().getGroups() == null || rule.getTarget().getGroups().size() == 0) )
            throw new DocerException("Nessun gruppo trovato in configurazione.");

        // Per la action ADD non si aggiunge il gruppo nei criteria
        List<String> values = new ArrayList<String>();
        if ( !IConst.ACTION_ADD.equalsIgnoreCase(rule.getAction()) ) {
            Groups groups = rule.getSource().getGroups().get(0);
            for (int i=0; i < groups.getGroup().size(); i++) {
                String group = groups.getGroup().get(i).getValue();
                values.add(group + "@*");
                values.add("*," + group + "@*");
            }

            addCriteria(searchCriteria, "ACL_EXPLICIT", values);
        }

        // Aggiungo i criteri relativi ad ente e aoo
        List<String> enteList = new ArrayList<String>();
        enteList.add(ente);
        List<String> aooList = new ArrayList<String>();
        aooList.add(aoo);
        addCriteria(searchCriteria, "COD_ENTE", enteList);
        addCriteria(searchCriteria, "COD_AOO", aooList);

        return searchCriteria;
    }

    /**
     * Costruisce una nuova mappa in cui mette i criteri base e aggiunge gli ulteriori filtri se presenti
     * @param searchCriteriaBase
     * @return
     */
    public static Map<String, List<String>> addQueryFilters(Map<String, List<String>> searchCriteriaBase, String query) {
        // Copio i criteri base nella mappa che devo restituire
        Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
        for (String key : searchCriteriaBase.keySet()) {
            searchCriteria.put(key, searchCriteriaBase.get(key));
        }

        String[] filters = query.split(",");
        if (filters.length > 0) {
            for (String filter : filters) {
                String[] crit = filter.split(":");
                if (crit.length == 2) {
                    addCriteria(searchCriteria, crit[0], crit[1]);
                }
            }
        }

        return searchCriteria;
    }

    /**
     * Aggiunge un criterio con valore singolo
     * @param searchCriteria
     * @param key
     * @param value
     */
    public static void addCriteria(Map<String, List<String>> searchCriteria, String key, String value) {
        List<String> list = searchCriteria.get(key);
        if (list == null)
            list = new ArrayList<String>();

        list.add(value);
        searchCriteria.put(key, list);
    }

    /**
     * Aggiunge un criterio con lista di valori
     * @param searchCriteria
     * @param key
     * @param values
     */
    public static void addCriteria(Map<String, List<String>> searchCriteria, String key, List<String> values) {
        searchCriteria.put(key, values);
    }

    /**
     * Aggiunge 100 ms alla data di creazione
     * @param item
     * @param searchCriteria
     */
    public static void incrementCreated(ISearchItem item, Map<String, List<String>> searchCriteria) {
        String creationDate = extractField(IConst.FLD_CREATED, item);
        Date oldDate = DateUtil.fromSolrDate(creationDate);
        Date nextStartDate = DateUtil.incrementDate(oldDate, Calendar.SECOND, 1);

        // Sostituisco la vecchia data con la nuova incrementata nei criteria
        searchCriteria.remove(IConst.FLD_CREATED);
        addCriteria(searchCriteria, IConst.FLD_CREATED, DateUtil.toSolrInterval(nextStartDate, null));
    }

    /**
     * Estrae un campo dalla lista dei metadati
     */
    public static String extractField(String field, ISearchItem item) {

        for (KeyValuePair pair : item.getMetadata()) {
            if (field.equalsIgnoreCase(pair.getKey())) {
                return pair.getValue();
            }
        }

        return "";
    }

}
