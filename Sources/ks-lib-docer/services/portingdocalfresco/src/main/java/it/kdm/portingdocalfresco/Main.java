package it.kdm.portingdocalfresco;

import it.kdm.docer.businesslogic.BusinessLogic;
import it.kdm.docer.sdk.EnumACLRights;
import it.kdm.docer.sdk.exceptions.DocerException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import oracle.jdbc.pool.OracleDataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;

public class Main {

	protected Properties props = null;
	public BasicDataSource ds = null;
	public String logFile = "";
	public String tableName = "";
	
	public String token = "";	
	public String authenticationWebServiceUrl = null;
	public String webServiceUrl = "";
	
	public String connectionURL = "";
	public String oracleDriver = "";
	public String uid = "";
	public String pwd = "";
	
	public Connection connection = null;
	
	public Map<String, Map<String, String>> docPropertiesPrincipale = null;
	public Map<String, Map<String, String>> docPropertiesAllegati = null;
	private BusinessLogic businessLogic = null;
	
	public String doLogin () throws Exception
	{
		try {
						
			return businessLogic.login(props.getProperty("LoginCodiceEnte"), props.getProperty("UserIDAlfresco"), props.getProperty("PasswordAlfresco"));
			
		} catch (Exception e) {
			throw e;
		}					
	}	
	
	
	 private void initBusinessLogic() {

        if (businessLogic == null) {
            
        	try {
                Properties p = new Properties();
                
                p.loadFromXML(this.getClass().getResourceAsStream("/docer_config.xml"));

                String providerName = p.getProperty("provider");

                BusinessLogic.setConfigPath(p.getProperty("CONFIG_DIR"));

                businessLogic = new BusinessLogic(providerName, 1000);
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

	}
	
	public static void main( String[] args ) throws DocerException, Exception {	
			   
		Main m = new Main();

		m.initBusinessLogic();
		
	    String filePath = "";
	    
		m.props = m.readParams("config.txt");
		m.tableName = m.props.getProperty("TableName");
		
		// parametri connessione db oracle
		m.logFile = m.props.getProperty("LogFilePath");
    	m.connectionURL = m.props.getProperty("ConnectionUrl");
		m.oracleDriver = m.props.getProperty("OracleDriver");
		m.uid = m.props.getProperty("UserIdEdocs");
		m.pwd = m.props.getProperty("PasswordEdocs");

		// oggetti da utilizzare per le query su db oracle
		m.ds = new BasicDataSource();
		m.ds.setDriverClassName(m.oracleDriver);
		m.ds.setUsername(m.uid);
		m.ds.setPassword(m.pwd);
		m.ds.setUrl(m.connectionURL);
		
		// cancello eventuali precedenti risultati del batch
		File f = new File(m.logFile);
	    f.delete();
				
		try {

			// login in alfresco
			m.token = m.doLogin();
			m.writeLog(m.logFile, "token iniziale");
			
		} catch(Exception e) {
			m.writeLog(m.logFile, "Routine: 'doLogin'. Errore: " + e.getMessage());
			return;
		}		
		
		m.connection = m.retrieveConnection();
		
		m.writeLog(m.logFile, "Inizio procedura");
		m.writeLog(m.logFile, "Recupero dati da DM");
		

		if (m.props.getProperty("TIPO_COMPONENTE").toString().equals("PRINCIPALE,ALLEGATO")) {
			m.docPropertiesPrincipale = m.retrieveAllDocuments("PRINCIPALE", m.connection);
			m.docPropertiesAllegati = m.retrieveAllDocuments("ALLEGATO", m.connection);
		}
		else {
			m.docPropertiesPrincipale = new HashMap<String, Map<String, String>>();
			
			// estraggo i documenti generici (che non sono 'PRINCIPALE' o 'ALLEGATO'). Li tratto come fossero principali
			m.docPropertiesPrincipale = m.retrieveAllDocuments("GENERICO", m.connection);
		}
		
		
		Map<String, String> profileInfoTotale = new HashMap<String, String>();
		Map<String, String> fascicolazioneProfileInfo = new HashMap<String, String>();
		Map<String, String> protocollazioneProfileInfo = new HashMap<String, String>();
		Map<String, String> classificazioneProfileInfo = new HashMap<String, String>();
		Map<String, String> genericoProfileInfo = new HashMap<String, String>();
		
		List<String> listIgnoreRelated = new ArrayList<String>();
		
		Map<String, String> map = null;
		
		long h = 0;
		
        FileInputStream fis = null;

        File file = null;   
        
		for (String doc : m.docPropertiesPrincipale.keySet()) {
						
			h++;
			
			m.writeLog(m.logFile, String.valueOf("loop numero: " + h));
			
			
			try {

				String docnumber = doc;

				map = new HashMap<String, String>();
				map = m.docPropertiesPrincipale.get(doc);
				
				for(String key : map.keySet()) 
					profileInfoTotale.put(key, map.get(key));
				
				// passo gli unici campi che devono popolare gli hashmap
				String campiFascicolazione = m.props.getProperty("CampiFascicolazione").toString();
				String campiProtocollazione = m.props.getProperty("CampiProtocollazione").toString();
				String campiClassificazione = m.props.getProperty("CampiClassificazione").toString();
				String campiProfiloGenerico = m.props.getProperty("CampiProfiloGenerico").toString();
				
				fascicolazioneProfileInfo  	= m.cleanProfileInfo(profileInfoTotale, campiFascicolazione,  "FASCICOLAZIONE");				
				protocollazioneProfileInfo 	= m.cleanProfileInfo(profileInfoTotale, campiProtocollazione, "PROTOCOLLAZIONE");	
				classificazioneProfileInfo 	= m.cleanProfileInfo(profileInfoTotale, campiClassificazione, "CLASSIFICAZIONE");				
				genericoProfileInfo   		= m.cleanProfileInfo(profileInfoTotale, campiProfiloGenerico, "GENERICO");				
				
				// path del file fisico associato al docnumber attuale
				if (m.props.getProperty("SettaFilePath").toString().toUpperCase().equals("Y"))
					filePath = m.props.get("FileOnFileSystem").toString();
				else
					filePath = map.get("FILE_PATH").toString();
							
				file = new File(filePath);
			    fis = new FileInputStream(file);
			    
				m.writeLog(m.logFile, "Inizio import documento: " + docnumber);
				
				String createdFlag = m.docPropertiesPrincipale.get(doc).get("CREATED");
				
				
				boolean fascicolazionePossibile = true;
				
//				if (!docnumber.equals("22500")) 
//					continue;
				 
				if ( createdFlag.toUpperCase().equals("Y") ) {
					
							m.writeLog(m.logFile, "Documento con docnumber " + docnumber + " esistente. Controllo dei flag per update...");
							
							// Sono stati cambiati i dati di profilo e quindi faccio update in alfresco
							if (map.get("NEEDS_PROFILE_UPDATE").toString().toUpperCase().equals("Y")) {
								
								try {
									long start = new Date().getTime();
									m.businessLogic.updateProfileDocument(m.token, docnumber,genericoProfileInfo);
									long end = new Date().getTime();
									m.writeLog(m.logFile, "Tempo impiegato per updateProfileDocument: " + (end-start));
									
									m.writeLog(m.logFile, "Profilo documento: " + docnumber + " modificato correttamente");
									
									m.update("UPDATE " + m.tableName + " SET NEEDS_PROFILE_UPDATE = 'N' WHERE DOCNUM = " + docnumber);

								}
								catch(Exception e) {
									
									if (m.sessioneScaduta(e.getMessage())) {
										m.token = m.doLogin();
										m.writeLog(m.logFile, "token sovrascritto");
									}
									
									// riportare sul log l'errore e 
									m.writeLog(m.logFile, "Errore in fase di modifica profilo documento. Docnumber: " + docnumber + "; Errore: " + e.getMessage() );						
								}		
								
							}
							
							// se il documento non e' un allegato allora posso fare il content_update
							if ( map.get("TIPO_COMPONENTE") == null || map.get("TIPO_COMPONENTE").toString().toUpperCase().equals("PRINCIPALE") ){
									
									// E' stato modificato il file di upload
									if (map.get("NEEDS_CONTENT_UPDATE").toString().toUpperCase().equals("Y")) {
		
											try {
												long start = new Date().getTime();
												m.businessLogic.replaceLastVersion(m.token, docnumber, fis);
												long end = new Date().getTime();
												m.writeLog(m.logFile, "Tempo impiegato per upload content-file: " + (end-start));

												m.writeLog(m.logFile, "Upload content-file documento: " + docnumber + " importato correttamente");
												
												m.update("UPDATE " + m.tableName + " SET NEEDS_CONTENT_UPDATE = 'N' WHERE DOCNUM = " + docnumber);

											} catch(Exception e) {
												
												if (m.sessioneScaduta(e.getMessage())) {
													m.token = m.doLogin();
													m.writeLog(m.logFile, "token sovrascritto");
												}
												
												// riportare sul log l'errore e 
												m.writeLog(m.logFile, "Errore in fase di upload file. Docnumber: " + docnumber + "; Errore: " + e.getMessage());						
											}
									}
										
							}
							
							// aggiungo le acl al documento su Alfresco
							try {
								
									// bisogna updatare le acl del documento
									if (map.get("NEEDS_ACL_UPDATE") != null &&  map.get("NEEDS_ACL_UPDATE").toString().toUpperCase().equals("Y")) {
										
										if (map.get("ACL") != null) {
											Map<String,EnumACLRights> aclDocument = m.retrieveSetACLDocument(map.get("ACL"));
											
											if (aclDocument.size()>0) {
												long start = new Date().getTime();
												m.businessLogic.setACLDocument(m.token, docnumber, aclDocument);												
												long end = new Date().getTime();
												m.writeLog(m.logFile, "Tempo impiegato per setACLDocument: " + (end-start));
												
												m.writeLog(m.logFile, "Le ACL del documento: " + docnumber + " sono state importate correttamente");
											}
										}

										m.update("UPDATE " + m.tableName + " SET NEEDS_ACL_UPDATE = 'N' WHERE DOCNUM = " + docnumber);
									}
									
								} catch(Exception e) {
									
									if (m.sessioneScaduta(e.getMessage())) {
										m.token = m.doLogin();
										m.writeLog(m.logFile, "token sovrascritto");
									}
									
									// riportare sul log l'errore e 
									m.writeLog(m.logFile, "Errore in fase di setACLDocument. Docnumber: " + docnumber + "; Errore: " + e.getMessage());						
								}	
							
							
								// update related
								if ( map.get("TIPO_COMPONENTE") == null || map.get("TIPO_COMPONENTE").toString().toUpperCase().equals("PRINCIPALE") ){
									
										if ( map.get("NEEDS_RELATED_UPDATE") != null &&  map.get("NEEDS_RELATED_UPDATE").toString().toUpperCase().equals("Y")) {
											
												// recupero allegati del documento da alfresco e rimuovo relazione. 
												// Creo poi la nuova relazione tra padre e allegati indicati nel campo 'RELATED'  											
												String docNumberAllegato = "";
											
												try {

														// Se nel campo related non ci sono indicati gli allegati da aggiungere allora salto l'addRelated
														if (map.get("RELATED") != null && !map.get("RELATED").toString().equals("")) {
															
																// recupero la lista degli alegati da relazionare col padre
																List<String> listaAllegatiDaAggiungere = Arrays.asList(map.get("RELATED").toString().split("[,;]"));
																List<String> allegatiDaAssociare = null;
	
																// controllo esistenza su Alfresco di ogni singolo related da associare
																for(String allegato : listaAllegatiDaAggiungere) {
																	
																		docNumberAllegato = allegato;
	
																		// solo per i generici
																		if (map.get("TIPO_COMPONENTE") == null || map.get("TIPO_COMPONENTE").toString().toUpperCase().equals("")) {
																			if (listIgnoreRelated.contains(allegato))
																				continue;
																		}
																		
																		allegatiDaAssociare = new ArrayList<String>();
																				
																		
																		// in questo caso il docnumber del principale è contenuto anche tra i related
																		if (allegato.equals(docnumber)) 
																			continue;

																		
																		if (m.docPropertiesAllegati.get(allegato) != null) {
																			
																			// controllo esistenza allegato in alfresco.
																			if(m.docPropertiesAllegati.get(allegato).get("CREATED").toString().toUpperCase().equals("N")) { 
																				
																				m.writeLog(m.logFile, "Inizio creazione allegato " + allegato + " al documento master " + map.get("DOCNUM").toString());
																				
																				// ricavo lista dei campi di profilo generico valorizzati. Se non valorizzati li scarto											
																				Map<String, String> mapAllegato = m.docPropertiesAllegati.get(allegato);
																				Map<String, String> mapProfiloGenericoAllegato = new HashMap<String, String>();

																				// lista dei campi di profilo generico dell'allegato
																				List<String> listCampiProfiloGenericoAllegato = Arrays.asList(m.props.getProperty("CampiProfiloGenerico").split(",") );

																				// mappa di profilo generico del documento allegato
																				for (String s : listCampiProfiloGenericoAllegato) {
																					if (mapAllegato.get(s) != null)
																						mapProfiloGenericoAllegato.put(s, mapAllegato.get(s));
																				}																
																				
																				/* controllo se gli allegati ereditano il type_id dal master */			
																				if ( m.props.getProperty("AllegatoEreditaTypeId").equals("Y") )																		
																					mapProfiloGenericoAllegato.put("TYPE_ID", map.get("TYPE_ID"));
																				else
																					mapProfiloGenericoAllegato.put("TYPE_ID", "DOCUMENTO");

																				/* fine */
																				
																				
																				
																				// creazione documento allegato
																				if (m.props.getProperty("SettaFilePath").toString().toUpperCase().equals("Y"))
																					filePath = m.props.get("FileOnFileSystem").toString();
																				else
																					filePath = map.get("FILE_PATH").toString();
																											
																				file = new File(filePath);
																			    fis = new FileInputStream(file);
																			    
																			    try{
																				    long start = new Date().getTime();
																					m.businessLogic.createDocument(m.token, mapProfiloGenericoAllegato, fis);
																					long end = new Date().getTime();
																					m.writeLog(m.logFile, "Tempo impiegato per creazione allegato: " + (end-start));
																				
																				
																					fis.close();
																				}
																				catch(Exception closeex){
																					m.writeLog(m.logFile, "errore chiusura file allegato: "+closeex.getMessage());
																					
																					if(closeex.getMessage().matches("MIGRATION_MODE: documento con docId .+ esistente.")){
																						m.update("UPDATE " + m.tableName + " SET CREATED = 'Y' WHERE DOCNUM = " + allegato);									
																						m.writeLog(m.logFile, "Documento: " + allegato + " esistente flag created impostato a 'Y' correttamente");
																					}
																					else{
																						continue;
																					}																					
																				}
																				
																				
																				// bisogna updatare le acl del documento allegato
																				if (mapAllegato.get("NEEDS_ACL_UPDATE") != null &&  mapAllegato.get("NEEDS_ACL_UPDATE").toString().toUpperCase().equals("Y")) {
																					
																					if (mapAllegato.get("ACL") != null) {
																						Map<String,EnumACLRights> aclDocumentAllegato = m.retrieveSetACLDocument(mapAllegato.get("ACL"));
																						
																						if (aclDocumentAllegato.size()>0) {
																							long startt = new Date().getTime();
																							m.businessLogic.setACLDocument(m.token, allegato, aclDocumentAllegato);	
																							long endd = new Date().getTime();
																							m.writeLog(m.logFile, "Tempo impiegato per setACLDocument allegato: " + (endd-startt));

																							m.writeLog(m.logFile, "Le ACL del documento: " + docnumber + " sono state importate correttamente");
																						}
																					}

																					m.update("UPDATE " + m.tableName + " SET NEEDS_ACL_UPDATE = 'N' WHERE DOCNUM = " + allegato);
																				}																				
																				
																				m.update("UPDATE " + m.tableName + " SET CREATED = 'Y' WHERE DOCNUM = " + allegato);
																				
																			}
			
																			// lista degli allegati da associare
																			allegatiDaAssociare.add(allegato);
																			
																			// solo per i generici
																			if (map.get("TIPO_COMPONENTE") == null || map.get("TIPO_COMPONENTE").toString().toUpperCase().equals(""))
																				listIgnoreRelated.add(allegato);																			
																		}
																		else
																		{
																			m.writeLog(m.logFile, "Non e' possibile reperire le informazioni dell'allegato " + allegato + " in quanto non e' presente nella tabella di import. L'unita' documentaria non puo' essere fascicolata, protocollata, classificata");
																			fascicolazionePossibile = false;
																		}
																}
	
															    // lista dei related del documento in alfresco 
																long start = new Date().getTime();
                                                                List<String> toRemoveRelated = m.businessLogic.getRelatedDocuments(m.token, docnumber);
                                                                long end = new Date().getTime();
																m.writeLog(m.logFile, "Tempo impiegato per getRelatedDocuments: " + (end-start));                                                                
                                                                
                                                                // rimuovo le vecchie relazioni tra master e allegati in alfresco                      
                                                                m.writeLog(m.logFile, "Rimozione vecchie associazioni del documento master " + docnumber);
                                                                
                                                                long start1 = new Date().getTime();
                                                                m.businessLogic.removeRelated(m.token, docnumber, toRemoveRelated);
                                                            	long end1 = new Date().getTime();
																m.writeLog(m.logFile, "Tempo impiegato per removeRelated: " + (end1-start1));
                                                                
                                                                m.writeLog(m.logFile, "Inizio associazione allegati al documento master " + docnumber);                                                                

                                                                long startt = new Date().getTime();
                                                                m.businessLogic.addRelated(m.token, docnumber, allegatiDaAssociare);
                                                                long endd = new Date().getTime();
																m.writeLog(m.logFile, "Tempo impiegato per addRelated: " + (endd-startt));
                                                                
																m.writeLog(m.logFile, "L'associazione degli allegati al documento master " + map.get("DOCNUM").toString() + " e' stata eseguita correttamente ");
																
																m.update("UPDATE " + m.tableName + " SET NEEDS_RELATED_UPDATE = 'N' WHERE DOCNUM = " + docnumber );
														
														}	// close if
													
													
												} catch(Exception e) {
													
													if (m.sessioneScaduta(e.getMessage())) {
														m.token = m.doLogin();
														m.writeLog(m.logFile, "token sovrascritto");
													}
													
													// riportare sul log l'errore e 
													m.writeLog(m.logFile, "Errore in fase di setRelated. Docnumber: " + docNumberAllegato + "; Errore: " + e.getMessage());						
												}
											
										}	
									
								}
							
								// classifica
								if (fascicolazionePossibile) {
									
										if ( map.get("TIPO_COMPONENTE") != null && map.get("TIPO_COMPONENTE").toString().toUpperCase().equals("PRINCIPALE") ){
											
												// se devo fascicolare e' inutile che classifico
												if (map.get("NEEDS_FASCICOLO_UPDATE") == null || map.get("NEEDS_FASCICOLO_UPDATE").toString().toUpperCase().equals("") 
														|| map.get("NEEDS_FASCICOLO_UPDATE").toString().toUpperCase().equals("N") ) {
													
														// controllo se va classificato
														if (map.get("NEEDS_CLASSIFICA_UPDATE") != null && map.get("NEEDS_CLASSIFICA_UPDATE").toString().toUpperCase().equals("Y") ) {
															
																// classifico documento
																try {
																	
																	// se il campo 'classifica' non e' valorizzato salto la routine
																	if ( classificazioneProfileInfo.get("CLASSIFICA") != null && !classificazioneProfileInfo.get("CLASSIFICA").toString().equals("") )
																	{
																		m.writeLog(m.logFile, "Inizio classificazione documento con docnumber " + docnumber );
																		
																		long start = new Date().getTime();
																		m.businessLogic.classificaDocumento(m.token, docnumber, classificazioneProfileInfo);
																		long end = new Date().getTime();
																		m.writeLog(m.logFile, "Tempo impiegato per classificaDocumento: " + (end-start));
																		
																		m.update("UPDATE " + m.tableName + " SET NEEDS_CLASSIFICA_UPDATE = 'N' WHERE DOCNUM = " + docnumber);
										
																		m.writeLog(m.logFile, "Il documento con docnumber: " + docnumber + " e' stato classificato correttamente");
																	}
																	
																} catch(Exception e) {
																	
																	if (m.sessioneScaduta(e.getMessage())) {
																		m.token = m.doLogin();
																		m.writeLog(m.logFile, "token sovrascritto");
																	}
																	
																	// riportare sul log l'errore e 
																	m.writeLog(m.logFile, "Errore in fase di classificazione documento. Docnumber: " + docnumber + "; Errore: " + e.getMessage());						
																}
														}	
													
												}
											
										}
								}
							
							
								if (fascicolazionePossibile) {
									
										if ( map.get("TIPO_COMPONENTE") != null && map.get("TIPO_COMPONENTE").toString().toUpperCase().equals("PRINCIPALE") ){
											
												// controllo se il documento va fascicolo
												if (map.get("NEEDS_FASCICOLO_UPDATE") != null &&  map.get("NEEDS_FASCICOLO_UPDATE").toString().toUpperCase().equals("Y") ) {
													
														try {
															
															// se il campo 'classifica' non e' valorizzato salto la routine
															if ( fascicolazioneProfileInfo.get("CLASSIFICA") != null && !fascicolazioneProfileInfo.get("CLASSIFICA").toString().equals("") )
															{
																m.writeLog(m.logFile, "Inizio fascicolazione documento con docnumber " + docnumber );
																
																long start = new Date().getTime();
																m.businessLogic.fascicolaDocumento(m.token, docnumber, fascicolazioneProfileInfo);
																long end = new Date().getTime();
																m.writeLog(m.logFile, "Tempo impiegato per fascicolaDocumento: " + (end-start));
																
																m.writeLog(m.logFile, "Il documento con docnumber " + docnumber + " e' stato fascicolato correttamente");
								
																m.update("UPDATE " + m.tableName + " SET NEEDS_FASCICOLO_UPDATE = 'N' WHERE DOCNUM = " + docnumber);
															}
							
														} catch(Exception e) {
															
															if (m.sessioneScaduta(e.getMessage())) {
																m.token = m.doLogin();
																m.writeLog(m.logFile, "token sovrascritto");
															}
															
															// riportare sul log l'errore
															m.writeLog(m.logFile, "Errore in fase di fascicolazione documento. Docnumber: " + docnumber + "; Errore: " + e.getMessage());						
														}
												}
										
												// protocollo
												if (map.get("NEEDS_PROTOCOLLO_UPDATE") != null &&  map.get("NEEDS_PROTOCOLLO_UPDATE").toString().toUpperCase().equals("Y") ) {
													
														try {
															
															m.writeLog(m.logFile, "Inizio protocollazione documento con docnumber " + docnumber );
															
															long start = new Date().getTime();
															m.businessLogic.protocollaDocumento(m.token, docnumber, protocollazioneProfileInfo);
															long end = new Date().getTime();
															m.writeLog(m.logFile, "Tempo impiegato per protocollaDocumento: " + (end-start));
															
															m.writeLog(m.logFile, "Il documento con docnumber " + docnumber + " e' stato protocollato correttamente");
							
															m.update("UPDATE " + m.tableName + " SET NEEDS_PROTOCOLLO_UPDATE = 'N' WHERE DOCNUM = " + docnumber);
							
														} catch(Exception e) {
															
															if (m.sessioneScaduta(e.getMessage())) {
																m.token = m.doLogin();
																m.writeLog(m.logFile, "token sovrascritto");
															}
															
															// riportare sul log l'errore e 
															m.writeLog(m.logFile, "Errore in fase di protocollazione documento. Docnumber: " + docnumber + "; Errore: " + e.getMessage());
														}
												}
										
									}	// close tipo componente
							
							}	// close if dofascicola
					
					
				}	// CLOSE IF CREATED = Y
				else
				{
							m.writeLog(m.logFile, "Documento: " + docnumber + " non presente in DocER. Inizio inserimento...");
					
							// CREATED = N. Creo profilo generico + file. Blocco il record fissando il campo id_process.
							try {
								
								// creo il documento
								long start = new Date().getTime();								
								m.businessLogic.createDocument(m.token, genericoProfileInfo, fis);
								long end = new Date().getTime();
								m.writeLog(m.logFile, "Tempo impiegato per creazione documento: " + (end-start));								
								
								// resetto il flag a N (fine lavorazione)
								m.update("UPDATE " + m.tableName + " SET CREATED = 'Y' WHERE DOCNUM = " + docnumber);
								
								m.writeLog(m.logFile, "Profilo documento: " + docnumber + " inserito correttamente");
								
							} catch(Exception e) {
								
								if (m.sessioneScaduta(e.getMessage())) {
									m.token = m.doLogin();
									m.writeLog(m.logFile, "token sovrascritto");
								}
								
								// riportare sul log l'errore e 
								m.writeLog(m.logFile, "Errore in fase di inserimento profilo documento. Docnumber: " + docnumber + "; Errore: " + e.getMessage() );		
								
								if(e.getMessage().matches("MIGRATION_MODE: documento con docId .+ esistente.")){
									m.update("UPDATE " + m.tableName + " SET CREATED = 'Y' WHERE DOCNUM = " + docnumber);									
									m.writeLog(m.logFile, "Documento: " + docnumber + " esistente flag created impostato a 'Y' correttamente");
								}
								else{
									continue;
								}								
							}	
								
								
							// aggiungo le acl al documento su Alfresco
							try {
								
								if ( map.get("NEEDS_ACL_UPDATE") != null && map.get("NEEDS_ACL_UPDATE").toString().equals("Y") ) {
									
									if (map.get("ACL") != null ) {
										
										Map<String,EnumACLRights> aclDocument = m.retrieveSetACLDocument(map.get("ACL"));
										
										if (aclDocument.size()>0) {
											long start = new Date().getTime();
											m.businessLogic.setACLDocument(m.token, docnumber, aclDocument);		
											long end = new Date().getTime();
											m.writeLog(m.logFile, "Tempo impiegato per setACLDocument: " + (end-start));											

											m.writeLog(m.logFile, "Le ACL del documento: " + docnumber + " sono state importate correttamente");
										}										
										
										m.update("UPDATE " + m.tableName + " SET NEEDS_ACL_UPDATE = 'N' WHERE DOCNUM = " + docnumber);
										m.writeLog(m.logFile, "ACL del documento: " + docnumber + " inserite correttamente");	
									
									}
									
								}
									
							} catch(Exception e) {
								
								if (m.sessioneScaduta(e.getMessage())) {
									m.token = m.doLogin();
									m.writeLog(m.logFile, "token sovrascritto");
								}
								
								// riportare sul log l'errore e 
								m.writeLog(m.logFile, "Errore in fase di settaggio ACL. Docnumber: " + docnumber + "; Errore: " + e.getMessage());						
							}


							if ( map.get("TIPO_COMPONENTE") == null || map.get("TIPO_COMPONENTE").toString().toUpperCase().equals("PRINCIPALE") ) {			
									
									String docNumberAllegato = "";
									
									if ( map.get("NEEDS_RELATED_UPDATE") != null && map.get("NEEDS_RELATED_UPDATE").toString().equals("Y") ) {
										
										// related
										try {
											
												// Se nel campo related non ci sono indicati gli allegati da aggiungere allora salto l'addRelated
												if (map.get("RELATED") != null && !map.get("RELATED").toString().equals("")) {
													
													// recupero la lista degli alegati da relazionare col padre
													List<String> listaAllegatiDaAggiungere = Arrays.asList(map.get("RELATED").toString().split(";"));
													List<String> allegatiDaAssociare = new ArrayList<String>();
													
													// controllo esistenza su Alfresco di ogni singolo related da associare
													for(String allegato : listaAllegatiDaAggiungere) {
														
														docNumberAllegato = allegato;
														
														// solo per i generici
														if (map.get("TIPO_COMPONENTE") == null || map.get("TIPO_COMPONENTE").toString().toUpperCase().equals("")) {
															if (listIgnoreRelated.contains(allegato))
																continue;
														}
														
														
														// in questo caso il docnumber del principale è contenuto anche tra i related
														if (allegato.equals(docnumber)) continue;
														
														if (m.docPropertiesAllegati.get(allegato) != null) {
															
															// controllo esistenza allegato in alfresco. 
															if(m.docPropertiesAllegati.get(allegato).get("CREATED").toString().toUpperCase().equals("N")) { 
																
																m.writeLog(m.logFile, "Inizio creazione allegato " + allegato + " al documento master " + map.get("DOCNUM").toString());
																
																
																/* ricavo la lista dei metadati di profilo valorizzati per gli allegati. Se non valorizzati li scarto */																
																Map<String, String> mapAllegato = m.docPropertiesAllegati.get(allegato);
																Map<String, String> mapProfiloGenericoAllegato = new HashMap<String, String>();

																// lista dei campi di profilo generico dell'allegato
																List<String> listCampiProfiloGenericoAllegato = Arrays.asList(m.props.getProperty("CampiProfiloGenerico").split(",") );

																// mappa di profilo generico del documento allegato
																for (String s : listCampiProfiloGenericoAllegato) {
																	if (mapAllegato.get(s) != null)
																		mapProfiloGenericoAllegato.put(s, mapAllegato.get(s));
																}																
																
																// controllo se l'allegato deve ereditare il type_id dal padre oppure se deve essere settato = 'DOCUMENTO'
																if ( m.props.getProperty("AllegatoEreditaTypeId").equals("Y") )																		
																	mapProfiloGenericoAllegato.put("TYPE_ID", map.get("TYPE_ID"));
																else
																	mapProfiloGenericoAllegato.put("TYPE_ID", "DOCUMENTO");

																/* fine */
																
																
																
																// creazione documento allegato
																if (m.props.getProperty("SettaFilePath").toString().toUpperCase().equals("Y"))
																	filePath = m.props.get("FileOnFileSystem").toString();
																else
																	filePath = map.get("FILE_PATH").toString();
																							
																file = new File(filePath);
															    fis = new FileInputStream(file);
															    
															    try{
																    long start = new Date().getTime();
																	m.businessLogic.createDocument(m.token, mapProfiloGenericoAllegato, fis);
																	long end = new Date().getTime();
																	m.writeLog(m.logFile, "Tempo impiegato per creazione doc allegato: " + (end-start));
											
																
																	fis.close();
																}
																catch(Exception closeex){
																	m.writeLog(m.logFile, "errore chiusura file master2: "+closeex.getMessage());
																	
																	if(closeex.getMessage().matches("MIGRATION_MODE: documento con docId .+ esistente.")){
																		m.update("UPDATE " + m.tableName + " SET CREATED = 'Y' WHERE DOCNUM = " + allegato);									
																		m.writeLog(m.logFile, "Documento: " + allegato + " esistente flag created impostato a 'Y' correttamente");
																	}
																	else{
																		continue;
																	}																		
																}
																
																
																// bisogna settare le acl del documento allegato
																if (mapAllegato.get("ACL") != null) {
																	Map<String,EnumACLRights> aclDocumentAllegato = m.retrieveSetACLDocument(mapAllegato.get("ACL"));

																	if (aclDocumentAllegato.size()>0) {
																		long startt = new Date().getTime();
																		m.businessLogic.setACLDocument(m.token, allegato, aclDocumentAllegato);												
																		long endd = new Date().getTime();
																		m.writeLog(m.logFile, "Tempo impiegato per setACLDocument allegato: " + (endd-startt));
																		
																		m.writeLog(m.logFile, "Le ACL del documento: " + allegato + " sono state importate correttamente");
																	}
																}
																/*fine*/

																m.update("UPDATE " + m.tableName + " SET CREATED = 'Y' WHERE DOCNUM = " + allegato );
																
															}
															
															// lista degli allegati da associare
															allegatiDaAssociare.add(allegato);
															
															if (map.get("TIPO_COMPONENTE") == null || map.get("TIPO_COMPONENTE").toString().toUpperCase().equals("")) 
																listIgnoreRelated.add(allegato);															
														}
														else
														{
															m.writeLog(m.logFile, "Non e' possibile reperire le informazioni dell'allegato " + allegato + " in quanto non e' presente nella tabella di import. L'unita' documentaria non puo' essere fascicolata, protocollata, classificata");
															fascicolazionePossibile = false;
														}
														
													}
													
													// lista dei related del documento in alfresco  
													long start1 = new Date().getTime();													
                                                    List<String> toRemoveRelated = m.businessLogic.getRelatedDocuments(m.token, docnumber);
                                                	long end1 = new Date().getTime();
													m.writeLog(m.logFile, "Tempo impiegato per getRelatedDocuments: " + (end1-start1));                                                    
                                                    
                                                    // rimuovo le vecchie relazioni tra master e allegati in alfresco                      
                                                    m.writeLog(m.logFile, "Rimozione vecchie associazioni del documento master " + docnumber);
                                                    long start = new Date().getTime();
                                                    m.businessLogic.removeRelated(m.token, docnumber, toRemoveRelated);
                                                	long end = new Date().getTime();
													m.writeLog(m.logFile, "Tempo impiegato per removeRelated: " + (end-start));                                                    

                                                    m.writeLog(m.logFile, "Inizio associazione allegati al documento master " + docnumber);   
                                                    long startt = new Date().getTime();                                                    
                                                    m.businessLogic.addRelated(m.token, docnumber, allegatiDaAssociare);
                                                    long endd = new Date().getTime();
													m.writeLog(m.logFile, "Tempo impiegato per addRelated: " + (endd-startt));     
													
													m.writeLog(m.logFile, "L'associazione degli allegati al documento master " + map.get("DOCNUM").toString() + " e' stata eseguita correttamente ");
													
												}
												
												// resetto i flag
												m.update("UPDATE " + m.tableName + " SET NEEDS_RELATED_UPDATE = 'N' WHERE DOCNUM = " + docnumber);
											
											
										} catch(Exception e) {
											
											if (m.sessioneScaduta(e.getMessage())) {
												m.token = m.doLogin();
												m.writeLog(m.logFile, "token sovrascritto");
											}
											
											// riportare sul log l'errore e 
											m.writeLog(m.logFile, "Errore in fase di associazione allegati. Docnumber: " + docNumberAllegato + "; Errore: " + e.getMessage());						
										}										
										
									
									}
							}
							
							
							if (fascicolazionePossibile) {
								
									if ( map.get("TIPO_COMPONENTE") != null && map.get("TIPO_COMPONENTE").toString().toUpperCase().equals("PRINCIPALE") ) {
									
											// se devo fascicolare è inutile che classifico
											if (map.get("NEEDS_FASCICOLO_UPDATE") == null || map.get("NEEDS_FASCICOLO_UPDATE").toString().toUpperCase().equals("") 
													|| map.get("NEEDS_FASCICOLO_UPDATE").toString().toUpperCase().equals("N") ) {
													
													// classifico documento solo se principale
													try {
														
														if ( map.get("NEEDS_CLASSIFICA_UPDATE") != null && map.get("NEEDS_CLASSIFICA_UPDATE").toString().equals("Y")) {
															
															// se il campo 'classifica' non e' valorizzato salto la routine
															if ( classificazioneProfileInfo.get("CLASSIFICA") != null && !classificazioneProfileInfo.get("CLASSIFICA").toString().equals("") )
															{
																m.writeLog(m.logFile, "Inizio classificazione documento con docnumber " + docnumber );
																long start = new Date().getTime();
																m.businessLogic.classificaDocumento(m.token, docnumber, classificazioneProfileInfo);
																long end = new Date().getTime();
																m.writeLog(m.logFile, "Tempo impiegato per classificaDocumento: " + (end-start));																
																
																// update flag
																m.update("UPDATE " + m.tableName + " SET NEEDS_CLASSIFICA_UPDATE = 'N' WHERE DOCNUM = " + docnumber);
																
																m.writeLog(m.logFile, "Il documento: " + docnumber + " e' stato classificato correttamente");
															}		
															
														}
														
													} catch(Exception e) {
														
														if (m.sessioneScaduta(e.getMessage())) {
															m.token = m.doLogin();
															m.writeLog(m.logFile, "token sovrascritto");
														}
														
														// riportare sul log l'errore
														m.writeLog(m.logFile, "Errore in fase di classificazione documento. Docnumber: " + docnumber + "; Errore: " + e.getMessage());
													}
											}
									}
							}
							
								
							if (fascicolazionePossibile) {
								
									if ( map.get("TIPO_COMPONENTE") != null && map.get("TIPO_COMPONENTE").toString().toUpperCase().equals("PRINCIPALE") ) {	
										
											// fascicolo documento
											try {
												
												if ( map.get("NEEDS_FASCICOLO_UPDATE") != null && map.get("NEEDS_FASCICOLO_UPDATE").toString().equals("Y")) {
													
													// se il campo 'classifica' non e' valorizzato salto la routine
													if ( fascicolazioneProfileInfo.get("CLASSIFICA") != null && !fascicolazioneProfileInfo.get("CLASSIFICA").toString().equals("") )
													{
														m.writeLog(m.logFile, "Inizio fascicolazione documento con docnumber " + docnumber );
														
														long start = new Date().getTime();
														m.businessLogic.fascicolaDocumento(m.token, docnumber, fascicolazioneProfileInfo);
														long end = new Date().getTime();
														m.writeLog(m.logFile, "Tempo impiegato per fascicolaDocumento: " + (end-start));														
														
														// update flag
														m.update("UPDATE " + m.tableName + " SET NEEDS_FASCICOLO_UPDATE = 'N' WHERE DOCNUM = " + docnumber);
														
														m.writeLog(m.logFile, "Il documento: " + docnumber + " e' stato fascicolato correttamente");
													}													
													
												}
												
											} catch(Exception e) {
												
												if (m.sessioneScaduta(e.getMessage())) {
													m.token = m.doLogin();
													m.writeLog(m.logFile, "token sovrascritto");
												}
												
												// riportare sul log l'errore e 
												m.writeLog(m.logFile, "Errore in fase di fascicolazione documento. Docnumber: " + docnumber + "; Errore: " + e.getMessage());						
											}
											
										
											// protocolla documento
											try {
												
												if ( map.get("NEEDS_PROTOCOLLO_UPDATE") != null &&  map.get("NEEDS_PROTOCOLLO_UPDATE").toString().equals("Y")) {
													
													m.writeLog(m.logFile, "Inizio protocollazione documento con docnumber " + docnumber );
													
													long start = new Date().getTime();
													m.businessLogic.protocollaDocumento(m.token, docnumber, protocollazioneProfileInfo);
													long end = new Date().getTime();
													m.writeLog(m.logFile, "Tempo impiegato per protocollaDocumento: " + (end-start));
													
													// update flag
													m.update("UPDATE " + m.tableName + " SET NEEDS_PROTOCOLLO_UPDATE = 'N' WHERE DOCNUM = " + docnumber);
													
													m.writeLog(m.logFile, "Il documento: " + docnumber + " e' stato protocollato correttamente");													
												}
												
												
											} catch(Exception e) {
												
												if (m.sessioneScaduta(e.getMessage())) {
													m.token = m.doLogin();
													m.writeLog(m.logFile, "token sovrascritto");
												}
												
												// riportare sul log l'errore e 
												m.writeLog(m.logFile, "Errore in fase di protocollazione documento. Docnumber: " + docnumber + "; Errore: " + e.getMessage());						
											}		
											
										
									}	// close if
									
								
							}	// close dofascicola
							
					
				}	// close else
				
				
			}	// CLOSE TRY
			catch (Exception e) {
								
				if (m.sessioneScaduta(e.getMessage())) {
					m.token = m.doLogin();
					continue;
				}
				
				// riportare sul log l'errore e 
				m.writeLog(m.logFile, "Errore per il documento con docnumber: " + doc + "; Errore: " + e.getMessage());				
				continue;
			}
			finally {
				if (fis != null) {
					fis.close();
					fis = null;
				}
			}
			
			
		}	// CLOSE CICLO FOR
		
			
		m.connection.close();
	    
		m.writeLog(m.logFile, "Fine procedura");
	}
	
	private Properties readParams(String filePath) throws IOException {
		
		if ( props == null ) {
			props = new Properties();
			File config = new File(filePath);
			props.load(new FileInputStream(config));
		}
		
		return props;
	}	
	
	public void writeLog(String file, String s) throws IOException {
		
        TimeZone tz = TimeZone.getTimeZone("Europe/Paris"); 
        Date now = new Date();
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        df.setTimeZone(tz);
        String currentTime = df.format(now);
       
        FileWriter aWriter = new FileWriter(file, true);
        aWriter.write(currentTime + " " + s + "\r\n\r\n");
        
        System.out.println(currentTime + " " + s + "\r\n\r\n");
        
        aWriter.flush();
        aWriter.close();
    }		
	
	private String select(String whereCondition, String field, String nameTable, Connection conn) throws Exception {
		
		String valField = "";
		
		Statement statement = null;
		ResultSet rs = null;

		statement = conn.createStatement();
		
		try {
			String sql = "SELECT " + field + " FROM " + nameTable + " WHERE " + whereCondition;
			
			rs = statement.executeQuery(sql);

			while (rs.next()) 
				valField = rs.getString(field).toString();
			
		} catch (Exception e) {
			throw e;
		}
		finally {
			statement.close();
			rs.close();
		}

		return valField;
	}
	
	private Map<String, String> cleanProfileInfo(Map<String, String> pInfoTotale, String fields, String azione) throws Exception {
		
		String[] arrFields = fields.split(",");
		Map<String, String> mappaFinale = new HashMap<String, String>();
		
		try {
				
				for (String field : arrFields) {
					
					for (String key : pInfoTotale.keySet()) {
						
						if (key.toUpperCase().equals(field.toUpperCase())) {
							
							
							if (pInfoTotale.get(key) == null)
								continue;
								
							mappaFinale.put(key.toUpperCase(), pInfoTotale.get(key));
							
						}
							
					}	
					
				}

				
		} catch(Exception e) {
			writeLog(logFile, "Routine: 'cleanProfileInfo'. Errore: " + e.getMessage());
			throw e;
		}
		
		return mappaFinale;
	}		
		
	private void update(String sql) throws Exception  {
		long start = new Date().getTime();
		
		
		QueryRunner qr = null;
		
		try {
			
			qr = new QueryRunner(ds);
			
			try {
				qr.update(sql);
				
			} catch (SQLException e) {	
				
				// riportare sul log l'errore e 
				writeLog(logFile, "Errore in update. sql = : " + sql + "; Errore: " + e.getMessage());
				throw e;
			}
			
		} catch (Exception e) {

			// riportare sul log l'errore
			writeLog(logFile, "Routine: 'update'. Errore: " + e.getMessage());
			throw e;
		}
		finally {
			long end = new Date().getTime();
			
			writeLog(logFile, "tempo update : " + (end - start) + " msec");
		}
		
	}	
		
	private Map<String,EnumACLRights> retrieveSetACLDocument(String strACL) throws Exception {

	    Map<String,EnumACLRights> acldocument = new HashMap<String, EnumACLRights>();
		String[] arrCoppia = null;
		
		try {

			String[] arrStrACL = strACL.split("[,;]");
			List<String> listACL = Arrays.asList(arrStrACL); 

			// trasformazione delle coppie acl in EnumACLRights
			for(String coppia : listACL) {
				
				arrCoppia = coppia.split("=");
				
				acldocument.put(arrCoppia[0], getEnumACLRights(arrCoppia[1]));			
		    }
			
			return acldocument;
		}
			
		 catch(Exception e) {
			
			// riportare sul log l'errore e 
			writeLog(logFile, "Routine: 'retrieveSetACLDocument'. Errore: " + e.getMessage());
			throw new Exception();
		}
		
	}
	
	private Map<String, Map<String, String>> retrieveAllDocuments(String tipoComponente, Connection conn) throws Exception {
		
		String nomeCampo = "";
		String valoreCampo = "";

		Statement statement = null;
		ResultSet rs = null;
		
		String rangeMinYear = "";
		if ( props.getProperty("RangeMinYear") != null && !props.getProperty("RangeMinYear").equals("") )
			rangeMinYear = props.getProperty("RangeMinYear");
		
		String rangeMaxYear = "";
		if ( props.getProperty("RangeMaxYear") != null && !props.getProperty("RangeMaxYear").equals("") )
			rangeMaxYear = props.getProperty("RangeMaxYear");
		
		
		statement = conn.createStatement();
		
		Map<String, String> map = null;
		Map<String, Map<String, String>> profileProperties = new HashMap<String, Map<String, String>>();
		String docnumber = "";
		
		try {
			
			String[] arrReturnFields = props.getProperty("ReturnFields").toString().split(",");
			
			String sql = "SELECT " + props.getProperty("ReturnFields").toString() + " FROM " + props.getProperty("TableName");
			
			if (tipoComponente.toUpperCase().equals("GENERICO"))
				sql += " WHERE TIPO_COMPONENTE = NULL OR TIPO_COMPONENTE = '' ";
			else {
				sql += " WHERE TIPO_COMPONENTE = '" + tipoComponente + "' ";
			}
			
			if ( !rangeMinYear.equals("") && !rangeMaxYear.equals("") ) {
				String startYear = "01/01/" + rangeMinYear;
				String finishYear = "31/12/" + rangeMaxYear;
				
				sql += " AND CREATION_DATE BETWEEN TO_DATE('" + startYear + "','DD/MM/YYYY') AND TO_DATE('" + finishYear + "','DD/MM/YYYY')";
			}
			
//			sql += " and docnum = 22500";

			rs = statement.executeQuery(sql);

			while (rs.next()) {

				map = new HashMap<String, String>();
				
				docnumber = rs.getString("DOCNUM").toString();
				
				for(int k=0; k<=arrReturnFields.length-1; k++) {
					
					nomeCampo = arrReturnFields[k].toString();
					valoreCampo = rs.getString(rs.getMetaData().getColumnName(k+1));
					
					map.put(nomeCampo, valoreCampo);
				}
				
				profileProperties.put(docnumber, map);
			}			
			
		} catch (SQLException e) {

			// riportare sul log l'errore
			writeLog(logFile, "Routine: 'retrieveAllDocuments'. Errore: " + e.getMessage());
			throw e;
		}		
		finally {
			statement.close();
			rs.close();
		}
		
		return profileProperties;
	}
	
	
	private boolean sessioneScaduta(String errorMessage){
		
		if (errorMessage.toUpperCase().matches("TICKET +NON +VALIDO +O +UTENTE +NON +AUTORIZZATO")) {
			return true;
		}
		if (errorMessage.toUpperCase().matches("TOKEN +NON +VALIDO")) {
			return true;
		}
		return false;
	}
	
	private Connection retrieveConnection() throws SQLException {
		
		try {
			
			OracleDataSource ods = new OracleDataSource();
			ods.setUser(uid);
			ods.setPassword(pwd);
			ods.setURL(connectionURL);
			connection = ods.getConnection();			

		} catch (SQLException e) {
			throw e;
		} 
		
		return connection;
	}
	
	public static EnumACLRights getEnumACLRights(String value) {

        if (value == null)
            return EnumACLRights.undefined;

        try {
            int acl = Integer.parseInt(value);
            if (acl == -2)
                return EnumACLRights.deny;
            if (acl == -1)
                return EnumACLRights.remove;
            if (acl == 3)
                return EnumACLRights.viewProfile;
            if (acl == 2)
                return EnumACLRights.readOnly;
            if (acl == 1)
                return EnumACLRights.normalAccess;
            if (acl == 0)
                return EnumACLRights.fullAccess;

            return EnumACLRights.undefined;
        } catch (NumberFormatException e) {
            return EnumACLRights.undefined;
        }

    }
	
	
}	
