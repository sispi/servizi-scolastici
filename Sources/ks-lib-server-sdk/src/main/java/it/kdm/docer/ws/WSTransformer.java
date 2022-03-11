package it.kdm.docer.ws;

import com.google.common.base.Strings;
import it.kdm.docer.businesslogic.BusinessLogic;
import it.kdm.docer.commons.TicketCipher;
import it.kdm.docer.commons.Utils;
import it.kdm.docer.sdk.EnumACLRights;
import it.kdm.docer.sdk.EnumSearchOrder;
import it.kdm.docer.sdk.classes.xsd.KeyValuePair;
import it.kdm.docer.sdk.classes.xsd.SearchItem;
import it.kdm.docer.sdk.interfaces.ISearchItem;
import it.kdm.docer.webservices.ObjectFactory;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.util.ReflectionUtils;

import javax.xml.bind.JAXBElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class WSTransformer {

    public static final String NULL_VALUE = null;

    public static final ObjectFactory wsFactory = new ObjectFactory();
    public static final it.kdm.docer.sdk.classes.xsd.ObjectFactory sdkfFactory = new it.kdm.docer.sdk.classes.xsd.ObjectFactory();

    public static final it.kdm.docer.core.authentication.ObjectFactory coreFactory = new it.kdm.docer.core.authentication.ObjectFactory();

    public static final it.kdm.docer.fascicolazione.ObjectFactory fascicolazioneFactory = new it.kdm.docer.fascicolazione.ObjectFactory();
    public static final it.kdm.docer.fascicolazione.xsd.ObjectFactory fascicolazioneXsdFactory = new it.kdm.docer.fascicolazione.xsd.ObjectFactory();

    public static final it.kdm.docer.protocollazione.ObjectFactory protocollazioneFactory = new it.kdm.docer.protocollazione.ObjectFactory();
    public static final it.kdm.docer.protocollazione.xsd.ObjectFactory protocollazioneXsdFactory = new it.kdm.docer.protocollazione.xsd.ObjectFactory();

    public static final it.kdm.docer.registrazione.ObjectFactory registrazioneFactory = new it.kdm.docer.registrazione.ObjectFactory();
    public static final it.kdm.docer.registrazione.xsd.ObjectFactory registrazioneXsdFactory = new it.kdm.docer.registrazione.xsd.ObjectFactory();

    public static final it.kdm.docer.wspec.ObjectFactory pecFactory = new it.kdm.docer.wspec.ObjectFactory();
    public static final it.kdm.docer.firma.ObjectFactory firmaFactory = new it.kdm.docer.firma.ObjectFactory();

    public static final org.apache.commons.collections.keyvalue.xsd.ObjectFactory authXsdFactory = new org.apache.commons.collections.keyvalue.xsd.ObjectFactory();

    final static Collection<String> prefixedValues = Arrays.asList("CREATOR","MODIFIER","GROUP_ID","ADMIN_GROUP_ID","PARENT_GROUP_ID","USER_ID");

    public static String getOnDemandTicket(String docTicketEncrypted){
        String ticket = "";

        String docTicket = new TicketCipher().decryptTicket(docTicketEncrypted);

        ticket = Utils.addTokenKey(ticket, "ente", Utils.extractTokenKeyNoExc(docTicket,"ente"));
        String aoo = Utils.extractTokenKeyNoExc(docTicket,"aoo");
        if (org.apache.logging.log4j.util.Strings.isNotEmpty(aoo))
            Utils.addTokenKey(ticket, "aoo", aoo);
        ticket = Utils.addTokenKey(ticket, "uid", Utils.extractTokenKeyNoExc(docTicket,"uid"));
        ticket = Utils.addTokenKey(ticket, "documentale", docTicketEncrypted);
        return ticket;
    }

    public static String addPrefix(String str){
        String prefix = ThreadContext.getContext().get("prefix");
        if (str!=null && !Strings.isNullOrEmpty(prefix))
            return prefix+str;
        else
            return str;
    }

    public static String removePrefix(String str){
        String prefix = ThreadContext.getContext().get("prefix");
        if (str!=null && !Strings.isNullOrEmpty(prefix) && str.startsWith(prefix))
            return str.substring(prefix.length());
        else
            return str;
    }

    public static List<String> toList(String[] array){
        List<String> list = new ArrayList<String>();
        if(array!=null){
            for(String key : array) {
                list.add(key);
            }
        }
        return list;
    }

    /* dalla bl in output al ws (rimozione prefissi) */

    public static List<SearchItem> toSearchItemArray(List<ISearchItem> items){
        Map<String, String> metadata = new HashMap<String, String>();

        List<SearchItem> results = new ArrayList<>();

        if(items!=null)
            for(ISearchItem item : items){

                it.kdm.docer.sdk.classes.KeyValuePair[] pairs = item.getMetadata();

                SearchItem result = new SearchItem();

                Map<String,String> map = new LinkedHashMap<>();

                for( it.kdm.docer.sdk.classes.KeyValuePair kvp : pairs ){
                    map.put(kvp.getKey(),kvp.getValue());
                }

                List<KeyValuePair> list = toPairs(map);

                result.getMetadata().addAll(list);

                results.add(result);
            }

        return results;
    }

    public static List<KeyValuePair> toArray2(Map<String, EnumACLRights> acls){

        Map<String,String> map = new LinkedHashMap<>();
        for ( String id: acls.keySet()){
            if(id == null || acls.get(id) == null)
                continue;
            int aclCode = acls.get(id).getCode();
            map.put(id, String.valueOf(aclCode));
        }

        return toPairs(map);
    }

    public static List<KeyValuePair> toArray1(Map<String, String> metadataMap){
        return toPairs(metadataMap);
    }

    public static List<KeyValuePair> toPairs(it.kdm.docer.sdk.interfaces.IKeyValuePair[] blPairs){

        //occorre rimuovere il prefisso
        String prefix = ThreadContext.getContext().get("prefix");

        List<KeyValuePair> metadata = new ArrayList<>();

        if(blPairs!=null)
            for(int i=0; i<blPairs.length; i++){
                it.kdm.docer.sdk.interfaces.IKeyValuePair blPair = blPairs[i];

                String value = blPair.getValue();
                String key = blPair.getKey();

                if (!Strings.isNullOrEmpty(prefix)){
                    if (value!=null && value.startsWith(prefix)) {
                        value = value.substring(prefix.length());
                    }
                    if (key!=null && key.startsWith(prefix)) {
                        key = key.substring(prefix.length());
                    }
                }

                KeyValuePair kvp = new KeyValuePair();
                JAXBElement<String> jValue = sdkfFactory.createKeyValuePairValue(value);
                JAXBElement<String> jKey = sdkfFactory.createKeyValuePairKey(key);
                kvp.setKey(jKey);
                kvp.setValue(jValue);
                metadata.add(kvp);
            }

        return metadata;

    }
    public static List<KeyValuePair> toPairs(Map<String,String> map){
        //occorre rimuovere il prefisso
        String prefix = ThreadContext.getContext().get("prefix");

        List<KeyValuePair> metadata = new ArrayList<>();

        if(map!=null)
            for(String key : map.keySet()){

                String value = map.get(key);

                if (!Strings.isNullOrEmpty(prefix)){
                    if (value!=null && value.startsWith(prefix)) {
                        value = value.substring(prefix.length());
                    }
                    if (key!=null && key.startsWith(prefix)) {
                        key = key.substring(prefix.length());
                    }
                }

                KeyValuePair kvp = new KeyValuePair();
                JAXBElement<String> jValue = sdkfFactory.createKeyValuePairValue(value);
                JAXBElement<String> jKey = sdkfFactory.createKeyValuePairKey(key);
                kvp.setKey(jKey);
                kvp.setValue(jValue);
                metadata.add(kvp);
            }

        return metadata;
    }

    /* dall'input del ws alla bl (aggiunta prefissi) */

    /* usato per ricevere una lista di elementi */
    public static List<Map<String, String>> toList1(SearchItem[] searchItemsArray){
        List<Map<String, String>> listMetadata = new ArrayList<Map<String, String>>();
        for (SearchItem item : searchItemsArray){
            Map<String, String> metadata = new HashMap<String, String>();

            if(item.getMetadata()!=null) {
                for (KeyValuePair kvp : item.getMetadata()) {
                    //if(kvp==null || kvp.getKey() == null || kvp.getValue()==null)
                    if (kvp == null || kvp.getKey() == null)
                        continue;

                    String value = NULL_VALUE;
                    if (kvp.getValue()!=null)
                        value = kvp.getValue().getValue();
                    //metadata.put(kvp.getKey().toUpperCase(), kvp.getValue());
                    metadata.put(kvp.getKey().getValue(), value);
                }
                listMetadata.add(metadata);
            }
        }
        return listMetadata;
    }



    public static it.kdm.docer.sdk.classes.KeyValuePair[] toKeyValuePairsArray(List<KeyValuePair> pairs, boolean prefixKeys){
        return toKeyValuePairs(pairs,prefixKeys).toArray(new it.kdm.docer.sdk.classes.KeyValuePair[0]);
    }

    public static it.kdm.docer.sdk.classes.KeyValuePair[] toKeyValuePairsArray(Map<String,String> metadata, boolean prefixKeys){
        return toKeyValuePairs(toPairs(metadata),prefixKeys).toArray(new it.kdm.docer.sdk.classes.KeyValuePair[0]);
    }

    public static List<it.kdm.docer.sdk.classes.KeyValuePair> toKeyValuePairs(List<KeyValuePair> pairs, boolean prefixKeys){
        //occorre aggiungere il prefisso
        String prefix = ThreadContext.getContext().get("prefix");

        List<it.kdm.docer.sdk.classes.KeyValuePair> metadata = new ArrayList<>();

        if(pairs!=null)
            for(KeyValuePair kvp : pairs){
                //if(kvp==null || kvp.getKey() == null || kvp.getValue()==null)
                if(kvp==null || kvp.getKey() == null)
                    continue;
                //metadata.put(kvp.getKey().toUpperCase(), kvp.getValue());
                String key = kvp.getKey().getValue();
                String value = NULL_VALUE;
                if (kvp.getValue()!=null)
                    value = kvp.getValue().getValue();

                if (!Strings.isNullOrEmpty(prefix)){
                    if (prefixKeys)
                        key = prefix+key;
                    if (prefixedValues.contains(key)) {
                        if (value!=null && value.length()>0)
                            value = prefix + value;
                    }
                }

                it.kdm.docer.sdk.classes.KeyValuePair kvp2 = new it.kdm.docer.sdk.classes.KeyValuePair();
                kvp2.setKey(key);
                kvp2.setValue(value);

                metadata.add(kvp2);
            }

        return metadata;
    }

    public static Map<String, EnumSearchOrder> toMap4(List<KeyValuePair> kvpArray) {

        Map<String,String> map = toMap(kvpArray,false);

        Map<String, EnumSearchOrder> orderby = null;

        if (kvpArray != null) {
            orderby = new LinkedHashMap<>();
            for (String key : map.keySet()) {
                orderby.put(key,"DESC".equalsIgnoreCase(map.get(key)) ? EnumSearchOrder.DESC:EnumSearchOrder.ASC);
            }
        }

        return orderby;

    }

    public static Map<String, EnumACLRights> toMap2(List<KeyValuePair> kvpArray){

        Map<String,String> map = toMap(kvpArray,true);
        Map<String, EnumACLRights> acls = new LinkedHashMap<>();

        if(kvpArray!=null)
            for(String actor : map.keySet()){
                acls.put(actor, BusinessLogic.getEnumACLRights(map.get(actor)));
            }

        return acls;
    }

    public static Map<String, String> toMap1(List<KeyValuePair> pairs){
        return toMap(pairs,false);
    }

    public static Map<String,String> toMap(List<KeyValuePair> pairs, boolean prefixKeys){
        //occorre aggiungere il prefisso
        String prefix = ThreadContext.getContext().get("prefix");

        Map<String, String> metadata = new HashMap<String, String>();

        if(pairs!=null)
            for(KeyValuePair kvp : pairs){
                //if(kvp==null || kvp.getKey() == null || kvp.getValue()==null)
                if(kvp==null || kvp.getKey() == null)
                    continue;
                //metadata.put(kvp.getKey().toUpperCase(), kvp.getValue());
                String key = kvp.getKey().getValue();
                String value = NULL_VALUE;
                if (kvp.getValue()!=null)
                    value = kvp.getValue().getValue();

                if (!Strings.isNullOrEmpty(prefix)){
                    if (prefixKeys)
                        key = prefix+key;
                    if (prefixedValues.contains(key)) {
                        if (value!=null && value.length()>0)
                            value = prefix + value;
                    }
                }

                metadata.put(key,value);
            }

        return metadata;
    }

    public static List removeNulls(List list){
        List nl = new ArrayList();
        for( Object val : list){
            if (val!=null)
                nl.add(val);
        }
        return nl;
    }

    /* usato per creare i criteri di ricerca */
    public static Map<String, List<String>> toMap3(List<KeyValuePair> kvpArray){

        //occorre aggiungere il prefisso
        String prefix = ThreadContext.getContext().get("prefix");

        Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

        if(kvpArray!=null) {

            for(KeyValuePair kvp : kvpArray) {

                //if(kvp==null || kvp.getKey() == null || kvp.getValue()==null)
                if(kvp==null || kvp.getKey() == null)
                    continue;

                String key = kvp.getKey().getValue().toUpperCase();

                if ( !searchCriteria.containsKey(key) )
                    searchCriteria.put(key, new ArrayList<String>());

                List<String> criteria = searchCriteria.get(key);

                String value = NULL_VALUE;

                if (kvp.getValue()!=null)
                    value = kvp.getValue().getValue();

                if (prefixedValues.contains(key)) {
                    if (value!=null && value.length()>0)
                        value = prefix + value;
                }

                criteria.add(value);

                //searchCriteria.put(kvp.getKey().toUpperCase(), criteria);
                searchCriteria.put(key, criteria);
            }
        }

        return searchCriteria;
    }

    public static <T> T getReturn(Class<T> cls,Object returnValue){
        try {

            Class factory = Class.forName(cls.getPackage().getName()+".ObjectFactory");
            Object oFact = factory.newInstance();

            Method createWithArg = ReflectionUtils.findMethod( factory, "create"+cls.getName(), returnValue.getClass());

            if (createWithArg!=null){
                return (T) createWithArg.invoke(oFact, returnValue);
            }

            Method createResponse = factory.getDeclaredMethod("create"+cls.getSimpleName());
            Method createReturn = ReflectionUtils.findMethod( factory, "create"+cls.getSimpleName() + "Return", returnValue.getClass());

            Object response = createResponse.invoke(oFact);

            if (createReturn!=null){
                Object ret = createReturn.invoke(oFact,returnValue);
                cls.getDeclaredMethod("setReturn",ret.getClass()).invoke(response,ret);
            } else {
                cls.getDeclaredMethod("setReturn",returnValue.getClass()).invoke(response,returnValue);
            }

            return (T) response;

        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException e ){
            throw new RuntimeException(e);
        } catch (InvocationTargetException | InstantiationException re ){
            throw new RuntimeException(re);
        }
    }
}
