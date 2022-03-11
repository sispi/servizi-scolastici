package it.kdm.docer.fonte.batch.popolamentoFonte.objects;

import it.kdm.docer.commons.XMLUtil;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class FieldDescriptor {

    static private final String VOID = "";

    private String propName = "";

    private String regexFormat = "";
    private Pattern stringPattern = Pattern.compile(regexFormat);

    private File xsdFileLocation = null; 
    // 2004-02-01T00:00:00.000+01:00
    String regexDate = "^([0-9]{4})\\-([0-9]{2})\\-([0-9]{2})";
    String regexDatetime = "^([0-9]{4})\\-([0-9]{2})\\-([0-9]{2})[T ]([0-9]{2})\\:([0-9]{2})\\:([0-9]{2})";

    private Pattern datePattern = Pattern.compile(regexDate);
    private Pattern datetimePattern = Pattern.compile(regexDatetime);

    private boolean pointToAnagrafica = false;
    private String anagraficaTypeId = "";
    private boolean multivalue = false;

    private EnumBLPropertyType type = EnumBLPropertyType.UNDEFINED;

    public FieldDescriptor(String propName, EnumBLPropertyType type, String format, boolean multivalue) throws Exception {

        if (propName == null || propName.equals(VOID))
            throw new Exception("Errore configurazione DocumentType: propName is null");

        this.propName = String.valueOf(propName).toUpperCase();

        if (type.equals(EnumBLPropertyType.UNDEFINED))
            throw new Exception("Errore configurazione DocumentType: property type is UNDEFINED");

        this.type = type;

        if (format != null) {
            
                if(format.startsWith("schemaLocation=")){
                    xsdFileLocation = new File(format.replaceAll("^schemaLocation=", ""));
//                    if(!xsdFileLocation.exists()){
//                        throw new Exception("Errore definizione nodi fields: campo format: schemaLocation fa riferimento ad un file XSD non esistente per il nodo: " + propName);
//                    }
                }
                else{
                    try {
                        stringPattern = Pattern.compile(format);
                    }
                    catch (PatternSyntaxException e) {
                        throw new Exception("Errore definizione nodi fields: sintassi errata regexp format per il nodo: " + propName);
                    }
                }
                
                this.regexFormat = format;
            
        }

        this.multivalue = multivalue;

    }

    public void setPropName(String propName) {
        this.propName = String.valueOf(propName).toUpperCase();
    }

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

    public String checkValueFormat(String propValue) throws Exception {

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

    private String checkValue(String propValue) throws Exception {

        if (propValue == null) {
            return null;
        }

        if (!propValue.equals("")){
            
            if (type.equals(EnumBLPropertyType.DATE) || type.equals(EnumBLPropertyType.DATETIME)) {
                
                String dateFormatMessage = null;

                try {
                    DateTime date = parseDateTime(propValue);
                }
                catch (Exception e) {
                    dateFormatMessage = e.getMessage();
                }

                if (dateFormatMessage != null) {

                    String anno = "0";
                    String mese = "0";
                    String giorno = "0";
                    String ore = "0";
                    String minuti = "0";
                    String secondi = "0";

                    Matcher matcher = datetimePattern.matcher(propValue);

                    if (!matcher.find()) {

                        matcher = datePattern.matcher(propValue);
                        if (!matcher.find()) {
                            throw new Exception("il metadato " + propName + " di tipo " + type + " ha formato errato: atteso ISO8601 nel formato completo oppure YYYY-MM-DD oppure YYYY-MM-DDThh:mm:ss oppure YYYY-MM-DD hh:mm:ss");
                        }

                        anno = matcher.group(1);
                        mese = matcher.group(2);
                        giorno = matcher.group(3);
                    }
                    else {
                        anno = matcher.group(1);
                        mese = matcher.group(2);
                        giorno = matcher.group(3);
                        ore = matcher.group(4);
                        minuti = matcher.group(5);
                        secondi = matcher.group(6);
                    }

                    DateTime d = new DateTime(Integer.parseInt(anno), Integer.parseInt(mese), Integer.parseInt(giorno), Integer.parseInt(ore), Integer.parseInt(minuti), Integer.parseInt(secondi), 0);
                    propValue = d.toString(ISODateTimeFormat.dateTime());
                }

                return propValue;

            }
            
            if (type.equals(EnumBLPropertyType.INTEGER)) {
                
                try {
                    Integer.parseInt(propValue);
                }
                catch (NumberFormatException e) {
                    throw new Exception(propName + " di tipo " + type + " ha formato errato: valore=" + propValue);
                }
            }
            
            if (type.equals(EnumBLPropertyType.DECIMAL)) {

                try {
                    DecimalFormat.getInstance().parse(propValue);
                }
                catch (ParseException e) {
                    throw new Exception(propName + " di tipo " + type + " ha formato errato: valore=" + propValue);
                }
            }

        }
        
        if (type.equals(EnumBLPropertyType.BOOLEAN)) {

            if (!propValue.equals("true") && !propValue.equals("false")) {
                throw new Exception(propName + " di tipo " + type + " ammette true/false: valore=" + propValue);
            }
        }
       
        // se il formato e' definito
        if (regexFormat != null && !regexFormat.equals("")) {

            if(regexFormat.startsWith("schemaLocation=")){
                
                // annullamento del campo
//                if(propValue.equals("")){
//                    return propValue;
//                }
                
                if(!xsdFileLocation.exists()){
                    throw new Exception("metadato " + propName + " di tipo " + type + ": il file XSD di validazione non e' stato trovato: " +xsdFileLocation.getName());
                }
                
                XMLUtil dom = null;                             
                
                try{
                    dom = new XMLUtil(propValue);
                    
                    dom.validate(xsdFileLocation.getAbsolutePath());
                }
                catch(Exception e) {
                    throw new Exception("errore di validazione XSD per il metadato " + propName +": " +e.getMessage());
                }
                
                                
                return propValue;
            }
                        
            Matcher matcher = stringPattern.matcher(propValue);
            if (!matcher.find()) {
                throw new Exception("il metadato " + propName + " di tipo " + type + " ha formato errato; regex formato atteso: " + regexFormat + "; value=" + propValue);
            }            
                
        }

        return propValue;

    }

    private DateTime parseDateTime(String datetime) {
        DateTimeFormatter fm = ISODateTimeFormat.dateTime();
        return fm.parseDateTime(datetime);
    }

}
