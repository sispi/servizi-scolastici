package it.emilia_romagna.fonteraccoglitore.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FonteMappingWithJson extends FonteMapping {
	private static String FONTE_MAPPING_FILE = "fonte_mapping.json";
	private static Logger LOG = org.slf4j.LoggerFactory.getLogger(FonteMappingWithJson.class);
	private Map<String, String> fonti;

	public FonteMappingWithJson() throws Exception {
		//TODO: Fare una cache per evitare di caricare il file tutte le volte
		InputStream str = null;
		try {
			str = this.getClass().getClassLoader().getResourceAsStream(FONTE_MAPPING_FILE);
			JsonParser parser = new JsonParser();
			JsonElement parsed = parser.parse(new InputStreamReader(str));
			fonti = new HashMap<String, String>();
			if (parsed.isJsonArray()) {
				for (JsonElement element : parsed.getAsJsonArray()) {
					fonti.put(element.getAsString(), element.getAsString());
				}
			} else if (parsed.isJsonObject()) {
				JsonObject jsonObject = parsed.getAsJsonObject();
				for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
					fonti.put(entry.getKey(), entry.getValue().getAsString());
				}
			} else {
				throw new Exception("Configurazione non ammissibile.");
			}
		} finally {
			if (str != null) {
				try {
					str.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	@Override
	public Collection<String> getFonti() {
		return fonti.values();
	}

	@Override
	public Map<String, String> getFontiMap() {
		return fonti;
	}
}
