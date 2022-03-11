package it.kdm.docer.sdk;

import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.docer.sdk.interfaces.*;
import it.kdm.utils.DataTable;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Interfaccia che deve essere implementata da ogni provider DOC/ER
 * @author kdm
 *
 */
public interface IProvider {

	/**
	 * Esegue l'apertura della sessione di lavoro nel sistema documentale
	 * 
	 * @param userName Userid dell'utente che deve eseguire il login nel sistema documentale
	 * @param password La password dell'utente per l'accesso al sistema documentale
	 * @param codiceEnte Identificativo univoco dell'Ente su cui si intende operare
	 * @return Il ticket di sessione
	 * @throws DocerException
	 */
	public String login(String userName, String password, String codiceEnte) throws DocerException;

    /**
     * Esegue l'apertura della sessione di lavoro nel sistema documentale via SSO
     *
     * @param saml
     * @param codiceEnte
     * @return
     * @throws DocerException
     */
    public ISsoUserInfo loginSSO(String saml, String codiceEnte) throws DocerException;
	
	/**
	 * Esegue la chiusura della sessione di lavoro dell'utente 
	 * 
	 * @throws DocerException
	 */
	public void logout() throws DocerException;
	
	/**
	 * Imposta l'utente operatore per l'istanza del Provider
	 * 
	 * @param info Oggetto che contiene le informazioni dell'utente operatore (userid, codiceEnte, ticket)
	 * @throws DocerException
	 */
	public void setCurrentUser(ILoggedUserInfo info) throws DocerException;
	
	/**
	 * Esegue la creazione di un documento
	 * 
	 * @param documentType Il tipo ovvero il TYPE_ID del documento
	 * @param profileInfo Metadati del profilo del documento (come coppie chiave-valore)
	 * @param file Documento fisico (null se il tipo di documento non richiede un documento fisico)
	 * @return id del documento
	 * @throws DocerException
	 */
	public String createDocument(String documentType, Map<String, String> profileInfo, InputStream file) throws DocerException;
		
	/**
	 * Esegue una ricerca di documenti
	 * 
	 * @param searchCriteria Criteri di ricerca (criteri per la stessa chiave sono in OR logico, per chiavi diverse in AND logico)
	 * @param keywords Parole chiave da ricercare nel testo contenuto nel documento fisico e/o nei metadati di profilo (dipende dalle funzionalita' del sistema documentale)
	 * @param returnProperties Metadati che devono essere restituiti dalla ricerca
	 * @param maxResults Numero massimo dei risultati
	 * @param orderby Criteri di ordinamento dei risultati, nome del metadato (key) - ASC/DESC (value)
	 * @return DataTable che contiene un array di DataRow - ogni DataRow rappresenta il profilo di un risultato
	 * @throws DocerException
	 */
	public DataTable<String> searchDocuments(Map<String, List<String>> searchCriteria, List<String> keywords, List<String> returnProperties,int maxResults, Map<String,EnumSearchOrder> orderby) throws DocerException;	
	
	/**
	 * Recupera la lista dei documenti related al documento con ID docId
	 * 
	 * @param docId Id del documento
	 * @return Lista di Id dei related
	 * @throws DocerException
	 */
	public List<String> getRelatedDocuments(String docId) throws DocerException;
	
	/**
	 * Aggiunge elementi alla catena dei related di un documento
	 * 
	 * @param docId Id del documento
	 * @param related Lista degli Id dei documenti da aggiungere alla catena dei related del documento docId
	 * @throws DocerException
	 */
	public void addRelatedDocuments(String docId, List<String> related) throws DocerException;
	
	/**
	 * Elimina elementi dalla catena dei related di un documento
	 * 
	 * @param docId Id del documento
	 * @param related Lista degli Id dei documenti da eliminare dalla catena dei related del documento dodId
	 * @throws DocerException
	 */
	public void removeRelatedDocuments(String docId, List<String> related) throws DocerException;
	
	/**
	 * Recupera la lista dei riferimenti del documento docId
	 * 
	 * @param docId Id del documento
	 * @return L'insieme dei riferimenti del documento docId
	 * @throws DocerException
	 */
	public List<String> getRiferimentiDocuments(String docId) throws DocerException;
	
	/**
	 * Aggiunge riferimenti al documento docId
	 * 
	 * @param docId Id del documento
	 * @param riferimenti Lista dei riferimenti da aggiungere all'insieme dei riferimenti del documento docId
	 * @throws DocerException
	 */
	public void addRiferimentiDocuments(String docId, List<String> riferimenti) throws DocerException;
	
	/**
	 * Elimina riferimenti dal documento docId
	 * 
	 * @param docId Id del documento
	 * @param riferimenti Lista dei riferimenti da eliminare dall'insieme dei riferimenti del documento docId
	 * @throws DocerException
	 */
	public void removeRiferimentiDocuments(String docId, List<String> riferimenti) throws DocerException;
	
	/**
	 * Recupera il profilo di un Utente insieme alla lista dei Gruppi a cui e' associato
	 * 
	 * @param userId Id dell'Utente
	 * @return Profilo dell'Utente insieme alla lista dei group-id dei Gruppi a cui esso e' associato
	 * @throws DocerException
	 */
	public IUserInfo getUser(String userId) throws DocerException;

	/**
	 * Recupera il profilo di un Gruppo insieme alla lista degli Utenti ad esso associati
	 * 
	 * @param groupId Id del Gruppo
	 * @return Profilo del Gruppo insieme alla lista degli user-id degli Utenti ad esso associati
	 * @throws DocerException
	 */
	public IGroupInfo getGroup(String groupId) throws DocerException;
	
	/**
	 * Aggiorna il profilo di un'anagrafica Ente
	 * 
	 * @param enteId Id dell'anagrafica Ente
	 * @param enteNewInfo Profilo dell'Ente contenente i metadati da modificare
	 * @throws DocerException
	 */
	public void updateEnte(IEnteId enteId, IEnteInfo enteNewInfo) throws DocerException;
	
	/**
	 * Aggiora il profilo di un'anagrafica AOO
	 * 
	 * @param aooId Id dell'anagrafica AOO
	 * @param aooNewInfo Profilo della AOO contenente i metadati da modificare
	 * @throws DocerException
	 */
	public void updateAOO(IAOOId aooId, IAOOInfo aooNewInfo) throws DocerException;
	
	/**
	 * Aggiora il profilo di un'anagrafica voce di Titolario
	 * 
	 * @param titolarioId Id dell'anagrafica voce di Titolario
	 * @param titolarioNewInfo Profilo della voce di Titolario contenente i metadati da modificare
	 * @throws DocerException
	 */
	public void updateTitolario(ITitolarioId titolarioId, ITitolarioInfo titolarioNewInfo) throws DocerException;	
	
	/**
	 * Aggiora il profilo di un'anagrafica Fascicolo
	 * 
	 * @param fascicoloId Id dell'anagrafica Fascicolo
	 * @param fascicoloNewInfo Profilo dell'anagrafica Fascicolo contenente i metadati da modificare
	 * @throws DocerException
	 */
	public void updateFascicolo(IFascicoloId fascicoloId, IFascicoloInfo fascicoloNewInfo) throws DocerException;
	
	/**
	 * Aggiora il profilo di un'anagrafica custom
	 * 
	 * @param customId Id dell'anagrafica custom
	 * @param customNewInfo Profilo dell'anagrafica custom contenente i metadati da modificare
	 * @throws DocerException
	 */
	public void updateCustomItem(ICustomItemId customId, ICustomItemInfo customNewInfo) throws DocerException;
	
	/**
	 * Esegue l'aggiornamento del profilo di un Utente
	 * 
	 * @param userId Id dell'Utente da aggiornare
	 * @param userNewInfo Il profilo dell'Utente contenente i metadati da modificare
	 * @throws DocerException
	 */
	public void updateUser(String userId, IUserProfileInfo userNewInfo) throws DocerException;
	
	/**
	 * Esegue l'aggiornamento del profilo di un Gruppo
	 * 
	 * @param groupId Id del Gruppo da aggiornare
	 * @param groupNewInfo Il profilo del Gruppo contenente i metadati da modificare
	 * @throws DocerException
	 */
	public void updateGroup(String groupId, IGroupProfileInfo groupNewInfo) throws DocerException; 		

	/**
	 * Elimina l'associazione tra un Utente ed una lista di Gruppi a cui esso e' associato
	 * 
	 * @param userId Id dell'Utente
	 * @param groups List di Id dei Gruppi da cui l'Utente deve essere rimosso 
	 * @throws DocerException
	 */
	public void removeGroupsFromUser(String userId, List<String> groups) throws DocerException;
	
	/**
	 * Elimina l'associazione tra un Gruppo ed una lista di Utenti ad esso associati
	 * 
	 * @param groupId Id del Gruppo
	 * @param users Id degli Utenti da rimuovere dal Gruppo
	 * @throws DocerException
	 */
	public void removeUsersFromGroup(String groupId, List<String> users) throws DocerException;
	
	/**
	 * Esegue l'associazione di un Utente ad una Lista di Gruppi
	 * 
	 * @param userId Id dell'Utente
	 * @param groups Lista di Id dei Gruppi da associare all'Utente
	 * @throws DocerException
	 */
	public void addGroupsToUser(String userId, List<String> groups) throws DocerException;		
	
	/**
	 * Esegue l'associazione ad un Gruppo di una lista di Utenti
	 * @param groupId Id del Gruppo
	 * @param users Lista di Id degli Utenti da associare al Gruppo
	 * @throws DocerException
	 */
	public void addUsersToGroup(String groupId, List<String> users) throws DocerException;
	
	/**
	 * Esegue la creazione di un'anagrafica Ente e del relativo Gruppo Ente
	 * 
	 * @param enteInfo Profilo dell'anagrafica Ente
	 * @throws DocerException
	 */
	public void createEnte(IEnteInfo enteInfo) throws DocerException;
	
	/**
	 * Esegue la creazione di un'anagrafica AOO
	 * 
	 * @param aooInfo Profilo dell'anagrafica AOO
	 * @throws DocerException
	 */
	public void createAOO(IAOOInfo aooInfo) throws DocerException;
	
	/**
	 * Esegue la creazione di un'anagrafica voce di Titolario
	 * 
	 * @param titolarioInfo Profilo dell'anagrafica voce di Titolario
	 * @throws DocerException
	 */
	public void createTitolario(ITitolarioInfo titolarioInfo) throws DocerException;
	
	/**
	 * Esegue la creazione di un'anagrafica Fascicolo
	 * 
	 * @param fascicoloInfo Profilo dell'anagrafica Fascicolo
	 * @throws DocerException
	 */
	public void createFascicolo(IFascicoloInfo fascicoloInfo) throws DocerException;
	
	/**
	 * Esegue la creazione di un'anagrafica custom
	 * 
	 * @param customItemInfo Profilo dell'anagrafica custom
	 * @throws DocerException
	 */
	public void createCustomItem(ICustomItemInfo customItemInfo) throws DocerException;
	
	/**
	 * Esegue la creazione di un Utente
	 * 
	 * @param userProfile Profilo dell'Utente
	 * @throws DocerException
	 */
	public void createUser(IUserProfileInfo userProfile) throws DocerException;
	
	/**
	 * Esegue la creazione di un Gruppo
	 * 
	 * @param groupProfile Profilo del Gruppo
	 * @throws DocerException
	 */
	public void createGroup(IGroupProfileInfo groupProfile) throws DocerException;
	
	/**
	 * Assegna le ACL ad un Documento (con sovrascrittura)
	 * 
	 * @param docId Id del documento
	 * @param acls ACL da assegnare al documento, Id di Utenti e Gruppi (key) - permesso (value)
	 * @throws DocerException
	 */
	public void setACLDocument(String docId, Map<String, EnumACLRights> acls) throws DocerException;
	
	/**
	 * Recupera la ACL assegnate ad un documento
	 * 
	 * @param docId Id del documeto
	 * @return Lista delle ACL del documento, Id di Utenti e Gruppi (key) - permesso (value)
	 * @throws DocerException
	 */
	public Map<String, EnumACLRights> getACLDocument(String docId) throws DocerException;
	
	/**
	 * Recupera lo stato di blocco esclusivo di un documento
	 * 
	 * @param docId Id del documento
	 * @return Oggetto che contiene le informazioni sullo stato di blocco esclusico
	 * @throws DocerException
	 */
	public ILockStatus isCheckedOutDocument(String docId) throws DocerException;
	
	/**
	 * Esegue il blocco esclusivo di un documento
	 * 
	 * @param docId Id del documento
	 * @throws DocerException
	 */
	public void lockDocument(String docId) throws DocerException;
	
	/**
	 * Esegue lo sblocco di un documento in stato di blocco esclusivo
	 * 
	 * @param docId Id del documento
	 * @throws DocerException
	 */
	public void unlockDocument(String docId) throws DocerException;
	
	/**
	 * Esegue l'aggiornamento del profilo di un documento
	 * 
	 * @param docId Id del documento
	 * @param metadata I metadati da modificare nel profilo del documento
	 * @throws DocerException
	 */
	public void updateProfileDocument(String docId, Map<String, String> metadata) throws DocerException;
	
	/**
	 * Esegue la cancellazione di un documento nel sistema documentale
	 * 
	 * @param docId Id del documento
	 * @throws DocerException
	 */
	public void deleteDocument(String docId) throws DocerException;
	
	/**
	 * Recupera l'ultima versione del File associato al documento
	 * 
	 * @param docId Id del documento
	 * @param destinationFilePath Percorso fisico, univoco, su cui deve essere scritto lo Stream
	 * del file nel caso in cui la sua dimensione superi il parametro maxFileLength (in bytes)
	 * @param maxFileLength Dimensione massima del file fisico entro la quale il file deve essere 
	 * restituito come byte[], altrimenti, salvato su destinationFilePath
	 * @return Il file fisico se con dimensione minore o uguale a maxFilePath altrimenti null
	 * @throws DocerException
	 */
	public byte[] downloadLastVersion(String docId, String destinationFilePath, long maxFileLength) throws DocerException;
	
	/**
	 * Recupera la lista delle versioni del documento
	 *  
	 * @param docId Id del documento
	 * @return La lista degli Id delle versioni del documento
	 * @throws DocerException
	 */
	public List<String> getVersions(String docId) throws DocerException;

	/**
	 * Crea una nuova versione del file del documento
	 * 
	 * @param docId Id del documento
	 * @param file Il documento fisico
	 * @return Id della versione creata
	 * @throws DocerException
	 */
	public String addNewVersion(String docId, InputStream file) throws DocerException;
	
	/**
	 * Sostituisce l'ultima versione del file del documento
	 * 
	 * @param docId Id del documento
	 * @param file Documento fisico
	 * @throws DocerException
	 */
	public void replaceLastVersion(String docId,InputStream file) throws DocerException;
	
	/**
	 * Recupera il file del documento nella versione richiesta
	 * 
	 * @param docId Id del documento
	 * @param versionNumber Id della versione
	 * @param destinationFilePath Percorso fisico, univoco, su cui deve essere scritto lo Stream
	 * del file nel caso in cui la sua dimensione superi il parametro maxFileLength (in bytes)
	 * @param maxFileLength Dimensione massima del file fisico entro la quale il file deve essere 
	 * restituito come byte[], altrimenti, salvato su destinationFilePath
	 * @return Il file fisico della versione se con dimensione minore o uguale a maxFilePath altrimenti null
	 * @throws DocerException
	 */
	public byte[] downloadVersion(String docId, String versionNumber, String destinationFilePath, long maxFileLength) throws DocerException;
	
	/**
	 * Esegue una ricerca tra le anagrafiche, per tipo
	 * 
	 * @param type Tipo o TYPE_ID dell'anagrafica (ENTE, AOO, TITOLARIO, FASCICOLO, oggetti custom...)
	 * @param searchCriteria Criteri di ricerca (criteri per la stessa key sono in OR logico, per key diverse in AND logico)
	 * @param returnProperties Metadati che devono essere restituiti dalla ricerca
	 * @return Lista dei profili trovati per il tipo di anagrafica cercata
	 * @throws DocerException
	 */
	public List<Map<String,String>> searchAnagrafiche(String type, Map<String, List<String>> searchCriteria, List<String> returnProperties) throws DocerException;

    /**
     * Esegue una ricerca tra le anagrafiche, per tipo
     *
     * @param type Tipo o TYPE_ID dell'anagrafica (ENTE, AOO, TITOLARIO, FASCICOLO, oggetti custom...)
     * @param searchCriteria Criteri di ricerca (criteri per la stessa key sono in OR logico, per key diverse in AND logico)
     * @param returnProperties Metadati che devono essere restituiti dalla ricerca
     * @param maxResults Numero massimo dei risultati
     * @param orderby Criteri di ordinamento dei risultati, nome del metadato (key) - ASC/DESC (value)
     * @return Lista dei profili trovati per il tipo di anagrafica cercata
     * @throws DocerException
     */
    public List<Map<String,String>> searchAnagraficheEstesa(String type, Map<String, List<String>> searchCriteria, List<String> returnProperties, int maxResults, Map<String, EnumSearchOrder> orderby) throws DocerException;

    /**
	 * Esegue una ricerca tra gli Utenti del sistema documentale 
	 * 
	 * @param searchCriteria Criteri di ricerca (criteri per la stessa key sono in OR logico, per key diverse in AND logico) 
	 * @return Lista dei profili degli Utenti trovati
	 * @throws DocerException
	 */
	public List<IUserProfileInfo> searchUsers(Map<String, List<String>> searchCriteria) throws DocerException;
	
	/**
	 * Esegue una ricerca tra i Gruppi del sistema documentale 
	 * 
	 * @param searchCriteria Criteri di ricerca (criteri per la stessa key sono in OR logico, per key diverse in AND logico) 
	 * @return Lista dei profili dei Gruppi trovati
	 * @throws DocerException
	 */
	public List<IGroupProfileInfo> searchGroups(Map<String, List<String>> searchCriteria) throws DocerException;	
	
	/**
	 * Recupera la History di un documento
	 * 
	 * @param docId Id del documento
	 * @return Lista degli delle azioni eseguite sul documento (il numero ed il tipo di storicizzazione delle azioni dipende dal sistema documentale)
	 * @throws DocerException
	 */
	public List<IHistoryItem> getHistory(String docId) throws DocerException;
	
	/**
	 * Recupera i diritti equivalenti (o efficaci) di un Utente su un documento considerando anche i permessi impliciti ed i permessi assegnati ai suoi Gruppi di appartenenza
	 * 
	 * @param docId Id del documento
	 * @param userid Id dell'Utente
	 * @return Diritti equivalenti (o efficaci) dell'Utente sul documento
	 * @throws DocerException
	 */
	public EnumACLRights getEffectiveRights(String docId, String userid) throws DocerException;
	
	/**
	 * Recupera i diritti equivalenti (o efficaci) di un Utente su un documento considerando anche i permessi impliciti ed i permessi assegnati ai suoi Gruppi di appartenenza
	 * 
	 * @param id dell' anagrafica
	 * @param userid Id dell'Utente
	 * @return Diritti equivalenti (o efficaci) dell'Utente sull'anagrafica
	 * @throws DocerException
	 */
	public EnumACLRights getEffectiveRightsAnagrafiche(String type, Map<String,String> id, String userid) throws DocerException;	
	
	/**
	 * Recupera i diritti equivalenti (o efficaci) di un Utente su un documento considerando anche i permessi impliciti ed i permessi assegnati ai suoi Gruppi di appartenenza
	 * 
	 * @param folderId id della folder
	 * @param userid Id dell'Utente
	 * @return Diritti equivalenti (o efficaci) dell'Utente sulla folder
	 * @throws DocerException
	 */
	public EnumACLRights getEffectiveRightsFolder(String folderId, String userid) throws DocerException;
	
	/**
	 * Verifica la validita' di un Ticket di sessione
	 * 
	 * @param userId Id dell'Utente
	 * @param codiceEnte Identificativo univoco dell'Ente su cui si intente operare
	 * @param ticket Ticket di sessione
	 * @return true se il Ticket di sessione e' ancora valido altrimenti false
	 * @throws DocerException
	 */
	public boolean verifyTicket(String userId, String codiceEnte, String ticket) throws DocerException;
	
	/**
	 * Assegna le ACL ad una voce di Titolario (con sovrascrittura)
	 * 
	 * @param titolarioId Id della voce di Titolario
	 * @param acls Lista delle ACL della voce di Titolario: ad ogni Id di Utente o Gruppo (key), corrisponde un permesso (value)
	 * @throws DocerException
	 */
	public void setACLTitolario(ITitolarioId titolarioId, Map<String, EnumACLRights> acls) throws DocerException;
	
	/**
	 * Recupera la lista dei permessi di una voce di Titolario
	 * 
	 * @param titolarioId Id della voce di Titolario
	 * @return Lista delle ACL della voce di Titolario: ad ogni Id di Utente o Gruppo (key), corrisponde un permesso (value)
	 * @throws DocerException
	 */
	public Map<String, EnumACLRights> getACLTitolario(ITitolarioId titolarioId) throws DocerException;
	
	/**
	 * Assegna le ACL ad un Fascicolo (con sovrascrittura)
	 * 
	 * @param fascicoloId Id del Fascicolo
	 * @param acls Lista delle ACL del Fascicolo: ad ogni Id di Utente o Gruppo (key), corrisponde un permesso (value)
	 * @throws DocerException
	 */
	public void setACLFascicolo(IFascicoloId fascicoloId, Map<String, EnumACLRights> acls) throws DocerException;
	
	/**
	 * Recupera la lista dei permessi di un Fascicolo
	 * 
	 * @param fascicoloId Id del Fascicolo
	 * @return Lista delle ACL del Fascicolo: ad ogni Id di Utente o Gruppo (key), corrisponde un permesso (value)
	 * @throws DocerException
	 */	
	public Map<String, EnumACLRights> getACLFascicolo(IFascicoloId fascicoloId) throws DocerException;
	
	/**
	 * Esegue la creazione di una Folder nel sistema documentale
	 * 
	 * @param folderInfo Profilo della Folder
	 * @return Id della Folder
	 * @throws DocerException
	 */
	public String createFolder(IFolderInfo folderInfo) throws DocerException;
	
	/**
	 * Esegue l'aggiornamento del profilo di una Folder nel sistema documentale
	 * 
	 * @param folderId Id della Folder
	 * @param folderNewInfo Il profilo della Folder contenente i metadati da modificare
	 * @throws DocerException
	 */
	public void updateFolder(String folderId, IFolderInfo folderNewInfo) throws DocerException;
	
	/**
	 * Esegue la cancellazione di una Folder dal sistema documentale
	 * @param folderId Id della Folder
	 * @throws DocerException
	 */
	public void deleteFolder(String folderId) throws DocerException;
	
	/**
	 * Assegna le ACL ad una Folder (con sovrascrittura)
	 * 
	 * @param folderId Id della Folder
	 * @param acls Lista delle ACL della Folder: ad ogni Id di Utente o Gruppo (key), corrisponde un permesso (value)
	 * @throws DocerException
	 */	
	public void setACLFolder(String folderId, Map<String, EnumACLRights> acls) throws DocerException;
	
	/**
	 * Recupera la lista dei permessi di una Folder
	 * 
	 * @param folderId Id della Folder
	 * @return Lista delle ACL della Folder: ad ogni Id di Utente o Gruppo (key), corrisponde un permesso (value)
	 * @throws DocerException
	 */		
	public Map<String, EnumACLRights> getACLFolder(String folderId) throws DocerException;
	
	/**
	 * Recupera la lista dei documenti contenuti nella Folder
	 * @param folderId Id della Folder
	 * @return Lista degli Id dei documenti contenuti nella Folder
	 * @throws DocerException
	 */
	public List<String> getFolderDocuments(String folderId) throws DocerException;
	
	/**
	 * Aggiunge documenti ad una Folder (anche come associazione logica, dipende dal sistema documentale)
	 * 
	 * @param folderId Id della Folder
	 * @param documents Lista degli Id dei documenti da aggiungere alla Folder
	 * @throws DocerException
	 */
	public void addToFolderDocuments(String folderId, List<String> documents) throws DocerException;
	
	/**
	 * Rimuove documenti da una Folder
	 * @param folderId Id della Folder
	 * @param documents Lista di Id dei documenti da rimuovere dalla Folder
	 * @throws DocerException
	 */
	public void removeFromFolderDocuments(String folderId, List<String> documents) throws DocerException;
	
	/**
	 * Esegue una ricerca di Folder
	 * 
	 * @param searchCriteria Criteri di ricerca (criteri per la stessa chiave sono in OR logico, per chiavi diverse in AND logico)	
	 * @param returnProperties Metadati che devono essere restituiti dalla ricerca
	 * @param maxResults Numero massimo dei risultati
	 * @param orderby Criteri di ordinamento dei risultati, nome del metadato (key) - ASC/DESC (value)
	 * @return DataTable che contiene un array di DataRow - ogni DataRow rappresenta il profilo di un risultato
	 * @throws DocerException
	 */
	public DataTable<String> searchFolders(Map<String, List<String>> searchCriteria, List<String> returnProperties,int maxResults, Map<String,EnumSearchOrder> orderby) throws DocerException;	

	/**
	 * Aggiunge una nuova Versione Avanzata
	 * 
	 * @param docIdLastVersion docId del documento a cui va aggiunta una Versione Avanzata
	 * @param docIdNewVersion docId del documento che rappresenta la Versione Avanzata
	 * @throws DocerException
	 */
	public void addNewAdvancedVersion(String docIdLastVersion, String docIdNewVersion) throws DocerException;
	

	/**
	 * 
	 * @param docId id del documento di riferimento
	 * @return La lista dei docId di tutte le Versioni Avanzate del documento compreso il docId stesso
	 * @throws DocerException
	 */
	public List<String> getAdvancedVersions(String docId) throws DocerException;


    /**
     * Recupera la lista dei riferimenti del fascicolo
     *
     * @param fascicoloId Id del Fascicolo
     * @return L'insieme dei riferimenti del fascicoloId
     * @throws DocerException
     */
    public List<Map<String,String>> getRiferimentiFascicolo(IFascicoloId fascicoloId) throws DocerException;

    /**
     * Aggiunge riferimenti al fascicolo fascicoloId
     *
     * @param fascicoloId Id del Fascicolo
     * @param riferimenti Lista dei riferimenti da aggiungere all'insieme dei riferimenti del fascicolo fascicoloId
     * @throws DocerException
     */
    public void addRiferimentiFascicolo(IFascicoloId fascicoloId, List<IFascicoloId> riferimenti) throws DocerException;

    /**
     * Rimuove riferimenti al fascicolo fascicoloId
     *
     * @param fascicoloId Id del Fascicolo
     * @param riferimenti Lista dei riferimenti da rimuovere dall'insieme dei riferimenti del fascicolo fascicoloId
     * @throws DocerException
     */
    public void removeRiferimentiFascicolo(IFascicoloId fascicoloId, List<IFascicoloId> riferimenti) throws DocerException;
}
