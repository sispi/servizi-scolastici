package it.kdm.docer.conservazione;

import it.kdm.docer.sdk.EnumStatiConservazione;
import it.kdm.docer.sdk.classes.KeyValuePair;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.utils.DataTable;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EmptyDocerLib implements IDocerLib {

	public String login(String userName, String password, String codiceEnte)
			throws DocerException {
		return "FAKE_TOKEN";
	}

	@Override
	public KeyValuePair[] loginSSO(String saml, String codiceEnte) throws DocerException {
		return new KeyValuePair[0];
	}

	public DataTable<String> search(String token,
			Map<String, List<String>> searchCriteria, Set<String> columnNames)
			throws DocerException {
		return new DataTable<String>();
	}

	public DataTable<String> searchAllegati(String token, String docId,
			Set<String> columnNames) throws DocerException {
		return new DataTable<String>();
	}

	public void updateDocumentState(String token, String docId,
			EnumStatiConservazione statoConservazione) throws DocerException {
		}

	public void updateDocumentProfile(String token, String docId,
			Map<String, String> metadata) throws DocerException {
	}

	public URI getDocuments(String token, String docId) throws DocerException {
		try {
			return new URI("file:///dev/null");
		} catch (URISyntaxException e) {
			throw new DocerException(e);
		}
	}

	@Override
	public boolean writeConfig(String ticket, String xml) throws DocerException {
		return false;
	}

}
