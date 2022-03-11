package it.kdm.docer.sdk.frontend.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.security.KeyException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

public class Utils {
	
	static Logger logger = org.slf4j.LoggerFactory.getLogger(Utils.class);
	
	private static XMLInputFactory xif = XMLInputFactory.newInstance();
	
	private Utils() {}
	
	public static String extractTokenKey(String token, String key) throws KeyException {
		logger.debug(String.format("extractTokenKey(%s, %s)", token, key));
		
                if(key == null || key.equals("")) {
                    throw new KeyException("Empty key cannot be extracted from token");
                }
                
		Pattern pattern = Pattern.compile(String.format("(?:\\||^)%s:([^|]*?)\\|.*", key));
		Matcher matcher = pattern.matcher(token);
		if(!matcher.find()) {
			throw new KeyException(String.format("Key %s not found in token: %s", key, token));
		}

		return matcher.group(1);
	}
        
        public static String addTokenKey(String token, String key, String value) {
            logger.debug(String.format("addTokenKey(%s, %s, %s)", token, key, value));
            
            if(key == null || key.equals("")) {
                return token;
            }
            
            if(value == null || value.equals("")) {
                return token;
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
        
        private static final Pattern tokenPattern = Pattern.compile("^(?:[^:|]+:[^:|]+\\|)+$");

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
			logger.error(ex.getMessage(),ex);
			decodedString = new String(data);
		}
		
		return decodedString;
	}
}
