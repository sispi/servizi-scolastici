package it.kdm.docer.providers.solr;

import it.kdm.docer.providers.solr.bl.anagrafiche.AnagraficaBL;
import it.kdm.docer.providers.solr.bl.documenti.DocumentoBL;
import it.kdm.docer.providers.solr.bl.sicurezza.AttoriBL;
import it.kdm.docer.sdk.EnumACLRights;
import it.kdm.docer.sdk.EnumSearchOrder;
import it.kdm.docer.sdk.IExtendedProvider;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.docer.sdk.interfaces.*;
import it.kdm.utils.DataTable;
import org.slf4j.Logger;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by microchip on 28/04/15.
 */
public class Provider extends SolrBaseUtil implements IExtendedProvider {

    static Logger log = org.slf4j.LoggerFactory.getLogger(Provider.class.getName());

    private String ROOT_TICKET = "admin";
    private ILoggedUserInfo currentUser;

    public Provider() throws DocerException {
        super();
    }


    @Override
    public String login(String username, String password, String ente) throws DocerException {
        AttoriBL manager = new AttoriBL();
        return manager.login(username,password,ente);
    }

    @Override
    public ISsoUserInfo loginSSO(String saml, String codiceEnte) throws DocerException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void logout() throws DocerException {

    }

    @Override
    public void setCurrentUser(ILoggedUserInfo loggedUserInfo) throws DocerException {
        this.currentUser = loggedUserInfo;
    }

    @Override
    public String createDocument(String documentType, Map<String, String> profileInfo, InputStream documentFile) throws DocerException {
        DocumentoBL manager = new DocumentoBL();
        return manager.createDocument(this.currentUser, documentType, profileInfo, documentFile);
    }

    @Override
    public DataTable<String> searchDocuments(Map<String, List<String>> searchCriteria, List<String> keywords, List<String> returnProperties, int maxResults, Map<String, EnumSearchOrder> orderBy) throws DocerException {
        DocumentoBL manager = new DocumentoBL();
        return manager.search(this.currentUser, searchCriteria, keywords, returnProperties, maxResults, orderBy);
    }

    @Override
    public List<String> getRelatedDocuments(String docId) throws DocerException {
        DocumentoBL manager = new DocumentoBL();
        return manager.getRelatedDocuments(this.currentUser,docId);
    }

    @Override
    public void addRelatedDocuments(String docId, List<String> items) throws DocerException {
        DocumentoBL manager = new DocumentoBL();
        manager.addRelatedDocuments(this.currentUser,docId,items);
    }

    @Override
    public void removeRelatedDocuments(String docId, List<String> items) throws DocerException {
        DocumentoBL manager = new DocumentoBL();
        manager.removeRelatedDocuments(this.currentUser, docId, items);
    }

    @Override
    public List<String> getRiferimentiDocuments(String docId) throws DocerException {
        DocumentoBL manager = new DocumentoBL();
        return manager.getRiferimentiDocuments(this.currentUser, docId);
    }

    @Override
    public void addRiferimentiDocuments(String docId, List<String> riferimenti) throws DocerException {
        DocumentoBL manager = new DocumentoBL();
        manager.addRiferimentiDocuments(this.currentUser, docId, riferimenti);
    }

    @Override
    public void removeRiferimentiDocuments(String docId, List<String> riferimenti) throws DocerException {
        DocumentoBL manager = new DocumentoBL();
        manager.removeRiferimentiDocuments(this.currentUser, docId, riferimenti);
    }

    @Override
    public IUserInfo getUser(String userId) throws DocerException {
        AttoriBL manager = new AttoriBL();
        return manager.getUser(this.currentUser,userId);
    }

    @Override
    public IGroupInfo getGroup(String groupId) throws DocerException {
        AttoriBL manager = new AttoriBL();
        return manager.getGroup(this.currentUser, groupId);
    }

    @Override
    public void updateEnte(IEnteId enteId, IEnteInfo enteNewInfo) throws DocerException {
        AnagraficaBL manager = new AnagraficaBL();
        manager.updateEnte(this.currentUser, enteId, enteNewInfo);
    }

    @Override
    public void updateAOO(IAOOId aooId, IAOOInfo aooInfo) throws DocerException {
        AnagraficaBL manager = new AnagraficaBL();
        manager.updateAOO(this.currentUser, aooId, aooInfo);
    }

    @Override
    public void updateTitolario(ITitolarioId titolarioId, ITitolarioInfo titolarioInfo) throws DocerException {
        AnagraficaBL manager = new AnagraficaBL();
        manager.updateTitolario(this.currentUser, titolarioId, titolarioInfo);
    }

    @Override
    public void updateFascicolo(IFascicoloId fascicoloId, IFascicoloInfo fascicoloInfo) throws DocerException {
        AnagraficaBL manager = new AnagraficaBL();
        manager.updateFascicolo(this.currentUser, fascicoloId, fascicoloInfo);
    }

    @Override
    public void updateCustomItem(ICustomItemId customItemId, ICustomItemInfo customItemInfo) throws DocerException {
        AnagraficaBL manager = new AnagraficaBL();
        manager.updateCustomItem(this.currentUser, customItemId, customItemInfo);
    }

    @Override
    public void updateUser(String userId, IUserProfileInfo userNewInfo) throws DocerException {
        AttoriBL manager = new AttoriBL();
        manager.updateUser(this.currentUser, userId, userNewInfo);
    }

    @Override
    public void updateGroup(String groupId, IGroupProfileInfo groupNewInfo) throws DocerException {
        AttoriBL manager = new AttoriBL();
        manager.updateGroup(this.currentUser, groupId, groupNewInfo);
    }

    @Override
    public void removeGroupsFromUser(String userId, List<String> groups) throws DocerException {
        AttoriBL manager = new AttoriBL();
        manager.removeGroupsFromUser(this.currentUser,userId,groups);
    }

    @Override
    public void removeUsersFromGroup(String groupId, List<String> users) throws DocerException {
        AttoriBL manager = new AttoriBL();
        manager.removeUsersFromGroup(this.currentUser,groupId,users);
    }

    @Override
    public void addGroupsToUser(String userId, List<String> groups) throws DocerException {
        AttoriBL manager = new AttoriBL();
        manager.addGroupsToUser(this.currentUser,userId, groups);
    }

    @Override
    public void addUsersToGroup(String groupId, List<String> users) throws DocerException {
        AttoriBL manager = new AttoriBL();
        manager.addUsersToGroup(this.currentUser,groupId,users);
    }

    @Override
    public void createEnte(IEnteInfo enteInfo) throws DocerException {
        AnagraficaBL manager = new AnagraficaBL();
        manager.createEnte(this.currentUser, enteInfo);
    }

    @Override
    public void createAOO(IAOOInfo aooInfo) throws DocerException {
        AnagraficaBL manager = new AnagraficaBL();
        manager.createAoo(this.currentUser, aooInfo);
    }

    @Override
    public void createTitolario(ITitolarioInfo titolarioInfo) throws DocerException {
        AnagraficaBL manager = new AnagraficaBL();
        manager.createTitolario(this.currentUser, titolarioInfo);
    }

    @Override
    public void createFascicolo(IFascicoloInfo fascicoloInfo) throws DocerException {
        AnagraficaBL manager = new AnagraficaBL();
        manager.createFascicolo(this.currentUser, fascicoloInfo);
    }

    @Override
    public void createCustomItem(ICustomItemInfo customItemInfo) throws DocerException {
        AnagraficaBL manager = new AnagraficaBL();
        manager.createCustomItem(this.currentUser, customItemInfo);
    }

    @Override
    public void createUser(IUserProfileInfo userProfileInfo) throws DocerException {
        AttoriBL manager = new AttoriBL();
        manager.createUser(this.currentUser, userProfileInfo);
    }

    @Override
    public void createGroup(IGroupProfileInfo groupProfileInfo) throws DocerException {
        AttoriBL manager = new AttoriBL();
        manager.createGroup(this.currentUser, groupProfileInfo);
    }

    @Override
    public void setACLDocument(String docId, Map<String, EnumACLRights> acls) throws DocerException {
        DocumentoBL manager = new DocumentoBL();
        manager.setACLDocument(this.currentUser, docId, acls);
    }

    @Override
    public Map<String, EnumACLRights> getACLDocument(String docId) throws DocerException {
        DocumentoBL manager = new DocumentoBL();
        return manager.getACLDocument(this.currentUser, docId);
    }

    @Override
    public ILockStatus isCheckedOutDocument(String docId) throws DocerException {
        DocumentoBL manager = new DocumentoBL();
        return manager.isCheckedOutDocument(this.currentUser, docId);
    }

    @Override
    public void lockDocument(String docId) throws DocerException {
        DocumentoBL manager = new DocumentoBL();
        manager.lockDocument(this.currentUser, docId);
    }

    @Override
    public void unlockDocument(String docId) throws DocerException {
        DocumentoBL manager = new DocumentoBL();
        manager.unlockDocument(this.currentUser, docId);
    }

    @Override
    public void updateProfileDocument(String docId, Map<String, String> metaDati) throws DocerException {
        DocumentoBL manager = new DocumentoBL();
        manager.updateProfileDocument(this.currentUser, docId, metaDati);
    }

    @Override
    public void deleteDocument(String docId) throws DocerException {
        DocumentoBL manager = new DocumentoBL();
        manager.deleteDocument(this.currentUser, docId);
    }

    @Override
    public InputStream streamLastVersion(String docId) throws DocerException {
        DocumentoBL manager = new DocumentoBL();
        return manager.streamLastVersion(currentUser,docId);
    }

    @Override
    public InputStream streamVersion(String docId, String versionNumber) throws DocerException {
        DocumentoBL manager = new DocumentoBL();
        return manager.streamVersion(currentUser,docId,versionNumber);
    }

    @Override
    public byte[] downloadLastVersion(String docId, String destinationFilePath, long maxFileLength) throws DocerException {
        DocumentoBL manager = new DocumentoBL();
        return manager.downloadLastVersion(this.currentUser,docId, destinationFilePath,maxFileLength);
    }

    @Override
    public byte[] downloadVersion(String docId, String versionNumber, String destinationFilePath, long maxFileLength) throws DocerException {
        DocumentoBL manager = new DocumentoBL();
        return manager.downloadVersion(this.currentUser,docId,versionNumber,destinationFilePath,maxFileLength);
    }

    @Override
    public List<String> getVersions(String docId) throws DocerException {
        DocumentoBL manager = new DocumentoBL();
        return manager.getVersions(this.currentUser,docId);
    }

    @Override
    public String addNewVersion(String docId, InputStream documentFile) throws DocerException {
        DocumentoBL manager = new DocumentoBL();
        return manager.addNewVersion(this.currentUser,docId,documentFile);
    }

    @Override
    public void replaceLastVersion(String docId, InputStream content) throws DocerException {
        DocumentoBL manager = new DocumentoBL();
        manager.replaceLastVersion(this.currentUser, docId, content);
    }

    @Override
    public List<Map<String, String>> searchAnagrafiche(String type, Map<String, List<String>> searchCriteria, List<String> returnProperties) throws DocerException {
        AnagraficaBL manager = new AnagraficaBL();
        return manager.search(this.currentUser, type, searchCriteria, returnProperties);
    }

    @Override
    public List<Map<String, String>> searchAnagraficheEstesa(String type, Map<String, List<String>> searchCriteria, List<String> returnProperties, int maxResults, Map<String, EnumSearchOrder> orderby) throws DocerException {
        AnagraficaBL manager = new AnagraficaBL();
        return manager.search(this.currentUser, type, searchCriteria, returnProperties, maxResults, orderby);
    }

    @Override
    public List<IUserProfileInfo> searchUsers(Map<String, List<String>> searchCriteria) throws DocerException {
        AttoriBL manager = new AttoriBL();
        return manager.searchUsers(currentUser, searchCriteria);
    }

    @Override
    public List<IGroupProfileInfo> searchGroups(Map<String, List<String>> searchCriteria) throws DocerException {
        AttoriBL manager = new AttoriBL();
        return manager.searchGroups(currentUser, searchCriteria);
    }

    @Override
    public List<IHistoryItem> getHistory(String docId) throws DocerException {
        //not implemented!!!
        return new ArrayList<IHistoryItem>();
    }

    @Override
    public EnumACLRights getEffectiveRights(String docId, String userid) throws DocerException {
        DocumentoBL manager = new DocumentoBL();
        return manager.getEffectiveRights(this.currentUser, docId, userid);
    }

    @Override
    public EnumACLRights getEffectiveRightsAnagrafiche(String type, Map<String, String> id, String userid) throws DocerException {
        AnagraficaBL manager = new AnagraficaBL();
        return manager.getEffectiveRightsAnagrafiche(this.currentUser,type,id,userid);
    }

    @Override
    public EnumACLRights getEffectiveRightsFolder(String folderId, String userid) throws DocerException {
        AnagraficaBL manager = new AnagraficaBL();
        return manager.getEffectiveRightsFolder(this.currentUser,folderId,userid);
    }

    @Override
    public boolean verifyTicket(String userId, String codiceEnte, String ticket) throws DocerException {
        return true;
    }

    @Override
    public void setACLTitolario(ITitolarioId titolarioId, Map<String, EnumACLRights> acls) throws DocerException {
        AnagraficaBL manager = new AnagraficaBL();
        manager.setACLTitolario(this.currentUser, titolarioId, acls);
    }

    @Override
    public Map<String, EnumACLRights> getACLTitolario(ITitolarioId titolarioId) throws DocerException {
        AnagraficaBL manager = new AnagraficaBL();
        return manager.getACLTitolario(this.currentUser, titolarioId);
    }

    @Override
    public void setACLFascicolo(IFascicoloId fascicoloId, Map<String, EnumACLRights> acls) throws DocerException {
        AnagraficaBL manager = new AnagraficaBL();
        manager.setACLFascicolo(this.currentUser, fascicoloId, acls);
    }

    @Override
    public Map<String, EnumACLRights> getACLFascicolo(IFascicoloId fascicoloId) throws DocerException {
        AnagraficaBL manager = new AnagraficaBL();
        return manager.getACLFascicolo(this.currentUser,fascicoloId);
    }

    @Override
    public String createFolder(IFolderInfo folderInfo) throws DocerException {
        AnagraficaBL manager = new AnagraficaBL();
        return manager.createFolder(this.currentUser, folderInfo);
    }

    @Override
    public void updateFolder(String folderId, IFolderInfo folderNewInfo) throws DocerException {
        AnagraficaBL manager = new AnagraficaBL();
        manager.updateFolder(this.currentUser, folderId, folderNewInfo);
    }

    @Override
    public void deleteFolder(String folderId) throws DocerException {
        AnagraficaBL manager = new AnagraficaBL();
        manager.deleteFolder(this.currentUser, folderId);
    }

    @Override
    public void setACLFolder(String folderId, Map<String, EnumACLRights> acls) throws DocerException {
        AnagraficaBL manager = new AnagraficaBL();
        manager.setACLFolder(this.currentUser, folderId, acls);
    }

    @Override
    public Map<String, EnumACLRights> getACLFolder(String folderId) throws DocerException {
        AnagraficaBL manager = new AnagraficaBL();
        return manager.getACLFolder(this.currentUser, folderId);
    }

    @Override
    public List<String> getFolderDocuments(String folderId) throws DocerException {
        DocumentoBL manager = new DocumentoBL();
        return manager.getFolderDocuments(this.currentUser,folderId);
    }

    @Override
    public void addToFolderDocuments(String folderId, List<String> documents) throws DocerException {
        DocumentoBL manager = new DocumentoBL();
        manager.addToFolderDocuments(this.currentUser,folderId, documents);
    }

    @Override
    public void removeFromFolderDocuments(String folderId, List<String> documents) throws DocerException {
        DocumentoBL manager = new DocumentoBL();
        manager.removeFromFolderDocuments(this.currentUser,folderId,documents);
    }

    @Override
    public DataTable<String> searchFolders(Map<String, List<String>> searchCriteria, List<String> returnProperties, int maxResults, Map<String, EnumSearchOrder> orderby) throws DocerException {
        AnagraficaBL manager = new AnagraficaBL();
        return manager.searchFolders(currentUser,searchCriteria,returnProperties,maxResults,orderby);
    }

    @Override
    public void addNewAdvancedVersion(String docIdLastVersion, String docIdNewVersion) throws DocerException {
        DocumentoBL manager = new DocumentoBL();
        manager.addNewAdvancedVersion(this.currentUser,docIdLastVersion,docIdNewVersion);
    }

    @Override
    public List<String> getAdvancedVersions(String docId) throws DocerException {
        DocumentoBL manager = new DocumentoBL();
        return manager.getAdvancedVersions(this.currentUser,docId);
    }

    @Override
    public List<Map<String, String>> getRiferimentiFascicolo(IFascicoloId fascicoloId) throws DocerException {
        AnagraficaBL manager = new AnagraficaBL();
        return manager.getRiferimentiFascicolo(this.currentUser, fascicoloId);

    }

    @Override
    public void addRiferimentiFascicolo(IFascicoloId fascicoloId, List<IFascicoloId> riferimenti) throws DocerException {
        AnagraficaBL manager = new AnagraficaBL();
        manager.addRiferimentiFascicolo(this.currentUser, fascicoloId, riferimenti);
    }

    @Override
    public void removeRiferimentiFascicolo(IFascicoloId fascicoloId, List<IFascicoloId> riferimenti) throws DocerException {
        AnagraficaBL manager = new AnagraficaBL();
        manager.removeRiferimentiFascicolo(this.currentUser, fascicoloId, riferimenti);
    }
}
