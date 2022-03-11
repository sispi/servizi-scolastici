package it.kdm.docer.conservazione;

import it.kdm.docer.businesslogic.BusinessLogic;
import it.kdm.docer.sdk.EnumSearchOrder;
import it.kdm.docer.sdk.EnumStatiConservazione;
import it.kdm.docer.sdk.classes.KeyValuePair;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.docer.sdk.interfaces.IKeyValuePair;
import it.kdm.docer.sdk.interfaces.ISearchItem;
import it.kdm.utils.DataRow;
import it.kdm.utils.DataTable;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public class DocerLib implements IDocerLib {
	
	String providerName;
	int pimarySearchMaxRows;
	int maxFileLength;
	File diskBufferDirectory;
	
	BusinessLogic bl;

	public DocerLib(String provider, int primarySearchMaxRows, 
			File diskBufferDirectory, int maxFileLength,
			String configPath) throws DocerException {
		
		
		try {
			this.providerName = provider;
			this.pimarySearchMaxRows = primarySearchMaxRows;
			this.diskBufferDirectory = diskBufferDirectory;
			if(!this.diskBufferDirectory.exists()) {
				FileUtils.forceMkdir(this.diskBufferDirectory);
			}
			this.maxFileLength = maxFileLength;
			
			BusinessLogic.setConfigPath(configPath);
			bl = new BusinessLogic(providerName, pimarySearchMaxRows);
		
		} catch (IOException e) {
			throw new DocerException(e);
		}
	} 

	public String login(String userid, String password, String codiceEnte) throws DocerException {
		
		String token = "";
		
		try {
			
			// invocazione login della business logic
			token = bl.login(codiceEnte, userid, password);
		} 
		catch (Exception err) {
			throw new DocerException(err.getMessage());
		}

		return token;
	}

	public KeyValuePair[] loginSSO(String saml, String codiceEnte) throws DocerException {

        try {
            return bl.loginSSO(saml, codiceEnte);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + "Autenticazione fallita.", e);
        }

    }

	public DataTable<String> search(String token, Map<String, List<String>> searchCriteria, Set<String> columnNames) throws DocerException {
		
		// searchCriteri non valorizzati
		if (searchCriteria == null)
			throw new DocerException("criteri di ricerca non valorizzati");

		// columnNames non valorizzati
		if (columnNames == null)
			throw new DocerException("nomi delle colonne di ritorno non valorizzati");
		
		// ordinamento risultati
		Map<String, EnumSearchOrder> orderby = new HashMap<String, EnumSearchOrder>();
		orderby.put("DOCNAME", EnumSearchOrder.ASC);

		DataTable<String> dt = new DataTable<String>();
		
		try {
		
			// popolo il datatable con le colonne corrispondenti alle colonne richieste tra i params 
			for (String colname : columnNames) 
				dt.addColumn(colname);
								
			List<ISearchItem> list = bl.searchDocuments(token, searchCriteria, null, 100, orderby);
			
			// ciclo i searchItem
			for (ISearchItem item : list) {
				
				String docId = this.findDocId(item.getMetadata());
				
				Map<String, String> profile = getProfileDocument(token, docId);
				
				// per ogni item aggiungo un nuovo dataRow al dataTable
				DataRow<String> dr = dt.newRow();
				
				// coppie campo,valore
				for (String field : profile.keySet()) {
				
					if(columnNames.contains(field)) {
						dr.put(field, profile.get(field));
					}
				}
				
				// aggiungo la row al dataTable
				dt.addRow(dr);
			}			
		}
		catch (Exception err) {
			throw new DocerException(err);
		}
		
		return dt;		
	}

	private String findDocId(IKeyValuePair[] metadata) throws DocerException {
		
		for(IKeyValuePair pair : metadata) {
			if(pair.getKey().equalsIgnoreCase("DOCNUM")) {
				return pair.getValue();
			}
		}
		
		throw new DocerException("DOCNUM non trovato");
	}

	public DataTable<String> searchAllegati(String token, String docId, Set<String> columnNames) throws DocerException {
		
		// columnNames non valorizzati
		if (columnNames == null)
			throw new DocerException("nomi delle colonne di ritorno non valorizzati");

		DataTable<String> dt = new DataTable<String>();
				
		try {
			
			// popolo il datatable con le colonne corrispondenti alle colonne richieste tra i params 
			for (String colname : columnNames) 
				dt.addColumn(colname);
			
			// lista dei docnumber allegati al docId
			List<String> allegati = bl.getRelatedDocuments(token, docId);

			// ciclo gli allegati
			for (String docIdAllegato : allegati) {
				
				// recupero il profilo di ogni allegato
				Map<String, String> profDocument = getProfileDocument(token, docIdAllegato);
				
				// per ogni allegato aggiungo un nuovo dataRow al dataTable
				DataRow<String> dr = dt.newRow();
				
				// coppie campo,valore
				for (String campo : profDocument.keySet() ) {
				
					if(columnNames.contains(campo)) {
						dr.put(campo, profDocument.get(campo) );																										
					}
				}
				
				// aggiungo la row al dataTable
				dt.addRow(dr);
				
				// pulisco l'hash dai risultati
				profDocument.clear();
			}				
		}
		catch (Exception err) {
			throw new DocerException(err);
		}
		
		// ottengo un dt con le colonne corrispondenti ai campi [columnNames] richiesti e i record corrispondenti 
		// agli allegati al [docId]
		return dt;
	}

	public void updateDocumentState(String token, String docId, EnumStatiConservazione statoConservazione) throws DocerException {

		// TODO: Why?
		//if (statoConservazione == null || statoConservazione.equals(EnumStatiConservazione.in_errore))
		//	throw new DocerException("stato conservazione errato");
		
		try {
			
			String conservazioneStato = Integer.toString(statoConservazione.getCode());
			Map<String, String> meta = new HashMap<String, String>();
			meta.put("STATO_CONSERV", conservazioneStato);
						
			bl.updateProfileDocument(token, docId, meta);
		}
		catch (Exception err) {
			throw new DocerException(err);
		}		
	}

	public void updateDocumentProfile(String token, String docId, Map<String, String> metadata) throws DocerException {
		
		// i controlli sui parametri di input vengono fatti nella BL
		try {
			bl.updateProfileDocument(token, docId, metadata);
		}
		catch (Exception err) {
			throw new DocerException(err);
		}
	}

	public URI getDocuments(String token, String docId) throws DocerException {
		
		// i controlli sui parametri di input vengono fatti nella BL		
		try {
			
			Map<String, String> profile = getProfileDocument(token, docId);
			String name = docId;
			
			if(profile.containsKey("DOCNAME")) {
				String tempName = profile.get("DOCNAME");
				if(!tempName.equals("")) {
					name = String.format("%s-%s", name, tempName);
				}
			}
			
			File file = new File(this.diskBufferDirectory, name);
			
			byte[] returnBytes = bl.downloadDocument(token, docId, file.getAbsolutePath(), 
					this.maxFileLength);
			
			if(returnBytes != null) {
				FileUtils.writeByteArrayToFile(file, returnBytes);
			}
			
			return file.toURI();
		}
		catch (Exception err) {
			throw new DocerException(err);
		}
		
	}

	@Override
	public boolean writeConfig(String ticket, String xml) throws DocerException {
		return bl.writeConfig(ticket, xml);
	}

	private Map<String, String> getProfileDocument(String token, String docId) throws DocerException {
        Map<String, String> profile = bl.getProfileDocument(token, docId);

        if (profile.containsKey("DOCNAME")) {
            profile.put("DOCNAME", normalizeName(profile.get("DOCNAME")));
        }

        return profile;
    }

    private String normalizeName(String name) {
        return name.replaceAll("[\\\\/?%*:|\"<>]", "");
    }
	
}
