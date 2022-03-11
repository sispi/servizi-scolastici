package keysuite.docer.server;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.underscore.lodash.U;
import com.google.common.base.Strings;
import it.kdm.orchestratore.session.Session;
import keysuite.docer.client.*;
import keysuite.docer.client.corrispondenti.Corrispondente;
import keysuite.docer.client.corrispondenti.ICorrispondente;
import keysuite.solr.SolrUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.MultiMapSolrParams;
import org.apache.solr.common.util.StrUtils;

import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerUtils {

    public final static String RUOLI_GRUPPO = "ruoli.gruppo";
    public final static String UP_SEP = System.getProperty("ruoli.separator","_");
    public final static String NORMAL = "normalAccess";
    public final static String READ = "readOnly";
    public final static String FULL = "fullAccess";
    public static ObjectMapper OM = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    public static Map<String,String> toDocerMap(Documento bean){
        InputStream stream = null;
        try {
            stream = bean.getStream();
            bean.setStream(null);
            Map<String,String> map =  ServerUtils.toDocerMap( (DocerBean) bean);

            map.remove("VERSION_COUNT");

            /*if (bean.getMittente()!=null){
                map.put("MITTENTI", "<Mittenti>" + bean.getMittente().toXml("Mittente") + "</Mittenti>" );
            }

            if (bean.getDestinatari()!=null && bean.getDestinatari().length>0){

                String xml = "<Destinatari>";

                for ( ICorrispondente dest : bean.getDestinatari() )
                    xml += dest.toXml("Destinatario");

                xml += "</Destinatari>";

                map.put("DESTINATARI" , xml );
            }*/

            return map;
        } finally {
            bean.setStream(stream);
        }
    }

    public static Map<String,String> toDocerKey(DocerBean bean){
        String type = bean.getType();
        Map<String,String> map = toDocerMap(bean);
        Map<String,String> keys = new LinkedHashMap<>();

        keys.put("COD_ENTE", map.get("COD_ENTE"));
        keys.put("COD_AOO", map.get("COD_AOO"));

        if (map.containsKey("CLASSIFICA"))
            keys.put("CLASSIFICA", map.get("CLASSIFICA"));
        if (map.containsKey("PIANO_CLASS"))
            keys.put("PIANO_CLASS", map.get("PIANO_CLASS"));
        if (map.containsKey("ANNO_FASCICOLO"))
            keys.put("ANNO_FASCICOLO", map.get("ANNO_FASCICOLO"));
        if (map.containsKey("PROGR_FASCICOLO"))
            keys.put("PROGR_FASCICOLO", map.get("PROGR_FASCICOLO"));
        if (map.containsKey("COD_"+type)) {
            keys.put("COD_" + type, map.get("COD_" + type));
            keys.put("TYPE_ID",type);
        }

        return keys;
    }

    public static SearchParams parseQueryString(String querystring){
        MultiMapSolrParams params = SolrUtils.parseQueryString(querystring);
        SearchParams sp = new SearchParams();
        for( String param : params.getMap().keySet() ){
            sp.set(param, params.getParams(param));
        }
        return sp;
    }

    final static List<String> ignoreUpdate = Arrays.asList(
            "CREATION_DATE","MODIFIER","MODIFIED","CREATOR","CREATED",
            "PARENT_PROGR_FASCICOLO","INHERITS_ACL","PARENTIDS","VIRTUAL_PATH","PHYSICAL_PATH");

    public static Map<String,String> toUpdateMap(DocerBean oldBean, DocerBean bean){
        //String type = bean.getType();
        Map<String,String> oldMap = toDocerMap(oldBean);
        Map<String,String> map = toDocerMap(bean);

        for( String ignF : ignoreUpdate ){
            map.remove(ignF);
            oldMap.remove(ignF);
        }

        //Map<String,String> checkMap = new HashMap<>(map);

        for ( String key : map.keySet().toArray(new String[0]) ){
            Object newVal = map.get(key);
            if (newVal==null || "".equals(newVal))
                map.remove(key);
        }

        for ( String key : map.keySet().toArray(new String[0]) ){
            if (key.matches(".*(_DT|_X|_XD|_I|_T)$")) {
                int idx = key.lastIndexOf("_");
                String shorter = key.substring(0, idx);
                if (map.containsKey(shorter) || oldMap.containsKey(shorter))
                    map.remove(key);
            }
        }

        for ( String key : oldMap.keySet().toArray(new String[0]) ){
            if (key.matches(".*(_DT|_X|_XD|_I|_T)$")) {
                int idx = key.lastIndexOf("_");
                String shorter = key.substring(0, idx);
                if (map.containsKey(shorter) || oldMap.containsKey(shorter))
                    oldMap.remove(key);
            }
        }

        for ( String key : oldMap.keySet() ){

            Object oldVal = oldMap.get(key);
            Object newVal = map.get(key);

            if ("".equals(oldVal))
                oldVal = null;

            if ("".equals(newVal))
                newVal = null;

            if (oldVal == null && newVal == null){
                map.remove(key);
                continue;
            }

            if (oldVal == null || newVal == null)
                continue;

            if (oldVal.equals(newVal)) {
                map.remove(key);
                continue;
            }

            if (key.equals("MITTENTI") && oldBean instanceof Documento){

                Corrispondente oldCorr = (Corrispondente) ((Documento)oldBean).getMittente();
                Corrispondente newCorr = (Corrispondente) ((Documento)bean).getMittente();

                if (oldCorr.equals(newCorr))
                    map.remove(key);
            }

            if (key.equals("DESTINATARI") && oldBean instanceof Documento){

                int hc1 = Arrays.hashCode ( (Object[]) ((Documento)oldBean).getDestinatari());
                int hc2 = Arrays.hashCode ( (Object[]) ((Documento)bean).getDestinatari());

                if (hc1==hc2)
                    map.remove(key);
            }
        }

        for( String key : bean.getNullFields() )
            if (oldMap.get(key)!=null && !map.containsKey(key))
                map.put(key,null);

        //map.remove("COD_ENTE");
        //map.remove("COD_AOO");

        return map;
    }

    final static Collection<String> prefixedValues = Arrays.asList("CREATOR","MODIFIER","GROUP_ID","ADMIN_GROUP_ID","PARENT_GROUP_ID","USER_ID");
    final static Collection<String> prefixedArrays = Arrays.asList("acl_explicit","groups");
    final static Pattern prefixedRegex = Pattern.compile("(id|ADMIN_GROUP_ID|PARENT_GROUP_ID|GROUP_ID|USER_ID|acl_explicit|groups):((?:\\([^\\)]+\\)|[^\\s+]+))");

    public static String rewriteWithPrefix(String prefix, String query){

        StringBuffer resultString = new StringBuffer();
        Matcher regexMatcher = prefixedRegex.matcher(query);
        while (regexMatcher.find()) {
            String field = regexMatcher.group(1);
            String value = regexMatcher.group(2);

            if (value.startsWith("(") && value.endsWith(")")) {
                value = value.substring(1, value.length() - 1);
                List<String> values = StrUtils.splitSmart(value, ' ');
                for (int i = 0; i < values.size(); i++) {
                    value = values.get(i);
                    if (!value.startsWith(prefix) && ( !"id".equals(field) || (value.endsWith("@user") || value.endsWith("@group")))) {
                        value = prefix + value;
                        values.set(i, value);
                    }
                }
                value = "(" + StrUtils.join(values, ' ') + ")";
            } else {
                if (!value.startsWith(prefix) && ( !"id".equals(field) || (value.endsWith("@user") || value.endsWith("@group"))))
                    value = prefix + value;
            }


            regexMatcher.appendReplacement(resultString, regexMatcher.group(1)+":"+ value);
        }
        regexMatcher.appendTail(resultString);

        String res = resultString.toString();
        return res;
    }

    public static void removePrefix( SolrDocument doc ){
        String prefix = Session.getPrefix();
        String type = (String) doc.getFieldValue("type");

        //il prefisso potrebbe non dipendere dalla sessione
        if (Group.TYPE_ENTE.equals(type) || Group.TYPE_AOO.equals(type)){
            prefix = "";
            String GROUP_ID = (String) doc.getFieldValue("GROUP_ID");
            if (GROUP_ID!=null && GROUP_ID.contains("__"))
                prefix = GROUP_ID.split("__")[0];
                //prefix = (String) doc.getFieldValue("COD_ENTE");
        }

        if (Strings.isNullOrEmpty(prefix))
            return;

        if (Group.TYPE.equals(type) || User.TYPE.equals(type) || Group.TYPE_AOO.equals(type) || Group.TYPE_ENTE.equals(type))
            doc.setField("prefix",prefix);

        if (!prefix.endsWith("__"))
            prefix += "__";

        for( String field : prefixedValues ){
            String val = (String) doc.getFieldValue(field);
            if (val!=null && val.startsWith(prefix)){
                doc.setField(field, val.substring(prefix.length()) );
            }
        }

        for( String field : prefixedArrays ){
            Collection<String> vals = (Collection) doc.getFieldValues(field);
            if (vals!=null){
                List<String> newvals = new ArrayList<>();
                for( String val : vals ){
                    if (val!=null && val.startsWith(prefix)){
                        newvals.add(val.substring(prefix.length()));
                    } else {
                        newvals.add(val);
                    }
                }
                doc.setField(field, newvals);
            }
        }
    }

    public static void applyPrefix( Map<String, Object> map ){
        String prefix = Session.getPrefix();
        if (Strings.isNullOrEmpty(prefix))
            return;

        if (!prefix.endsWith("__"))
            prefix += "__";

        for( String field : prefixedValues ){
            String val = (String) map.get(field);
            if (val!=null && !val.startsWith(prefix)){
                map.put(field,prefix+val);
            }
        }
    }

    public static Map<String,String> toDocerMap(DocerBean bean){
        try {
            Map<String, Object> map = OM.readValue(OM.writeValueAsString(bean), Map.class);
            applyPrefix(map);

            Map<String, String> docerMap = new LinkedHashMap<>();
            docerMap.put("COD_ENTE", Session.getUserInfo().getCodEnte());
            docerMap.put("COD_AOO",Session.getUserInfo().getCodAoo());

            for( String key : map.keySet()){
                if (key.matches(".*[a-z].*"))
                    continue;

                Object v = map.get(key);

                if (v != null && v instanceof String[])
                    docerMap.put (key, StringUtils.join( (String[]) v, DocerBean.MV_SEP ));
                else if (v != null && v instanceof Object[])
                    docerMap.put (key, StringUtils.join( (Object[]) v, DocerBean.MV_SEP ));
                if (v != null && v instanceof Collection)
                    docerMap.put (key, StringUtils.join( (Collection) v, DocerBean.MV_SEP ));
                else if (v!=null)
                    docerMap.put (key, String.valueOf(map.get(key)) );
                //else if (v==null)
                //    docerMap.put (key, null );

                //if ((map.get(key) instanceof Boolean) )
                //    map.put(key, map.get(key).toString() );
            }

            return docerMap;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }



    public static String generateGUID(DocerBean bean){
        String base = bean.getDocerId();
        if (base==null){
            Map<String,String> map = ServerUtils.toDocerMap(bean);
            base = bean.getType()+map.hashCode();
        }

        return UUID.nameUUIDFromBytes(base.getBytes()).toString();
    }

    /*public static String generateGUID(Documento bean){

        String base = bean.getId();
        if (base==null){
            Map<String,String> map = ServerUtils.toDocerMap(bean);
            base = bean.getType()+map.hashCode();
        }

        return UUID.nameUUIDFromBytes(base.getBytes()).toString();
    }*/

    private static Map buildFascicolo(String fascicolId){
        final String[] splitted = ClientUtils.splitFascicoloId(fascicolId);

        if (splitted!=null) {

            final String cls = splitted[0];
            //if (!Strings.isNullOrEmpty(splitted[3]))
            //    cls = splitted[0] + "$"+splitted[3];

            return new LinkedHashMap(){{
                put("CodiceAmministrazione", Session.getUserInfo().getCodEnte());
                put("CodiceAOO", Session.getUserInfo().getCodAoo());
                put("Classifica", cls);
                put("Anno", "".equals(splitted[1]) ? "0" : splitted[1] );
                put("Progressivo", splitted[2]);
            }};
        } else {
            return null;
        }
    }

    public static String buildXMLProtocollazione(
            final String oggetto,
            final String verso,
            final ICorrispondente mittente,
            final ICorrispondente[] destinatari
            ){

        Map Segnatura = new LinkedHashMap(){{
            put( "Segnatura", new LinkedHashMap(){{
                put("Intestazione" , new LinkedHashMap(){{
                    put("Oggetto" , oggetto);
                    put("Flusso" , new LinkedHashMap(){{
                        put("TipoRichiesta" , verso );
                        put("Firma" , "NF" );
                        put("ForzaRegistrazione", "1");
                        //put("Firmataraio", ... );
                    }});

                    put("Mittenti", new LinkedHashMap() {{
                        if (mittente!=null) {
                            put("Mittente", mittente.toXmlMap());
                        }
                    }});

                    put("Destinatari", new ArrayList() {{
                        if (destinatari!=null) {
                            for (final ICorrispondente corr : destinatari)
                                add(new LinkedHashMap() {{
                                    put("Destinatario", corr.toXmlMap());
                                }});
                        }
                    }});
                }});
            }});
        }};

        String xml = U.toXml(Segnatura);

        xml = xml.replaceAll("</Destinatari>\\s*<Destinatari>","");

        return cleanUXml(xml);
    }

    public static String buildXMLRegistrazione(
            final String oggetto
    ){

        Map Segnatura = new LinkedHashMap(){{
            put( "Segnatura", new LinkedHashMap(){{
                put("Intestazione" , new LinkedHashMap(){{
                    put("Oggetto" , oggetto);
                    put("Flusso" , new LinkedHashMap(){{
                        put("TipoRichiesta" , "I" );
                        put("Firma" , "NF" );
                        put("ForzaRegistrazione", "1");
                        //put("Firmataraio", ... );
                    }});
                    /*put("Mittenti", new LinkedHashMap(){{
                    }});
                    put("Destinatari", new ArrayList() {{
                    }});*/
                }});
            }});
        }};

        String xml = U.toXml(Segnatura);

        return cleanUXml(xml);
    }

    public static String cleanUXml(String xml){

        xml = xml.replaceAll("<\\?xml[^>]+>\\n?","");
        xml = xml.replaceAll("(string|empty-array|array|null)\\s*=\\s*\"[^\"]+\"","");

        return xml;
    }

    public static String buildXMLFascicolazione(final String primario, final String[] secondari){

        LinkedHashMap Segnatura = new LinkedHashMap(){{
            put("Segnatura", new LinkedHashMap(){{
                put("Intestazione" , new LinkedHashMap(){{
                    if (primario!=null)
                        put("FascicoloPrimario" , buildFascicolo(primario));

                    if (secondari!=null){
                        put("FascicoliSecondari", new ArrayList() {{
                            for (final String sec : secondari){
                                add( new LinkedHashMap() {{
                                    put("FascicoloSecondario", buildFascicolo(sec));
                                }});
                            }
                        }});
                    }
                }});
            }});
        }};

        String xml = U.toXml(Segnatura);

        xml = xml.replaceAll("</FascicoliSecondari>\\s*<FascicoliSecondari>","");

        return cleanUXml(xml);
    }

    public static Map<String,String> docerAclMap(Map<String,String> aclMap){
        if (aclMap==null)
            return null;
        Map<String,String> map = new LinkedHashMap<>();
        for( String acl : aclMap.keySet()) {
            String key = acl.split("@")[0];
            String right = aclMap.get(acl);
            String docerRight=null;
            if (right.equals(READ))
                docerRight = "2";
            else if (right.equals(NORMAL))
                docerRight = "1";
            else if (right.equals(FULL))
                docerRight = "0";

            map.put(key,docerRight);
        }
        return map;
    }

    public static Map<String,String> aclMap(String[] acls){
        Map<String,String> map = new LinkedHashMap<>();
        if (acls==null)
            return map;
        for( String acl : acls) {
            String[] parts = acl.split(":");

            String right = "*";

            if (parts.length>1)
                right = parts[1];

            String actor = parts[0];

            String prev = map.get(actor);

            if (FULL.equals(prev) || NORMAL.equals(prev) && READ.equals(right))
                continue;

            map.put(actor,right);
        }
        return map;
    }

    public static String[] aclStrings(Map<String,String> map){
        if (map==null)
            return null;
        List<String> acls = new ArrayList<>();
        for( String acl : map.keySet() )
            acls.add(acl + ":" + map.get(acl));
        return acls.toArray(new String[0]);
    }

    public static String[] resolveUpAcl(String[] acls){
        if (acls==null)
            return null;
        List<String> newAcls = new ArrayList<>();
        for( String acl : acls ) {
            if (acl.contains("^")){
                acl = acl.replace("^","");
                String[] parts = acl.split(":");
                String group = parts[0].split("@")[0];
                String prefix = "";

                if (group.startsWith("+") || group.startsWith("-")){
                    prefix = group.substring(0,1);
                    group = group.substring(1);
                }

                String role = "";
                String right = parts[1];

                /*String group_roles = System.getProperty(ServerUtils.RUOLI_GRUPPO);

                if (!Strings.isNullOrEmpty(group_roles) && StringUtils.endsWithAny(group, group_roles.split(","))) {
                    int idx = group.lastIndexOf(UP_SEP);
                    role = group.substring(idx);
                    group = group.substring(0,idx);
                }*/

                parts = group.split(UP_SEP);

                String suffix = role+"@group:"+right;

                group = prefix+parts[0];
                newAcls.add(group+suffix);

                for ( int i=1; i<parts.length; i++){
                    group += UP_SEP + parts[i];
                    newAcls.add(group+suffix);
                }

            } else {
                newAcls.add(acl);
            }
        }
        return aclStrings(aclMap(newAcls.toArray(new String[0])));
    }

    public static String[] diffAcl(String[] oldAcls, String[] newAcls){
        Map<String,String> oldMap = aclMap(oldAcls);
        Map<String,String> newMap = aclMap(newAcls);

        for ( String key : newMap.keySet() ){
            if (key.startsWith("-")){
                String right = newMap.get(key);
                key = key.substring(1);
                if (oldMap.containsKey(key) && (right.equals("*") || right.equals(oldMap.get(key))))
                    oldMap.remove(key);
            }
        }

        for ( String key : newMap.keySet() ){

            if (!key.startsWith("-")){

                String newRight = newMap.get(key);

                if (key.startsWith("+")){
                    key = key.substring(1);
                }

                String oldRight = oldMap.get(key);

                if (FULL.equals(oldRight) || NORMAL.equals(oldRight) && READ.equals(newRight))
                    continue;

                oldMap.put(key,newRight);
            }
        }

        return aclStrings(oldMap);
        //List<String> acls = new ArrayList<>();

        //for( String acl : oldMap.keySet() )
        //    acls.add(acl + ":" + oldMap.get(acl));

        //return acls.toArray(new String[0]);
    }
}
