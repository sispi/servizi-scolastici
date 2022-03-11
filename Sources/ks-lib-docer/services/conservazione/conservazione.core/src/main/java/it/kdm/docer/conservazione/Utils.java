package it.kdm.docer.conservazione;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.w3c.dom.CDATASection;


public class Utils {

	private final static Pattern xmlHeaderPattern = Pattern.compile("<\\?xml.*?encoding=\"(\\S*?)\".*?\\?>");


	public static String findXmlCharset(String xml) {
		String firstLine = xml.substring(0, xml.indexOf(">")+1);
		Matcher matcher = xmlHeaderPattern.matcher(firstLine);
		if(matcher.matches()) {
			return matcher.group(1);
		} else {
			return "UTF-8";
		}
	}
	
	private static Set<String> envelopes;
	
	static {
		envelopes = new HashSet<String>();
		envelopes.add(".P7M");
		envelopes.add(".P7S");
		envelopes.add(".P7X");
	}
	
	public static String findExtension(String filename) {
		StringBuffer ret = new StringBuffer();
		int index = filename.lastIndexOf('.');
		
		if(index != -1) {
			String ext = filename.substring(index);
			filename = filename.substring(0, index);
		
			while(index != -1 && envelopes.contains(ext.toUpperCase())) {
				ret.insert(0, ext);
				
				index = filename.lastIndexOf('.');
				
				if(index != -1) {
					ext = filename.substring(index);
					filename = filename.substring(0, index);
				}
			}
			
			if(index != -1) {
				ret.insert(0, ext);
			}
			
			ret.deleteCharAt(0);
		}
		
		return ret.toString();
	}
	
	private static DateTimeFormatter[] formats = new DateTimeFormatter[] {
			DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"),
			DateTimeFormat.forPattern("yyyy-MM-dd"),
			ISODateTimeFormat.dateTime()
	};
	
	public static boolean isDateParsable(String dateString) {
		
		if(dateString == null || dateString.equals("")) {
			return false;
		}
		
		DateTime date = parse(dateString);
		if(date == null) return false;
		if(date.getCenturyOfEra() < 19) return false;
		
		return true;
	}
	
	public static String parseDateTime(String dateString) {
		DateTime date = parse(dateString);
		return ISODateTimeFormat.dateTime().print(date);
	}
	
	public static String parseDate(String dateString) {
		DateTime date = parse(dateString);
		return ISODateTimeFormat.date().print(date);
	}
	
	private static DateTime parse(String dateString) {
		for(DateTimeFormatter format : formats) {
			DateTime date = null;
			try {
				date = format.parseDateTime(dateString);
			} catch(Exception ex) {
				continue;
			}
			
			return date;
		}
		
		return null;
	}

	public static final String INNER_SEP = " - ";
	public static final String OUTER_SEP = ";";

	public static String collapseMitDes(String content) throws Exception {

		StringBuilder out = new StringBuilder();

		OMElement doc = AXIOMUtil.stringToOM(content);

		process(doc, out, "//Amministrazione", new String[] {
				"Denominazione",
				"CodiceAmministrazione",
				"../AOO/Denominazione",
				"../AOO/CodiceAOO",
				"UnitaOrganizzativa/Denominazione",
				"UnitaOrganizzativa/Identificativo"
		});

		process(doc, out, "//PersonaGiuridica", new String[] {
				"Denominazione",
				"IndirizzoTelematico",
				"concat(IndirizzoPostale/Toponimo, ' ', " +
						"IndirizzoPostale/Civico, ' ', " +
						"IndirizzoPostale/CAP, ' ', " +
						"IndirizzoPostale/Comune, ' ', " +
						"IndirizzoPostale/Provinzia, ' ', " +
						"IndirizzoPostale/Nazione)"
		});

		process(doc, out, "//Persona", new String[] {
				"concat(Nome, ' ', Cognome)",
				"IndirizzoTelematico",
				"concat(IndirizzoPostale/Toponimo, ' ', " +
						"IndirizzoPostale/Civico, ' ', " +
						"IndirizzoPostale/CAP, ' ', " +
						"IndirizzoPostale/Comune, ' ', " +
						"IndirizzoPostale/Provinzia, ' ', " +
						"IndirizzoPostale/Nazione)"
		});

		if (out.length() > 0) {
			return out.substring(1);
		}

		return out.toString();
	}

	private static void process(OMElement doc, StringBuilder out, String selector, String[] format) throws Exception {
		AXIOMXPath xPath = new AXIOMXPath(selector);
		List<?> elements = xPath.selectNodes(doc);

		for (Object obj : elements) {
			OMElement amm = (OMElement) obj;

			out.append(OUTER_SEP);

			for(int i=0; i<format.length; i++) {
				String xpath = format[i];

				String text = getText(amm, xpath);
				if (!text.trim().isEmpty()) {

					out.append(text).append(INNER_SEP);
				}
			}
		}

		int idxLastSep = out.length() - INNER_SEP.length();

		if (idxLastSep > 0 && out.substring(idxLastSep).equals(INNER_SEP)) {
			out.delete(idxLastSep, out.length());
		}
	}

	private static String getText(OMElement element, String xPath) throws Exception {
		AXIOMXPath axiomxPath = new AXIOMXPath(xPath);
		Object ret = axiomxPath.selectSingleNode(element);

		if (ret != null) {
			if (ret instanceof OMElement) return ((OMElement)ret).getText();
			else return ret.toString();
		}

		return "";
	}
}
