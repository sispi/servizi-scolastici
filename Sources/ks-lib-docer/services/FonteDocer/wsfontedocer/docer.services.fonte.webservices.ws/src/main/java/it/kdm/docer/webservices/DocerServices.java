package it.kdm.docer.webservices;

import it.kdm.docer.fonte.webservices.businesslogic.BusinessLogic;
import it.kdm.docer.sdk.EnumACLRights;
import it.kdm.docer.sdk.classes.HistoryItem;
import it.kdm.docer.sdk.classes.KeyValuePair;
import it.kdm.docer.sdk.classes.LockStatus;
import it.kdm.docer.sdk.classes.StreamDescriptor;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.docer.webservices.utility.TemporaryFileSource;
import it.kdm.docer.webservices.utility.WSTransformer;
import it.kdm.ws.ISearchItem;
import it.kdm.ws.SearchItem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.activation.DataHandler;

import org.apache.axiom.attachments.ByteArrayDataSource;
import org.slf4j.Logger;

public class DocerServices {

	private String providerName = "";
	private int PRIMARYSEARCH_MAX_RESULTS = 5000;
	private long DISK_BUFFER_THRESHOLD = 0;
	private String DISK_BUFFER_DIRECTORY = "";
	BusinessLogic bl = null;
	private static Logger log = org.slf4j.LoggerFactory.getLogger(DocerServices.class);
	public DocerServices() throws DocerException { // throws DocerException {

		Properties p = new Properties();
		try {
			p.loadFromXML(this.getClass().getResourceAsStream(
					"/docer_config.xml"));
		} catch (InvalidPropertiesFormatException e) {
			log
					.error("500 - read config.xml: InvalidPropertiesFormatException: "
							+ e.getMessage());
			throw new DocerException(
					"500 - read config.xml: InvalidPropertiesFormatException: "
							+ e.getMessage());
		} catch (IOException e) {
			log.error("500 - read config.xml: IOException: " + e.getMessage());
			throw new DocerException("500 - read config.xml: IOException: "
					+ e.getMessage());
		}

		// appena viene invocato il WS recupero l'informazione del 'provider'
		// dal file 'config.xml'
		providerName = p.getProperty("provider");

		try {
			DISK_BUFFER_THRESHOLD = Long.parseLong(p
					.getProperty("DISK_BUFFER_THRESHOLD"));
		} catch (Exception provEx) {
			log
					.error("500 - DISK_BUFFER_THRESHOLD config key wrong or missing: "
							+ provEx.getMessage());
			throw new DocerException(
					"500 - DISK_BUFFER_THRESHOLD config key wrong or missing: "
							+ provEx.getMessage());
		}

		try {
			DISK_BUFFER_DIRECTORY = p.getProperty("DISK_BUFFER_DIRECTORY");
		} catch (Exception provEx) {
			log
					.error("500 - DISK_BUFFER_DIRECTORY config key wrong or missing: "
							+ provEx.getMessage());
			throw new DocerException(
					"500 - DISK_BUFFER_DIRECTORY config key wrong or missing: "
							+ provEx.getMessage());
		}

		try {
			PRIMARYSEARCH_MAX_RESULTS = Integer.parseInt(p
					.getProperty("PRIMARYSEARCH_MAX_RESULTS"));
		} catch (Exception provEx) {
			log.error("500 - PRIMARYSEARCH_MAX_RESULTS config key missing: "
					+ provEx.getMessage());
			throw new DocerException(
					"500 - PRIMARYSEARCH_MAX_RESULTS config key missing: "
							+ provEx.getMessage());
		}
		
		try {
			BusinessLogic.setConfigPath(p.getProperty("CONFIG_DIR"));
		} catch (Exception provEx) {
			log.error("500 - CONFIG_DIR config key missing: "
					+ provEx.getMessage());
			throw new DocerException(
					"500 - CONFIG_DIR config key missing: "
							+ provEx.getMessage());
		}

		try {
			bl = new BusinessLogic(providerName, PRIMARYSEARCH_MAX_RESULTS);
		} catch (DocerException e) {
			log.error("500 - errore istanza Business Logic: " + e.getMessage());
			throw new DocerException("500 - errore istanza Business Logic: " +e.getMessage());
		}
	}
	
    public boolean writeConfig(String token, String xmlConfig) throws DocerException {
		try {
			bl.writeConfig(token, xmlConfig);
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
        }
    }
    
    public String readConfig(String token) throws DocerException {
    	
    	try {
    		return bl.readConfig(token);
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
    }
        
	public String createDocument(String token, KeyValuePair[] metadata,
			DataHandler file) throws DocerException {

		Map<String, String> metadataMap = WSTransformer.toMap1(metadata);

		try {
			if (file == null)
				return bl.createDocument(token, metadataMap, null);
			else
				return bl.createDocument(token, metadataMap, file.getInputStream());
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		} catch (IOException e) {
			throw new DocerException("520 - " + e.getMessage());
		}

	}

	public boolean setACLDocument(String token, String docId, KeyValuePair[] acls)
			throws DocerException {

		Map<String, EnumACLRights> aclsMap = WSTransformer.toMap2(acls);

		try {
			bl.setACLDocument(token, docId, aclsMap);
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}

	}

	public KeyValuePair[] getACLDocument(String token, String docId)
			throws DocerException {

		try {
			Map<String, EnumACLRights> aclsMap = bl
					.getACLDocument(token, docId);
			return WSTransformer.toArray2(aclsMap);
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}

	}

	public boolean updateProfileDocument(String token, String docId,
			KeyValuePair[] metadata) throws DocerException {
		try {
			Map<String, String> metadataMap = WSTransformer.toMap1(metadata);
			bl.updateProfileDocument(token, docId, metadataMap);
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}

	}

	public KeyValuePair[] getProfileDocument(String token, String docId)
			throws DocerException {
		try {
			Map<String, String> metadataMap = bl.getProfileDocument(token,
					docId);
			return WSTransformer.toArray1(metadataMap);
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}

	}

	public String[] getVersions(String token, String docId) throws DocerException {

		try {
			List<String> versions = bl.getVersions(token, docId);
			return versions.toArray(new String[0]);
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}

	}

	public String addNewVersion(String token, String docId, DataHandler file)
			throws DocerException {

		try {
			if (file == null)
				return bl.addNewVersion(token, docId, null);
			else
				return bl.addNewVersion(token, docId, file.getInputStream());
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		} catch (IOException e) {
			throw new DocerException("520 - " + e.getMessage());
		}

	}

	public boolean replaceLastVersion(String token, String docId, DataHandler file)
			throws DocerException {

		try {
			
			if (file == null)
				bl.replaceLastVersion(token, docId, null);
			else
				bl.replaceLastVersion(token, docId, file.getInputStream());			

			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		} catch (IOException e) {
			throw new DocerException("520 - " + e.getMessage());
		}
	}

	public boolean lockDocument(String token, String docId) throws DocerException {

		try {
			bl.lockDocument(token, docId);
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}

	}

	public boolean unlockDocument(String token, String docId) throws DocerException {

		try {
			bl.unlockDocument(token, docId);
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}

	}

	public LockStatus getLockStatus(String token, String docId)
			throws DocerException {

		try {
			LockStatus lockStatus = (LockStatus) bl.getLockStatus(token, docId);
			return lockStatus;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}

	}

	public StreamDescriptor downloadDocument(String token, String docId)
			throws DocerException {

		try {
			StreamDescriptor descriptor = new StreamDescriptor();
			
			File f = new File(DISK_BUFFER_DIRECTORY, UUID.randomUUID()
					.toString());

			byte[] file = bl.downloadDocument(token, docId,
					f.getAbsolutePath(), DISK_BUFFER_THRESHOLD);
			if (file == null) {
				descriptor.setByteSize(f.length());
				TemporaryFileSource tfs = new TemporaryFileSource(f);
				descriptor.setHandler(new DataHandler(tfs));
				return descriptor;
			}
			descriptor.setByteSize(file.length);
			descriptor.setHandler(new DataHandler(new ByteArrayDataSource(file)));
			return descriptor;

		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}

	}

	public StreamDescriptor downloadVersion(String token, String docId,
			String versionNumber) throws DocerException {

		try {
			StreamDescriptor descriptor = new StreamDescriptor(); 
			File f = new File(DISK_BUFFER_DIRECTORY, UUID.randomUUID()
					.toString());

			byte[] file = bl.downloadVersion(token, docId, versionNumber, f
					.getAbsolutePath(), DISK_BUFFER_THRESHOLD);
			if (file == null) {
				descriptor.setByteSize(f.length());
				TemporaryFileSource tfs = new TemporaryFileSource(f);
				descriptor.setHandler(new DataHandler(tfs));
				return descriptor;
			}
			descriptor.setByteSize(file.length);
			descriptor.setHandler(new DataHandler(new ByteArrayDataSource(file)));
			return descriptor;

		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}

	}

	public boolean addRelated(String token, String docId, String[] related)
			throws DocerException {

		try {
			if (related == null)
				return true;
			List<String> toAddRelated = new ArrayList<String>();
			for (String id : related) {
				if (toAddRelated.contains(id))
					continue;
				toAddRelated.add(id);
			}
			bl.addRelated(token, docId, toAddRelated);
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}

	}

	public boolean removeRelated(String token, String docId, String[] related)
			throws DocerException {

		try {
			if (related == null)
				return true;
			List<String> toRemoveRelated = new ArrayList<String>();
			for (String id : related) {
				if (toRemoveRelated.contains(id))
					continue;
				toRemoveRelated.add(id);
			}
			bl.removeRelated(token, docId, toRemoveRelated);
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}

	}

	public String[] getRelatedDocuments(String token, String docId)
			throws DocerException {

		try {
			return bl.getRelatedDocuments(token, docId).toArray(new String[0]);
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}

	}

	public String login(String userId, String password, String codiceEnte)
			throws DocerException{
		
		try {			
			return bl.login(codiceEnte, userId, password);
		} catch (DocerException e) {
			
			log.error(e.getMessage() +" @ "  +e.getStackTrace()[0].toString());
			
			throw new DocerException(e.getErrNumber() + " - "
					+ "Autenticazione fallita.");
		}

	}

	public boolean logout(String token) throws DocerException {

		try {
			bl.logout(token);
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}

	}

	public boolean deleteDocument(String token, String docId) throws DocerException {

		try {
			bl.deleteDocument(token, docId);
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}

	}

	public int getUserRights(String token, String docId, String userId)
			throws DocerException {

		try {
			return bl.getUserRights(token, docId, userId).getCode();
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}

	}

	public HistoryItem[] getHistory(String token, String docId)
			throws DocerException {
		try {
			return bl.getHistory(token, docId).toArray(new HistoryItem[0]);
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}

	public SearchItem[] searchDocuments(String token,
			KeyValuePair[] searchCriteria, String[] keywords, int maxRows, KeyValuePair[] orderby) throws DocerException {

		try {
			List<ISearchItem> list = new ArrayList<ISearchItem>();
			list = bl.searchDocuments(token, WSTransformer.toMap3(searchCriteria),WSTransformer.toList(keywords), maxRows,WSTransformer.toMap4(orderby));

			return list.toArray(new SearchItem[0]);
			
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}

	public boolean createEnte(String token, KeyValuePair[] enteInfo)
			throws DocerException {

		try {
			bl.createEnte(token, WSTransformer.toMap1(enteInfo));
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}

	public boolean updateEnte(String token, String codiceEnte,
			KeyValuePair[] enteInfo) throws DocerException {

		try {
			bl.updateEnte(token, codiceEnte, WSTransformer.toMap1(enteInfo));
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}

	public KeyValuePair[] getEnte(String token, String codiceEnte)
			throws DocerException {

		try {
			return WSTransformer.toArray1(bl.getEnte(token, codiceEnte));
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}

	public boolean createAOO(String token, KeyValuePair[] aooInfo)
			throws DocerException {

		try {
			bl.createAOO(token, WSTransformer.toMap1(aooInfo));
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}

	}

	public boolean updateAOO(String token, KeyValuePair[] aooId,
			KeyValuePair[] aooInfo) throws DocerException {

		try {
			bl.updateAOO(token, WSTransformer.toMap1(aooId), WSTransformer
					.toMap1(aooInfo));
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}

	public KeyValuePair[] getAOO(String token, KeyValuePair[] aooId)
			throws DocerException {
		try {
			return WSTransformer.toArray1(bl.getAOO(token, WSTransformer
					.toMap1(aooId)));
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}

//	public boolean createAreaTematica(String token, KeyValuePair[] areaTematicaInfo)
//			throws DocerException {
//
//		try {
//			bl.createAreaTematica(token, WSTransformer
//							.toMap1(areaTematicaInfo));
//			return true;
//		} catch (DocerException e) {
//			throw new DocerException(e.getErrNumber() + " - "
//					+ e.getErrDescription());
//		}
//	}
//
//	public boolean updateAreaTematica(String token, KeyValuePair[] areaTematicaId,
//			KeyValuePair[] areaTematicaInfo) throws DocerException {
//
//		try {
//			bl.updateAreaTematica(token, WSTransformer.toMap1(areaTematicaId),
//					WSTransformer.toMap1(areaTematicaInfo));
//			return true;
//		} catch (DocerException e) {
//			throw new DocerException(e.getErrNumber() + " - "
//					+ e.getErrDescription());
//		}
//	}
//
//	public KeyValuePair[] getAreaTematica(String token,
//			KeyValuePair[] areaTematicaId) throws DocerException {
//		try {
//			return WSTransformer.toArray1(bl.getAreaTematica(token,
//					WSTransformer.toMap1(areaTematicaId)));
//		} catch (DocerException e) {
//			throw new DocerException(e.getErrNumber() + " - "
//					+ e.getErrDescription());
//		}
//	}

	public boolean createTitolario(String token, KeyValuePair[] titolarioInfo)
			throws DocerException {

		try {
			bl.createTitolario(token, WSTransformer.toMap1(titolarioInfo));
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}

	}

	public boolean updateTitolario(String token, KeyValuePair[] titolarioId,
			KeyValuePair[] titolarioInfo) throws DocerException {

		try {
			bl.updateTitolario(token, WSTransformer.toMap1(titolarioId),
					WSTransformer.toMap1(titolarioInfo));
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}

	public KeyValuePair[] getTitolario(String token, KeyValuePair[] titolarioId)
			throws DocerException {

		try {
			return WSTransformer.toArray1(bl.getTitolario(token, WSTransformer
					.toMap1(titolarioId)));
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}

	public boolean createFascicolo(String token, KeyValuePair[] fascicoloInfo)
			throws DocerException {

		try {
			bl.createFascicolo(token, WSTransformer.toMap1(fascicoloInfo));
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}

	public boolean updateFascicolo(String token, KeyValuePair[] fascicoloId,
			KeyValuePair[] fascicoloInfo) throws DocerException {
		try {
			bl.updateFascicolo(token, WSTransformer.toMap1(fascicoloId),
					WSTransformer.toMap1(fascicoloInfo));
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}

	public KeyValuePair[] getFascicolo(String token, KeyValuePair[] fascicoloId)
			throws DocerException {
		try {
			return WSTransformer.toArray1(bl.getFascicolo(token, WSTransformer
					.toMap1(fascicoloId)));
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}

	public boolean createAnagraficaCustom(String token, KeyValuePair[] customInfo)
			throws DocerException {

		try {
			bl.createAnagraficaCustom(token, WSTransformer.toMap1(customInfo));
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}

	}

	public boolean updateAnagraficaCustom(String token, KeyValuePair[] customId,
			KeyValuePair[] customInfo) throws DocerException {

		try {
			bl.updateAnagraficaCustom(token, WSTransformer.toMap1(customId),
					WSTransformer.toMap1(customInfo));
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}

	public KeyValuePair[] getAnagraficaCustom(String token,
			KeyValuePair[] customId) throws DocerException {

		try {
			return WSTransformer.toArray1(bl.getAnagraficaCustom(token,
					WSTransformer.toMap1(customId)));
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}

	public SearchItem[] searchAnagrafiche(String token, String type,
			KeyValuePair[] searchCriteria) throws DocerException {

	    long start = new Date().getTime();
	    
		try {
			return bl.searchAnagrafiche(token, type,
					WSTransformer.toMap3(searchCriteria)).toArray(
					new SearchItem[0]);
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
		finally{
//		    long end = new Date().getTime();
//		    log.info("searchAnagrafiche: " +(end-start) +" ms");
		}
	}

	public boolean createUser(String token, KeyValuePair[] userInfo)
			throws DocerException {

		try {
			bl.createUser(token, WSTransformer.toMap1(userInfo));
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}

	public boolean updateUser(String token, String userId, KeyValuePair[] userInfo)
			throws DocerException {

		try {
			bl.updateUser(token, userId, WSTransformer.toMap1(userInfo));
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}

	public KeyValuePair[] getUser(String token, String userId) throws DocerException {

		try {
			return WSTransformer.toArray1(bl.getUser(token, userId));
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}

	public SearchItem[] searchUsers(String token, KeyValuePair[] searchCriteria)
			throws DocerException {

		try {
			return bl.searchUsers(token, WSTransformer.toMap3(searchCriteria))
					.toArray(new SearchItem[0]);
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}

	public boolean createGroup(String token, KeyValuePair[] groupInfo)
			throws DocerException {

		try {
			bl.createGroup(token, WSTransformer.toMap1(groupInfo));
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}

	public boolean updateGroup(String token, String groupId,
			KeyValuePair[] groupInfo) throws DocerException {

		try {
			bl.updateGroup(token, groupId, WSTransformer.toMap1(groupInfo));
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}

	public KeyValuePair[] getGroup(String token, String groupId)
			throws DocerException {

		try {
			return WSTransformer.toArray1(bl.getGroup(token, groupId));
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}

	public SearchItem[] searchGroups(String token, KeyValuePair[] searchCriteria)
			throws DocerException {
		try {
			return bl.searchGroups(token, WSTransformer.toMap3(searchCriteria))
					.toArray(new SearchItem[0]);
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}

	public boolean updateUsersOfGroup(String token, String groupId,
			String[] usersToAdd, String[] usersToRemove) throws DocerException {

		try {
			bl.updateUsersOfGroup(token, groupId, WSTransformer
					.toList(usersToAdd), WSTransformer.toList(usersToRemove));
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}

	public boolean setUsersOfGroup(String token, String groupId, String[] users)
			throws DocerException {
		try {
			bl.setUsersOfGroup(token, groupId, WSTransformer.toList(users));
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}

	public String[] getUsersOfGroup(String token, String groupId)
			throws DocerException {

		try {
			return bl.getUsersOfGroup(token, groupId).toArray(new String[0]);
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}

	public boolean updateGroupsOfUser(String token, String userId,
			String[] groupsToAdd, String[] groupsToRemove) throws DocerException {

		try {
			bl.updateGroupsOfUser(token, userId, WSTransformer
					.toList(groupsToAdd), WSTransformer.toList(groupsToRemove));
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}

	public boolean setGroupsOfUser(String token, String userId, String[] groups)
			throws DocerException {
		try {
			bl.setGroupsOfUser(token, userId, WSTransformer.toList(groups));
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}

	public String[] getGroupsOfUser(String token, String userId)
			throws DocerException{

		try {
			return bl.getGroupsOfUser(token, userId).toArray(new String[0]);
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}
	
	
	public KeyValuePair[] getDocumentTypes(String token) throws DocerException {
		try {
			return bl.getDocumentTypes(token).toArray(new KeyValuePair[0]);
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}

	public boolean verifyTicket(String token){
			return bl.verifyTicket(token);
	}

	
	public boolean setACLTitolario(String token, KeyValuePair[] titolarioId,
			KeyValuePair[] acls) throws DocerException {

		Map<String, EnumACLRights> aclsMap = WSTransformer.toMap2(acls);

		try {
			bl.setACLTitolario(token, WSTransformer.toMap1(titolarioId), aclsMap);
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}

	}
	
	public KeyValuePair[] getACLTitolario(String token, KeyValuePair[] titolarioId)
			throws DocerException {

		try {
			Map<String, EnumACLRights> aclsMap = bl.getACLTitolario(token, WSTransformer.toMap1(titolarioId));
			return WSTransformer.toArray2(aclsMap);
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}

	}

	
	public boolean setACLFascicolo(String token, KeyValuePair[] fascicoloId,
			KeyValuePair[] acls) throws DocerException {

		Map<String, EnumACLRights> aclsMap = WSTransformer.toMap2(acls);

		try {
			bl.setACLFascicolo(token, WSTransformer.toMap1(fascicoloId), aclsMap);
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}

	}
	
	public KeyValuePair[] getACLFascicolo(String token, KeyValuePair[] fascicoloId)
			throws DocerException {

		try {
			Map<String, EnumACLRights> aclsMap = bl.getACLFascicolo(token, WSTransformer.toMap1(fascicoloId));
			return WSTransformer.toArray2(aclsMap);
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}

	}
	
	public String createFolder(String token, KeyValuePair[] folderInfo)
			throws DocerException {

	    try {
            return bl.createFolder(token, WSTransformer.toMap1(folderInfo));
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - "
                    + e.getErrDescription());
        }
		
	}
	
	public boolean updateFolder(String token, String folderId,
			KeyValuePair[] folderInfo) throws DocerException {
		
	    try {
            bl.updateFolder(token, folderId, WSTransformer.toMap1(folderInfo));
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - "
                    + e.getErrDescription());
        }
	}
	
	public boolean deleteFolder(String token, String folderId) throws DocerException {
		
	    try {
            bl.deleteFolder(token, folderId);
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - "
                    + e.getErrDescription());
        }
	}
	
	public boolean setACLFolder(String token, String folderId,
			KeyValuePair[] acls) throws DocerException {

	    Map<String, EnumACLRights> aclsMap = WSTransformer.toMap2(acls);

        try {
            bl.setACLFolder(token, folderId, aclsMap);
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - "
                    + e.getErrDescription());
        }

	}
	
	public KeyValuePair[] getACLFolder(String token, String folderId)
			throws DocerException {

	    try {
            Map<String, EnumACLRights> aclsMap = bl.getACLFolder(token, folderId);
            return WSTransformer.toArray2(aclsMap);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - "
                    + e.getErrDescription());
        }
	}
	
	public String[] getFolderDocuments(String token, String folderId)
			throws DocerException {
	
	    try {
            return bl.getFolderDocuments(token, folderId).toArray(new String[0]);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - "
                    + e.getErrDescription());
        }
	}
	
	public boolean addToFolderDocuments(String token, String folderId, String[] document)
			throws DocerException {

	    try {
            if (document == null)
                return true;
            List<String> toAdd = new ArrayList<String>();
            for (String id : document) {
                if (toAdd.contains(id))
                    continue;
                toAdd.add(id);
            }
            bl.addToFolderDocuments(token, folderId, toAdd);
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - "
                    + e.getErrDescription());
        }
	}
	
	public boolean removeFromFolderDocuments(String token, String folderId, String[] document)
			throws DocerException {

	    try {
            if (document == null)
                return true;
            List<String> toRemove = new ArrayList<String>();
            for (String id : document) {
                if (toRemove.contains(id))
                    continue;
                toRemove.add(id);
            }
            bl.removeFromFolderDocuments(token, folderId, toRemove);
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - "
                    + e.getErrDescription());
        }
	}
	
	public SearchItem[] searchFolders(String token,
			KeyValuePair[] searchCriteria, int maxRows, KeyValuePair[] orderby) throws DocerException {

	    try {
            List<ISearchItem> list = new ArrayList<ISearchItem>();
            list = bl.searchFolders(token, WSTransformer.toMap3(searchCriteria), maxRows,WSTransformer.toMap4(orderby));

            return list.toArray(new SearchItem[0]);
            
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - "
                    + e.getErrDescription());
        }
	}
	
	public String[] getRiferimentiDocuments(String token, String docId)
			throws DocerException {

		try {
			return bl.getRiferimentiDocuments(token, docId).toArray(new String[0]);
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}
	
	public boolean addRiferimentiDocuments(String token, String docId, String[] riferimenti)
			throws DocerException {

		try {
			if (riferimenti == null)
				return true;
			List<String> toAddRiferimenti = new ArrayList<String>();
			for (String id : riferimenti) {
				if (toAddRiferimenti.contains(id))
					continue;
				toAddRiferimenti.add(id);
			}
			bl.addRiferimentiDocuments(token, docId, toAddRiferimenti);
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}

	public boolean removeRiferimentiDocuments(String token, String docId, String[] riferimenti)
			throws DocerException {
		
		try {
			if (riferimenti == null)
				return true;
			List<String> toRemoveRiferimenti = new ArrayList<String>();
			for (String id : riferimenti) {
				if (toRemoveRiferimenti.contains(id))
					continue;
				toRemoveRiferimenti.add(id);
			}
			bl.removeRelated(token, docId, toRemoveRiferimenti);
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}

	public boolean protocollaDocumento(String token, String docId,
			KeyValuePair[] metadata) throws DocerException {
		

			try {
				Map<String, String> metadataMap = WSTransformer.toMap1(metadata);
				bl.protocollaDocumento(token, docId, metadataMap);
				return true;
			} catch (DocerException e) {
				throw new DocerException(e.getErrNumber() + " - "
						+ e.getErrDescription());
			}
	}
	
	public boolean registraDocumento(String token, String docId,
			KeyValuePair[] metadata) throws DocerException {
		
		try {
			Map<String, String> metadataMap = WSTransformer.toMap1(metadata);
			bl.registraDocumento(token, docId, metadataMap);
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}
	
	public boolean fascicolaDocumento(String token, String docId,
			KeyValuePair[] metadata) throws DocerException {
		
		try {
			Map<String, String> metadataMap = WSTransformer.toMap1(metadata);
			bl.fascicolaDocumento(token, docId, metadataMap);
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}
	
	public boolean classificaDocumento(String token, String docId,
			KeyValuePair[] metadata) throws DocerException {
		
		try {
			Map<String, String> metadataMap = WSTransformer.toMap1(metadata);
			bl.classificaDocumento(token, docId, metadataMap);
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}
	
	public boolean pubblicaDocumento(String token, String docId,
			KeyValuePair[] metadata) throws DocerException {
		
		try {
			Map<String, String> metadataMap = WSTransformer.toMap1(metadata);
			bl.pubblicaDocumento(token, docId, metadataMap);
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}
	
	public boolean archiviaDocumento(String token, String docId,
			KeyValuePair[] metadata) throws DocerException {
		
		try {
			Map<String, String> metadataMap = WSTransformer.toMap1(metadata);
			bl.archiviaDocumento(token, docId, metadataMap);
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}
	
	public int getUserRightsAnagrafiche(String token, String type, KeyValuePair[] id, String userId)
			throws DocerException {

		try {
			return bl.getUserRightsAnagrafiche(token, type, WSTransformer.toMap1(id), userId).getCode();
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}

	}
	
	public int getUserRightsFolder(String token, String folderId, String userId)
			throws DocerException {

		try {
			return bl.getUserRightsFolder(token, folderId, userId).getCode();
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}

	}
		
	
	public KeyValuePair[] getDocumentTypesByAOO(String token, String codiceEnte, String codiceAOO) throws DocerException {
		try {
			return bl.getDocumentTypes(token,codiceEnte,codiceAOO).toArray(new KeyValuePair[0]);
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	}
	

	public boolean addNewAdvancedVersion(String token, String docIdLastVersion, String docIdNewVersion) throws DocerException{
		
		try {
			
			bl.addNewAdvancedVersion(token, docIdLastVersion, docIdNewVersion);
			return true;
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
		
	}

	public String[] getAdvancedVersions(String token, String docId) throws DocerException {
	
		try {
			return bl.getAdvancedVersions(token, docId).toArray(new String[0]);
		} catch (DocerException e) {
			throw new DocerException(e.getErrNumber() + " - "
					+ e.getErrDescription());
		}
	
	}
			
}
