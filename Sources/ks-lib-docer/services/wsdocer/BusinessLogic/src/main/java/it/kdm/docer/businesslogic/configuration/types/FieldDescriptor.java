package it.kdm.docer.businesslogic.configuration.types;

import it.kdm.docer.businesslogic.configuration.enums.EnumBLPropertyType;
import it.kdm.docer.commons.XMLUtil;
import it.kdm.docer.sdk.exceptions.DocerException;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class FieldDescriptor {

	static private final String VOID = "";

	boolean inheritedFromDefault = true;
	private String propName = "";

	private String regexFormat = "";
	private Pattern stringPattern = Pattern.compile(regexFormat);

	private File xsdFileLocation = null;

	private boolean pointToAnagrafica = false;
	private String anagraficaTypeId = "";
	private boolean multivalue = false;

	private EnumBLPropertyType type = EnumBLPropertyType.UNDEFINED;

	public static List<DateTimeFormatter> dateTimeFormats;

	static {
		List<DateTimeFormatter> dateTimeFormatListLocale = new ArrayList<DateTimeFormatter>();
		dateTimeFormatListLocale.add(ISODateTimeFormat.basicDate().withLocale(Locale.ITALIAN));
		dateTimeFormatListLocale.add(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withLocale(Locale.ITALIAN));
		dateTimeFormatListLocale.add(DateTimeFormat.forPattern("yyyy-MM-dd").withLocale(Locale.ITALIAN));
		dateTimeFormatListLocale.add(ISODateTimeFormat.basicDateTimeNoMillis().withLocale(Locale.ITALIAN));
		dateTimeFormatListLocale.add(ISODateTimeFormat.dateTimeParser().withLocale(Locale.ITALIAN));
		dateTimeFormatListLocale.add(DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss").withLocale(Locale.ITALIAN));
		dateTimeFormatListLocale.add(DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss").withLocale(Locale.ITALIAN));
		dateTimeFormatListLocale.add(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS").withLocale(Locale.ITALIAN));
		dateTimeFormatListLocale.add(DateTimeFormat.forPattern("EEE MMM d HH:mm:ss 'UTC' yyyy").withLocale(Locale.ENGLISH));
		dateTimeFormats = Collections.unmodifiableList(dateTimeFormatListLocale);
	}


	public FieldDescriptor(String propName, EnumBLPropertyType type, String format, boolean multivalue) throws DocerException {

		if (propName == null || propName.equals(VOID))
			throw new DocerException("Errore configurazione DocumentType: propName is null");

		this.propName = String.valueOf(propName).toUpperCase();

		if (type.equals(EnumBLPropertyType.UNDEFINED))
			throw new DocerException("Errore configurazione DocumentType: property type is UNDEFINED");

		this.type = type;

		if (format != null) {

			if (format.startsWith("schemaLocation=")) {
				xsdFileLocation = new File(format.replaceAll("^schemaLocation=", ""));
				// if(!xsdFileLocation.exists()){
				// throw new
				// DocerException("Errore definizione nodi fields: campo format: schemaLocation fa riferimento ad un file XSD non esistente per il nodo: "
				// + propName);
				// }
			} else {
				try {
					stringPattern = Pattern.compile(format);
				} catch (PatternSyntaxException e) {
					throw new DocerException("Errore definizione nodi fields: sintassi errata regexp format per il nodo: " + propName);
				}
			}

			this.regexFormat = format;

		}

		this.multivalue = multivalue;

	}

	//    public void setPropName(String propName) {
	//	this.propName = String.valueOf(propName).toUpperCase();
	//    }

	public void setAnagraficaTypeId(String anagraficaTypeId) {
		this.anagraficaTypeId = anagraficaTypeId.toUpperCase();
		this.pointToAnagrafica = true;
	}

	public String getPropName() {
		return propName;
	}

	public String getFormat() {
		return regexFormat;
	}

	public EnumBLPropertyType getType() {
		return type;
	}

	public String getAnagraficaTypeId() {
		return anagraficaTypeId;
	}

	public boolean isMultivalue() {
		return multivalue;
	}

	public boolean pointToAnagrafica() {
		return pointToAnagrafica;
	}

	public String checkValueFormat(String propValue) throws DocerException {

		if (propValue == null)
			return null;

		if (!multivalue)
			return checkValue(propValue);
		else {
			String checkedString = "";
			String[] propValueArr = propValue.split(" *; *");
			for (String pv : propValueArr) {
				if (!pv.equals(VOID))
					checkedString += checkValue(pv) + ";";
			}

			checkedString = checkedString.replaceAll(";$", "");

			return checkedString;
		}
	}

	private String checkValue(String propValue) throws DocerException {

		if (propValue == null) {
			return null;
		}

		if (!propValue.equals("")) {

			if (type.equals(EnumBLPropertyType.DATE) || type.equals(EnumBLPropertyType.DATETIME)) {

				try {
					propValue = convertToZuluDateTime(propValue);
				} catch (Exception e) {
					throw new DocerException(propName + " di tipo " + type + " ha formato errato: valore=" + propValue);
				}

				return propValue;
			}


			if (type.equals(EnumBLPropertyType.INTEGER)) {

				try {
					Integer.parseInt(propValue);
				} catch (NumberFormatException e) {
					throw new DocerException(propName + " di tipo " + type + " ha formato errato: valore=" + propValue);
				}
			}

			if (type.equals(EnumBLPropertyType.DECIMAL)) {

				try {
					DecimalFormat.getInstance().parse(propValue);
				} catch (ParseException e) {
					throw new DocerException(propName + " di tipo " + type + " ha formato errato: valore=" + propValue);
				}
			}

		}

		if (type.equals(EnumBLPropertyType.BOOLEAN)) {

			if (!propValue.equals("true") && !propValue.equals("false")) {
				throw new DocerException(propName + " di tipo " + type + " ammette true/false: valore=" + propValue);
			}
		}

		// se il formato e' definito
		if (regexFormat != null && !regexFormat.equals("")) {

			if (regexFormat.startsWith("schemaLocation=")) {

				// annullamento del campo
				// if(propValue.equals("")){
				// return propValue;
				// }

				if (!xsdFileLocation.exists()) {
					throw new DocerException("metadato " + propName + " di tipo " + type + ": il file XSD di validazione non e' stato trovato: " + xsdFileLocation.getName());
				}

				XMLUtil dom = null;

				try {
					dom = new XMLUtil(propValue);

					dom.validate(xsdFileLocation.getAbsolutePath());
				} catch (Exception e) {
					throw new DocerException("errore di validazione XSD per il metadato " + propName + ": " + e.getMessage());
				}

				return propValue;
			}

			Matcher matcher = stringPattern.matcher(propValue);
			if (!matcher.find()) {
				throw new DocerException("il metadato " + propName + " di tipo " + type + " ha formato errato; regex formato atteso: " + regexFormat + "; value=" + propValue);
			}

		}

		return propValue;

	}

	public static String convertToZuluDateTime(String localInstant) {
		DateTime dt = null;
		String result = "N/A";
		Locale localeUsed = Locale.getDefault();

		for (DateTimeFormatter dateFormat : dateTimeFormats) {
			try {
				dt = dateFormat.parseDateTime(localInstant);
				localeUsed = dateFormat.getLocale();
				if (dt != null)
					break;
			} catch (IllegalArgumentException iae) {
			}
		}

		if (dt == null) {
			throw new IllegalArgumentException("Unable to convert to Date the instant: " + localInstant);
		} else {
			DateTime dtUTC;
			if (localeUsed.equals(Locale.ITALIAN)) {
				dtUTC = dt.toDateTime(DateTimeZone.UTC).toDateTimeISO();
				result = ISODateTimeFormat.dateTime().print(dtUTC);
			} else {
				result = dt.toString(DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")).toString();
			}
		}
		return result;
	}

	public boolean isInheritedFromDefault() {
		return inheritedFromDefault;
	}

	public void setInheritedFromDefault(boolean inheritedFromDefault) {
		this.inheritedFromDefault = inheritedFromDefault;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		sb.append("[");
		sb.append("propName=" + propName);
		sb.append(", type=" + type);

		if (StringUtils.isNotEmpty(regexFormat)) {
			sb.append(", regexFormat=" + regexFormat);
		}

		if (multivalue) {
			sb.append(", multivalue=" + multivalue);
		}
		if (xsdFileLocation != null) {
			sb.append(", xsdFileLocation=" + xsdFileLocation);
		}

		if (pointToAnagrafica) {
			sb.append(", pointToAnagrafica=" + pointToAnagrafica);
			sb.append(", anagraficaTypeId=" + anagraficaTypeId);
		}

		sb.append("]");

		return sb.toString();
	}

	public FieldDescriptor copy() throws DocerException {
		FieldDescriptor fd = new FieldDescriptor(this.propName, this.type, this.regexFormat, this.multivalue);

		if (this.pointToAnagrafica) {
			fd.setAnagraficaTypeId(this.anagraficaTypeId);
		}

		return fd;
	}

}
