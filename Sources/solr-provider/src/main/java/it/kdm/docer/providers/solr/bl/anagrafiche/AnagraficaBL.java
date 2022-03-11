package it.kdm.docer.providers.solr.bl.anagrafiche;

import it.kdm.docer.providers.solr.SolrBaseUtil;
import it.kdm.docer.providers.solr.bl.Fields;
import it.kdm.docer.sdk.EnumACLRights;
import it.kdm.docer.sdk.EnumBoolean;
import it.kdm.docer.sdk.EnumSearchOrder;
import it.kdm.docer.sdk.classes.*;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.docer.sdk.interfaces.*;
import it.kdm.solr.client.SolrClient;
import it.kdm.utils.DataTable;
import it.kdm.utils.exceptions.DataTableException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.NamedList;

import java.util.*;

/**
 * Created by microchip on 04/05/15.
 */
public class AnagraficaBL extends SolrBaseUtil {

    static Logger log = org.slf4j.LoggerFactory.getLogger(AnagraficaBL.class.getName());

    public AnagraficaBL() throws DocerException{
        super();
    }


    public EnumACLRights getEffectiveRightsAnagrafiche(ILoggedUserInfo currentUser, String type, Map<String, String> id, String userid) throws DocerException {
        List<String> returnProps = new ArrayList<String>();
        returnProps.add("*");
        returnProps.add(Fields.Folder.EFFECTIVE_RIGHTS);

        Map<String, List<String>> criteria = new HashMap<String, List<String>>();
        for (String key : id.keySet()) {
            if (id.get(key) != null) {
                criteria.put(key, Collections.singletonList(id.get(key)));
                /*String value = id.get(key);
                if ("fascicolo".equalsIgnoreCase(type) && "CLASSIFICA".equals(key) && value.contains("$")){
                    String cls = value.split("\\$")[0];
                    String piano = value.split("\\$")[1];
                    criteria.put(key, Collections.singletonList(cls));
                    criteria.put("PIANO_CLASS",Collections.singletonList(piano));
                } else {
                    criteria.put(key, Collections.singletonList(id.get(key)));
                }*/

            }
        }

        if (currentUser.isPrefixed() && userid!=null && !userid.equals("admin") && currentUser.getCodiceEnte()!=null && !userid.startsWith(currentUser.getPrefix()))
            //userid = currentUser.getCodiceEnte()+"__"+userid;
            userid = currentUser.getPrefix()+userid;

        Map<String,String> params = new HashMap<String, String>();
        params.put("userId", userid);

        SolrDocumentList result = solrSearch(currentUser, type, criteria, null, returnProps,1,null,params,null);

        if (result.size()>0) {
            SolrDocument rec = result.get(0);
            String rights = (String)rec.getFirstValue(Fields.Documento.EFFECTIVE_RIGHTS);

            return convertSolrACLRights(rights);
        }

        throw new DocerException("Impossibile recuperare gli EffectiveRights. Anagrafica '"+type+"' non trovata.");
    }

    public EnumACLRights getEffectiveRightsFolder(ILoggedUserInfo currentUser, String folderId, String userid) throws DocerException {
        List<String> returnProps = new ArrayList<String>();
        returnProps.add("*");
        returnProps.add(Fields.Folder.EFFECTIVE_RIGHTS);

        Map<String, List<String>> criteria = new HashMap<String, List<String>>();
        criteria.put(Fields.Folder.SYSTEM_ID, Collections.singletonList(folderId));

        Map<String,String> params = new HashMap<String, String>();

        if (currentUser.isPrefixed() && userid!=null && !userid.equals("admin") && currentUser.getCodiceEnte()!=null && !userid.startsWith(currentUser.getPrefix()))
            userid = currentUser.getPrefix()+userid;
            //userid = currentUser.getCodiceEnte()+"__"+userid;

        params.put("userId", userid);

        SolrDocumentList result = solrSearch(currentUser, TYPE_FOLDER, criteria, null, returnProps,1,null,params,null);

        if (result.size()>0) {
            SolrDocument rec = result.get(0);
            String rights = (String)rec.getFirstValue(Fields.Documento.EFFECTIVE_RIGHTS);

            return convertSolrACLRights(rights);
        }

        throw new DocerException("Impossibile recuperare gli EffectiveRights. Folder non trovato.");
    }


    public Map<String, EnumACLRights> getACLFolder(ILoggedUserInfo currentUser, String folderId) throws DocerException {
        SolrDocument doc = getSolrObjectByFieldId(currentUser, TYPE_FOLDER, folderId, Fields.Folder.SYSTEM_ID);
        String solrId = (String)doc.get(Fields.Folder.SOLR_ID);

        Collection<Object> aclExplicit = doc.getFieldValues(Fields.Fascicolo.ACL);

        return convertSolrACLToMap(aclExplicit);
    }

    public void setACLFolder(ILoggedUserInfo currentUser, String folderId, Map<String, EnumACLRights> acls) throws DocerException {
        SolrDocument doc = getSolrObjectByFieldId(currentUser, TYPE_FOLDER, folderId, Fields.Folder.SYSTEM_ID);
        String solrId = (String)doc.get(Fields.Folder.SOLR_ID);

        List<String> solrAcl = convertMapToSolrACL(currentUser,acls);
        SolrInputDocument metadata = new SolrInputDocument();

        for (String acl : solrAcl)
            metadata.addField(Fields.Fascicolo.ACL, acl);

        solrUpdate(currentUser, solrId, metadata);
    }

    public Map<String, EnumACLRights> getACLTitolario(ILoggedUserInfo currentUser, ITitolarioId titolarioId) throws DocerException {
        String solrId = getSolrId(titolarioId);
        SolrDocument doc = getSolrObject(currentUser, solrId);
        Collection<Object> aclExplicit = doc.getFieldValues(Fields.Fascicolo.ACL);

        return convertSolrACLToMap(aclExplicit);
    }

    public void setACLTitolario(ILoggedUserInfo currentUser, ITitolarioId titolarioId, Map<String, EnumACLRights> acls) throws DocerException {
        String solrId = getSolrId(titolarioId);

        List<String> solrAcl = convertMapToSolrACL(currentUser,acls);
        SolrInputDocument metadata = new SolrInputDocument();

        for (String acl : solrAcl)
            metadata.addField(Fields.Fascicolo.ACL, acl);

        if (solrAcl.size()==0)
            metadata.setField(Fields.Documento.ACL, Collections.singletonMap("set",null));

        solrUpdate(currentUser, solrId, metadata);
    }

    public Map<String, EnumACLRights> getACLFascicolo(ILoggedUserInfo currentUser, IFascicoloId fascicoloId) throws DocerException {
        String solrId = getSolrId(fascicoloId);
        SolrDocument doc = getSolrObject(currentUser, solrId);
        Collection<Object> aclExplicit = doc.getFieldValues(Fields.Fascicolo.ACL);

        return convertSolrACLToMap(aclExplicit);
    }

    public void setACLFascicolo(ILoggedUserInfo currentUser, IFascicoloId fascicoloId, Map<String, EnumACLRights> acls) throws DocerException {
        String solrId = getSolrId(fascicoloId);

        List<String> solrAcl = convertMapToSolrACL(currentUser,acls);
        SolrInputDocument metadata = new SolrInputDocument();

        for (String acl : solrAcl)
            metadata.addField(Fields.Fascicolo.ACL, acl);

        if (solrAcl.size()==0)
            metadata.setField(Fields.Documento.ACL, Collections.singletonMap("set",null));

        solrUpdate(currentUser, solrId, metadata);
    }

    public void createEnte(ILoggedUserInfo currentUser, IEnteInfo enteInfo) throws DocerException {

        if (isNullOrEmpty(enteInfo.getCodiceEnte()))
            throw new DocerException("Impossible creare l'Ente. Codice Ente non impostato.");

        IEnteId enteId = new EnteId();
        enteId.setCodiceEnte(enteInfo.getCodiceEnte());

        String solrId = getSolrId(enteId);

        //convertire info in metadata
        SolrInputDocument metadata = new SolrInputDocument();

        if (enteInfo.getCodiceEnte()!=null)
            metadata.addField(Fields.Ente.COD_ENTE, enteInfo.getCodiceEnte());

        if (enteInfo.getDescrizione()!=null)
            metadata.addField(Fields.Ente.DES_ENTE, enteInfo.getDescrizione());

        if (enteInfo.getEnabled()!=null && !enteInfo.getEnabled().equals(EnumBoolean.UNSPECIFIED))
            metadata.addField(Fields.Ente.ENABLED, enteInfo.getEnabled().equals(EnumBoolean.TRUE));

        //set solr parent
        metadata.setField(Fields.Base.SOLR_PARENT, this.getLocation() + "!@location");

        metadata = buildExtraInfo(metadata, enteInfo.getExtraInfo());
        //TODO: rimappare errore se già esiste???
        //TODO: settare ACL in creazione???

        solrInsert(currentUser, solrId, metadata);
    }

    public void createAoo(ILoggedUserInfo currentUser, IAOOInfo aooInfo) throws DocerException {

        if (isNullOrEmpty(aooInfo.getCodiceEnte()))
            throw new DocerException("Impossible creare l'AOO. Codice Ente non impostato.");

        if (isNullOrEmpty(aooInfo.getCodiceAOO()))
            throw new DocerException("Impossible creare l'AOO. Codice Aoo non impostato.");

        IAOOId aooId = new AOOId();
        aooId.setCodiceEnte(aooInfo.getCodiceEnte());
        aooId.setCodiceAOO(aooInfo.getCodiceAOO());

        String solrId = getSolrId(aooId);

        //convertire info in metadata
        SolrInputDocument metadata = new SolrInputDocument();

        if (aooInfo.getCodiceEnte()!=null)
            metadata.addField(Fields.Aoo.COD_ENTE, aooInfo.getCodiceEnte());

        if (aooInfo.getDescrizione()!=null)
            metadata.addField(Fields.Aoo.DES_AOO, aooInfo.getDescrizione());

        if (aooInfo.getCodiceAOO()!=null)
            metadata.addField(Fields.Aoo.COD_AOO, aooInfo.getCodiceAOO());

        if (aooInfo.getEnabled()!=null && !aooInfo.getEnabled().equals(EnumBoolean.UNSPECIFIED))
            metadata.addField(Fields.Aoo.ENABLED, aooInfo.getEnabled().equals(EnumBoolean.TRUE));

        metadata = buildExtraInfo(metadata, aooInfo.getExtraInfo());
        //TODO: rimappare errore se già esiste???
        //TODO: settare ACL in creazione???

        //set solr parent
        IEnteId enteId = new EnteId();
        enteId.setCodiceEnte(aooId.getCodiceEnte());
        String solrParent = getSolrId(enteId);
        metadata.setField(Fields.Base.SOLR_PARENT, solrParent);

        solrInsert(currentUser, solrId, metadata);
    }

    public void createTitolario(ILoggedUserInfo currentUser, ITitolarioInfo titolarioInfo) throws DocerException {

        if (isNullOrEmpty(titolarioInfo.getCodiceEnte()))
            throw new DocerException("Impossible creare il Titolario. Codice Ente non impostato.");

        if (isNullOrEmpty(titolarioInfo.getCodiceAOO()))
            throw new DocerException("Impossible creare il Titolario. Codice Aoo non impostato.");

        if (isNullOrEmpty(titolarioInfo.getClassifica()))
            throw new DocerException("Impossible creare il Titolario. Classifica non impostato.");

        ITitolarioId titId = new TitolarioId();
        titId.setCodiceEnte(titolarioInfo.getCodiceEnte());
        titId.setCodiceAOO(titolarioInfo.getCodiceAOO());
        titId.setClassifica(titolarioInfo.getClassifica());
        // DOCER-36 Piano di classificazione
        if (!StringUtils.isEmpty(titolarioInfo.getPianoClassificazione()))
            titId.setPianoClassificazione(titolarioInfo.getPianoClassificazione());

        String solrId = getSolrId(titId);

        //convertire info in metadata
        SolrInputDocument metadata = new SolrInputDocument();

        if (titolarioInfo.getCodiceEnte()!=null)
            metadata.addField(Fields.Titolario.COD_ENTE, titolarioInfo.getCodiceEnte());

        if (titolarioInfo.getDescrizione()!=null)
            metadata.addField(Fields.Titolario.DES_TITOLARIO, titolarioInfo.getDescrizione());

        if (titolarioInfo.getCodiceAOO()!=null)
            metadata.addField(Fields.Titolario.COD_AOO, titolarioInfo.getCodiceAOO());

        if (titolarioInfo.getClassifica()!=null)
            metadata.addField(Fields.Titolario.CLASSIFICA, titolarioInfo.getClassifica());

        if (titolarioInfo.getParentClassifica()!=null)
            metadata.addField(Fields.Titolario.PARENT_CLASSIFICA, titolarioInfo.getParentClassifica());

        if (titolarioInfo.getEnabled()!=null && !titolarioInfo.getEnabled().equals(EnumBoolean.UNSPECIFIED))
            metadata.addField(Fields.Titolario.ENABLED, titolarioInfo.getEnabled().equals(EnumBoolean.TRUE));

        // DOCER-36
        if (titolarioInfo.getPianoClassificazione()!=null)
            metadata.addField(Fields.Titolario.PIANO_CLASS, titolarioInfo.getPianoClassificazione());

        //metadato "classifica_sort" per gestire l'ordinamento delle classifiche
        metadata.addField(Fields.Titolario.CLASSIFICA_SORT, paddingClassifica(titolarioInfo.getClassifica()));

        metadata = buildExtraInfo(metadata, titolarioInfo.getExtraInfo());
        //TODO: rimappare errore se già esiste???
        //TODO: settare ACL in creazione???

        //set solr parent
//        IAOOId aooId = new AOOId();
//        aooId.setCodiceEnte(titolarioInfo.getCodiceEnte());
//        aooId.setCodiceAOO(titolarioInfo.getCodiceAOO());
//        String solrParent = getSolrId(aooId);
//        metadata.setField(Fields.Base.SOLR_PARENT, solrParent);

        solrInsert(currentUser, solrId, metadata);
    }

    public void createFascicolo(ILoggedUserInfo currentUser, IFascicoloInfo fascicoloInfo) throws DocerException {

        if (isNullOrEmpty(fascicoloInfo.getCodiceEnte()))
            throw new DocerException("Impossible creare il Fascicolo. Codice Ente non impostato.");

        if (isNullOrEmpty(fascicoloInfo.getCodiceAOO()))
            throw new DocerException("Impossible creare il Fascicolo. Codice Aoo non impostato.");

        if (isNullOrEmpty(fascicoloInfo.getAnnoFascicolo()))
            throw new DocerException("Impossible creare il Fascicolo. Anno Fascicolo non impostato.");

        if (isNullOrEmpty(fascicoloInfo.getClassifica()))
            throw new DocerException("Impossible creare il Fascicolo. Classifica non impostato.");

        IFascicoloId fascId = new FascicoloId();
        fascId.setCodiceEnte(fascicoloInfo.getCodiceEnte());
        fascId.setCodiceAOO(fascicoloInfo.getCodiceAOO());
        fascId.setClassifica(fascicoloInfo.getClassifica());
        fascId.setAnnoFascicolo(fascicoloInfo.getAnnoFascicolo());
        fascId.setProgressivo(fascicoloInfo.getProgressivo());
        // DOCER-36 Piano di classificazione
        if (!StringUtils.isEmpty(fascicoloInfo.getPianoClassificazione()))
            fascId.setPianoClassificazione(fascicoloInfo.getPianoClassificazione());

        String solrId = getSolrId(fascId);

        //convertire info in metadata
        SolrInputDocument metadata = new SolrInputDocument();

        if (fascicoloInfo.getCodiceEnte()!=null)
            metadata.addField(Fields.Fascicolo.COD_ENTE, fascicoloInfo.getCodiceEnte());

        if (fascicoloInfo.getCodiceAOO()!=null)
            metadata.addField(Fields.Fascicolo.COD_AOO, fascicoloInfo.getCodiceAOO());

        // DOCER-36 Piano di classificazione
        if (fascicoloInfo.getPianoClassificazione()!=null)
            metadata.addField(Fields.Fascicolo.PIANO_CLASS, fascicoloInfo.getPianoClassificazione());

        if (fascicoloInfo.getClassifica()!=null)
            metadata.addField(Fields.Fascicolo.CLASSIFICA, fascicoloInfo.getClassifica());

        if (fascicoloInfo.getAnnoFascicolo()!=null)
            metadata.addField(Fields.Fascicolo.ANNO_FASCICOLO, fascicoloInfo.getAnnoFascicolo());

        if (fascicoloInfo.getProgressivo()!=null)
            metadata.addField(Fields.Fascicolo.PROGRESSIVO, fascicoloInfo.getProgressivo());

        if (fascicoloInfo.getParentProgressivo()!=null)
            metadata.addField(Fields.Fascicolo.PARENT_PROGRESSIVO, fascicoloInfo.getParentProgressivo());
        //TODO: campo numFascicolo deve essere gestito dal mapping di solr???
        if (fascicoloInfo.getNumeroFascicolo()!=null)
            metadata.addField(Fields.Fascicolo.NUM_FASCICOLO, fascicoloInfo.getNumeroFascicolo());

        if (fascicoloInfo.getDescrizione()!=null)
            metadata.addField(Fields.Fascicolo.DES_FASCICOLO, fascicoloInfo.getDescrizione());

        if (fascicoloInfo.getEnabled()!=null && !fascicoloInfo.getEnabled().equals(EnumBoolean.UNSPECIFIED))
            metadata.addField(Fields.Fascicolo.ENABLED, fascicoloInfo.getEnabled().equals(EnumBoolean.TRUE));

        metadata = buildExtraInfo(metadata, fascicoloInfo.getExtraInfo());
        //TODO: rimappare errore se già esiste???
        //TODO: settare ACL in creazione???
        //TODO: verificare campo [FULLPATH] di solr perché forse è sbagliato???

        //set solr parent
        if (fascicoloInfo.getParentProgressivo()!=null && !fascicoloInfo.getParentProgressivo().equalsIgnoreCase("")) {
            //il parent è un fascicolo
            IFascicoloId fId = new FascicoloId();
            fId.setCodiceEnte(fascicoloInfo.getCodiceEnte());
            fId.setCodiceAOO(fascicoloInfo.getCodiceAOO());
            fId.setClassifica(fascicoloInfo.getClassifica());
            fId.setProgressivo(fascicoloInfo.getParentProgressivo());
            fId.setAnnoFascicolo(fascicoloInfo.getAnnoFascicolo());
            // DOCER-36 Piano di classificazione
            if (!StringUtils.isEmpty(fascicoloInfo.getPianoClassificazione()))
                fId.setPianoClassificazione(fascicoloInfo.getPianoClassificazione());

            String solrParent = getSolrId(fId);

            metadata.setField(Fields.Base.SOLR_PARENT,solrParent);
        } else {
            //il parent è un titolario
            ITitolarioId titId = new TitolarioId();
            titId.setCodiceEnte(fascicoloInfo.getCodiceEnte());
            titId.setCodiceAOO(fascicoloInfo.getCodiceAOO());
            titId.setClassifica(fascicoloInfo.getClassifica());
            // DOCER-36 Piano di classificazione
            if (!StringUtils.isEmpty(fascicoloInfo.getPianoClassificazione()))
                titId.setPianoClassificazione(fascicoloInfo.getPianoClassificazione());

            String solrParent = getSolrId(titId);

            metadata.setField(Fields.Base.SOLR_PARENT,solrParent);
        }



        solrInsert(currentUser,solrId, metadata);
    }

    public String createFolder(ILoggedUserInfo currentUser, IFolderInfo folderInfo) throws DocerException {

        //TODO: implementare anche la gestione isPublic???
        if (folderInfo.getFolderName() == null || folderInfo.getFolderName().equals("")) {
            throw new DocerException("Impossibile creare il folder. FOLDER_NAME obbligatorio");
        }

        if (folderInfo.getCodiceEnte() == null)
            throw new DocerException("Impossibile creare il folder. COD_ENTE obbligatorio");

        if (folderInfo.getCodiceAOO() == null)
            throw new DocerException("Impossibile creare il folder. COD_AOO obbligatorio");

        if (folderInfo.getFolderId() != null) {
            throw new DocerException("Impossibile creare il folder. FOLDER_ID non puo' essere assegnato dall'esterno");
        }

        //convertire info in metadata
        SolrInputDocument metadata = new SolrInputDocument();

        if (folderInfo.getCodiceEnte()!=null)
            metadata.addField(Fields.Folder.COD_ENTE, folderInfo.getCodiceEnte());

        if (folderInfo.getCodiceAOO()!=null)
            metadata.addField(Fields.Folder.COD_AOO, folderInfo.getCodiceAOO());

        if (folderInfo.getDescrizione()!=null)
            metadata.addField(Fields.Folder.DES_FOLDER, folderInfo.getDescrizione());

        if (folderInfo.getFolderName()!=null)
            metadata.addField(Fields.Folder.FOLDER_NAME, folderInfo.getFolderName());

        if (folderInfo.getFolderOwner()!=null)
            metadata.addField(Fields.Folder.FOLDER_OWNER, folderInfo.getFolderOwner());

        if (folderInfo.getParentFolderId()!=null)
            metadata.addField(Fields.Folder.PARENT_FOLDER_ID, folderInfo.getParentFolderId());

        //TODO: verificare se settare solo se non è null???
        if (folderInfo.getEnabled()!=null && !folderInfo.getEnabled().equals(EnumBoolean.UNSPECIFIED))
            metadata.addField(Fields.Ente.ENABLED, folderInfo.getEnabled().equals(EnumBoolean.TRUE));

        metadata = buildExtraInfo(metadata, folderInfo.getExtraInfo());
        //TODO: manca la gestione per far generare l'id a solr

        //set solr parent
        if (folderInfo.getParentFolderId() != null && !folderInfo.getParentFolderId().equalsIgnoreCase("")) {
            //il parent è un folder
            IFolderInfo fId = new FolderInfo();
            fId.setCodiceAOO(folderInfo.getCodiceAOO());
            fId.setCodiceEnte(folderInfo.getCodiceEnte());
            fId.setFolderId(folderInfo.getParentFolderId());
            String solrParent = getSolrId(folderInfo.getParentFolderId(), fId);

            metadata.setField(Fields.Base.SOLR_PARENT, solrParent);
        } else {
            //il parent è una AOO
            IAOOId aooId = new AOOId();
            aooId.setCodiceEnte(folderInfo.getCodiceEnte());
            aooId.setCodiceAOO(folderInfo.getCodiceAOO());
            String solrParent = getSolrId(aooId);

            metadata.setField(Fields.Base.SOLR_PARENT, solrParent);
        }


        NamedList<?> nameValue = solrInsert(currentUser, TYPE_FOLDER, metadata);
        String solrId = (String)((SolrDocument)nameValue.get("processAdd")).get("id");
        //String folderId = (String)nameValue.get(Fields.Folder.FOLDER_ID);
        String folderId = extractIdItemFromSolrId(solrId);

        return folderId;
    }

    public NamedList<?> createCustomItem(ILoggedUserInfo currentUser, ICustomItemInfo customItemInfo) throws DocerException {

        if (isNullOrEmpty(customItemInfo.getCodiceEnte()))
            throw new DocerException("Impossibile creare il customItem. COD_ENTE obbligatorio");

        if (isNullOrEmpty(customItemInfo.getCodiceAOO()))
            throw new DocerException("Impossibile creare il customItem. COD_AOO obbligatorio");

        if (isNullOrEmpty(customItemInfo.getType()))
            throw new DocerException("Impossibile creare il customItem. TYPE obbligatorio");

        if (isNullOrEmpty(customItemInfo.getCodiceCustom()))
            throw new DocerException("Impossibile creare il customItem. CODICE_CUSTOM obbligatorio");

        ICustomItemId custId = new CustomItemId();
        custId.setCodiceCustom(customItemInfo.getCodiceCustom());
        custId.setCodiceAOO(customItemInfo.getCodiceAOO());
        custId.setCodiceEnte(customItemInfo.getCodiceEnte());
        custId.setType(customItemInfo.getType());

//        String solrId = getSolrId(custId);

        //convertire info in metadata
        SolrInputDocument metadata = new SolrInputDocument();

        if (customItemInfo.getCodiceEnte()!=null)
            metadata.addField(Fields.CustomItem.COD_ENTE, customItemInfo.getCodiceEnte());

        if (customItemInfo.getCodiceAOO()!=null)
            metadata.addField(Fields.CustomItem.COD_AOO, customItemInfo.getCodiceAOO());

        if (customItemInfo.getType()!=null)
            metadata.addField(Fields.CustomItem.TYPE, customItemInfo.getType());

        if (customItemInfo.getCodiceCustom()!=null)
            metadata.addField("sid", customItemInfo.getCodiceCustom());
            //metadata.addField("COD_"+custId.getType(), customItemInfo.getCodiceCustom());

        if (customItemInfo.getDescrizione()!=null)
            metadata.addField("name", customItemInfo.getDescrizione());
            //metadata.addField("DES_"+custId.getType(), customItemInfo.getDescrizione());

        if (customItemInfo.getEnabled()!=null && !customItemInfo.getEnabled().equals(EnumBoolean.UNSPECIFIED))
            metadata.addField(Fields.CustomItem.ENABLED, customItemInfo.getEnabled().equals(EnumBoolean.TRUE));

        metadata = buildExtraInfo(metadata, customItemInfo.getExtraInfo());

        if (customItemInfo.getExtraInfo()!=null && customItemInfo.getExtraInfo().containsKey("PARENT_COD_"+custId.getType()))     {
            custId.setCodiceCustom(customItemInfo.getExtraInfo().get("PARENT_COD_"+custId.getType()));
            String solrParent = getSolrId(custId);

            metadata.setField(Fields.Base.SOLR_PARENT,solrParent);
        } else {
            //il parent è una AOO
            IAOOId aooId = new AOOId();
            aooId.setCodiceEnte(custId.getCodiceEnte());
            aooId.setCodiceAOO(custId.getCodiceAOO());
            String solrParent = getSolrId(aooId);

            metadata.setField(Fields.Base.SOLR_PARENT, solrParent);
        }

        return solrInsert(currentUser, customItemInfo.getType(), metadata);
    }

    public NamedList<?> updateCustomItem(ILoggedUserInfo currentUser, ICustomItemId customItemId, ICustomItemInfo customItemInfo) throws DocerException {
//        String solrId = getSolrId(customItemId);

        //convertire info in metadata
        SolrInputDocument metadata = new SolrInputDocument();

        // MODIFICA 06-11-2015 -- Deve essere valorizzato l'id altrimenti la update fallisce
        /*if (customItemInfo.getCodiceEnte()!=null)
            metadata.addField(Fields.CustomItem.COD_ENTE, customItemInfo.getCodiceEnte());

        if (customItemInfo.getCodiceAOO()!=null)
            metadata.addField(Fields.CustomItem.COD_AOO, customItemInfo.getCodiceAOO());

        if (customItemInfo.getCodiceCustom()!=null)
            metadata.addField("sid", customItemInfo.getCodiceCustom());
        //metadata.addField("COD_"+customItemId.getType(), customItemInfo.getCodiceCustom()); */
        if (customItemId.getCodiceEnte()!=null)
            metadata.addField(Fields.CustomItem.COD_ENTE, customItemId.getCodiceEnte());
        if (customItemId.getCodiceAOO()!=null)
            metadata.addField(Fields.CustomItem.COD_AOO, customItemId.getCodiceAOO());
        if (customItemId.getType()!=null)
            metadata.addField(Fields.CustomItem.TYPE, customItemId.getType());
        if (customItemId.getCodiceCustom()!=null)
            metadata.addField("sid", customItemId.getCodiceCustom());
        // FINE MODIFICA

        if (customItemInfo.getDescrizione()!=null)
            metadata.addField("name", customItemInfo.getDescrizione());
            //metadata.addField("DES_"+customItemId.getType(), customItemInfo.getDescrizione());

        if (customItemInfo.getEnabled()!=null && !customItemInfo.getEnabled().equals(EnumBoolean.UNSPECIFIED))
            metadata.addField(Fields.CustomItem.ENABLED, customItemInfo.getEnabled().equals(EnumBoolean.TRUE));

        metadata = buildExtraInfo(metadata, customItemInfo.getExtraInfo());
        //TODO: verificare se corretto impostare la classifica con i blank (per il solrId)


        if (customItemInfo.getExtraInfo()!=null && customItemInfo.getExtraInfo().containsKey("PARENT_COD_"+customItemId.getType())) {
            String parent = customItemInfo.getExtraInfo().get("PARENT_COD_" + customItemId.getType());
            if (!isNullOrEmpty(parent)) {
                ICustomItemId custId = new CustomItemId();
                custId.setCodiceEnte(customItemId.getCodiceEnte());
                custId.setCodiceAOO(customItemId.getCodiceAOO());
                custId.setType(customItemId.getType());
                custId.setCodiceCustom(customItemInfo.getExtraInfo().get("PARENT_COD_" + customItemId.getType()));
                String solrParent = getSolrId(custId);

                metadata.setField(Fields.Base.SOLR_PARENT, solrParent);
            } else {
                //il parent è una AOO
                IAOOId aooId = new AOOId();
                aooId.setCodiceEnte(customItemId.getCodiceEnte());
                aooId.setCodiceAOO(customItemId.getCodiceAOO());
                String solrParent = getSolrId(aooId);

                metadata.setField(Fields.Base.SOLR_PARENT, solrParent);
            }
        }

        return solrUpdate(currentUser, customItemId.getType(), metadata);
    }

    public NamedList<?> updateFascicolo(ILoggedUserInfo currentUser, IFascicoloId fascicoloId, IFascicoloInfo fascicoloInfo) throws DocerException {
        String solrId = getSolrId(fascicoloId);

        //convertire info in metadata
        SolrInputDocument metadata = new SolrInputDocument();

        if (fascicoloInfo.getCodiceEnte()!=null)
            metadata.addField(Fields.Fascicolo.COD_ENTE, fascicoloInfo.getCodiceEnte());

        if (fascicoloInfo.getCodiceAOO()!=null)
            metadata.addField(Fields.Fascicolo.COD_AOO, fascicoloInfo.getCodiceAOO());

        if (fascicoloInfo.getClassifica()!=null)
            metadata.addField(Fields.Fascicolo.CLASSIFICA, fascicoloInfo.getClassifica());

        if (fascicoloInfo.getAnnoFascicolo()!=null)
            metadata.addField(Fields.Fascicolo.ANNO_FASCICOLO, fascicoloInfo.getAnnoFascicolo());

        if (fascicoloInfo.getProgressivo()!=null)
            metadata.addField(Fields.Fascicolo.PROGRESSIVO, fascicoloInfo.getProgressivo());

        if (fascicoloInfo.getParentProgressivo()!=null)
            metadata.addField(Fields.Fascicolo.PARENT_PROGRESSIVO, fascicoloInfo.getParentProgressivo());

        if (fascicoloInfo.getNumeroFascicolo()!=null)
            metadata.addField(Fields.Fascicolo.NUM_FASCICOLO, fascicoloInfo.getNumeroFascicolo());

        if (fascicoloInfo.getDescrizione()!=null)
            metadata.addField(Fields.Fascicolo.DES_FASCICOLO, fascicoloInfo.getDescrizione());

        if (fascicoloInfo.getEnabled()!=null && !fascicoloInfo.getEnabled().equals(EnumBoolean.UNSPECIFIED))
            metadata.addField(Fields.Fascicolo.ENABLED, fascicoloInfo.getEnabled().equals(EnumBoolean.TRUE));

        metadata = buildExtraInfo(metadata, fascicoloInfo.getExtraInfo());


        //set solr parent

        //se è presente un valore o blank per parent-progressivo reimposta il parent di solr altrimenti no
        if (fascicoloInfo.getParentProgressivo()!=null) {
            if (!fascicoloInfo.getParentProgressivo().equalsIgnoreCase("")) {
                //il parent è un fascicolo
                IFascicoloId fId = new FascicoloId();
                fId.setCodiceEnte(fascicoloId.getCodiceEnte());
                fId.setCodiceAOO(fascicoloId.getCodiceAOO());
                fId.setClassifica(fascicoloId.getClassifica());
                fId.setProgressivo(fascicoloInfo.getParentProgressivo());
                fId.setAnnoFascicolo(fascicoloId.getAnnoFascicolo());
                // DOCER-36 Piano di classificazione
                if (!StringUtils.isEmpty(fascicoloId.getPianoClassificazione()))
                    fId.setPianoClassificazione(fascicoloId.getPianoClassificazione());

                String solrParent = getSolrId(fId);

                metadata.setField(Fields.Base.SOLR_PARENT, solrParent);
            } else {
                //il parent è un titolario
                ITitolarioId titId = new TitolarioId();
                titId.setCodiceEnte(fascicoloId.getCodiceEnte());
                titId.setCodiceAOO(fascicoloId.getCodiceAOO());
                titId.setClassifica(fascicoloId.getClassifica());
                // DOCER-36 Piano di classificazione
                if (!StringUtils.isEmpty(fascicoloId.getPianoClassificazione()))
                    titId.setPianoClassificazione(fascicoloId.getPianoClassificazione());

                String solrParent = getSolrId(titId);

                metadata.setField(Fields.Base.SOLR_PARENT, solrParent);
            }
        }

        return solrUpdate(currentUser, solrId, metadata);
    }

    public NamedList<?> updateTitolario(ILoggedUserInfo currentUser, ITitolarioId titolarioId, ITitolarioInfo titolarioInfo) throws DocerException {
        String solrId = getSolrId(titolarioId);

        //convertire info in metadata
        SolrInputDocument metadata = new SolrInputDocument();

        if (titolarioInfo.getCodiceEnte()!=null)
            metadata.addField(Fields.Titolario.COD_ENTE, titolarioInfo.getCodiceEnte());

        if (titolarioInfo.getCodiceAOO()!=null)
            metadata.addField(Fields.Titolario.COD_AOO, titolarioInfo.getCodiceAOO());

        if (titolarioInfo.getClassifica()!=null)
            metadata.addField(Fields.Titolario.CLASSIFICA, titolarioInfo.getClassifica());

        if (titolarioInfo.getParentClassifica()!=null)
            metadata.addField(Fields.Titolario.PARENT_CLASSIFICA, titolarioInfo.getParentClassifica());

        if (titolarioInfo.getDescrizione()!=null)
            metadata.addField(Fields.Titolario.DES_TITOLARIO, titolarioInfo.getDescrizione());

        //TODO: campo COD_TITOLARIO va gestito in update???
//        if (titolarioInfo.getCodiceTitolario()!=null)
//            metadata.addField(Fields.Titolario.COD_TITOLARIO, titolarioInfo.getCodiceTitolario());

        if (titolarioInfo.getEnabled()!=null && !titolarioInfo.getEnabled().equals(EnumBoolean.UNSPECIFIED))
            metadata.addField(Fields.Titolario.ENABLED, titolarioInfo.getEnabled().equals(EnumBoolean.TRUE));

        //metadato "classifica_sort" per gestire l'ordinamento delle classifiche
        metadata.addField(Fields.Titolario.CLASSIFICA_SORT, paddingClassifica(titolarioId.getClassifica()));

        metadata = buildExtraInfo(metadata, titolarioInfo.getExtraInfo());

//        //set solr parent
//        IAOOId aooId = new AOOId();
//        aooId.setCodiceEnte(titolarioId.getCodiceEnte());
//        aooId.setCodiceAOO(titolarioId.getCodiceAOO());
//        String solrParent = getSolrId(aooId);
//        metadata.setField(Fields.Base.SOLR_PARENT, solrParent);

        return solrUpdate(currentUser, solrId, metadata);
    }

    public NamedList<?> updateAOO(ILoggedUserInfo currentUser, IAOOId aooId, IAOOInfo aooInfo) throws DocerException {
        String solrId = getSolrId(aooId);

        //convertire info in metadata
        SolrInputDocument metadata = new SolrInputDocument();

        if (aooInfo.getCodiceEnte()!=null)
            metadata.addField(Fields.Aoo.COD_ENTE, aooInfo.getCodiceEnte());

        if (aooInfo.getDescrizione()!=null)
            metadata.addField(Fields.Aoo.DES_AOO, aooInfo.getDescrizione());

        if (aooInfo.getEnabled()!=null && !aooInfo.getEnabled().equals(EnumBoolean.UNSPECIFIED))
            metadata.addField(Fields.Aoo.ENABLED, aooInfo.getEnabled().equals(EnumBoolean.TRUE));

        metadata = buildExtraInfo(metadata, aooInfo.getExtraInfo());

        //set solr parent
        IEnteId enteId = new EnteId();
        enteId.setCodiceEnte(aooId.getCodiceEnte());
        String solrParent = getSolrId(enteId);
        metadata.setField(Fields.Base.SOLR_PARENT, solrParent);

        return solrUpdate(currentUser, solrId, metadata);
    }

    public NamedList<?> updateEnte(ILoggedUserInfo currentUser, IEnteId enteId, IEnteInfo enteNewInfo) throws DocerException {
        String solrId = getSolrId(enteId);

        //convertire info in metadata
        SolrInputDocument metadata = new SolrInputDocument();

        if (enteNewInfo.getCodiceEnte()!=null)
            metadata.addField(Fields.Ente.COD_ENTE, enteNewInfo.getCodiceEnte());

        if (enteNewInfo.getDescrizione()!=null)
            metadata.addField(Fields.Ente.DES_ENTE, enteNewInfo.getDescrizione());

        if (enteNewInfo.getEnabled()!=null && !enteNewInfo.getEnabled().equals(EnumBoolean.UNSPECIFIED))
            metadata.addField(Fields.Ente.ENABLED, enteNewInfo.getEnabled().equals(EnumBoolean.TRUE));

        metadata = buildExtraInfo(metadata, enteNewInfo.getExtraInfo());

        //set solr parent
        //TODO: verificare se giusta la location presa cosi visto che in SolrUpdate c'e' un retrieve dell'object
        metadata.setField(Fields.Base.SOLR_PARENT, this.getLocation() + "!@location");

        return solrUpdate(currentUser, solrId, metadata);
    }

    public NamedList<?> updateFolder(ILoggedUserInfo currentUser, String folderId, IFolderInfo folderNewInfo) throws DocerException {

        if (isNullOrEmpty(folderId))
            throw new DocerException("Impossibile eseguire la modifica del folder. FolderId non impostato");

        List<String> returnProps = new ArrayList<String>();
        returnProps.add(Fields.Folder.SOLR_ID);
        returnProps.add(Fields.Folder.COD_AOO);
        returnProps.add(Fields.Folder.COD_ENTE);
        returnProps.add(Fields.Folder.PARENT_FOLDER_ID);

        SolrDocument solrDoc = getSolrObjectByFieldId(currentUser, TYPE_FOLDER, folderId, Fields.Folder.SYSTEM_ID, returnProps);
        String solrId = (String)solrDoc.get(Fields.Folder.SOLR_ID);
        String codAoo = (String)solrDoc.get(Fields.Folder.COD_AOO);
        String codEnte = (String)solrDoc.get(Fields.Folder.COD_ENTE);
        String parentFolderId = "";

        if (solrDoc.containsKey(Fields.Folder.PARENT_FOLDER_ID)) {
            parentFolderId = (String)solrDoc.get(Fields.Folder.PARENT_FOLDER_ID);
        }

        //convertire info in metadata
        SolrInputDocument metadata = new SolrInputDocument();

        if (folderNewInfo.getCodiceEnte()!=null)
            metadata.addField(Fields.Folder.COD_ENTE, folderNewInfo.getCodiceEnte());

        if (folderNewInfo.getDescrizione()!=null)
            metadata.addField(Fields.Folder.DES_FOLDER, folderNewInfo.getDescrizione());

        if (folderNewInfo.getFolderName()!=null)
            metadata.addField(Fields.Folder.FOLDER_NAME, folderNewInfo.getFolderName());

        if (folderNewInfo.getFolderId()!=null)
            metadata.addField(Fields.Folder.FOLDER_ID, folderNewInfo.getFolderId());

        if (folderNewInfo.getFolderOwner()!=null)
            metadata.addField(Fields.Folder.FOLDER_OWNER, folderNewInfo.getFolderOwner());

        if (folderNewInfo.getParentFolderId()!=null)
            metadata.addField(Fields.Folder.PARENT_FOLDER_ID, folderNewInfo.getParentFolderId());

        //TODO: verificare se settare solo se non è null???
        if (folderNewInfo.getEnabled()!=null && !folderNewInfo.getEnabled().equals(EnumBoolean.UNSPECIFIED))
            metadata.addField(Fields.Ente.ENABLED, folderNewInfo.getEnabled().equals(EnumBoolean.TRUE));

        metadata = buildExtraInfo(metadata, folderNewInfo.getExtraInfo());

        //set solr parent
        //se è valorizzato parentFolder o è blank reimposta il parent solr
        if (folderNewInfo.getParentFolderId() != null) {
            if (!folderNewInfo.getParentFolderId().equalsIgnoreCase("")) {
                //il parent è un folder
                IFolderInfo fInfo = new FolderInfo();
                fInfo.setCodiceAOO(codAoo);
                fInfo.setCodiceEnte(codEnte);
                fInfo.setFolderId(folderNewInfo.getParentFolderId());
                String solrParent = getSolrId(folderNewInfo.getParentFolderId(), fInfo);

                metadata.setField(Fields.Base.SOLR_PARENT, solrParent);
            } else {
                //il parent è una AOO
                IAOOId aooId = new AOOId();
                aooId.setCodiceEnte(codEnte);
                aooId.setCodiceAOO(codAoo);
                String solrParent = getSolrId(aooId);

                metadata.setField(Fields.Base.SOLR_PARENT, solrParent);
            }
        }

        return solrUpdate(currentUser, solrId, metadata);
    }

    public NamedList<?> deleteFolder(ILoggedUserInfo currentUser, String folderId) throws DocerException {
        List<String> returnProps = Collections.singletonList(Fields.Base.SOLR_ID);
        SolrDocument solrDoc = getSolrObjectByFieldId(currentUser, TYPE_FOLDER, folderId, Fields.Folder.SYSTEM_ID, returnProps);
        String solrId = (String)solrDoc.get(Fields.Base.SOLR_ID);
        return solrDeleteById(currentUser, solrId);
    }

    public List<Map<String, String>> search(ILoggedUserInfo currentUser, String type, Map<String, List<String>> searchCriteria, List<String> returnProperties) throws DocerException {
        log.debug("----------------------------------------------------------------------------------------------");
        log.debug("METODO: search");
        log.debug("----------------------------------------------------------------------------------------------");

        SolrDocumentList results = solrSearch(currentUser, type, searchCriteria, null, returnProperties, -1, null);

        //converte solr results
        List<Map<String, String>> mapList = this.convertSolrToMapList(results);

        return mapList;
    }

    public List<Map<String, String>> search(ILoggedUserInfo currentUser, String type, Map<String, List<String>> searchCriteria, List<String> returnProperties, int maxResults, Map<String, EnumSearchOrder> orderBy) throws DocerException {
        log.debug("----------------------------------------------------------------------------------------------");
        log.debug("METODO: search");
        log.debug("----------------------------------------------------------------------------------------------");

        SolrDocumentList results = solrSearch(currentUser, type, searchCriteria, null, returnProperties, maxResults, orderBy);

        //converte solr results
        List<Map<String, String>> mapList = this.convertSolrToMapList(results);

        return mapList;

    }

    public DataTable<String> searchFolders(ILoggedUserInfo currentUser,Map<String, List<String>> searchCriteria, List<String> returnProperties, int maxResults, Map<String, EnumSearchOrder> orderBy) throws DocerException {
        log.debug("----------------------------------------------------------------------------------------------");
        log.debug("METODO: searchFolders");
        log.debug("----------------------------------------------------------------------------------------------");

        SolrDocumentList results = solrSearch(currentUser, TYPE_FOLDER, searchCriteria, null, returnProperties, maxResults, orderBy);

        //converte solr results
        try {

            DataTable<String> table = this.convertSolrToDataTable(results,returnProperties);

            return table;
        } catch (DataTableException e) {
            throw new DocerException(e);
        }

    }

    public List<Map<String, String>> getRiferimentiFascicolo(ILoggedUserInfo currentUser, IFascicoloId fascicoloId) throws DocerException {

        String solrId = getSolrId(fascicoloId);
        SolrDocument solrDoc = getSolrObject(currentUser, solrId);

        Collection<Object> riferimenti = solrDoc.getFieldValues(Fields.Documento.RIFERIMENTI);

        List<String> rifId = new ArrayList<String>();
        if (riferimenti!=null) {
            for (Object rif : riferimenti) {
                rifId.add(rif.toString());
            }
        }

        //recupera i riferimenti degli altri doc
        String q = "";
        q += SolrClient.Query.makeClause(Fields.Documento.RIFERIMENTI, solrId);

        List<String> returnProperties = new ArrayList<String>();
        returnProperties.add(Fields.Fascicolo.SOLR_ID);

        SolrDocumentList rifList = solrSearchByQuery(currentUser, q, returnProperties, -1, null);

        for (SolrDocument r : rifList) {
            if (!rifId.contains(r.getFieldValue(Fields.Fascicolo.SOLR_ID).toString()))
                rifId.add(r.getFieldValue(Fields.Fascicolo.SOLR_ID).toString());
        }

        List<Map<String, String>> out = new ArrayList<Map<String, String>>();

        for(String id : rifId){
            q = "";
            q += SolrClient.Query.makeClause(Fields.Fascicolo.SOLR_ID, id);
            returnProperties = new ArrayList<String>();
            returnProperties.add(Fields.Fascicolo.ANNO_FASCICOLO);
            returnProperties.add(Fields.Fascicolo.PROGRESSIVO);
            returnProperties.add(Fields.Fascicolo.COD_ENTE);
            returnProperties.add(Fields.Fascicolo.COD_AOO);
            returnProperties.add(Fields.Fascicolo.CLASSIFICA);
            returnProperties.add(Fields.Fascicolo.DES_FASCICOLO);
            returnProperties.add(Fields.Fascicolo.ENABLED);
            returnProperties.add(Fields.Fascicolo.PARENT_PROGRESSIVO);

            SolrDocumentList fascList = solrSearchByQuery(currentUser, q, returnProperties, -1, null);
            if (fascList.size() != 1)
                throw new DocerException("Trovati " + fascList.size() + " risultati");

            //converte solr results
            List<Map<String, String>> mapList = this.convertSolrToMapList(fascList);
            out.addAll(mapList);

        }

        return out;

    }

    public void addRiferimentiFascicolo(ILoggedUserInfo currentUser, IFascicoloId fascicoloId, List<IFascicoloId> riferimenti) throws DocerException {
        String solrId = getSolrId(fascicoloId);
        SolrDocument solrDoc = getSolrObject(currentUser, solrId);

        Collection<Object> fasc_rif = solrDoc.getFieldValues(Fields.Documento.RIFERIMENTI);

        if (fasc_rif==null)
            fasc_rif = new ArrayList<Object>();

        for (IFascicoloId rif : riferimenti) {
            String solrRifId = getSolrId(rif);

            if (!fasc_rif.contains(solrRifId)) {
                fasc_rif.add(solrRifId);
            }
        }

        //update solr object
        SolrInputDocument metadata = new SolrInputDocument();
        metadata.setField(Fields.Documento.RIFERIMENTI, fasc_rif);

        solrUpdate(currentUser, solrId, metadata);
    }

    public void removeRiferimentiFascicolo(ILoggedUserInfo currentUser, IFascicoloId fascicoloId, List<IFascicoloId> riferimenti) throws DocerException {
        String solrId = getSolrId(fascicoloId);
        SolrDocument solrDoc = getSolrObject(currentUser, solrId);

        Collection<Object> fasc_rif = solrDoc.getFieldValues(Fields.Documento.RIFERIMENTI);

        if (!(fasc_rif==null || fasc_rif.size()==0)) {

            for (IFascicoloId rif : riferimenti) {
                String solrRifId = getSolrId(rif);

                if (fasc_rif.contains(solrRifId)) {
                    fasc_rif.remove(solrRifId);
                }
            }

            //update solr object
            SolrInputDocument metadata = new SolrInputDocument();
            if (fasc_rif.size() == 0)
                metadata.setField(Fields.Documento.RIFERIMENTI, Collections.singletonMap("set", null));
            else
                metadata.setField(Fields.Documento.RIFERIMENTI, fasc_rif);

            solrUpdate(currentUser, solrId, metadata);
        }


        //Controllo sui riferimenti passati in input se hanno un riferimento al fascicolo principale
        //Potrebbe essere presente un riferimento al contrario; cioè da un elemento della lista passata in input al fascicolo principale passato in input
        for (IFascicoloId rif : riferimenti) {
            String solrRifId = getSolrId(rif);
            SolrDocument solrDocRif = getSolrObject(currentUser, solrRifId);
            Collection<Object> fascRifRif = solrDocRif.getFieldValues(Fields.Documento.RIFERIMENTI);

            if(fascRifRif!=null) {
                if (fascRifRif.contains(solrId)) {
                    fascRifRif.remove(solrId);
                    SolrInputDocument metarif = new SolrInputDocument();
                    if (fascRifRif.size() == 0)
                        metarif.setField(Fields.Documento.RIFERIMENTI, Collections.singletonMap("set", null));
                    else
                        metarif.addField(Fields.Documento.RIFERIMENTI, fascRifRif.toArray());


                    solrUpdate(currentUser, solrRifId, metarif);
                }
            }


        }


    }

}
