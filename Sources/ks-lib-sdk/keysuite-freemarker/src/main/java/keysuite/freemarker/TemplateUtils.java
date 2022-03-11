package keysuite.freemarker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Strings;
import freemarker.ext.dom.NodeModel;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import it.kdm.orchestratore.session.ActorsCache;
import it.kdm.orchestratore.session.Session;
import it.kdm.orchestratore.session.UserInfo;
import it.kdm.orchestratore.utils.ResourceUtils;
import keysuite.cache.ClientCache;
import keysuite.docer.client.ClientUtils;
import keysuite.docer.client.DocerBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.text.StrLookup;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.context.MessageSource;
import org.springframework.core.env.PropertyResolver;
import org.xml.sax.InputSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.*;

//import it.kdm.doctoolkit.services.SOLRClient;

public class TemplateUtils {

    //final static long ttl = 60*10*1000;
    //final static String isoPattern = "0000-00-00T00:00:00";
    //final static String token = "|ente:*|app:DOCAREA|uid:admin|";
    //private final static PassiveExpiringMap<String,String> map = new PassiveExpiringMap<>(ttl);

    MessageSource messageSource = null;
    //private final static KDMUtils KDMUtils = new KDMUtils();
    //private final static ResourceUtils RsxUtils = new ResourceUtils();
    //final static String resVer = ""+Math.abs((KDMUtils.getWarVersion()+"."+ KDMUtils.getWarTimestamp()).hashCode());
    //private final static ActorsCache actorsCache = ActorsCache.getInstance();
    //private final static ConfigAppBean configApp = new ConfigAppBean();

    public TemplateUtils(){
        try {
            this.messageSource = (MessageSource) Session.getBean("messageSource");
        } catch (Exception e) {
            this.messageSource = null;
        }
    }

    public HttpServletRequest getRequest(){
        return Session.getRequest();
    }

    public HttpServletResponse getResponse(){
        return Session.getResponse();
    }

    public Object getBean(String bean){
        return Session.getBean(bean);
    }

    public Object getKdmUtils(){
        return this;
    }

    public ResourceUtils getResources(){
        return new ResourceUtils();
    }
    
    public PropertyResolver getEnv(){ return (PropertyResolver) Session.getBean("environment"); };

    public Map getCurrentApp(){
        return (Map) getRequest().getAttribute("currentAppBean");
    }

    public ActorsCache getActorsCache() { return ActorsCache.getInstance(); }

    public ClientCache getClientCache() { return ClientCache.getInstance(); }

    public MessageSource getMessageSource() { return messageSource; };

    public static String remove(String qs, String key){
        return remove(qs,key,null);
    }

    public static String remove(String qs, String key, String val){

        String re;
        if (val!=null) {
            /*try {
                val = URLEncoder.encode(val,"utf-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }*/
            val = val.replaceAll("([^_a-zA-Z0-9])", "\\\\$1");

            re = "&"+key+"="+val+"(&|$)";
        } else {
            re = "&"+key+"=[^&]*(&|$)";
        }

        return qs.replaceAll(re,"$1");
    }

    public UserInfo getUserInfo(){
        return Session.getUserInfoNoExc();
    }

    /*public String getContext() {

        String reqContext = (String) Session.getRequest().getAttribute("context");
        if (Strings.isNullOrEmpty(reqContext))
            return Session.getRequest().getContextPath();
        else
            return reqContext;
    }*/

    /*public String resource(String path){
        return KDMUtils.resFolder+"/"+path.replaceFirst("^/","")+"?"+resVer;
    }*/

    /*public String context(String appName){
        Map app = ConfigAppBean.getApps().get(appName);
        if (app!=null){
            String context = (String) app.get("context");
            if (context==null) {
                String regex = (String) app.get("regex");
                context = regex.split("/")[1];
            }
            return context;
        } else {
            return null;
        }
    }*/

    public String ftl(String tmpl, Map<String,Object> buffer, Map<String,Object> buffer2){
        if (buffer2!=null) {
            buffer = new HashMap<>(buffer);
            buffer.putAll(buffer2);
        }
        return ftl(tmpl,buffer);
    }

    public String ftl(String tmpl, Map<String,Object> buffer){
        try {
            if (!buffer.containsKey("sede")){
                buffer = new LinkedHashMap<>(buffer);
                buffer.put("sede", Session.getSede());
            }

            return ftlHandler(tmpl, buffer);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public Map<String,List<Map<String,Object>>> groupBy(List<Map<String,Object>> data, String field){
        Map<String,List<Map<String,Object>>> list = new LinkedHashMap<>();

        for ( Map item : data ){

            String rCat = (String) item.get(field);

            if (rCat==null)
                rCat = "null";

            List<Map<String,Object>> cat = list.get(rCat);

            if (cat==null){
                cat = new ArrayList<>();
                list.put(rCat,cat);
            }

            cat.add(item);
        }
        return list;
    }

    public List<Map<String,Object>> clusterize(List<Map<String,Object>> instances, Object pivot){
        Map<String,String> chain = new LinkedHashMap<>();
        for( Map<String,Object> instance : instances){
            Object p = instance.get("parentInstanceId");
            if (p==null)
                chain.put( String.valueOf(instance.get("instanceId")), null);
            else
                chain.put( String.valueOf(instance.get("instanceId")), String.valueOf(p));
        }
        final Map<String,String> newchain = new LinkedHashMap<>();
        for (String instanceId : chain.keySet().toArray(new String[0]) ){
            String path = instanceId;
            String parent = instanceId;
            while(true) {
                parent = chain.get(parent);

                if (parent==null) {
                    break;
                }

                path = parent + "|" + path;
            }
            newchain.put(instanceId,path);
        }
        chain = newchain;

        instances = new ArrayList<>(instances);

        Collections.sort(instances, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                String i1 = String.valueOf(o1.get("instanceId"));
                String i2 = String.valueOf(o2.get("instanceId"));
                String p1 = newchain.get(i1);
                String p2 = newchain.get(i2);
                return p1.compareTo(p2);
            }
        });

        List<Map<String,Object>> cluster = new ArrayList<>();

        String pivotPath = null;
        if (pivot!=null)
            pivotPath = chain.get(pivot.toString());

        for( Map<String,Object> instance : instances){

            String instanceId = String.valueOf(instance.get("instanceId"));

            instance = new LinkedHashMap<>(instance);

            String path = chain.get(instanceId);

            instance.put("PATH",path);
            cluster.add(instance);

            if (pivotPath!=null) {
                if (instanceId.equals(pivot.toString())) {
                    instance.put("CLUSTER",true);
                    instance.put("CURRENT", true);
                } else if (("|" + path + "|").contains("|" + pivot + "|")) {
                    instance.put("CLUSTER",true);
                    instance.put("CHILD", true);
                } else if (("|" + pivotPath + "|").contains("|" + instanceId + "|")) {
                    instance.put("CLUSTER",true);
                    instance.put("PARENT", true);
                }
            }
        }
        return cluster;
    }

    public List<Map<String,String>> getBreadCrumb(String path, Collection parents){
        List<Map<String,String>> segments = new ArrayList<>();
        if (!Strings.isNullOrEmpty(path) && parents!=null){
            String[] PARENTIDS = (String[]) parents.toArray(new String[0]);

            path = path.substring(Session.getUserInfoNoExc().getCurrentTreeviewProfile().length()+1);


            String[] parts = path.split("/");

            for( int i=0; i < (parts.length-1) ; i++){
                String parentId = PARENTIDS[1+i+(PARENTIDS.length-parts.length)];
                String[] sids = parentId.split("[\\.!@]");
                String sid;
                String type = sids[sids.length-1];

                if ("aoo".equals(type))
                    sid = sids[sids.length-3];
                else
                    sid = sids[sids.length-2];

                Map<String,String> item = new LinkedHashMap<>();
                item.put("name",parts[i]);
                item.put("sid",sid);
                item.put("type",type);

                segments.add(item);
            }
        }
        return segments;
    }

    public static String getType(String id){
        if (id==null || !id.contains("@"))
            return null;
        return id.split("@")[1];
    }

    public static String getSid(String id){
        if (id==null || !id.contains("@"))
            return id;
        if (id.endsWith("@group") || id.endsWith("@user"))
            return id.split("@")[0];
        String[] sids = id.split("[\\.!@]");
        String sid = null;
        for(int i=sids.length-2;i>=0;i--){
            sid = sids[i];
            if (sid.length()>0)
                break;
        }
        try {
            return URLDecoder.decode(sid,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public Object format(Object item){
        return format(item,null,null);
    }

    public Object format(Object item, String opt){
        if (opt!=null && opt.contains("%")){
            return format(item,opt,null);
        } else {
            return format(item,null,opt);
        }
    }

    public Object format(Object item, String fmt, String type){
        if (item instanceof List){
            List in = (List) item;
            List out = new ArrayList();
            for( Object obj : in){
                out.add(format(obj,fmt,type).toString());
            }
            return out;
        } else if (item instanceof Map){
            Set<Map.Entry> in = ((Map)item).entrySet();
            List out = new ArrayList();
            for( Map.Entry obj : in){
                out.add( obj.getKey()+":"+ format(obj.getValue(),fmt,type).toString());
            }
            return out;
        } else if (item instanceof String){

            if (((String) item).matches("^\\d{4}-\\d{2}-\\d{2}T.*"))
                return ClientUtils.datetime(item,fmt);

            String[] sids;
            String sid = (String) item;
            String sep = null;

            if (sid.contains(",")) {
                sep = ",";
                sids = sid.split(sep);
            } else if (sid.contains(";")){
                sep = ";";
                sids = sid.split(sep);
            } else {
                sids = new String[] {sid};
            }

            for( int i=0; i<sids.length; i++ ){
                DocerBean bean = ClientUtils.getItem(sids[i], type );
                if (bean!=null) {
                    if (fmt!=null)
                        sids[i] = String.format(fmt,bean.getName(),sids[i]);
                    else
                        sids[i] = bean.getName();
                }
            }

            if (sep!=null)
                return StringUtils.join(sids,sep);
            else
                return sids[0];

        } else if (item!=null ) {
            return item.toString();
        } else {
            return "";
        }
    }

    public String getDisplayName(String id){
        return (String) format(id);
    }

    /*

    public List<String> getDisplayNames(String ids){
        List<String> names = new ArrayList<>();
        if (Strings.isNullOrEmpty(ids))
            return names;

        for( String id : ids.split("[,;]") ){
            String name = getDisplayName(id);
            names.add(name);
        }
        return names;
    }

    public String getDisplayName(String id){
        return getDisplayName(getSid(id),getType(id));
    }

    public List<String> getDisplayNames(String sids,String type){
        List<String> names = new ArrayList<>();
        if (Strings.isNullOrEmpty(sids))
            return names;

        for( String sid : sids.split("[,;]") ){
            String name = getDisplayName(sid,type);
            names.add(name);
        }
        return names;
    }

    public String getDisplayName(String sid,String type){
        String name = getName(sid,type);
        if (Strings.isNullOrEmpty(name) || name.equals(sid))
            return sid;
        else
            return String.format("%s (%s)",name,sid);
    }

    public String getName(String id){
        return getName(getSid(id),getType(id));
    }

    public List<String> getNames(String ids){
        List<String> names = new ArrayList<>();
        if (Strings.isNullOrEmpty(ids))
            return names;

        for( String id : ids.split("[,;]") ){
            String name = getName(getSid(id),getType(id));
            names.add(name);
        }
        return names;
    }

    public List<String> getNames(String sids,String type){
        List<String> names = new ArrayList<>();
        if (Strings.isNullOrEmpty(sids))
            return names;

        for( String sid : sids.split("[,;]") ){
            String name = getName(sid,type);
            names.add(name);
        }
        return names;
    }

    public String getName(String sid,String type){

        if (sid==null)
            return null;

        if (type==null || type.equals("group") || type.equals("user") || type.equals("aoo") || type.equals("ente"))
            return ActorsCache.getName(sid);
        else{
            DocerBean bean = ClientCache.getInstance().getItem(ClientUtils.getSolrId(sid,type));
            if (bean!=null)
                return bean.getName();
            else
                return null;
        }
    }

*/

    public static String date(Object date){
        if (date==null || "".equals(date))
            return "";
        return ClientUtils.datetime(date);
    }

    public static String datetime(Object date){
        return ClientUtils.datetime(date);
    }

    public static String isodate(Object date){
        if (date==null || "".equals(date))
            return "";
        //String iso = isodatetime(date);
        return isodatetime(date).substring(0,10);
    }

    public static String isodatetime(Object date){
        return ClientUtils.datetime(date,"iso");
        /*if (date==null || "".equals(date))
            return "";

        String iso;
        if (date instanceof String) {
            iso = (String) date;

            if (iso.matches("\\w.*"))
                iso = Utils.formatDateTime(Utils.parseDateTime(iso));

        } else if (date instanceof Date)
            iso = KDMUtils.getISO8601StringForDate( (Date) date );
        else if (date instanceof DateTime)
            iso = KDMUtils.getISO8601StringForDate( ((DateTime)date).toDate() );
        else
            throw new RuntimeException("invalid date type");

        if (iso.length()<isoPattern.length())
            iso = iso + isoPattern.substring(iso.length());
        return iso;*/
    }

    public String getMessage(String code){
        return getMessage(code,code);
    }

    public String getMessage(String code, String def, String... args){
        if (def==null)
            def = code;
        if (messageSource==null)
            return code;
        return messageSource.getMessage(code,args,def, getUserInfo().getLocale());
    }

    /*public String getCards(List<Map<String,Object>> corrispondenti) throws Exception{

        List<String> cards = new ArrayList<>();
        if (Strings.isNullOrEmpty(xmlStr))
            return cards;

        xmlStr = xmlStr.replaceAll("<SKIP>", "");
        xmlStr = xmlStr.replaceAll("</SKIP>", "");
        SAXReader reader = new SAXReader();
        Document document = reader.read(new InputSource(new StringReader(xmlStr)));

        //RootElement
        Element rootElement = document.getRootElement();

        for (Iterator i = rootElement.elementIterator(); i.hasNext();) {
            //Mittente
            Element corrElem = (Element)i.next();

            String des = null;
            String des1 = null;
            String cod = null;

            //codice, descrizione, indirizzo telematico,

            //Amministrazione
            Element ammElem = corrElem.element("Amministrazione");
            Element perFisElem = corrElem.element("Persona");
            Element perGiuElem = corrElem.element("PersonaGiuridica");
            Element uoElem = corrElem.element("UnitaOrganizzativa");

            if(ammElem != null){
                Amministrazione amministrazione = new Amministrazione();
                String amministrazioneXML =  ammElem.asXML();
                amministrazione.parse(amministrazioneXML);

                String codAmm = amministrazione.getCodiceAmministrazione();
                String desAmm = amministrazione.getDenominazione();
                String ind = amministrazione.getIndirizzoTelematico();

                if (corrElem.element("AOO")!=null) {
                    Element aooElem = corrElem.element("AOO");
                    String aooXML = aooElem.asXML();
                    AOO aoo = new AOO();
                    aoo.parse(aooXML);

                    String codAOO = aoo.getAOO();
                    String desAOO = aoo.getDenominazione();
                    String indAOO = aoo.getIndirizzoTelematico();

                    if (!Strings.isNullOrEmpty(indAOO))
                        ind = indAOO;

                    if (!Strings.isNullOrEmpty(desAOO) && !Strings.isNullOrEmpty(codAOO)){
                        codAmm = codAOO;
                        desAmm = desAOO;
                    }
                }

                des = desAmm;
                cod = ind!=null? ind : codAmm;

                UO uo = amministrazione.getUnitaOrganizzativa();

                if (uo!=null){
                    String codUO = uo.getCodiceUnitaOrganizzativa();
                    String desUO = uo.getDenominazione();
                    String indUO = uo.getDenominazione();

                    if (!Strings.isNullOrEmpty(indUO))
                        cod = indUO;

                    if (!Strings.isNullOrEmpty(desUO)){
                        des = desUO;
                        des1 = desAmm;
                    }
                }
            } else if(perFisElem != null){
                PersonaFisica personaFisica = new PersonaFisica();
                String personaFisicaXML =  perFisElem.asXML();
                personaFisica.parse(personaFisicaXML);

                des = personaFisica.getDenominazione();
                cod = personaFisica.getCodiceFiscale();
                String ind = personaFisica.getIndirizzoTelematico();
                if (!Strings.isNullOrEmpty(ind)){
                    des1 = cod;
                    cod = ind;
                }

            } else if(perGiuElem != null){
                PersonaGiuridica personaGiuridica = new PersonaGiuridica();
                String personaGiuridicaXML =  perGiuElem.asXML();
                personaGiuridica.parse(personaGiuridicaXML);

                des = personaGiuridica.getDenominazione();
                cod = personaGiuridica.getId();
                String ind = personaGiuridica.getIndirizzoTelematico();
                if (!Strings.isNullOrEmpty(ind)){
                    des1 = cod;
                    cod = ind;
                }

            } else if(uoElem != null){
                UO uo = new UO();
                String uoXML =  uoElem.asXML();
                uo.parse(uoXML);

                des = uo.getDenominazione();
                cod = uo.getCodiceUnitaOrganizzativa();
                String ind = uo.getIndirizzoTelematico();
                if (!Strings.isNullOrEmpty(ind)){
                    cod = ind;
                }
            }

            String card = String.format("%s <%s>",des,cod);
            if (des1!=null)
                card += "\n" + des1;

            cards.add(card);
        }

        return cards;

    }*/

    /*public String getCard(String corrispondenti) throws Exception{
        List<String> cards = getCards(corrispondenti);
        if (cards.size()>0)
            return cards.get(0);
        else
            return null;
    }*/

    /*public List<String> getCards(String xml) throws Exception{

        List<String> cards = new ArrayList<>();

        if (Strings.isNullOrEmpty(xml))
            return cards;

        List<Map<String,String>> corrs = parseCorrispondenti(xml);

        for( Map<String,String> corrispondente: corrs){

            //String type = corrispondente.get("@type");
            String des = corrispondente.get("denominazione");
            String cod = corrispondente.get("id");
            String des1 = corrispondente.get("info");

            String card = "";
            if (!Strings.isNullOrEmpty(des) && !Strings.isNullOrEmpty(cod))
                card = String.format("%s <%s>",des,cod);
            else if (!Strings.isNullOrEmpty(des))
                card = des;
            else if (!Strings.isNullOrEmpty(cod))
                card = cod;

            if (!Strings.isNullOrEmpty(des1))
                card += "\n<" + des1+">";

            cards.add(card);
        }

        return cards;
    }*/

    /*public static Map<String,Object> parseSegnatura(String xmlStr) throws Exception {
        Map<String,Object> result = new LinkedHashMap<>();

        if (Strings.isNullOrEmpty(xmlStr)){
            return result;
        }

        OMElement xml = AXIOMUtil.stringToOM(xmlStr);
        OMElement extra = AXIOMUtil.stringToOM(xml.getFirstElement().getText());

        AXIOMXPath xpathExpression = new AXIOMXPath("//Mittenti");

        String mittenti = xpathExpression.selectSingleNode(extra).toString();
        List<Map<String,String>> parsedMittenti = parseCorrispondenti(mittenti);

        xpathExpression = new AXIOMXPath("//Destinatari");

        String destinatari = xpathExpression.selectSingleNode(extra).toString();
        List<Map<String,String>> parsedDestinatari = parseCorrispondenti(destinatari);

        xpathExpression = new AXIOMXPath("//Oggetto");
        String oggetto = ((OMElement)xpathExpression.selectSingleNode(extra)).getText();

        result.put("Mittenti",parsedMittenti);
        result.put("Destinatari",parsedDestinatari);
        result.put("Oggetto",oggetto);

        return result;
    }*/

    /*public static List<Map<String,String>> parseCorrispondenti(String xmlStr){

        List<Map<String,String>> result = new ArrayList<>();
        try{

            if(xmlStr != null && xmlStr.length() != 0 ){
                xmlStr = xmlStr.replaceAll("<SKIP>", "");
                xmlStr = xmlStr.replaceAll("</SKIP>", "");
                SAXReader reader = new SAXReader();
                Document document = reader.read(new InputSource(new StringReader(xmlStr)));

                //RootElement
                Element rootElement = document.getRootElement();

                for (Iterator i = rootElement.elementIterator(); i.hasNext();) {

                    Map<String,String> corr = new LinkedHashMap<>();

                    //Mittente
                    Element mittElem = (Element)i.next();

                    //Amministrazione
                    Element ammElem = mittElem.element("Amministrazione");
                    if(ammElem != null){
                        Amministrazione amministrazione = new Amministrazione();
                        String amministrazioneXML =  ammElem.asXML();
                        amministrazione.parse(amministrazioneXML);

                        corr.put("@type","Amministrazione");
                        corr.put("COD_ENTE",amministrazione.getCodiceAmministrazione());
                        corr.put("DES_ENTE",amministrazione.getDenominazione());
                        String ind = amministrazione.getIndirizzoTelematico();
                        String id = amministrazione.getCodiceAmministrazione();
                        String denominazione = amministrazione.getDenominazione();
                        String denominazione2 = null;

                        UO uo = amministrazione.getUnitaOrganizzativa();

                        if (uo!=null){
                            String COD_UO = uo.getCodiceUnitaOrganizzativa();
                            String DES_UO = uo.getDenominazione();
                            String indUO = uo.getIndirizzoTelematico();

                            if (Strings.isNullOrEmpty(COD_UO))
                                COD_UO = DES_UO;
                            else if (Strings.isNullOrEmpty(DES_UO))
                                DES_UO = COD_UO;

                            if (Strings.isNullOrEmpty(ind))
                                ind = indUO;

                            denominazione2 = denominazione;
                            denominazione = DES_UO;
                            id = COD_UO;
                            corr.put("COD_UO",COD_UO);
                            corr.put("DES_UO",DES_UO);
                            PersonaFisica pf = uo.getPersona();
                            if (pf!=null){
                                corr.put("ACTOR_ID",pf.getId());
                            }

                            if ("competente".equals(uo.getTipo()))
                                corr.put("competente","true");
                        }

                        if (mittElem.element("AOO")!=null) {
                            Element aooElem = mittElem.element("AOO");
                            String aooXML = aooElem.asXML();
                            AOO aoo = new AOO();
                            aoo.parse(aooXML);

                            corr.put("COD_AOO",aoo.getCodiceAOO());
                            corr.put("DES_AO",aoo.getDenominazione());
                        }

                        corr.put("id",id);
                        corr.put("denominazione",denominazione);
                        corr.put("info",denominazione2);
                        corr.put("IndirizzoTelematico",ind);
                    }
                    //PersonaFisica
                    Element perFisElem = mittElem.element("Persona");
                    if(perFisElem != null){
                        PersonaFisica personaFisica = new PersonaFisica();
                        String personaFisicaXML =  perFisElem.asXML();
                        personaFisica.parse(personaFisicaXML);

                        corr.put("@type","Persona");
                        corr.put("id",personaFisica.getId());
                        corr.put("CodiceFiscale",personaFisica.getCodiceFiscale());
                        corr.put("info",personaFisica.getCodiceFiscale());
                        corr.put("IndirizzoTelematico",personaFisica.getIndirizzoTelematico());
                        corr.put("denominazione",personaFisica.getDenominazione());
                    }
                    //PersonaGiuridica
                    Element perGiuElem = mittElem.element("PersonaGiuridica");
                    if(perGiuElem != null){
                        PersonaGiuridica personaGiuridica = new PersonaGiuridica();
                        String personaGiuridicaXML =  perGiuElem.asXML();
                        personaGiuridica.parse(personaGiuridicaXML);

                        corr.put("@type","PersonaGiuridica");
                        corr.put("id",personaGiuridica.getId());
                        corr.put("IndirizzoTelematico",personaGiuridica.getIndirizzoTelematico());
                        corr.put("denominazione",personaGiuridica.getDenominazione());
                    }
                    //UO
                    Element uoElem = mittElem.element("UnitaOrganizzativa");
                    if(uoElem != null){
                        UO uo = new UO();
                        String uoXML =  uoElem.asXML();
                        uo.parse(uoXML);

                        String COD_UO = uo.getCodiceUnitaOrganizzativa();
                        String DES_UO = uo.getDenominazione();

                        corr.put("@type","UnitaOrganizzativa");
                        corr.put("id",COD_UO);
                        corr.put("IndirizzoTelematico",uo.getIndirizzoTelematico());
                        corr.put("denominazione",DES_UO);
                        corr.put("COD_UO",COD_UO);
                        corr.put("DES_UO",DES_UO);
                    }

                    result.add(corr);
                }
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return result;
    }*/

    public static boolean hasGroup(String[] roles){
        return Session.getUserInfoNoExc().hasGroup(roles);
    }

    public static boolean hasGroup(List<String> roles){
        return hasGroup(roles.toArray(new String[roles.size()]));
    }

    /*public static boolean checkRights(String rightsKey) {
        return checkRights(rightsKey,null);
    }*/

    /*public static boolean checkRights(String rightsKey,Integer user_rights) {
        try{
            if (user_rights!=null){
                ICIFSObject d = new Documento();
                d.setProperty("user_rights",""+user_rights);
                return Security.checkRights(d, rightsKey);
            } else {
                return Security.checkRights(null, rightsKey);
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }

    }*/

    public static String getProperty(String property){
        return getProperty(property,null);
    }

    public static String getProperty(String property, String def){
        return System.getProperty(property,def);
    }

    public static NodeModel parseXml(String xmlStr){
        try {
            return NodeModel.parse(new InputSource(new StringReader(xmlStr)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String ToJson(Object bean) throws IOException {
        return ToJson(bean,false);
    }

    public static String ToJson(Object bean, boolean trycatch) throws IOException {
        try{
            return new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(bean);
        } catch (Exception e) {
            if (trycatch) {
                if (bean instanceof Map) {
                    Map<String, Object> map = new LinkedHashMap<>();
                    for (Object key : ((Map)bean).keySet().toArray(new String[0])) {
                        try {
                            ToJson(((Map)bean).get(key));
                            map.put( (String) key,((Map)bean).get(key));
                        } catch (Exception e2) {
                            map.put( (String) key, "(serialization error)");
                        }
                    }
                    return ToJson(map);
                }
            }
            throw e;
        }

    }

    public static String toJson(Object bean) throws IOException {
        return ToJson(bean);
    }

    public static Object FromJson(String json) throws IOException{
        if (Strings.isNullOrEmpty(json)){
            return null;
        }
        return new ObjectMapper().readValue(json, Object.class);
    }

    public static Object fromJson(String json) throws IOException{
        return FromJson(json);
    }

    public static String FromBase64(String base64) throws UnsupportedEncodingException {
        byte[] bytes = Base64.getDecoder().decode(base64);
        return new String(bytes,"utf-8");
    }

    public static String fromBase64(String base64) throws UnsupportedEncodingException {
        return FromBase64(base64);
    }

    public static String ToBase64(String str) throws UnsupportedEncodingException {
        byte[] bytes = Base64.getEncoder().encode(str.getBytes("utf-8"));
        return new String(bytes,"utf-8");
    }

    public static String toBase64(String str) throws UnsupportedEncodingException{
        return ToBase64(str);
    }

    public static String getStackTrace(Throwable throwable){
        return ExceptionUtils.getStackTrace(throwable);
    }

    public static String fixContext(String context){
        if (Strings.isNullOrEmpty(context)){
            return "";
        } else if (!context.startsWith("/")){
            context=  "/" + context;
        }
        if (context.endsWith("/"))
            context = context.substring(0,context.length()-1);
        return context;
    }

    public static Map<String,Object> buildRenderModel(Map<String,Object> settings){
        settings = new LinkedHashMap(settings);

        //settings.put("resVer",resVer);
        //settings.put("resources",resFolder);
        settings.put("context",fixContext( (String) settings.get("context")));
        settings.put("utils", new TemplateUtils() );
        settings.put("$", new TemplateUtils() );

        if (!settings.containsKey("baseUrl")){
            settings.put("baseUrl",Session.getRequest().getServletPath());
        }
        return settings;
    }

    public static String ftlHandler( String template, Map settings) throws TemplateException, IOException {
        return ftlHandler(getFTLTemplate(template),settings);
    }

    public static String ftlHandler(Template t, Map settings) throws TemplateException, IOException {

        settings = buildRenderModel(settings);

        Writer out = new StringWriter();
        try {
            t.process(settings, out);
        } catch (Exception e) {
            HttpServletRequest request = Session.getRequest();
            if (request!=null)
                request.setAttribute("exceptionModel",settings);
            throw e;
        }
        String outString = out.toString();

        outString = resolveMessages(outString);

        return outString;
    }

    static Configuration cfg = null;

    public static Configuration getFTLConfiguration(){
        if (cfg==null){
            try{
				/*String basePath = ToolkitConnector.getGlobalProperty("resources.folder",RSX_FOLDER);

				final File templates = new File(basePath,"templates");
				final File reports = new File(basePath,"reports");

				if (!templates.exists())
					templates.mkdir();

				if (!reports.exists())
					reports.mkdir();*/

                cfg = new Configuration(Configuration.VERSION_2_3_0);

				/*MultiTemplateLoader ml = new MultiTemplateLoader(
						new TemplateLoader[]{
								//new FileTemplateLoader( reports ),
								//new FileTemplateLoader( templates ),
								new ResourceTemplateLoader( "reports/", cfg),
								new ResourceTemplateLoader( "templates/", cfg)
						}
				);

				cfg.setTemplateLoader(ml);*/

                cfg.setTemplateLoader(new ResourceTemplateLoader( "templates/", cfg));

                cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            } catch ( Exception e ){
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return cfg;
    }

    public static Template getFTLTemplate(String template) throws TemplateException, IOException{
        Template t;

        if (template.length()<=255 && template.matches("^[/\\w\\-. ]+$")){
            if (!template.toLowerCase().endsWith(".ftl"))
                template+=".ftl";
            Locale locale = (Locale) Session.getRequest().getAttribute("KS_LOCALE");
            t = getFTLConfiguration().getTemplate(template, locale );
        } else {
            t = new Template("temp"+template.hashCode(), new StringReader(template), getFTLConfiguration());
        }

        return t;
    }

    public static String resolveMessages(String outString){
        if (outString.contains("$[")){
            HttpServletRequest req = Session.getRequest();

            final Map<String,String> properties = (Map) req.getAttribute("properties");
            final MessageSource messageSource = Session.getBean(MessageSource.class);
            UserInfo ui = Session.getUserInfoNoExc();

            final String lang  = (String) Session.getRequest().getAttribute("KS_LANG");
            final Locale locale = (Locale) Session.getRequest().getAttribute("KS_LOCALE");

            StrSubstitutor substr = new StrSubstitutor(new StrLookup() {
                @Override
                public String lookup(String key) {

                    if (key.startsWith("#noparse"))
                        return "$"+key+"]";

                    String[] parts = key.split("(?<!\\\\),");
                    if (parts.length>1) {
                        for (String part : parts) {
                            if (part.startsWith(lang + ":")) {
                                String msg = part.substring(part.indexOf(":") + 1);
                                return msg;
                            }
                        }
                        return parts[0].substring(parts[0].indexOf(":") + 1);
                    }

                    if (key.startsWith("name:")){
                        key = key.substring(5);
                        DocerBean bean = ClientUtils.getItem(key.substring(5),null);
                        if (bean!=null)
                            return bean.getName();
                        else
                            return key;
                    }

                    String def = ui.isAdmin() ? String.format("<span class='mnotfound'>[%s]</span>",key) : String.format("[%s]",key) ;

                    if (key.contains(":")){
                        def = key.split(":")[1];
                        key = key.split(":")[0];
                    }

                    if (!Strings.isNullOrEmpty(System.getProperty("label:"+key)))
                        return System.getProperty("label:"+key);
                    else if (properties!=null&& properties.containsKey(key))
                        return properties.get(key);
                    else if (messageSource!=null)
                        return messageSource.getMessage( key, null, def, locale);
                    else
                        return def;

                }
            } ,"$[","]",'$');
            outString = substr.replace(outString);
            //outString = outString.replace("$#noparse","$[");

        }
        return outString;
    }
}
