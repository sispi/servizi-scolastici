package it.kdm.docer.providers.solr6;

import it.kdm.docer.providers.solr6.bl.Fields;
import it.kdm.docer.providers.solr6.bl.sicurezza.AttoriBL;
import it.kdm.docer.sdk.EnumACLRights;
import it.kdm.docer.sdk.EnumBoolean;
import it.kdm.docer.sdk.EnumSearchOrder;
import it.kdm.docer.sdk.classes.*;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.docer.sdk.interfaces.*;
import it.kdm.utils.DataRow;
import it.kdm.utils.DataTable;
import org.slf4j.Logger;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Assert;
import org.junit.Test;
import sun.misc.UUDecoder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * Created by microchip on 28/04/15.
 */
public class ProviderTest extends SolrBaseUtil {

    static Logger log = org.slf4j.LoggerFactory.getLogger(ProviderTest.class.getName());

    String ente;
    String aoo;
    String group = "GROUP TEST";
    String classifica = "CLASSIFICA SOLR TEST";
    String parentClassifica = "PARENT CLASSIFICA SOLR TEST";


    String username;
    String pwd;

    Provider p;
    ILoggedUserInfo currentUser;

    public ProviderTest() throws DocerException {

        Properties config = new Provider().config;

        ente = config.getProperty("unittest.ente");
        aoo = config.getProperty("unittest.aoo");
        username = config.getProperty("unittest.username");
        pwd = config.getProperty("unittest.pwd");

        System.setProperty("zkHost", config.getProperty("unittest.zkHost") );
    }

    @Test
    public void login() throws DocerException {
        p = new Provider();

        String ticket = p.login(username,pwd,ente);

        currentUser = new LoggedUserInfo();
        currentUser.setCodiceEnte(ente);
        currentUser.setTicket(ticket);
        currentUser.setUserId(username);

        p.setCurrentUser(currentUser);

        log.debug("--------------------------------------------------");
        log.debug("Provider: initialized");
        log.debug("Usename: " + currentUser.getUserId());
        log.debug("ticket: " + currentUser.getTicket());
        log.debug("--------------------------------------------------");
    }

    @Test
    public void initDocer() throws DocerException {
        //String ente = "ENTEPROVA";

        p = new Provider();

        //String ticket = p.login(username,pwd,ente);

        currentUser = new LoggedUserInfo();
        currentUser.setCodiceEnte(ente);
        currentUser.setTicket("admin");
        currentUser.setUserId(username);

        p.setCurrentUser(currentUser);

        p.solrDelete(currentUser,null,"COD_ENTE",ente);
        //p.createLocation(currentUser,"DOCAREA");

        //crea l'ente e l'aoo docer
        IEnteInfo enteInfo = new EnteInfo();
        enteInfo.setCodiceEnte(ente);
        enteInfo.setDescrizione("Descrizione ENTE SOLR");
        enteInfo.setEnabled(EnumBoolean.TRUE);

        p.createEnte(enteInfo);

        IAOOInfo aooInfo = new AOOInfo();
        aooInfo.setCodiceEnte(enteInfo.getCodiceEnte());
        aooInfo.setCodiceAOO("AOOPROVA");
        aooInfo.setDescrizione("Descrizione AOO SOLR");
        aooInfo.setEnabled(EnumBoolean.TRUE);

        p.createAOO(aooInfo);


        /*IGroupProfileInfo gInfo = new GroupProfileInfo();
        gInfo.setGroupId("everyone");
        gInfo.setGroupName("everyone");
        gInfo.setEnabled(EnumBoolean.TRUE);
        //gInfo.setExtraInfo(Collections.singletonMap("COD_ENTE", enteInfo.getCodiceEnte()));

        p.createGroup(gInfo);


        IUserProfileInfo uInfo = new UserProfileInfo();
        uInfo.setUserId("admin");
        uInfo.setUserPassword("admin");
        uInfo.setEmailAddress("admin@kdm.it");
        uInfo.setFullName("Administrator");
        uInfo.setFirstName("Utente");
        uInfo.setLastName("Admin");
        uInfo.getExtraInfo().put("COD_ENTE",ente);
        uInfo.getExtraInfo().put("COD_AOO",aoo);
        uInfo.setEnabled(EnumBoolean.TRUE);

        p.createUser(uInfo);



        IGroupProfileInfo gInfo2 = new GroupProfileInfo();
        gInfo2.setGroupId("admins");
        gInfo2.setGroupName("admins");
        gInfo2.setEnabled(EnumBoolean.TRUE);
        //gInfo2.setExtraInfo(Collections.singletonMap("COD_ENTE", enteInfo.getCodiceEnte()));

        p.createGroup(gInfo2);

        IGroupProfileInfo gInfo3 = new GroupProfileInfo();
        gInfo3.setGroupId("SYS_ADMINS");
        gInfo3.setGroupName("SYS_ADMINS");
        gInfo3.setEnabled(EnumBoolean.TRUE);
        //gInfo3.setExtraInfo(Collections.singletonMap("COD_ENTE", enteInfo.getCodiceEnte()));

        p.createGroup(gInfo3);

        IGroupProfileInfo gInfo4 = new GroupProfileInfo();
        gInfo4.setGroupId("ALFRESCO_ADMINISTRATORS");
        gInfo4.setGroupName("ALFRESCO_ADMINISTRATORS");
        gInfo4.setEnabled(EnumBoolean.TRUE);

        p.createGroup(gInfo4);

        p.addGroupsToUser(uInfo.getUserId(), Collections.singletonList(gInfo2.getGroupId()));
        p.addGroupsToUser(uInfo.getUserId(), Collections.singletonList(gInfo3.getGroupId()));
        p.addGroupsToUser(uInfo.getUserId(), Collections.singletonList(gInfo4.getGroupId()));*/

        IFolderInfo folder = new FolderInfo();
        folder.setFolderName("Cartella "+aooInfo.getDescrizione());
        folder.setCodiceEnte(aooInfo.getCodiceEnte());
        folder.setCodiceAOO(aooInfo.getCodiceAOO());

        p.createFolder(folder);
    }

    @Test
    public void createTestUser() throws DocerException {
        login();

        _createUser();
    }

    @Test
    public void testGetUserOrGroup() throws DocerException {
        login();
        List<String> userGroup = new ArrayList<String>();
        userGroup.add("stefano.vigna");
        userGroup.add("everyone");

        AttoriBL manager = new AttoriBL();
        Map<String,String> ret = manager.getUserOrGroup(currentUser,userGroup);


    }

    @Test
    public void testCreateDocHistory() {
        log.info("--------------------------------------------------");
        log.info("START ALL TESTS");
        log.info("--------------------------------------------------");

        try {
            login();
            log.info("Test: login OK");
        } catch (DocerException e) {
            log.info("Test: login ERROR");
        }

        String docNum = null;
        try {
            IAOOInfo aoo = new AOOInfo();
            aoo.setCodiceAOO("C_F704_AOO");
            aoo.setCodiceEnte("C_F704");

            docNum = _createDocument(aoo);
            log.info("Test: createDocument OK");
        } catch (DocerException e) {
            log.info("Test: createDocument ERROR");
        }
    }

    @Test
    public void testALL() {
        log.info("--------------------------------------------------");
        log.info("START ALL TESTS");
        log.info("--------------------------------------------------");

        try {
            login();
            log.info("Test: login OK");
        } catch (DocerException e) {
            log.info("Test: login ERROR");
        }

        IEnteInfo ente = null;
        try {
            ente = _createEnte(UUID.randomUUID().toString());
            log.info("Test: createEnte OK");
        } catch (DocerException e) {
            log.info("Test: createEnte ERROR");
            log.info("IMPOSSIBILE CONTINUARE CON I TEST. SESSIONE DI TEST INTERRROTTA!");
            return;
        }

        IAOOInfo aoo = null;
        try {
            aoo = _createAoo(ente);
            log.info("Test: createAoo OK");
        } catch (DocerException e) {
            log.info("Test: createAoo ERROR");
            log.info("IMPOSSIBILE CONTINUARE CON I TEST. SESSIONE DI TEST INTERRROTTA!");
        }

        ITitolarioInfo tit = null;
        try {
            tit = _createTitolario(aoo);
            log.info("Test: createTitolario OK");
        } catch (DocerException e) {
            log.info("Test: createTitolario ERROR");
            log.info("IMPOSSIBILE CONTINUARE CON I TEST. SESSIONE DI TEST INTERRROTTA!");
        }

        IFascicoloInfo fasc = null;
        try {
            fasc = _createFascicolo(tit);
            log.info("Test: createFascicolo OK");
        } catch (DocerException e) {
            log.info("Test: createFascicolo ERROR");
            log.info("IMPOSSIBILE CONTINUARE CON I TEST. SESSIONE DI TEST INTERRROTTA!");
        }

        IFascicoloInfo sottofasc = null;
        try {
            sottofasc = _createSottoFascicolo(fasc);
            log.info("Test: createSottoFascicolo OK");
        } catch (DocerException e) {
            log.info("Test: createSottoFascicolo ERROR");
        }

        ICustomItemInfo custom = null;
        try {
            custom = _createCustomItem(aoo);
            log.info("Test: createCustomItem OK");
        } catch (DocerException e) {
            log.info("Test: createCustomItem ERROR");
        }

        String folderId = null;
        try {
            folderId = _createFolder(aoo);
            log.info("Test: createFolder OK");
        } catch (DocerException e) {
            log.info("Test: createFolder ERROR");
        }

        IGroupProfileInfo group = null;
        try {
            group = _createGroup(ente);
            log.info("Test: createGroup OK");
        } catch (DocerException e) {
            log.info("Test: createGroup ERROR");
        }

        IGroupProfileInfo group2 = null;
        try {
            group2 = _createGroup(ente);
            log.info("Test: createGroup OK");
        } catch (DocerException e) {
            log.info("Test: createGroup ERROR");
        }

        IGroupProfileInfo group3 = null;
        try {
            group3 = _createGroup(ente);
            log.info("Test: createGroup OK");
        } catch (DocerException e) {
            log.info("Test: createGroup ERROR");
        }

        IGroupProfileInfo sottogroup = null;
        try {
            sottogroup = _createGroupParent(ente,group);
            log.info("Test: createGroupParent OK");
        } catch (DocerException e) {
            log.info("Test: createGroupParent ERROR");
        }

        IUserProfileInfo user = null;
        try {
            user = _createUser();
            log.info("Test: createUser OK");
        } catch (DocerException e) {
            log.info("Test: createUser ERROR");
        }

        IUserProfileInfo user2 = null;
        try {
            user2 = _createUser();
            log.info("Test: createUser OK");
        } catch (DocerException e) {
            log.info("Test: createUser ERROR");
        }

        IUserProfileInfo user3 = null;
        try {
            user3 = _createUser();
            log.info("Test: createUser OK");
        } catch (DocerException e) {
            log.info("Test: createUser ERROR");
        }

        String folderOwnerId = null;
        try {
            folderOwnerId = _createFolderOwner(aoo, user);
            log.info("Test: createFolderOwner OK");
        } catch (DocerException e) {
            log.info("Test: createFolderOwner ERROR");
        }

        Map<String, EnumACLRights> acls =new HashMap<String, EnumACLRights>();
        acls.put(user.getUserId(),EnumACLRights.viewProfile);
        acls.put(group.getGroupId(),EnumACLRights.normalAccess);

        try {
            IFascicoloId fascId = new FascicoloId();
            fascId.setCodiceEnte(fasc.getCodiceEnte());
            fascId.setCodiceAOO(fasc.getCodiceAOO());
            fascId.setProgressivo(fasc.getProgressivo());
            fascId.setClassifica(fasc.getClassifica());
            fascId.setAnnoFascicolo(fasc.getAnnoFascicolo());

            _setACLFascicolo(fascId,acls);
            log.info("Test: setACLFascicolo OK");
        } catch (DocerException e) {
            log.info("Test: setACLFascicolo ERROR");
        }
        try {
            IFascicoloId fascId = new FascicoloId();
            fascId.setCodiceEnte(fasc.getCodiceEnte());
            fascId.setCodiceAOO(fasc.getCodiceAOO());
            fascId.setProgressivo(fasc.getProgressivo());
            fascId.setClassifica(fasc.getClassifica());
            fascId.setAnnoFascicolo(fasc.getAnnoFascicolo());

            Map<String, EnumACLRights> aclRet = _getACLFascicolo(fascId);
            for (String k : aclRet.keySet()) {
                if (!acls.containsKey(k))
                    Assert.fail();
            }
            log.info("Test: getACLFascicolo OK");
        } catch (DocerException e) {
            log.info("Test: getACLFascicolo ERROR");
        }

        try {
            ITitolarioId titId = new TitolarioId();
            titId.setCodiceEnte(tit.getCodiceEnte());
            titId.setCodiceAOO(tit.getCodiceAOO());
            titId.setClassifica(tit.getClassifica());

            _setACLTitolario(titId, acls);
            log.info("Test: setACLTitolario OK");
        } catch (DocerException e) {
            log.info("Test: setACLTitolario ERROR");
        }
        try {
            ITitolarioId titId = new TitolarioId();
            titId.setCodiceEnte(tit.getCodiceEnte());
            titId.setCodiceAOO(tit.getCodiceAOO());
            titId.setClassifica(tit.getClassifica());

            Map<String, EnumACLRights> aclRet = _getACLTitolario(titId);
            for (String k : aclRet.keySet()) {
                if (!acls.containsKey(k))
                    Assert.fail();
            }
            log.info("Test: getACLTitolario OK");
        } catch (DocerException e) {
            log.info("Test: getACLTitolario ERROR");
        }

        try {
            _setACLFolder(folderId, acls);
            log.info("Test: setACLTitolario OK");
        } catch (DocerException e) {
            log.info("Test: setACLTitolario ERROR");
        }
        try {
            Map<String, EnumACLRights> aclRet = _getACLFolder(folderId);
            for (String k : aclRet.keySet()) {
                if (!acls.containsKey(k))
                    Assert.fail();
            }
            log.info("Test: getACLTitolario OK");
        } catch (DocerException e) {
            log.info("Test: getACLTitolario ERROR");
        }

        String docNum = null;
        try {
            docNum = _createDocument(aoo);
            log.info("Test: createDocument OK");
        } catch (DocerException e) {
            log.info("Test: createDocument ERROR");
        }

        try {
            byte[] bytes = _downloadLastVersion(docNum);
            log.info("Test: downloadLastVersion OK");
        } catch (DocerException e) {
            log.info("Test: downloadLastVersion ERROR");
        }

        try {
            _setACLDocument(docNum, acls);
            log.info("Test: setACLDocumento OK");
        } catch (DocerException e) {
            log.info("Test: setACLDocumento ERROR");
        }
        try {
            Map<String, EnumACLRights> aclRet = _getACLDocument(docNum);

            for (String k : aclRet.keySet()) {
                if (!acls.containsKey(k))
                    Assert.fail();
            }

            log.info("Test: getACLDocument OK");
        } catch (DocerException e) {
            log.info("Test: getACLDocument ERROR");
        }

        String docNum2 = null;
        try {
            docNum2 = _createDocument(aoo);
            log.info("Test: createDocument OK");
        } catch (DocerException e) {
            log.info("Test: createDocument ERROR");
        }

        String docNum3 = null;
        try {
            docNum3 = _createDocument(aoo);
            log.info("Test: createDocument OK");
        } catch (DocerException e) {
            log.info("Test: createDocument ERROR");
        }
//----------------------------------------------------------------------

        String versionDocNum = null;
        try {
            versionDocNum = _addNewVersion(docNum);
            log.info("Test: addNewVersion OK");
        } catch (DocerException e) {
            log.info("Test: addNewVersion ERROR");
        }

        try {
            byte[] bytes = _downloadVersion(docNum, versionDocNum);
            log.info("Test: downloadVersion OK");
        } catch (DocerException e) {
            log.info("Test: downloadVersion ERROR");
        }

        try {
            _replaceLastVersion(docNum);
            log.info("Test: replaceLastVersion OK");
        } catch (DocerException e) {
            log.info("Test: replaceLastVersion ERROR");
        }

        try {
            byte[] bytes = _downloadLastVersion(docNum);
            log.info("Test: downloadLastVersion OK");
        } catch (DocerException e) {
            log.info("Test: downloadLastVersion ERROR");
        }

        List<String> versionsDocNum;
        try {
            versionsDocNum = _getVersions(docNum);
            log.info("Test: getVersions OK");
        } catch (DocerException e) {
            log.info("Test: getVersions ERROR");
        }

        List<String> docList = new ArrayList<String>();
        docList.add(docNum);
        docList.add(docNum2);
        docList.add(docNum3);
        try {
            _addToFolderDocuments(folderId, docList);
            log.info("Test: addToFolderDocuments OK");
        } catch (DocerException e) {
            log.info("Test: addToFolderDocuments ERROR");
        }

        try {
            _addToFolderDocuments(folderId, docList);
            log.info("Test: addToFolderDocuments OK");
        } catch (DocerException e) {
            log.info("Test: addToFolderDocuments ERROR");
        }

        try {
            List<String> docListRet = _getFolderDocuments(folderId);
            docListRet.removeAll(docList);
            assert(docListRet.size()==0);

            log.info("Test: getFolderDocuments OK");
        } catch (DocerException e) {
            log.info("Test: getFolderDocuments ERROR");
        }

        try {
            _removeFromFolderDocuments(folderId, docList);
            log.info("Test: removeFromFolderDocuments OK");
        } catch (DocerException e) {
            log.info("Test: removeFromFolderDocuments ERROR");
        }

        try {
            List<String> docListRet = _getFolderDocuments(folderId);
            if (docListRet.size()>0)
                Assert.fail();

            log.info("Test: getFolderDocuments OK");
        } catch (DocerException e) {
            log.info("Test: getFolderDocuments ERROR");
        }

        List<String> groups = new ArrayList<String>();
        groups.add(group.getGroupId());
        groups.add(group2.getGroupId());
        groups.add(group3.getGroupId());

        try {
            _addGroupsToUser(user.getUserId(), groups);
            log.info("Test: addGroupsToUser OK");
        } catch (DocerException e) {
            log.info("Test: addGroupsToUser ERROR");
        }

        try {
            _removeGroupsFromUser(user.getUserId(), groups);
            log.info("Test: removeGroupsFromUser OK");
        } catch (DocerException e) {
            log.info("Test: removeGroupsFromUser ERROR");
        }

        List<String> users = new ArrayList<String>();
        users.add(user.getUserId());
        users.add(user2.getUserId());
        users.add(user3.getUserId());
        try {
            _addUsersToGroup(group.getGroupId(), users);
            log.info("Test: addUsersToGroup OK");
        } catch (DocerException e) {
            log.info("Test: addUsersToGroup ERROR");
        }

        try {
            _removeUsersFromGroup(group.getGroupId(), users);
            log.info("Test: removeUsersFromGroup OK");
        } catch (DocerException e) {
            log.info("Test: removeUsersFromGroup ERROR");
        }

//----------------------------------------------------------------------
        List<String> related = new ArrayList<String>();
        related.add(docNum2);
        related.add(docNum3);

        try {
            _addRelatedDocuments(docNum, related);
            log.info("Test: addRelatedDocuments OK");
        } catch (DocerException e) {
            log.info("Test: addRelatedDocuments ERROR");
        }

        try {
            List<String> docRelated = _getRelatedDocuments(docNum);
            if (!docRelated.contains(docNum2) || !docRelated.contains(docNum3))
                throw new DocerException("TEST FALLITO");

            log.info("Test: getRelatedDocuments OK");
        } catch (DocerException e) {
            log.info("Test: getRelatedDocuments ERROR");
        }

        //rimuove un related
        try {
            List<String> relatedRem = new ArrayList<String>();
            relatedRem.add(docNum2);
            _removeRelatedDocuments(docNum, relatedRem);
            log.info("Test: removeRelatedDocuments 1 related OK");
        } catch (DocerException e) {
            log.info("Test: removeRelatedDocuments 1 related ERROR");
        }

        //rimuove tutti i related
        try {
            _removeRelatedDocuments(docNum, related);
            log.info("Test: removeRelatedDocuments tutti OK");
        } catch (DocerException e) {
            log.info("Test: removeRelatedDocuments tutti ERROR");
        }

        try {
            _addRiferimentiDocuments(docNum, related);
            log.info("Test: addRiferimentiDocuments OK");
        } catch (DocerException e) {
            log.info("Test: addRiferimentiDocuments ERROR");
        }

        try {
            List<String> docRelated = _getRiferimentiDocuments(docNum);
            if (!docRelated.contains(docNum2) || !docRelated.contains(docNum3))
                throw new DocerException("TEST FALLITO");

            log.info("Test: getRiferimentiDocuments OK");
        } catch (DocerException e) {
            log.info("Test: getRiferimentiDocuments ERROR");
        }

        try {
            _removeRiferimentiDocuments(docNum, related);
            log.info("Test: removeRiferimentiDocuments OK");
        } catch (DocerException e) {
            log.info("Test: removeRiferimentiDocuments ERROR");
        }

        try {
            _getEffectiveRights(docNum, user.getUserId());
            log.info("Test: getEffectiveRights OK");
        } catch (DocerException e) {
            log.info("Test: getEffectiveRights ERROR");
        }

        try {
            _getEffectiveRightsFolder(folderId, user.getUserId());
            log.info("Test: getEffectiveRightsFolder OK");
        } catch (DocerException e) {
            log.info("Test: getEffectiveRightsFolder ERROR");
        }

        try {
            Map<String,String> id = new HashMap<String, String>();
            id.put(Fields.Titolario.COD_ENTE,tit.getCodiceEnte());
            id.put(Fields.Titolario.COD_AOO,tit.getCodiceAOO());
            id.put(Fields.Titolario.CLASSIFICA,tit.getClassifica());

            _getEffectiveRightsAnagrafiche(TYPE_TITOLARIO, id, user.getUserId());
            log.info("Test: getEffectiveRightsAnagrafiche titolario OK");
        } catch (DocerException e) {
            log.info("Test: getEffectiveRightsAnagrafiche titolario ERROR");
        }

        try {
            Map<String,String> id = new HashMap<String, String>();
            id.put(Fields.Fascicolo.COD_ENTE,fasc.getCodiceEnte());
            id.put(Fields.Fascicolo.COD_AOO,fasc.getCodiceAOO());
            id.put(Fields.Fascicolo.PROGRESSIVO,fasc.getProgressivo());
            id.put(Fields.Fascicolo.ANNO_FASCICOLO,fasc.getAnnoFascicolo());
            id.put(Fields.Fascicolo.CLASSIFICA,fasc.getClassifica());

            _getEffectiveRightsAnagrafiche(TYPE_FASCICOLO, id, user.getUserId());
            log.info("Test: getEffectiveRightsAnagrafiche fascicolo OK");
        } catch (DocerException e) {
            log.info("Test: getEffectiveRightsAnagrafiche fascicolo ERROR");
        }

        try {
            Map<String,String> id = new HashMap<String, String>();
            id.put(Fields.CustomItem.COD_ENTE,custom.getCodiceEnte());
            id.put(Fields.CustomItem.COD_AOO,custom.getCodiceAOO());
            id.put("COD_"+custom.getType(),custom.getCodiceCustom());

            _getEffectiveRightsAnagrafiche(custom.getType(), id, user.getUserId());
            log.info("Test: getEffectiveRightsAnagrafiche fascicolo OK");
        } catch (DocerException e) {
            log.info("Test: getEffectiveRightsAnagrafiche fascicolo ERROR");
        }



        log.info("--------------------------------------------------");
        log.info("DELETE ALL OBJECT");
        log.info("--------------------------------------------------");
        try {
            HashMap<String, List<String>> criteria = new HashMap<String, List<String>>();
            criteria.put("COD_ENTE",Collections.singletonList(ente.getCodiceEnte()));
            criteria.put("COD_AOO",Collections.singletonList(aoo.getCodiceAOO()));
            SolrDocumentList results = this.solrSearch(currentUser,"*", criteria,null,Collections.singletonList("id"),-1,null);
            for (Iterator iterator = results.iterator(); iterator.hasNext();) {

                Map rec = (Map) iterator.next();
                String solrId = rec.get("id").toString();
                SolrInputDocument metadata = new SolrInputDocument();
                metadata.addField("enabled",false);

                this.solrUpdate(currentUser,solrId,metadata);
            }

            log.info("Test: delete all ente objects OK");
        } catch (DocerException e) {
            log.info("Test: delete all ente objects ERROR");
        }

        log.info("--------------------------------------------------");
        log.info("END ALL TESTS");
        log.info("--------------------------------------------------");
    }

    @Test
    public void addUsersToGroup() throws DocerException {
        login();

        IUserProfileInfo ui = _createUser();
        IGroupProfileInfo gi = _createGroup();

        List<String> users = new ArrayList<String>();
        users.add(ui.getUserId());

        _addUsersToGroup(gi.getGroupId(), users);
    }

    public void _addUsersToGroup(String groupid, List<String> users) throws DocerException {
        p.addUsersToGroup(groupid, users);
    }

    @Test
    public void removeFromFolderDocuments() throws DocerException {
        login();
        String folderId = "xxxxxx";
        List<String> documents = new ArrayList<String>();
        documents.add("160");
        documents.add("161");

        _removeFromFolderDocuments(folderId, documents);
    }

    public void _removeFromFolderDocuments(String folderId, List<String> documents) throws DocerException {
        p.removeFromFolderDocuments(folderId, documents);
    }

    @Test
    public void addToFolderDocuments() throws DocerException {
        login();
        String folderId = "xxxxxx";
        List<String> documents = new ArrayList<String>();
        documents.add("160");
        documents.add("161");

        _addToFolderDocuments(folderId, documents);
    }

    public void _addToFolderDocuments(String folderId, List<String> documents) throws DocerException {
        p.addToFolderDocuments(folderId, documents);
    }

    @Test
    public void removeUsersFromGroup() throws DocerException {
        login();
        IUserProfileInfo ui = _createUser();
        IUserProfileInfo ui2 = _createUser();
        IGroupProfileInfo gi1 = _createGroup();
        List<String> users = new ArrayList<String>();
        users.add(ui.getUserId());
        users.add(ui2.getUserId());

        _addUsersToGroup(gi1.getGroupId(),users);

        _removeUsersFromGroup(gi1.getGroupId(), users);
    }

    public void _removeUsersFromGroup(String groupId, List<String> users) throws DocerException {
        p.removeUsersFromGroup(groupId, users);
    }

    @Test
    public void getAdvancedVersions() throws DocerException {
        login();
        String docnum = _createDocument();
        String docnum1 = _createDocument();

        _addNewAdvancedVersion(docnum,docnum1);

        List<String> vers =_getAdvancedVersions(docnum);
    }

    public List<String> _getAdvancedVersions(String docId) throws DocerException {
        return p.getAdvancedVersions(docId);
    }

    @Test
    public void addNewAdvancedVersion() throws DocerException {
        login();
        String docnum = _createDocument();
        String docnum2 = _createDocument();

        _addNewAdvancedVersion(docnum, docnum2);

    }

    public void _addNewAdvancedVersion(String docIdLastVersion, String docIdNewVersion) throws DocerException {
        p.addNewAdvancedVersion(docIdLastVersion,docIdNewVersion);
    }

    @Test
    public void addGroupsToUser() throws DocerException {
        login();

        IGroupProfileInfo g1 = _createGroup();
        IGroupProfileInfo g2 = _createGroup();
        IUserProfileInfo ui = _createUser();

        List<String> groups = new ArrayList<>();
        groups.add(g1.getGroupId());
        groups.add(g2.getGroupId());

        _addGroupsToUser(ui.getUserId(), groups);

    }

    public void _addGroupsToUser(String userId, List<String> groups) throws DocerException {
        p.addGroupsToUser(userId, groups);
    }

    @Test
    public void removeGroupsFromUser() throws DocerException {
        login();
        IUserProfileInfo ui = _createUser();
        IGroupProfileInfo gi1 = _createGroup();
        IGroupProfileInfo gi2 = _createGroup();
        List<String> groups = new ArrayList<String>();
        groups.add(gi1.getGroupId());
        groups.add(gi2.getGroupId());

        _addGroupsToUser(ui.getUserId(),groups);

        _removeGroupsFromUser(ui.getUserId(), groups);

    }

    public void _removeGroupsFromUser(String userId, List<String> groups) throws DocerException {
        p.removeGroupsFromUser(userId,groups);
    }

    @Test
    public void getFolderDocuments() throws DocerException {
        login();
        String folderId = "xxxxxxx";
        List<String> docs = _getFolderDocuments(folderId);

    }

    public List<String> _getFolderDocuments(String folderId) throws DocerException {
        return p.getFolderDocuments(folderId);
    }


    @Test
    public void addNewVersion() throws DocerException {
        login();
        String docnum = _createDocument();
        String version = _addNewVersion(docnum);

    }

    public String _addNewVersion(String docnum) throws DocerException {
        String fileContent = "TEST VERSIONE 2";

        InputStream filedoc = new ByteArrayInputStream( fileContent.getBytes() );

        return p.addNewVersion(docnum, filedoc);
    }

    @Test
    public void replaceLastVersion() throws DocerException {
        login();
        String docnum = _createDocument();
        _replaceLastVersion(docnum);

    }

    public void _replaceLastVersion(String docnum) throws DocerException {
        String fileContent = "TEST VERSIONE 2"+UUID.randomUUID().toString();

        InputStream filedoc = new ByteArrayInputStream( fileContent.getBytes() );

        p.replaceLastVersion(docnum, filedoc);
    }

    @Test
    public void getVersions() throws DocerException {
        login();
        String docnum = _createDocument();
        _getVersions(docnum);
    }

    public List<String> _getVersions(String docnum) throws DocerException {
        return p.getVersions(docnum);
    }


    @Test
    public void downloadLastVersion() throws DocerException {
        login();
        String docnum = _createDocument();
        byte[] bytes = _downloadLastVersion(docnum);

    }

    public byte[] _downloadLastVersion(String docnum) throws DocerException {
        return p.downloadLastVersion(docnum,"",100000000);
    }
    @Test
    public void downloadVersion() throws DocerException {
        login();
        String docnum = _createDocument();
        String version = _addNewVersion(docnum);

        byte[] bytes = _downloadVersion(docnum,version);


    }

    public byte[] _downloadVersion(String docnum, String version) throws DocerException {
        return p.downloadVersion(docnum, version, "",100000000);
    }


    @Test
    public void getRelatedDocuments() throws DocerException {
        login();

        String docNum = _createDocument();
        _getRelatedDocuments(docNum);
    }

    public List<String> _getRelatedDocuments(String docNum) throws DocerException {

        List<String> related = p.getRelatedDocuments(docNum);

        return related;
    }

    @Test
    public void addRelatedDocuments() throws DocerException {
        login();

        String d1 = _createDocument();
        String d2 = _createDocument();
        String d3 = _createDocument();

        List<String> related = new ArrayList<String>();
        related.add(d1);
        related.add(d2);

        _addRelatedDocuments(d3, related);
    }

    public void _addRelatedDocuments(String docNum, List<String> related) throws DocerException {

        p.addRelatedDocuments(docNum, related);
    }

    @Test
    public void removeRelatedDocuments() throws DocerException {
        login();

        String d1 = _createDocument();
        String d2 = _createDocument();
        String d3 = _createDocument();

        List<String> related = new ArrayList<String>();
        related.add(d1);
        related.add(d2);

        _addRelatedDocuments(d3, related);

        _removeRelatedDocuments(d3, related);
    }

    public void _removeRelatedDocuments(String docNum, List<String> related) throws DocerException {

        p.removeRelatedDocuments(docNum, related);
    }


    @Test
    public void getRiferimentiDocuments() throws DocerException {
        login();

        String docNum = _createDocument();
        _getRiferimentiDocuments(docNum);
    }

    public List<String> _getRiferimentiDocuments(String docNum) throws DocerException {

        List<String> related = p.getRiferimentiDocuments(docNum);

        return related;
    }

    @Test
    public void addRiferimentiDocuments() throws DocerException {
        login();

        String d1 = _createDocument();
        String d2 = _createDocument();
        String d3 = _createDocument();

        List<String> related = new ArrayList<String>();
        related.add(d1);
        related.add(d2);

        _addRiferimentiDocuments(d3, related);
    }

    public void _addRiferimentiDocuments(String docNum, List<String> related) throws DocerException {

        p.addRiferimentiDocuments(docNum, related);
    }

    @Test
    public void removeRiferimentiDocuments() throws DocerException {
        login();

        String d1 = _createDocument();
        String d2 = _createDocument();
        String d3 = _createDocument();

        List<String> related = new ArrayList<String>();
        related.add(d1);
        related.add(d2);

        _addRiferimentiDocuments(d3, related);

        _removeRiferimentiDocuments(d3,related);
    }

    public void _removeRiferimentiDocuments(String docNum, List<String> related) throws DocerException {

        p.removeRiferimentiDocuments(docNum, related);
    }

    @Test
    public void getEffectiveRights() throws DocerException {
        login();

        String docNum = _createDocument();
        //String userId = "stefano.vigna";

        _getEffectiveRights(docNum, username);
    }

    public void _getEffectiveRights(String docNum, String userId) throws DocerException {

        p.getEffectiveRights(docNum, userId);
    }

    @Test
    public void getEffectiveRightsFolder() throws DocerException {
        login();

        String userId = "stefano.vigna";

        Map<String,List<String>> criteria = new HashMap<String, List<String>>();
        SolrDocumentList result = solrSearch(currentUser,TYPE_FOLDER,criteria,null,Collections.singletonList("*"),1,null);

        SolrDocument rec = result.get(0);

        String folderId = rec.getFirstValue(Fields.Folder.FOLDER_ID).toString();

        _getEffectiveRightsFolder(folderId, userId);
    }

    public void _getEffectiveRightsFolder(String folderId, String userId) throws DocerException {

        p.getEffectiveRightsFolder(folderId, userId);
    }

    @Test
    public void getEffectiveRightsAnagraficheTitolario() throws DocerException {
        login();

        ITitolarioInfo tit = _createTitolario();

        String type = TYPE_TITOLARIO;

        Map<String,String> id = new HashMap<String, String>();
        id.put(Fields.Titolario.COD_ENTE, ente);
        id.put(Fields.Titolario.COD_AOO, aoo);
        id.put(Fields.Titolario.CLASSIFICA, tit.getClassifica());

        _getEffectiveRightsAnagrafiche(type, id, username);
    }

    @Test
    public void getEffectiveRightsAnagraficheFascicolo() throws DocerException {
        login();

        String userId = "stefano.vigna";

        String type = TYPE_FASCICOLO;

        Map<String,List<String>> criteria = new HashMap<String, List<String>>();
        SolrDocumentList result = solrSearch(currentUser,TYPE_FASCICOLO,criteria,null,Collections.singletonList("*"),1,null);

        SolrDocument rec = result.get(0);

        Map<String,String> id = new HashMap<String, String>();
        id.put(Fields.Fascicolo.COD_ENTE, rec.getFirstValue(Fields.Fascicolo.COD_ENTE).toString());
        id.put(Fields.Fascicolo.COD_AOO, rec.getFirstValue(Fields.Fascicolo.COD_AOO).toString());
        id.put(Fields.Fascicolo.CLASSIFICA, rec.getFirstValue(Fields.Fascicolo.CLASSIFICA).toString());
        id.put(Fields.Fascicolo.ANNO_FASCICOLO, rec.getFirstValue(Fields.Fascicolo.ANNO_FASCICOLO).toString());
        id.put(Fields.Fascicolo.PROGRESSIVO, rec.getFirstValue(Fields.Fascicolo.PROGRESSIVO).toString());

        _getEffectiveRightsAnagrafiche(type, id, userId);
    }

    public void _getEffectiveRightsAnagrafiche(String type, Map<String,String> id, String userId) throws DocerException {

        p.getEffectiveRightsAnagrafiche(type, id, userId);
    }

    @Test
    public void LockUnlockDocument() throws DocerException {

        login();

        Map<String,List<String>> criteria = new HashMap();
        criteria.put("COD_AOO", Collections.singletonList("40"));

        Map<String,EnumSearchOrder> orderby = new HashMap<String, EnumSearchOrder>();
        orderby.put("id",EnumSearchOrder.DESC);

        List<String> returnProps = new ArrayList<String>();
        returnProps.add("id");
        returnProps.add("indexed_on");
        returnProps.add("type");
        returnProps.add("DOCNUM");

        List<String> keywords = new ArrayList<String>();
        keywords.add("test");

        DataTable<String> results = p.searchDocuments(criteria, keywords, returnProps, 5, orderby);

        for (DataRow<String> rec : results.getRows()) {
            ILockStatus status;
            p.lockDocument(rec.get(Fields.Documento.DOCNUM));
            status = p.isCheckedOutDocument(rec.get(Fields.Documento.DOCNUM));
            p.unlockDocument(rec.get(Fields.Documento.DOCNUM));
            status = p.isCheckedOutDocument(rec.get(Fields.Documento.DOCNUM));
            int a = 0;
        }

    }

    @Test
    public void isCheckedOutDocument() throws DocerException {

        login();

        Map<String,List<String>> criteria = new HashMap();
        criteria.put("COD_AOO", Collections.singletonList("40"));

        Map<String,EnumSearchOrder> orderby = new HashMap<String, EnumSearchOrder>();
        orderby.put("id",EnumSearchOrder.DESC);

        List<String> returnProps = new ArrayList<String>();
        returnProps.add("id");
        returnProps.add("indexed_on");
        returnProps.add("type");
        returnProps.add("DOCNUM");

        List<String> keywords = new ArrayList<String>();
        keywords.add("test");

        DataTable<String> results = p.searchDocuments(criteria, keywords, returnProps, 5, orderby);

        for (DataRow<String> rec : results.getRows()) {
            ILockStatus status = p.isCheckedOutDocument(rec.get(Fields.Documento.DOCNUM));
            int a = 0;
        }

    }

    @Test
    public void updateEnte() throws DocerException {

        login();

        //search dell'ente
        Map<String,List<String>> criteria = new HashMap();

        List<String> returnProps = new ArrayList<String>();
        returnProps.add("*");

        String type = "ente";
        List<Map<String, String>> results = p.searchAnagrafiche(type, criteria, returnProps);

        for (Map metaEnte : results) {
            IEnteId enteId = new EnteId();
            enteId.setCodiceEnte(metaEnte.get(Fields.Ente.COD_ENTE).toString());

            IEnteInfo enteInfo = new EnteInfo();
            enteInfo.setDescrizione(metaEnte.get(Fields.Ente.DES_ENTE).toString() + " (UNIT TEST PROVIDER SOLR) ");

            //TODO: al secondo gir si blocca solr
            p.updateEnte(enteId,enteInfo);
        }

    }

    @Test
    public void updateAoo() throws DocerException {

        login();

        //search dell'ente
        Map<String,List<String>> criteria = new HashMap();
        criteria.put("COD_ENTE",Collections.singletonList("STUDIO_PIROLA"));
        criteria.put("COD_AOO",Collections.singletonList("40"));

        List<String> returnProps = new ArrayList<String>();
        returnProps.add("*");

        String type = "aoo";
        List<Map<String, String>> results = p.searchAnagrafiche(type, criteria, returnProps);

        for (Map meta : results) {
            IAOOId aooId = new AOOId();
            aooId.setCodiceEnte(meta.get(Fields.Aoo.COD_ENTE).toString());
            aooId.setCodiceAOO(meta.get(Fields.Aoo.COD_AOO).toString());

            IAOOInfo info = new AOOInfo();
            info.setDescrizione(meta.get(Fields.Aoo.DES_AOO).toString() + " (UNIT TEST PROVIDER SOLR) ");

            p.updateAOO(aooId, info);
        }

    }

    @Test
    public void updateTitolario() throws DocerException {

        login();

        //search dell'ente
        Map<String,List<String>> criteria = new HashMap();
        criteria.put("COD_ENTE",Collections.singletonList("STUDIO_PIROLA"));
        criteria.put("COD_AOO",Collections.singletonList("40"));
        criteria.put("CLASSIFICA",Collections.singletonList("64001"));

        List<String> returnProps = new ArrayList<String>();
        returnProps.add("*");

        String type = "titolario";
        List<Map<String, String>> results = p.searchAnagrafiche(type, criteria, returnProps);

        for (Map meta : results) {
            ITitolarioId titId = new TitolarioId();
            titId.setCodiceEnte(meta.get(Fields.Titolario.COD_ENTE).toString());
            titId.setCodiceAOO(meta.get(Fields.Titolario.COD_AOO).toString());
            titId.setClassifica(meta.get(Fields.Titolario.CLASSIFICA).toString());

            ITitolarioInfo info = new TitolarioInfo();
            info.setDescrizione(meta.get(Fields.Titolario.DES_TITOLARIO).toString() + " (UNIT TEST PROVIDER SOLR) ");

            p.updateTitolario(titId, info);
        }

    }

    @Test
    public void updateFascicolo() throws DocerException {

        login();

        Map<String,List<String>> criteria = new HashMap();
        criteria.put("COD_ENTE",Collections.singletonList("STUDIO_PIROLA"));
        criteria.put("COD_AOO",Collections.singletonList("40"));
        criteria.put("ANNO_FASCICOLO",Collections.singletonList("1900"));
        criteria.put("PROGR_FASCICOLO",Collections.singletonList("40-75226-60046/1"));
        criteria.put("CLASSIFICA",Collections.singletonList("75226"));

        List<String> returnProps = new ArrayList<String>();
        returnProps.add("*");

        String type = "fascicolo";
        List<Map<String, String>> results = p.searchAnagrafiche(type, criteria, returnProps);

        for (Map meta : results) {
            IFascicoloId fascId = new FascicoloId();
            fascId.setCodiceEnte(meta.get(Fields.Fascicolo.COD_ENTE).toString());
            fascId.setCodiceAOO(meta.get(Fields.Fascicolo.COD_AOO).toString());
            fascId.setClassifica(meta.get(Fields.Fascicolo.CLASSIFICA).toString());
            fascId.setAnnoFascicolo(meta.get(Fields.Fascicolo.ANNO_FASCICOLO).toString());
            fascId.setProgressivo(meta.get(Fields.Fascicolo.PROGRESSIVO).toString());

            IFascicoloInfo info = new FascicoloInfo();
            info.setDescrizione(meta.get(Fields.Fascicolo.DES_FASCICOLO).toString() + " (UNIT TEST PROVIDER SOLR) ");

            p.updateFascicolo(fascId, info);
        }

    }

    @Test
    public void updateCustomItem() throws DocerException {

        login();

        Map<String,List<String>> criteria = new HashMap();
        criteria.put("COD_ENTE",Collections.singletonList("STUDIO_PIROLA"));
        criteria.put("COD_AOO",Collections.singletonList("40"));

        List<String> returnProps = new ArrayList<String>();
        returnProps.add("*");

        String type = "anagraficaCustom";
        List<Map<String, String>> results = p.searchAnagrafiche(type, criteria, returnProps);

        for (Map meta : results) {
            ICustomItemId custId = new CustomItemId();
            custId.setCodiceEnte(meta.get(Fields.CustomItem.COD_ENTE).toString());
            custId.setCodiceAOO(meta.get(Fields.CustomItem.COD_AOO).toString());
            custId.setType(meta.get("type").toString());
            custId.setCodiceCustom(meta.get("COD_"+custId.getType()).toString());

            ICustomItemInfo info = new CustomItemInfo();
            info.setDescrizione(meta.get("DES_"+custId.getType()).toString() + " (UNIT TEST PROVIDER SOLR) ");

            p.updateCustomItem(custId, info);
        }

    }

    @Test
    public void setUserPassword() throws DocerException {
        ILoggedUserInfo user = new LoggedUserInfo();
        user.setTicket(username);
        user.setCodiceEnte(ente);
        user.setUserId(username);

        p = new Provider();
        p.setCurrentUser(user);

        IUserProfileInfo ui = _createUser();

        IUserProfileInfo meta = new UserProfileInfo();
        meta.setUserId(ui.getUserId());
        meta.setUserPassword("blah");

        p.updateUser(meta.getUserId(), meta);
    }
    @Test
    public void updateUser() throws DocerException {

        login();

        Map<String,List<String>> criteria = new HashMap();
        criteria.put("EMAIL_ADDRESS",Collections.singletonList("stefano.vigna@kdm.it"));

        List<String> returnProps = new ArrayList<String>();
        returnProps.add("*");

        String type = "user";
        List<IUserProfileInfo> results = p.searchUsers(criteria);

        for (IUserProfileInfo meta : results) {

            ICustomItemInfo info = new CustomItemInfo();
            meta.setFullName(meta.getFullName() + " (UNIT TEST PROVIDER SOLR) ");

            p.updateUser(meta.getUserId(), meta);
        }

    }

    //TODO: da finire e testare
    @Test
    public void updateGroup() throws DocerException {

        login();

        Map<String,List<String>> criteria = new HashMap();
        criteria.put("COD_ENTE",Collections.singletonList("ente"));
        criteria.put("GROUP_NAME",Collections.singletonList("GROUP TEST SOLR"));

        List<String> returnProps = new ArrayList<String>();
        returnProps.add("*");

        String type = "group";
        List<IGroupProfileInfo> results = p.searchGroups(criteria);

        Map<String,String> extra = new HashMap<String, String>();
        extra.put("exra_custom_1","custom1");
        extra.put("exra_custom_2","custom2");
        extra.put("exra_custom_3","custom3");

        for (IGroupProfileInfo meta : results) {

            meta.setExtraInfo(extra);
//            meta.setGroupId("GROUP ID TEST SOLR");
            p.updateGroup(meta.getGroupId(), meta);
        }



    }

    @Test
    public void updateFolder() throws DocerException {

        login();

        Map<String,List<String>> criteria = new HashMap();
        criteria.put("COD_ENTE",Collections.singletonList("STUDIO_PIROLA"));
        criteria.put("FOLDER_ID",Collections.singletonList("33378"));

        List<String> returnProps = new ArrayList<String>();
        returnProps.add("*");

        String type = "folder";

        Map<String,EnumSearchOrder> orderby = new HashMap<String, EnumSearchOrder>();
        orderby.put("id",EnumSearchOrder.DESC);

        DataTable<String> results = p.searchFolders(criteria, returnProps, -1, orderby);

        Map<String,String> extra = new HashMap<String, String>();
        extra.put("exra_custom_1","custom1");
        extra.put("exra_custom_2","custom2");
        extra.put("exra_custom_3","custom3");

        for (DataRow<String> rec : results.getRows()) {
            IFolderInfo folder = new FolderInfo();
            folder.setCodiceEnte("STUDIO_PIROLA");
            folder.setCodiceAOO("CARTELLE_PERSONALI");
            folder.setDescrizione(folder.getDescrizione() + " (UNIT TEST PROVIDER SOLR) ");
            folder.setExtraInfo(extra);

            p.updateFolder(rec.get(Fields.Folder.FOLDER_ID), folder);
        }

    }

    @Test
    public void deleteFolder() throws DocerException {

        login();
        IAOOInfo aooInfo = new AOOInfo();
        aooInfo.setCodiceEnte(ente);
        aooInfo.setCodiceAOO(aoo);

        String folderId = _createFolder(aooInfo);
        _deleteFolder(folderId);

    }

    public void _deleteFolder(String folderId) throws DocerException {
        p.deleteFolder(folderId);
    }

    //TODO: da testare con nuova versione di paolo
    @Test
    public void deleteDocument() throws DocerException {

        login();
        Map<String,List<String>> criteria = new HashMap();
        criteria.put("COD_AOO", Collections.singletonList("40"));

        Map<String,EnumSearchOrder> orderby = new HashMap<String, EnumSearchOrder>();
        orderby.put("id",EnumSearchOrder.DESC);

        List<String> returnProps = new ArrayList<String>();
        returnProps.add("id");
        returnProps.add("indexed_on");
        returnProps.add("type");
        returnProps.add("DOCNUM");

        List<String> keywords = new ArrayList<String>();
        keywords.add("test");

        DataTable<String> results = p.searchDocuments(criteria, keywords, returnProps, 1, orderby);

        for (DataRow<String> rec : results.getRows()) {
            _deleteDocument(rec.get(Fields.Documento.DOCNUM));
        }


    }

    public void _deleteDocument(String docnum) throws DocerException {
        p.deleteDocument(docnum);
    }

    @Test
    public void setACLFascicolo() throws DocerException {
        login();

        Map<String,List<String>> criteria = new HashMap();
        criteria.put("COD_AOO", Collections.singletonList("40"));
        criteria.put("acl_explicit", Collections.singletonList("*"));

//        Map<String,EnumSearchOrder> orderby = new HashMap<String, EnumSearchOrder>();
//        orderby.put("id",EnumSearchOrder.DESC);

        List<String> returnProps = new ArrayList<String>();
        returnProps.add("id");
        returnProps.add("SEDE_LAV");
        returnProps.add("PROGR_FASCICOLO");
        returnProps.add("ANNO_FASCICOLO");
        returnProps.add("COD_ENTE");
        returnProps.add("COD_AOO");
        returnProps.add("CLASSIFICA");


        List<String> keywords = new ArrayList<String>();
        keywords.add("test");

        String type = "fascicolo";
        List<Map<String, String>> results = p.searchAnagrafiche(type, criteria, returnProps);

        for (Map map : results) {
            IFascicoloId fascId = new FascicoloId();
            fascId.setCodiceEnte(map.get(Fields.Fascicolo.COD_ENTE).toString());
            fascId.setCodiceAOO(map.get(Fields.Fascicolo.COD_AOO).toString());
            fascId.setClassifica(map.get(Fields.Fascicolo.CLASSIFICA).toString());
            fascId.setAnnoFascicolo(map.get(Fields.Fascicolo.ANNO_FASCICOLO).toString());
            fascId.setProgressivo(map.get(Fields.Fascicolo.PROGRESSIVO).toString());

            Map<String, EnumACLRights> acls =new HashMap<String, EnumACLRights>();
            acls.put("Upippo",EnumACLRights.viewProfile);
            acls.put("Upluto",EnumACLRights.readOnly);
            acls.put("Gpluto",EnumACLRights.normalAccess);

            _setACLFascicolo(fascId, acls);

        }
    }

    public void _setACLFascicolo(IFascicoloId fascId, Map<String, EnumACLRights> acls) throws DocerException {
        p.setACLFascicolo(fascId,acls);
    }

    @Test
    public void setACLTitolario() throws DocerException {
        login();

        Map<String,List<String>> criteria = new HashMap();
        criteria.put("COD_AOO", Collections.singletonList("40"));
        criteria.put("acl_explicit", Collections.singletonList("*"));

//        Map<String,EnumSearchOrder> orderby = new HashMap<String, EnumSearchOrder>();
//        orderby.put("id",EnumSearchOrder.DESC);

        List<String> returnProps = new ArrayList<String>();
        returnProps.add("*");

        String type = "titolario";
        List<Map<String, String>> results = p.searchAnagrafiche(type, criteria, returnProps);

        for (Map map : results) {
            ITitolarioId titId = new TitolarioId();
            titId.setCodiceEnte(map.get(Fields.Fascicolo.COD_ENTE).toString());
            titId.setCodiceAOO(map.get(Fields.Fascicolo.COD_AOO).toString());
            titId.setClassifica(map.get(Fields.Fascicolo.CLASSIFICA).toString());

            Map<String, EnumACLRights> acls =new HashMap<String, EnumACLRights>();
            acls.put("Upippo",EnumACLRights.viewProfile);
            acls.put("Upluto",EnumACLRights.readOnly);
            acls.put("Gpluto",EnumACLRights.normalAccess);

            _setACLTitolario(titId, acls);

        }
    }

    public void _setACLTitolario(ITitolarioId titId,Map<String, EnumACLRights> acls) throws DocerException {
        p.setACLTitolario(titId, acls);
    }

    //TODO: da testare con nuova versione di paolo
    @Test
    public void getACLTitolario() throws DocerException {
        login();

        Map<String,List<String>> criteria = new HashMap();
        criteria.put("COD_AOO", Collections.singletonList("40"));
        criteria.put("acl_explicit", Collections.singletonList("*"));

//        Map<String,EnumSearchOrder> orderby = new HashMap<String, EnumSearchOrder>();
//        orderby.put("id",EnumSearchOrder.DESC);

        List<String> returnProps = new ArrayList<String>();
        returnProps.add("*");

        String type = "titolario";
        List<Map<String, String>> results = p.searchAnagrafiche(type, criteria, returnProps);

        for (Map map : results) {
            ITitolarioId titId = new TitolarioId();
            titId.setCodiceEnte(map.get(Fields.Fascicolo.COD_ENTE).toString());
            titId.setCodiceAOO(map.get(Fields.Fascicolo.COD_AOO).toString());
            titId.setClassifica(map.get(Fields.Fascicolo.CLASSIFICA).toString());

            Map<String, EnumACLRights> acls = _getACLTitolario(titId);

        }
    }

    public Map<String, EnumACLRights> _getACLTitolario(ITitolarioId titId) throws DocerException {
         return p.getACLTitolario(titId);
    }

    @Test
    public void setACLFolder() throws DocerException {
        login();

        Map<String,List<String>> criteria = new HashMap();
        criteria.put("COD_AOO", Collections.singletonList("CARTELLE_PERSONALI"));
        criteria.put("acl_explicit", Collections.singletonList("*"));

        List<String> returnProps = new ArrayList<String>();
        returnProps.add("*");

        String type = "folder";
        List<Map<String, String>> results = p.searchAnagrafiche(type, criteria, returnProps);

        for (Map map : results) {
            Map<String, EnumACLRights> acls =new HashMap<String, EnumACLRights>();
            acls.put("Upippo",EnumACLRights.viewProfile);
            acls.put("Upluto",EnumACLRights.readOnly);
            acls.put("Gpluto",EnumACLRights.normalAccess);

            _setACLFolder(map.get(Fields.Folder.FOLDER_ID).toString(), acls);

        }
    }

    public void _setACLFolder(String folderId, Map<String, EnumACLRights> acls) throws DocerException {
        p.setACLFolder(folderId, acls);
    }

    //TODO: da testare con nuova versione di paolo
    @Test
    public void getACLFolder() throws DocerException {
        login();

        Map<String,List<String>> criteria = new HashMap();
        criteria.put("COD_AOO", Collections.singletonList("CARTELLE_PERSONALI"));
        criteria.put("acl_explicit", Collections.singletonList("*"));

        List<String> returnProps = new ArrayList<String>();
        returnProps.add("*");

        String type = "folder";
        List<Map<String, String>> results = p.searchAnagrafiche(type, criteria, returnProps);

        for (Map map : results) {
            Map<String, EnumACLRights> acls = _getACLFolder(map.get(Fields.Folder.FOLDER_ID).toString());
        }
    }

    public Map<String, EnumACLRights> _getACLFolder(String folderId) throws DocerException {
        return p.getACLFolder(folderId.toString());
    }

    //TODO: da testare con nuova versione di paolo
    @Test
    public void getACLFascicolo() throws DocerException {
        login();

        Map<String,List<String>> criteria = new HashMap();
        criteria.put("COD_AOO", Collections.singletonList("40"));
        criteria.put("acl_explicit", Collections.singletonList("*"));

//        Map<String,EnumSearchOrder> orderby = new HashMap<String, EnumSearchOrder>();
//        orderby.put("id",EnumSearchOrder.DESC);

        List<String> returnProps = new ArrayList<String>();
        returnProps.add("id");
        returnProps.add("SEDE_LAV");
        returnProps.add("PROGR_FASCICOLO");
        returnProps.add("ANNO_FASCICOLO");
        returnProps.add("COD_ENTE");
        returnProps.add("COD_AOO");
        returnProps.add("CLASSIFICA");


        List<String> keywords = new ArrayList<String>();
        keywords.add("test");

        String type = "fascicolo";
        List<Map<String, String>> results = p.searchAnagrafiche(type, criteria, returnProps);

        for (Map map : results) {
            IFascicoloId fascId = new FascicoloId();
            fascId.setCodiceEnte(map.get(Fields.Fascicolo.COD_ENTE).toString());
            fascId.setCodiceAOO(map.get(Fields.Fascicolo.COD_AOO).toString());
            fascId.setClassifica(map.get(Fields.Fascicolo.CLASSIFICA).toString());
            fascId.setAnnoFascicolo(map.get(Fields.Fascicolo.ANNO_FASCICOLO).toString());
            fascId.setProgressivo(map.get(Fields.Fascicolo.PROGRESSIVO).toString());

            Map<String, EnumACLRights> acls = _getACLFascicolo(fascId);

        }
    }

    public Map<String, EnumACLRights> _getACLFascicolo(IFascicoloId fascId) throws DocerException {
        return p.getACLFascicolo(fascId);
    }

    //TODO: da testare con nuova versione di paolo
    @Test
    public void getACLDocument() throws DocerException {
        login();
        Map<String,List<String>> criteria = new HashMap();
        criteria.put("COD_AOO", Collections.singletonList("40"));

        Map<String,EnumSearchOrder> orderby = new HashMap<String, EnumSearchOrder>();
        orderby.put("id",EnumSearchOrder.DESC);

        List<String> returnProps = new ArrayList<String>();
        returnProps.add("id");
        returnProps.add("indexed_on");
        returnProps.add("type");
        returnProps.add("DOCNUM");

        List<String> keywords = new ArrayList<String>();
        keywords.add("test");

        DataTable<String> results = p.searchDocuments(criteria, keywords, returnProps, 5, orderby);

        for (DataRow<String> rec : results.getRows()) {
            Map<String, EnumACLRights> acls = _getACLDocument(rec.get(Fields.Documento.DOCNUM));
        }


    }

    public Map<String, EnumACLRights> _getACLDocument(String docnum) throws DocerException {
         return p.getACLDocument(docnum);
    }


    @Test
    public void setACLDocument() throws DocerException {
        login();
        Map<String,List<String>> criteria = new HashMap();
        criteria.put("COD_AOO", Collections.singletonList("40"));

        Map<String,EnumSearchOrder> orderby = new HashMap<String, EnumSearchOrder>();
        orderby.put("id",EnumSearchOrder.DESC);

        List<String> returnProps = new ArrayList<String>();
        returnProps.add("*");
        returnProps.add("indexed_on");
        returnProps.add("type");
        returnProps.add("DOCNUM");

        DataTable<String> results = p.searchDocuments(criteria, null, returnProps, 1, orderby);

        for (DataRow<String> rec : results.getRows()) {
            Map<String, EnumACLRights> acls = new HashMap<String, EnumACLRights>();
            acls.put("Upippo",EnumACLRights.viewProfile);
            acls.put("Upluto",EnumACLRights.readOnly);
            acls.put("Gpluto",EnumACLRights.normalAccess);

            p.setACLDocument(rec.get(Fields.Documento.DOCNUM),acls);
        }


    }

    public void _setACLDocument(String docnum,Map<String, EnumACLRights> acls) throws DocerException {
        p.setACLDocument(docnum,acls);
    }

    @Test
    public void updateProfileDocument() throws DocerException {

        login();

        String docNum = "33520";
        Map<String,List<String>> criteria = new HashMap();
        criteria.put("COD_ENTE",Collections.singletonList("STUDIO_PIROLA"));
        criteria.put("COD_AOO",Collections.singletonList("40"));
        criteria.put("DOCNUM",Collections.singletonList(docNum));

        List<String> returnProps = new ArrayList<String>();
        returnProps.add("*");

        String type = "documento";

        Map<String,EnumSearchOrder> orderby = new HashMap<String, EnumSearchOrder>();
        orderby.put("id",EnumSearchOrder.DESC);

        DataTable<String> results = p.searchDocuments(criteria, null, returnProps, -1, orderby);

        Map<String,String> extra = new HashMap<String, String>();
        extra.put("exra_custom_1","custom1");
        extra.put("exra_custom_2","custom2");
        extra.put("exra_custom_3","custom3");

        Map<String,String> meta = new HashMap<String, String>();
        for (DataRow<String> rec : results.getRows()) {
            for (String colName : results.getColumnNames()) {
                meta.put(colName,rec.get(colName));
            }

            meta.put(Fields.Documento.DESCRIZIONE, "(TEST SOLR PROVIDER)");
            p.updateProfileDocument(docNum, meta);
        }



    }

    @Test
    public void createEnte() throws DocerException {
        //login();
        //_createEnte("");
    }
    public IEnteInfo _createEnte(String codEnte) throws DocerException {


        String ente = codEnte; //"ENTE_SOLR_TEST";
        String desc = "DESCRIZIONE " + ente;

        Map<String,String> extra = new HashMap<String, String>();
        extra.put("exra_custom_1","custom1");
        extra.put("exra_custom_2","custom2");
        extra.put("exra_custom_3","custom3");

        IEnteInfo enteInfo = new EnteInfo();
        enteInfo.setCodiceEnte(ente);
        enteInfo.setDescrizione(desc);
        enteInfo.setEnabled(EnumBoolean.TRUE);
        enteInfo.setExtraInfo(extra);

        p.createEnte(enteInfo);

        return enteInfo;
    }
    @Test
    public void createAoo() throws DocerException {
        login();
        IEnteInfo enteInfo = new EnteInfo();
        enteInfo.setCodiceEnte(ente);

        _createAoo(enteInfo);
    }

    public IAOOInfo _createAoo(IEnteInfo ente) throws DocerException {

        String unique = UUID.randomUUID().toString();

        String desc = "DESCRIZIONE " + unique;

        Map<String,String> extra = new HashMap<String, String>();
        extra.put("exra_custom_1","custom1");
        extra.put("exra_custom_2","custom2");
        extra.put("exra_custom_3","custom3");

        IAOOInfo aooInfo = new AOOInfo();
        aooInfo.setCodiceEnte(ente.getCodiceEnte());
        aooInfo.setCodiceAOO(unique);
        aooInfo.setDescrizione(desc+unique);
        aooInfo.setEnabled(EnumBoolean.TRUE);
        aooInfo.setExtraInfo(extra);

        p.createAOO(aooInfo);

        return aooInfo;
    }

    @Test
    public void createTitolario() throws DocerException {
        ITitolarioInfo tit = _createTitolario();
    }

    public ITitolarioInfo _createTitolario() throws DocerException {
        login();
        IAOOInfo aooInfo = new AOOInfo();
        aooInfo.setCodiceEnte(ente);
        aooInfo.setCodiceAOO(aoo);

        return _createTitolario(aooInfo);
    }

    public ITitolarioInfo _createTitolario(IAOOInfo aoo) throws DocerException {

        String unique = UUID.randomUUID().toString();


        Map<String,String> extra = new HashMap<String, String>();
        extra.put("exra_custom_1","custom1");
        extra.put("exra_custom_2","custom2");
        extra.put("exra_custom_3","custom3");

        ITitolarioInfo titInfo = new TitolarioInfo();
        titInfo.setCodiceEnte(aoo.getCodiceEnte());
        titInfo.setCodiceAOO(aoo.getCodiceAOO());
        titInfo.setClassifica(unique);
//        titInfo.setParentClassifica(parentClassifica);
        titInfo.setEnabled(EnumBoolean.TRUE);
        titInfo.setExtraInfo(extra);

        p.createTitolario(titInfo);

        return titInfo;
    }

    @Test
    public void createCustomItem() throws DocerException {
        login();

        IAOOInfo aooInfo = new AOOInfo();
        aooInfo.setCodiceEnte(ente);
        aooInfo.setCodiceAOO(aoo);

        _createCustomItem(aooInfo);
    }

    public ICustomItemInfo _createCustomItem(IAOOInfo aooInfo) throws DocerException {

        String unique = UUID.randomUUID().toString();

        ICustomItemInfo c = new CustomItemInfo();
        c.setCodiceEnte(aooInfo.getCodiceEnte());
        c.setCodiceAOO(aooInfo.getCodiceAOO());
        c.setCodiceCustom(unique);
        c.setDescrizione("descrizione" + unique);
        c.setType("custom" + unique);

        p.createCustomItem(c);

        return c;
    }

    @Test
    public void createFascicolo() throws DocerException {
        login();

        ITitolarioInfo titInfo = _createTitolario();

        _createFascicolo(titInfo);
    }

    public IFascicoloInfo _createFascicolo(ITitolarioInfo tit) throws DocerException {

        login();

        String anno = "2015";
        String progressivo = UUID.randomUUID().toString();

        IFascicoloInfo fascInfo = new FascicoloInfo();
        fascInfo.setCodiceEnte(tit.getCodiceEnte());
        fascInfo.setCodiceAOO(tit.getCodiceAOO());
        fascInfo.setClassifica(tit.getClassifica());
        fascInfo.setAnnoFascicolo(anno);
        fascInfo.setProgressivo(progressivo);

        p.createFascicolo(fascInfo);

        return fascInfo;
    }

    public IFascicoloInfo _createSottoFascicolo(IFascicoloInfo fasc) throws DocerException {


        String anno = "2015";
        IFascicoloInfo fascInfo = new FascicoloInfo();
        fascInfo.setCodiceEnte(fasc.getCodiceEnte());
        fascInfo.setCodiceAOO(fasc.getCodiceAOO());
        fascInfo.setClassifica(fasc.getClassifica());
        fascInfo.setAnnoFascicolo(anno);
        fascInfo.setParentProgressivo(fasc.getProgressivo());
        fascInfo.setProgressivo(UUID.randomUUID().toString());

        p.createFascicolo(fascInfo);

        return fascInfo;
    }

    @Test
    public void createFolder() throws DocerException {
        login();

        IAOOInfo aooInfo = new AOOInfo();
        aooInfo.setCodiceEnte(ente);
        aooInfo.setCodiceAOO(aoo);
        _createFolder(aooInfo);
    }

    public String _createFolder(IAOOInfo aoo) throws DocerException {


        String name = UUID.randomUUID().toString();
        String description = "DESCRIZIONE " + name;

        IFolderInfo fInfo = new FolderInfo();
        fInfo.setCodiceEnte(aoo.getCodiceEnte());
        fInfo.setCodiceAOO(aoo.getCodiceAOO());
        fInfo.setDescrizione(description);
        fInfo.setFolderName(name);

        String folderId = p.createFolder(fInfo);

        return folderId;
    }

    @Test
    public void createFolderOwner() throws DocerException {
        login();
        IAOOInfo aooInfo = new AOOInfo();
        aooInfo.setCodiceEnte(ente);
        aooInfo.setCodiceAOO(aoo);

        IUserProfileInfo user = new UserProfileInfo();
        user.setUserId("admin");

        _createFolderOwner(aooInfo, user);
    }
    public String _createFolderOwner(IAOOInfo aoo, IUserProfileInfo user) throws DocerException {


        String name = UUID.randomUUID().toString();
        String description = "DESCRIZIONE " + name;
        String owner = user.getUserId();

        IFolderInfo fInfo = new FolderInfo();
        fInfo.setCodiceEnte(aoo.getCodiceEnte());
        fInfo.setCodiceAOO(aoo.getCodiceAOO());
        fInfo.setDescrizione(description);
        fInfo.setFolderName(name);
        fInfo.setFolderOwner(owner);

        String folderId = p.createFolder(fInfo);

        return folderId;
    }

    @Test
    public void createUser() throws DocerException {
        login();
        _createUser();
    }

    public IUserProfileInfo _createUser() throws DocerException {


        Map<String,String> extra = new HashMap<String, String>();
        extra.put("exra_custom_1","custom1");
        extra.put("exra_custom_2","custom2");
        extra.put("exra_custom_3","custom3");

        String unique = UUID.randomUUID().toString();

        IUserProfileInfo userInfo = new UserProfileInfo();
        userInfo.setFullName("FULLNAME " + unique);
        userInfo.setUserId("userId"+unique);
        userInfo.setEmailAddress("email@test.it");
        userInfo.setFirstName("FIRSTNAME"+unique);
        userInfo.setLastName("LASTNAME"+unique);
        userInfo.setUserPassword("PWD"+unique);
        userInfo.setEnabled(EnumBoolean.TRUE);
        userInfo.setNetworkAlias("NETWORKALIAS"+unique);
        userInfo.setExtraInfo(extra);

        p.createUser(userInfo);

        return userInfo;
    }

    @Test
    public void createGroup() throws DocerException {
        login();
        IEnteInfo enteInfo = new EnteInfo();
        enteInfo.setCodiceEnte(ente);
        _createGroup(enteInfo);
    }

    public IGroupProfileInfo _createGroup() throws DocerException {
        login();
        IEnteInfo enteInfo = new EnteInfo();
        enteInfo.setCodiceEnte(ente);
        return _createGroup(enteInfo);
    }

    public IGroupProfileInfo _createGroup(IEnteInfo ente) throws DocerException {


        Map<String,String> extra = new HashMap<String, String>();
        extra.put("exra_custom_1","custom1");
        extra.put("exra_custom_2","custom2");
        extra.put("COD_ENTE",ente.getCodiceEnte());

        String unique = UUID.randomUUID().toString();

        IGroupProfileInfo grupInfo = new GroupProfileInfo();
        grupInfo.setGroupName("Everyone"+unique);
        grupInfo.setGroupId("everyone"+unique);
//        grupInfo.setParentGroupId("PARENT GROUP ID TEST SOLR");
        grupInfo.setEnabled(EnumBoolean.TRUE);
        grupInfo.setExtraInfo(extra);

        p.createGroup(grupInfo);

        return grupInfo;
    }

    @Test
    public void createGroupParent() throws DocerException {
        /*login();
        IEnteInfo enteInfo = new EnteInfo();
        enteInfo.setCodiceEnte(ente);
        IGroupProfileInfo groupinfo = new GroupProfileInfo();
        groupinfo.setGroupId(group);

        _createGroupParent(enteInfo, groupinfo);*/
    }

    public IGroupProfileInfo _createGroupParent(IEnteInfo ente, IGroupProfileInfo group) throws DocerException {


        Map<String,String> extra = new HashMap<String, String>();
        extra.put("exra_custom_1","custom1");
        extra.put("exra_custom_2","custom2");
        extra.put("COD_ENTE",ente.getCodiceEnte());

        String unique = UUID.randomUUID().toString();

        IGroupProfileInfo grupInfo = new GroupProfileInfo();
        grupInfo.setGroupName("GROUP TEST"+unique);
        grupInfo.setGroupId("GROUP ID TEST"+unique);
        grupInfo.setParentGroupId(group.getGroupId());
        grupInfo.setEnabled(EnumBoolean.TRUE);
        grupInfo.setExtraInfo(extra);

        p.createGroup(grupInfo);

        return grupInfo;
    }

    @Test
    public void getUser() throws DocerException {

        login();

        IUserInfo info = p.getUser("stefano.vigna");

    }

    @Test
    public void getGroup() throws DocerException {

        login();

        IGroupInfo info = p.getGroup("GROUP ID TEST SOLR");

    }

    @Test
    public void createDocument() throws DocerException {

        login();

        //String ente = "ENTE_COLLAUDO";
        //String aoo = "AOO_COLLAUDO";

//        String ente = "ENTEPROVA";
//        String aoo = "AOOPROVA";

        IAOOInfo aooInfo = new AOOInfo();
        aooInfo.setCodiceEnte(ente);
        aooInfo.setCodiceAOO(aoo);

        _createDocument(aooInfo);
    }

    public String _createDocument() throws DocerException {
        login();

        IAOOInfo aooInfo = new AOOInfo();
        aooInfo.setCodiceEnte(ente);
        aooInfo.setCodiceAOO(aoo);

        return _createDocument(aooInfo);
    }

    public String _createDocument(IAOOInfo aoo) throws DocerException {
        Map<String,String> extra = new HashMap<String, String>();
        extra.put("exra_custom_1","custom1");
        extra.put("exra_custom_2","custom2");
        extra.put("exra_custom_3","custom3");

        String unique = UUID.randomUUID().toString();

        Map<String,String> meta = new HashMap<String, String>();
        meta.put(Fields.Documento.COD_ENTE,aoo.getCodiceEnte());
        meta.put(Fields.Documento.COD_AOO,aoo.getCodiceAOO());
        meta.put(Fields.Documento.DESCRIZIONE,"Descrizione documento"+unique);
        meta.put(Fields.Documento.DOCNAME,"documento\\<>|! "+unique+".txt");

        String fileContent = "TEST FILE CONTENT SOLR PROVIDER";

        InputStream filedoc = new ByteArrayInputStream( fileContent.getBytes() );
        String docNum = p.createDocument("GENERICO", meta, filedoc);

        return docNum;
    }




        @Test
    public void searchUsers() throws DocerException {

        login();

        Map<String,List<String>> criteria = new HashMap();
        criteria.put("COD_AOO", Collections.singletonList("50"));

        String type = SolrBaseUtil.TYPE_USER;
        List<IUserProfileInfo> results = p.searchUsers(criteria);

        log.debug("--------------------------------------------------");
        for (IUserProfileInfo item : results) {
            String logRow = "";
            logRow+="["+item.getUserId()+"]";
            logRow+="["+item.getFirstName()+"]";
            logRow+="["+item.getLastName()+"]";
            logRow+="["+item.getEmailAddress()+"]";
            logRow+="["+item.getFullName()+"]";
            logRow+="["+item.getNetworkAlias()+"]";
            logRow+="["+item.getUserPassword()+"]";
            logRow+="["+item.getEnabled()+"]";

            log.debug(logRow);
            log.debug("--------------------------------------------------");
        }
    }

    @Test
    public void searchGroups() throws DocerException {

        login();

        Map<String,List<String>> criteria = new HashMap();
        criteria.put("COD_AOO", Collections.singletonList("50"));

        String type = SolrBaseUtil.TYPE_GROUP;
        List<IGroupProfileInfo> results = p.searchGroups(criteria);

        log.debug("--------------------------------------------------");
        for (IGroupProfileInfo item : results) {
            String logRow = "";
            logRow+="["+item.getGroupId()+"]";
            logRow+="["+item.getGroupName()+"]";
            logRow+="["+item.getParentGroupId()+"]";
            logRow+="["+item.getEnabled()+"]";

            log.debug(logRow);
            log.debug("--------------------------------------------------");
        }
    }

    @Test
    public void searchFolders() throws DocerException {

        login();

        Map<String,List<String>> criteria = new HashMap();
        criteria.put("COD_AOO", Collections.singletonList("40"));

        Map<String,EnumSearchOrder> orderby = new HashMap<String, EnumSearchOrder>();
        orderby.put("id",EnumSearchOrder.DESC);

        List<String> returnProps = new ArrayList<String>();
        returnProps.add("*");

        DataTable<String> results = p.searchFolders(criteria, returnProps, 5, orderby);

        log.debug("--------------------------------------------------");
        for (DataRow<String> rec : results.getRows()) {
            String logRow = "";
            for (String colName : results.getColumnNames()) {
                logRow+="["+colName + ":" + rec.get(colName) + "]";
            }
            log.debug(logRow);
            log.debug("--------------------------------------------------");
        }
    }

    @Test
    public void searchAnagrafiche() throws DocerException {

        login();

        Map<String,List<String>> criteria = new HashMap();
        criteria.put("COD_AOO", Collections.singletonList("50"));

//        Map<String,EnumSearchOrder> orderby = new HashMap<String, EnumSearchOrder>();
//        orderby.put("id",EnumSearchOrder.DESC);

        List<String> returnProps = new ArrayList<String>();
        returnProps.add("id");
        returnProps.add("SEDE_LAV");
        returnProps.add("PROGR_FASCICOLO");
        returnProps.add("ANNO_FASCICOLO");


        List<String> keywords = new ArrayList<String>();
        keywords.add("test");

        String type = "fascicolo";
        List<Map<String, String>> results = p.searchAnagrafiche(type, criteria, returnProps);

        log.debug("--------------------------------------------------");
        for (Map map : results) {
            String logRow = "";
            for (Object colName : map.keySet()) {
                logRow+="["+colName + ":" + map.get(colName) + "]";
            }
            log.debug(logRow);
            log.debug("--------------------------------------------------");
        }
    }


    @Test
    public void searchAnagraficheEstesa() throws DocerException {

        login();

        Map<String,List<String>> criteria = new HashMap();
        criteria.put("COD_AOO", Collections.singletonList("50"));

        Map<String,EnumSearchOrder> orderby = new HashMap<String, EnumSearchOrder>();
        orderby.put("id",EnumSearchOrder.DESC);

        List<String> returnProps = new ArrayList<String>();
        returnProps.add("id");
        returnProps.add("SEDE_LAV");
        returnProps.add("PROGR_FASCICOLO");
        returnProps.add("ANNO_FASCICOLO");


        List<String> keywords = new ArrayList<String>();
        keywords.add("test");

        String type = SolrBaseUtil.TYPE_FASCICOLO;
        List<Map<String, String>> results = p.searchAnagraficheEstesa(type, criteria, returnProps, 5, orderby);

        log.debug("--------------------------------------------------");
        for (Map map : results) {
            String logRow = "";
            for (Object colName : map.keySet()) {
                logRow+="["+colName + ":" + map.get(colName) + "]";
            }
            log.debug(logRow);
            log.debug("--------------------------------------------------");
        }
    }

    @Test
    public void searchDocuments() throws DocerException {

        login();

        Map<String,List<String>> criteria = new HashMap();
        criteria.put("COD_AOO", Collections.singletonList("50"));

        Map<String,EnumSearchOrder> orderby = new HashMap<String, EnumSearchOrder>();
        orderby.put("id",EnumSearchOrder.DESC);

        List<String> returnProps = new ArrayList<String>();
        returnProps.add("id");
        returnProps.add("indexed_on");
        returnProps.add("type");
        returnProps.add("DOCNUM");

        List<String> keywords = new ArrayList<String>();
        keywords.add("test");


        DataTable<String> results = p.searchDocuments(criteria, keywords, returnProps, 5, orderby);

        log.debug("--------------------------------------------------");
        for (DataRow<String> rec : results.getRows()) {
            String logRow = "";
            for (String colName : results.getColumnNames()) {
                logRow+="["+colName + ":" + rec.get(colName) + "]";
            }
            log.debug(logRow);
            log.debug("--------------------------------------------------");
        }
    }
}


