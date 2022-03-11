package it.kdm.docer.sdk.frontend.utils;

import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.sdk.exceptions.DocerException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

public enum UserInfoCache {

	INSTANCE;
    private Pattern conservRegex = Pattern.compile("GRUPPO_CONSERV\\[([^\\]]+)\\]\\[([^\\]]+)\\]");

    private Logger log = org.slf4j.LoggerFactory.getLogger(UserInfoCache.class);
	
	private Map<String, OMElement> userInfoMap;

	private String coreAddr;
	
	private UserInfoCache() {
		try {
			this.userInfoMap = new HashMap<String, OMElement>();
			
			Properties props = ConfigurationUtils.loadProperties("docer_frontend.properties");
			this.coreAddr = props.getProperty("core.addr");
            String conservRegex = props.getProperty("conserv.group.regex");
            if (!StringUtils.isEmpty(conservRegex)) {
                this.conservRegex = Pattern.compile(conservRegex);
            } else {
                log.warn("La regex dei gruppi per la gestione della conservazione e' vuota");
            }
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} catch (PatternSyntaxException e) {
            log.error("La regex dei gruppi per la gestione della conservazione non e' valida", e);
        }
	}

	public synchronized OMElement getUserInfo(String token) throws DocerException {
		log.debug("getUserInfo(, Bool forceReset)");
		
		return getUserInfo(token, false);
	}
	public synchronized OMElement getUserInfo(String token, Boolean forceReset) throws DocerException {
		log.debug("getUserInfo()");
		try {
			
			String uid = Utils.extractTokenKey(token, "uid");
			if(!this.userInfoMap.containsKey(uid) || forceReset) {
				log.debug("Caching userInfo data for: " + uid);
				HttpClient client = new HttpClient();
				
				PostMethod method = new PostMethod(coreAddr + "/getUserInfo");
				
				method.addParameter("token", token);
				
				client.executeMethod(method);
				
				OMElement doc = Utils.parseCleanedSoapXML(method.getResponseBodyAsStream());
				
				if(method.getStatusCode() != 200) {
					OMElement root = doc.getFirstElement();
					String response;
					if (root.getFirstElement() != null) {
						response = root.getFirstElement().getText();
					} else {
						response = root.getText();
					}
					throw new DocerException(response);
				}
				
				OMFactory factory = OMAbstractFactory.getOMFactory();
				OMElement userInfo = factory.createOMElement("userInfo", null);

				OMElement enteSelezionato = null;
				OMElement enteSelezionatoDesc = null;

				Iterator<?> nodes = doc.getChildElements();
				while(nodes.hasNext()) {
					OMElement elem = (OMElement)nodes.next();
					Iterator<?> childNodes = elem.getChildElements();
					String key=null, value=null;
					while(childNodes.hasNext()) {
						OMElement child = (OMElement)childNodes.next();
						if(child.getLocalName().equals("key")) {
							key = child.getText().toLowerCase();
						} else if(child.getLocalName().equals("value")) {
							value = child.getText();
						}
					}
					
					if(key == null) {
						//TODO: migliorare messaggio d'errore
						throw new DocerException("Invalid service response");
					}

                    if (key.equals("___groups")) {

                        OMElement groupsElement = factory.createOMElement("gruppi_conserv", null);
                        userInfo.addChild(groupsElement);


                        if (conservRegex != null) {
                            String[] groups = value.split(";");
                            for (String group : groups) {
                                Matcher matcher = conservRegex.matcher(group.trim());
                                if (matcher.matches()) {
                                    String ente = matcher.group(1);
                                    String aoo = matcher.group(2);

                                    OMElement groupElement = factory.createOMElement("gruppo", null);
                                    groupsElement.addChild(groupElement);

                                    OMElement enteElement = factory.createOMElement("ente", null);
                                    enteElement.setText(ente);
                                    groupElement.addChild(enteElement);
                                    OMElement aooElement = factory.createOMElement("aoo", null);
                                    aooElement.setText(aoo);
                                    groupElement.addChild(aooElement);
                                }
                            }
                        }
                    }

					OMElement item = factory.createOMElement(key, null);
					item.setText(value);

					if (key.equals("ente_selezionato")) {
						enteSelezionato = item;
					} else if (key.equals("ente_selezionato_desc")) {
						enteSelezionatoDesc = item;
					}

					userInfo.addChild(item);
				}

				if(Utils.hasTokenKey(token, "ente") && !Utils.extractTokenKey(token, "ente").isEmpty()) {
					if (enteSelezionato == null) {
						enteSelezionato = OMAbstractFactory.getOMFactory().createOMElement("ente_selezionato", null);
						userInfo.addChild(enteSelezionato);
					}

					enteSelezionato.setText(Utils.extractTokenKey(token, "ente"));

					if (enteSelezionatoDesc == null) {
						enteSelezionatoDesc = OMAbstractFactory.getOMFactory().createOMElement("ente_selezionato_desc", null);
						userInfo.addChild(enteSelezionatoDesc);
					}

					enteSelezionatoDesc.setText(getEnteDesc(token));
				}
				
				this.userInfoMap.put(uid, userInfo);
			}
			
			return this.userInfoMap.get(uid);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new DocerException(e);
		}
	}

	private String getEnteDesc(String token) throws Exception{
		HttpClient client = new HttpClient();

		PostMethod method = new PostMethod(coreAddr + "/getEnteDescription");

		method.addParameter("token", token);

		client.executeMethod(method);

		OMElement doc = Utils.parseCleanedSoapXML(method.getResponseBodyAsStream());

		String response = doc.getFirstElement().getText();
		if(method.getStatusCode() != 200) {
			throw new DocerException(response);
		}

		return response;
	}
}

	