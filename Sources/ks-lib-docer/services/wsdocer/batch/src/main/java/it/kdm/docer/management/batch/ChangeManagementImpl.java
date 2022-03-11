package it.kdm.docer.management.batch;

import it.kdm.docer.management.batch.persistence.dao.CMDocumentiDAO;
import it.kdm.docer.management.batch.persistence.dao.CMFascicoliDAO;
import it.kdm.docer.management.batch.persistence.dao.CMTitolariDAO;
import it.kdm.docer.management.batch.persistence.model.CMBase;
import it.kdm.docer.management.batch.persistence.model.CMDocumento;
import it.kdm.docer.management.batch.persistence.model.CMFascicolo;
import it.kdm.docer.management.batch.persistence.model.CMTitolario;
import it.kdm.docer.management.batch.util.AclUtil;
import it.kdm.docer.management.batch.util.CriteriaUtil;
import it.kdm.docer.management.batch.util.IConst;
import it.kdm.docer.management.model.*;
import it.kdm.docer.sdk.EnumACLRights;
import it.kdm.docer.sdk.classes.KeyValuePair;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.docer.sdk.interfaces.ISearchItem;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import java.util.*;

public class ChangeManagementImpl implements ChangeManagment {

    private String username;
    private String password;
    private String codiceEnte;
    private String pianoClass;
    private Batch batch;
    private Map<String, Groups> mapGroupsSource;
    private Map<String, Groups> mapGroupsTarget;
    private int searchMaxRows;
    private RunMode runMode;
    private String configPath;

    private BLFacade blFacade;
    private CMDocumentiDAO cmDocumentiDAO;
    private CMFascicoliDAO cmFascicoliDAO;
    private CMTitolariDAO cmTitolariDAO;

    private static Logger log = org.slf4j.LoggerFactory.getLogger(ChangeManagementImpl.class);


    public ChangeManagementImpl(String username,
                                String password,
                                String codiceEnte,
                                String pianoClass,
                                Batch batch,
                                int searchMaxRows,
                                RunMode runMode,
                                String configPath) {
        this.username = username;
        this.password = password;
        this.codiceEnte = codiceEnte;
        this.pianoClass = pianoClass;
        this.batch = batch;
        this.searchMaxRows = searchMaxRows;
        this.runMode = runMode;
        this.configPath = configPath;
    }

    /**
     * Inizializza gli oggetti
     */
    private void init() throws Exception {
        this.blFacade = new BLFacade(username, password, codiceEnte, searchMaxRows, configPath);
        this.cmDocumentiDAO = new CMDocumentiDAO();
        this.cmFascicoliDAO = new CMFascicoliDAO();
        this.cmTitolariDAO = new CMTitolariDAO();

        mapGroupsSource = new HashMap<String, Groups>();
        mapGroupsTarget = new HashMap<String, Groups>();

        String token = null;

        // Controllo che siano presenti ente e aoo
        if (StringUtils.isEmpty(batch.getEnte()) || StringUtils.isEmpty(batch.getAoo()))
            throw new DocerException("Gli attributi ente e aoo del tag <batch> sono obbligatori");

        // Controllo che non manchi il source o la uniqueKey e inizializzo le mappe
        Set<String> uniqueKeys = new HashSet<String>();
        for (Rule rule : batch.getRules()) {
            if (StringUtils.isEmpty(rule.getUniqueKey()))
                throw new DocerException("L'attributo uniqueKey del tag <rule> e' obbligatorio");

            if (StringUtils.isEmpty(rule.getAction()))
                throw new DocerException("Nessuna action definita per la rule " + rule.getUniqueKey());

            // A parte l'azione ADD, per le altre azioni ci deve essere almeno un gruppo source
            if ( !IConst.ACTION_ADD.equalsIgnoreCase(rule.getAction())
                    && (rule.getSource().getGroups().get(0) == null) )
                throw new DocerException("Nessun gruppo source in configurazione per la rule " + rule.getUniqueKey());

            if (!uniqueKeys.add(rule.getUniqueKey()))
                throw new DocerException("Lo stesso attributo uniqueKey del tag <rule> non può essere utilizzato per più di una rule");

            // Controllo che i gruppi source esistano su DocER
            if (token == null)
                token = blFacade.login();

            if (rule.getSource().getGroups().get(0).getGroup() != null) {
                for (Group g : rule.getSource().getGroups().get(0).getGroup()) {
                    if (!blFacade.checkGroupExists(token, g.getValue()))
                        throw new DocerException("Il gruppo " + g.getValue() + " della rule " + rule.getUniqueKey() + " non e' presente su DocER");
                }
            }

            mapGroupsSource.put(rule.getUniqueKey(), rule.getSource().getGroups().get(0));

            // A parte l'azione REMOVE, per le altre azioni ci deve essere almeno un gruppo target
            if ( !IConst.ACTION_REMOVE.equalsIgnoreCase(rule.getAction())
                    && (rule.getTarget().getGroups().get(0) == null) )
                throw new DocerException("Nessun gruppo target in configurazione per la rule " + rule.getUniqueKey());

            // Controllo che i gruppi target esistano su DocER e che eventuali rights siano scritti correttamente
            if (rule.getTarget().getGroups().get(0).getGroup() != null) {
                for (Group g : rule.getTarget().getGroups().get(0).getGroup()) {
                    if (!blFacade.checkGroupExists(token, g.getValue()))
                        throw new DocerException("Il gruppo " + g.getValue() + " della rule " + rule.getUniqueKey() + " non e' presente su DocER");

                    if (!AclUtil.checkRightsExists(g.getRights()))
                        throw new DocerException("La permission " + g.getRights() + " della rule " + rule.getUniqueKey() + " non e' corretta. Sono accettati solo i valori: "
                            + EnumACLRights.readOnly.toString() + " | " + EnumACLRights.normalAccess.toString() + " | " +EnumACLRights.fullAccess);
                }
            }

            mapGroupsTarget.put(rule.getUniqueKey(), rule.getTarget().getGroups().get(0));
        }
    }

	@Override
	public void execute() throws Exception {

        init();

        if (runMode.equals(RunMode.TEST) || runMode.equals(RunMode.ALL)) {
            log.info("Step 1: inserimento dei dati su db...");

            loadTitolari();
            loadFascicoli();
            loadDocumenti();

            log.info("Fine Step 1.");

            log.info("Step 2: update dei dati su db...");

            updateGroupTitolari();
            updateGroupFascicoli();
            updateGroupDocumenti();

            log.info("--------------------------------------------");
            log.info("Numero record in tabella cm_titolari: " + cmTitolariDAO.getCountTitolari());
            log.info("Numero record in tabella cm_fascicoli: " + cmFascicoliDAO.getCountFascicoli());
            log.info("Numero record in tabella cm_documenti: " + cmDocumentiDAO.getCountDocumenti());

            log.info("--------------------------------------------");
            log.info("Fine Step 2.");
        }

        if (runMode.equals(RunMode.RUN) || runMode.equals(RunMode.ALL)) {
            log.info("Step 3 (RUN-MODE): scrittura dei dati su Solr...");

            sendGroupTitolari();
            sendGroupFascicoli();
            sendGroupDocumenti();

            log.info("--------------------------------------------");
            log.info("Fine Step 3.");
        }

    }

    /**
     * Carica i documenti nel db
     */
    private void loadDocumenti() throws DocerException {
        log.info("--------------------------------------------");
        log.info("Inizio carico documenti...");

        String token = null;
        int ruleCount = 0;

        // Per ogni rule
        for (Rule rule : batch.getRules()) {
            if (rule.getQuery_documento() == null)
                continue;

            log.info("RULE: " + rule.getUniqueKey());

            // Inizializzo i searchCriteria di questa rule
            Map<String, List<String>> searchCriteriaBase = CriteriaUtil.initSearchCriteria(rule, batch.getEnte(), batch.getAoo());
            if (token == null)
                token = blFacade.login();

            // Per ogni query_documento
            for (int i=0; i < rule.getQuery_documento().size(); i++) {
                String queryDoc = rule.getQuery_documento().get(i);

                // Inizializzo i contatori di questa rule
                int recordsFound = 0;
                int recordsInserted = 0;

                // Aggiungo i criteri per ciascuna query_documento all'interno della rule
                Map<String, List<String>> searchCriteria = CriteriaUtil.addQueryFilters(searchCriteriaBase, queryDoc);

                List<CMDocumento> listDoc = new ArrayList<CMDocumento>();

                // Vado avanti a chunks finché ce ne sono
                List<ISearchItem> results = blFacade.getDocumentiChunk(token, searchCriteria);
                while (results.size() > 0) {
                    recordsFound += results.size();

                    for (ISearchItem item : results) {
                        CMDocumento doc = new CMDocumento();
                        doc.setCodEnte(batch.getEnte());
                        doc.setCodAoo(batch.getAoo());
                        doc.setDocnum(CriteriaUtil.extractField("DOCNUM", item));
                        String aclExplicit = CriteriaUtil.extractField("ACL_EXPLICIT", item).replace(" ", "");
                        doc.setAclCurrent(aclExplicit);
                        doc.setAclModified(aclExplicit);
                        doc.setRules(AclUtil.makeRuleId(rule.getUniqueKey()));

                        listDoc.add(doc);
                    }

                    // Incremento data di creazione e riparto con la ricerca
                    ISearchItem lastItem = results.get(results.size() - 1);
                    CriteriaUtil.incrementCreated(lastItem, searchCriteria);

                    // Prossimo chunk
                    results = blFacade.getDocumentiChunk(token, searchCriteria);
                }

                // Inserisco nel db, alla prima insert svuoto prima la tabella
                recordsInserted += cmDocumentiDAO.insert(listDoc, ++ruleCount == 1);

                log.info("Query " + String.valueOf(i+1));
                log.info("Documenti trovati: " + recordsFound + " - inseriti: " + recordsInserted + " - gia' presenti: " + String.valueOf(recordsFound - recordsInserted));
                log.info("--------------------------------------------");
            }
        }
    }

    /**
     * Carica i fascicoli nel db
     */
    private void loadFascicoli() throws DocerException {
        log.info("--------------------------------------------");
        log.info("Inizio carico fascicoli...");

        String token = null;
        int ruleCount = 0;

        // Per ogni rule
        for (Rule rule : batch.getRules()) {
            if (rule.getQuery_fascicolo() == null)
                continue;

            log.info("RULE: " + rule.getUniqueKey());

            // Inizializzo i searchCriteria di questa rule
            Map<String, List<String>> searchCriteriaBase = initSearchCriteria(rule, batch.getEnte(), batch.getAoo());
            if (token == null)
                token = blFacade.login();

            // Per ogni query_fascicolo
            for (int i=0; i < rule.getQuery_fascicolo().size(); i++) {
                String queryFasc = rule.getQuery_fascicolo().get(i);

                // Inizializzo i contatori di questa rule
                int recordsFound = 0;
                int recordsInserted = 0;

                // Aggiungo i criteri per ciascuna query_fascicolo all'interno della rule
                Map<String, List<String>> searchCriteria = CriteriaUtil.addQueryFilters(searchCriteriaBase, queryFasc);

                List<CMFascicolo> listFasc = new ArrayList<CMFascicolo>();

                // Vado avanti a chunks finché ce ne sono
                List<ISearchItem> results = blFacade.getFascicoliChunk(token, searchCriteria);
                while (results.size() > 0) {
                    recordsFound += results.size();

                    for (ISearchItem item : results) {
                        CMFascicolo fasc = new CMFascicolo();
                        fasc.setCodEnte(batch.getEnte());
                        fasc.setCodAoo(batch.getAoo());
                        fasc.setAnnoFascicolo(CriteriaUtil.extractField("ANNO_FASCICOLO", item));
                        fasc.setProgrFascicolo(CriteriaUtil.extractField("PROGR_FASCICOLO", item));
                        fasc.setClassifica(CriteriaUtil.extractField("CLASSIFICA", item));
                        String aclExplicit = CriteriaUtil.extractField("ACL_EXPLICIT", item).replace(" ", "");
                        fasc.setAclCurrent(aclExplicit);
                        fasc.setAclModified(aclExplicit);
                        fasc.setRules(AclUtil.makeRuleId(rule.getUniqueKey()));

                        listFasc.add(fasc);
                    }

                    // Incremento data di creazione e riparto con la ricerca
                    ISearchItem lastItem = results.get(results.size() - 1);
                    CriteriaUtil.incrementCreated(lastItem, searchCriteria);

                    // Prossimo chunk
                    results = blFacade.getFascicoliChunk(token, searchCriteria);
                }

                // Inserisco nel db, alla prima botta svuoto prima la tabella
                recordsInserted += cmFascicoliDAO.insert(listFasc, ++ruleCount == 1);

                log.info("Query " + String.valueOf(i+1));
                log.info("Fascicoli trovati: " + recordsFound + " - inseriti: " + recordsInserted + " - gia' presenti: " + String.valueOf(recordsFound - recordsInserted));
            }
        }
    }

    /**
     * Carica i titolari nel db
     */
    private void loadTitolari() throws DocerException {
        log.info("--------------------------------------------");
        log.info("Inizio carico titolari...");

        String token = null;
        int ruleCount = 0;

        // Per ogni rule
        for (Rule rule : batch.getRules()) {
            if (rule.getQuery_titolario() == null)
                continue;

            log.info("RULE: " + rule.getUniqueKey());

            // Inizializzo i searchCriteria di questa rule
            Map<String, List<String>> searchCriteriaBase = initSearchCriteria(rule, batch.getEnte(), batch.getAoo());
            if (token == null)
                token = blFacade.login();

            // Per ogni query_titolario
            for (int i=0; i < rule.getQuery_titolario().size(); i++) {
                String queryTit = rule.getQuery_titolario().get(i);

                // Inizializzo i contatori per ciascuna query
                int recordsFound = 0;
                int recordsInserted = 0;

                // Aggiungo i criteri per ciascuna query_titolario all'interno della rule
                Map<String, List<String>> searchCriteria = CriteriaUtil.addQueryFilters(searchCriteriaBase, queryTit);

                List<CMTitolario> listTit = new ArrayList<CMTitolario>();

                // Vado avanti a chunks finché ce ne sono
                List<ISearchItem> results = blFacade.getTitolariChunk(token, searchCriteria);
                while (results.size() > 0) {
                    recordsFound += results.size();

                    for (ISearchItem item : results) {
                        CMTitolario tit = new CMTitolario();
                        tit.setCodEnte(batch.getEnte());
                        tit.setCodAoo(batch.getAoo());
                        tit.setClassifica(CriteriaUtil.extractField("CLASSIFICA", item));
                        String aclExplicit = CriteriaUtil.extractField("ACL_EXPLICIT", item).replace(" ", "");
                        tit.setAclCurrent(aclExplicit);
                        tit.setAclModified(aclExplicit);
                        tit.setRules(AclUtil.makeRuleId(rule.getUniqueKey()));

                        listTit.add(tit);
                    }

                    // Incremento data di creazione e riparto con la ricerca
                    ISearchItem lastItem = results.get(results.size() - 1);
                    CriteriaUtil.incrementCreated(lastItem, searchCriteria);

                    // Prossimo chunk
                    results = blFacade.getTitolariChunk(token, searchCriteria);
                }

                // Inserisco nel db, alla prima botta svuoto prima la tabella
                recordsInserted += cmTitolariDAO.insert(listTit, ++ruleCount == 1);

                log.info("Query " + String.valueOf(i+1));
                log.info("Titolari trovati: " + recordsFound + " - inseriti: " + recordsInserted + " - gia' presenti: " + String.valueOf(recordsFound - recordsInserted));
            }
        }
    }

    /**
     * Effettua le modifiche relative ai gruppi sul db per i documenti
     */
    private void updateGroupDocumenti() throws DocerException {
        log.info("--------------------------------------------");
        log.info("Inizio update Acl Documenti su db...");

        //String oldAcl = "[SYS_ADMINS@group:fullAccess,admins@group:normalAccess]";
        for (Rule rule : batch.getRules()) {
            if (rule.getQuery_documento() == null)
                continue;

            Groups groupsSource =  mapGroupsSource.get(rule.getUniqueKey());
            Groups groupsTarget =  mapGroupsTarget.get(rule.getUniqueKey());

            List<CMDocumento> listDoc = cmDocumentiDAO.getByRule(AclUtil.makeRuleId(rule.getUniqueKey()));

                // Azione REMOVE
            if (IConst.ACTION_REMOVE.equalsIgnoreCase(rule.getAction())) {
                // Per ogni record
                for (CMDocumento doc : listDoc) {
                    String newAcl = calculateAclRemove(doc, groupsSource);
                    doc.setAclModified(newAcl);
                }
                // Scrivo sul db
                cmDocumentiDAO.update(listDoc);

            }  // Azione ADD
            else if (IConst.ACTION_ADD.equalsIgnoreCase(rule.getAction())) {
                // Per ogni record
                for (CMDocumento doc : listDoc) {
                    String newAcl = calculateAclAdd(doc, groupsTarget);
                    doc.setAclModified(newAcl);
                }
                // Scrivo sul db
                cmDocumentiDAO.update(listDoc);

            }  // Azione CHANGE
            else if (IConst.ACTION_CHANGE.equalsIgnoreCase(rule.getAction())) {
                // Per il change ci deve essere un solo gruppo source e un solo gruppo target
                String source = groupsSource.getGroup().get(0).getValue() + IConst.GROUP;
                Group targetGroup = groupsTarget.getGroup().get(0);

                // Per ogni record
                for (CMDocumento doc : listDoc) {
                    String newAcl = calculateAclChange(doc, source, targetGroup);
                    doc.setAclModified(newAcl);
                }
                // Scrivo sul db
                cmDocumentiDAO.update(listDoc);

            } // Azione SPLIT
            else if (IConst.ACTION_SPLIT.equalsIgnoreCase(rule.getAction())) {
                // Per lo split ci deve essere un solo gruppo source e uno o più gruppi target
                String source = groupsSource.getGroup().get(0).getValue() + IConst.GROUP;

                // Per ogni record
                for (CMDocumento doc : listDoc) {
                    String newAcl = calculateAclSplit(doc, source, groupsTarget);
                    doc.setAclModified(newAcl);
                }
                // Scrivo sul db
                cmDocumentiDAO.update(listDoc);

            }  // Azione FUSION
            else if (IConst.ACTION_FUSION.equalsIgnoreCase(rule.getAction())) {
                // Per la fusion ci deve essere uno o più gruppi source e un solo gruppo target
                Group targetGroup = groupsTarget.getGroup().get(0);

                // Per ogni record
                for (CMDocumento doc : listDoc) {
                    String newAcl = calculateAclFusion(doc, targetGroup, groupsSource);
                    doc.setAclModified(newAcl);
                }
                // Scrivo sul db
                cmDocumentiDAO.update(listDoc);
            }
        }

        log.info("Fine update Acl Documenti.");
    }

    /**
     * Effettua le modifiche relative ai gruppi sul db per i fascicoli
     */
    private void updateGroupFascicoli() throws DocerException {
        log.info("--------------------------------------------");
        log.info("Inizio update Acl Fascicoli su db...");

        for (Rule rule : batch.getRules()) {
            if (rule.getQuery_fascicolo() == null)
                continue;

            Groups groupsSource =  mapGroupsSource.get(rule.getUniqueKey());
            Groups groupsTarget =  mapGroupsTarget.get(rule.getUniqueKey());

            List<CMFascicolo> listFasc = cmFascicoliDAO.getAllByRule(AclUtil.makeRuleId(rule.getUniqueKey()));

            // Azione REMOVE
            if (IConst.ACTION_REMOVE.equalsIgnoreCase(rule.getAction())) {
                // Per ogni record
                for (CMFascicolo fasc : listFasc) {
                    String newAcl = calculateAclRemove(fasc, groupsSource);
                    fasc.setAclModified(newAcl);
                }
                // Scrivo sul db
                cmFascicoliDAO.update(listFasc);

            }  // Azione ADD
            else if (IConst.ACTION_ADD.equalsIgnoreCase(rule.getAction())) {
                // Per ogni record
                for (CMFascicolo fasc : listFasc) {
                    String newAcl = calculateAclAdd(fasc, groupsTarget);
                    fasc.setAclModified(newAcl);
                }
                // Scrivo sul db
                cmFascicoliDAO.update(listFasc);

            }  // Azione CHANGE
            else if (IConst.ACTION_CHANGE.equalsIgnoreCase(rule.getAction())) {
                // Per il change ci deve essere un solo gruppo source e un solo gruppo target
                String source = groupsSource.getGroup().get(0).getValue() + IConst.GROUP;
                Group targetGroup = groupsTarget.getGroup().get(0);

                // Per ogni record
                for (CMFascicolo fasc : listFasc) {
                    String newAcl = calculateAclChange(fasc, source, targetGroup);
                    fasc.setAclModified(newAcl);
                }
                // Scrivo sul db
                cmFascicoliDAO.update(listFasc);

            } // Azione SPLIT
            else if (IConst.ACTION_SPLIT.equalsIgnoreCase(rule.getAction())) {
                // Per lo split ci deve essere un solo gruppo source e uno o più gruppi target
                String source = groupsSource.getGroup().get(0).getValue() + IConst.GROUP;

                // Per ogni record
                for (CMFascicolo fasc : listFasc) {
                    String newAcl = calculateAclSplit(fasc, source, groupsTarget);
                    fasc.setAclModified(newAcl);
                }
                // Scrivo sul db
                cmFascicoliDAO.update(listFasc);

            }  // Azione FUSION
            else if (IConst.ACTION_FUSION.equalsIgnoreCase(rule.getAction())) {
                // Per la fusion ci deve essere uno o più gruppi source e un solo gruppo target
                Group targetGroup = groupsTarget.getGroup().get(0);

                // Per ogni record
                for (CMFascicolo doc : listFasc) {
                    String newAcl = calculateAclFusion(doc, targetGroup, groupsSource);
                    doc.setAclModified(newAcl);
                }
                // Scrivo sul db
                cmFascicoliDAO.update(listFasc);
            }
        }

        log.info("Fine update Acl Fascicoli.");
    }

    /**
     * Effettua le modifiche relative ai gruppi sul db per i titolari
     */
    private void updateGroupTitolari() throws DocerException {
        log.info("--------------------------------------------");
        log.info("Inizio update Acl Titolari su db...");

        for (Rule rule : batch.getRules()) {
            if (rule.getQuery_titolario() == null)
                continue;

            Groups groupsSource =  mapGroupsSource.get(rule.getUniqueKey());
            Groups groupsTarget =  mapGroupsTarget.get(rule.getUniqueKey());

            List<CMTitolario> listTit = cmTitolariDAO.getAllByRule(AclUtil.makeRuleId(rule.getUniqueKey()));

            // Azione REMOVE
            if (IConst.ACTION_REMOVE.equalsIgnoreCase(rule.getAction())) {
                // Per ogni record
                for (CMTitolario tit : listTit) {
                    String newAcl = calculateAclRemove(tit, groupsSource);
                    tit.setAclModified(newAcl);
                }
                // Scrivo sul db
                cmTitolariDAO.update(listTit);

            }  // Azione ADD
            else if (IConst.ACTION_ADD.equalsIgnoreCase(rule.getAction())) {
                // Per ogni record
                for (CMTitolario tit : listTit) {
                    String newAcl = calculateAclAdd(tit, groupsTarget);
                    tit.setAclModified(newAcl);
                }
                // Scrivo sul db
                cmTitolariDAO.update(listTit);

            }  // Azione CHANGE
            else if (IConst.ACTION_CHANGE.equalsIgnoreCase(rule.getAction())) {
                // Per il change ci deve essere un solo gruppo source e un solo gruppo target
                String source = groupsSource.getGroup().get(0).getValue() + IConst.GROUP;
                Group targetGroup = groupsTarget.getGroup().get(0);

                // Per ogni record
                for (CMTitolario tit : listTit) {
                    String newAcl = calculateAclChange(tit, source, targetGroup);
                    tit.setAclModified(newAcl);
                }
                // Scrivo sul db
                cmTitolariDAO.update(listTit);

            } // Azione SPLIT
            else if (IConst.ACTION_SPLIT.equalsIgnoreCase(rule.getAction())) {
                // Per lo split ci deve essere un solo gruppo source e uno o più gruppi target
                String source = groupsSource.getGroup().get(0).getValue() + IConst.GROUP;

                // Per ogni record
                for (CMTitolario tit : listTit) {
                    String newAcl = calculateAclSplit(tit, source, groupsTarget);
                    tit.setAclModified(newAcl);
                }
                // Scrivo sul db
                cmTitolariDAO.update(listTit);

            }  // Azione FUSION
            else if (IConst.ACTION_FUSION.equalsIgnoreCase(rule.getAction())) {
                // Per la fusion ci deve essere uno o più gruppi source e un solo gruppo target
                Group targetGroup = groupsTarget.getGroup().get(0);

                // Per ogni record
                for (CMTitolario tit : listTit) {
                    String newAcl = calculateAclFusion(tit, targetGroup, groupsSource);
                    tit.setAclModified(newAcl);
                }
                // Scrivo sul db
                cmTitolariDAO.update(listTit);
            }
        }

        log.info("Fine update Acl Titolari.");
    }

    /**
     * Scrive su Solr le nuove permission per i documenti
     */
    private void sendGroupDocumenti() throws DocerException {
        log.info("--------------------------------------------");
        log.info("Inizio scrittura Acl Documenti su Solr...");

        List<CMDocumento> listDoc = cmDocumentiDAO.getByRule(null);

        String token = blFacade.login();

        // Per ogni record setto i permessi su Solr
        int recordCount = 0;
        for (CMDocumento doc : listDoc) {
            Map<String, EnumACLRights> mapAcl = AclUtil.calculateAclForSolr(doc.getAclModified());
            if (blFacade.setAclDocumento(token, doc.getDocnum(), mapAcl))
                recordCount++;
            else {
                // Fallita la scrittura su SolR
                cmDocumentiDAO.flagInError(doc, 1);
            }
        }

        log.info("Fine scrittura Acl Documenti su Solr: " + recordCount + " record aggiornati.");
    }

    /**
     * Scrive su Solr le nuove permission per i documenti
     */
    private void sendGroupFascicoli() throws DocerException {
        log.info("--------------------------------------------");
        log.info("Inizio scrittura Acl Fascicoli su Solr...");

        List<CMFascicolo> listFasc = cmFascicoliDAO.getAllByRule(null);

        String token = blFacade.login();

        // Per ogni record setto i permessi su Solr
        int recordCount = 0;
        for (CMFascicolo fasc : listFasc) {
            Map<String, EnumACLRights> mapAcl = AclUtil.calculateAclForSolr(fasc.getAclModified());
            if (blFacade.setAclFascicolo(token, fasc.getCodEnte(), fasc.getCodAoo(), fasc.getAnnoFascicolo(), fasc.getProgrFascicolo(), fasc.getClassifica(), pianoClass, mapAcl))
                recordCount++;
            else {
                // Fallita la scrittura su SolR
                cmFascicoliDAO.flagInError(fasc, 1);
            }
        }

        log.info("Fine scrittura Acl Fascicoli su Solr: " + recordCount + " record aggiornati.");
    }

    /**
     * Scrive su Solr le nuove permission per i titolari
     */
    private void sendGroupTitolari() throws DocerException {
        log.info("--------------------------------------------");
        log.info("Inizio scrittura Acl Titolari su Solr...");

        List<CMTitolario> listTit = cmTitolariDAO.getAllByRule(null);

        String token = blFacade.login();

        // Per ogni record setto i permessi su Solr
        int recordCount = 0;
        for (CMTitolario tit : listTit) {
            Map<String, EnumACLRights> mapAcl = AclUtil.calculateAclForSolr(tit.getAclModified());
            if (blFacade.setAclTitolario(token, tit.getCodEnte(), tit.getCodAoo(), tit.getClassifica(), pianoClass, mapAcl))
                recordCount++;
            else {
                // Fallita la scrittura su SolR
                cmTitolariDAO.flagInError(tit, 1);
            }
        }

        log.info("Fine scrittura Acl Titolari su Solr: " + recordCount + " record aggiornati.");
    }

    /**
     * Calcola la nuova Acl per la REMOVE
     * @param rec
     * @return
     */
    private String calculateAclRemove(CMBase rec, Groups groupsSource) {
        String oldAcl = rec.getAclModified();
        List<KeyValuePair> aclList = AclUtil.splitAclExplicit(oldAcl);
        if (aclList.size() == 0)
            return "";

        // Per ogni gruppo source
        for (Group group : groupsSource.getGroup()) {
            String source = group.getValue() + IConst.GROUP;
            aclList = AclUtil.deleteGroup(source, aclList);
        }

        return AclUtil.compactAclExplicit(aclList);
    }

    /**
     * Calcola la nuova Acl per la ADD
     * @param rec
     * @return
     */
    private String calculateAclAdd(CMBase rec, Groups groupsTarget) {
        String acl = rec.getAclModified();

        // Per ogni gruppo source
        for (Group group : groupsTarget.getGroup()) {
            String permission = !StringUtils.isEmpty(group.getRights()) ? group.getRights() : EnumACLRights.fullAccess.toString();
            String source = group.getValue() + IConst.GROUP +  ":" + permission;
            acl = AclUtil.addGroup(source, acl);
        }

        return acl;
    }

    /**
     * Calcola la nuova Acl per il CHANGE
     * @param rec
     * @param source
     * @param target
     * @return
     */
    private String calculateAclChange(CMBase rec, String source, Group target) {
        String oldAcl = rec.getAclModified();
        return AclUtil.replaceGroup(source, target, oldAcl);
    }

    /**
     * Calcola la nuova Acl per lo SPLIT
     * @param rec
     * @param source
     * @return
     */
    private String calculateAclSplit(CMBase rec, String source, Groups groupsTarget) {
        String oldAcl = rec.getAclModified();
        List<KeyValuePair> oldAclList = AclUtil.splitAclExplicit(oldAcl);
        String permission = AclUtil.findGroupPermission(source, oldAclList);

        // Elimino il gruppo source
        oldAcl = AclUtil.deleteGroup(source, oldAcl);

        // Inserisco la stringa con i nuovi gruppi
        String target = AclUtil.buildSplitTarget(groupsTarget.getGroup(), permission);
        return AclUtil.addGroup(target, oldAcl);
    }

    /**
     * Calcola la nuova Acl per la FUSION
     * @param rec
     * @param target
     * @return
     */
    private String calculateAclFusion(CMBase rec, Group target, Groups groupsSource) {
        String oldAcl = rec.getAclModified();
        //List<KeyValuePair> oldAclList = AclUtil.splitAclExplicit(oldAcl);

        // Trovo il permesso più ampio dei gruppi da fondere (se non è stato indicato esplicitamente)
        String permission = !StringUtils.isEmpty(target.getRights()) ? target.getRights()
                : AclUtil.findBestPermission(groupsSource.getGroup(), oldAcl);

        // Elimino i gruppi source
        for (Group group : groupsSource.getGroup()) {
            String source = group.getValue() + IConst.GROUP;
            oldAcl = AclUtil.deleteGroup(source, oldAcl);
        }

        // Aggiungo il gruppo nuovo con la permission calcolata
        return AclUtil.addGroup(target.getValue() + IConst.GROUP + ":" + permission, oldAcl);
    }

    /**
     *
     * @param rule
     * @return
     */
    private Map<String, List<String>> initSearchCriteria(Rule rule, String ente, String aoo) throws DocerException {
        Map<String, List<String>> searchCriteria = CriteriaUtil.initSearchCriteria(rule, ente, aoo);

        // Aggiungo il piano di classificazione se c'è
        if (!StringUtils.isEmpty(pianoClass)) {
            List<String> values = new ArrayList<String>();
            values.add(pianoClass);
            searchCriteria.put("PIANO_CLASS", values);
        }

        return searchCriteria;
    }

}
