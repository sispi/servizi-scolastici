package it.kdm.docer.commons;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import it.kdm.docer.clients.ClientManager;
import it.kdm.docer.clients.DocerExceptionException;
import it.kdm.docer.clients.DocerServicesStub;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.doctoolkit.model.Documento;
import it.kdm.utils.TransformerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.rmi.RemoteException;
import java.security.KeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

public class Utils {

	static Logger logger = org.slf4j.LoggerFactory.getLogger(Utils.class);

	private static XMLInputFactory xif = XMLInputFactory.newInstance();

	private Utils() {}

    public static final String USERNAME_SEP = "__";

    public static String validateField(String fieldValue){

            String value = fieldValue;


            //check sql inject
            while (value.startsWith("'"))
                value = value.substring(1);

            return value;
        }

        public static String validateField(String fieldValue, String defaultValue){

            String value = validateField(fieldValue);

            //check for default value
            if (fieldValue.equals(""))
                value = defaultValue;

            return value;
        }

        public static String convertToXML(List<Map<String,Object>> rs) throws Exception {
            return convertToXML(rs,null);
        }

        public static String convertToXML(List<Map<String, Object>> rs, String id) throws Exception
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder =factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element results = doc.createElement("Results");

            if (id!=null)
                results.setAttribute("id", id);

            doc.appendChild(results);

            for (Map<String, Object> result : rs) {
                Element row = doc.createElement("Row");
                results.appendChild(row);
                for (String columnName : result.keySet()) {
                    Object value = result.get(columnName);
                    Element node = doc.createElement(columnName.toLowerCase());
                    if (value == null)
                        node.appendChild(doc.createTextNode(""));
                    else if (columnName.equalsIgnoreCase("xmlconfig")) {
                        node.appendChild(doc.createCDATASection(value.toString()));
                    } else {
                        node.appendChild(doc.createTextNode(value.toString()));
                    }
                    row.appendChild(node);
                }
            }

            return getDocumentAsXml(doc);
        }

        public static String getDocumentAsXml(Document doc) throws TransformerConfigurationException, TransformerException {

            DOMSource domSource = new DOMSource(doc);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            //transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
            // we want to pretty format the XML output
            // note : this is broken in jdk1.5 beta!
            transformer.setOutputProperty
            ("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            //
            java.io.StringWriter sw = new java.io.StringWriter();
            StreamResult sr = new StreamResult(sw);
            transformer.transform(domSource, sr);
            return sw.toString();
        }

	public static String extractTokenKey(String token, String key) throws KeyException {
		logger.debug(String.format("extractTokenKey(%s, %s)", token, key));

                if(key == null || key.equals("")) {
                    throw new KeyException("Empty key cannot be extracted from token");
                }

		Pattern pattern = Pattern.compile(String.format("(?:\\||^)%s:([^|]*?)\\|.*", key));
		Matcher matcher = pattern.matcher(token);
		if(!matcher.find()) {

            // FIX per far funzionare l'SSO.
            if(key.startsWith("Provider_")){
                return null;
            }

			throw new KeyException(String.format("Key %s not found in token: %s", key, token));
		}

		return matcher.group(1);
	}

    public static String extractFirstTicket(String token) throws KeyException {
        logger.debug(String.format("extractFirstTicket(%s)", token));

        for (String cur : Splitter.on("|").omitEmptyStrings().split(token)) {
            if (cur.startsWith("ticket")) {
                return cur.split(":")[1];
            }
        }

        throw new KeyException(String.format("Key ticket* not found in token: %s", token));
    }

        public static String addTokenKey(String token, String key, String value) {
            logger.debug(String.format("addTokenKey(%s, %s, %s)", token, key, value));

            if(key == null || key.equals("")) {
                return token;
            }

//            if(value == null || value.equals("")) {
//                return token;
//            }

            if(value == null ) {
                value = "";
            }

            Pattern pattern = Pattern.compile(String.format("((?:\\||^)%s:)[^|]*?(\\|.*)", key));
            Matcher matcher = pattern.matcher(token);
            if(!matcher.find()) {
                token = token + String.format("%s:%s|", key, value);
            } else {
                token = matcher.replaceFirst(String.format("$1%s$2", value));
            }

            return token;
        }

        public static String removeTokenKey(String token, String key) {
            logger.debug(String.format("removeTokenKey(%s, %s)", token, key));

            if(key == null || key.equals("")) {
                return token;
            }

            Pattern pattern = Pattern.compile(String.format("(\\||^)%s:[^|]*?\\|(.*)", key));
            Matcher matcher = pattern.matcher(token);
            if(matcher.find()) {
                token = matcher.replaceFirst("$1$2");
            }

            return token;
        }

        //private static final Pattern tokenPattern = Pattern.compile("^(?:[^:|]+:[^:|]+\\|)+$");
        private static final Pattern tokenPattern = Pattern.compile("^(?:[^:|]+:[^:|]*\\|)+$");

        public static boolean isTokenWellFormed(String token) {
            logger.debug(String.format("isTokenWellFormed(%s)", token));

            Matcher matcher = tokenPattern.matcher(token);

            return matcher.matches();
        }

        public static boolean hasTokenKey(String token, String key) throws KeyException {
		logger.debug(String.format("extractTokenKey(%s, %s)", token, key));

                if(key == null || key.equals("")) {
                    return false;
                }

		Pattern pattern = Pattern.compile(String.format("(?:\\||^)%s:(?:[^|]*?)\\|.*", key));
		Matcher matcher = pattern.matcher(token);
		if(!matcher.find()) {
			return false;
		}

		return true;
	}

	public static OMElement findToken(OMElement methodElement) {
		Iterator<?> iterator = methodElement.getChildElements();
		while(iterator.hasNext()) {
			OMElement currElement = (OMElement)iterator.next();
			if(currElement.getLocalName().equalsIgnoreCase("token")) {
				return currElement;
			}
		}

		return null;
	}

    public static String getUserFromToken(String token) throws KeyException {
        if (!hasTokenKey(token, "uid")) {
            throw new KeyException("Token must have a valid uid");
        }

        String user = extractTokenKey(token, "uid");

        if (hasTokenKey(token, "prefix")) {
            String prefix = extractTokenKey(token, "prefix");
            if (!Strings.isNullOrEmpty(prefix)) {
                user = prefix + USERNAME_SEP + user;
            }
        }

        return user;
    }

	public static OMElement parseMTOMSoapXML(String xml) throws XMLStreamException {
		return parseCleanedSoapXML(extractMessage(xml));
	}

	public static OMElement parseCleanedSoapXML(String xml) throws XMLStreamException {
		XMLStreamReader reader = xif.createXMLStreamReader(new StringReader(xml));

		return parseSoapXML(reader);
	}

	public static OMElement parseCleanedSoapXML(InputStream xml) throws XMLStreamException {
		XMLStreamReader reader = xif.createXMLStreamReader(xml);

		return parseSoapXML(reader);
	}

	public static OMElement parseMTOMSoapXML(InputStream xml)
			throws FactoryConfigurationError, XMLStreamException {

		return parseCleanedSoapXML(extractMessage(xml));
	}

	private static String extractMessage(String input)  throws XMLStreamException {
		try {
			return extractMessage(IOUtils.readLines(new StringReader(input)));
		} catch (IOException e) {
			throw new XMLStreamException(e);
		}
	}

	private static String extractMessage(InputStream input)  throws XMLStreamException {
		try {
			return extractMessage(IOUtils.readLines(input));
		} catch (IOException e) {
			throw new XMLStreamException(e);
		}
	}

	private static String extractMessage(List<String> lines) {

		String firstLine = lines.get(0).trim();

		while (firstLine.isEmpty()) {
			lines.remove(0);
			firstLine = lines.get(0).trim();
		}

		if (firstLine.startsWith("--")) {
			String line = firstLine;
			while (!line.startsWith("<")) {
				lines.remove(0);
				line = lines.get(0).trim();
			}

			line = lines.get(lines.size()-1).trim();
			while (!line.startsWith("--")) {
				lines.remove(lines.size());
				line = lines.get(lines.size()-1);
			}

			lines.remove(lines.size()-1);
		}

		return StringUtils.join(lines, '\n');

	}

	private static OMElement parseSoapXML(XMLStreamReader reader) {

		StAXOMBuilder builder = new StAXOMBuilder(reader);

		OMElement payload = builder.getDocumentElement();
		return payload;
	}

	public static String base64Encode(String text) {
		byte[] textBytes;
		try {
			textBytes = text.getBytes("UTF-8");
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			textBytes = text.getBytes();
		}

		byte[] data = Base64.encodeBase64(textBytes);
		return new String(data);
	}

	public static String base64Decode(String encodedText) {
		byte[] data = Base64.decodeBase64(encodedText.getBytes());

		String decodedString;
		try {
			decodedString = new String(data, "UTF-8");
		} catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
			decodedString = new String(data);
		}

		return decodedString;
	}

	public static String SHA1Sum(String string) throws IllegalStateException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            return toHex(md.digest(string.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    private static String toHex(byte[] digest) {
        Formatter formatter = new Formatter();
        for (byte b : digest) {
            formatter.format("%02x", b);
        }

        String ret = formatter.toString();
        formatter.close();

        return ret;
    }

    public static DateTime dateTimeParse(String dateTime, DateTime defaultReturn) {
        if (dateTime == null || dateTime.isEmpty()) {
            return defaultReturn;
        }

        DateTime dt;

        try {
            dt = ISODateTimeFormat.dateTimeParser().parseDateTime(dateTime);
        } catch (IllegalArgumentException ex) {
            try {
                dt = ISODateTimeFormat.dateParser().parseDateTime(dateTime.substring(0, dateTime.length()-1));
            } catch (IllegalArgumentException ex1) {
                dt = defaultReturn;
            }
        }

        return dt;

    }

    public static DateTime dateParse(String date, DateTime defaultReturn) {
        if (date == null || date.isEmpty()) {
            return defaultReturn;
        }

        DateTime dt;

        try {
            dt = ISODateTimeFormat.dateParser().parseDateTime(date);
        } catch (IllegalArgumentException ex) {
            try {
                dt = ISODateTimeFormat.dateTimeParser().parseDateTime(date);
            } catch (IllegalArgumentException ex1) {
                dt = defaultReturn;
            }
        }

        return dt;
    }

    public static DocerServicesStub.KeyValuePair[] getProfile(String token, String docId)
            throws RemoteException, DocerExceptionException {
        DocerServicesStub.KeyValuePair[] metadatiDOC;
        DocerServicesStub.GetProfileDocument doc = new DocerServicesStub.GetProfileDocument();

        doc.setDocId(String.valueOf(docId));
        doc.setToken(token);
        DocerServicesStub.GetProfileDocumentResponse resp = ClientManager.INSTANCE.getDocerServicesClient()
                .getProfileDocument(doc);
        metadatiDOC = resp.get_return();
        return metadatiDOC;
    }

    public enum enumSignProcessAction {PROTOCOLLARE,REGISTRARE}

    public static void runWorkflowSignProcess(String token, enumSignProcessAction action, String registro, String userName, String figuraProfessionale, String datiRichiesta, DocerServicesStub.KeyValuePair[] metadati) throws DocerException {

        token = "uid:" + userName + "|" + token;

        //TODO: mettere l'url in confgurazione
        String NEW_INSTANCE_COMPLETE_DOCER = "http://192.168.0.25:8082/"+ "bpm-server/process/startProcessDocer/{0}/{1}";
        String processName = "Filiera Semplice bis";
        String ID = "Filiera Semplice bis1.0";

        //converte il formato da KeyValurPair in HashMap
        HashMap<String,String> metaMap = convertKeyValuePairToMap(metadati);

        Map <String, Object> map = new HashMap<String, Object>();

        map.put("ENTE", metaMap.get("COD_ENTE"));
        map.put("DEPLOYMENT_ID", "default_per_instance");
        map.put("AOO", metaMap.get("COD_AOO"));
        map.put("outcome", "Complete");
        map.put("userToken", token);

        //parametri del processo

        map.put("redazione", "no");
        map.put("conversione", "no");
        map.put("firmare", "si");

        map.put("versioning", "si");
        map.put("registrare", "si");
        map.put("fascicolazione", "no");

        List <Map<String,String>> assegnazioneL = new ArrayList<Map<String,String>>();
        Map<String,String> assegnazione1 = new HashMap<String,String>();
        assegnazione1.put("@structure", "object");
        assegnazione1.put("identity", userName);
        assegnazione1.put("diritti", "r");
        assegnazione1.put("id", "assegnazione_base");
        assegnazione1.put("name", "assegnazione_base_name");

        assegnazioneL.add(assegnazione1);

        XMLUtil dom;
        OMElement tipoFirmatarioNode;
        String oggettoRegistrazione;
        OMElement mittenti;
        OMElement destinatari;
        try {
            dom = new XMLUtil(datiRichiesta);
            tipoFirmatarioNode = dom.getNode("//Flusso/Firmatario");
            mittenti = dom.getNode("//Flusso/Mittenti");
            destinatari = dom.getNode("//Flusso/Destinatari");
            oggettoRegistrazione = dom.getNodeValue("//Intestazione/Oggetto");
        } catch (Exception e) {
            e.printStackTrace();
            throw new DocerException(e);
        }

        if (mittenti!=null)
            map.put("mittenti",mittenti);

        if (destinatari!=null)
            map.put("destinatari",destinatari);

        if (action==enumSignProcessAction.REGISTRARE) {
            map.put("registrazione", "si");
            map.put("protocollazione", "no");
            map.put("tipo_registrazione", "u");
            map.put("reg_auto", "no");

            List<String> listaRegistri = new ArrayList<String>();
            listaRegistri.add(registro);
            map.put("lista_registri",listaRegistri);

            Map<String,String> registroMap = new HashMap<String, String>();
            registroMap.put("@structure","registro");
            registroMap.put("IDRegistro",registro);
            registroMap.put("id","registro_scelto");
            registroMap.put("name","registro_scelto");
            map.put("registro_scelto",registroMap);

            //map.put("assegnazione_registro",assegnazioneL);
            map.put("assegnazione",assegnazioneL);
            map.put("tipo_firma_registrazione","FD");
            map.put("oggetto_registro",oggettoRegistrazione);
//            map.put("firmatari_registrazione",firmatario);
        }

        if (action==enumSignProcessAction.PROTOCOLLARE) {
            map.put("protocollazione", "si");
            map.put("registrazione", "no");
            map.put("tipo_registrazione", "u");
            map.put("assegnazione",assegnazioneL);
            map.put("oggetto_protocollo",oggettoRegistrazione);
        }

        map.put("documento_principale_convertito.DOCNUM", metaMap.get("DOCNUM"));

        map.put("proto_auto", "no");
        map.put("approvazione", "no");
        map.put("timbro", "no");
        map.put("invio", "no");
        map.put("pubblicazione", "no");
        map.put("tipo_firma", "locale");
        map.put("deploymentId", "default-per-instance");






        //Call REST

        Documento doc = new Documento();
        doc.copyFrom(metaMap);
        Map<String,Object> flowObj = null;
        try {
            flowObj = doc.toFlowObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        List <Map<String,String>> mittentiL = (List <Map<String,String>>)flowObj.get("MITTENTI");
        List <Map<String,String>> destinatariL = (List <Map<String,String>>)flowObj.get("DESTINATARI");
        List <Map<String,String>> firmatariL = (List <Map<String,String>>)flowObj.get("FIRMATARIO");

        if (firmatariL.size()==0)
            throw new DocerException("Firmatario obbigatorio.");

        String firmatario = firmatariL.get(0).get("id");
        map.put("firmatario", firmatario);
        map.put("figura_professionale", metaMap.get("COD_AOO"));



        //risetta i mittenti/destinatari con il formato trasformato
        map.put("mittenti",mittentiL);
        map.put("destinatari",destinatariL);

        if (action==enumSignProcessAction.REGISTRARE) {
            map.put("firmatari_registrazione",firmatariL);
        }

        String restCall = MessageFormat.format(NEW_INSTANCE_COMPLETE_DOCER, ID, processName);

        RestTemplate restTemplate = new RestTemplate();
        setMessageConverterMixed(restTemplate);
        String parameters = hashMapToJson(map);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity request = new HttpEntity(parameters, headers);
        restTemplate.postForObject(restCall, request, String.class);

    }

    private static void setMessageConverterMixed(RestTemplate restTemplate) {
        List<HttpMessageConverter<?>> converter = new ArrayList<HttpMessageConverter<?>>();
        FormHttpMessageConverter httpConverter = new FormHttpMessageConverter();
        httpConverter.setCharset(Charset.forName("UTF-8"));
        converter.add(httpConverter);
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
        converter.add(stringConverter);
        MappingJackson2HttpMessageConverter mappingJacksonConverter = new MappingJackson2HttpMessageConverter();
        converter.add(mappingJacksonConverter);
        restTemplate.setMessageConverters(converter);
    }

    private static String hashMapToJson(Map<String,Object> mapSetting) {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";

        //convert map to JSON string
        try {
            json = mapper.writeValueAsString(mapSetting);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return json;
    }

    private static HashMap<String, String> convertKeyValuePairToMap(DocerServicesStub.KeyValuePair[] metadati) {
        HashMap<String, String> meta = new HashMap<String, String>();

        for (DocerServicesStub.KeyValuePair pair : metadati) {
            meta.put(pair.getKey(),pair.getValue());
        }

        return meta;
    }

    /*
        private static Map getMittenti(String xmlStr) throws DocumentException {

            if(xmlStr != null && xmlStr.length() != 0 ){
                xmlStr = xmlStr.replaceAll("<SKIP>", "");
                xmlStr = xmlStr.replaceAll("</SKIP>", "");
                SAXReader reader = new SAXReader();
                org.dom4j.Document document = reader.read(new InputSource(new StringReader(xmlStr)));

                //RootElement
                org.dom4j.Element rootElement = document.getRootElement();

                for (Iterator i = rootElement.elementIterator("Mittente"); i.hasNext();) {
                    //Mittente
                    org.dom4j.Element mittElem = (org.dom4j.Element)i.next();

                    //Amministrazione
                    org.dom4j.Element ammElem = mittElem.element("Amministrazione");
                    if(ammElem != null){
                        Amministrazione amministrazione = new Amministrazione();
                        String amministrazioneXML =  ammElem.asXML();
                        amministrazione.parse(amministrazioneXML);

                        org.dom4j.Element aooElem = mittElem.element("AOO");
                        String aooXML = aooElem.asXML();
                        AOO aoo = new AOO();
                        aoo.parse(aooXML);
                        amministrazione.setAOO(aoo);

                        result.add(amministrazione);
                    }
                    //PersonaFisica
                    org.dom4j.Element perFisElem = mittElem.element("Persona");
                    if(perFisElem != null){
                        PersonaFisica personaFisica = new PersonaFisica();
                        String personaFisicaXML =  perFisElem.asXML();
                        personaFisica.parse(personaFisicaXML);
                        result.add(personaFisica);
                    }
                    //PersonaGiuridica
                    org.dom4j.Element perGiuElem = mittElem.element("PersonaGiuridica");
                    if(perGiuElem != null){
                        PersonaGiuridica personaGiuridica = new PersonaGiuridica();
                        String personaGiuridicaXML =  perGiuElem.asXML();
                        personaGiuridica.parse(personaGiuridicaXML);
                        result.add(personaGiuridica);
                    }
                }
            }
        }
    */
    public static boolean checkArchiveTypeConsistency(String token, String docId, String archiveType) throws Exception {

        if (archiveType.equals("URL")) {
            throw new Exception("Documenti di tipo URL non supportati");
        }

        DocerServicesStub.GetRelatedDocuments req = new DocerServicesStub.GetRelatedDocuments();
        req.setToken(token);
        req.setDocId(docId);
        DocerServicesStub.GetRelatedDocumentsResponse resp = ClientManager.INSTANCE.getDocerServicesClient()
                .getRelatedDocuments(req);

        if (resp.get_return() != null) {
            for (String relatedId : resp.get_return()) {
                DocerServicesStub.KeyValuePair[] profile = getProfile(token, relatedId);

                String relatedArchiveType = null;

                for (DocerServicesStub.KeyValuePair pair : profile) {
                    if (pair.getKey().equalsIgnoreCase("ARCHIVE_TYPE")) {
                        relatedArchiveType = pair.getValue();
                        break;
                    }
                }

                if (!archiveType.equals(relatedArchiveType)) {
                    return false;
                }
            }
        }

        return true;
    }
}
